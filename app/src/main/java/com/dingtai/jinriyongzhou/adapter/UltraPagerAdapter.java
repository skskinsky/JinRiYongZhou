/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dingtai.base.livelib.activtity.LiveAudioActivity;
import com.dingtai.base.livelib.activtity.LiveMainActivity;
import com.dingtai.base.livelib.activtity.PictureAndTextLive;
import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by mikeafc on 15/11/26.
 */
public class UltraPagerAdapter extends PagerAdapter {

	private List<LiveChannelModel> resLive_gridlist;
	private Context context;

	public UltraPagerAdapter(Context context, List<LiveChannelModel> resLive_gridlist,boolean isMultiScr) {
		this.resLive_gridlist = resLive_gridlist;
		this.context = context;
	}

	private boolean isMultiScr;


	@Override
	public int getCount() {
		return resLive_gridlist.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		LinearLayout view = (LinearLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.layout_child, null);
		//new LinearLayout(container.getContext());
		TextView reslive_name = (TextView) view.findViewById(R.id.reslive_name);

		ImageView reslive_img = (ImageView) view.findViewById(R.id.reslive_img);
		ImageView reslive_flag = (ImageView) view.findViewById(R.id.reslive_flag);


		LayoutParams par =new LayoutParams(DisplayMetricsTool.getWidth(context)/2, DisplayMetricsTool.getWidth(context)/2*9/16);

		reslive_img.setLayoutParams(par);

		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		lp.leftMargin = DisplayMetricsTool.dip2px(context, 7);
		lp.topMargin = DisplayMetricsTool.dip2px(context, 7);

		reslive_flag.setLayoutParams(lp);


		reslive_name.setText(resLive_gridlist.get(position).getLiveChannelName());		

		ImageLoader.getInstance().displayImage(resLive_gridlist.get(position).getLiveImageUrl(),reslive_img);



		int strState = -1;
		int Begin = -1;
		int End = -1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		Date strBegin;
		Date strEnd;
		Date strNow;
		try {
			strBegin = formatter.parse(resLive_gridlist.get(position).getLiveBeginDate());
			strEnd = formatter.parse(resLive_gridlist.get(position).getLiveEndDate());
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


		if(resLive_gridlist.get(position).getLiveType().equals("3")){	//活动直播
			reslive_flag.setImageResource(R.drawable.live_status_1);
			if(strState==2){
				reslive_flag.setImageResource(R.drawable.live_status_3);
			}


		}else if(resLive_gridlist.get(position).getLiveType().equals("2")){ //FM直播
			reslive_flag.setImageResource(R.drawable.live_status_4);
			if(strState==2){
				reslive_flag.setImageResource(R.drawable.live_status_3);
			}

		}

		else if(resLive_gridlist.get(position).getLiveType().equals("1")){ //频道直播
			reslive_flag.setImageResource(R.drawable.live_status_2);
			if(strState==2){
				reslive_flag.setImageResource(R.drawable.live_status_3);
			}

		}


		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {  

				// TODO Auto-generated method stub


				Intent intent = null;
				if ("1".equals(resLive_gridlist.get(position).getLiveType())) {
					intent = new Intent(context,
							LiveMainActivity.class);
					intent.putExtra("VideoUrl", resLive_gridlist.get(position)
							.getVideoUrl());
					intent.putExtra("RTMPUrl", resLive_gridlist.get(position)
							.getLiveRTMPUrl());
					intent.putExtra("PicPath", resLive_gridlist.get(position)
							.getLiveImageUrl());
					intent.putExtra("ID", resLive_gridlist.get(position).getID());
					intent.putExtra("Week", resLive_gridlist.get(position).getWeek());
					intent.putExtra("CommentsNum", resLive_gridlist.get(position)
							.getCommentsNum());
					intent.putExtra("name", resLive_gridlist.get(position)
							.getLiveProgramName());
					intent.putExtra("livename", resLive_gridlist.get(position)
							.getLiveChannelName());
					intent.putExtra("nowtime", resLive_gridlist.get(position)
							.getLiveProgramDate());
					intent.putExtra("isLive", resLive_gridlist.get(position).getIsLive());
					intent.putExtra("picUrl", resLive_gridlist.get(position).getPicPath());
					intent.putExtra("startTime", resLive_gridlist.get(position)
							.getLiveBeginDate());
					intent.putExtra("logo", resLive_gridlist.get(position)
							.getLiveBeginLogo());
					intent.putExtra("newsID", resLive_gridlist.get(position)
							.getLiveNewChID());
					intent.putExtra("liveType", 1);
				} else if ("2".equals(resLive_gridlist.get(position).getLiveType())) {
					intent = new Intent(context,
							LiveAudioActivity.class);
					intent.putExtra("VideoUrl", resLive_gridlist.get(position)
							.getVideoUrl());
					intent.putExtra("RTMPUrl", resLive_gridlist.get(position)
							.getLiveRTMPUrl());
					intent.putExtra("ID", resLive_gridlist.get(position).getID());
					intent.putExtra("Week", resLive_gridlist.get(position).getWeek());
					intent.putExtra("CommentsNum", resLive_gridlist.get(position)
							.getCommentsNum());
					intent.putExtra("name", resLive_gridlist.get(position)
							.getLiveProgramName());
					intent.putExtra("channelName", resLive_gridlist.get(position)
							.getLiveChannelName());
					intent.putExtra("nowtime", resLive_gridlist.get(position)
							.getLiveProgramDate());
					intent.putExtra("startTime", resLive_gridlist.get(position)
							.getLiveBeginDate());
					intent.putExtra("logo", resLive_gridlist.get(position)
							.getLiveBeginLogo());
					intent.putExtra("newsID", resLive_gridlist.get(position)
							.getLiveNewChID());
					intent.putExtra("picUrl", resLive_gridlist.get(position).getLiveImageUrl());
				}if ("3".equals(resLive_gridlist.get(position).getLiveType())) {
					intent = new Intent(context,
							LiveMainActivity.class);



					switch (getStatus(resLive_gridlist.get(position))) {
					case 0:// 灏嗗紑濮�
						intent.putExtra("RTMPUrl", resLive_gridlist.get(position)
								.getLiveRTMPUrl());
						break;
					case 1:// 杩涜涓�
						intent.putExtra("RTMPUrl", resLive_gridlist.get(position)
								.getLiveRTMPUrl());
						break;
					case 2:// 缁撴潫
						intent.putExtra("RTMPUrl", resLive_gridlist.get(position)
								.getLiveLink());
						break;
					}

					intent.putExtra("VideoUrl", resLive_gridlist.get(position)
							.getVideoUrl());

					intent.putExtra("PicPath", resLive_gridlist.get(position)
							.getLiveImageUrl());
					intent.putExtra("ID", resLive_gridlist.get(position).getID());
					intent.putExtra("Week", resLive_gridlist.get(position).getWeek());
					intent.putExtra("CommentsNum", resLive_gridlist.get(position)
							.getCommentsNum());
					intent.putExtra("name", resLive_gridlist.get(position)
							.getLiveProgramName());
					intent.putExtra("livename", resLive_gridlist.get(position)
							.getLiveChannelName());
					intent.putExtra("nowtime", resLive_gridlist.get(position)
							.getLiveProgramDate());
					intent.putExtra("isLive", resLive_gridlist.get(position).getIsLive());
					intent.putExtra("picUrl", resLive_gridlist.get(position).getPicPath());
					intent.putExtra("startTime", resLive_gridlist.get(position)
							.getLiveBeginDate());
					intent.putExtra("logo", resLive_gridlist.get(position)
							.getLiveBeginLogo());
					intent.putExtra("liveEvent", resLive_gridlist.get(position)
							.getLiveEventID());
					intent.putExtra("newsID", resLive_gridlist.get(position)
							.getLiveNewChID());
					intent.putExtra("status", resLive_gridlist.get(position)
							.getLiveBeginStatus());
					intent.putExtra("liveType", 2);
					intent.putExtra("LiveLink",resLive_gridlist.get(position).getLiveLink());
					intent.putExtra("LiveBeginType", resLive_gridlist.get(position).getLiveBeginType());
					intent.putExtra("LiveBeginMedia", resLive_gridlist.get(position).getLiveBeginMedia());

				} else if ("4".equals(resLive_gridlist.get(position).getLiveType())) {
					intent = new Intent(context,
							PictureAndTextLive.class);
					intent.putExtra("zhiboid", resLive_gridlist.get(position).getID());
					intent.putExtra("imglogin", resLive_gridlist.get(position)
							.getLiveBeginLogo());
					intent.putExtra("EventName", resLive_gridlist.get(position)
							.getLiveChannelName());

				}
				context.startActivity(intent);




			}
		});



		container.addView(view);

		return view;
	}


	private int getStatus(LiveChannelModel model){
		int strState = -1;
		int Begin = -1;
		int End = -1;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 鑾峰彇褰撳墠鏃堕棿
		Date strBegin;
		Date strEnd;
		Date strNow;
		try {
			strBegin = formatter.parse(model.getLiveBeginDate());
			strEnd = formatter.parse(model.getLiveEndDate());
			strNow = DateTool.convertStrToDate2(formatter.format(curDate));
			Begin = strNow.compareTo(strBegin);
			End = strNow.compareTo(strEnd);

			if (End > 0) {// 缁撴潫
				strState = 2;
			} else if (Begin >= 0 && End <= 0) {
				strState = 1;
			} else {
				strState = 0;
			}
		} catch (ParseException e) {
			return strState;
		} catch (Exception e) {
			return strState;

		} 

		return strState;

	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		LinearLayout view = (LinearLayout) object;
		container.removeView(view);
	}
}
