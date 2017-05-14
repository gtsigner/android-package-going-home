package com.onynet.a30home;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.android.barcodescandemo.ScannerInerface;
import com.onynet.a30home.broadcast.ConnectionChangeReceiver;
import com.onynet.a30home.zxing.DisplayUtil;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;


/**
 * 时 间: 2016/12/24 0024
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class MyApplication extends Application {

    ConnectionChangeReceiver connectionChangeReceiver;

    public static String token;
    public static String group_type;
    public static long UpGoodsCode;//提货码
    public static String date;//提货码日期

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        initPDA();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        SharedPreferences sharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        group_type = sharedPreferences.getString("group_type", "");

        sharedPreferences = getSharedPreferences("UpGoodsCode", MODE_PRIVATE);
        date = sharedPreferences.getString("date", "");
        UpGoodsCode = sharedPreferences.getLong("UpGoodsCode", 0);
        System.out.println(UpGoodsCode);

        /**
         * 初始化尺寸工具类
         */
        initDisplayOpinion();

        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        connectionChangeReceiver=new ConnectionChangeReceiver();
        this.registerReceiver(connectionChangeReceiver, filter);

    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }

    //运用list来保存们每一个activity是关键
    private List<Activity> mList = new LinkedList<Activity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static MyApplication instance;

    //构造方法
    public MyApplication() {
    }

    //实例化一次
    public synchronized static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
    /**
     * PDA扫码属性
     */
    private BroadcastReceiver mReceiver;
    private IntentFilter mFilter;
    public static ScannerInerface controll;
    /**
     * 初始化PAD
     */
    private void initPDA() {
        controll = new ScannerInerface(this);
        controll.open();
        controll.enablePlayBeep(true);
        controll.setOutputMode(1);//给扫描头进行上电，扫描前一定要先执行此操作再启动扫描
        controll.unlockScanKey();
        controll.enablePlayVibrate(true);

        mFilter = new IntentFilter("android.intent.action.SCANRESULT");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // 此处获取扫描结果信息
                final String scanResult = intent.getStringExtra("value");
//				String  barocode=intent.getStringExtra("value");
                int barocodelen = intent.getIntExtra("length", 0);
//                int type = intent.getIntExtra("type", 0);
                if (!TextUtils.isEmpty(scanResult)) {
                    if (onScanResultListener!=null)
                    onScanResultListener.onResult(scanResult);
                    controll.scan_stop();
                }
            }
        };

        registerReceiver(mReceiver, mFilter);

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mReceiver, mFilter);
//    }


    @Override
    public void onTerminate() {
        if (mReceiver != null)
            mReceiver = null;
        if (mFilter != null)
            mFilter = null;
        if (controll != null)
            controll.close();

        // 注销获取扫描结果的广播
        this.unregisterReceiver(mReceiver);

        this.unregisterReceiver(connectionChangeReceiver);

        super.onTerminate();

    }

    public interface onScanResultListener{
       void onResult(String result);
    }
    private onScanResultListener onScanResultListener;

    public void setOnScanResultListener(MyApplication.onScanResultListener onScanResultListener) {
        this.onScanResultListener = onScanResultListener;
    }
}
