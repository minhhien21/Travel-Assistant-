package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ygaps.travelapp.Model.UserSearchResponse;
import com.ygaps.travelapp.R;

import java.util.List;

public class UserSearchAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<UserSearchResponse> ListUser ;
    public UserSearchAdapter(Context context, List<UserSearchResponse> ListUser)
    {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.ListUser = ListUser;
    }
    static class ViewHolder
    {
        TextView fullName;
        TextView email;
    }
    @Override
    public int getCount() {
        return ListUser.size();
    }

    @Override
    public Object getItem(int position) {
        return ListUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        UserSearchAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new UserSearchAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_user_search,null);
            holder.fullName = convertView.findViewById(R.id.item_add_member_name);
            holder.email = convertView.findViewById(R.id.item_add_member_email);
            convertView.setTag(holder);
        }
        else{
            holder = (UserSearchAdapter.ViewHolder) convertView.getTag();
        }
        holder.fullName.setText(ListUser.get(position).getFullName());
        holder.email.setText(ListUser.get(position).getEmail());
        return convertView;
    }
}
