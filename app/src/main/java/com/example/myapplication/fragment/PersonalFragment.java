package com.example.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.RegisterActivity;
import com.example.myapplication.model.User;

import java.util.HashMap;
import java.util.Map;


public class PersonalFragment extends Fragment {
    public TextView edtusername;
    public EditText edtpass,edtconfirm_pass,edtfullname;
    public ImageButton update;
    public User UserCurrent;
    String url="http://192.168.1.239:8888/PBL4/Git_PBL4/update_user.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.personal_fragment, container, false);
        init(view);
        handle();
        return view;
    }

    private void handle() {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtpass.getText().toString().isEmpty()&&!edtconfirm_pass.getText().toString().isEmpty()&&!edtfullname.getText().toString().isEmpty())
                {
                   if(edtpass.getText().toString().trim().equals(edtconfirm_pass.getText().toString().trim()))
                   {
                       Update_Infor(url);
                   }
                   else
                   {
                       Toast.makeText(getActivity(),"Mật khẩu chưa khớp",Toast.LENGTH_SHORT).show();
                   }
                }
                else
                {
                    Toast.makeText(getActivity(),"Không được để trống",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Update_Infor(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Ok"))
                        {
                            Toast.makeText(getActivity(),"Cập nhật thông tin thành công",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(),"Cập nhật thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Xảy ra lỗi",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params = new HashMap<>();
                params.put("UserID",String.valueOf(UserCurrent.getUserID()));
                params.put("password",edtpass.getText().toString().trim());
                params.put("fullname",edtfullname.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void init(View view) {
        UserCurrent = (User) getActivity().getIntent().getExtras().getSerializable("UserCurrent");
        edtusername=(TextView)view.findViewById(R.id.username);
        edtpass=(EditText)view.findViewById(R.id.pass);
        edtconfirm_pass=(EditText)view.findViewById(R.id.confirm_pass);
        edtfullname=(EditText)view.findViewById(R.id.fullname);
        update=(ImageButton) view.findViewById(R.id.update);
        edtusername.setText(UserCurrent.getUsername());
        edtfullname.setText(UserCurrent.getFullName());
    }

}
