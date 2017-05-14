package com.onynet.a30home.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * 时 间: 2017/1/9
 * 作 者: 郑 亮
 * Q  Q: 1023007219
 */
public abstract class ARecyclerBaseAdapter<VH extends RecyclerView.ViewHolder, T> extends RecyclerView.Adapter<VH> {
    protected Context mContext;
    protected List<T> mList;
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener onItemLongClickListener;

    public T getItem(int position) {
        if (position < mList.size())
            return mList.get(position);
        else return null;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ARecyclerBaseAdapter(Context mContext, List<T> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(T t) {
        insert(t, mList.size());
    }

    public void insert(T t, int position) {
        mList.add(position, t);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void addAll(List<T> ts) {
        int startIndex = mList.size();
        mList.addAll(startIndex, ts);
        notifyItemRangeInserted(startIndex, ts.size());
    }

}
