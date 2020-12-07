package com.example.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.adapter.ChatAdapter;
import com.example.myapplication.fragment.ContactFragment;
import com.example.myapplication.fragment.ContactFragment.CustomRequest;
import com.example.myapplication.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    public static int ToUserID,UserID;
    private String url="http://192.168.1.239:8888/PBL4/Git_PBL4/select_message.php";
    ListView listView;
    private static ArrayList<Message> smss;
    private EditText edtEnter;
    private ImageView imvSend,imvIcons,imvPicture,imvCamera;
    private int[] icons = {R.id.icon_shy, R.id.icon_sad, R.id.icon_happy, R.id.icon_superise, R.id.icon_angry,
            R.id.icon_love, R.id.icon_cry, R.id.icon_died, R.id.icon_embarass, R.id.icon_sleepy};
    private List<String> iconList;
    private RecyclerView rcv;
    private TextView nameClient;
    private Date timeSend;
    private ChatAdapter chatAdapter;
    private Toolbar toolbar;
    private LinearLayout layoutIcons,linearLayoutChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        UserID=this.getIntent().getExtras().getInt("idCurrentUser");
        ToUserID = this.getIntent().getExtras().getInt("idUsername");
        init();
        initRecyclerView();
        handle();
        Get_Message(url);
    }


    private  void SendMessage(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.trim().equals("Fail"))
                       {
                           Toast.makeText(ChatActivity.this,"Không thể gửi tin nhắn",Toast.LENGTH_SHORT).show();
                       }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChatActivity.this,"Error",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String ,String> params = new HashMap<>();
                //params.put("Text",edtuser.getText().toString().trim());//Text
                //params.put("UserID",edtpass.getText().toString().trim());//UserID
                //params.put("ToUserID",edtpass.getText().toString().trim());//ToUserID
                //params.put("RoomID",edtpass.getText().toString().trim());//RoomID
                //params.put("Time",edtfullname.getText().toString().trim());//Time
                return params;
            }
        };
        requestQueue.add(jsonArrayRequest);
    }
    public void Get_Message(String url)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        CustomRequest request = new CustomRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(ChatActivity.this,response.toString(),Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray= response.getJSONArray("message");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        //Toast.makeText(ChatActivity.this,jsonObject.getString("Text").toString(),Toast.LENGTH_SHORT).show();
                        smss.add(new Message(
                                jsonObject.getInt("MessageID"),
                                jsonObject.getInt("RoomID"),
                                jsonObject.getInt("UserID"),
                                jsonObject.getInt("ToUserID"),
                                jsonObject.getString("Text"),
                                jsonObject.getString("Time")
                        ));
                        //Toast.makeText(ChatActivity.this,String.valueOf(smss.size()),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                chatAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Error."+error.toString(), Toast.LENGTH_SHORT).show();
                //Log.d("response",""+error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserID",String.valueOf(UserID));
                params.put("ToUserID",String.valueOf(ToUserID));
                Toast.makeText(ChatActivity.this,String.valueOf(UserID),Toast.LENGTH_SHORT).show();
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void initRecyclerView()
    {
        smss = new ArrayList<Message>();
        rcv = (RecyclerView)findViewById(R.id.rcv_chat);
        chatAdapter = new ChatAdapter(this,smss,ToUserID,UserID);
        LinearLayoutManager llm =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcv.setLayoutManager(llm);
        rcv.setAdapter(chatAdapter);
    }

    public void init() {
        //timeSend = new Date();
        nameClient = (TextView) findViewById(R.id.name_client);
        edtEnter = (EditText) findViewById(R.id.edt_enter);
        imvSend = (ImageView) findViewById(R.id.imv_send);
        imvIcons = (ImageView) findViewById(R.id.imv_icons);
        imvPicture = (ImageView) findViewById(R.id.imv_image);
        imvCamera = (ImageView) findViewById(R.id.imv_camera);
        layoutIcons = (LinearLayout) findViewById(R.id.icons);
        linearLayoutChat = (LinearLayout) findViewById(R.id.line_chat);
        iconList = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            findViewById(icons[i]).setOnClickListener((View.OnClickListener) this);
        }

        nameClient.setText(this.getIntent().getExtras().getString("nameCurrent"));
       /* Intent intent = getIntent();
        clientId = intent.getStringExtra("clientId");
        currentId = intent.getStringExtra("currentId");
        curentName = intent.getStringExtra("currentName");
        clientName = intent.getStringExtra("nameClient");
        nameClient.setText(intent.getStringExtra("nameClient"));*/
    }

    public void handle()
    {
        imvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture,2);
            }
        });
        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollRecycleView();
                if(edtEnter.getText().toString()!=null || (edtEnter.getText().toString()!=""))
                {
                    String value = edtEnter.getText().toString();
                    //Date time = new Date();
                    Message smsCurrent = new Message(UserID,ToUserID,value);
                    smss.add(smsCurrent);//Post len database
                    edtEnter.setText("");
                }

            }
        });
        imvIcons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutIcons.setVisibility(View.VISIBLE);
                linearLayoutChat.setVisibility(View.GONE);
            }
        });

        imvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_angry:
                setIcon("><", R.drawable.angry);
                break;
            case R.id.icon_cry:
                setIcon("😢", R.drawable.cry);
                break;
            case R.id.icon_died:
                setIcon("😑", R.drawable.died);
                break;
            case R.id.icon_embarass:
                setIcon("@@", R.drawable.embarrass);
                break;
            case R.id.icon_happy:
                setIcon("😃", R.drawable.happy);
                break;
            case R.id.icon_love:
                setIcon("❤", R.drawable.love);
                break;
            case R.id.icon_sad:
                setIcon("🙁", R.drawable.sad);
                break;
            case R.id.icon_shy:
                setIcon("🙂", R.drawable.shy);
                break;
            case R.id.icon_sleepy:
                setIcon("-.-", R.drawable.sleep);
                break;
            case R.id.icon_superise:
                setIcon("0.0", R.drawable.superise);
                break;
        }
    }

    public void setIcon(String sign, int id) {
        layoutIcons.setVisibility(View.INVISIBLE);
        linearLayoutChat.setVisibility(View.VISIBLE);
        Drawable d = getResources().getDrawable(id);
        addIconInEditText(d, sign);
    }

    private void addIconInEditText(Drawable drawable, String sign) {
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        int selectionCursorPos = edtEnter.getSelectionStart();
        edtEnter.getText().insert(selectionCursorPos, sign);
        selectionCursorPos = edtEnter.getSelectionStart();
        SpannableStringBuilder builder = new SpannableStringBuilder(edtEnter.getText());
        int startPos = selectionCursorPos - sign.length();
        builder.setSpan(new ImageSpan(drawable), startPos, selectionCursorPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        edtEnter.setText(builder);
        edtEnter.setSelection(selectionCursorPos);
    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            setPhotoData(bitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bitmap selectedImage = (Bitmap)data.getExtras().get("data");
            setPhotoData(selectedImage);
        }
    }*/

    public void scrollRecycleView() {
        rcv.postDelayed(new Runnable() {
            @Override
            public void run() {
                rcv.scrollToPosition(rcv.getAdapter().getItemCount() - 1);
            }
        }, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            InputStream is = null;
            try {
                is = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            //setPhotoData(bitmap);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bitmap selectedImage = (Bitmap)data.getExtras().get("data");
            //setPhotoData(selectedImage);
        }

    }

}