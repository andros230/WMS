package com.andros230.wms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryData extends Activity {
    private String TAG = "QueryData";
    private String type;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_data);
        init();
    }

    public void init() {
        TextView ShowView = (TextView) findViewById(R.id.query_data_ShowView);
        list = (ListView) findViewById(R.id.query_data_list);

        Intent intent = getIntent();
        type = intent.getStringExtra("extra");
        if (type.equals("pack")) {
            ShowView.setText("打包数据");
        } else if (type.equals("pick")) {
            ShowView.setText("拣货数据");
        } else if (type.equals("out")) {
            ShowView.setText("收货数据");
        }
        //请求网络查询数据
        queryData();
    }

    //发送数据
    public void queryData() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final StringRequest postRequest = new StringRequest(Request.Method.POST, Utils.SURL + "QueryData", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // 返回数据
                Log.i(TAG, response);
                Gson gson = new Gson();
                List<Map<String, Object>> list2 = gson.fromJson(response, new TypeToken<List<Map<String, Object>>>() {
                }.getType());
                String[][] Str = new String[list2.size()][2];
                int i = 0;
                List<String> listData = new ArrayList<>();
                for (Map<String, Object> map : list2) {
                    Str[i][0] = map.get("all").toString();
                    Str[i][1] = map.get("name").toString();
                    i++;
                }
                String[][] data = bubbleSort(Str);
                for (int i4 = 0; i4 < data.length; i4++) {
                    String all = data[i4][0];
                    String name = data[i4][1];
                    if (name.length() == 2) {
                        if (i4 < 9) {
                            listData.add("  第" + (i4 + 1) + "名　  " + name + "     单量: " + all);
                        } else {
                            listData.add("第" + (i4 + 1) + "名　  " + name + "     单量: " + all);
                        }
                    } else {
                        if (i4 < 9) {
                            listData.add("  第" + (i4 + 1) + "名　" + name + "   单量: " + all);
                        } else {
                            listData.add("第" + (i4 + 1) + "名　" + name + "   单量: " + all);
                        }
                    }
                }
                list.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, listData));
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
                params.put("type", type);
                return params;
            }
        };
        requestQueue.add(postRequest);
    }

    public String[][] bubbleSort(String[][] Str) {
        String temp; // 记录临时中间值
        String nameTemp;
        int size = Str.length; // 数组大小
        for (int i3 = 0; i3 < size - 1; i3++) {
            for (int j = i3 + 1; j < size; j++) {
                if (Integer.parseInt(Str[i3][0]) < Integer.parseInt(Str[j][0])) { // 交换两数的位置
                    temp = Str[i3][0];
                    nameTemp = Str[i3][1];
                    Str[i3][0] = Str[j][0];
                    Str[i3][1] = Str[j][1];
                    Str[j][0] = temp;
                    Str[j][1] = nameTemp;
                }
            }
        }
        return Str;
    }
}
