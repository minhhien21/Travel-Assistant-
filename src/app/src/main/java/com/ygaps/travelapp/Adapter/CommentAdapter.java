package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Model.CommentInfo;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private ArrayList<CommentInfo> CommentList ;
    public CommentAdapter(Context context, ArrayList<CommentInfo> listComment)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.CommentList = listComment;
    }
    static class ViewHolder
    {
        ImageView AvatarImgView;
        TextView NameTextView;
        TextView ContentTextView;
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
        CommentAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new CommentAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_comment_list,null);
            holder.AvatarImgView = convertView.findViewById(R.id.CommentAvatar);
            holder.NameTextView = convertView.findViewById(R.id.CommentName);
            holder.ContentTextView = convertView.findViewById(R.id.CommentContent);
            convertView.setTag(holder);
        }
        else{
            holder = (CommentAdapter.ViewHolder) convertView.getTag();
        }

        CommentInfo commentInfo = this.CommentList.get(position);
        holder.NameTextView.setText(commentInfo.getmName());
        holder.ContentTextView.setText(commentInfo.getmComment());
        return convertView;
    }
}
