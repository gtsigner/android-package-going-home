package com.onynet.a30home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MainActivity;
import com.onynet.a30home.MainActivity2;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.ExpressModel;
import com.onynet.a30home.model.ProfileModel;
import com.onynet.a30home.model.UpDateModel;
import com.onynet.a30home.service.AppUpdateService;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.VersionUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.onynet.a30home.MyApplication.controll;

public class LoadingActivity extends BaseActivity {
    /*版本信息*/
    public static UpDateModel upDateModel = new UpDateModel();

    public static ProfileModel profileModel = new ProfileModel();

    public static String APK_PATH;

    //快递公司列表
    public static List<ExpressModel> expressModels = new ArrayList<>();

    /*当前版本号*/
    public static int VERSION;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (MyApplication.group_type.equals("0")){
                        startActivity(new Intent(LoadingActivity.this, MainActivity.class));
                    }else {
                        startActivity(new Intent(LoadingActivity.this, MainActivity2.class));
                    }
                    break;
                case 1001:
                    load();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 让状态了成半透明状态
         */
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色透明
            window.setNavigationBarColor(Color.TRANSPARENT);//设置导航栏颜色透明
        }

        setContentView(R.layout.activity_loading);

        APK_PATH = getExternalCacheDir() + "/30home.apk";

        /*获取当前版本*/
        VERSION = VersionUtils.getVersionCode(this);

        System.out.println("版本号:" + VERSION);

        getUpdate();
    }

    private void load() {
        if (TextUtils.isEmpty(MyApplication.token)) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {

            HttpUtils httpUtils = new HttpUtils();
            httpUtils.setOnResult(new HttpUtils.OnResult() {
                @Override
                public RequestParams onParams() {
                    RequestParams params = new RequestParams(ApiConfig.GET_PROFILE);
                    params.addBodyParameter("token", MyApplication.token);
                    return params;
                }

                @Override
                public void onUsage(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getInt("code") == 200) {
                            profileModel = new ProfileModel();
                            Gson gson = new Gson();
                            profileModel = gson.fromJson(object.getJSONObject("data").toString(), ProfileModel.class);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessageDelayed(1000, 1000);
                }

                @Override
                public void onError(String error) {
                    startActivity(new Intent(LoadingActivity.this, LoginActivity.class));

                }
            });

            httpUtils.postSend(this);
        }

    }


    private void getUpdate() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                return new RequestParams(ApiConfig.GET_UPDATE);
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);

                    if (object.getInt("code") == 200) {
                        Gson gson = new Gson();
                        upDateModel = gson.fromJson(object.getJSONObject("data").toString(), UpDateModel.class);
                        final int version = Integer.valueOf(upDateModel.getVersion_code());
                        System.out.println("下载地址" + ApiConfig.APP_PATH + upDateModel.getFile_path());
                        if (version > VERSION) {
                            new MaterialDialog.Builder(LoadingActivity.this)
                                    .iconRes(R.drawable.ic_update)
                                    .title("下载更新")
                                    .positiveText("更新")
                                    .negativeText("以后再说")
                                    .content(upDateModel.getDesc())
                                    .onAny(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            switch (which) {
                                                case POSITIVE://更新
                                                    /*开启更新服务*/
                                                    AppUpdateService.startUpdate(LoadingActivity.this, ApiConfig.APP_PATH + upDateModel.getFile_path(), APK_PATH, new AppUpdateService.OnProgressListener() {
                                                        MaterialDialog dialog = new MaterialDialog.Builder(LoadingActivity.this)
                                                                .content("正在更新,请耐心等待....")
                                                                .progress(false, 100, true)
                                                                .negativeText("后台更新")
                                                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                                    @Override
                                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                })
                                                                .show();

                                                        @Override
                                                        public void onProgress(int progress) {
                                                            dialog.setProgress(progress);
                                                        }

                                                        @Override
                                                        public void onSuccess(boolean isSuccess) {
                                                            dialog.dismiss();
                                                            //失败提示
                                                            if (!isSuccess) {
                                                                Toast.makeText(LoadingActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    break;
                                                case NEGATIVE:
                                                    handler.sendEmptyMessageDelayed(1001, 1000);
                                                    break;
                                            }
                                        }
                                    })
                                    .show();
                        } else {
                            handler.sendEmptyMessageDelayed(1001, 1000);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                handler.sendEmptyMessageDelayed(1001, 1000);
            }
        });

        httpUtils.postSend(this);
    }


    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        if (keyCode == 139 || keyCode == 140 || keyCode == 141) {
            controll.scan_stop();
            controll.scan_start();
        }
        return super.onKeyDown(keyCode, event);
    }

}
