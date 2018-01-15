package com.dingtai.jinriyongzhou.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.StringOper;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @作者 谢慧强
 * @时间 2015-11-6上午10:07:59
 * @描述 广电视频直播列表适配器
 */
public class ResLiveAdapter extends BaseAdapter {
	private List<LiveChannelModel> list;
	private Context context;
	private ImageLoadingListenerImpl mImageLoadingListenerImpl;
	private DisplayImageOptions option;
	public ResLiveAdapter(List<LiveChannelModel> list,Context context) {
		// TODO 自动生成的构造函数存根
		this.context = context;
		this.list = list;
		option= MyImageLoader.option();
		mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
	}


	public void setData(List<LiveChannelModel> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;  
	}

	@Override
	public View getView(int position,  View arg1, ViewGroup arg2) {
		Holder vh;
		if (arg1 == null) {
			vh = new Holder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_reslive_listview, null);
			vh.reslive_name = (TextView) arg1.findViewById(R.id.reslive_name);
			vh.tv_num = (TextView) arg1.findViewById(R.id.tv_num);
			vh.reslive_img = (ImageView) arg1.findViewById(R.id.reslive_img);
			vh.reslive_flag = (ImageView) arg1.findViewById(R.id.reslive_flag);
			vh.linear_layout = (LinearLayout) arg1.findViewById(R.id.linear_layout);
			
			RelativeLayout.LayoutParams par =(RelativeLayout.LayoutParams) vh.reslive_img.getLayoutParams();
			
			par.width = DisplayMetricsTool.getWidth(context);
			par.height =(int) (par.width*9/ 16);// 控件的宽强制设成1/2
			
			RelativeLayout.LayoutParams text_par =(RelativeLayout.LayoutParams) vh.linear_layout.getLayoutParams();
			text_par.width = DisplayMetricsTool.getWidth(context);
			text_par.height = par.height/5;
		

			vh.linear_layout.setLayoutParams(text_par); //使设置好的布局参数应用到控件
			vh.reslive_name.setLines(1);
			
			vh.reslive_img.setLayoutParams(par);
			arg1.setTag(vh);
		} else {
			vh = (Holder) arg1.getTag();
		}


		vh.reslive_name.setText(list.get(position).getLiveChannelName());
		vh.tv_num.setText(StringOper.getNumString(list.get(position).getReadNo(), "人"));
		
		
		
		
		int strState = -1;
		int Begin = -1;
		int End = -1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		Date strBegin;
		Date strEnd;
		Date strNow;
		try {
			strBegin = formatter.parse(list.get(position).getLiveBeginDate());
			strEnd = formatter.parse(list.get(position).getLiveEndDate());
			strNow = formatter.parse(formatter.format(curDate));
			Begin = strNow.compareTo(strBegin);
			End = strNow.compareTo(strEnd);

			if (End > 0) {// 结束
				strState = 2;
			} else if (Begin >= 0 && End <= 0) {
				strState = 1;
			} else {
				strState = 0;
			}
		} catch (ParseException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(DisplayMetricsTool.dip2px(context, 60),DisplayMetricsTool.dip2px(context,17));
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		lp.leftMargin = DisplayMetricsTool.dip2px(context, 10);
		lp.topMargin = DisplayMetricsTool.dip2px(context, 15);
		vh.reslive_flag.setLayoutParams(lp);
		switch (strState) {
		case 0:// 将开始
		
			vh.reslive_flag.setImageResource(R.drawable.livestatus2);
			break;
		case 1:// 进行中
		
			vh.reslive_flag.setImageResource(R.drawable.livestatus1);

			break;
		case 2:// 结束
			
			vh.reslive_flag.setImageResource(R.drawable.livestatus3);
			break;

		default:
		
			vh.reslive_flag.setImageResource(R.drawable.livestatus3);
			break;
		}

		

		ImageLoader.getInstance().displayImage(list.get(position).getLiveImageUrl(), vh.reslive_img,option, mImageLoadingListenerImpl);

		return arg1;
	}
	public class Holder {
		private ImageView reslive_flag;

		private ImageView reslive_img;
		
		private TextView reslive_name;
		
		private TextView tv_num;

		private LinearLayout linear_layout;
	}
	// 监听图片异步加载
	public static class ImageLoadingListenerImpl extends SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
			if (bitmap != null) {
				ImageView imageView = (ImageView) view;
				boolean isFirstDisplay = !displayedImages.contains(imageUri);
				if (isFirstDisplay) {
					// 图片的淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
