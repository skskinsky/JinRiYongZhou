package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.view.CircularImage;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 作 者： 李亚军 时 间：2015-1-29 上午10:04:18 活动适配
 */
public class PingDaoZhiBoAdapter extends BaseAdapter {

	private Context context;
	private List<LiveChannelModel> list;

	public List<LiveChannelModel> delList = new ArrayList<LiveChannelModel>();
	

	public PingDaoZhiBoAdapter(Context context, List<LiveChannelModel> list) {
		this.context = context;
		this.list = list;
		// TODO 自动生成的构造函数存根
	}

	public void setList(List<LiveChannelModel> list) {
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
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}


	@Override
	public View getView(final int position, View converView, ViewGroup parent) {
		final PingDaoLiveViewHolder holder;
		if (converView == null || converView.getTag() == null) {
			holder = new PingDaoLiveViewHolder();
			converView = LayoutInflater.from(context).inflate(
					R.layout.item_jiemu_dianbo, null);

			holder.video_head_img = (CircularImage) converView
					.findViewById(R.id.video_head_img);
			holder.video_img = (ImageView) converView
					.findViewById(R.id.video_img);
			holder.video_name = (TextView) converView
					.findViewById(R.id.video_name);

			RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
					DisplayMetricsTool.getWidth(context),
					(int)((DisplayMetricsTool.getWidth(context)) *9/ 16));
			para.topMargin = DisplayMetricsTool.dip2px(context, 50);
			holder.video_img.setLayoutParams(para);
			converView.setTag(holder);
		} else {
			holder = (PingDaoLiveViewHolder) converView.getTag();
		}
		
		
	
		ImageLoader.getInstance().displayImage(list.get(position).getLiveChannleLogo(), holder.video_head_img);
		ImageLoader.getInstance().displayImage(list.get(position).getLiveImageUrl(), holder.video_img);
		holder.video_name.setText(list.get(position).getLiveChannelName());
		
		
		
		return converView;
	}

}

class PingDaoLiveViewHolder {
	// LinearLayout baoliao_media;
	CircularImage video_head_img;
	TextView video_name;
	ImageView video_img;

}
