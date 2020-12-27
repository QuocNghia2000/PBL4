package com.example.myapplication.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activity.UpdateWorkActivity;
import com.example.myapplication.activity.WorkActivity;
import com.example.myapplication.model.Work;

import java.util.ArrayList;

public class WorkAdapter extends BaseAdapter {
    private ArrayList<Work> listWork ;
    private WorkActivity context;
    LayoutInflater inflater;

    public WorkAdapter(ArrayList<Work> listWork, WorkActivity context)
    {
        this.listWork = listWork;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return listWork.size();
    }

    @Override
    public Object getItem(int position) {
        return listWork.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Work work = listWork.get(position);
        final WorkAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_work, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_nameWork);
            holder.detail = (TextView) convertView.findViewById(R.id.tv_detailWork);
            holder.date = (TextView) convertView.findViewById(R.id.tv_dateWork);
            holder.img_compltete=(ImageView) convertView.findViewById(R.id.ic_complete);
            holder.img_deletework=(ImageView) convertView.findViewById(R.id.img_deletework);
            holder.img_update=(ImageView) convertView.findViewById(R.id.img_updatework);
            final View finalConvertView = convertView;
            holder.img_deletework.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Confirm_delete(work.getNamework(),work.getIDwork());
                }
            });
            holder.img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(context, UpdateWorkActivity.class);
                update.putExtra("work",work);
                int Request_code2=10;
                context.startActivityForResult(update,Request_code2);
            }
            });
            convertView.setTag(holder);
        } else {
            holder = (WorkAdapter.ViewHolder) convertView.getTag();
        }
        holder.name.setText(work.getNamework());
        holder.detail.setText(work.getDetail_work());
        holder.date.setText(work.getTime());
        if (work.isStatus()==1) holder.img_compltete.setVisibility(View.VISIBLE);//Toast.makeText(context, "123", Toast.LENGTH_SHORT).show(); //
        return convertView;
    }
    private  void Confirm_delete(String namework, final int IDwork)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có muốn xoá công việc "+namework+" không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.Delete_work(IDwork);
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

    static class ViewHolder{
        TextView name;
        TextView detail;
        TextView date;
        ImageView img_deletework,img_update,img_compltete;
    }
}
