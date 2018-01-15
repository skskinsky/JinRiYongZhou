package com.dingtai.jinriyongzhou.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.dingtai.jinriyongzhou.R.id.iv_IsPolitic;
import static com.dingtai.jinriyongzhou.R.id.tv_PoliticsAreaName;
import static com.dingtai.jinriyongzhou.R.id.tv_remark;

/**
 * Created by Administrator on 2017/11/10 0010.
 */

public class ModelListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SubscribeListsModel> subscribeList;
    private DisplayImageOptions options;
    private String state;

    public ModelListAdapter(LayoutInflater inflater, List<SubscribeListsModel> subscribeList) {
        this.mInflater = inflater;
        this.subscribeList = subscribeList;
        this.options = MyImageLoader.option();
    }

    public void setData(List<SubscribeListsModel> subscribeList) {
        this.subscribeList = subscribeList;
    }

    @Override
    public int getCount() {
        return subscribeList != null ? subscribeList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return subscribeList != null ? subscribeList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mInflater.inflate(R.layout.item_model_list, parent, false);
            holder = new ViewHolder();
            holder.tv_PoliticsAreaName = (TextView) convertView.findViewById(tv_PoliticsAreaName);
            holder.iv_IsPolitic = (ImageView) convertView.findViewById(iv_IsPolitic);
            holder.iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_remark = (TextView) convertView.findViewById(tv_remark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(
                subscribeList.get(position).getSmallPicUrl(),
                holder.iv_pic, options);
        holder.tv_remark.setText(subscribeList.get(position).getReMark());
        holder.tv_PoliticsAreaName.setText(subscribeList.get(position).getPoliticsAreaName());
        state = subscribeList.get(position).getIsPolitic();
        if ("1".equals(state)) {
            holder.iv_IsPolitic.setImageDrawable(convertView.getResources().getDrawable(R.drawable.yidingy));
        } else {
            holder.iv_IsPolitic.setImageDrawable(convertView.getResources().getDrawable(R.drawable.dingyue1));
        }
        holder.iv_IsPolitic.setTag(position);
        holder.iv_IsPolitic.setOnClickListener(listeners);

        return convertView;
    }

    class ViewHolder {
        TextView tv_PoliticsAreaName;
        ImageView iv_IsPolitic;
        ImageView iv_pic;
        TextView tv_remark;
    }


    public interface OnImageViewClickListener {
        void onImageViewClick(int position);
    }

    private OnImageViewClickListener listener;

    public void setOnImageViewClickListener(OnImageViewClickListener listener) {
        this.listener = listener;
    }

    private View.OnClickListener listeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = (int) v.getTag();
                listener.onImageViewClick(position);
            }
        }
    };
}
