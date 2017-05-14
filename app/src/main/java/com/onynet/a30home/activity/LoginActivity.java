package com.onynet.a30home.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MainActivity;
import com.onynet.a30home.MainActivity2;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.MainUIUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login_et_account)
    EditText loginEtAccount;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_cb_remember_password)
    CheckBox loginCbRememberPassword;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    private String user, password;
    private boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainUIUtils.setImmersionStatusBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        loginEtAccount.setText(sharedPreferences.getString("user", ""));
        loginEtPassword.setText(sharedPreferences.getString("password", ""));
        loginCbRememberPassword.setChecked(sharedPreferences.getBoolean("checked", false));

        loginEtAccount.setSelection(loginEtAccount.getText().toString().length());

    }

    @OnClick(R.id.login_btn_login)
    public void onClick() {
        login();
    }

    private void login() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_TOKEN);
                params.addBodyParameter("username", loginEtAccount.getText().toString().trim());
                params.addBodyParameter("password", loginEtPassword.getText().toString().trim());
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        if (loginCbRememberPassword.isChecked()) {
                            saveAccount(loginEtAccount.getText().toString(), loginEtPassword.getText().toString(), true);
                        } else {
                            saveAccount("", "", true);
                        }
                        saveToken(object.getJSONObject("data").getString("token"), object.getJSONObject("data").getJSONObject("user").getString("group_type"));

                        if (object.getJSONObject("data").getJSONObject("user").getString("group_type").equals("0")) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, MainActivity2.class));
                        }
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        httpUtils.postSend(this);
    }


    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 3000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                MyApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
