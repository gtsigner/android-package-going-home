package com.onynet.a30home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onynet.a30home.R;
import com.onynet.a30home.model.SmsListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SmsListItemAdapter extends RecyclerView.Adapter<SmsListItemAdapter.MyVierHodler> {

    private List<SmsListModel.PoolsBean> poolsBeens = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    // 用来控制CheckBox的选中状况
    private static Map<Integer, Boolean> isSelected;

    public SmsListItemAdapter(Context context, List<SmsListModel.PoolsBean> poolsBeens) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.poolsBeens = poolsBeens;


    }


    @Override
    public MyVierHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sms_list_item, parent, false);
        return new MyVierHodler(view);
    }

    @Override
    public void onBindViewHolder(MyVierHodler holder, final int position) {
        //剩余量
        int ResidueMsg = Integer.valueOf(poolsBeens.get(position).getTotal_count())-Integer.valueOf(poolsBeens.get(position).getLast_count());

        holder.msgSum.setText("短信总量"+poolsBeens.get(position).getTotal_count()+"条");//总量
        holder.msgBeenUsed.setText("已使用"+ResidueMsg+"条");//已使用量
        holder.msgResidueMsg.setText("剩余"+poolsBeens.get(position).getLast_count()+"条");
        holder.seekBar.setEnabled(false);
        holder.seekBar.setMax(Integer.valueOf(poolsBeens.get(position).getTotal_count()));
        holder.seekBar.setProgress(Integer.valueOf(poolsBeens.get(position).getLast_count()));
        ;
    }

    @Override
    public int getItemCount() {
        return poolsBeens.size();
    }

    class MyVierHodler extends RecyclerView.ViewHolder {
        private TextView msgResidueMsg;
        private SeekBar seekBar;
        private TextView msgBeenUsed;
        private TextView msgSum;

        public MyVierHodler(View view) {
            super(view);
            msgResidueMsg = (TextView) view.findViewById(R.id.msg_residue_msg);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            msgBeenUsed = (TextView) view.findViewById(R.id.msg_been_used);
            msgSum = (TextView) view.findViewById(R.id.msg_sum);
        }

    }


}
