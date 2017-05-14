package com.onynet.a30home.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.onynet.a30home.activity.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Http请求工具
 */

public class HttpUtils {

    private static HttpUtils httpPost;

    public HttpUtils() {
    }

    public static HttpUtils getInstance() {
        if (httpPost == null) {
            httpPost = new HttpUtils();
        }
        return httpPost;
    }

    public void postSend(final Context context) {


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
//            setNetwork(context);
            ToastUtils.showToast(context,"你已经进入没有网络的异次元世界");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //没有网络的时候自动调至登录界面
                    if (!LoginActivity.class.isInstance(context)){
                        context.startActivity(new Intent(context, LoginActivity.class));
                    }
                }
            },1000);

        } else {
            x.http().post(onResult.onParams(), new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {

                    System.out.println(result);

                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getInt("code") == 205) {
                            ToastUtils.showToast(context,"登录失效请重新登录");
                            context.startActivity(new Intent(context, LoginActivity.class));
                        } else {
                            onResult.onUsage(result);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    System.out.println("请求出错...");
                    System.out.println(ex);
                    onResult.onError(ex.toString());
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }


    }

    public void postSend() {
        x.http().post(onResult.onParams(), new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                onResult.onUsage(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("请求出错...");
                System.out.println(ex);
                onResult.onError(ex.toString());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    public void getSend(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!mobNetworkInfo.isConnected() && !wifiNetworkInfo.isConnected()) {
//            setNetwork(context);
            ToastUtils.showToast(context,"你已经进入没有网络的异次元世界");

        } else {

            x.http().get(onResult.onParams(), new Callback.CacheCallback<String>() {
                @Override
                public boolean onCache(String result) {
                    return false;
                }

                @Override
                public void onSuccess(String result) {

                    onResult.onUsage(result);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    System.out.println("请求出错...");
                    System.out.println(ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }
            });
        }
    }

    public interface OnResult {
        /**
         * @return 自定义的RequestParame请求参数
         */
        RequestParams onParams();

        /**
         * 请求成功后的操作
         *
         * @param result
         */
        void onUsage(String result);

        /**
         * 请求出错后的操作
         */
        void onError(String error);
    }

    private OnResult onResult;

    public void setOnResult(OnResult onResult) {
        this.onResult = onResult;
    }
}
