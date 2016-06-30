package com.andros230.wms;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.andros230.bean.Box;

import java.util.HashMap;
import java.util.Map;

public class SH_Scan extends Activity {
    private String TAG = "SH_Scan";
    private EditText edit;
    private TextView tv_showText, tv_outN, tv_desk, tv_packN;
    private ScrollView scroll;
    private Box box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sh__scan);
        init();

        tv_outN.setText("收货人: " + Utils.getOutName(this));

        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    String way = edit.getText().toString().trim();
                    String desk = tv_desk.getText().toString().trim();
                    String outN = tv_outN.getText().toString().trim();
                    edit.setText("");
                    box = new Box();
                    box.setWay(way);
                    box.setDesk(desk);
                    box.setOutN(outN);
                    if (way.length() == 5) {
                        queryDesk(way);
                    } else {
                        String error = Utils.wayIsPass(box).getError();
                        if (error == null) {
                            sendData();
                        } else {
                            tv_showText.setText(error + "\n" + tv_showText.getText().toString());
                            scroll.fullScroll(View.FOCUS_FORWARD);
                            Utils.playerGlass(getApplicationContext());
                        }
                    }
                }
                return false;
            }
        });
    }

    private void init() {
        tv_outN = (TextView) findViewById(R.id.sh_scan_outN);
        edit = (EditText) findViewById(R.id.edit_main);
        tv_desk = (TextView) findViewById(R.id.sh_scan_desk);
        tv_showText = (TextView) findViewById(R.id.sh_scan_showText);
        scroll = (ScrollView) findViewById(R.id.sh_scan_scroll);
        tv_packN = (TextView) findViewById(R.id.sh_scan_packN);
    }

    //发送数据
    public void sendData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.SURL + "ShScan", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 返回数据
                if (response.equals("YES")) {
                    tv_showText.setText(box.getWay() + "  扫描完成\n" + tv_showText.getText().toString());
                } else {
                    tv_showText.setText(response + "\n" + tv_showText.getText().toString());
                    Utils.playerGlass(getApplicationContext());
                }
                scroll.fullScroll(View.FOCUS_FORWARD);

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
                params.put("way", box.getWay());
                params.put("desk", box.getDesk());
                params.put("exp", box.getExp());
                params.put("outN", box.getOutN());
                return params;
            }
        };
        requestQueue.add(postRequest);
    }


    public void queryDesk(final String desk) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.SURL + "QueryDesk", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && !response.equals("0") && !response.equals("null")) {
                    tv_desk.setText(desk);
                    tv_packN.setText(response);
                } else {
                    tv_packN.setText("");
                    tv_desk.setText("");
                    Utils.playerGlass(getApplicationContext());
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
                params.put("desk", desk);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }


}
