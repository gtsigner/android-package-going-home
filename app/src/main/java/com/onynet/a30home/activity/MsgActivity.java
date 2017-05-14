package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MsgListItemAdapter;
import com.onynet.a30home.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.onynet.a30home.MainActivity.msgListModels;

public class MsgActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.msg_lv)
    RecyclerView msgLv;


    private MsgListItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < msgListModels.size(); i++) {
            System.out.println(msgListModels.get(i).getTitle());
        }
        adapter = new MsgListItemAdapter(msgListModels,this);
        msgLv.setAdapter(adapter);
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.msg_center);

        msgLv.setLayoutManager(new LinearLayoutManager(this));
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
