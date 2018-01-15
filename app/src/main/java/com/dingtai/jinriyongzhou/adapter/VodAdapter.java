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
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.Vod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class VodAdapter extends BaseAdapter {

	private List<Vod> vod;
	private DisplayImageOptions options;
	private Context context;

	public VodAdapter(Context context,List<Vod> Vod) {
		this.vod = Vod;
		this.context = context;
		options = MyImageLoader.option();
	}

	public void setData(List<Vod> Vod) {
		this.vod = Vod;
	}

	@Override
	public int getCount() {
		return vod.size();
	}

	@Override
	public Object getItem(int position) {
		return vod.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_index_vod, null);
			holder.iv_banner = (ImageView) convertView.findViewById(R.id.iv_banner);
			holder.play_flag = (ImageView) convertView.findViewById(R.id.play_flag);
			holder.linear_layout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.play_flag.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		RelativeLayout.LayoutParams par =(RelativeLayout.LayoutParams) holder.iv_banner.getLayoutParams();
		
		par.width = (DisplayMetricsTool.getWidth(context)-2)/2;
		par.height = (int)(par.width/1.78);// 控件的宽强制设成2:3

		holder.iv_banner.setLayoutParams(par); //使设置好的布局参数应用到控件
		
		
		RelativeLayout.LayoutParams text_par =(RelativeLayout.LayoutParams) holder.linear_layout.getLayoutParams();
		text_par.width = (DisplayMetricsTool.getWidth(context)-2)/2;
		text_par.height = par.height*2/3;
		text_par.addRule(RelativeLayout.BELOW, R.id.rel_layout);

		holder.linear_layout.setLayoutParams(text_par); //使设置好的布局参数应用到控件
	
		ImageLoader.getInstance().displayImage(vod.get(position).getProgramContentLogo(),
				holder.iv_banner, options);
		holder.tv_title.setText(vod.get(position).getProgramContentName());
		
		

		return convertView;
	}

	class ViewHolder {
		ImageView iv_banner,play_flag;
		TextView tv_title;
		LinearLayout linear_layout;
	}



}

