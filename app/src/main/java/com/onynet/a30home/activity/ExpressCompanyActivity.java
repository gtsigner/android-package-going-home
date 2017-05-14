package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;
import com.onynet.a30home.adapter.ExpressCompanyListItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.onynet.a30home.activity.LoadingActivity.expressModels;

public class ExpressCompanyActivity extends BaseActivity {

    private static final String TAG = "ExpressCompanyActivity";

    @BindView(R.id.expressCompany_lv)
    ListView expressCompanyLv;
    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ExpressCompanyListItemAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_company);
        ButterKnife.bind(this);
        initData();
        initViews();
        setListener();

    }

    private void setListener() {
        expressCompanyLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("name", expressModels.get(i).getName());
                intent.putExtra("company_id", expressModels.get(i).getId());
                setResult(2000,intent);
                finish();
            }
        });
    }

    private void initData() {
        adapter = new ExpressCompanyListItemAdapter(this, expressModels);
    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.selected_express_company);

        expressCompanyLv.setAdapter(adapter);
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
