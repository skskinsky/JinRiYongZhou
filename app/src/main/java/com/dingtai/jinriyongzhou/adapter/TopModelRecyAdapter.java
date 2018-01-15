package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/11/8 0008.
 */

public class TopModelRecyAdapter extends RecyclerView.Adapter<TopModelRecyAdapter.ViewHolder> {

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<PoliticsAreaModel> mDatas;
    private Context context;

    public TopModelRecyAdapter(Context context, List<PoliticsAreaModel> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView iv_icon;
        TextView tv_name;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setData(List<PoliticsAreaModel> mDatas) {
        if (mDatas != null) {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }
    }


    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_recy_model,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) viewHolder.iv_icon.getLayoutParams();
        layoutParams.width = (int) ((DisplayMetricsTool.getWidth(context) - DisplayMetricsTool.dip2px(context, 150)) / 5);
        layoutParams.height = layoutParams.width;

        layoutParams.leftMargin = DisplayMetricsTool.dip2px(context, 15);
        layoutParams.rightMargin = DisplayMetricsTool.dip2px(context, 15);
        layoutParams.topMargin = DisplayMetricsTool.dip2px(context, 5);
        viewHolder.iv_icon.setLayoutParams(layoutParams);

        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {


        ImageLoader.getInstance().displayImage(mDatas.get(i).getAreaSmallPicUrl(),
                viewHolder.iv_icon);
        viewHolder.tv_name.setText(mDatas.get(i).getAreaPoliticsAreaName());


        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }
    }
}
