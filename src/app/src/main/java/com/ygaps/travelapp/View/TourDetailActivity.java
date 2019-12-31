package com.ygaps.travelapp.View;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.ygaps.travelapp.Adapter.CommentAdapter;
import com.ygaps.travelapp.Adapter.MemberAdapter;
import com.ygaps.travelapp.Adapter.UserSearchAdapter;
import com.ygaps.travelapp.Model.CommentInfo;
import com.ygaps.travelapp.Model.CommentResponse;
import com.ygaps.travelapp.Model.MemberInfo;
import com.ygaps.travelapp.Model.PointStatResponse;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Model.StopPointInfo;
import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.Model.TourInfoResponse;
import com.ygaps.travelapp.Model.UserSearchResponse;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.Adapter.StopPointAdapter;
import com.ygaps.travelapp.Model.PointStatInfo;
import com.ygaps.travelapp.R;

import org.json.JSONArray;
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


public class TourDetailActivity extends AppCompatActivity implements UpdateStopPointInformationFragment.UpdateStopPointListener{
    private String token;
    private TourInfoResponse tourInfoResponse = new TourInfoResponse();
    private Dialog dialog;
    private int itemId;
    private int hostId;
    private int userId;

    private TextView nameText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView adultText;
    private TextView childText;
    private TextView minCostText;
    private TextView maxCostText;
    private Button addMember;
    private Button btnFollow;

    private FloatingActionButton update;
    private FloatingActionButton delete;
    private Button chooseY;
    private Button chooseN;

    private RatingBar ratingBar;

    private ImageButton dlgCancelButton;
    private EditText dlgSearchText;
    private TextView dlgNameTextView;
    private TextView dlgEmailTextView;
    private Button dlgAddButton;

    private ListView commentListView;
    public ArrayList<CommentInfo> _commentInfos;
    private CommentAdapter commentAdapter;
    private EditText dlgReview;

    public ArrayList<StopPointInfo> _stoppointInfos;
    private StopPointAdapter stopPointAdapter;
    private ListView stopPointListView;

    public ArrayList<MemberInfo> _memberInfos;
    private MemberAdapter memberAdapter;
    private ListView memberListView;

    private ListView userSearchListView;
    private List<UserSearchResponse> listUserSearch = new ArrayList<UserSearchResponse>();
    private int userSearchId = -1;
    private ArrayList<PointStatInfo> _pointStatInfos;
    private TextView averagePointText;
    private ActionBar toolbar;
    private NumberPicker npPageIndex;
    private NumberPicker npPageSize;

