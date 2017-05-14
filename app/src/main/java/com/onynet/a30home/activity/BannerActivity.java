package com.onynet.a30home.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BannerActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.infoWebView)
    WebView infoWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        ButterKnife.bind(this);

        System.out.println("Url地址:"+getIntent().getExtras().getString("url"));


        initViews();
    }

    private void initViews() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText("详情");
        infoWebView.loadUrl(getIntent().getExtras().getString("url"));
        //不使用缓存：
        infoWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //b:加上这两句基本上就可以做到屏幕适配了
        infoWebView.getSettings().setUseWideViewPort(true);
        infoWebView.getSettings().setLoadWithOverviewMode(true);
        //设置html页面定位的支持
        infoWebView.getSettings().setGeolocationEnabled(true);
        //添加对js功能的支持
        infoWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        infoWebView.getSettings().setBuiltInZoomControls(true);

        //若要在当前activity的webview中加载url,
        // 而不是跳转到手机别的浏览器中，
        // 可以在代码中添加如下代码：
        infoWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 设置webview页面时设置后退时，回退到上一web页面。
        if (keyCode == KeyEvent.KEYCODE_BACK && infoWebView.canGoBack()) {
            infoWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
