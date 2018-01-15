package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.view.CircularImage;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.PoliticsNewsModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/30 0030.
 */

public class PoliticsNewsAdapter extends BaseAdapter {

    private List<PoliticsNewsModel> list = new ArrayList<>();
    private Context context;
    private DisplayImageOptions option;

    public PoliticsNewsAdapter(Context context) {
        this.context = context;
        option = MyImageLoader.option();
    }

    public void setList(List<PoliticsNewsModel> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PoliticsNewsViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_institution_list, parent, false);
            holder = new PoliticsNewsViewHolder();
            holder.iv_head = (CircularImage) convertView.findViewById(R.id.iv_head);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.iv_content = (ImageView) convertView.findViewById(R.id.iv_content);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }else {
            holder = (PoliticsNewsViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(
                list.get(position).getAreaLogo(), holder.iv_head, option);
        holder.tv_name.setText(list.get(position).getPoliticsAreaName());
        holder.tv_date.setText(list.get(position).getUpdateTime());
        holder.tv_content.setText(list.get(position).getTitle());
        ImageLoader.getInstance().displayImage(
                list.get(position).getSmallPicUrl(), holder.iv_content, option);

        return convertView;
    }

    class PoliticsNewsViewHolder {
        CircularImage iv_head;
        TextView tv_name;
        TextView tv_date;
        ImageView iv_content;
        TextView tv_content;
    }
}
