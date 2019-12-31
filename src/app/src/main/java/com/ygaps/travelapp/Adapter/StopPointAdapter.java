package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Model.StopPointInfo;
import com.ygaps.travelapp.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class StopPointAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private ArrayList<StopPointInfo> stopPointList ;
    private String[] serviceTypes = {
            "Restaurant", "Hotel", "Rest Station", "Other"
    };
    public StopPointAdapter(Context context, ArrayList<StopPointInfo> listStopPoint)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stopPointList = listStopPoint;
    }
    static class ViewHolder
    {
        ImageView AvatarImgView;
        TextView NameTextView;
        TextView ArriveAtTextView;
        TextView ServiceTypeTextView;
        TextView MaxcostTextView;
        TextView MincostTextView;
        TextView LeaveAtTextView;
        TextView AddressTextView;
    }
    @Override
    public int getCount() {
        return stopPointList.size();
    }

    @Override
    public Object getItem(int position) {
        return stopPointList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        StopPointAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new StopPointAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_stoppoint_list,null);
            holder.AvatarImgView = convertView.findViewById(R.id.StopPointAvatar);
            holder.NameTextView = convertView.findViewById(R.id.StopPointName);
            holder.ServiceTypeTextView = convertView.findViewById(R.id.StopPointService);
            holder.AddressTextView =  convertView.findViewById(R.id.StopPointAddress);
            holder.ArriveAtTextView = convertView.findViewById(R.id.StopPointArriveAt);
            holder.MincostTextView = convertView.findViewById(R.id.StopPointMinCost);
            holder.MaxcostTextView = convertView.findViewById(R.id.StopPointMaxCost);
            holder.LeaveAtTextView = convertView.findViewById(R.id.StopPointLeaveAt);
            convertView.setTag(holder);
        }
        else{
            holder = (StopPointAdapter.ViewHolder) convertView.getTag();
        }

        StopPointInfo stopPointInfo = this.stopPointList.get(position);
        holder.NameTextView.setText(stopPointInfo.getmName());
        holder.ServiceTypeTextView.setText(serviceTypes[stopPointInfo.getmServiceTypeId() - 1]);
        holder.ArriveAtTextView.setText(MilisecondToDate(stopPointInfo.getmArrivalAt()));
        holder.LeaveAtTextView.setText(MilisecondToDate(stopPointInfo.getmLeaveAt()));
        holder.AddressTextView.setText(stopPointInfo.getmAddress());
        holder.MaxcostTextView.setText(stopPointInfo.getmMaxCost());
        holder.MincostTextView.setText(stopPointInfo.getmMincost());

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
