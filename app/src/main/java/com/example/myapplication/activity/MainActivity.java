package com.example.myapplication.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.fragment.ContactFragment;
import com.example.myapplication.fragment.PersonalFragment;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Work;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    public User UserCurrent;
    ImageButton img_delete,imgbt_logout;
    MultiAutoCompleteTextView edt_search;
    ArrayList<User> list_user;
    ArrayList<String> listfullname;
    public ArrayList<Work> listwork,listworkUser;

    String url="http://192.168.1.239:8888/PBL4/Git_PBL4/search.php";
    String url_logout="http://192.168.1.239:8888/PBL4/Git_PBL4/user_logout.php";
    String url_delete_work="http://192.168.1.239:8888/PBL4/Git_PBL4/delete_work.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //changeStatusBarColor("#1651E4");
        setContentView(R.layout.activity_main);
        init();
        handle();
    }

    private void init() {
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        settupViewpager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        UserCurrent = (User) getIntent().getExtras().getSerializable("UserCurrent");
        img_delete=findViewById(R.id.img_delete);
        edt_search=findViewById(R.id.edt_search);
        list_user=new ArrayList<>();
        listfullname=new ArrayList<>();
        listwork=new ArrayList<>();
        listworkUser = new ArrayList<>();

    }

    private  void Get_fullname(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ChatActivity.CustomRequest request = new ChatActivity.CustomRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(ChatActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                        list_user.clear();
                        try {
                            JSONArray jsonArray= response.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                list_user.add(new User(
                                        jsonObject.getInt("UserID"),
                                        jsonObject.getString("Fullname")
                                ));
                                listfullname.add(jsonObject.getString("Fullname"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error."+error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("response",""+error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID",String.valueOf(UserCurrent.getUserID()));
                return params;
            }
        };
        requestQueue.add(request);
    }
    private  void Logout(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Oke"))
                        {
                            Toast.makeText(getApplicationContext(),"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
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
                params.put("UserID",String.valueOf(UserCurrent.getUserID()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void handle() {
        Get_fullname(url);
        ArrayAdapter adapterSecondLanguage = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listfullname);
        edt_search.setAdapter(adapterSecondLanguage);

        // Sét đặt số ký tự nhỏ nhất, để cửa sổ gợi ý hiển thị
        edt_search.setThreshold(1);

        // Các đoạn text ngăn cách nhau bởi dấu phẩy.
        edt_search.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        edt_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotochat = new Intent(MainActivity.this, ChatActivity.class);
                String edt=edt_search.getText().toString();
                edt=edt.substring(0,edt.length()-2);
                int pos=listfullname.indexOf(edt);
                gotochat.putExtra("nameCurrent", list_user.get(pos).fullName);
                gotochat.putExtra("idUsername", list_user.get(pos).userID);
                gotochat.putExtra("idCurrentUser", UserCurrent.getUserID());
                //gotochat.putExtra("status",userList.get(position).status);
                startActivity(gotochat);
            }
        });
        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
            }
        });
    }

    @Override
    public void finish() {
        Logout(url_logout);
        super.finish();
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu_actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_assign)
        {
            Intent intent = new Intent(this,WorkActivity.class);
            intent.putExtra("idCurrentUser",UserCurrent.getUserID());
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_back)
        {
            finish();
        }
        if (item.getItemId() == R.id.menu_logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
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
        return super.onOptionsItemSelected(item);
    }

    public void settupViewpager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public List<Fragment> fragments = new ArrayList<>();
        public List<String> titles= new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            titles.add("Tin nhắn");
            titles.add("Cá nhân");
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0: return new ContactFragment();
                case 1: return new PersonalFragment();
            }
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}