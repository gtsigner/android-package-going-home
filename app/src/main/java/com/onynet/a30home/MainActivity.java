package com.onynet.a30home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.onynet.a30home.activity.BannerActivity;
import com.onynet.a30home.activity.HelpActivity;
import com.onynet.a30home.activity.ManagerActivity;
import com.onynet.a30home.activity.MoveActivity;
import com.onynet.a30home.activity.MsgActivity;
import com.onynet.a30home.activity.OutActivity;
import com.onynet.a30home.activity.PutActivity;
import com.onynet.a30home.activity.QueryActivity;
import com.onynet.a30home.activity.SetActivity;
import com.onynet.a30home.activity.SmsActivity;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.BannerModel;
import com.onynet.a30home.model.ExpressModel;
import com.onynet.a30home.model.MsgListModel;
import com.onynet.a30home.model.ProfileModel;
import com.onynet.a30home.utils.GlideImageLoader;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.ImageUtil;
import com.onynet.a30home.utils.MainUIUtils;
import com.onynet.a30home.zxing.activity.CaptureActivity;
import com.onynet.a30home.zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.MyApplication.controll;
import static com.onynet.a30home.activity.LoadingActivity.expressModels;
import static com.onynet.a30home.activity.LoadingActivity.profileModel;


public class MainActivity extends AppCompatActivity {
    private static List<String> images = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.main_iv_scan)
    ImageView mainIvScan;
    @BindView(R.id.main_btn_query)
    Button mainBtnQuery;
    @BindView(R.id.main_ll_batch_put)
    LinearLayout mainLlBatchPut;
    @BindView(R.id.main_ll_scan_put)
    LinearLayout mainLlScanPut;
    @BindView(R.id.main_ll_express)
    LinearLayout mainLlExpress;
    @BindView(R.id.main_ll_data)
    LinearLayout mainLlData;
    @BindView(R.id.main_ll_msg)
    LinearLayout mainLlMsg;
    @BindView(R.id.main_ll_set)
    LinearLayout mainLlSet;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.main_et_scan)
    EditText mainEtScan;
    @BindView(R.id.main_iv_msg_icon)
    ImageView mainIvMsgIcon;
    @BindView(R.id.main_rl_msg)
    RelativeLayout mainRlMsg;

    TextView main_name;
    TextView main_location;


    /*创建一个Drawerlayout和Toolbar联动的开关*/
    private ActionBarDrawerToggle toggle;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;

    private List<BannerModel> bannerModels = new ArrayList<>();

    public static List<MsgListModel> msgListModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainUIUtils.setImmersionStatusBar(this);
        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        /*获取轮播图*/
        getBanner();
        /*获取通知*/
        getNotice();
        /*初始化View*/
        initViews();
         /*隐藏滑动条*/
        hideScrollBar();
        /*设置ActionBar*/
        setActionBar();

        /*设置Drawerlayout开关*/
        setDrawerToggle();
        /*设置监听器*/
        setListener();

    }

    private void iniData() {
        images = new ArrayList<>();
        for (int i = 0; i < bannerModels.size(); i++) {
            images.add(ApiConfig.APP_PATH + bannerModels.get(i).getFile_path());
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setIndicatorGravity(BannerConfig.CENTER)//设置指示器居中
                .setBannerAnimation(Transformer.Default)//设置动画
                .setImageLoader(new GlideImageLoader())//设置图片加载器
                .setImages(images)//设置图片集合
                .start();
    }

    /*初始化View*/
    private void initViews() {
        View headerView = navigationView.getHeaderView(0);
        main_name = (TextView) headerView.findViewById(R.id.main_tv_head);
        main_location = (TextView) headerView.findViewById(R.id.main_tv_location);

    }

    /*去掉navigation中的滑动条*/
    private void hideScrollBar() {
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        navigationView.setItemIconTintList(null);
    }

    /*设置ActionBar*/
    private void setActionBar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbarTvTitle.setText("");
        /*显示Home图标*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*设置Drawerlayout的开关,并且和Home图标联动*/
    private void setDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        /*同步drawerlayout的状态*/
        toggle.syncState();
    }

    /*设置监听器*/
    private void setListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main_menu_help:
                        startActivity(new Intent(MainActivity.this, HelpActivity.class));
                        break;
                    case R.id.main_menu_logout:
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("温馨提示")
                                .content("你确定要退出当前账号?")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onAny(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        switch (which){
                                            case POSITIVE:
                                                MyApplication.token = "";
                                                getNotice();
                                                break;
                                        }
                                    }
                                })
                                .show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        banner.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(MainActivity.this, BannerActivity.class);
                intent.putExtra("url", bannerModels.get(position - 1).getUrl());
                startActivity(intent);
            }
        });

        mainEtScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mainEtScan.getText().toString().trim().isEmpty()) {
                    mainIvScan.setImageResource(R.drawable.img_scan);
                } else {
                    mainIvScan.setImageResource(R.drawable.ic_clear);
                    mainEtScan.setSelection(mainEtScan.getText().toString().trim().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                mainEtScan.setText(result);
            }
        });


    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }

    private void getBanner() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_BANNER);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        BannerModel bannerModel = new BannerModel();
                        Gson gson = new Gson();

                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            bannerModel = gson.fromJson(array.getJSONObject(i).toString(), BannerModel.class);
                            bannerModels.add(bannerModel);

                        }

                        /*初始化数据*/
                        iniData();

                    } else {
                        Toast.makeText(MainActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        httpUtils.postSend(this);
    }

    @OnClick({R.id.main_iv_scan, R.id.main_btn_query, R.id.main_ll_batch_put, R.id.main_ll_scan_put, R.id.main_ll_express,
            R.id.main_ll_data, R.id.main_ll_msg, R.id.main_ll_set, R.id.main_rl_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_iv_scan://扫码
                if (mainEtScan.getText().toString().length() != 0) {
                    mainIvScan.setImageResource(R.drawable.ic_clear);
                    mainEtScan.setText("");
                } else {


                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);

//                本地扫描，识别相册中的二维码
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//                startActivityForResult(intent, REQUEST_IMAGE);
                }
                break;
            case R.id.main_btn_query://查询/提货
                Intent intent = new Intent(this, QueryActivity.class);
                intent.putExtra("queryCode", mainEtScan.getText().toString().trim());
                startActivity(intent);
                break;
            case R.id.main_ll_batch_put://包裹入库

                startActivity(new Intent(MainActivity.this, PutActivity.class));

                break;
            case R.id.main_ll_scan_put://包裹出库

                startActivity(new Intent(MainActivity.this, OutActivity.class));

                break;
            case R.id.main_ll_express://移库
                startActivity(new Intent(MainActivity.this, MoveActivity.class));
                break;
            case R.id.main_ll_data://问题件处理
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                break;
            case R.id.main_ll_msg://短信
                startActivity(new Intent(MainActivity.this, SmsActivity.class));
                break;
            case R.id.main_ll_set://设置
                startActivity(new Intent(MainActivity.this, SetActivity.class));
                break;
            case R.id.main_rl_msg://消息
                startActivity(new Intent(MainActivity.this, MsgActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    mainEtScan.setText(result);
                    mainBtnQuery.performClick();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            mainEtScan.setText(result);
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取通知列表
     */
    private void getNotice() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_NOTICE);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        msgListModels.clear();
                        Gson gson = new Gson();
                        MsgListModel msgListModel = new MsgListModel();
                        JSONArray array = object.getJSONObject("data").getJSONArray("list");
                        for (int i = 0; i < array.length(); i++) {
                            msgListModel = gson.fromJson(array.getJSONObject(i).toString(), MsgListModel.class);
                            msgListModels.add(msgListModel);
                        }
                    }

                    if (!object.getJSONObject("data").getString("new_count").equals("0")) {
                        mainIvMsgIcon.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        httpUtils.postSend(this);
    }

    /**
     * 获得快递公司名字
     */
    private void getExpressNames() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_COMPANY_LIST);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        expressModels.clear();
                        ExpressModel expressModel = new ExpressModel();
                        Gson gson = new Gson();

                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            expressModel = gson.fromJson(item.toString(), ExpressModel.class);
                            expressModels.add(expressModel);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        httpUtils.postSend(this);
    }

    /**
     * 获得个人信息
     */
    private void getProfile() {
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

                        main_name.setText("【"+profileModel.getName()+"】");
                        main_location.setText(profileModel.getAddress_desc());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

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

    @Override
    protected void onResume() {
        getNotice();
        getExpressNames();
        getProfile();
        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                mainEtScan.setText(result);
                mainBtnQuery.performClick();
            }
        });
        super.onResume();
    }

    @Override
    protected void onPause() {
        ((MyApplication) getApplication()).setOnScanResultListener(null);
        super.onPause();
    }

}

