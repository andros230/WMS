package com.andros230.wms;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.andros230.bean.Box;


/**
 * Created by andros230 on 2016/6/29.
 */
public class Utils {
    private static MediaPlayer mp;
    public static final int LENGTH_SF = 12;
    public static final int LENGTH_JD = 13;
    public static final String SURL = "http://192.168.0.103:8080/DCServer/";

    public static void playerGlass(Context context) {
        if (mp != null) {
            mp.stop();
            mp.release();
        }
        mp = MediaPlayer.create(context, R.raw.glass);
        mp.start();
    }

    public static String getOutName(Context context) {
        // 获取收货人姓名
        SharedPreferences pref = context.getSharedPreferences("SH_Scan", Context.MODE_PRIVATE);
        String outName = pref.getString("name", "");
        return outName;
    }

    public static void setOutName(Context context,String outName){
        SharedPreferences pref = context.getSharedPreferences("SH_Scan", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", outName.trim());
        editor.commit();
    }

    public static Box wayIsPass(Box box) {
        if (box.getWay().length() == Utils.LENGTH_SF) {
            box.setExp("sf");
        } else if (box.getWay().length() == Utils.LENGTH_JD) {
            box.setExp("jd");
        } else {
            box.setError("--条码长度有误--");
        }

        if (box.getDesk().length() < 1) {
            box.setError("--请先扫台位--");
        }

        if (box.getOutN().length() < 1) {
            box.setError("--请设置收货人--");
        }

        return box;
    }
}
