package com.example.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.Work;

import java.util.HashMap;
import java.util.Map;

public class UpdateWorkActivity extends AppCompatActivity {
    String url_update_work="http://192.168.1.239:8888/PBL4/Git_PBL4/update_work.php";
    private EditText name,detail;
    TimePicker timePicker;
    DatePicker datePicker;
    CheckBox cb_done;
    ImageView img_update;
    Work work;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_work);
        Intent intent = getIntent();
        work = (Work)intent.getExtras().getSerializable("work");
        init();
        handle();
    }

    private void handle() {
        img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_work();
                finish();
            }
        });
    }
    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("congviec","complete");
        setResult(RESULT_OK,data);
        super.finish();
    }

    private void init() {
        name = (EditText)findViewById(R.id.nameworkAC);
        detail = (EditText)findViewById(R.id.detailworkAC);
        timePicker=findViewById(R.id.Time_work);
        datePicker=findViewById(R.id.date_work);
        cb_done=findViewById(R.id.cb_done);
        img_update=findViewById(R.id.img_confirmAC);
        name.setText(work.getNamework());
        detail.setText(work.getDetail_work());
        int year=Integer.valueOf(work.getTime().substring(0,4));
        int month=Integer.valueOf(work.getTime().substring(5,7));
        int date=Integer.valueOf(work.getTime().substring(8,10));
        int Hour=Integer.valueOf(work.getTime().substring(11,13));
        int Minute=Integer.valueOf(work.getTime().substring(14,16));
        timePicker.setCurrentHour(Hour); timePicker.setCurrentMinute(Minute);
        datePicker.updateDate(year,month-1,date);
        if(work.isStatus()==1) cb_done.setChecked(true);
        else cb_done.setChecked(false);
        timePicker.setIs24HourView(true);
    }
    public void Update_work()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_update_work,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(!name.getText().toString().equals("")&&!detail.getText().toString().equals(""))
                        {
                            if(response.trim().equals("Oke"))
                            {
                                Toast.makeText(getApplicationContext(),"Cập nhật công việc thành công",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Nhập đủ đã",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params = new HashMap<>();
                params.put("IDwork",String.valueOf(work.getIDwork()));//IDwork
                params.put("Name_work",name.getText().toString().trim());//Name_work
                params.put("Detail_work",detail.getText().toString().trim());//Detail_work
                String time=datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth()+" "+timePicker.getCurrentHour()
                        +":"+timePicker.getCurrentMinute()+":00";
                params.put("Time",time);//Time
                if(cb_done.isChecked())
                {
                    params.put("status","1");//status
                }
                else params.put("status","0");//status
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
}