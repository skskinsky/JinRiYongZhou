package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.NewsADs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

public class ButtomMenuAdapter extends BaseAdapter {

	private List<NewsADs> ads;
	private DisplayImageOptions options;
	private Context context;
	private boolean isPushAD = false;

	public ButtomMenuAdapter(Context context,List<NewsADs> ads) {
		this.ads = ads;
		this.context = context;
		options  = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.default_icon)
		.showImageForEmptyUri(R.drawable.default_icon)
		.showImageOnFail(R.drawable.default_icon)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
		.displayer(new RoundedBitmapDisplayer(10))
		.build();
	}

	public void setData(List<NewsADs> ads) {
		this.ads = ads;
	}

	@Override
	public int getCount() {
		return ads.size();
	}

	@Override
	public Object getItem(int position) {
		return ads.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_ads, null);
			holder.iv_banner = (ImageView) convertView.findViewById(R.id.iv_banner);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_title1 = (TextView) convertView.findViewById(R.id.tv_title1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		RelativeLayout.LayoutParams par =(RelativeLayout.LayoutParams) holder.iv_banner.getLayoutParams();

		par.width = DisplayMetricsTool.getWidth(context);
		par.height = par.width/5;// 控件的宽强制设成2:3

		holder.iv_banner.setLayoutParams(par); //使设置好的布局参数应用到控件


		NewsADs ad = ads.get(position);
		ImageLoader.getInstance().displayImage(ad.getImgUrl(),
				holder.iv_banner, options);

		holder.tv_title.setVisibility(View.GONE);

		return convertView;
	}

	class ViewHolder {
		ImageView iv_banner;
		TextView tv_title,tv_title1;
	}

}

