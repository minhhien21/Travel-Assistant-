package com.ygaps.travelapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Adapter.TourAdapter;
import com.ygaps.travelapp.Model.MyTourInformations;
import com.ygaps.travelapp.Adapter.MyTourAdapter;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HistoryFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "com.example.travelassistant.Extra.Object";
    private List<MyTourInformations> MylistTour = new ArrayList<MyTourInformations>();
    private ListView listView;
    private SearchView searchView;
    private MyTourAdapter myTourAdapter;
    private String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        SharedToken obj = new SharedToken(HistoryFragment.super.getActivity());
        token = obj.getToken( HistoryFragment.super.getActivity());
        listView = view.findViewById(R.id.my_tour_listView);
        final TextView textView = view.findViewById(R.id.my_tour_count_trip);
        searchView = view.findViewById(R.id.history_search);
        Map<String, Number> params = new HashMap<>();
        params.put("pageIndex",1);
        params.put("pageSize",100);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int hostId = Integer.parseInt(MylistTour.get(position).getHostId());
                int sentId = MylistTour.get(position).getId();
                Intent intent = new Intent(getApplicationContext(),TourDetailActivity.class);
                intent.putExtra(EXTRA_MESSAGE,sentId);
                intent.putExtra("hostId",hostId);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myTourAdapter.getFilter(s);
                return false;
            }
        });

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getListTourHistory(token,params);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if(code == 200) {
                    try {
                        String bodyListTour = response.body().string();
                        JSONObject tourData = new JSONObject(bodyListTour);
                        JSONArray tourArray = tourData.getJSONArray("tours");
                        if(tourArray.length()>0){

                            for(int i=0;i<tourArray.length();i++)
                            {
                                JSONObject  jb = tourArray.getJSONObject(i);
                                if(jb.getInt( "status" )!=-1) {
                                    MylistTour.add(new MyTourInformations(jb.getInt("id"), jb.getString("hostId"), jb.getInt("status"), jb.getString("name"),
                                            jb.getString("minCost"), jb.getString("maxCost"), jb.getString("startDate"), jb.getString("endDate"),
                                            jb.getString("adults"), jb.getString("childs"), jb.getString("avatar"), jb.getBoolean("isHost"),
                                            jb.getBoolean("isKicked")));
                                }
                            }
                            textView.setText(MylistTour.size() + " trips");
                            myTourAdapter = new MyTourAdapter(getContext(),MylistTour);
                            listView.setAdapter(myTourAdapter);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
        return view;
    }
}
