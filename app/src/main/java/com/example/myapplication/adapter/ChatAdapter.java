package com.example.myapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.ChatActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.User;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> sms;
    private Context mContext;
    private int ToUserID,UserID;
    private LayoutInflater mLayoutInflater;
//    public static boolean isMe;

    public ChatAdapter(Context context, List<Message> sms) {
        this.mContext = context;
        this.sms = sms;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public ChatAdapter(Context context, List<Message> sms,int ToUserID,int UserID) {
        this.mContext = context;
        this.sms = sms;
        this.ToUserID = ToUserID;
        this.UserID = UserID;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        itemView = mLayoutInflater.inflate(R.layout.item_sms, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Message message = sms.get(position);
        if (message != null) {
            if (message.getUserID()==UserID && message.getToUserID()==ToUserID ) {
                viewHolder.tvSend.setText(message.getText());
                viewHolder.tvSend.setVisibility(View.VISIBLE);
                viewHolder.tvReceive.setVisibility(View.GONE);
            } else if (message.getUserID()==ToUserID && message.getToUserID()==UserID ) {
                viewHolder.tvReceive.setText(message.getText());
                viewHolder.tvSend.setVisibility(View.GONE);
                viewHolder.tvReceive.setVisibility(View.VISIBLE);
            } else if (message.getUserID()==new LoginActivity().UserID && message.getToUserID()==new ChatActivity().ToUserID /*&& message.isPhoto()*/) {
                viewHolder.imvSend.setImageBitmap(convertStringToBitmap(message.getText()));
                viewHolder.imvSend.setVisibility(View.VISIBLE);
                viewHolder.imvReceive.setVisibility(View.GONE);
            } else if (message.getToUserID()==new LoginActivity().UserID && message.getUserID()==new ChatActivity().ToUserID /*&& message.isPhoto()*/) {
                viewHolder.imvReceive.setImageBitmap(convertStringToBitmap(message.getText()));
                viewHolder.imvSend.setVisibility(View.GONE);
                viewHolder.imvReceive.setVisibility(View.VISIBLE);

            } else{
                viewHolder.tvSend.setVisibility(View.GONE);
                viewHolder.tvReceive.setVisibility(View.GONE);
                viewHolder.imvSend.setVisibility(View.GONE);
                viewHolder.imvReceive.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return sms.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSend;
        private TextView tvReceive;
        private ImageView imvSend;
        private ImageView imvReceive;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSend = (TextView) itemView.findViewById(R.id.sms_send);
            tvReceive = (TextView) itemView.findViewById(R.id.sms_receive);
            imvSend = (ImageView) itemView.findViewById(R.id.img_send);
            imvReceive = (ImageView) itemView.findViewById(R.id.img_recieve);

        }
    }
    private Bitmap convertStringToBitmap(String s) {
        byte[] manghinh = Base64.decode(s, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(manghinh, 0, manghinh.length);
    }
}
