package com.dingtai.jinriyongzhou.adapter;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.ViewHolderUtils;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7 0007.
 */


public class HallGridviewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PoliticsAreaModel> politicsModels;
    private DisplayImageOptions option;

    public HallGridviewAdapter(LayoutInflater inflater, List<PoliticsAreaModel> politicsModels) {
        this.inflater = inflater;
        this.politicsModels = politicsModels;
        this.option = MyImageLoader.option();
    }

    public void setData(List<PoliticsAreaModel> politicsModels) {
        this.politicsModels = politicsModels;
    }

    public int getCount() {
        return this.politicsModels.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_model, parent, false);
        }

        ImageView iv_icon = ViewHolderUtils.get(convertView, R.id.iv_icon);
        TextView tv_name = ViewHolderUtils.get(convertView, R.id.tv_name);
        PoliticsAreaModel model = politicsModels.get(position);

            ImageLoader.getInstance().displayImage(model.getAreaSmallPicUrl(), iv_icon, this.option);
            tv_name.setText(model.getAreaPoliticsAreaName());

        return convertView;
    }
}


