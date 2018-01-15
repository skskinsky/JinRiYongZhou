package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.dtbaoliao.model.ModelZhiboList;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class AdapterZhiboList extends BaseAdapter {
	private Context context;
	private List<ModelZhiboList> list;
	private ImageLoadingListenerImpl mImageLoadingListenerImpl;
	Drawable background1;// 正在结束
	private DisplayImageOptions option;

	public AdapterZhiboList(Context context, List<ModelZhiboList> list) {
		this.context = context;
		this.list = list;
		mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
		// addTestDate();
		option = MyImageLoader.option();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// 无图处理
		String detail = list.get(position).getEventSurmmy();
		String title = list.get(position).getEventName();
		// String type = list.get(position).getEventStatus();// 角标
		// String pinglun = list.get(position).getJoinNum().trim();
		// 无图判断
		// 0：直播未开始；1：正在直播；3：直播结束
		String EventStatus = list.get(position).getEventStatus();// 直播状态
		if (!WutuSetting.getIsImg()) {
			// if ("True".equals(EventStatus)) {
			// detail = "正在直播";
			//
			// } else {
			// // detail = "直播结束";
			// detail = "已经结束";
			// }
			if ("0".equals(EventStatus)) {
				detail = "直播未开始";

			} else if ("1".equals(EventStatus)) {
				// detail = "直播结束";
				detail = "正在直播";
			} else if ("2".equals(EventStatus)) {
				detail = "直播已关闭";
			} else {
				detail = "直播结束";
			}
			return WutuSetting.getView(context, convertView, title, detail,
					"5", "", "");
		}
		try {
			holder = (ViewHolder) convertView.getTag();
		} catch (Exception e) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_zhibo_list, null);
			holder.llitem1 = (LinearLayout) convertView
					.findViewById(R.id.llitem1);
			holder.llitem2 = (LinearLayout) convertView
					.findViewById(R.id.llitem2);
			holder.llitem3 = (LinearLayout) convertView
					.findViewById(R.id.llitem3);
			holder.tvitem1title = (TextView) convertView
					.findViewById(R.id.tvitem1title);
			holder.tvitem2title = (TextView) convertView
					.findViewById(R.id.tvitem2title);
			holder.tvitem3title = (TextView) convertView
					.findViewById(R.id.tvitem3title);
			holder.tvitem2detail = (TextView) convertView
					.findViewById(R.id.tvitem2detail);
			holder.tvitem3detail = (TextView) convertView
					.findViewById(R.id.tvitem3detail);
			holder.ivitem1img = (ImageView) convertView
					.findViewById(R.id.ivitem1img);
			holder.ivitem2img = (ImageView) convertView
					.findViewById(R.id.ivitem2img);
			holder.ivitem3img = (ImageView) convertView
					.findViewById(R.id.ivitem3img);
			holder.tvitem1type = (TextView) convertView
					.findViewById(R.id.tvitem1type);
			holder.tvitem2type = (TextView) convertView
					.findViewById(R.id.tvitem2type);
			holder.tvitem3type = (TextView) convertView
					.findViewById(R.id.tvitem3type);
			
			holder.tv_date = (TextView) convertView
					.findViewById(R.id.tv_date);
			
			holder.tv_week = (TextView) convertView
					.findViewById(R.id.tv_week);
			
			holder.tvitem2time = (TextView) convertView.findViewById(R.id.tvitem2time);
			
			convertView.setTag(holder);
		}
		holder.llitem1.setVisibility(View.GONE);
		holder.llitem2.setVisibility(View.GONE);
		holder.llitem3.setVisibility(View.GONE);
		
		
		
		String dateString =  list.get(position).getCreateTime();
		String month = "";
		String day = "";
		try  
		{  
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		    Date date = sdf.parse(dateString); 
		    if(date.getMonth()<10){
		    	month = "0"+(date.getMonth()+1);
		    }else{
		    	month = ""+(date.getMonth()+1);
		    }
		    
		    if(date.getDate()<10){
		    	day = "0"+date.getDate();
		    }else{
		    	day = ""+date.getDate();
		    }
		    
		    switch(date.getDay()){
		    case 1:
		    	holder.tv_week.setText("星期一");
		    	break;
		    case 2:
		    	holder.tv_week.setText("星期二");
		    	break;
		    case 3:
		    	holder.tv_week.setText("星期三");
		    	break;
		    case 4:
		    	holder.tv_week.setText("星期四");
		    	break;
		    case 5:
		    	holder.tv_week.setText("星期五");
		    	break;
		    case 6:
		    	holder.tv_week.setText("星期六");
		    	break;
		    case 7:
		    	holder.tv_week.setText("星期日");
		    	break;
		    }
		    
			
		  
		}  
		catch (java.text.ParseException e)  
		{  
		    System.out.println(e.getMessage());  
		}  
		
		month =  month+"/";
		
		SpannableString WordtoSpan = new SpannableString(month+day);  
		WordtoSpan.setSpan(new AbsoluteSizeSpan(24,true), 0, month.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new AbsoluteSizeSpan(18,true), month.length()-1, month.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new AbsoluteSizeSpan(14,true), month.length(), (month+day).length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
         
		holder.tv_date.setText(WordtoSpan, TextView.BufferType.SPANNABLE);  
		
		holder.tvitem2time.setText("直播时间:  "+list.get(0).getCreateTime());

		String EventLogo = list.get(position).getEventLogo();// img
		String csstype = list.get(position).getEventCSS();
		// if (position == 0) {
		// holder.llitem1.setVisibility(View.VISIBLE);
		// ImageLoader.getInstance().displayImage(EventLogo, holder.ivitem1img,
		// mImageLoadingListenerImpl);
		// holder.tvitem1title.setText(title);
		// if (!"1".equals(EventStatus)) {
		// // 状态部不为直播时
		// if ("2".equals(EventStatus)) {
		// holder.tvitem1type.setText("直播关闭");
		// } else {
		// holder.tvitem1type.setText("直播结束");
		// }
		//
		// holder.tvitem1type.setTextColor(Color.parseColor("#ADADAD"));
		// holder.tvitem1type.setBackgroundResource(R.drawable.bac_huise);
		//
		// }
		// } else
		if ("2".equals(csstype) || "1".equals(csstype)) {
			// 上图下字
			holder.llitem2.setVisibility(View.VISIBLE);

			LayoutParams layoutParams = (LayoutParams) holder.ivitem2img.getLayoutParams();
			layoutParams.width = DisplayMetricsTool.getWidth(context) -DisplayMetricsTool.dip2px(context, 100);
			layoutParams.height = layoutParams.width*9/16;
			holder.ivitem2img.setLayoutParams(layoutParams);

			ImageLoader.getInstance().displayImage(EventLogo,
					holder.ivitem2img, option, mImageLoadingListenerImpl);
			 holder.tvitem2title.setText(title);

			if ("1".equals(csstype)||"2".equals(csstype)) {
				holder.tvitem2detail.setText(detail);
				holder.tvitem2detail.setVisibility(View.VISIBLE);
			} else {
				holder.tvitem2detail.setVisibility(View.GONE);
			}
			
			
//			if ("0".equals(EventStatus)) {
//				// 状态部不为直播时
//				holder.tvitem2type.setText("直播未开始");
//				holder.tvitem2type.setTextColor(Color.parseColor("#ADADAD"));
//				holder.tvitem2type.setBackgroundResource(R.drawable.bac_huise);
//
//				SpannableStringBuilder builder = new SpannableStringBuilder(
//						"【 直播未开始 】" + title);
//				ForegroundColorSpan Span1 = new ForegroundColorSpan(
//						Color.parseColor("#ADADAD"));
//				builder.setSpan(Span1, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				holder.tvitem2title.setText(builder);
//
//			} else if ("1".equals(EventStatus)) {
//				holder.tvitem2type.setText("正在直播");
//				holder.tvitem2type.setTextColor(Color.parseColor("#11cd6e"));
//				holder.tvitem2type.setBackgroundResource(R.drawable.bac_green);
//
//				SpannableStringBuilder builder = new SpannableStringBuilder(
//						"【 正在直播 】" + title);
//				ForegroundColorSpan Span1 = new ForegroundColorSpan(
//						Color.parseColor("#11cd6e"));
//				builder.setSpan(Span1, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				holder.tvitem2title.setText(builder);
//
//			} else {
//				holder.tvitem2type.setText("直播结束");
//				holder.tvitem2type.setTextColor(Color.parseColor("#ADADAD"));
//				holder.tvitem2type.setBackgroundResource(R.drawable.bac_huise);
//
//				SpannableStringBuilder builder = new SpannableStringBuilder(
//						"【 直播结束 】" + title);
//				ForegroundColorSpan Span1 = new ForegroundColorSpan(
//						Color.parseColor("#ADADAD"));
//				builder.setSpan(Span1, 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				holder.tvitem2title.setText(builder);
//
//			}
		} else {
			// 左图右字
			holder.llitem3.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(EventLogo,
					holder.ivitem3img, option, mImageLoadingListenerImpl);
			holder.tvitem3title.setText(title);
			holder.tvitem3detail.setText(detail);

			if ("0".equals(EventStatus)) {
				// 状态部不为直播时
				holder.tvitem2type.setText("直播未开始");
				holder.tvitem2type.setTextColor(Color.parseColor("#ADADAD"));
				holder.tvitem2type.setBackgroundResource(R.drawable.bac_huise);

			} else if ("1".equals(EventStatus)) {
				holder.tvitem2type.setText("正在直播");
				holder.tvitem2type.setTextColor(Color.parseColor("#11cd6e"));
				holder.tvitem2type.setBackgroundResource(R.drawable.bac_green);
			} else {
				holder.tvitem2type.setText("直播结束");
				holder.tvitem2type.setTextColor(Color.parseColor("#ADADAD"));
				holder.tvitem2type.setBackgroundResource(R.drawable.bac_huise);
			}
		}

		return convertView;
	}


	class ViewHolder {

		public LinearLayout llitem1;
		public LinearLayout llitem2;
		public LinearLayout llitem3;
		public TextView tvitem1title;
		public TextView tvitem2title;
		public TextView tvitem3title;
		public TextView tvitem2detail;
		public TextView tvitem3detail;
		public TextView tvitem1type;
		public TextView tvitem2type;
		public TextView tvitem3type;
		public ImageView ivitem1img;
		public ImageView ivitem2img;
		public ImageView ivitem3img;
		
		public TextView tv_date;
		public TextView tv_week;
		public TextView tvitem2time;
		
	}

	// 监听图片异步加载
	public static class ImageLoadingListenerImpl extends
			SimpleImageLoadingListener {

		public static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
			if (bitmap != null) {
				ImageView imageView = (ImageView) view;
				boolean isFirstDisplay = !displayedImages.contains(imageUri);
				if (isFirstDisplay) {
					// 图片的淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
					System.out.println("===> loading " + imageUri);
				}
			}
		}
	}
}
