package com.onynet.a30home.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSmsTplActivity extends BaseActivity {

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar_btn_menu)
    Button toolbarBtnMenu;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tmpl_et_msg_tmpl)
    EditText tmplEtMsgTmpl;
    @BindView(R.id.tmpl_tv_express_company)
    TextView tmplTvExpressCompany;
    @BindView(R.id.tmpl_tv_number)
    TextView tmplTvNumber;
    @BindView(R.id.tmpl_tv_location)
    TextView tmplTvLocation;
    @BindView(R.id.tmpl_tv_goods_code)
    TextView tmplTvGoodsCode;
    @BindView(R.id.tmpl_tv_time)
    TextView tmplTvTime;
    @BindView(R.id.tmpl_btn_confirm_add)
    Button tmplBtnConfirmAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sms_tpl);
        ButterKnife.bind(this);

        initView();
        setListener();

    }

    private void setListener() {
        tmplEtMsgTmpl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains("{$company_name}")){
                    tmplTvExpressCompany.setVisibility(View.GONE);
                }else {
                    tmplTvExpressCompany.setVisibility(View.VISIBLE);
                }
                if (s.toString().contains("{$yun_no}")){
                    tmplTvNumber.setVisibility(View.GONE);
                }else {
                    tmplTvNumber.setVisibility(View.VISIBLE);
                }
                if (s.toString().contains("{$address}")){
                    tmplTvLocation.setVisibility(View.GONE);
                }else {
                    tmplTvLocation.setVisibility(View.VISIBLE);
                }
                if (s.toString().contains("{$get_no}")){
                    tmplTvGoodsCode.setVisibility(View.GONE);
                }else {
                    tmplTvGoodsCode.setVisibility(View.VISIBLE);
                }
                if (s.toString().contains("{$open_date}")) {
                    tmplTvTime.setVisibility(View.GONE);
                } else {
                    tmplTvTime.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.add_tmpl);
    }

    @OnClick({R.id.tmpl_tv_express_company, R.id.tmpl_tv_number, R.id.tmpl_tv_location, R.id.tmpl_tv_goods_code, R.id.tmpl_tv_time, R.id.tmpl_btn_confirm_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tmpl_tv_express_company://快递公司
                addTpl("{$company_name}", R.drawable.ic_kuaidi);
                break;
            case R.id.tmpl_tv_number://单号
                addTpl("{$yun_no}", R.drawable.ic_danhao);

                break;
            case R.id.tmpl_tv_location://地址
                addTpl("{$address}", R.drawable.ic_dizi);

                break;
            case R.id.tmpl_tv_goods_code://取货码
                addTpl("{$get_no}", R.drawable.ic_quhuoma);
                break;
            case R.id.tmpl_tv_time://时间
                addTpl("{$open_date}", R.drawable.ic_shijian);

                break;
            case R.id.tmpl_btn_confirm_add:
                System.out.println("短信模版:"+tmplEtMsgTmpl.getText().toString());
                addSmsTpl();
                break;
        }
    }

    private void addSmsTpl() {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {

                RequestParams params = new RequestParams(ApiConfig.ADD_TPL);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("tpl_content", tmplEtMsgTmpl.getText().toString());
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(AddSmsTplActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddSmsTplActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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

    private void addTpl(String s, int res) {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res);
            ImageSpan imageSpan = new ImageSpan(this, bitmap);
            SpannableString spannableString = new SpannableString(s);
            spannableString.setSpan(imageSpan, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            int index = tmplEtMsgTmpl.getSelectionStart();
            // 获取光标所在位置
            Editable edit_text = tmplEtMsgTmpl.getEditableText();
            if (index < 0 || index >= edit_text.length()) {
                edit_text.append(spannableString);
            } else {
                edit_text.insert(index, spannableString);
            }
//            tmplEtMsgTmpl.append(spannableString);
        } catch (Exception e) {
            e.printStackTrace();
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
