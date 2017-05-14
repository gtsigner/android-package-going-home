package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IScanActivity extends BaseActivity {

    @BindView(R.id.et_scan_result)
    EditText mTvScanResult;
    @BindView(R.id.btn_scan)
    Button btnScan;
    @BindView(R.id.btn_scan_set)
    Button btnScanSet;
    @BindView(R.id.activity_iscan)
    LinearLayout activityIscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iscan);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_scan)
    public void onClick() {

        MaterialDialog dialog = new MaterialDialog.Builder(IScanActivity.this)
                .iconRes(R.drawable.ic_update)
                .title("下载更新")
                .positiveText("更新")
                .negativeText("以后再说")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      switch (which){
                          case POSITIVE:

                              Toast.makeText(IScanActivity.this, "下载更新", Toast.LENGTH_SHORT).show();

                              break;
                          case NEGATIVE:
                              Toast.makeText(IScanActivity.this, "以后再说", Toast.LENGTH_SHORT).show();
                              break;
                      }
                    }
                })
                .progress(false,100,true)
                .content("加载中...")
                .show();

        dialog.setProgress(50);

    }
}
