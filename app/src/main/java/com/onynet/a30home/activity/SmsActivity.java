package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.SmsListItemAdapter;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.SmsListModel;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsActivity extends BaseActivity {


    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sms_lv)
    RecyclerView smsLv;
    @BindView(R.id.activity_sms)
    LinearLayout activitySms;

    private SmsListItemAdapter adapter;
    private SmsListModel smsListModel;
    private List<SmsListModel.PoolsBean> poolsBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ButterKnife.bind(this);
        initViews();
        initData();
        getSms();

    }

    private void initData() {
        poolsBeans = new ArrayList<>();

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.sms);
        toolbarBtnMenu.setText(R.string.top_up);

        smsLv.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick(R.id.toolbar_btn_menu)
    public void onClick() {

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

    private void getSms() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_POOL);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code")==200){
                        Gson gson = new Gson();
                        smsListModel = gson.fromJson(object.getJSONObject("data").toString(),SmsListModel.class);
                        poolsBeans = smsListModel.getPools();
                        adapter = new SmsListItemAdapter(SmsActivity.this,poolsBeans);
                        smsLv.setAdapter(adapter);
                    }else {
                        Toast.makeText(SmsActivity.this, object.getString("sms"), Toast.LENGTH_SHORT).show();
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


}
