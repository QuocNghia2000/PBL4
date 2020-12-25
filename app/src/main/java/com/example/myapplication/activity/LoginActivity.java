package com.example.myapplication.activity;

import android.app.ActionBar;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

import static java.util.jar.Pack200.Packer.PASS;

public class LoginActivity extends AppCompatActivity {
     EditText edtuser,edtpass;
     Button btlogin,btregister;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";
    CheckBox cbRemember;
    SharedPreferences sharedpreferences;
    public static ArrayList<User> listuser;
    public static int UserID;
    String url="http://192.168.1.239:8888/PBL4/Git_PBL4/select_user.php";
    String url_login="http://192.168.1.239:8888/PBL4/Git_PBL4/Insert_login.php";
    String url_exist="http://192.168.1.239:8888/PBL4/Git_PBL4/user_exist.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        //khởi tạo shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        init();
        loadData();
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
        cbRemember=findViewById(R.id.cbRemember);
    }
    public boolean Get_User_Exist(String url,final int userID)
    {
        final boolean[] check = new boolean[1];
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Oke"))
                        {
                            Toast.makeText(getApplicationContext(),"Tài khoản đã được đăng nhập",Toast.LENGTH_SHORT).show();
                            check[0] =false;
                        }
                        else  check[0] =true;
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
                params.put("UserID",String.valueOf(userID));
                return params;
            }
        };
        requestQueue.add(stringRequest);
        return check[0];
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
    private void Insert_login(String url_register, final int userID)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_register,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Fail"))
                        {
                            Toast.makeText(getApplicationContext(),"Đăng nhập thất bại",Toast.LENGTH_SHORT).show();
                        }
                        else {
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
                params.put("UserID",String.valueOf(userID));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void handle()
    {
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRemember.isChecked())
                    //lưu lại thông tin đăng nhập
                    saveData(edtuser.getText().toString(), edtpass.getText().toString());
                else
                    clearData();//xoá thông tin đăng nhập
                if(!edtuser.getText().toString().trim().isEmpty()&&!edtpass.getText().toString().trim().isEmpty())
                {
                        if(posCurrent(edtuser.getText().toString(),edtpass.getText().toString())!=-1)
                        {
                            int index = posCurrent(edtuser.getText().toString(),edtpass.getText().toString());
                            //if(Get_User_Exist(url_exist,listuser.get(index).userID)==true)
                            {
                                Insert_login(url_login,listuser.get(index).userID);
                                Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                                gotoMain.putExtra("idCurrentUser",listuser.get(index).userID);
                                gotoMain.putExtra("username",listuser.get(index).username);
                                gotoMain.putExtra("fullname",listuser.get(index).fullName);
                                //finish();
                                startActivity(gotoMain);
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Sai thông tin đăng nhập",Toast.LENGTH_SHORT).show();
                        }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                }
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
    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,cbRemember.isChecked());
        editor.commit();
    }

    private void loadData() {
        if (sharedpreferences.getBoolean(REMEMBER, false)) {
            edtuser.setText(sharedpreferences.getString(USERNAME, ""));
            edtpass.setText(sharedpreferences.getString(PASS, ""));
            cbRemember.setChecked(true);
        }
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
}