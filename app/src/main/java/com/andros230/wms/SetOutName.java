package com.andros230.wms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SetOutName extends AppCompatActivity {
    private String TAG = "SetOutName";
    private EditText edit;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_out_name);
        tv = (TextView) findViewById(R.id.set_out_name_tv);
        edit = (EditText) findViewById(R.id.set_out_name_edit);
        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String uid = edit.getText().toString().trim();
                    if (uid.length() == 6) {
                        //网络请求
                        sendData(uid);
                    } else {
                        tv.setText("条码长度有误");
                        Utils.playerGlass(getApplicationContext());
                    }
                    edit.setText("");
                }
                return false;
            }
        });

    }

    //发送数据
    public void sendData(final String uid) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.SURL + "QueryName", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response == null || response.equals("NO")) {
                    tv.setText("收货人锁定失败");
                    Utils.playerGlass(getApplicationContext());
                } else {
                    tv.setText("收货人成功锁定为: " + response);
                    Utils.setOutName(getApplicationContext(),response);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("uid", uid);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

}
