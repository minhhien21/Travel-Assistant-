package com.ygaps.travelapp.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Adapter.CommentAdapter;
import com.ygaps.travelapp.Model.CommentResponse;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.TourInformations;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.Adapter.TourAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends Fragment {
    public static final String EXTRA_MESSAGE = "com.example.travelassistant.Extra.Object";
    private String token;
    private List<TourInformations> listTour = new ArrayList<TourInformations>();
    private ListView listView;
    private SearchView searchView;
    private TourAdapter tourAdapter;
    private Dialog dialog;
    private NumberPicker npPageIndex;
    private NumberPicker npPageSize;
    private TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedToken obj = new SharedToken(HomeFragment.super.getActivity());
        token = obj.getToken( HomeFragment.super.getActivity());
        listView = view.findViewById(R.id.listView);
        textView = view.findViewById(R.id.count_trip);
        searchView = view.findViewById(R.id.home_search);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.super.getActivity(),CreateTour.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int sentId = listTour.get(i).getId();
                Intent intent = new Intent(getApplicationContext(),PublicTourDetail.class);
                intent.putExtra(HomeFragment.EXTRA_MESSAGE,sentId);
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

                tourAdapter.getFilter(s);
                return false;
            }
        });
        Map<String, String> params = new HashMap<>();
        params.put("rowPerPage","200");
        params.put("pageNum","1");

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getListTour(token,params);
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
                                    listTour.add(new TourInformations(jb.getInt("id"), jb.getInt("status"), jb.getString("name"), jb.getString("minCost"),
                                            jb.getString("maxCost"), jb.getString("startDate"), jb.getString("endDate"), jb.getString("adults"),
                                            jb.getString("childs"), jb.getString("isPrivate"), jb.getString("avatar")));
                                }
                            }
                            textView.setText(listTour.size() + " trips");
                            tourAdapter = new TourAdapter(getContext(),listTour);
                            listView.setAdapter(tourAdapter);

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
