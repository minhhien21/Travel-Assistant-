package com.ygaps.travelapp.View;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralFragment extends Fragment {
    private String token;
    private TextView name;
    private TextView address;
    private TextView service;
    private TextView startDate;
    private TextView endDate;
    private TextView minCost;
    private TextView maxCost;
    private TextView averagePoint;
    private TextView countReview;
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private ProgressBar progressBar4;
    private ProgressBar progressBar5;
    private TextView countReview1;
    private TextView countReview2;
    private TextView countReview3;
    private TextView countReview4;
    private TextView countReview5;
    private RatingBar ratingBar;
    private EditText review;
    private Button submit;

    private StopPointInfos stopPointInfos;
    private String[] serviceTypeArray = {
            "Restaurant", "Hotel", "Rest Station", "Other"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_stop_point, container, false);

        SharedToken obj = new SharedToken(GeneralFragment.super.getActivity());
        token = obj.getToken( GeneralFragment.super.getActivity());

        name = view.findViewById(R.id.general_stop_point_name);
        address = view.findViewById(R.id.general_stop_point_address);
        service = view.findViewById(R.id.general_stop_point_service);
        startDate = view.findViewById(R.id.general_stop_point_startDate);
        endDate = view.findViewById(R.id.general_stop_point_endDate);
        minCost = view.findViewById(R.id.general_stop_point_minCost);
        maxCost = view.findViewById(R.id.general_stop_point_maxCost);
        averagePoint = view.findViewById(R.id.general_stop_point_average);
        countReview = view.findViewById(R.id.general_stop_point_count);
        progressBar1 = view.findViewById(R.id.general_stop_point_progressBar1);
        progressBar2 = view.findViewById(R.id.general_stop_point_progressBar2);
        progressBar3 = view.findViewById(R.id.general_stop_point_progressBar3);
        progressBar4 = view.findViewById(R.id.general_stop_point_progressBar4);
        progressBar5 = view.findViewById(R.id.general_stop_point_progressBar5);
        countReview1 = view.findViewById(R.id.general_stop_point_count1);
        countReview2 = view.findViewById(R.id.general_stop_point_count2);
        countReview3 = view.findViewById(R.id.general_stop_point_count3);
        countReview4 = view.findViewById(R.id.general_stop_point_count4);
        countReview5 = view.findViewById(R.id.general_stop_point_count5);
        ratingBar = view.findViewById(R.id.general_stop_point_rating);
        review = view.findViewById(R.id.general_stop_point_review);
        submit = view.findViewById(R.id.general_stop_point_submit);

        Bundle bundle = getArguments();
        String JSONPointInfo = bundle.getString("StopPointInfo");
        //Toast.makeText(GeneralFragment.super.getContext(), JSONPointInfo, Toast.LENGTH_LONG).show();
        Gson gson = new Gson();
        stopPointInfos = gson.fromJson(JSONPointInfo, StopPointInfos.class);

        name.setText(stopPointInfos.getName());
        address.setText(stopPointInfos.getAddress());
        service.setText(serviceTypeArray[stopPointInfos.getServiceTypeId() + 1]);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(String.valueOf(stopPointInfos.getArrivalAt()).equals("null")) {
            startDate.setText("null");
        } else {
            String dateS = simpleDateFormat.format(new Date(stopPointInfos.getArrivalAt()));
            startDate.setText(dateS);
        }
        if(String.valueOf(stopPointInfos.getLeaveAt()).equals("null")) {
            endDate.setText("null");
        } else {
            String dateE = simpleDateFormat.format(new Date(stopPointInfos.getLeaveAt()));
            endDate.setText(dateE);
        }
        minCost.setText(stopPointInfos.getMinCost());
        maxCost.setText(stopPointInfos.getMaxCost());
        getPointStart();
        addReview();
        return view;
    }
    private void getPointStart(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getFeedbackPointStart(token,stopPointInfos.getServiceId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 200){

                    try {
                        String body = response.body().string();
                        JSONObject pointData = new JSONObject(body);
                        JSONArray pointArray = pointData.getJSONArray("pointStats");
                        if(pointArray.length()>0){
                            float pointStart = 0;
                            int count = 0;
                            int count1 = 0;
                            int count2 = 0;
                            int count3 = 0;
                            int count4 = 0;
                            int count5 = 0;
                            for(int i=0;i<pointArray.length();i++)
                            {
                                JSONObject  jb = pointArray.getJSONObject(i);
                                int point = jb.getInt("point");
                                int total = jb.getInt("total");
                                if(point == 1)
                                    count1 = total;
                                else if(point == 2)
                                    count2 = total;
                                else if(point == 3)
                                    count3 = total;
                                else if(point == 4)
                                    count4 = total;
                                else if(point == 5)
                                    count5 = total;
                                count += total;
                                pointStart += point*total;
                            }
                            if(count == 0){
                                averagePoint.setText(String.valueOf(count1));
                                countReview1.setText(String.valueOf(count1));
                                countReview2.setText(String.valueOf(count2));
                                countReview3.setText(String.valueOf(count3));
                                countReview4.setText(String.valueOf(count4));
                                countReview5.setText(String.valueOf(count5));
                                progressBar1.setProgress(count1);
                                progressBar2.setProgress(count2);
                                progressBar3.setProgress(count3);
                                progressBar4.setProgress(count4);
                                progressBar5.setProgress(count5);
                            }
                            else{
                                pointStart /= count;
                                pointStart = (float) Math.round(pointStart * 10) / 10;
                                averagePoint.setText(String.valueOf(pointStart));
                                progressBar1.setMax(count);
                                progressBar2.setMax(count);
                                progressBar3.setMax(count);
                                progressBar4.setMax(count);
                                progressBar5.setMax(count);
                                progressBar1.setProgress(count1);
                                progressBar2.setProgress(count2);
                                progressBar3.setProgress(count3);
                                progressBar4.setProgress(count4);
                                progressBar5.setProgress(count5);
                                countReview1.setText(String.valueOf(count1));
                                countReview2.setText(String.valueOf(count2));
                                countReview3.setText(String.valueOf(count3));
                                countReview4.setText(String.valueOf(count4));
                                countReview5.setText(String.valueOf(count5));
                            }
                            countReview.setText(String.valueOf(count));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(GeneralFragment.super.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void addReview(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int point = Math.round(ratingBar.getRating());
                if(point == 0){
                    Toast.makeText(GeneralFragment.super.getContext(), "Please rate this service", Toast.LENGTH_LONG).show();
                    return;
                }
                String sReview= review.getText().toString();
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .sendReviewService(token,stopPointInfos.getServiceId(),sReview,point);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code() == 200){
                            Toast.makeText(GeneralFragment.super.getContext(), "Review sent", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(GeneralFragment.super.getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

    }
}
