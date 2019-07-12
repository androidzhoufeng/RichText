package com.zf.richtext.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zf.richtext.R;
import com.zf.richtext.bean.SpinnerBean;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private SpinnerBean[] mDatas;

    public SpinnerAdapter(SpinnerBean[] mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.length : 0;
    }

    @Override
    public SpinnerBean getItem(int position) {
        return mDatas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rich_text_setting_item, parent, false);
        }
        TextView tvItem = convertView.findViewById(R.id.tvItem);
        ImageView ivItem = convertView.findViewById(R.id.ivItem);
        if (mDatas[position].id == 3){
            tvItem.setVisibility(View.GONE);
            ivItem.setVisibility(View.VISIBLE);
            ivItem.setImageResource(Integer.parseInt(mDatas[position].name));
        }else {
            tvItem.setVisibility(View.VISIBLE);
            ivItem.setVisibility(View.GONE);
            tvItem.setText(mDatas[position].name);
            if (mDatas[position].id == 1) {//字体大小
                tvItem.setTextSize(Float.parseFloat(mDatas[position].content));
            } else if (mDatas[position].id == 2) {//颜色
                tvItem.setTextColor(Color.parseColor(mDatas[position].content));
            }
        }

        return convertView;
    }
}
