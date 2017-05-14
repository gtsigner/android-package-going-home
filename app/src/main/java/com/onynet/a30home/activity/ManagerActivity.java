package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManagerActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.data_ll_inform)
    LinearLayout dataLlInform;
    @BindView(R.id.data_ll_arrive_station)
    LinearLayout dataLlArriveStation;
    @BindView(R.id.data_ll_reject)
    LinearLayout dataLlReject;
    @BindView(R.id.data_ll_up_goods)
    LinearLayout dataLlUpGoods;
    @BindView(R.id.data_ll_inform_failure)
    LinearLayout dataLlInformFailure;
    @BindView(R.id.data_package_error)
    LinearLayout dataPackageError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.data);
    }

    @OnClick({R.id.data_package_error, R.id.data_ll_inform, R.id.data_ll_arrive_station, R.id.data_ll_reject, R.id.data_ll_up_goods, R.id.data_ll_inform_failure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.data_ll_inform://通知中
                startActivity(new Intent(ManagerActivity.this, InformActivity.class));

                break;
            case R.id.data_ll_arrive_station://以到站
                startActivity(new Intent(ManagerActivity.this, ArriveStationActivity.class));

                break;
            case R.id.data_ll_reject://拒收件
                startActivity(new Intent(ManagerActivity.this, RejectActivity.class));

                break;
            case R.id.data_ll_up_goods://本月以提货
                startActivity(new Intent(ManagerActivity.this, UpGoodsActivity.class));
                break;
            case R.id.data_ll_inform_failure://通知失败
                startActivity(new Intent(ManagerActivity.this, InformFailureActivity.class));
                break;
            case R.id.data_package_error://问题件处理
                startActivity(new Intent(ManagerActivity.this, ErrorActivity.class));
                break;
        }
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
}
