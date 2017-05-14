package com.onynet.a30home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.ProfileModel;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.activity.LoadingActivity.profileModel;

public class SetActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.set_sw_msg_inform)
    Switch setSwMsgInform;
    @BindView(R.id.set_sw_wechat_inform)
    Switch setSwWechatInform;
    @BindView(R.id.set_et_change_tiem)
    EditText setEtChangeTiem;
    @BindView(R.id.set_btn_change_time)
    Button setBtnChangeTime;
    @BindView(R.id.set_ll_template)
    LinearLayout setLlTemplate;
    @BindView(R.id.set_ll_version_update)
    LinearLayout setLlVersionUpdate;
    @BindView(R.id.set_ll_about)
    LinearLayout setLlAbout;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.set_tv_version_name)
    TextView setTvVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
        initView();
        setListener();
    }

    private void setListener() {
        setSwMsgInform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setProfile(b, setSwWechatInform.isChecked(), setEtChangeTiem.getText().toString().trim());
            }
        });

        setSwWechatInform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setProfile(setSwMsgInform.isChecked(), b, setEtChangeTiem.getText().toString().trim());
            }
        });
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.setting);

        setTvVersionName.setText(LoadingActivity.upDateModel.getVersion_name());

        if (profileModel.getSms_notice_status().equals("1")) {
            setSwMsgInform.setChecked(true);
        } else {
            setSwMsgInform.setChecked(false);
        }
        if (profileModel.getWx_notice_status().equals("1")) {
            setSwWechatInform.setChecked(true);
        } else {
            setSwWechatInform.setChecked(false);
        }

        if (!profileModel.getOpen_date().isEmpty()) {
            setEtChangeTiem.setText(profileModel.getOpen_date());
        }

    }

    @OnClick({R.id.set_btn_change_time, R.id.set_ll_template, R.id.set_ll_version_update, R.id.set_ll_about})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_btn_change_time://营业时间
                setProfile(setSwMsgInform.isChecked(), setSwWechatInform.isChecked(), setEtChangeTiem.getText().toString().trim());
                break;
            case R.id.set_ll_template://设置模版
                startActivity(new Intent(this, SmsTplActivity.class));
                break;
            case R.id.set_ll_version_update://版本更新

                break;
            case R.id.set_ll_about://关于我们
                startActivity(new Intent(SetActivity.this,AboutUsActivity.class)
                        .putExtra("url",ApiConfig.ABOUT_US)
                        .putExtra("title",R.string.about));
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

    private void setProfile(final boolean sms, final boolean wx, final String open_date) {

        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.SET_PROFILE);

                params.addBodyParameter("token", MyApplication.token);
                if (sms) {
                    params.addBodyParameter("sms_notice_status", "1");
                } else {
                    params.addBodyParameter("sms_notice_status", "0");
                }
                if (wx) {
                    params.addBodyParameter("wx_notice_status", "1");
                } else {
                    params.addBodyParameter("wx_notice_status", "0");
                }

                params.addBodyParameter("open_date", open_date);

                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        LoadingActivity.profileModel = new ProfileModel();
                        Gson gson = new Gson();
                        LoadingActivity.profileModel = gson.fromJson(object.getJSONObject("data").toString(), ProfileModel.class);
                    } else {
                        Toast.makeText(SetActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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
