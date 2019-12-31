package com.ygaps.travelapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ygaps.travelapp.Adapter.CommentAdapter;
import com.ygaps.travelapp.Adapter.MemberAdapter;
import com.ygaps.travelapp.Adapter.StopPointAdapter;
import com.ygaps.travelapp.Model.CommentInfo;
import com.ygaps.travelapp.Model.CommentResponse;
import com.ygaps.travelapp.Model.MemberInfo;
import com.ygaps.travelapp.Model.PointStatInfo;
import com.ygaps.travelapp.Model.PointStatResponse;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfo;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Model.TourInfoResponse;
import com.ygaps.travelapp.Model.UserSearchResponse;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicTourDetail extends AppCompatActivity {
    private TourInfoResponse tourInfoResponse;
    private Dialog dialog;
    private int itemId;
    private int userId;
    private String token;
    private TextView nameText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView adultText;
    private TextView childText;
    private TextView minCostText;
    private TextView maxCostText;

    private RatingBar ratingBar;

    private Button dlgAddButton;
    private ListView commentListView;
    public ArrayList<CommentInfo> _commentInfos ;
    private CommentAdapter commentAdapter;
    private EditText dlgReview;

    public ArrayList<StopPointInfo> _stoppointInfos;
    private StopPointAdapter stopPointAdapter;
    private ListView stopPointListView;

    public ArrayList<MemberInfo> _memberInfos;
    private MemberAdapter memberAdapter;
    private ListView memberListView;

    private ArrayList<PointStatInfo> _pointStatInfos;
    private TextView averagePointText;

    private NumberPicker npPageIndex;
    private  NumberPicker npPageSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_tour_detail);
        SharedToken obj = new SharedToken(PublicTourDetail.this);
        token = obj.getToken( PublicTourDetail.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.RefreshTourDetail);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetPointStat();
                GetTourInfo();
                Toast.makeText(getApplicationContext(), "Page refreshed", Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
            }
        });
        Intent intent = getIntent();
        itemId = (int) intent.getSerializableExtra(HomeFragment.EXTRA_MESSAGE);
        //getUserInfo();
        nameText = findViewById(R.id.TourNameDetail);
        startDateText = findViewById(R.id.StartDate);
        endDateText = findViewById(R.id.EndDate);
        adultText = findViewById(R.id.AdultNumber);
        childText = findViewById(R.id.ChildrenNumber);
        minCostText = findViewById(R.id.MinCost);
        maxCostText = findViewById(R.id.MaxCost);

        stopPointListView = findViewById(R.id.StopPointListView);
        getStopPointInfo();
        commentListView = findViewById(R.id.CommentListView);
        memberListView = findViewById(R.id.MemberListView);
        GetPointStat();
        GetTourInfo();


    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getUserInfo(){
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getUserInfo(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                if(code == 200){
                    try {
                        String bodyUserInfo = response.body().string();
                        JSONObject userInfo = new JSONObject(bodyUserInfo);
                        userId = userInfo.getInt("id");
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
    }

    private void getStopPointInfo(){
        stopPointListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                StopPointInfos stopPointInfos = new StopPointInfos(_stoppointInfos.get(position).getmId(),_stoppointInfos.get(position).getServiceId(),
                        _stoppointInfos.get(position).getmName(),_stoppointInfos.get(position).getmAddress(),_stoppointInfos.get(position).getmProvinceId(),
                        Double.parseDouble(_stoppointInfos.get(position).getmLat()),Double.parseDouble(_stoppointInfos.get(position).getmLong()),
                        Long.parseLong(_stoppointInfos.get(position).getmArrivalAt()),Long.parseLong(_stoppointInfos.get(position).getmLeaveAt()),
                        _stoppointInfos.get(position).getmServiceTypeId(),_stoppointInfos.get(position).getmMincost(),_stoppointInfos.get(position).getmMaxCost());
                Bundle bundle = new Bundle();
                bundle.putString("StopPointInfo", new Gson().toJsonTree(stopPointInfos).getAsJsonObject().toString());
                bundle.putInt("tourId", itemId);
                StopPointDialogInTourDetail stopPointDialogInTourDetail = new StopPointDialogInTourDetail();
                stopPointDialogInTourDetail.setArguments(bundle);
                stopPointDialogInTourDetail.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                stopPointDialogInTourDetail.show(getSupportFragmentManager(), "Stop point dialog");
            }
        });
    }

    public void AddReviewClick(View view) {
        dialog = new Dialog(PublicTourDetail.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_send_review);
        dialog.getWindow();
        dialog.show();
        dlgAddButton = dialog.findViewById(R.id.send_review_button);
        dlgAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgReview = dialog.findViewById(R.id.InputReviewText);
                ratingBar = dialog.findViewById(R.id.RateThisTour);
                final int tourId = itemId;
                final int point =  Math.round(ratingBar.getRating()) ;
                final String reviewstr = dlgReview.getText().toString();
                SendReview(token,tourId,point,reviewstr);
                dialog.dismiss();
            }
        });


    }


    public void AllReviewClick(View view) {
        Intent intent = new Intent(getApplicationContext(), AllReviewsTourActivity.class);
        intent.putExtra(HomeFragment.EXTRA_MESSAGE,itemId);
        startActivity(intent);
    }

    private float CalculateAverageReviewPoint(ArrayList<PointStatInfo> poinStatList)
    {
        float totalReviewPoint = 0;
        int totalReviewTurn = 0;
        if (poinStatList.size() > 0) {
            for (int i = 0; i < poinStatList.size(); i++) {
                totalReviewPoint += poinStatList.get(i).getPoint() * poinStatList.get(i).getTotal();
                totalReviewTurn += poinStatList.get(i).getTotal();
            }
            if (totalReviewTurn == 0)
            {
                totalReviewTurn = 1;
            }
            totalReviewPoint = totalReviewPoint / totalReviewTurn;
            totalReviewPoint = (float) Math.round(totalReviewPoint * 10) / 10;
        }
        return totalReviewPoint;
    }
    private String MilisecondToDate(String milisecond) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliSeconds = Long.parseLong(milisecond);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

   }

    public void PageCommentSettingClick(View view) {
        dialog = new Dialog(PublicTourDetail.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_page_settings);
        dialog.getWindow();
        dialog.show();
        npPageIndex = dialog.findViewById(R.id.page_index_number);
        npPageIndex.setMinValue(1);
        npPageIndex.setMaxValue(10);
        npPageIndex.setWrapSelectorWheel(true);
        npPageSize = dialog.findViewById(R.id.page_size_number);
        npPageSize.setMinValue(1);
        npPageSize.setMaxValue(10);
        npPageSize.setWrapSelectorWheel(true);
        dialog.show();

    }
    public void CancelPageSettingClick(View view) {
        dialog.dismiss();
    }

    public void ApplyPageSettingClick(View view) {

        Call<CommentResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getCommentList(token,itemId, 1,2);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if(response.isSuccessful()) {
                    CommentResponse commentResponse = response.body();
                    _commentInfos = commentResponse.getCommentList();
                    if(_commentInfos.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "No comments", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
                        commentAdapter = new CommentAdapter(getApplicationContext(),_commentInfos);
                        commentListView.setAdapter(commentAdapter);
                    }

                }
                else
                {

                }

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {

            }
        });


    }

    public void CancelReview(View view) {
        dialog.dismiss();
    }

    public void SendCommentClick(View view) {
        dialog = new Dialog(PublicTourDetail.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_send_comment);
        dialog.getWindow();
        dialog.show();
        dlgAddButton = dialog.findViewById(R.id.send_review_button);
        dlgAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgReview = dialog.findViewById(R.id.InputReviewText);
                final int tourId = itemId;
                final String reviewstr = dlgReview.getText().toString();
                SendComment(token,tourId,String.valueOf(userId),reviewstr);
                dialog.dismiss();
            }
        });
    }

    private void SendComment(String token, int tourId,String userId,String reviewstr)
    {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .sendComment(token, tourId, userId, reviewstr);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Comment sent", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void SendReview(String token,int tourId, int point, String reviewstr)
    {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getUserService()
                .sendReviewTour(token, tourId, point, reviewstr);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Review sent", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private void GetPointStat() {
        Call<PointStatResponse> callPoint = RetrofitClient.getInstance()
                .getUserService()
                .getPointStatsReview(token, itemId);
        callPoint.enqueue(new Callback<PointStatResponse>() {
            @Override
            public void onResponse(Call<PointStatResponse> call, Response<PointStatResponse> response) {
                if(response.isSuccessful())
                {
                    _pointStatInfos  =   response.body().getPointStats();
                    float averageReviewPoint = CalculateAverageReviewPoint(_pointStatInfos);
                    ratingBar = findViewById(R.id.AverageStar);
                    ratingBar.setRating(averageReviewPoint);
                    averagePointText = findViewById(R.id.AveragePointReview);
                    averagePointText.setText("("+averageReviewPoint+")");
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<PointStatResponse> call, Throwable t) {

            }
        });
    }


    private void GetTourInfo() {
        Call<TourInfoResponse> call = RetrofitClient.getInstance()
                .getUserService()
                .getTourDetail(token, itemId);
        call.enqueue(new Callback<TourInfoResponse>() {
            @Override
            public void onResponse(Call<TourInfoResponse> call, Response<TourInfoResponse> response) {
                if (response.isSuccessful()) {
                    tourInfoResponse = response.body();
                    nameText.setText(tourInfoResponse.getmName());
                    if(tourInfoResponse.getmStartDate()==null) {
                        tourInfoResponse.setmStartDate("0");
                        startDateText.setText("0");
                    }
                    else if(tourInfoResponse.getmStartDate().equals("null")) {
                        tourInfoResponse.setmStartDate("0");
                        startDateText.setText("0");
                    }
                    else{
                        String stringDate = MilisecondToDate(tourInfoResponse.getmStartDate());
                        startDateText.setText(stringDate);
                    }
                    if(tourInfoResponse.getmEndDate() == null) {
                        tourInfoResponse.setmEndDate("0");
                        endDateText.setText("0");
                    }
                    else if(tourInfoResponse.getmEndDate().equals("null")) {
                        tourInfoResponse.setmEndDate("0");
                        startDateText.setText("0");
                    }
                    else{
                        String stringDate = MilisecondToDate(tourInfoResponse.getmEndDate());
                        endDateText.setText(stringDate);
                    }
                    minCostText.setText(tourInfoResponse.getmMinCost());
                    maxCostText.setText(tourInfoResponse.getmMaxCost());
                    adultText.setText(String.valueOf(tourInfoResponse.getmAdults()));
                    childText.setText(String.valueOf(tourInfoResponse.getmChilds()));
                    if (tourInfoResponse.getmCommentList().isEmpty()) {
                        //   Toast.makeText(getApplicationContext(), "This tour have no comment", Toast.LENGTH_LONG).show();
                        _commentInfos = new ArrayList<>();
                        _commentInfos.add(new CommentInfo(0, "Tour Assistant", "This tour have no comment", ""));
                        commentAdapter = new CommentAdapter(getApplicationContext(), _commentInfos);
                        commentListView.setAdapter(commentAdapter);

                    } else {
                        _commentInfos = new ArrayList<>(tourInfoResponse.getmCommentList());
                        commentAdapter = new CommentAdapter(getApplicationContext(), _commentInfos);
                        commentListView.setAdapter(commentAdapter);
                    }

                    if (tourInfoResponse.getmStopPointList().isEmpty()) {

                    } else {
                        _stoppointInfos = new ArrayList<>(tourInfoResponse.getmStopPointList());
                        stopPointAdapter = new StopPointAdapter(getApplicationContext(), _stoppointInfos);
                        stopPointListView.setAdapter(stopPointAdapter);
                    }

                    if (tourInfoResponse.getmMemberList().isEmpty()) {
                        //Toast.makeText(getApplicationContext(), "This tour have no member", Toast.LENGTH_LONG).show();

                    } else {
                        //Toast.makeText(getApplicationContext(), "This tour have many members", Toast.LENGTH_LONG).show();
                        _memberInfos = new ArrayList<>(tourInfoResponse.getmMemberList());
                        memberAdapter = new MemberAdapter(getApplicationContext(), _memberInfos);
                        memberListView.setAdapter(memberAdapter);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), " Khong thanh cong", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TourInfoResponse> call, Throwable t) {
            }
        });
    }
}
