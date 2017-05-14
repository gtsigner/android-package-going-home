package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.QueryListItemAdapter1;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.QueryListModel1;
import com.onynet.a30home.model.QueryListModel1.PackageListBean;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.zxing.activity.CaptureActivity;
import com.onynet.a30home.zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.MyApplication.controll;
import static com.onynet.a30home.activity.OutActivity.REQUEST_CODE;
import static com.onynet.a30home.adapter.QueryListItemAdapter.getIsSelected;

public class QueryActivity1 extends BaseActivity {

    private static final int REQUEST_THUMBNAIL = 1000;
    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.query_et_query)
    EditText queryEtQuery;
    @BindView(R.id.query_iv_clear)
    ImageView queryIvClear;
    @BindView(R.id.query_iv_search)
    ImageView queryIvSearch;
    @BindView(R.id.query_lv)
    RecyclerView queryLv;
    @BindView(R.id.activity_query)
    RelativeLayout activityQuery;
    @BindView(R.id.query_tv_sum)
    TextView queryTvSum;
    @BindView(R.id.query_tv_no_out)
    TextView queryTvNoOut;
    @BindView(R.id.query_cb_select_all)
    CheckBox queryCbSelectAll;
    @BindView(R.id.query_all_put)
    TextView queryAllPut;
    @BindView(R.id.query_rl_all_out)
    RelativeLayout queryRlAllOut;


    public static QueryListModel1 queryListModel = new QueryListModel1();
    public static List<PackageListBean> packageListBeans;
    public static List<PackageListBean> packageListBeanss;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private QueryListItemAdapter1 adapter;
    public int checkNum;

    private ImageView queryIvCamera;

    private int p = 1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2000:
                    queryPackageMore();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        initViews();
        initData();
        queryPackage();
        setListener();
    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.query);

        queryRlAllOut.setVisibility(View.GONE);

        queryLv.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();
        queryEtQuery.setText(intent.getExtras().getString("queryCode"));


        if (!queryEtQuery.getText().toString().isEmpty()) {
            queryIvClear.setImageResource(R.drawable.ic_clear);
            queryEtQuery.setSelection(queryEtQuery.getText().toString().trim().length());
        }
    }


    private void initData() {
        packageListBeans = new ArrayList<>();
        packageListBeanss = new ArrayList<>();
    }

    private void setListener() {

        queryEtQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (queryEtQuery.getText().toString().trim().isEmpty()) {
                    queryIvClear.setImageResource(R.drawable.img_scan);
                } else {
                    queryIvClear.setImageResource(R.drawable.ic_clear);
                    queryEtQuery.setSelection(queryEtQuery.getText().toString().trim().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }


        });


        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                //下拉刷新...
                refresh.finishRefresh();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //上拉刷新...
                p++;
                handler.sendEmptyMessageDelayed(2000,1500);
            }
        });

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
                    queryEtQuery.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(QueryActivity1.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
    @OnClick({R.id.query_iv_clear, R.id.query_iv_search, R.id.query_all_put})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query_iv_clear:
                if (queryEtQuery.getText().toString().length() != 0) {
                    queryIvClear.setImageResource(R.drawable.ic_clear);
                    queryEtQuery.setText("");
                } else {
                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
                }
                break;
            case R.id.query_iv_search:
                queryPackage();
                break;
        }
    }
    //查询包裹列表
    private void queryPackage() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {

            @Override
            public RequestParams onParams() {

                RequestParams params = new RequestParams(ApiConfig.GET_ALL_PACKAGE);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("keywords", queryEtQuery.getText().toString().trim());
                params.addBodyParameter("p", "1");
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        packageListBeans.clear();
                        Gson gson = new Gson();
                        queryListModel = gson.fromJson(object.getJSONObject("data").toString(), QueryListModel1.class);
                        System.out.println(queryListModel.getPackage_list().toString());
                        packageListBeans = queryListModel.getPackage_list();

                        adapter = new QueryListItemAdapter1(QueryActivity1.this, packageListBeans);
                        queryLv.setAdapter(adapter);
                        queryTvSum.setVisibility(View.VISIBLE);
                        queryTvNoOut.setVisibility(View.VISIBLE);
                        queryTvSum.setText("查询到包裹" + queryListModel.getStaticX().getTotal_count() + "个");
                        queryTvNoOut.setText("未出库" + queryListModel.getStaticX().getIn_count() + "个");


                    } else {
//                        Toast.makeText(QueryActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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

    private void queryPackageMore() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {

            @Override
            public RequestParams onParams() {

                RequestParams params = new RequestParams(ApiConfig.QUERY_PACKAGE);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("keywords", queryEtQuery.getText().toString().trim());
                params.addBodyParameter("p", p + "");
                return params;
            }

            @Override
            public void onUsage(String result) {

                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        packageListBeanss.clear();
                        Gson gson = new Gson();
                        queryListModel = gson.fromJson(object.getJSONObject("data").toString(), QueryListModel1.class);
                        System.out.println(queryListModel.getPackage_list().toString());
                        packageListBeanss = queryListModel.getPackage_list();
                        int startIndex = packageListBeans.size();
                        packageListBeans.addAll(packageListBeanss);

                        for (int i = startIndex; i < packageListBeans.size() ; i++) {
                            getIsSelected().put(i, false);
                        }
                        adapter.notifyItemRangeInserted(startIndex, packageListBeanss.size());

                        adapter.notifyDataSetChanged();
                        refresh.finishRefreshLoadMore();
                        System.out.println("页数："+p);


                    } else {
//                        Toast.makeText(QueryActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        refresh.finishRefreshLoadMore();
                        p--;
                        System.out.println("页数："+p);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                queryEtQuery.setText(result);
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
