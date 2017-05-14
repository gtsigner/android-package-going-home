package com.onynet.a30home.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.PackageTypeModel;
import com.onynet.a30home.model.SenderListMoadel;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.StringUtils;
import com.onynet.a30home.utils.ToastUtils;
import com.onynet.a30home.zxing.activity.CaptureActivity;
import com.onynet.a30home.zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.MainActivity.REQUEST_CODE;
import static com.onynet.a30home.MyApplication.UpGoodsCode;
import static com.onynet.a30home.MyApplication.controll;
import static com.onynet.a30home.MyApplication.token;

public class PutActivity extends BaseActivity {

    private static final String TAG = "PutActivity";

    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.put_tv_msg_sum)
    TextView putTvMsgSum;
    @BindView(R.id.put_et_theAwb)
    EditText putEtTheAwb;
    @BindView(R.id.put_iv_scan)
    ImageView putIvScan;
    @BindView(R.id.put_tv_express_company_name)
    TextView putTvExpressCompanyName;
    @BindView(R.id.put_ll_express_company)
    LinearLayout putLlExpressCompany;
    @BindView(R.id.put_et_name)
    EditText putEtName;
    @BindView(R.id.put_ll_goods_flag)
    LinearLayout putLlGoodsFlag;
    @BindView(R.id.put_tv_package_type)
    TextView putTvPackageType;
    @BindView(R.id.put_et_package_type)
    EditText putEtPackageType;
    @BindView(R.id.put_et_up_goods_code)
    EditText putEtUpGoodsCode;
    @BindView(R.id.put_btn_reset)
    Button putBtnReset;
    @BindView(R.id.put_btn_submit)
    Button putBtnSubmit;
    @BindView(R.id.put_btn_unifies_send_msg)
    Button putBtnUnifiesSendMsg;
    @BindView(R.id.put_et_building)
    EditText putEtBuilding;
    @BindView(R.id.put_et_home_number)
    EditText putEtHomeNumber;
    public static List<PackageTypeModel> packageTypeModels;
    @BindView(R.id.put_act_number)
    AutoCompleteTextView putActNumber;
    @BindView(R.id.put_iv_number)
    ImageView putIvNumber;
    @BindView(R.id.put_iv_name)
    ImageView putIvName;

    private String[] packageTypes;

    private RadioOnClick radioOnClick = new RadioOnClick(0);
    private ListView listView;

    private String company_id;//快递公司
    private String package_type_id;//包裹类型
    private String date;

    private List<SenderListMoadel> senderListMoadels;
    private List<String> phones;
    private ArrayAdapter<String> adapter;
    private Map<String, SenderListMoadel> mapType;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put);
        ButterKnife.bind(this);
        initData();
        getExpressNames();
        getSmsList();
        initViews();
        setListener();
    }

    private void setListener() {
        putEtTheAwb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(putEtTheAwb.getText().toString().trim())) {
                    putIvScan.setImageDrawable(getResources().getDrawable(R.drawable.img_scan));
                } else {
                    putIvScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear));
                }
            }
        });

        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                putEtTheAwb.setText(result);
            }
        });

        putActNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 6) {
                    searchSender();
                    System.out.println("自动匹配");
                }

                if (s.toString().length() > 0) {
                    putIvNumber.setVisibility(View.VISIBLE);
                } else {
                    putIvNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().length() == 11) {
                    if (mapType != null) {
                        if (mapType.get(s.toString()) != null) {
                            //判断是否时疑难用户
                            if (mapType.get(s.toString()).getSender_type().equals("1")) {
                                new MaterialDialog.Builder(PutActivity.this)
                                        .title(R.string.TIPS)
                                        .content(R.string.difficult_user)
                                        .positiveText("否")
                                        .negativeText("是")
                                        .onAny(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                switch (which) {
                                                    case POSITIVE:
                                                        reset();
                                                        break;
                                                }
                                            }
                                        })
                                        .show();

                            }
                            putEtName.setText(mapType.get(s.toString()).getName());
                            String address = mapType.get(s.toString()).getAddress();
                            if (address.contains("-")) {
                                String building = address.substring(0, address.indexOf("-"));
                                String homenuber = address.substring(address.indexOf("-") + 1, address.length());
                                putEtBuilding.setText(building);
                                putEtHomeNumber.setText(homenuber);
                            }
                        } else {
                            putEtName.setText("");
                            putEtBuilding.setText("");
                            putEtHomeNumber.setText("");
                        }
                    }
                }
            }
        });

        putEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    putIvName.setVisibility(View.VISIBLE);
                } else {
                    putIvName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    private void initData() {
        packageTypeModels = new ArrayList<>();
        senderListMoadels = new ArrayList<>();
        phones = new ArrayList<>();
        mapType = new HashMap<>();


        //从本地得到取货码
        SharedPreferences sharedPreferences = getSharedPreferences("UpGoodsCode", MODE_PRIVATE);
        MyApplication.date = sharedPreferences.getString("date", "");
        MyApplication.UpGoodsCode = sharedPreferences.getLong("UpGoodsCode", 0);
        System.out.println(MyApplication.UpGoodsCode);


    }

    private void initViews() {

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.package_put);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(new Date());

        System.out.println(date);
        System.out.println(MyApplication.date);

        //如果取货码的日期是今天的日期就设置取货码为本地获得的,否则自己定义
        if (date.equals(MyApplication.date)) {
            UpGoodsCode++;
            putEtUpGoodsCode.setText(String.valueOf(UpGoodsCode));
        } else {
            putEtUpGoodsCode.setText("");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2000) {
            if (data != null) {
                putTvExpressCompanyName.setText(data.getExtras().getString("name"));
                company_id = data.getExtras().getString("company_id");
            }
        }
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    putEtTheAwb.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(PutActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @OnClick({R.id.put_btn_unifies_send_msg, R.id.put_iv_scan, R.id.put_ll_express_company,
            R.id.put_ll_goods_flag, R.id.put_btn_reset, R.id.put_btn_submit, R.id.put_iv_number, R.id.put_iv_name})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.put_iv_scan:

                if (TextUtils.isEmpty(putEtTheAwb.getText().toString().trim())) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
                } else {
                    putEtTheAwb.setText("");
                }

                break;
            case R.id.put_ll_express_company://选择快递公司

                Intent intent = new Intent(PutActivity.this, ExpressCompanyActivity.class);
                startActivityForResult(intent, 2000);

                break;
            case R.id.put_ll_goods_flag://选择包裹类型

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle(R.string.selected_package_type)
                        .setSingleChoiceItems(packageTypes, radioOnClick.getIndex(), radioOnClick).create();
                listView = dialog.getListView();
                dialog.show();

                break;
            case R.id.put_btn_reset:
                reset();
                break;
            case R.id.put_btn_submit:
                //验证手机号格式
                if (StringUtils.isMobileNO(putActNumber.getText().toString())) {
                    push();
                } else {
                    putActNumber.setError("手机号格式错误");
                }

                break;
            case R.id.put_btn_unifies_send_msg:
                senSms();
                break;

            case R.id.put_iv_number:
                putActNumber.setText("");
                break;
            case R.id.put_iv_name:
                putEtName.setText("");
                break;
        }
    }


    class RadioOnClick implements DialogInterface.OnClickListener {
        private int index;

        RadioOnClick(int index) {
            this.index = index;
        }

        void setIndex(int index) {
            this.index = index;
        }

        int getIndex() {
            return index;
        }


        @Override
        public void onClick(DialogInterface dialog, int i) {
            setIndex(i);
            putTvPackageType.setText(packageTypes[i]);//设置包裹类型显示
            package_type_id = packageTypeModels.get(i).getId();//得到包裹类型id
            dialog.dismiss();
        }

    }

    @Override
    protected String setVoicePrompt() {
        return getString(R.string.put_success);
    }

    /**
     * 重置表单
     */
    private void reset() {
        putEtTheAwb.setText("");
        putEtBuilding.setText("");
        putEtHomeNumber.setText("");
        putActNumber.setText("");
        putEtName.setText("");
        putEtPackageType.setText("");
    }


    /**
     * 包裹入库
     */
    private void push() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.PUSH_PACKAGE);
                params.addBodyParameter("token", token); //
                params.addBodyParameter("yun_no", putEtTheAwb.getText().toString().trim()); //运单号
                params.addBodyParameter("company_id", company_id); //公司ID
                params.addBodyParameter("get_address", putEtBuilding.getText().toString().trim() + "-" + putEtHomeNumber.getText().toString().trim()); //获取地址
                params.addBodyParameter("get_phone", putActNumber.getText().toString()); //手机号
                params.addBodyParameter("get_name", putEtName.getText().toString().trim()); //姓名
                params.addBodyParameter("get_no", putEtUpGoodsCode.getText().toString()); //取单号

                //判断包裹类型是不是手动输入的
                if (TextUtils.isEmpty(putEtPackageType.getText().toString().trim())) {
                    params.addBodyParameter("package_type_id", package_type_id); //包裹类型
                } else {
                    params.addBodyParameter("package_type_text", putEtPackageType.getText().toString().trim()); //文字，如果id不传这项就必须
                }
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        saveGoodsCode(Long.valueOf(putEtUpGoodsCode.getText().toString()), date);
                        UpGoodsCode++;//提货码+1
                        putEtUpGoodsCode.setText(String.valueOf(UpGoodsCode));
                        reset();//重置表单
                        ToastUtils.getToastUtils().ToastShowMsg(PutActivity.this, 0, "入库成功");
                        //播放提示音...
