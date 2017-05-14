package com.onynet.a30home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onynet.a30home.R;
import com.onynet.a30home.activity.ImgActivity;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.QueryListModel1;
import com.onynet.a30home.utils.TimeUtils;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class QueryListItemAdapter1 extends RecyclerView.Adapter<QueryListItemAdapter1.MyVierHodler> {

    private List<QueryListModel1.PackageListBean> packageListBeans = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageOptions imageOptions;

    // 用来控制CheckBox的选中状况
    private static Map<Integer, Boolean> isSelected;

    public QueryListItemAdapter1(Context context, List<QueryListModel1.PackageListBean> packageListBeans) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.packageListBeans = packageListBeans;
        imageOptions = new ImageOptions.Builder()
//                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))//设置圆角
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true)
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                //设置使用缓存
                .setUseMemCache(true)
                //设置支持gif
                .setIgnoreGif(false)
                //设置显示圆形图片
                .setCircular(false)
                .setSquare(true)
                .build();

    }


    @Override
    public MyVierHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.query_list_item1, parent, false);

        return new MyVierHodler(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(MyVierHodler holder, final int position) {
        //图片地址
        final String img_path = ApiConfig.APP_PATH + packageListBeans.get(position).getOut_img_path();

        //到站时间
        holder.queryTvPutTime.setText(TimeUtils.timeToString("yyyy-MM-dd HH:mm", Long.valueOf(packageListBeans.get(position).getPush_time())));
        holder.queryUpGoodsCode.setText(packageListBeans.get(position).getGet_no());//提货码
        holder.queryTvName.setText(packageListBeans.get(position).getGet_name());//名字
        holder.queryTvPhoneNumber.setText(packageListBeans.get(position).getGet_phone());//电话
        holder.queryTvExpressName.setText(packageListBeans.get(position).getCompany_name());
        holder.queryTvBarCode.setText(packageListBeans.get(position).getYun_no());
        holder.queryTVLocation.setText(packageListBeans.get(position).getAddress_desc());
        holder.queryLlOutTime.setVisibility(View.GONE);

        if (packageListBeans.get(position).getOut_img_path()!=null){
            holder.queryLlImg.setVisibility(View.VISIBLE);
            //显示图片
            holder.queryTvCamera.setText("");
            x.image().bind(holder.queryIvCamera, img_path, imageOptions);


            //点击放大预览图片
            holder.queryIvCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ImgActivity.class);
                    intent.putExtra("img_path", img_path);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in, android.support.v7.appcompat.R.anim.abc_fade_out);
                }
            });

        }else {
            holder.queryLlImg.setVisibility(View.GONE);
        }

        switch (packageListBeans.get(position).getStatus()) {
            case "0"://到站
                holder.queryTvPackgeStatus.setText(R.string.YiDaoZhan);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary1));

                break;
            case "2"://移库
                holder.queryTvPackgeStatus.setText(R.string.YiYiKu);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary2));

                break;
            case "4"://退件
                holder.queryTvPackgeStatus.setText(R.string.YiTuiJian);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary3));
                break;

            case "5"://出库
                holder.queryTvOutTime.setText(TimeUtils.timeToString("yyyy-MM-dd HH:mm", Long.valueOf(packageListBeans.get(position).getPop_time())));
                holder.queryLlOutTime.setVisibility(View.VISIBLE);
                holder.queryTvPackgeStatus.setText(R.string.YiChuKu);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary4));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return packageListBeans.size();
    }

    class MyVierHodler extends RecyclerView.ViewHolder {

        public TextView queryTvPackgeStatus;
        public TextView queryTvTime;
        public TextView queryUpGoodsCode;
        public TextView queryTvName;
        public TextView queryTvPhoneNumber;
        public TextView queryTvExpressName;
        public TextView queryTvBarCode;
        public TextView queryTvPutTime;//入库时间
        public TextView queryTvOutTime;//出库时间
        public LinearLayout queryLlOutTime;
        public ImageView queryIvCamera;
        public TextView queryTvCamera;
        public TextView queryTVLocation;
        public LinearLayout queryLlImg;



        public MyVierHodler(View view) {
            super(view);
            queryTvPackgeStatus = (TextView) view.findViewById(R.id.query_tv_packge_status);
            queryTvTime = (TextView) view.findViewById(R.id.query_tv_time);
            queryUpGoodsCode = (TextView) view.findViewById(R.id.query_up_goods_code);
            queryTvName = (TextView) view.findViewById(R.id.query_tv_name);
            queryTvPhoneNumber = (TextView) view.findViewById(R.id.query_tv_phone_number);
            queryTvExpressName = (TextView) view.findViewById(R.id.query_tv_express_name);
            queryTvBarCode = (TextView) view.findViewById(R.id.query_tv_bar_code);
            queryTvPutTime = (TextView) view.findViewById(R.id.query_tv_put_time);
            queryTvOutTime = (TextView) view.findViewById(R.id.query_tv_out_time);
            queryLlOutTime = (LinearLayout) view.findViewById(R.id.query_ll_out_time);
            queryIvCamera = (ImageView) view.findViewById(R.id.query_iv_camera);
            queryTvCamera = (TextView) view.findViewById(R.id.query_tv_camera);
            queryTVLocation = (TextView) view.findViewById(R.id.query_tv_location);
            queryLlImg = (LinearLayout) view.findViewById(R.id.query_ll_img);


        }

    }

    public interface IvCameraOnClickListener {
        void onClick(int position, View view);
    }

    private IvCameraOnClickListener ivCameraOnClickLinsteren;

    public void setIvCameraOnClickLinsteren(IvCameraOnClickListener ivCameraOnClickLinsteren) {
        this.ivCameraOnClickLinsteren = ivCameraOnClickLinsteren;
    }
}
