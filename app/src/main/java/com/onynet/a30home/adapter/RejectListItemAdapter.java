package com.onynet.a30home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onynet.a30home.R;

import java.util.ArrayList;
import java.util.List;

public class RejectListItemAdapter extends BaseAdapter {

    private List<String> objects = new ArrayList<String>();

    private Context context;
    private LayoutInflater layoutInflater;

    public RejectListItemAdapter(Context context, List<String> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public String getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.reject_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {
        //TODO implement
    }

    private static class ViewHolder {
        private TextView rejectTvTime;
        private TextView rejectTvExpressName;
        private TextView rejectTvExpressNumber;
        private TextView rejectTvName;
        private TextView rejectTvPhoneNumber;
        private TextView rejectTvSerialNumber;

        ViewHolder(View view) {
            rejectTvTime = (TextView) view.findViewById(R.id.reject_tv_time);
            rejectTvExpressName = (TextView) view.findViewById(R.id.reject_tv_express_name);
            rejectTvExpressNumber = (TextView) view.findViewById(R.id.reject_tv_express_number);
            rejectTvName = (TextView) view.findViewById(R.id.reject_tv_name);
            rejectTvPhoneNumber = (TextView) view.findViewById(R.id.reject_tv_phone_number);
            rejectTvSerialNumber = (TextView) view.findViewById(R.id.reject_tv_serial_number);
        }
    }
}
