package com.ygaps.travelapp.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.NotificationDetail;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;
import com.ygaps.travelapp.View.AllReviewsTourActivity;
import com.ygaps.travelapp.View.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationAdapter extends BaseAdapter {

    private  String token = LoginActivity.TOKEN;
    private Dialog dialog;
    Context context;
    LayoutInflater inflater;
    private ArrayList<NotificationDetail> CommentList ;
    public NotificationAdapter(Context context, ArrayList<NotificationDetail> listComment)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.CommentList = listComment;
    }
    static class ViewHolder
    {
        TextView NameTextView;
        TextView DateTextView;
        TextView TourNameTextView;
        Button AcceptButton;
        Button DeclineButton;

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
        NotificationAdapter.ViewHolder holder;
        View rowView=inflater.inflate(R.layout.item_notifications, null, true);
        if(convertView == null)
        {
            final int index = position;
            final NotificationDetail commentInfo = this.CommentList.get(position);
            final ArrayList<NotificationDetail> CommentToReport = CommentList ;
            holder = new NotificationAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_notifications,null);
            holder.NameTextView = convertView.findViewById(R.id.InviteName);
            holder.DateTextView = convertView.findViewById(R.id.InviteDate);
            holder.TourNameTextView = convertView.findViewById(R.id.InviteTourName);
            holder.AcceptButton = convertView.findViewById(R.id.InviteAccept);
            holder.DeclineButton = convertView.findViewById(R.id.InviteDecline);
            convertView.setTag(holder);
        }
        else{
            holder = (NotificationAdapter.ViewHolder) convertView.getTag();
        }

        final NotificationDetail commentInfo = this.CommentList.get(position);
        holder.NameTextView.setText(commentInfo.getHostName());
        holder.DateTextView.setText(MilisecondToDate(commentInfo.getCreatedOn()));
        holder.TourNameTextView.setText(commentInfo.getName());
        holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tourId = commentInfo.getTourId();
            }
        });
        holder.DeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .acceptTourInvitation(token,commentInfo.getTourId(), false);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Dclined ",Toast.LENGTH_LONG).show();
                            CommentList.remove(commentInfo);
                            notifyDataSetChanged();
                        }
                        else {

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                CommentList.remove(commentInfo);
                notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Declined ",Toast.LENGTH_LONG).show();

            }
        });

        holder.AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .acceptTourInvitation(token,commentInfo.getTourId(), true);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),"Accepted ",Toast.LENGTH_LONG).show();
                            CommentList.remove(commentInfo);
                            notifyDataSetChanged();
                        }
                        else {

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
