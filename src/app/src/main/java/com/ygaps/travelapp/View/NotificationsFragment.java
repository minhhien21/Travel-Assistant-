package com.ygaps.travelapp.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.ygaps.travelapp.Adapter.NotificationAdapter;
import com.ygaps.travelapp.Model.NotificationDetail;
import com.ygaps.travelapp.Model.NotificationListResponse;
import com.ygaps.travelapp.Model.SharedToken;
import com.ygaps.travelapp.Network.RetrofitClient;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private String token;
    private ArrayList<NotificationDetail> notificationDetails;
    private ListView notificationList;
    private NotificationAdapter notificationAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        SharedToken obj = new SharedToken(NotificationsFragment.super.getActivity());
        token = obj.getToken( NotificationsFragment.super.getActivity());
        notificationList = view.findViewById(R.id.NotiListView);
        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.RefreshNoti);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call<NotificationListResponse> call = RetrofitClient
                        .getInstance()
                        .getUserService()
                        .getInvitationList(token, 1,50);

                call.enqueue(new Callback<NotificationListResponse>() {
                    @Override
                    public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                        if (response.isSuccessful())
                        {
                            notificationDetails =  response.body().getNotificationDetailList();
                            notificationDetails = SortNotificationList(notificationDetails);
                            notificationAdapter = new NotificationAdapter(getContext(),notificationDetails);
                            notificationList.setAdapter(notificationAdapter);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationListResponse> call, Throwable t) {

                    }
                });
                Toast.makeText(getContext(), "Page refreshed", Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);
            }
        });

        Call<NotificationListResponse> call = RetrofitClient
                .getInstance()
                .getUserService()
                .getInvitationList(token, 1,50);

        call.enqueue(new Callback<NotificationListResponse>() {
            @Override
            public void onResponse(Call<NotificationListResponse> call, Response<NotificationListResponse> response) {
                if (response.isSuccessful())
                {
                    notificationDetails =  response.body().getNotificationDetailList();
                    notificationDetails = SortNotificationList(notificationDetails);
                    notificationAdapter = new NotificationAdapter(getContext(),notificationDetails);
                    notificationList.setAdapter(notificationAdapter);
                }
                else
                {
                    Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationListResponse> call, Throwable t) {

            }
        });



        return view;
    }
    private ArrayList<NotificationDetail> SortNotificationList(ArrayList <NotificationDetail > notificationDetails)
    {
        ArrayList<NotificationDetail> result = new ArrayList<>();
        while (notificationDetails.size() > 0 )
        {
            int lastIndex = notificationDetails.size() - 1;
            result.add(notificationDetails.get(lastIndex));
            notificationDetails.remove(lastIndex);
        }
        return result;
    }
}

