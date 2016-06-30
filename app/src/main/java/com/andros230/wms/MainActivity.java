package com.andros230.wms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SH_Scan.class);
                    startActivity(intent);

                } else if (i == 2) {
                    Intent intent = new Intent();
                    intent.putExtra("extra", "pack");
                    intent.setClass(MainActivity.this, QueryData.class);
                    startActivity(intent);
                } else if (i == 5) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SetOutName.class);
                    startActivity(intent);
                }
            }
        });
    }

    private List<String> getData() {
        List<String> data = new ArrayList<>();
        data.add("* 收货扫描");
        data.add("* 拣货数据");
        data.add("* 打包数据");
        data.add("* 收货数据");
        data.add("* 设置打包人");
        data.add("* 设置收货人");
        data.add("* 运单查询");
        data.add("* 顺丰－出库扫描");
        data.add("* 京东－出库扫描");
        data.add("* 前置仓－出库扫描");
        data.add("* 出库数据");
        data.add("* 未出库运单");
        data.add("* 前置仓检查");
        return data;
    }
}
