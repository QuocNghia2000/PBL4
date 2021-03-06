package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activity.ChatActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> sms;
    private List<Icons> iconList;
    private ChatActivity mContext;
    private int ToUserID,UserID;
    private LayoutInflater mLayoutInflater;
//    public static boolean isMe;

    public ChatAdapter(ChatActivity context, List<Message> sms) {
        this.mContext = context;
        this.sms = sms;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public ChatAdapter(ChatActivity context, List<Message> sms,int ToUserID,int UserID) {
        this.mContext = context;
        this.sms = sms;
        this.ToUserID = ToUserID;
        this.UserID = UserID;
        this.mLayoutInflater = LayoutInflater.from(context);
        iconList = new ArrayList<Icons>();
        getIcons();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = null;
        itemView = mLayoutInflater.inflate(R.layout.item_sms, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {

        final Message message = sms.get(position);
        if (message != null) {
            viewHolder.imvSend.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    viewHolder.delete.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            viewHolder.tvSend.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    viewHolder.delete.setVisibility(View.VISIBLE);
                    return false;
                }
            });
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Confirm_delete(message.MessageID);
                }
            });
            if (message.getUserID() == UserID && message.getToUserID() == ToUserID && message.IsImage() != 1) {
                viewHolder.tvSend.setText(message.getText());
                for (ChatAdapter.Icons value : iconList) {
                    String st = sms.get(position).Text;
                    int index = st.indexOf(value.sign);
                    while (index != -1) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(viewHolder.tvSend.getText());
                        Drawable d = this.mContext.getResources().getDrawable(value.id);
                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                        builder.setSpan(new ImageSpan(d), index, index + value.sign.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tvSend.setText(builder);
                        index = st.indexOf(value.sign, index + value.sign.length());
                    }
                }
                viewHolder.tvSend.setVisibility(View.VISIBLE);
                viewHolder.delete.setVisibility(View.INVISIBLE);
                viewHolder.tvReceive.setVisibility(View.INVISIBLE);
                viewHolder.imvReceive.setVisibility(View.GONE);
                viewHolder.imvSend.setVisibility(View.GONE);
            } else if (message.getUserID() == ToUserID && message.getToUserID() == UserID && message.IsImage() != 1) {
                viewHolder.tvReceive.setText(message.getText());
                for (ChatAdapter.Icons value : iconList) {
                    String st = sms.get(position).Text;
                    int index = st.indexOf(value.sign);
                    while (index != -1) {
                        SpannableStringBuilder builder = new SpannableStringBuilder(viewHolder.tvReceive.getText());
                        Drawable d = this.mContext.getResources().getDrawable(value.id);
                        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                        builder.setSpan(new ImageSpan(d), index, index + value.sign.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        viewHolder.tvReceive.setText(builder);
                        index = st.indexOf(value.sign, index + value.sign.length());
                    }
                }
                viewHolder.tvSend.setVisibility(View.INVISIBLE);
                viewHolder.delete.setVisibility(View.INVISIBLE);
                viewHolder.tvReceive.setVisibility(View.VISIBLE);
                viewHolder.imvReceive.setVisibility(View.GONE);
                viewHolder.imvSend.setVisibility(View.GONE);
            } else if (message.getToUserID() == ToUserID && message.getUserID() == UserID && message.IsImage() == 1) {
                viewHolder.imvSend.setImageBitmap(message.getImage());
                viewHolder.delete.setVisibility(View.INVISIBLE);
                viewHolder.imvSend.setVisibility(View.VISIBLE);
                viewHolder.tvSend.setVisibility(View.INVISIBLE);
                viewHolder.tvReceive.setVisibility(View.INVISIBLE);
                viewHolder.imvReceive.setVisibility(View.INVISIBLE);
            } else if (message.getToUserID() == UserID && message.getUserID() == ToUserID && message.IsImage() == 1) {
                viewHolder.imvReceive.setImageBitmap(message.getImage());
                viewHolder.delete.setVisibility(View.INVISIBLE);
                viewHolder.imvReceive.setVisibility(View.VISIBLE);
                viewHolder.tvSend.setVisibility(View.INVISIBLE);
                viewHolder.tvReceive.setVisibility(View.INVISIBLE);
                viewHolder.imvSend.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.tvSend.setVisibility(View.INVISIBLE);
                viewHolder.delete.setVisibility(View.INVISIBLE);
                viewHolder.tvReceive.setVisibility(View.INVISIBLE);
                viewHolder.imvSend.setVisibility(View.INVISIBLE);
                viewHolder.imvReceive.setVisibility(View.INVISIBLE);
            }
        }
    }
    private  void Confirm_delete(final int ID_sms)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Bạn có muốn xoá tin nhắn này không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContext.Delete_Message(ID_sms);
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
    @Override
    public int getItemCount() {
        return sms.size();
    }

    public static  Bitmap ConvertByteToBitmap(String bytebitmap)
    {
        byte[] bytes=bytebitmap.getBytes();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSend;
        private TextView tvReceive;
        private ImageView imvSend;
        private ImageView imvReceive;
        private  ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvSend = (TextView) itemView.findViewById(R.id.sms_send);
            tvReceive = (TextView) itemView.findViewById(R.id.sms_receive);
            imvSend = (ImageView) itemView.findViewById(R.id.img_send);
            imvReceive = (ImageView) itemView.findViewById(R.id.img_recieve);
            delete=(ImageView) itemView.findViewById(R.id.delete_sms);
        }
    }

    public void getIcons()
    {
        iconList.add(new ChatAdapter.Icons("><",R.drawable.angry));
        iconList.add(new ChatAdapter.Icons("<>3",R.drawable.cry));
        iconList.add(new ChatAdapter.Icons("-_-",R.drawable.died));
        iconList.add(new ChatAdapter.Icons("@@",R.drawable.embarrass));
        iconList.add(new ChatAdapter.Icons(":D",R.drawable.happy));
        iconList.add(new ChatAdapter.Icons("<3",R.drawable.love));
        iconList.add(new ChatAdapter.Icons(":(",R.drawable.sad));
        iconList.add(new ChatAdapter.Icons(":)",R.drawable.shy));
        iconList.add(new ChatAdapter.Icons("-.-",R.drawable.sleep));
        iconList.add(new ChatAdapter.Icons("0.0",R.drawable.superise));
    }

    static class Icons{
        private String sign;
        private int id;

        public Icons(String sign,int id)
        {
            this.sign = sign;
            this.id = id;
        }
    }
}
