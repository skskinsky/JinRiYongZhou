package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.CJIndexNewsListModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MyGridVewAdapter extends BaseAdapter {

	private List<CJIndexNewsListModel> CJIndexNewsListModels;
	private DisplayImageOptions options;
	private Context context;

	public MyGridVewAdapter(Context context,List<CJIndexNewsListModel> CJIndexNewsListModels) {
		this.CJIndexNewsListModels = CJIndexNewsListModels;
		this.context = context;
		options = MyImageLoader.option();
	}

	public void setData(List<CJIndexNewsListModel> CJIndexNewsListModels) {
		this.CJIndexNewsListModels = CJIndexNewsListModels;
	}

	@Override
	public int getCount() {
		return CJIndexNewsListModels.size();
	}

	@Override
	public Object getItem(int position) {
		return CJIndexNewsListModels.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_activity_normal, null);
			holder.iv_banner = (ImageView) convertView.findViewById(R.id.iv_banner);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_title1 = (TextView) convertView.findViewById(R.id.tv_title1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		RelativeLayout.LayoutParams par =(RelativeLayout.LayoutParams) holder.iv_banner.getLayoutParams();
		
		par.width = DisplayMetricsTool.getWidth(context)/2-DisplayMetricsTool.dip2px(context, 1);
		par.height = par.width*2/3;// 控件的宽强制设成2:3

		holder.iv_banner.setLayoutParams(par); //使设置好的布局参数应用到控件
		
		
		CJIndexNewsListModel CJIndexNewsListModel = CJIndexNewsListModels.get(position);
		ImageLoader.getInstance().displayImage(CJIndexNewsListModel.getSmallPicUrl(),
				holder.iv_banner, options);
		holder.tv_title.setText(CJIndexNewsListModel.getTitle());
//		if(!"".equals(CJIndexNewsListModel.getSummary())){
//			holder.tv_title1.setText(CJIndexNewsListModel.getSummary());
//		}else{
//			holder.tv_title1.setText("今日永州为您推荐");
//		}
		return convertView;
	}

	class ViewHolder {
		ImageView iv_banner;
		TextView tv_title,tv_title1;
	}

}