    private StopPointDialogInTourDetail stopPointDialogInTourDetail;
    private UpdateStopPointDialogInTourDetail updateStopPointDialogInTourDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);
        SharedToken obj = new SharedToken(TourDetailActivity.this);
        token = obj.getToken( TourDetailActivity.this);
        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);

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
        itemId = (int) intent.getSerializableExtra(HistoryFragment.EXTRA_MESSAGE);
        hostId = intent.getIntExtra("hostId",0);
        getUserInfo();
        nameText = findViewById(R.id.TourNameDetail);
        startDateText = findViewById(R.id.StartDate);
        endDateText = findViewById(R.id.EndDate);
        adultText = findViewById(R.id.AdultNumber);
        childText = findViewById(R.id.ChildrenNumber);
        minCostText = findViewById(R.id.MinCost);
        maxCostText = findViewById(R.id.MaxCost);
        update = findViewById(R.id.update);
        delete = findViewById(R.id.delete);
        addMember = findViewById(R.id.AddMember);
        btnFollow = findViewById(R.id.FollowTour);
        followTour();
        updateTour();
        deleteTour();
        AddMemberClick();
        stopPointListView = findViewById(R.id.StopPointListView);
        getStopPointInfo();
        commentListView = findViewById(R.id.CommentListView);
        memberListView = findViewById(R.id.MemberListView);
        GetPointStat();
        GetTourInfo();

    }

    private void followTour(){
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tourInfoResponse.getmStatus() == 2){
                    Toast.makeText(getApplicationContext(), "Tour is Closed", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(TourDetailActivity.this, FollowTour.class);
                    intent.putExtra("tourId", itemId);
                    intent.putExtra("userId", userId);
                    intent.putExtra("hostId", hostId);
                    startActivity(intent);
                }
            }
        });

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
                if(userId != hostId){
                    update.hide();
                    delete.hide();
                    addMember.setVisibility(View.INVISIBLE);
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
                if(userId != hostId) {
                    stopPointDialogInTourDetail = new StopPointDialogInTourDetail();
                    stopPointDialogInTourDetail.setArguments(bundle);
                    stopPointDialogInTourDetail.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    stopPointDialogInTourDetail.show(getSupportFragmentManager(), "Stop point dialog");
                }else{
                    updateStopPointDialogInTourDetail = new UpdateStopPointDialogInTourDetail();
                    updateStopPointDialogInTourDetail.setArguments(bundle);
                    updateStopPointDialogInTourDetail.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    updateStopPointDialogInTourDetail.show(getSupportFragmentManager(), "Stop point dialog");
                }
            }
        });
    }

    private void updateTour(){
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateTour.class);
                intent.putExtra(HistoryFragment.EXTRA_MESSAGE, itemId);
                startActivity(intent);
            }
        });
    }

    private void deleteTour(){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(TourDetailActivity.this);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.dialog_delete_tour);
                chooseY = dialog.findViewById(R.id.delete_tour_yes);
                chooseN = dialog.findViewById(R.id.delete_tour_no);
                chooseN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                chooseY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Call<ResponseBody> call = RetrofitClient
                                .getInstance()
                                .getUserService()
                                .deleteTour(token,String.valueOf(itemId),-1);
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.code() == 200){
                                    Toast.makeText(getApplicationContext(), "Delete successful", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(TourDetailActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }else{
                                    Toast.makeText(getApplicationContext(), "Some thing wrong!!!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                dialog.show();
            }
        });
    }

    private String MilisecondToDate(String milisecond) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliSeconds = Long.parseLong(milisecond);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());

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
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
    public void AddMemberClick() {
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tourInfoResponse.getmMemberList().size() == 0) {
                    Toast.makeText(getApplicationContext(), "Some thing wrong!!!", Toast.LENGTH_LONG).show();
                } else {
                    dialog = new Dialog(TourDetailActivity.this);
                    int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
                    int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_add_member);
                    dialog.getWindow().setLayout(width, height);
                    dlgCancelButton = dialog.findViewById(R.id.add_member_cancel_button);
                    dlgSearchText = dialog.findViewById(R.id.add_member_editText);
                    dlgEmailTextView = dialog.findViewById(R.id.add_member_email);
                    dlgAddButton = dialog.findViewById(R.id.add_member_add_button);
                    userSearchListView = dialog.findViewById(R.id.add_member_listView);
                    dlgNameTextView = dialog.findViewById(R.id.add_member_full_name);
                    dlgCancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    dlgSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                            String sDlgNameText = dlgSearchText.getText().toString();
                            if(sDlgNameText.isEmpty()){
                                dlgSearchText.setError("Tour name is empty");
                                dlgSearchText.requestFocus();
                                return false;
                            }else{
                                listUserSearch.clear();
                            }
                            Call<ResponseBody> call = RetrofitClient.getInstance()
                                    .getUserService()
                                    .searchUser(sDlgNameText, 1, 30);
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.code() == 200) {
                                        try {
                                            String bodyListUserSearch = response.body().string();
                                            Log.d("123", bodyListUserSearch);
                                            JSONObject userData = new JSONObject(bodyListUserSearch);
                                            JSONArray userArray = userData.getJSONArray("users");
                                            if (userArray.length() > 0) {
                                                for (int i = 0; i < userArray.length(); i++) {
                                                    JSONObject jb = userArray.getJSONObject(i);
                                                    listUserSearch.add(new UserSearchResponse(jb.getInt("id"), jb.getString("fullName"),
                                                            jb.getString("email"), jb.getString("phone"), jb.getString("gender"),
                                                            jb.getString("dob"), jb.getString("avatar"), jb.getString("typeLogin")));
                                                }

                                                UserSearchAdapter userAdapter = new UserSearchAdapter(getApplicationContext(), listUserSearch);
                                                userSearchListView.setAdapter(userAdapter);
                                                userSearchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                        userSearchId = listUserSearch.get(position).getId();
                                                        dlgNameTextView.setText(listUserSearch.get(position).getFullName());
                                                        dlgEmailTextView.setText(listUserSearch.get(position).getEmail());
                                                    }
                                                });

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
                            return false;
                        }
                    });
                    dlgAddButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (userSearchId != -1) {
                                Call<ResponseBody> call1 = RetrofitClient.getInstance()
                                        .getUserService()
                                        .addMemberToTour(token, String.valueOf(itemId), String.valueOf(userSearchId), tourInfoResponse.getmIsPrivate());
                                call1.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.code() == 200) {
                                            Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    public void AddReviewClick(View view) {
        dialog = new Dialog(TourDetailActivity.this);
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
                final int point = Math.round(ratingBar.getRating());
                final String reviewstr = dlgReview.getText().toString();
                SendReview(token, tourId, point, reviewstr);
                dialog.dismiss();
            }
        });
    }

    public void CancelReview(View view) {
        dialog.dismiss();
    }

    public void AllReviewClick(View view) {
        Intent intent = new Intent(getApplicationContext(), AllReviewsTourActivity.class);
        intent.putExtra(HistoryFragment.EXTRA_MESSAGE, itemId);
        startActivity(intent);
    }

    public void PageCommentSettingClick(View view) {
        dialog = new Dialog(TourDetailActivity.this);
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
                .getCommentList(token, itemId, 1, 2);
        call.enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                if (response.isSuccessful()) {
                    CommentResponse commentResponse = response.body();
                    _commentInfos = commentResponse.getCommentList();
                    if (_commentInfos.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No comments", Toast.LENGTH_SHORT).show();
                    } else {
                        commentAdapter = new CommentAdapter(getApplicationContext(), _commentInfos);
                        commentListView.setAdapter(commentAdapter);
                    }

                } else {

                }

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {

            }
        });

    }


    public void SendCommentClick(View view) {
        dialog = new Dialog(TourDetailActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_send_comment);
        dialog.getWindow();
        dialog.show();
        dlgAddButton = dialog.findViewById(R.id.send_review_button);
        dlgAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlgReview = dialog.findViewById(R.id.InputReviewText);
                final String reviewstr = dlgReview.getText().toString();
                SendComment(token,itemId,String.valueOf(userId),reviewstr);
                dialog.dismiss();
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

    public void applyItemList(String name, int serviceTypeId, String address, int provinceId, String minCost, String maxCost,String arriveAt,String leaveAt){
        GetTourInfo();
        if(userId != hostId) {
            stopPointDialogInTourDetail.dismiss();
        }else{
            updateStopPointDialogInTourDetail.dismiss();
        }
    }
}
