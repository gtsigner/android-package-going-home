package com.onynet.a30home.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onynet.a30home.R;
import com.onynet.a30home.activity.QueryActivity;

/**
 * 时 间: 2016/12/28 0028
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class ToastUtils {
    public static ToastUtils toastUtils;
    public static final int LENGHT_SHOT = 3000;
    private Toast toast;
    private ImageView toastiv;
    private TextView textToast;

    public ToastUtils() {
    }

    public static ToastUtils getToastUtils() {
        if (toastUtils == null) {
            toastUtils = new ToastUtils();
        }
        return toastUtils;
    }

    public void ToastShowMsg(Context context, int img, String s) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_msg, null);
        this.textToast = (TextView) view.findViewById(R.id.textToast);
        this.toastiv = (ImageView) view.findViewById(R.id.toast_iv);
        if (img != 0) {
            toastiv.setImageResource(img);
        }
        textToast.setText(s);
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);//设置显示位置
        toast.setDuration(200);
        toast.setView(view);
        toast.show();
    }

    public void ToastShowImg(Context context, String img_path) {
        View view = LayoutInflater.from(context).inflate(R.layout.toast_img, null);
        this.toastiv = (ImageView) view.findViewById(R.id.toast_iv);
        if (!img_path.isEmpty()) {
            //加载图片
            Glide.with(context)
                    .load(img_path)
                    .crossFade()//淡出效果
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//开启缓存
//                    .placeholder(R.mipmap.ic_img)
                    .into(toastiv);
        }
        toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);//设置显示位置
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();

    }

    public void ToastCancel() {
        if (toast != null) {
            toast.cancel();
        }
    }


    /**
     * 解决Toast重复弹出 长时间不消失的问题
     * @param context
     * @param message
     */

    private static Toast toast1;

    public static void showToast(Context context,String message){
        if (toast1==null){
            toast1 = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        }else {
            toast1.setText(message);
        }
        toast1.show();//设置新的消息提示
    }

}
