package com.example.myapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activity.ChatActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.RegisterActivity;

import com.example.myapplication.adapter.ContactAdapter;
import com.example.myapplication.model.Message;
import com.example.myapplication.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapplication.activity.LoginActivity.listuser;

public class ContactFragment extends Fragment {
    String url_mess="http://192.168.1.239:8888/PBL4/Git_PBL4/message_user.php";
    String url_user="http://192.168.1.239:8888/PBL4/Git_PBL4/infor_ToUserID_Mess.php";
    int idCurrentUser;
    public static ArrayList<Integer> sms;
    public static ArrayList<User> userArrayList;
    ArrayList<User> userList;
    ListView lvcontact;
    ContactAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.contact_fragment, container, false);
        init(view);
        Get_User_Contact(url_mess);
        return view;
    }

    public void init(View view)
    {
        lvcontact = (ListView)view.findViewById(R.id.lv_contact);
        sms=new ArrayList<>();
        userArrayList=new ArrayList<>();
        userList=new ArrayList<>();
    }
    private  void handle( final ArrayList<User> userArrayList)
    {
        ArrayList<User> Listuser = new ArrayList<User>();
        for(int i=0;i<userArrayList.size();i++)
        {
            boolean check=true;
            for (int j=0;j<sms.size();j++)
            {
                if(userArrayList.get(i).userID==sms.get(j))
                {
                    check=false;
                }
            }
            if(check==false)  Listuser.add(userArrayList.get((i)));
        }

        //Toast.makeText(getActivity(), String.valueOf(idCurrentUser), Toast.LENGTH_SHORT).show();
        adapter = new ContactAdapter(Listuser, getActivity());
        lvcontact.setAdapter(adapter);
        lvcontact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotochat = new Intent(getActivity(), ChatActivity.class);

                gotochat.putExtra("nameCurrent",userArrayList.get(position).fullName);
                gotochat.putExtra("idUsername",userArrayList.get(position).userID);
                gotochat.putExtra("idCurrentUser",idCurrentUser);
                //gotochat.putExtra("status",userList.get(position).status);
                startActivity(gotochat);
            }
        });
    }
    public void Get_User_Contact(String url)
    {
        idCurrentUser = getActivity().getIntent().getExtras().getInt("idCurrentUser");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        CustomRequest request = new CustomRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray = response.getJSONArray("message");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sms.add(jsonObject.getInt("ToUserID"));
                    }
                    GetData(url_user,sms);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error."+error.toString(), Toast.LENGTH_SHORT).show();
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
    }

    public  void GetData(String url,final ArrayList<Integer> sms)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray= response.getJSONArray("user");
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                userArrayList.add(new User(
                                        jsonObject.getInt("UserID"),
                                        jsonObject.getString("Username"),
                                        jsonObject.getString("Password"),
                                        jsonObject.getString("Fullname")
                                ));
                            }
                            handle(userArrayList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
    public static class CustomRequest extends Request<JSONObject> {

        private Response.Listener<JSONObject> listener;
        private Map<String, String> params;

        public CustomRequest(String url, Map<String, String> params,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
            super(Method.GET, url, errorListener);
            this.listener = reponseListener;
            this.params = params;
        }

        public CustomRequest(int method, String url, Map<String, String> params,
                             Response.Listener<JSONObject> reponseListener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.listener = reponseListener;
            this.params = params;
        }

        protected Map<String, String> getParams()
                throws com.android.volley.AuthFailureError {
            return params;
        };

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                return Response.success(new JSONObject(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }

        @Override
        protected void deliverResponse(JSONObject response) {
            // TODO Auto-generated method stub
            listener.onResponse(response);
        }
    }
}

