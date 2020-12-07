package com.example.myapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.model.User;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private ArrayList<User> userList;
    private Context context;
    LayoutInflater inflater;

    public ContactAdapter (ArrayList<User> userList, Context context)
    {
        this.context = context;
        this.userList = userList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = userList.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_contact, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.image = (ImageView) convertView.findViewById(R.id.avatar);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(user.getUsername());
        //holder.image.setImageResource(user.ge());
        holder.status.setText("offline");
        return convertView;
    }
    static class ViewHolder {
        TextView name;
        ImageView image;
        TextView status;
    }
}
