package com.onynet.a30home;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onynet.a30home.activity.DetailsActivity;
import com.onynet.a30home.model.MsgListModel;
import com.onynet.a30home.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class MsgListItemAdapter extends RecyclerView.Adapter<MsgListItemAdapter.MyViewHolder> {
    private List<MsgListModel> listBeanList = new ArrayList<>();
    private Context context;

    public MsgListItemAdapter(List<MsgListModel> listBeanList, Context context) {
        this.listBeanList = listBeanList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.msg_list_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (listBeanList.get(position).getStatus().equals("0")) {
            TextPaint tp = holder.msgItemTvTitle.getPaint();
            tp.setFakeBoldText(true);
        } else {
            TextPaint tp = holder.msgItemTvTitle.getPaint();
            tp.setFakeBoldText(false);
        }
        holder.msgItemTvTitle.setText(listBeanList.get(position).getTitle());
        holder.msgItemTvBody.setText(listBeanList.get(position).getDesc());
        holder.msgItemTvTime.setText(TimeUtils.timeToString("yyyy-MM-dd HH:mm:ss", Long.valueOf(listBeanList.get(position).getCreate_time())));
        holder.msgRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("id", listBeanList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listBeanList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout msgRlItem;
        private ImageView msgItemIvIn;
        private TextView msgItemTvTitle;
        private TextView msgItemTvBody;
        private TextView msgItemTvTime;

        MyViewHolder(View view) {
            super(view);
            msgRlItem = (RelativeLayout) view.findViewById(R.id.msg_rl_item);
            msgItemIvIn = (ImageView) view.findViewById(R.id.msg_item_iv_in);
            msgItemTvTitle = (TextView) view.findViewById(R.id.msg_item_tv_title);
            msgItemTvBody = (TextView) view.findViewById(R.id.msg_item_tv_body);
            msgItemTvTime = (TextView) view.findViewById(R.id.msg_item_tv_time);
        }
    }
}
