package com.onynet.a30home.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时 间: 2017/1/7 0007
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class TimeUtils {
    /**
     * 将时间戳转换成字符串
     * @param timeFormat 字符串格式
     * @param timestamp 时间戳
     * @return
     */
    public static String timeToString(String timeFormat,long timestamp){
        String time;
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        time = format.format(new Date(timestamp*1000));
        return time;
    }
}
