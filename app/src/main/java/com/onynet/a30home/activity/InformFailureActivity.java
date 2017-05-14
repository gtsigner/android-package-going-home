package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.InformFailureListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InformFailureActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.informFailure_cb_select_all)
    CheckBox informFailureCbSelectAll;
    @BindView(R.id.informFailure_tv_resend)
    TextView informFailureTvResend;
    @BindView(R.id.informFailure_lv)
    ListView informFailureLv;
    @BindView(R.id.activity_inform_failure)
    LinearLayout activityInformFailure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform_failure);
        ButterKnife.bind(this);

        initViews();

    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.inform_failure);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("");

        }
        informFailureLv.setAdapter(new InformFailureListItemAdapter(this, strings));

    }

    @OnClick(R.id.informFailure_tv_resend)
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


}
