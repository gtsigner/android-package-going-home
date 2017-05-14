package com.onynet.a30home.activity;

import android.os.Bundle;
import android.view.View;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.onynet.a30home.R.id.photoview;

public class ImgActivity extends BaseActivity {

    @BindView(photoview)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);
        ButterKnife.bind(this);


        // 启用图片缩放功能
        photoView.enable();

        Glide.with(this)
                .load(getIntent().getStringExtra("img_path"))
                .crossFade()//淡出效果
                .diskCacheStrategy(DiskCacheStrategy.ALL)//开启缓存
                .into(photoView);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
        super.onBackPressed();
    }
}
