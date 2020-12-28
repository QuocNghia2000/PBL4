package com.example.myapplication.adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.ChatActivity;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.model.User;

import java.util.ArrayList;

public class ContactAdapter extends BaseAdapter {
    private ArrayList<User> userList;
    private  ArrayList<Integer> user_online;
    private MainActivity context;
    LayoutInflater inflater;
    private int UserID;

    public ContactAdapter (ArrayList<User> userList,int UserID,ArrayList<Integer> user_online, Context context)
    {
        this.context = (MainActivity) context;
        this.userList = userList;
        this.user_online = user_online;
        this.UserID=UserID;
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
            holder.delete=(ImageView)convertView.findViewById(R.id.delete_UC);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_contact);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(user.getFullName());
        boolean check=false;
       for(int i=0;i<user_online.size();i++)
       {
           if(user.getUserID()==user_online.get(i))
           {
               check=true;
           }
       }
        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.delete.setVisibility(View.VISIBLE);
                return true;
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.image_delete.setVisibility(View.GONE);
                if (holder.delete.getVisibility() == View.GONE)
                {
                    Intent gotochat = new Intent(context, ChatActivity.class);
                    gotochat.putExtra("nameCurrent", user.fullName);
                    gotochat.putExtra("idUsername", user.userID);
                    gotochat.putExtra("idCurrentUser", UserID);
                    context.startActivity(gotochat);
                }
                else
                {
                    holder.delete.setVisibility(View.GONE);
                }

            }
        });

       if(check) holder.status.setText("online");
       else  holder.status.setText("offline");
//       holder.name.setOnLongClickListener(new View.OnLongClickListener() {
//           @Override
//           public boolean onLongClick(View v) {
//               holder.delete.setVisibility(View.VISIBLE);
//               return false;
//           }
//       });
       holder.delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Confirm_delete(UserID,user.getUserID());
           }
       });
        return convertView;
    }
    private  void Confirm_delete(final int UserID,final int ToUserID)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xoá tin nhắn này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.Delete_User_Contact(UserID,ToUserID);
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    static class ViewHolder {
        TextView name;
        ImageView image;
        TextView status;
        ImageView delete;
        LinearLayout linearLayout;
    }
}
