package com.onynet.a30home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.PackageModel;
import com.onynet.a30home.utils.HttpUtils;
import com.onynet.a30home.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


/**
 * 时 间: 2017/1/8 0008
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class MoveListItemAdapter extends RecyclerView.Adapter<MoveListItemAdapter.MyViewHolder> {

    private Context context;
    private List<PackageModel> packageModels = new ArrayList<>();

    public MoveListItemAdapter(Context context, List<PackageModel> packageModels) {
        this.context = context;
        this.packageModels = packageModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.move_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.moveTvExpressCompanyName.setText(packageModels.get(position).getCompany_name());
        holder.moveTvNumber.setText(packageModels.get(position).getGet_phone());
        holder.moveTvName.setText(packageModels.get(position).getGet_name());
        holder.moveTvUpGoodsCode.setText(packageModels.get(position).getGet_no());
        holder.movePushTime.setText(TimeUtils.timeToString("yyyy-MM-dd hh:mm", Long.valueOf(packageModels.get(position).getPush_time())));
        if (packageModels.get(position).getStatus().equals("2")){
            holder.moveTvStatus.setText("已移库");
            holder.moveBtnMove.setText("再次移库");
        }else {
            holder.moveTvStatus.setText("");
            holder.moveBtnMove.setText(R.string.move);
        }

        holder.moveBtnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMovePackageListener.onVoice();
                movePackage(packageModels.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return packageModels.size();
    }

    /**
     * 包裹移库
     */
    private void movePackage(final String id) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.MOVE_PACKAGE);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("id", id);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        onMovePackageListener.onSuccess();
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


    class MyViewHolder extends RecyclerView.ViewHolder {
        private EditText moveEtScan;
        private ImageView moveIvScan;
        private TextView moveTvExpressCompanyName;
        private TextView moveTvStatus;
        private TextView moveTvNumber;
        private TextView moveTvName;
        private TextView moveTvUpGoodsCode;
        private TextView movePushTime;
        private Button moveBtnMove;

        public MyViewHolder(View view) {
            super(view);
            moveEtScan = (EditText) view.findViewById(R.id.move_et_scan);
            moveIvScan = (ImageView) view.findViewById(R.id.move_iv_scan);
            moveTvExpressCompanyName = (TextView) view.findViewById(R.id.move_tv_express_company_name);
            moveTvStatus = (TextView) view.findViewById(R.id.move_tv_status);
            moveTvNumber = (TextView) view.findViewById(R.id.move_tv_number);
            moveTvName = (TextView) view.findViewById(R.id.move_tv_name);
            moveTvUpGoodsCode = (TextView) view.findViewById(R.id.move_tv_up_goods_code);
            movePushTime = (TextView) view.findViewById(R.id.move_push_time);
            moveBtnMove = (Button) view.findViewById(R.id.move_btn_move);
        }
    }

    /**
     * 操作成功调用的接口刷新界面
     */
    public interface OnMovePackageListener{
        void onSuccess();
        void onVoice();
    }

    private MoveListItemAdapter.OnMovePackageListener onMovePackageListener;

    public void setOnMovePackageListener(MoveListItemAdapter.OnMovePackageListener onMovePackageListener) {
        this.onMovePackageListener = onMovePackageListener;
    }

}

