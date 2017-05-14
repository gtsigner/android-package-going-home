package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.SmsTplListItemAdapter;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.SmsTplListModel;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SmsTplActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.Sms_lv)
    RecyclerView SmsLv;

    private List<SmsTplListModel> smsTplListModels;
    private SmsTplListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_tpl);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();
        getSmsTplList();
    }

    private void setListener() {
        adapter.setTplHandleListener(new SmsTplListItemAdapter.TplHandleListener() {
            @Override
            public void onSetDefault() {
                getSmsTplList();
            }

            @Override
            public void onDelTpl() {
                getSmsTplList();
            }
        });
    }

    private void initData() {
        smsTplListModels = new ArrayList<>();
        adapter = new SmsTplListItemAdapter(SmsTplActivity.this, smsTplListModels);
        SmsLv.setAdapter(adapter);
    }


    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.set_sms_tpl);
        toolbarBtnMenu.setText(R.string.add);
        SmsLv.setLayoutManager(new LinearLayoutManager(this));

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


    @OnClick(R.id.toolbar_btn_menu)
    public void onClick() {
        startActivity(new Intent(this, AddSmsTplActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSmsTplList();
    }

    public void getSmsTplList() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_TPL_LIST);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        smsTplListModels.clear();
                        Gson gson = new Gson();
                        SmsTplListModel tplListModel = new SmsTplListModel();
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            tplListModel = gson.fromJson(array.getJSONObject(i).toString(), SmsTplListModel.class);
                            smsTplListModels.add(tplListModel);
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(SmsTplActivity.this, object.getString("sms"), Toast.LENGTH_SHORT).show();
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
