package com.onynet.a30home.utils;

import android.os.Environment;

import java.io.File;

/**
 * 时 间: 2017/1/10 0010
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class SDUtils {
    /**
     * 获取SD卡根目录
     * @return
     */
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
