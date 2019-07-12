package com.zf.richtext.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zf.richtext.Constant;
import com.zf.richtext.R;
import com.zf.richtext.bean.RichSettingBean;

import java.util.List;

public class RichAdapter extends RecyclerView.Adapter<RichAdapter.MyViewHolder> {

    private List<RichSettingBean> mDatas;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public RichAdapter(List<RichSettingBean> mDatas) {
        this.mDatas = mDatas;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.rich_text_setting_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        RichSettingBean item = mDatas.get(i);
        if (item.getArrays() != null) {
            myViewHolder.ivItem.setVisibility(View.GONE);
            myViewHolder.tvItem.setVisibility(View.GONE);
            myViewHolder.spinner.setVisibility(View.VISIBLE);

            myViewHolder.spinner.setDropDownVerticalOffset(dip2px(mContext, 35)); //下拉的纵向偏移
            SpinnerAdapter spinnerAdapter = new SpinnerAdapter(item.getArrays());  //创建一个数组适配器
            myViewHolder.spinner.setAdapter(spinnerAdapter);
            myViewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(Constant.TTAG,"==i=>"+i+",=position==>"+position+",==id=>"+id);
                    if (spinnerAdapter.getItem(position).content.equals(mDatas.get(i).getContent()))return;
                    mDatas.get(i).setContent(spinnerAdapter.getItem(position).content);
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(RichAdapter.this, i, mDatas.get(i).getContent());
//                    notifyItemChanged(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (!TextUtils.isEmpty(item.getContent())) {
            myViewHolder.ivItem.setVisibility(View.GONE);
            myViewHolder.tvItem.setVisibility(View.VISIBLE);
            myViewHolder.spinner.setVisibility(View.GONE);

            myViewHolder.tvItem.setText(item.getContent());
            if (item.isSelected()) {
                myViewHolder.tvItem.setTextColor(ContextCompat.getColor(mContext, R.color.color_select));
            } else {
                myViewHolder.tvItem.setTextColor(ContextCompat.getColor(mContext, R.color.color_normal));
            }
            myViewHolder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(this, i,null);
            });
        } else if (item.getImage() != 0) {
            myViewHolder.ivItem.setVisibility(View.VISIBLE);
            myViewHolder.tvItem.setVisibility(View.GONE);
            myViewHolder.ivItem.setImageResource(item.getImage());
            myViewHolder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(this, i,null);
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItem;
        private ImageView ivItem;
        private Spinner spinner;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
            ivItem = itemView.findViewById(R.id.ivItem);
            spinner = itemView.findViewById(R.id.spinner);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(RecyclerView.Adapter adapter,int position,String content);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
