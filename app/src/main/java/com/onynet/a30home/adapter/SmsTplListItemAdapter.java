package com.onynet.a30home.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onynet.a30home.MyApplication;
import com.onynet.a30home.R;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.SmsTplListModel;
import com.onynet.a30home.utils.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 时 间: 2016/12/30 0030
 * 作 者: 郑亮
 * Q  Q : 1023007219
 */

public class SmsTplListItemAdapter extends RecyclerView.Adapter<SmsTplListItemAdapter.TplViewHolder> {

    private List<SmsTplListModel> msgTplListModels = new ArrayList<>();
    private Context context;

    public SmsTplListItemAdapter(Context context, List<SmsTplListModel> msgTplListModels) {
        this.context = context;
        this.msgTplListModels = msgTplListModels;

    }


    @Override
    public TplViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sms_tmpl_list_item, parent, false);
        return new TplViewHolder(view);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(TplViewHolder holder, int position) {

        SmsTplListModel tplListModel = new SmsTplListModel();
        tplListModel = msgTplListModels.get(position);
        String tplContent = tplListModel.getTpl_content();
        holder.msgListTvMsgTpl.setText(replace(tplContent));

        if (tplListModel.getStatus().equals("0")){
           holder.msgListTvStatus.setText("*模版审核中");
        }else {
            holder.msgListTvStatus.setText("");
        }

        //选中状态
        if (tplListModel.getIs_default().equals("1")) {
            holder.msgListCbDefault.setChecked(true);
        } else {
            holder.msgListCbDefault.setChecked(false);
        }

        //设置短信模版为默认
        final SmsTplListModel finalTplListModel = tplListModel;
        if (holder.msgListCbDefault.isChecked()){
            holder.msgListCbDefault.setEnabled(false);
            holder.msgListCbDefault.setText(R.string.default_tpl);
            holder.msgListCbDefault.setTextSize(18);
            holder.msgListCbDefault.setTextColor(context.getResources().getColor(R.color.primary));
            holder.msgListCbDefault.setButtonDrawable(R.mipmap.ic_cb_selected);
        }else {
            holder.msgListCbDefault.setEnabled(true);
            holder.msgListCbDefault.setText(R.string.set_default);
            holder.msgListCbDefault.setTextSize(16);
            holder.msgListCbDefault.setTextColor(context.getResources().getColor(R.color.primary_text));
            holder.msgListCbDefault.setButtonDrawable(R.mipmap.ic_cb_normal);
            holder.msgListCbDefault.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        System.out.println("短信ID"+finalTplListModel.getId());
                        Isdefault(finalTplListModel.getId());
                    }
                }
            });
        }

        //删除短信模版
        holder.msgListLlDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确认删除短信模版")
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DelTpl(finalTplListModel.getId());
                            }
                        })
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return msgTplListModels.size();
    }

    class TplViewHolder extends RecyclerView.ViewHolder {

        private TextView msgListTvMsgTpl;
        private TextView msgListTvStatus;
        private CheckBox msgListCbDefault;
        private LinearLayout msgListLlDel;


        TplViewHolder(View view) {
            super(view);
            msgListTvMsgTpl = (TextView) view.findViewById(R.id.msgList_tv_msg_tpl);
            msgListTvStatus = (TextView) view.findViewById(R.id.msgList_tv_status);
            msgListCbDefault = (CheckBox) view.findViewById(R.id.msgList_cb_default);
            msgListLlDel = (LinearLayout) view.findViewById(R.id.msgList_ll_del);
        }
    }

    //设为默认短信模版
    private void Isdefault(final String tpl_id) {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.SET_DEFAULT_TPL);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("tpl_id", tpl_id);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        listener.onSetDefault();
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

    //删除短信模版
    private void DelTpl(final String tpl_id) {
        HttpUtils httpUtils = HttpUtils.getInstance();
        httpUtils.setOnResult(new HttpUtils.OnResult() {
            @Override
            public RequestParams onParams() {
                RequestParams params = new RequestParams(ApiConfig.DEL_TPL);
                params.addBodyParameter("token", MyApplication.token);
                params.addBodyParameter("tpl_id", tpl_id);
                return params;
            }

            @Override
            public void onUsage(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object.getInt("code") == 200) {
                        Toast.makeText(context, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        listener.onDelTpl();
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


    //替换短信模版内容
    private SpannableString replace(String content) {
        SpannableString spanStr = new SpannableString(content);
        String compang_name = "{$company_name}";
        String yun_no = "{$yun_no}";
        String address = "{$address}";
        String get_no = "{$get_no}";
        String open_date = "{$open_date}";

        if (content.contains(compang_name)) {
            int start = content.indexOf(compang_name);
            int end = start + compang_name.length();
            replaceImg(start, end, R.drawable.ic_kuaidi, spanStr);
        }
        if (content.contains(yun_no)) {
            int start = content.indexOf(yun_no);
            int end = start + yun_no.length();
            replaceImg(start, end, R.drawable.ic_danhao, spanStr);
        }
        if (content.contains(address)) {
            int start = content.indexOf(address);
            int end = start + address.length();
            replaceImg(start, end, R.drawable.ic_dizi, spanStr);
        }
        if (content.contains(get_no)) {
            int start = content.indexOf(get_no);
            int end = start + get_no.length();
            replaceImg(start, end, R.drawable.ic_quhuoma, spanStr);
        }
        if (content.contains(open_date)) {
            int start = content.indexOf(open_date);
            int end = start + open_date.length();
            replaceImg(start, end, R.drawable.ic_shijian, spanStr);
        }

        return spanStr;
    }

    private void  replaceImgAll(String s,String replace,SpannableString spanStr,int start,int res){
        start = s.indexOf(replace,start);
        int end = start+replace.length();
        replaceImg(start,end,res,spanStr);
        if (start>0){
            replaceImgAll(s,replace,spanStr,start,res);
        }
    }


    private void replaceImg(int start, int end, int res, SpannableString spanStr) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), res);
        ImageSpan imageSpan = new ImageSpan(bitmap);
        spanStr.setSpan(imageSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

    }
    //短信列表操作接口
    public interface TplHandleListener {
        void onSetDefault();
        void onDelTpl();
    }

    private TplHandleListener listener;

    public void setTplHandleListener(TplHandleListener listener) {
        this.listener = listener;
    }
}
