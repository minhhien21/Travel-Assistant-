package com.ygaps.travelapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.ygaps.travelapp.Adapter.ReviewAdapter;
import com.ygaps.travelapp.Model.AllReviewResponse;
import com.ygaps.travelapp.Model.CommentInfo;
import com.ygaps.travelapp.Model.ReviewInfo;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllReviewsTourActivity extends AppCompatActivity {
    private int tourId;
    private Dialog dialog;
    private ArrayList<ReviewInfo> _reviews;
    private ListView allReviewsList;
    private ReviewAdapter reviewAdapter;
    private String token;
    private NumberPicker npPageIndex;
    private  NumberPicker npPageSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reviews_tour);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedToken obj = new SharedToken(AllReviewsTourActivity.this);
        token = obj.getToken(AllReviewsTourActivity.this);
        Intent intent = getIntent();
        tourId = (int) intent.getSerializableExtra(HistoryFragment.EXTRA_MESSAGE);
        Call <AllReviewResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getReviewList(token,tourId, 1,50);
        call.enqueue(new Callback<AllReviewResponse>() {
            @Override
            public void onResponse(Call<AllReviewResponse> call, Response<AllReviewResponse> response) {
                if (response.isSuccessful())
                {
                    _reviews = response.body().getReviewList();
                    if (_reviews == null)
                    {
                        Toast.makeText(AllReviewsTourActivity.this, "This tour have no reviews", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        allReviewsList = findViewById(R.id.AllReviewListView);
                        reviewAdapter = new ReviewAdapter(getApplicationContext(),_reviews);
                        allReviewsList.setAdapter(reviewAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllReviewResponse> call, Throwable t) {

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void PageSettingReviewClick(View view) {
        dialog = new Dialog(AllReviewsTourActivity.this);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_page_settings);
        dialog.getWindow();

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

    public void ApplyPageSettingClick(View view) {
        final int pageIndex = npPageIndex.getValue();
        final int pageSize = npPageSize.getMaxValue();
        Call <AllReviewResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getReviewList(token,tourId, pageIndex,pageSize);
        call.enqueue(new Callback<AllReviewResponse>() {
            @Override
            public void onResponse(Call<AllReviewResponse> call, Response<AllReviewResponse> response) {
                if (response.isSuccessful())
                {
                    _reviews = response.body().getReviewList();
                    if (_reviews == null)
                    {
                        _reviews = new ArrayList<>();
                        _reviews.add(new ReviewInfo(0,"Tour Assistant","This tour have no review. You can not report this review",""));
                        allReviewsList = findViewById(R.id.AllReviewListView);
                        reviewAdapter = new ReviewAdapter(getApplicationContext(),_reviews);
                        allReviewsList.setAdapter(reviewAdapter);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Page is reloadded",Toast.LENGTH_LONG).show();
                        allReviewsList = findViewById(R.id.AllReviewListView);
                        reviewAdapter = new ReviewAdapter(getApplicationContext(),_reviews);
                        dialog.dismiss();
                        allReviewsList.setAdapter(reviewAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<AllReviewResponse> call, Throwable t) {

            }
        });
    }

    public void CancelPageSettingClick(View view) {
        dialog.dismiss();
    }
}
