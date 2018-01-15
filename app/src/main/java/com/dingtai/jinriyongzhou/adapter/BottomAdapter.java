package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.adapter.DragGridBaseAdapter;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.IndexModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Collections;
import java.util.List;

public class BottomAdapter extends BaseAdapter implements DragGridBaseAdapter {
    private List<IndexModel> list;
    private Context context;
    private Holder vh = null;
    private int mHidePosition;
    private DisplayImageOptions option;

    public BottomAdapter(Context context, List<IndexModel> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        mHidePosition = list.size();
        option = MyImageLoader.option();
    }

    // public TitltAdapter(Context context,List<MoreChannelModel> list,int
    // flag){
    // this.context = context;
    // this.list = list;
    // mHidePosition = list.size();
    // }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        vh = null;
        // if (arg1 == null) {
        if (arg1 == null) {
            vh = new Holder();
            arg1 = LayoutInflater.from(context).inflate(R.layout.item_model, null);
            vh.title_name = (TextView) arg1.findViewById(R.id.tv_name);
            vh.iv = (ImageView) arg1.findViewById(R.id.iv_icon);
            arg1.setTag(vh);
        } else {
            vh = (Holder) arg1.getTag();
        }
        // } else {
        // vh = (Holder) arg1.getTag();
        // }

        String name = list.get(position).getModuleName();
        // if ("True".equals(list.get(position).getIsDel())) {
        // vh.title_name.setTextColor(context.getResources().getColor(
        // R.color.grey));
        // vh.title_name.setEnabled(false);
        // }
        ImageLoader.getInstance().displayImage(
                list.get(position).getModuleLogo(), vh.iv, option);
        vh.title_name.setText(name);
        if (position == mHidePosition) {
            arg1.setVisibility(View.INVISIBLE);
        }
        return arg1;
    }

    public class Holder {
        public TextView title_name;
        public ImageView iv;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        // TODO Auto-generated method stub

        IndexModel temp = list.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        list.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        // TODO Auto-generated method stub
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    public List<IndexModel> getSwapList() {
        return list;
    }
}
