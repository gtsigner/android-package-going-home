package com.onynet.a30home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onynet.a30home.R;
import com.onynet.a30home.model.ExpressModel;

import java.util.ArrayList;
import java.util.List;

public class ExpressCompanyListItemAdapter extends BaseAdapter {

    private List<ExpressModel> expressNames = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;

    public ExpressCompanyListItemAdapter(Context context,List<ExpressModel> expressNames) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.expressNames = expressNames;
    }

    @Override
    public int getCount() {
        return expressNames.size();
    }

    @Override
    public ExpressModel getItem(int position) {
        return expressNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.express_company_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((ExpressModel) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(ExpressModel expressName, ViewHolder holder) {
        holder.expressCompanyItemTvName.setText(expressName.getName());
    }

    private class ViewHolder {
        private TextView expressCompanyItemTvName;

        ViewHolder(View view) {
            expressCompanyItemTvName = (TextView) view.findViewById(R.id.expressCompany_item_tv_name);
        }
    }
}
