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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
     EditText edtuser,edtpass;
     Button btlogin,btregister;
    public static ArrayList<User> listuser;
    public static int UserID;
    String url="http://192.168.1.239:8888/PBL4/Git_PBL4/infor_ToUserID_Mess.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        GetData(url);
        handle();
    }
    private void init()
    {
        edtuser = findViewById(R.id.edt_user);
        edtpass= findViewById(R.id.edt_password);
        btlogin = findViewById(R.id.bt_login);
        btregister=findViewById(R.id.tv_register);
        listuser=new ArrayList<>();
    }
    public  void GetData(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray= response.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listuser.add(new User(
                                        jsonObject.getInt("UserID"),
                                        jsonObject.getString("Username"),
                                        jsonObject.getString("Password"),
                                        jsonObject.getString("Fullname")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //chatAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    private void handle()
    {
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtuser.getText().toString().trim().isEmpty()||!edtpass.getText().toString().trim().isEmpty())
                {
                        if(posCurrent(edtuser.getText().toString(),edtpass.getText().toString())!=-1)
                        {
                            int index = posCurrent(edtuser.getText().toString(),edtpass.getText().toString());
                            Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                            gotoMain.putExtra("idCurrentUser",listuser.get(index).userID);
                            startActivity(gotoMain);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Sai thông tin đăng nhập",Toast.LENGTH_SHORT).show();
                        }
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
    public int posCurrent(String name, String pass) {
        if(listuser!=null){
            for (int i = 0; i < listuser.size(); i++) {
                if (name.equals(listuser.get(i).getUsername().trim()) && pass.equals(listuser.get(i).getPassword().trim())) {
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