package cc.ixcc.noveltwo.jsReader.utils;

import android.widget.Toast;

import cc.ixcc.noveltwo.treader.AppContext;


/**
 * Created by newbiechen on 17-5-11.
 */

public class ToastUtils {
    public static void show(String msg){
        if(!msg.equals("参数错误"))
        {
            Toast.makeText(AppContext.sInstance, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
