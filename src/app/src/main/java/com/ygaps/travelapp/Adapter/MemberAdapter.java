package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Model.MemberInfo;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class MemberAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private ArrayList<MemberInfo> memberList ;
    public MemberAdapter(Context context, ArrayList<MemberInfo> listMember)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.memberList = listMember;
    }
    static class ViewHolder
    {
        ImageView AvatarImgView;
        TextView NameTextView;
        TextView  PhoneTextView;
        TextView HostTextView;
    }
    @Override
    public int getCount() {
        return memberList.size();
    }

    @Override
    public Object getItem(int position) {
        return memberList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MemberAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new MemberAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_member_list,null);
            holder.NameTextView = convertView.findViewById(R.id.MemberName);
            holder.PhoneTextView = convertView.findViewById(R.id.MemberPhone);
            convertView.setTag(holder);
        }
        else{
            holder = (MemberAdapter.ViewHolder) convertView.getTag();
        }

        MemberInfo memberInfo = this.memberList.get(position);
        holder.NameTextView.setText(memberInfo.getmName());
        holder.PhoneTextView.setText(memberInfo.getmPhone());
        return convertView;
    }
}
