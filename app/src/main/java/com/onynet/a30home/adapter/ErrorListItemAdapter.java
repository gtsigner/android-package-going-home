package com.onynet.a30home.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.ExpressModel;
import com.onynet.a30home.model.PackageModel;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.onynet.a30home.activity.LoadingActivity.expressModels;


/**
 * 时 间: 2017/1/8 0008
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class ErrorListItemAdapter extends RecyclerView.Adapter<ErrorListItemAdapter.MyViewHolder> {

    private Context context;
    private List<PackageModel> packageModels = new ArrayList<>();

    /*选择过后的快递公司*/
    private ExpressModel expressModel = null;

    public ErrorListItemAdapter(Context context, List<PackageModel> packageModels) {
        this.context = context;
        this.packageModels = packageModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.error_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.errorTvExpressCompanyName.setText(packageModels.get(position).getCompany_name());
        holder.errorTvNumber.setText(packageModels.get(position).getGet_phone());
        holder.errorTvName.setText(packageModels.get(position).getGet_name());
        holder.errorTvUpGoodsCode.setText(packageModels.get(position).getGet_no());

        holder.errorLlExpressCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v, holder.errorTvExpressCompanyName);
            }
        });

        //退件
        holder.errorBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePackage("1", holder, position);
            }
        });
        //编辑
        holder.errorBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePackage("0", holder, position);
            }
        });
        //打电话
        holder.errorIvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call1 (holder.errorTvNumber.getText().toString().trim());
            }
        });
        switch (packageModels.get(position).getStatus()) {
            case "0"://到站
                holder.errorTvStatus.setText(R.string.YiDaoZhan);
                holder.errorTvStatus.setTextColor(context.getResources().getColor(R.color.primary1));
                break;
            case "2"://移库
                holder.errorTvStatus.setText(R.string.YiYiKu);
                holder.errorTvStatus.setTextColor(context.getResources().getColor(R.color.primary2));

                break;
            case "4"://退件
                holder.errorTvStatus.setText(R.string.YiTuiJian);
                holder.errorTvStatus.setTextColor(context.getResources().getColor(R.color.primary3));
                break;
            case "5"://出库
                holder.errorTvStatus.setText(R.string.YiChuKu);
                holder.errorTvStatus.setTextColor(context.getResources().getColor(R.color.primary4));
                break;
        }


    }

    @Override
    public int getItemCount() {
        return packageModels.size();
    }

    /**
     * 包裹移库
     */
    private void handlePackage(final String action_type, final MyViewHolder holder, final int position) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.HANDLE_PACKAGE);
                if (action_type.equals("1")) {
                    params.addBodyParameter("token", MyApplication.token);
                    params.addBodyParameter("action_type", action_type);
                    params.addBodyParameter("id", packageModels.get(position).getId());
                } else {
                    params.addBodyParameter("token", MyApplication.token);
                    params.addBodyParameter("action_type", action_type);
                    params.addBodyParameter("id", packageModels.get(position).getId());
                    //当选择的快的公司为空时传以前的id
                    if (expressModel == null) {
                        params.addBodyParameter("company_id", packageModels.get(position).getCompany_id());
                    } else {
                        params.addBodyParameter("company_id", expressModel.getId());
                    }
                    params.addBodyParameter("get_phone", holder.errorTvNumber.getText().toString().trim());
                    params.addBodyParameter("get_name", holder.errorTvName.getText().toString().trim());
                    params.addBodyParameter("get_no", holder.errorTvUpGoodsCode.getText().toString().trim());
                }
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (onHandelPackageListener != null) {
                            onHandelPackageListener.onSuccess();
                        }
                    } else {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });

        httpUtils.postSend(context);
    }


    public void showDialog(View view, final TextView textView) {

        List<String> expressNames = new ArrayList<>();
        for (int i = 0; i < expressModels.size(); i++) {
            expressNames.add(expressModels.get(i).getName());
        }

        final BottomSheetDialog dialog = new BottomSheetDialog(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.list_bottom, null);
        ListView listView = (ListView) dialogView.findViewById(R.id.listview);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, expressNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expressModel = expressModels.get(position);
                textView.setText(expressModel.getName());
                dialog.dismiss();
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout errorLlExpressCompany;
        private TextView errorTvExpressCompanyName;
        private TextView errorTvStatus;
        private TextView errorTvNumber;
        private ImageView errorIvCall;
        private EditText errorTvName;
        private EditText errorTvUpGoodsCode;
        private Button errorBtnBack;
        private Button errorBtnSave;

        MyViewHolder(View view) {
            super(view);
            errorLlExpressCompany = (LinearLayout) view.findViewById(R.id.error_ll_express_company);
            errorTvExpressCompanyName = (TextView) view.findViewById(R.id.error_tv_company_name);
            errorTvStatus = (TextView) view.findViewById(R.id.error_tv_status);
            errorTvNumber = (TextView) view.findViewById(R.id.error_tv_number);
            errorIvCall = (ImageView) view.findViewById(R.id.error_iv_call);
            errorTvName = (EditText) view.findViewById(R.id.error_tv_name);
            errorTvUpGoodsCode = (EditText) view.findViewById(R.id.error_tv_up_goods_code);
            errorBtnBack = (Button) view.findViewById(R.id.error_btn_back);
            errorBtnSave = (Button) view.findViewById(R.id.error_btn_save);
        }
    }

    /**
     * 操作成功调用的接口刷新界面
     */
    public interface OnHandelPackageListener {
        void onSuccess();
    }

    private OnHandelPackageListener onHandelPackageListener;

    public void setOnHandelPackageListener(OnHandelPackageListener onHandelPackageListener) {
        this.onHandelPackageListener = onHandelPackageListener;
    }


    /**
     * 调用拨号功能
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call1(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

