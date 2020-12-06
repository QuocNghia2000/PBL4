package com.example.myapplication.activity;

import android.app.ActionBar;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.myapplication.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
     EditText edtuser,edtpass;
     Button btlogin,btregister;
    public static List<User> listuser;
    public static int UserID;
     String url_login="http://192.168.1.239:8888/PBL4//login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        handle();

    }
    private void init()
    {
        edtuser = findViewById(R.id.edt_user);
        edtpass= findViewById(R.id.edt_password);
        btlogin = findViewById(R.id.bt_login);
        btregister=findViewById(R.id.tv_register);
    }
    private void handle()
    {
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtuser.getText().toString().trim().isEmpty()||!edtpass.getText().toString().isEmpty())
                {
                    Check_user(url_login);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
//                if(edtuser.getText().toString().equals("admin")&&edtpass.getText().toString().equals("123"))
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
//                    builder.setIcon(R.drawable.ic_check);
//                    builder.setTitle("Login Successfully");
//                    builder.setMessage("Welcome!");
//                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
//                    AlertDialog alertDialog=builder.create();
//                    alertDialog.show();
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Invalid username & password",Toast.LENGTH_SHORT).show();
//                }
            }
        });
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
    private void Check_user(String url_login)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Ok"))
                        {
                            Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Invalid username & password",Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public int posCurrent(String name, String pass) {
        if(listuser!=null){
            for (int i = 0; i < listuser.size(); i++) {
                if (name.equals(listuser.get(i).getUsername()) && pass.equals(listuser.get(i).getPassword())) {
                    return i;
                }
            }
        }
        return -1;
    }
    public boolean checkInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}