package com.onynet.a30home.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 时 间: 2016/12/25 0025
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class BitmapUtils {

    /**
     * 高斯模糊图片
     * @param context
     * @param bitmap
     * @return
     */
    public static Bitmap  blurBitmap(Context context,Bitmap bitmap){
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        RenderScript rs =RenderScript.create(context.getApplicationContext());
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation allIn = Allocation.createFromBitmap(rs,bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs,outBitmap);

        blurScript.setRadius(25.0f);

        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        allOut.copyTo(outBitmap);

        bitmap.recycle();

        rs.destroy();

        return outBitmap;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage   ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        if (newWidth>newHeight){
            float scaleHeight = ((float) newHeight) / height;
            // 缩放图片动作
            matrix.postScale(scaleHeight, scaleHeight);
        }else {
            float scaleWidth = ((float) newWidth) / width;
            matrix.postScale(scaleWidth, scaleWidth);
        }



        return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
    }


    /**
     * 计算位图的采样比例大小
     *
     * @param options
     * @return
     */
    private static int calculatInSampleSize(BitmapFactory.Options options, int width, int height) {
        //获取位图的原宽高
        final int w = options.outWidth;
        final int h = options.outHeight;

        System.out.println("宽:" + w);
        System.out.println("高:" + h);


        //默认为一(就是不压缩)
        int inSampleSize = 5;
        //如果原图的宽高比需要的图片宽高大
        if (w > width || h > height) {
            if (w > h) {
                inSampleSize = Math.round((float) h / (float) height);
            } else {
                inSampleSize = Math.round((float) w / (float) width);
            }
        }

        System.out.println("压缩比为:" + inSampleSize);

        return inSampleSize;

    }


    /**
     * 将Uri转换成Bitmap
     *
     * @param options
     * @return
     */
    private static Bitmap decodeBitmap(String picPath, BitmapFactory.Options options) {
        Bitmap bitmap = null;
        FileInputStream fis = null;
        try {
            //把图片转换成字节流
            fis = new FileInputStream(picPath);
            //把流转换成图片
            bitmap = BitmapFactory.decodeStream(fis,null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            try {
                assert fis != null;
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;


    }

    /**
     * 对图片进行重新采样
     *
     * @return
     */
    public static Bitmap compressBitmap(String picPath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        decodeBitmap(picPath, options);

//        options = new BitmapFactory.Options();

        options.inSampleSize = calculatInSampleSize(options, width, height);
        Bitmap bitmap = null;

        try {
            bitmap = decodeBitmap(picPath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }



    public static boolean saveMyBitmap(Context context,String bitName, Bitmap mBitmap) {
        Log.e("BitmapUtils", "保存图片");
        File f = new File(context.getExternalCacheDir(), bitName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i("BitmapUtils", "已经保存至:" + f.toURI());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
