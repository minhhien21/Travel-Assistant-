package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.SearchDestination;
import com.ygaps.travelapp.R;

import java.util.List;

public class SearchDestinationAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<SearchDestination> list;
    private String[] serviceTypeArray = {
            "Restaurant", "Hotel", "Rest Station", "Other"
    };

    public SearchDestinationAdapter(Context context, List<SearchDestination> list) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.list = list;
    }

    static class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView address;
        TextView service;
        TextView minPrice;
        TextView maxPrice;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_search_destination_list, null);
            holder.avatar = convertView.findViewById(R.id.explore_avatar_destination);
            holder.name = convertView.findViewById(R.id.explore_name);
            holder.address = convertView.findViewById(R.id.explore_address);
            holder.service = convertView.findViewById(R.id.explore_service);
            holder.minPrice = convertView.findViewById(R.id.explore_min_price);
            holder.maxPrice = convertView.findViewById(R.id.explore_max_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position).getAvatar() != "") {
            Picasso.get().load(list.get(position).getAvatar()).into(holder.avatar);
        }
        holder.name.setText(list.get(position).getName());
        holder.address.setText(list.get(position).getAddress());
        holder.service.setText(serviceTypeArray[list.get(position).getServiceTypeId() - 1]);
        if (list.get(position).getMinCost().equals("null")) {
            holder.minPrice.setText("0");
        } else {
            holder.minPrice.setText(list.get(position).getMinCost());
        }

        if (list.get(position).getMaxCost().equals("null")) {
            holder.maxPrice.setText("0");
        } else {
            holder.maxPrice.setText(list.get(position).getMaxCost());
        }
        return convertView;
    }
}
