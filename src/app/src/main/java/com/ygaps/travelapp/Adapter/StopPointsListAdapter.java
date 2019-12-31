package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ygaps.travelapp.Model.StopPointInfos;
import com.ygaps.travelapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StopPointsListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<StopPointInfos> listStopPoint;
    private String[] serviceTypeArray = {
            "Restaurant", "Hotel", "Rest Station", "Other"
    };

    public StopPointsListAdapter(Context context, ArrayList<StopPointInfos> listStopPoint)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.listStopPoint = listStopPoint;
    }

    static class ViewHolder
    {
        TextView StopPointName;
        TextView StopPointAddr;
        TextView Service;
        TextView ArriveAt;
        TextView LeaveAt;
        TextView MinCost;
        TextView MaxCost;
    }

    @Override
    public int getCount() {
        return listStopPoint.size();
    }

    @Override
    public Object getItem(int position) {
        return listStopPoint.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StopPointsListAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new StopPointsListAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_stop_point,null);
            holder.StopPointName = convertView.findViewById(R.id.StopPoint_Name);
            holder.StopPointAddr = convertView.findViewById(R.id.StopPoint_Address);
            holder.Service = convertView.findViewById(R.id.StopPoint_Service);
            holder.ArriveAt = convertView.findViewById(R.id.StopPoint_ArriveAt);
            holder.LeaveAt = convertView.findViewById(R.id.StopPoint_LeaveAt);
            holder.MinCost = convertView.findViewById(R.id.StopPoint_MinCost);
            holder.MaxCost = convertView.findViewById(R.id.StopPoint_MaxCost);
            convertView.setTag(holder);
        }
        else{
            holder = (StopPointsListAdapter.ViewHolder) convertView.getTag();
        }

        StopPointInfos stopPointInfos = this.listStopPoint.get(position);
        holder.StopPointName.setText(stopPointInfos.getName());
        holder.StopPointAddr.setText(stopPointInfos.getAddress());
        holder.Service.setText(serviceTypeArray[stopPointInfos.getServiceTypeId() - 1]);
        holder.ArriveAt.setText(MilisecondToDate(String.valueOf(stopPointInfos.getArrivalAt())));
        holder.LeaveAt.setText(MilisecondToDate(String.valueOf(stopPointInfos.getLeaveAt())));
        holder.MinCost.setText(stopPointInfos.getMinCost());
        holder.MaxCost.setText(stopPointInfos.getMaxCost());
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
