package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;
import com.ygaps.travelapp.Model.TourInformations;
import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TourAdapter extends BaseAdapter{
    Context context;
    LayoutInflater inflater;
    private List<TourInformations> ListTour = null;
    private ArrayList<TourInformations> arrayList ;
    public TourAdapter(Context context, List<TourInformations> listTour)
    {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.ListTour = listTour;
        this.arrayList = new ArrayList<TourInformations>();
        this.arrayList.addAll(ListTour);
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
        return ListTour.size();
    }

    @Override
    public Object getItem(int position) {
        return ListTour.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
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
            holder = (ViewHolder) convertView.getTag();
        }
        if(ListTour.get(position).getAvatar() != "") {
            Picasso.get().load(ListTour.get(position).getAvatar()).into(holder.avatar);
        }
        holder.name.setText(ListTour.get(position).getName());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        if(ListTour.get(position).getStartDate() ==null) {
            holder.startDate.setText("0");
        }
        else if(ListTour.get(position).getStartDate().equals("null")) {
            holder.startDate.setText("0");
        } else {
            long dateStart = Long.parseLong(ListTour.get(position).getStartDate());
            String dateS = simpleDateFormat.format(new Date(dateStart));
            holder.startDate.setText(dateS);
        }

        if(ListTour.get(position).getEndDate() == null){
            holder.endDate.setText("0");
        }
        else if(ListTour.get(position).getEndDate().equals("null")){
            holder.endDate.setText("0");
        }else{
            long dateEnd = Long.parseLong(ListTour.get(position).getEndDate());
            String dateE = simpleDateFormat.format(new Date(dateEnd));
            holder.endDate.setText(dateE);
        }

        if(ListTour.get(position).getAdults().equals("null")){
            holder.adults.setText("0");
        }else{
            holder.adults.setText(ListTour.get(position).getAdults());
        }

        if(ListTour.get(position).getMinCost().equals("null")){
            holder.minPrice.setText("0");
        }else{
            holder.minPrice.setText(ListTour.get(position).getMinCost());
        }

        if(ListTour.get(position).getMaxCost().equals("null")){
            holder.maxPrice.setText("0");
        }else{
            holder.maxPrice.setText(ListTour.get(position).getMaxCost());
        }
        return convertView;
    }
    public void getFilter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        ListTour.clear();
        if (charText.length()==0){
            ListTour.addAll(arrayList);
        }
        else {
            for (TourInformations item : arrayList){
                if (item.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    ListTour.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
