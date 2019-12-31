package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Model.CommentService;
import com.ygaps.travelapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CommentServiceAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<CommentService> CommentList ;
    public CommentServiceAdapter(Context context, List<CommentService> listComment)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.CommentList = listComment;
    }
    static class ViewHolder
    {
        ImageView AvatarImgView;
        TextView NameTextView;
        TextView PointTextView;
        TextView ContentTextView;
        TextView createOn;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        CommentServiceAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new CommentServiceAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_review_service_list,null);
            holder.AvatarImgView = convertView.findViewById(R.id.review_service_avatar);
            holder.NameTextView = convertView.findViewById(R.id.review_service_name);
            holder.PointTextView = convertView.findViewById(R.id.review_service_point);
            holder.ContentTextView = convertView.findViewById(R.id.review_service_feedback);
            holder.createOn = convertView.findViewById(R.id.review_service_createOn);
            convertView.setTag(holder);
        }
        else{
            holder = (CommentServiceAdapter.ViewHolder) convertView.getTag();
        }

        CommentService commentService = this.CommentList.get(position);
        holder.NameTextView.setText(commentService.getName());
        holder.PointTextView.setText(commentService.getPoint() + " points");
        holder.ContentTextView.setText(commentService.getFeedback());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(commentService.getCreatedOn().equals("null")){
            holder.createOn.setText("0");
        }else{
            long dateEnd = Long.parseLong(commentService.getCreatedOn());
            String dateReview = simpleDateFormat.format(new Date(dateEnd));
            holder.createOn.setText(dateReview);
        }
        return convertView;
    }
}