//                        initTextToSpeech();
                        initBeepSound();
                        playBeepSoundAndVibrate();
                        getSmsList();
                    } else {
                        Toast.makeText(PutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "包裹入库请求出错: " + error);
            }
        });

        httpUtils.postSend(this);
    }

    @Override
    protected int setBeepSoundRes() {
        return R.raw.put;
    }

    /**
     * 获得包裹类型
     */
    private void getExpressNames() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_PACKAGE_TYPES);
                params.addBodyParameter("token", token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        packageTypeModels.clear();
                        PackageTypeModel packageTypeModel = new PackageTypeModel();
                        Gson gson = new Gson();

                        JSONArray array = object.getJSONArray("data");

                        packageTypes = new String[array.length()];

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject item = array.getJSONObject(i);
                            packageTypeModel = gson.fromJson(item.toString(), PackageTypeModel.class);
                            packageTypeModels.add(packageTypeModel);
                            packageTypes[i] = packageTypeModel.getName();
                        }

                        package_type_id = packageTypeModels.get(0).getId();//默认为第一个

                    } else {
                        Toast.makeText(PutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "包裹类型请求出错: " + error);
            }
        });

        httpUtils.postSend(this);
    }

    /**
     * 获取待发短信包裹数量
     */
    private void getSmsList() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_SMS_LIST);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        String sms_count = object.getJSONObject("data").getString("sms_count");
                        putTvMsgSum.setText(sms_count);
                    } else {
                        Log.e(TAG, "onUsage: " + object.getString("msg"));
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

    //统一发送短信
    private void senSms() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.SEND_SMS);
                params.addBodyParameter("token", MyApplication.token);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(PutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        putTvMsgSum.setText("0");
                    } else {
                        Toast.makeText(PutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        getSmsList();
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

    /**
     * 自动匹配手机号
     */
    private void searchSender() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.SEARCH_SENDER);
                params.addBodyParameter("token", token);
                params.addBodyParameter("phone", putActNumber.getText().toString().trim());
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        senderListMoadels.clear();
                        phones.clear();
                        SenderListMoadel senderListMoadel = new SenderListMoadel();
                        String phone = "";
                        Gson gson = new Gson();
                        JSONArray array = object.getJSONObject("data").getJSONArray("sender_list");
                        for (int i = 0; i < array.length(); i++) {
                            senderListMoadel = gson.fromJson(array.getJSONObject(i).toString(), SenderListMoadel.class);
                            phone = senderListMoadel.getPhone();
                            senderListMoadels.add(senderListMoadel);
                            phones.add(phone);
                            mapType.put(phone, senderListMoadel);
                        }
                        adapter = new ArrayAdapter<String>(PutActivity.this, android.R.layout.simple_list_item_1, phones);
                        putActNumber.setAdapter(adapter);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 139 || keyCode == 140 || keyCode == 141) {
            controll.scan_stop();
            controll.scan_start();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onResume() {
        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                putEtTheAwb.setText(result);
            }
        });
        super.onResume();

    }

    @Override
    protected void onPause() {
        ((MyApplication) getApplication()).setOnScanResultListener(null);
        super.onPause();
    }


}