package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddWorkActivity extends AppCompatActivity {
    public ArrayList<Work> listwork;
    private EditText name,detail;
    private TextView tittle;
    private ImageView img_confirm;
    private int idCurrentUser;
    DatePicker datePicker;
    TimePicker timePicker;
    String check;
    String URL="http://192.168.1.239:8888/PBL4";
    String url_add_work=URL+"/Git_PBL4/add_work.php";
    String url_update_work=URL+"/Git_PBL4/update_work.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_work);

        init();
        handle();
    }

    private void init()
    {
        Intent intent = getIntent();
        Work work = (Work) intent.getSerializableExtra("work");
        idCurrentUser = this.getIntent().getExtras().getInt("idCurrentUser");
        name = (EditText)findViewById(R.id.nameworkAC);
        detail = (EditText)findViewById(R.id.detailworkAC);
        img_confirm = (ImageView)findViewById(R.id.img_confirmAC);
        tittle = (TextView)findViewById((R.id.tittle));
        datePicker=findViewById(R.id.date_work);
        timePicker=findViewById(R.id.Time_work);
        check=this.getIntent().getExtras().getString("inf");
        //Toast.makeText(WorkActivity.this, check, Toast.LENGTH_SHORT).show();
        timePicker.setIs24HourView(true);
    }
    private void handle() {
        img_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_work(url_add_work);
                finish();
            }
        });
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
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
                params.put("UserID",String.valueOf(idCurrentUser));//UserID
                params.put("Namework",name.getText().toString());//Name_work
                params.put("Detail_work",detail.getText().toString());//Detail_work
                String time=datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth()+" "+timePicker.getCurrentHour()
                        +":"+timePicker.getCurrentMinute()+":00";
                params.put("Time",time);//Time
                params.put("status","0");//status
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    public void Add_work(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(!name.getText().toString().equals("")&&!detail.getText().toString().equals(""))
                       {
                           if(response.trim().equals("Oke"))
                           {
                               Toast.makeText(getApplicationContext(),"Thêm công việc thành công",Toast.LENGTH_SHORT).show();
                               finish();
                               //startActivity(new Intent(getApplicationContext(),MainActivity.class));
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
                params.put("UserID",String.valueOf(idCurrentUser));//UserID
                params.put("Namework",name.getText().toString());//Name_work
                params.put("Detail_work",detail.getText().toString());//Detail_work
                String time=datePicker.getYear()+"-"+datePicker.getMonth()+"-"+datePicker.getDayOfMonth()+" "+timePicker.getCurrentHour()
                        +":"+timePicker.getCurrentMinute()+":00";
                params.put("Time",time);//Time
                params.put("status","0");//status
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_back)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}