package com.ygaps.travelapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ygaps.travelapp.Adapter.CommentServiceAdapter;
import com.ygaps.travelapp.Model.CommentService;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {
    private String token;
    private StopPointInfos stopPointInfos;
    private List<CommentService> list = new ArrayList<CommentService>();
    private ListView listView;
    private CommentServiceAdapter adapter;
    private TextView countComment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews_stop_point, container, false);
        SharedToken obj = new SharedToken(ReviewsFragment.super.getActivity());
        token = obj.getToken( ReviewsFragment.super.getActivity());
        Bundle bundle = getArguments();
        String JSONPointInfo = bundle.getString("StopPointInfo");

        //Toast.makeText(ReviewsFragment.super.getContext(), JSONPointInfo, Toast.LENGTH_LONG).show();

        Gson gson = new Gson();
        stopPointInfos = gson.fromJson(JSONPointInfo, StopPointInfos.class);

        countComment = view.findViewById(R.id.review_stop_point_count);
        listView = view.findViewById(R.id.review_stop_point_listView);
        getReviewList();
        return view;
    }
    private void getReviewList(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getListFeedBack(token,stopPointInfos.getServiceId(),"1","50");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){
                    try {
                        String body = response.body().string();
                        JSONObject serviceData = new JSONObject(body);
                        JSONArray serviceArray = serviceData.getJSONArray("feedbackList");
                        if(serviceArray.length()>0){
                            for(int i=0;i<serviceArray.length();i++)
                            {
                                JSONObject  jb = serviceArray.getJSONObject(i);
                                list.add(new CommentService(jb.getInt("id"),jb.getString("name"),jb.getString("phone"),
                                        jb.getString("point"),jb.getString("feedback"),jb.getString("createdOn"),jb.getString("avatar")));
                            }
                            countComment.setText(serviceArray.length() + " reviews");
                            adapter = new CommentServiceAdapter(ReviewsFragment.super.getContext(),list);
                            listView.setAdapter(adapter);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(ReviewsFragment.super.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
