package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.ErrorListItemAdapter;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.PackageModel;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.zxing.activity.CaptureActivity;
import com.onynet.a30home.zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.MainActivity.REQUEST_CODE;
import static com.onynet.a30home.MyApplication.controll;

public class ErrorActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.move_et_scan)
    EditText moveEtScan;
    @BindView(R.id.move_iv_scan)
    ImageView moveIvScan;
    @BindView(R.id.move_lv)
    RecyclerView moveLv;
    @BindView(R.id.toolbar_btn_menu)
    ImageView toolbarBtnMenu;
    private String company_id = "1";//快递公司

    public static List<PackageModel> packageModels;

    private ErrorListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
    }


    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.package_error);
        toolbarBtnMenu.setVisibility(View.GONE);
        moveLv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        packageModels = new ArrayList<>();
        adapter = new ErrorListItemAdapter(this, packageModels);
        moveLv.setAdapter(adapter);
    }

    private void setListener() {
        moveEtScan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (moveEtScan.getText().toString().trim().isEmpty()) {
                    moveIvScan.setImageResource(R.drawable.img_scan);
                } else {
                    moveIvScan.setImageResource(R.drawable.ic_clear);
                    moveEtScan.setSelection(moveEtScan.getText().toString().trim().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                getPackage();
            }
        });

        //开启PDA扫描
        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                moveEtScan.setText(result);
            }
        });

        adapter.setOnHandelPackageListener(new ErrorListItemAdapter.OnHandelPackageListener() {
            @Override
            public void onSuccess() {
                getPackage();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.move_iv_scan,R.id.toolbar_btn_menu})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.move_iv_scan://扫码
                if (moveEtScan.getText().toString().length() != 0) {
                    moveIvScan.setImageResource(R.drawable.ic_clear);
                    moveEtScan.setText("");
                } else {
                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
                }
                break;
            case R.id.toolbar_btn_menu:

                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    moveEtScan.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(ErrorActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    /**
     * 获取包裹信息
     */
    private void getPackage() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_PACKAGE);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("yun_no", moveEtScan.getText().toString().trim());
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        packageModels.clear();
                        Gson gson = new Gson();
                        PackageModel packageModel = new PackageModel();

                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            packageModel = gson.fromJson(array.getJSONObject(i).toString(), PackageModel.class);
                            packageModels.add(packageModel);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        System.out.println("错误信息: " + object.getString("msg"));
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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 140 || keyCode == 141) {
            controll.scan_stop();
            controll.scan_start();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                moveEtScan.setText(result);
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

