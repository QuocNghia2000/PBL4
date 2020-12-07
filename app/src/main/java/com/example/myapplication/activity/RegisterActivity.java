package com.example.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText edtuser,edtpass,edtfullname;
    Button bt_register;
    String url_register ="http://192.168.1.239:8888/PBL4/Git_PBL4//register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        init();
        handle();

    }
    private void handle()
    {
        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtuser.getText().toString().trim().isEmpty()||!edtpass.getText().toString().isEmpty()||!edtfullname.getText().toString().isEmpty())
                {
                    register(url_register);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Nhập đủ thông tin trước khi đăng ký",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void init()
    {
        edtuser=findViewById(R.id.edt_register_username);
        edtfullname=findViewById(R.id.edt_register_fullname);
        edtpass=findViewById(R.id.edt_register_password);
        bt_register=findViewById(R.id.bt_register);
    }
    private void register(String url_register)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Ok"))
                        {
                            Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
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
                params.put("username",edtuser.getText().toString().trim());
                params.put("password",edtpass.getText().toString().trim());
                params.put("fullname",edtfullname.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}