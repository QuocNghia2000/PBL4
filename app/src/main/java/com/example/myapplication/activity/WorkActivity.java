package com.example.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.myapplication.adapter.WorkAdapter;
import com.example.myapplication.model.Work;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkActivity extends AppCompatActivity {

    public ArrayList<Work> listwork;
    private int idCurrentUser;
    public ListView lvWork;
    public WorkAdapter adapterWork;
    private ImageView img_update,img_add,img_deletework;
    TimePicker timePicker;
    DatePicker datePicker;
    public static String url_get_work="http://192.168.1.239:8888/PBL4/Git_PBL4/select_work.php";
    String url_delete_work="http://192.168.1.239:8888/PBL4/Git_PBL4/delete_work.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        init();
    }

    @Override
    protected void onStart() {
        Get_work();
        super.onStart();
    }

    private void init()
    {
        lvWork = findViewById(R.id.lv_work);
        idCurrentUser = this.getIntent().getExtras().getInt("idCurrentUser");
        listwork=new ArrayList<>();
        img_update = findViewById(R.id.img_updatework);
        img_add = findViewById(R.id.img_addwork);
        timePicker=findViewById(R.id.Time_work);
        datePicker=findViewById(R.id.date_work);
        img_deletework=findViewById(R.id.img_deletework);
    }
    public ArrayList<Work> Get_work()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        ChatActivity.CustomRequest request = new ChatActivity.CustomRequest(Request.Method.POST, url_get_work, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listwork.clear();
                        try {
                            JSONArray jsonArray= response.getJSONArray("work");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listwork.add(new Work(
                                        jsonObject.getInt("IDwork"),
                                        jsonObject.getInt("UserID"),
                                        jsonObject.getString("Namework"),
                                        jsonObject.getString("Detail_work"),
                                        jsonObject.getString("Time"),
                                        jsonObject.getInt("status")
                                ));
                            }
                            handle(listwork);
                            adapterWork.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error."+error.toString(), Toast.LENGTH_SHORT).show();
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
                params.put("UserID",String.valueOf(idCurrentUser));
                return params;
            }
        };
        requestQueue.add(request);
        return listwork;
    }
    public void Delete_work(final int IDwork)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_delete_work,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Oke"))
                        {
                            Toast.makeText(getApplicationContext(),"Xoá thành công",Toast.LENGTH_SHORT).show();
                            Get_work();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_SHORT).show();
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
                params.put("IDwork",String.valueOf(IDwork));//IDwork
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void handle(final ArrayList<Work> list)
    {
        //Toast.makeText(getActivity(),String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
        adapterWork = new WorkAdapter(listwork,  this);
        lvWork.setAdapter(adapterWork);
        lvWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), String.valueOf(list.get(position).getIDwork()), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(),WorkActivity.class);
//                intent.putExtra("idCurrentUser",getActivity().getIntent().getExtras().getInt("idCurrentUser"));
//                intent.putExtra("idWork",-1);
//                int Request_code=9;
//                startActivityForResult(intent,Request_code);
            }
        });


        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddWorkActivity.class);
                intent.putExtra("idCurrentUser",idCurrentUser);
                intent.putExtra("inf","add");
                startActivity(intent);
            }
        });
    }


}