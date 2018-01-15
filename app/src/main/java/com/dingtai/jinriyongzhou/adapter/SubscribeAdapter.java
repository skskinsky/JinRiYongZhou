package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.ViewHolderUtils;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class SubscribeAdapter extends BaseAdapter {
    // private String[] gv_name = { "新闻", "服务", "商城", "社交", "旅游", "交通", "生活缴费",
    // "更多"};
    // private int[] gv_icon =
    // {R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience,
    // R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience,R.drawable.dt_standard_index_nav_left_convenience};
    private Context context;
    private List<SubscribeListsModel> indexModels;
    private DisplayImageOptions option;

    public SubscribeAdapter(Context context, List<SubscribeListsModel> indexModels) {
        this.context = context;
        this.indexModels = indexModels;
        option = MyImageLoader.option();
    }

    public void setData(List<SubscribeListsModel> indexModels) {
        this.indexModels = indexModels;
    }

    @Override
    public int getCount() {
        if (indexModels.size() == 0)
            return 0;
        return indexModels.size();
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_model, parent, false);
        }
        ImageView iv_icon = ViewHolderUtils.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolderUtils.get(convertView, R.id.tv_name);
        // if (position == indexModels.size()) {
        // iv_icon.setImageResource(R.drawable.dt_standard_index_nav_left_convenience);
        // tv_name.setText("更多");
        // } else {
        SubscribeListsModel model = indexModels.get(position);
        ImageLoader.getInstance().displayImage(model.getSmallPicUrl(), iv_icon,
                option);
        tv_name.setText(model.getPoliticsAreaName());
        // }
        return convertView;
    }

}