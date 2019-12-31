package com.ygaps.travelapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.CommentInfo;
import com.ygaps.travelapp.Model.ReviewInfo;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ReviewAdapter extends BaseAdapter {
    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiIzNzMiLCJwaG9uZSI6IjA3ODc1NTkyNjciLCJlbWFpbCI6Im1pbmhoaWVuQGdtYWlsLmNvbSIsImV4cCI6MTU3ODA2NDUxNTk2OSwiYWNjb3VudCI6InVzZXIiLCJpYXQiOjE1NzU0NzI1MTV9.7_9E6aX5wOEbVNEQ5wcqWqAIcXGIfsLqZbzDzLFpqec";
    private Dialog dialog;
    Context context;
    LayoutInflater inflater;
    private ArrayList<ReviewInfo> CommentList ;
    public ReviewAdapter(Context context, ArrayList<ReviewInfo> listComment)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.CommentList = listComment;
    }
    static class ViewHolder
    {
        ImageView AvatarImgView;
        TextView NameTextView;
        TextView DateTextView;
        TextView ContentTextView;
        TextView PointTextView;
        ImageView ReportButton;
    }
    @Override
    public int getCount() {
        return CommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return CommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ReviewAdapter.ViewHolder holder;
        View rowView=inflater.inflate(R.layout.item_all_review_list, null, true);
        if(convertView == null)
        {
            final int index = position;
            final ReviewInfo commentInfo = this.CommentList.get(position);
            final ArrayList<ReviewInfo> CommentToReport = CommentList ;
            holder = new ReviewAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_all_review_list,null);
            holder.AvatarImgView = convertView.findViewById(R.id.AllReviewAvatar);
            holder.NameTextView = convertView.findViewById(R.id.AllReviewName);
            holder.ContentTextView = convertView.findViewById(R.id.AllReviewContent);
            holder.ReportButton = convertView.findViewById(R.id.ReportReviewClick);
            holder.DateTextView = convertView.findViewById(R.id.AllReviewDate);
            holder.PointTextView = convertView.findViewById(R.id.AllReviewPoint);
            convertView.setTag(holder);
        }
        else{
            holder = (ReviewAdapter.ViewHolder) convertView.getTag();
        }

        final ReviewInfo commentInfo = this.CommentList.get(position);
        holder.NameTextView.setText(commentInfo.getName());
        holder.ContentTextView.setText(commentInfo.getReview());
        holder.DateTextView.setText(MilisecondToDate(commentInfo.getCreatedOn()));
        holder.PointTextView.setText(commentInfo.getPoint()+"  points");
        holder.ReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int reviewId = commentInfo.getId();
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .reportReview(token,reviewId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Reported "+response.message(),Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        return convertView;
    }
    private String MilisecondToDate(String milisecond) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliSeconds = Long.parseLong(milisecond);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
