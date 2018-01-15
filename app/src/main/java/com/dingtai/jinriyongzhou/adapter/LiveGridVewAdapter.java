package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.view.AlignTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LiveGridVewAdapter extends BaseAdapter {

	private List<LiveChannelModel> list;
	private DisplayImageOptions options;
	private Context context;

	public LiveGridVewAdapter(Context context,List<LiveChannelModel> list) {
		this.list = list;
		this.context = context;
		options = MyImageLoader.option();
	}

	public void setData(List<LiveChannelModel> list) {
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
	public View getView(final int position, View arg1, ViewGroup parent) {
		
		

		Holder vh;
		if (arg1 == null) {
			vh = new Holder();
			arg1 = LayoutInflater.from(context).inflate(R.layout.item_reslive_listview, null);
			vh.reslive_name = (AlignTextView) arg1.findViewById(R.id.reslive_name);
			vh.reslive_img = (ImageView) arg1.findViewById(R.id.reslive_img);
			vh.reslive_flag = (ImageView) arg1.findViewById(R.id.reslive_flag);
			vh.linear_layout = (LinearLayout) arg1.findViewById(R.id.linear_layout);
			
			RelativeLayout.LayoutParams par =(RelativeLayout.LayoutParams) vh.reslive_img.getLayoutParams();
			
			par.width = (DisplayMetricsTool.getWidth(context)-2)/2;
			par.height = par.width*2/3;// 控件的宽强制设成2:3
			
			RelativeLayout.LayoutParams text_par =(RelativeLayout.LayoutParams) vh.linear_layout.getLayoutParams();
			text_par.width = (DisplayMetricsTool.getWidth(context)-2)/2;
			text_par.height = par.height*1/4;
		

			vh.linear_layout.setLayoutParams(text_par); //使设置好的布局参数应用到控件
			
			
			vh.reslive_img.setLayoutParams(par);
			arg1.setTag(vh);
		} else {
			vh = (Holder) arg1.getTag();
		}

		 
		vh.reslive_name.setText(list.get(position).getLiveChannelName());		
	
		ImageLoader.getInstance().displayImage(list.get(position).getLiveImageUrl(), vh.reslive_img);
		
		
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
		return arg1;

		
	}

	public class Holder {
		private ImageView reslive_flag;

		private ImageView reslive_img;
		
		private AlignTextView reslive_name;

		private LinearLayout linear_layout;
	}

}
