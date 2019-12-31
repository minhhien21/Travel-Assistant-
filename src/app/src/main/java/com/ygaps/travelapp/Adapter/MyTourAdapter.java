package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygaps.travelapp.Model.MyTourInformations;
import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.TourInformations;
import com.ygaps.travelapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyTourAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    private List<MyTourInformations> MyListTour = null;
    private ArrayList<MyTourInformations> arrayList ;
    public MyTourAdapter(Context context, List<MyTourInformations> listTour)
    {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.MyListTour = listTour;
        this.arrayList = new ArrayList<MyTourInformations>();
        this.arrayList.addAll(MyListTour);
    }
    static class ViewHolder
    {
        ImageView avatar;
        TextView name;
        TextView startDate;
        TextView endDate;
        TextView adults;
        TextView minPrice;
        TextView maxPrice;
    }
    @Override
    public int getCount() {
        return MyListTour.size();
    }

    @Override
    public Object getItem(int position) {
        return MyListTour.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MyTourAdapter.ViewHolder holder;
        if(convertView == null)
        {
            holder = new MyTourAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_tour,null);
            holder.avatar = convertView.findViewById(R.id.avatar_tour);
            holder.name = convertView.findViewById(R.id.place_tour);
            holder.startDate = convertView.findViewById(R.id.Start_calender_tour);
            holder.endDate = convertView.findViewById(R.id.End_calender_tour);
            holder.adults = convertView.findViewById(R.id.adults_tour);
            holder.minPrice = convertView.findViewById(R.id.Min_price_tour);
            holder.maxPrice = convertView.findViewById(R.id.Max_price_tour);
            convertView.setTag(holder);
        }
        else{
            holder = (MyTourAdapter.ViewHolder) convertView.getTag();
        }
        if(MyListTour.get(position).getAvatar() != "") {
            Picasso.get().load(MyListTour.get(position).getAvatar()).into(holder.avatar);
        }
        holder.name.setText(MyListTour.get(position).getName());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(MyListTour.get(position).getStartDate().equals("null")) {
            holder.startDate.setText("0");
        } else {
            long dateStart = Long.parseLong(MyListTour.get(position).getStartDate());
            String dateS = simpleDateFormat.format(new Date(dateStart));
            holder.startDate.setText(dateS);
        }

        if(MyListTour.get(position).getEndDate().equals("null")){
            holder.endDate.setText("0");
        }else{
            long dateEnd = Long.parseLong(MyListTour.get(position).getEndDate());
            String dateE = simpleDateFormat.format(new Date(dateEnd));
            holder.endDate.setText(dateE);
        }

        if(MyListTour.get(position).getAdults().equals("null")){
            holder.adults.setText("0");
        }else{
            holder.adults.setText(MyListTour.get(position).getAdults());
        }

        if(MyListTour.get(position).getMinCost().equals("null")){
            holder.minPrice.setText("0");
        }else{
            holder.minPrice.setText(MyListTour.get(position).getMinCost());
        }

        if(MyListTour.get(position).getMaxCost().equals("null")){
            holder.maxPrice.setText("0");
        }else{
            holder.maxPrice.setText(MyListTour.get(position).getMaxCost());
        }
        return convertView;
    }
    public void getFilter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        MyListTour.clear();
        if (charText.length()==0){
            MyListTour.addAll(arrayList);
        }
        else {
            for (MyTourInformations item : arrayList){
                if (item.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    MyListTour.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
