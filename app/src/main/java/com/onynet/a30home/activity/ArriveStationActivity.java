package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.RejectListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArriveStationActivity extends BaseActivity {


    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.reject_tv_date)
    TextView rejectTvDate;
    @BindView(R.id.reject_tv_reject_sum)
    TextView rejectTvRejectSum;
    @BindView(R.id.reject_tv_stat)
    TextView rejectTvStat;
    @BindView(R.id.reject_iv_search)
    ImageView rejectIvSearch;
    @BindView(R.id.reject_lv)
    ListView rejectLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject);
        ButterKnife.bind(this);

        initViews();

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.arrive_station);

        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("");

        }
        rejectLv.setAdapter(new RejectListItemAdapter(this, strings));

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


    @OnClick({R.id.reject_tv_date, R.id.reject_tv_stat, R.id.reject_iv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reject_tv_date:
                break;
            case R.id.reject_tv_stat:
                break;
            case R.id.reject_iv_search:
                break;
        }
    }
}
