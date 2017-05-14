package com.onynet.a30home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.onynet.a30home.R;

import java.util.ArrayList;
import java.util.List;

public class InformFailureListItemAdapter extends BaseAdapter {

    private List<String> objects = new ArrayList<String>();

    private Context context;
    private LayoutInflater layoutInflater;


    public InformFailureListItemAdapter(Context context,List<String> objects) {
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
            convertView = layoutInflater.inflate(R.layout.inform_failure_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(String object, ViewHolder holder) {

    }

    private static class ViewHolder {
        private TextView informFailureTvExpressName;
        private TextView informFailureTvExpressNumber;
        private CheckBox informFailureCbSelect;
        private TextView informFailureTvName;
        private TextView informFailureTvPhoneNumber;
        private TextView informFailureTvSerialNumber;
        private TextView informFailureTvTime;

        ViewHolder(View view) {
            informFailureTvExpressName = (TextView) view.findViewById(R.id.informFailure_tv_express_name);
            informFailureTvExpressNumber = (TextView) view.findViewById(R.id.informFailure_tv_express_number);
            informFailureCbSelect = (CheckBox) view.findViewById(R.id.informFailure_cb_select);
            informFailureTvName = (TextView) view.findViewById(R.id.informFailure_tv_name);
            informFailureTvPhoneNumber = (TextView) view.findViewById(R.id.informFailure_tv_phone_number);
            informFailureTvSerialNumber = (TextView) view.findViewById(R.id.informFailure_tv_serial_number);
            informFailureTvTime = (TextView) view.findViewById(R.id.informFailure_tv_time);
        }
    }
}
