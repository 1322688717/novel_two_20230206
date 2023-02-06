package cc.ixcc.noveltwo.treader.util;

import android.os.Environment;

import cc.ixcc.noveltwo.treader.bean.Cache;

import java.io.File;
import java.util.ArrayList;

public class JiuSenBookUtil {
    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/treader/";
    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected final ArrayList<Cache> myArray = new ArrayList<>();

    public void JiuSenBookUtil(){
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

}
