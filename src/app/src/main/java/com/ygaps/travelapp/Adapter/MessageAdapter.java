package com.ygaps.travelapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.MessageSend;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageSend> {
    ArrayList<MessageSend> messages;

    public MessageAdapter(Context context, int resource, ArrayList<MessageSend> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        messages	=	objects;
    }
    public void add(MessageSend message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return messages.size();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MessageSend message = getItem( position );
        if (message.isBelongsToCurrentUser()) { //Nếu người là người đang sử dụng
            v = inflater.inflate( R.layout.my_message, null);
            TextView messageBody = (TextView) v.findViewById(R.id.message_body);
            messageBody.setText(message.getComment());
        }
        else {
            v = inflater.inflate( R.layout.my_friend_send_message, null);
            ImageView avatar = v.findViewById(R.id.avatar);
            TextView name =  v.findViewById(R.id.name_friend );
            TextView messageBody =  v.findViewById(R.id.message_body);

            if (!message.getAvatar().equals(""))
                Picasso.get().load(message.getAvatar()).into(avatar);
            name.setText(message.getName());
            messageBody.setText(message.getComment());
        }
        return v;
    }

}
