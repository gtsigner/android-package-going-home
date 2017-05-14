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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onynet.a30home.R;
import com.onynet.a30home.activity.ImgActivity;
import com.onynet.a30home.config.ApiConfig;
import com.onynet.a30home.model.QueryListModel;
import com.onynet.a30home.utils.SDUtils;
import com.onynet.a30home.utils.TimeUtils;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryListItemAdapter extends RecyclerView.Adapter<QueryListItemAdapter.MyVierHodler> {

    private List<QueryListModel.PackageListBean> packageListBeans = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private ImageOptions imageOptions;
    private List<String> filesPath = new ArrayList<>();

    // 用来控制CheckBox的选中状况
    private static Map<Integer, Boolean> isSelected;

    public QueryListItemAdapter(Context context, List<QueryListModel.PackageListBean> packageListBeans) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.packageListBeans = packageListBeans;


        isSelected = new HashMap<>();
        // 初始化数据
        initDate();


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

    // 初始化isSelected的数据
    private void initDate() {
        for (int i = 0; i < packageListBeans.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public static Map<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(Map<Integer, Boolean> isSelected) {
        QueryListItemAdapter.isSelected = isSelected;
    }


    @Override
    public MyVierHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.query_list_item, parent, false);

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
        holder.queryLlOutTime.setVisibility(View.GONE);

        switch (packageListBeans.get(position).getStatus()) {
            case "0"://到站
                holder.queryLlImg.setVisibility(View.VISIBLE);
                holder.queryTvPackgeStatus.setText(R.string.YiDaoZhan);
                holder.queryBtnSaveImg.setVisibility(View.GONE);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary1));
                //显示多选框
                holder.queryCb.setVisibility(View.VISIBLE);
                holder.queryCb.setChecked(getIsSelected().get(position));
                holder.queryCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        getIsSelected().put(position, b);
                    }
                });
                //显示默认拍照图片
                holder.queryTvCamera.setText(R.string.photo_put);
                holder.queryIvCamera.setImageResource(R.mipmap.ic_pic);
                //拍照入库
                holder.queryIvCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ivCameraOnClickLinsteren.onClick(position, view);
                    }
                });
                break;
            case "2"://移库
                //显示多选框
                holder.queryCb.setVisibility(View.GONE);
                holder.queryLlImg.setVisibility(View.VISIBLE);
                holder.queryBtnSaveImg.setVisibility(View.GONE);
                holder.queryTvPackgeStatus.setText(R.string.YiYiKu);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary2));
                //显示图片
                holder.queryTvCamera.setText("");
                x.image().bind(holder.queryIvCamera, img_path, imageOptions);
                break;
            case "4"://退件
                holder.queryCb.setVisibility(View.GONE);
                holder.queryLlImg.setVisibility(View.GONE);
                holder.queryTvPackgeStatus.setText(R.string.YiTuiJian);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary3));
                //显示图片
                holder.queryTvCamera.setText("");
                x.image().bind(holder.queryIvCamera, img_path, imageOptions);
                break;

            case "5"://出库
                holder.queryCb.setVisibility(View.GONE);
                holder.queryLlImg.setVisibility(View.VISIBLE);

                holder.queryTvPackgeStatus.setText(R.string.YiChuKu);
                holder.queryTvPackgeStatus.setTextColor(context.getResources().getColor(R.color.primary4));
                //出库时间
                holder.queryTvOutTime.setText(TimeUtils.timeToString("yyyy-MM-dd HH:mm", Long.valueOf(packageListBeans.get(position).getPop_time())));
                holder.queryLlOutTime.setVisibility(View.VISIBLE);
                if (packageListBeans.get(position).getOut_img_path() == null) {
                    holder.queryTvCamera.setText("补传图片");
                    holder.queryIvCamera.setImageResource(R.mipmap.ic_pic);
                    holder.queryBtnSaveImg.setVisibility(View.GONE);
                    holder.queryIvCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ivCameraOnClickLinsteren.onClick(position, view);
                        }
                    });
                } else {
                    //显示图片
                    holder.queryTvCamera.setText("");
                    holder.queryBtnSaveImg.setVisibility(View.VISIBLE);
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


                    holder.queryBtnSaveImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadImagesByUrl(context,img_path,filesPath);
                        }
                    });
                }
                break;
        }

    }

    public static void downloadImagesByUrl(final Context context, String url, final List<String> filesPath) {
       /*
       获取保存路径：手机SD卡1存储 storage/sdcard/Android/data/应用的包名/files
       Genymotion模拟器的路径：/storage/emulated/0/Android/data/com.atguigu.zhuatutu/files
        */
//        File filesDir = context.getExternalFilesDir(null);
        //获取文件名:/february_2016-001.jpg
        String fileName = url.substring(url.lastIndexOf("/"));
        //存到本地的绝对路径
        final String filePath = SDUtils.getSDPath() + "/DCIM/30DJ/Picture" + fileName;
        File file = new File(SDUtils.getSDPath() + "/DCIM/30DJ/Picture");
        //如果不存在
        if (!file.exists()) {
            //创建
            file.mkdirs();
        }

        RequestParams entity = new RequestParams(url);
        entity.setSaveFilePath(filePath);
        x.http().get(entity, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                filesPath.add(result.getAbsolutePath());
                LogUtil.e("onSuccess：" + result.getAbsolutePath());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("onError ");
                Toast.makeText(x.app(),"网络错误，下载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled ");
            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished ");
                Toast.makeText(x.app(), "下载成功,保存到：" + filesPath.get(filesPath.size() - 1), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return packageListBeans.size();
    }

    class MyVierHodler extends RecyclerView.ViewHolder {

        public TextView queryTvPackgeStatus;//包裹状态
        public TextView queryUpGoodsCode;//提货码
        public TextView queryTvName;//取货人名
        public TextView queryTvPhoneNumber;//取货人电话
        public TextView queryTvExpressName;//快递公司
        public TextView queryTvBarCode;//快递号
        public TextView queryTvPutTime;//入库时间
        public TextView queryTvOutTime;//出库时间
        public CheckBox queryCb;//多选框
        public ImageView queryIvCamera;
        public LinearLayout queryLlCamera;
        public LinearLayout queryLlImg;
        public LinearLayout queryLlOutTime;
        public TextView queryTvCamera;
        public Button queryBtnSaveImg;


        public MyVierHodler(View view) {
            super(view);

            queryTvPackgeStatus = (TextView) view.findViewById(R.id.query_tv_packge_status);
            queryUpGoodsCode = (TextView) view.findViewById(R.id.query_up_goods_code);
            queryTvName = (TextView) view.findViewById(R.id.query_tv_name);
            queryTvPhoneNumber = (TextView) view.findViewById(R.id.query_tv_phone_number);
            queryTvExpressName = (TextView) view.findViewById(R.id.query_tv_express_name);
            queryTvBarCode = (TextView) view.findViewById(R.id.query_tv_bar_code);
            queryTvPutTime = (TextView) view.findViewById(R.id.query_tv_put_time);
            queryTvOutTime = (TextView) view.findViewById(R.id.query_tv_out_time);
            queryCb = (CheckBox) view.findViewById(R.id.query_cb);
            queryIvCamera = (ImageView) view.findViewById(R.id.query_iv_camera);
            queryLlCamera = (LinearLayout) view.findViewById(R.id.query_ll_camera);
            queryLlImg = (LinearLayout) view.findViewById(R.id.query_ll_img);
            queryLlOutTime = (LinearLayout) view.findViewById(R.id.query_ll_out_time);
            queryTvCamera = (TextView) view.findViewById(R.id.query_tv_camera);
            queryBtnSaveImg = (Button) view.findViewById(R.id.query_save_img);

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
