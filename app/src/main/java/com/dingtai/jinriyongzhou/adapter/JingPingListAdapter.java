package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.Content;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class JingPingListAdapter extends BaseAdapter {

	private List<Content> list;
	private DisplayImageOptions options;
	private Context context;

	public JingPingListAdapter(Context context,List<Content> list) {
		this.list = list;
		this.context = context;
		options = MyImageLoader.option();
	}

	public void setData(List<Content> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_jingping_listview, null);
			holder.image_bg = (ImageView) convertView.findViewById(R.id.image_bg);
			holder.title = (TextView) convertView.findViewById(R.id.title);
		
			LayoutParams para = new LayoutParams(DisplayMetricsTool.getWidth(context)-DisplayMetricsTool.dip2px(context, 14), (DisplayMetricsTool.getWidth(context)-DisplayMetricsTool.dip2px(context, 14))*3/4);
			holder.image_bg.setLayoutParams(para);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Content obj = list.get(position);
		ImageLoader.getInstance().displayImage(obj.getContentProgramContentLogo(),
				holder.image_bg, options);
		holder.title.setText(obj.getContentProgramContentName());
		return convertView;
	}

	class ViewHolder {
		ImageView image_bg;
		TextView title;
	}

}
