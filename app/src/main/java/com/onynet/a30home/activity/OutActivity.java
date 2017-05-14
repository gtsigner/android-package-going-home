package com.onynet.a30home.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.onynet.a30home.BaseActivity;
import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.PackageModel;
import com.onynet.a30home.utils.BitmapUtils;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.ToastUtils;
import com.onynet.a30home.zxing.activity.CaptureActivity;
import com.onynet.a30home.zxing.activity.CodeUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.onynet.a30home.MyApplication.controll;

public class OutActivity extends BaseActivity {
    private static final String TAG = "OutActivity";
    @BindView(R.id.toolbar_tv_title)
    TextView toolbarTvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.out_iv_camera)
    ImageView outIvCamera;
    @BindView(R.id.out_iv_picture)
    ImageView outIvPicture;
    @BindView(R.id.out_et_theAwb)
    EditText outEtTheAwb;
    @BindView(R.id.out_iv_scan)
    ImageView outIvScan;
    @BindView(R.id.out_btn_confirm_out)
    Button outBtnConfirmOut;
    @BindView(R.id.out_rl_camera)
    RelativeLayout out_rl_camera;

    private static int REQUEST_THUMBNAIL = 1;// 请求缩略图信号标识
    @BindView(R.id.out_spinner)
    Spinner outSpinner;
    @BindView(R.id.out_ll_spinner)
    LinearLayout outLlSpinner;
    @BindView(R.id.out_cb_quick)
    CheckBox outCbQuick;
    private String picPath;//图片存储路径

    private boolean isPhoto = false;


    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;
    /**
     * 快递位置
     */
    private int position = 0;

    public List<PackageModel> packageModels = new ArrayList<>();

    private ArrayAdapter<String> adapter;
    List<String> data_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        picPath = this.getExternalCacheDir() + "/30KD.png";
        System.out.println("图片路径:" + picPath);
        ButterKnife.bind(this);
        initView();
        initData();
        setListener();

    }

    private void initData() {
        //数据
        data_list = new ArrayList<String>();
        //适配器
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        outSpinner.setAdapter(adapter);
    }

    private void setListener() {
        outEtTheAwb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(outEtTheAwb.getText().toString().trim())) {
                    outIvScan.setImageDrawable(getResources().getDrawable(R.drawable.img_scan));
                } else {
                    outIvScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear));
                    outEtTheAwb.setSelection(outEtTheAwb.getText().toString().trim().length());

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                outLlSpinner.setVisibility(View.GONE);
                getPackage();
            }
        });

        ((MyApplication) getApplication()).setOnScanResultListener(new MyApplication.onScanResultListener() {
            @Override
            public void onResult(String result) {
                outEtTheAwb.setText(result);
            }
        });


        outSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OutActivity.this.position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initView() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTvTitle.setText(R.string.package_out);


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

    @OnClick({R.id.out_rl_camera, R.id.out_iv_scan, R.id.out_btn_confirm_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.out_rl_camera:

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = Uri.fromFile(new File(picPath));
                //为拍摄的图片指定一个存储的路径
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                //启动相机
                startActivityForResult(intent, REQUEST_THUMBNAIL);

                break;
            case R.id.out_iv_scan:
                if (TextUtils.isEmpty(outEtTheAwb.getText().toString().trim())) {
                    startActivityForResult(new Intent(this, CaptureActivity.class), REQUEST_CODE);
                } else {
                    outEtTheAwb.setText("");
                }
                break;
            case R.id.out_btn_confirm_out:
                if (!outEtTheAwb.getText().toString().trim().isEmpty()) {
                    if (packageModels.size() >= 1) {
                        out(position);
                    } else {
                        ToastUtils.getToastUtils().showToast(OutActivity.this, "包裹已出库或未找到");
                    }
                } else {
                    Toast.makeText(this, "运单号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_THUMBNAIL) {
                /**
                 * 这种方法是通过内存卡的路径进行读取图片，所以的到的图片是拍摄的原图
                 */

                Bitmap bitmap = null;
                bitmap = BitmapUtils.compressBitmap(picPath, 800, 800);
                outIvPicture.setImageBitmap(bitmap);
                isPhoto = BitmapUtils.saveMyBitmap(OutActivity.this, "30KD.png", bitmap);
            } else if (requestCode == REQUEST_CODE) {
                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
//                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        outEtTheAwb.setText(result);
                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(OutActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }


    }


    /**
     * 获取包裹信息
     */
    private void getPackage() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.GET_PACKAGE);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("yun_no", outEtTheAwb.getText().toString().trim());
                params.addBodyParameter("filter", "1");
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        packageModels.clear();
                        data_list.clear();
                        Gson gson = new Gson();
                        PackageModel packageModel = new PackageModel();
                        List<String> strings = new ArrayList<String>();

                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            packageModel = gson.fromJson(array.getJSONObject(i).toString(), PackageModel.class);
                            data_list.add(packageModel.getCompany_name());
                            packageModels.add(packageModel);
                        }

                        if (data_list.size() > 1) {
                            outLlSpinner.setVisibility(View.VISIBLE);
                        } else {
                            outLlSpinner.setVisibility(View.GONE);
                            if (outCbQuick.isChecked()) {
                                out(position);
                            }
                        }

                        adapter.notifyDataSetChanged();

                    } else {
                        System.out.println("错误信息: " + object.getString("msg"));
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
     * 包裹出库
     */
    private void out(final int position) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.POP_PACKAGE);
                params.addBodyParameter("token", MyApplication.token); //
                params.addBodyParameter("id", packageModels.get(position).getId()); //运单号
                if (isPhoto) {
                    params.addBodyParameter("out_img", new File(picPath));
                }
                return params;
            }

            @Override
            public void onUsage(String result) {
                System.out.println(result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        isPhoto = false;
                        outEtTheAwb.setText("");
                        outIvPicture.setImageResource(0);
                        Toast.makeText(OutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        packageModels.clear();
                        data_list.clear();
                        OutActivity.this.position = 0;
                        adapter.notifyDataSetChanged();
                        //提示音
//                        initTextToSpeech();
                        initBeepSound();
                        playBeepSoundAndVibrate();

                    } else {
                        Toast.makeText(OutActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.e(TAG, "包裹出库请求出错: " + error);
            }
        });

        httpUtils.postSend(this);
    }

    @Override
    protected String setVoicePrompt() {
        return getString(R.string.out_success);
    }

    @Override
    protected int setBeepSoundRes() {
        return R.raw.out;
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
                outEtTheAwb.setText(result);
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
