package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.model.ActiveModel;
import com.dingtai.base.newsHolder.ActiveViewHolder3;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作 者： 李亚军 时 间：2015-1-29 上午10:04:18 活动适配
 */
public class ActiveAdapter extends BaseAdapter {

	private Context context;
	private List<ActiveModel> list;
	public List<ActiveModel> delList = new ArrayList<ActiveModel>();
	/**
	 * 是否是我的活动
	 */
	private boolean isMy = false;


	public void setList(List<ActiveModel> list) {
		this.list = list;
	}

	public void setIsMyActivity(boolean isMy) {
		this.isMy = isMy;
	}

	/**
	 * 
	 */
	public ActiveAdapter(Context context, List<ActiveModel> list) {
		this.context = context;
		this.list = list;
		// TODO 自动生成的构造函数存根
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		}
		return 0;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		if (list != null && position < list.size()) {
			return list.get(position);
		}
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ActiveViewHolder3 activeViewHolder3 = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.goods_activities_item, parent, false);
			activeViewHolder3 = new ActiveViewHolder3();
			activeViewHolder3.setImgActive((ImageView) convertView
					.findViewById(R.id.imgActive));
			activeViewHolder3.setImgState((ImageView) convertView
					.findViewById(R.id.imgState));
			activeViewHolder3.setTxtEndTime((TextView) convertView
					.findViewById(R.id.tv_time));
			activeViewHolder3.setTxtLox((TextView) convertView
					.findViewById(R.id.tv_lox));
			activeViewHolder3.setActiveName((TextView) convertView
					.findViewById(R.id.activities_tv3));

			
			
			RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(context),(int) (DisplayMetricsTool.getWidth(context)*9/ 16/5));

			layoutParams1.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.imgActive);
			activeViewHolder3.getActiveName().setLayoutParams(layoutParams1);
			
			
			
			RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
					,(int)((DisplayMetricsTool.getWidth(context)) *9/ 16));
			activeViewHolder3.getImgActive().setLayoutParams(layoutParams2);
			

			convertView.setTag(activeViewHolder3);
		} else {
			activeViewHolder3 = (ActiveViewHolder3) convertView.getTag();
		}


	



		// 获取图片
		ImageLoader.getInstance().displayImage(
				list.get(position).getActiveLogo(),
				activeViewHolder3.getImgActive());

		int strState = -1;
		int Begin = -1;
		int End = -1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		Date strBegin;
		Date strEnd;
		Date strNow;
		try {
			strBegin = formatter.parse(list.get(position).getBeginDate());
			strEnd = formatter.parse(list.get(position).getEndDate());
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
			activeViewHolder3.getImgState().setVisibility(View.INVISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		switch (strState) {
		case 0:// 将开始

			break;
		case 1:// 进行中
			activeViewHolder3.getImgState().setBackgroundResource(
					R.drawable.hh_active_ing);

			break;
		case 2:// 结束
			activeViewHolder3.getImgState().setBackgroundResource(
					R.drawable.hh_active_over);

			break;

		default:
			activeViewHolder3.getImgState().setVisibility(View.INVISIBLE);
			break;
		}

		activeViewHolder3.getActiveName().setText(
				list.get(position).getActiveName());
		activeViewHolder3.getTxtEndTime().setText(list.get(position).getBeginDate()+"--"+list.get(position).getEndDate());
		activeViewHolder3.getTxtLox().setText("活动地点："+list.get(position).getActiveAdress());
		return convertView;
	}

}
