package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.MediaList;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class IndexLikeAdapter extends BaseAdapter {

    private List<MediaList> data;
    private LayoutInflater inflater;
    private Context context;

    public IndexLikeAdapter(Context context, List<MediaList> data) {
        this.data = data;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    public void onBindViewHolder(com.dingtai.jinriyongzhou.view.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 1:
                GrideViewHolder viewHolder = (GrideViewHolder) holder;

                RelativeLayout.LayoutParams imgpar = (RelativeLayout.LayoutParams) viewHolder.news_img.getLayoutParams();

                imgpar.width = DisplayMetricsTool.getWidth(context);
                imgpar.height = (int) (imgpar.width * 9 / 16);// 控件的宽强制设成2:3

                viewHolder.news_img.setLayoutParams(imgpar); //使设置好的布局参数应用到控件

                viewHolder.news_title.setText(data.get(position).getName());
                ImageLoader.getInstance().displayImage(data.get(position).getImageUrl(), viewHolder.news_img);
                break;
            case 2:
                VodViewHolder viewHolder1 = (VodViewHolder) holder;
                RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) viewHolder1.iv_banner.getLayoutParams();
                par.width = (DisplayMetricsTool.getWidth(context)) / 2;
                par.height = (int) (par.width / 1.78);// 控件的宽强制设成2:3

                viewHolder1.iv_banner.setLayoutParams(par); //使设置好的布局参数应用到控件


                RelativeLayout.LayoutParams text_par = (RelativeLayout.LayoutParams) viewHolder1.linear_layout.getLayoutParams();
                text_par.width = (DisplayMetricsTool.getWidth(context)) / 2;
                text_par.height = par.height * 2 / 3;
                text_par.addRule(RelativeLayout.BELOW, R.id.rel_layout);

                viewHolder1.linear_layout.setLayoutParams(text_par); //使设置好的布局参数应用到控件

                ImageLoader.getInstance().displayImage(data.get(position).getImageUrl(),
                        viewHolder1.iv_banner);
                viewHolder1.tv_title.setText(data.get(position).getName());

                break;

        }
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        com.dingtai.jinriyongzhou.view.ViewHolder viewHolder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = inflater.inflate(R.layout.item_index_video, parent, false);
                    viewHolder = new GrideViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
                case 2:
                    convertView = inflater.inflate(R.layout.item_index_vod, parent, false);
                    viewHolder = new VodViewHolder(convertView);
                    convertView.setTag(viewHolder);
                    break;
            }
        } else {
            viewHolder = (com.dingtai.jinriyongzhou.view.ViewHolder) convertView.getTag();
        }
        onBindViewHolder(viewHolder, position);
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    public void addRes(List<MediaList> data) {
        if (data != null) {
            this.data = data;
            this.notifyDataSetChanged();
        }
    }


    public class GrideViewHolder extends com.dingtai.jinriyongzhou.view.ViewHolder {
        ImageView news_img;
        TextView news_title;

        public GrideViewHolder(View itemView) {
            news_img = (ImageView) itemView.findViewById(R.id.news_img);
            news_title = (TextView) itemView.findViewById(R.id.news_title);
        }

    }

    public class VodViewHolder extends com.dingtai.jinriyongzhou.view.ViewHolder {
        ImageView iv_banner;
        LinearLayout linear_layout;
        TextView tv_title;

        public VodViewHolder(View itemView) {
            iv_banner = (ImageView) itemView.findViewById(R.id.iv_banner);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            linear_layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }

    }
}