package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dingtai.base.model.BaoLiaoMedia;
import com.dingtai.base.model.Photos;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.VideoUtils;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.IjkVideoView;
import com.dingtai.dtbaoliao.adapter.MediaAdapter;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.TuWenDetailActivity;
import com.dingtai.jinriyongzhou.model.ModelZhiboDetail;
import com.dingtai.jinriyongzhou.view.ImageDecoration;
import com.dingtai.newslib3.activity.TuJiActivity;
import com.dingtai.newslib3.model.NewsDetailModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AdapterZhiboDetail extends BaseAdapter {
	private Context context;
	private List<ModelZhiboDetail> list;
	private String mDate;
	static DateFormat mDf;
	private int maxEms;// 每行显示的字符数
	// 视频控件
	public VideoView mVideoView;// 播放控件
	public ImageView mPlay;// //播放控件开关按钮
	private ProgressBar mpBar;// 进度圈
	// private int mPlayingID = -1;//播放中视频的播放按钮的位置
	// 音频控件
	private TextView mTime;
	private SeekBar mSeekBar;
	public ImageButton mButton;
	public int mPosition = -1;
	private static String path;// 视频播放路径
	private DisplayImageOptions option;
	// 图集ID
	private String strTuJiID = "";
	// 新闻详情模型
	private NewsDetailModel news_detail;
	// 新闻哈希数据
	private HashMap<String, String> news = null;






	public AdapterZhiboDetail(Context context, List<ModelZhiboDetail> list) {
		this.context = context;
		this.list = list;
		mDf = new SimpleDateFormat("yyyy-MM-dd");
		mDate = "";
		int width = DisplayMetricsTool.getWidth(context);
		int textSize = DisplayMetricsTool.sp2px(context, 14);
		int otherWidth = DisplayMetricsTool.dip2px(context, 90);
		maxEms = (width - otherWidth) / textSize;
		option  = new DisplayImageOptions.Builder()  
		.showImageOnLoading(R.drawable.item_zhibo_icon)  
		.showImageOnFail(R.drawable.item_zhibo_icon)  
		.cacheInMemory(true)  
		.cacheOnDisk(true)  
		.bitmapConfig(Bitmap.Config.RGB_565)  
		.build();  
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		HolderZhibo holder = null;
		// 无图处理
		String detail = list.get(position).getNewsLiveContent();
		String title = list.get(position).getNewsLiveTitle();

		try {
			holder = (HolderZhibo) convertView.getTag();
		} catch (Exception e) {
			holder = new HolderZhibo();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_zhibo, null);
			holder.tvzhibodate = (TextView) convertView
					.findViewById(R.id.tvzhibodate);
			holder.tvzhiboname = (TextView) convertView
					.findViewById(R.id.tvzhiboname);
			holder.tvzhibotime = (TextView) convertView
					.findViewById(R.id.tvzhibotime);
			holder.tvzhibotitle = (TextView) convertView
					.findViewById(R.id.tvzhibotitle);
			holder.tvzhiboitem4 = (TextView) convertView
					.findViewById(R.id.tvzhiboitem4);
			holder.tvzhiboitem = (TextView) convertView
					.findViewById(R.id.tvzhiboitem);
			holder.btopentext = (Button) convertView
					.findViewById(R.id.btopentext);
			holder.ivzhiboicon = (CircularImage) convertView
					.findViewById(R.id.ivzhiboicon);
			holder.ivzhiboitem2 = (ImageView) convertView
					.findViewById(R.id.ivzhiboitem2);
			holder.ivzhiboitem3 = (ImageView) convertView
					.findViewById(R.id.ivzhiboitem3);
			holder.vvzhiboitem3 = (IjkVideoView) convertView
					.findViewById(R.id.vvzhiboitem3);
			holder.sbitem4 = (SeekBar) convertView.findViewById(R.id.sbitem4);
			holder.rlzhiboitem4 = (RelativeLayout) convertView
					.findViewById(R.id.rlzhiboitem4);
			holder.btzhiboitem4 = (ImageButton) convertView
					.findViewById(R.id.btzhiboitem4);
			holder.pbzhiboitem4 = (ProgressBar) convertView
					.findViewById(R.id.pbzhiboitem4);
			holder.mRecyclerView = (RecyclerView) convertView
					.findViewById(R.id.medial_recyclerview);

			//设置布局管理器  
			LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);  
			linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); 
			//添加分割线
			holder.mRecyclerView.addItemDecoration(new ImageDecoration());
			holder.mRecyclerView.setLayoutManager(linearLayoutManager);  



			convertView.setTag(holder);
		}
		String CreateTime = list.get(position).getCreateTime();


		//设置适配器  
		final List<BaoLiaoMedia> mDatas = new ArrayList<BaoLiaoMedia>();


		mDatas.clear();

		if(!list.get(position).getPicUrl().equals("")){
			for (String s : StringOper.CutStringWithURL(list.get(position)
					.getPicUrl())) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("1");
				media.setUrl(s);
				media.setImageurl(s);
				mDatas.add(media);
			}
		}

		if(!list.get(position).getMediaPic().equals("")){
			String url1[] = StringOper.CutStringWithURL(list.get(position)
					.getMediaUrl());
			String url2[] = StringOper.CutStringWithURL(list.get(position)
					.getMediaPic());
			for (int i = 0; i < url1.length; i++) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("2");
				media.setUrl(url1[i]);
				media.setImageurl(url2[i]);
				mDatas.add(media);
			}
		}


		MediaAdapter   mMediaAdapter = new MediaAdapter(context, mDatas);
		mMediaAdapter.setOnItemClickLitener(new MediaAdapter.OnItemClickLitener()  
		{  
			@Override  
			public void onItemClick(View view, int poi)  
			{  
				// TODO 自动生成的方法存根
				if (mDatas.get(poi).getType()
						.equalsIgnoreCase("2")) {
					Intent intent = new Intent();
					VideoUtils.chooeseVideo(
							context, intent);
					intent.putExtra("video_url",
							mDatas.get(poi).getUrl());
					context.startActivity(intent);
				} else {
					ArrayList<Photos> arrs = new ArrayList<Photos>();
					for (int j = 0; j < mDatas.size(); j++) {
						if (mDatas.get(j).getType()
								.equalsIgnoreCase("1")) {
							Photos p = new Photos();
							p.setPicturePath(mDatas.get(j)
									.getImageurl());
							p.setPhotoTitle("直播间图集");
							p.setPhotoDescription(list.get(position).getNewsLiveContent());
							arrs.add(p);
						}
					}

					Intent i = new Intent(context,
							TuJiActivity.class);
					i.putParcelableArrayListExtra("tuji", arrs);
					Log.i("tag", arrs.size() + "arrs");
					i.putExtra("current", 0);
					context.startActivity(i);
				}



			}  
		});
		holder.mRecyclerView.setAdapter(mMediaAdapter); 



		holder.rlzhiboitem4.setVisibility(View.GONE);
		holder.ivzhiboitem3.setVisibility(View.GONE);
		holder.vvzhiboitem3.setVisibility(View.GONE);
		holder.ivzhiboitem2.setVisibility(View.GONE);
		holder.tvzhiboitem.setVisibility(View.GONE);
		holder.pbzhiboitem4.setVisibility(View.GONE);

		holder.tvzhibodate.setText(CreateTime);
		holder.tvzhibodate.setVisibility(View.VISIBLE);


		// 设置直播员名字
		holder.tvzhiboname.setText(list.get(position).getUserName());
		// 设置直播员icon
		if(!list.get(position).getUserLogo().equals(""))
			ImageLoader.getInstance().displayImage(list.get(position).getUserLogo(), holder.ivzhiboicon,option);

		if(title.equals("")){
			holder.tvzhibotitle.setVisibility(View.GONE);
		}else{
			// 标题
			holder.tvzhibotitle.setText(title);
			holder.tvzhibotitle.setVisibility(View.VISIBLE);
		}
		if(!detail.equals("")){
			holder.tvzhiboitem.setVisibility(View.VISIBLE);
			holder.tvzhiboitem.setText(detail);
		}else{
			holder.tvzhiboitem.setVisibility(View.GONE);
		}
		// 收起长短文字
		if (detail.length() / maxEms > 2) {
			holder.btopentext.setVisibility(View.VISIBLE);
			// 内容数大于2行时,显示maxEms+8 个字
			if (list.get(position).isOpen) {
				holder.tvzhiboitem.setText(detail);
				holder.btopentext.setText(Html.fromHtml("<u>" + "点击收起全文"
						+ "</u>"));
			} else {
				String text = StringOper.CutStringWithDot((maxEms + 8), detail)
						+ "...";
				holder.tvzhiboitem.setText(text);
				holder.btopentext.setText(Html.fromHtml("<u>" + "点击展开全文"
						+ "</u>"));
			}

			// TextHolder textHolder = new TextHolder();
			// textHolder.text = text;
			// textHolder.textdetail = detail;
			// textHolder.TextView = holder.tvzhiboitem;
			// textHolder.button = holder.btopentext;
			holder.btopentext.setTag(position);
			holder.btopentext.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					int num = (Integer) arg0.getTag();
					list.get(num).setOpen(!list.get(num).isOpen);
					TuWenDetailActivity.mAdapter.notifyDataSetChanged();
					// TextHolder textHolder = (TextHolder) arg0.getTag();
					// if (textHolder.TextView.getText().length() / maxEms > 2)
					// {
					// int h1=textHolder.TextView.getMeasuredHeight();
					// textHolder.TextView.setText(textHolder.text);
					// int h2=textHolder.TextView.getMeasuredHeight();
					// textHolder.button.setText(Html.fromHtml("<u>" + "点击展开全文"
					// + "</u>"));
					// SetHeight(-23*8);
					// } else {
					// int h1=textHolder.TextView.getMeasuredHeight();
					// textHolder.TextView.setText(textHolder.textdetail);
					// int h2=textHolder.TextView.getMeasuredHeight();
					// Log.i("test", "h1:"+h1+"h2"+h2);
					// textHolder.button.setText(Html.fromHtml("<u>" + "点击收起全文"
					// + "</u>"));
					// SetHeight(23*8);
					// }

				}
			});
		} else {
			holder.btopentext.setVisibility(View.GONE);
			holder.tvzhiboitem.setText(detail);
		}
		// NewsLiveType
		// 1、文字2、图片3、音频4、视频
		String NewsLiveType = list.get(position).getNewsLiveType();
		if ("1".equals(NewsLiveType)) {

		} else if ("2".equals(NewsLiveType)) {
			//			if (WutuSetting.getIsImg()) {
			//				holder.ivzhiboitem2.setVisibility(View.VISIBLE);
			//				ImageLoader.getInstance().displayImage(
			//						list.get(position).getPicUrl(), holder.ivzhiboitem2,
			//						option, mImageLoadingListenerImpl);
			//				holder.ivzhiboitem2.setOnClickListener(new OnClickListener() {
			//
			//					@Override
			//					public void onClick(View v) {
			//						ModelZhiboDetail modelZhiboDetail = list.get(position);
			//						Photos photo = new Photos();
			//						photo.setPhotoTitle(modelZhiboDetail.getEventName());
			//						photo.setCreateTime(modelZhiboDetail.getCreateTime());
			//						photo.setPicturePath(modelZhiboDetail.getPicUrl());
			//						photo.setID(modelZhiboDetail.getID());
			//						photo.setPhotoDescription(modelZhiboDetail
			//								.getNewsLiveContent());
			//						Intent intent = new Intent();
			//						intent.setClass(context, TuJiActivity.class);
			//						List<Photos> photos = new ArrayList<Photos>();
			//						photos.add(photo);
			//						intent.putParcelableArrayListExtra("tuji",
			//								(ArrayList<? extends Parcelable>) photos);
			//						context.startActivity(intent);
			//					}
			//				});
			//			}

		} else if ("3".equals(NewsLiveType)) {
			//			holder.btopentext.setVisibility(View.VISIBLE);
			//			holder.tvzhiboitem.setVisibility(View.VISIBLE);
			//			holder.rlzhiboitem4.setVisibility(View.GONE);
			//			AudioIDHolder audioIDHolder = new AudioIDHolder();
			//			audioIDHolder.position = position;
			//			audioIDHolder.btzhiboitem4 = holder.btzhiboitem4;
			//			audioIDHolder.sbitem4 = holder.sbitem4;
			//			audioIDHolder.tvzhiboitem4 = holder.tvzhiboitem4;
			//			audioIDHolder.url = list.get(position).getAudioUrl();
			//			holder.btzhiboitem4.setTag(audioIDHolder);
			//		 holder.btzhiboitem4.setOnClickListener(new myAudioID());
			//holder.btzhiboitem4.setOnClickListener(new myMediaID());
		} else if ("4".equals(NewsLiveType)) {
			//			holder.btopentext.setVisibility(View.GONE);
			//			holder.tvzhiboitem.setVisibility(View.GONE);
			//			holder.ivzhiboitem2.setVisibility(View.VISIBLE);
			//			ImageLoader.getInstance().displayImage(
			//					list.get(position).getMediaPic(), holder.ivzhiboitem2,
			//					option, mImageLoadingListenerImpl);
			//			holder.ivzhiboitem3.setVisibility(View.VISIBLE);
			//			MediaIDHolder mediaIDHolder = new MediaIDHolder();
			//			mediaIDHolder.path = list.get(position).getMediaUrl();
			//			mediaIDHolder.playButton = holder.ivzhiboitem3;
			//			mediaIDHolder.videoView = holder.vvzhiboitem3;
			//			mediaIDHolder.pBar = holder.pbzhiboitem4;
			//			mediaIDHolder.position = position;
			//			holder.ivzhiboitem3.setTag(mediaIDHolder);
			//			holder.ivzhiboitem3.setOnClickListener(new myMediaID());
		}
		// String imgUrl = list.get(position).getPicUrl();
		// String AudioUrl = list.get(position).getAudioUrl();
		// String MediaUrl = list.get(position).getMediaUrl();
		// if (!TextUtils.isEmpty(AudioUrl)) {
		// // AudioID音频ID不为空则为item4
		// holder.rlzhiboitem4.setVisibility(View.VISIBLE);
		// AudioIDHolder audioIDHolder = new AudioIDHolder();
		// audioIDHolder.position = position;
		// audioIDHolder.btzhiboitem4 = holder.btzhiboitem4;
		// audioIDHolder.sbitem4 = holder.sbitem4;
		// audioIDHolder.tvzhiboitem4 = holder.tvzhiboitem4;
		// audioIDHolder.url = AudioUrl;
		// holder.btzhiboitem4.setTag(audioIDHolder);
		// holder.btzhiboitem4.setOnClickListener(new myAudioID());
		// } else if (!TextUtils.isEmpty(imgUrl)) {
		// // PicUrl图片不为空则为item2或3
		// holder.ivzhiboitem2.setVisibility(View.VISIBLE);
		// ImageLoader.getInstance().displayImage(imgUrl, holder.ivzhiboitem2,
		// mImageLoadingListenerImpl);
		//
		// if (!TextUtils.isEmpty(MediaUrl)) {
		// // MediaID视频不为空则为item3
		// holder.ivzhiboitem3.setVisibility(View.VISIBLE);
		// MediaIDHolder mediaIDHolder = new MediaIDHolder();
		// mediaIDHolder.path = MediaUrl;
		// mediaIDHolder.playButton = holder.ivzhiboitem3;
		// mediaIDHolder.videoView = holder.vvzhiboitem3;
		// mediaIDHolder.pBar = holder.pbzhiboitem4;
		// mediaIDHolder.position = position;
		// holder.ivzhiboitem3.setTag(mediaIDHolder);
		// holder.ivzhiboitem3.setOnClickListener(new myMediaID());
		// // holder.vvzhiboitem3.setVisibility(View.VISIBLE);
		// } else {
		// //为item2时
		// holder.tvzhiboitem.setVisibility(View.VISIBLE);
		// if (detail.length() / maxEms > 2) {
		// //内容数大于2行时,显示maxEms+8 个字
		// holder.tvzhiboitem.setText(StringOper.CutStringWithDot((maxEms + 8),
		// detail) + "...");
		// holder.btopentext.setVisibility(View.VISIBLE);
		// holder.btopentext.setText(Html.fromHtml("<u>" + "点击展开全文" + "</u>"));
		// TextHolder textHolder = new TextHolder();
		// textHolder.text = detail;
		// textHolder.TextView = holder.tvzhiboitem;
		// textHolder.button = holder.btopentext;
		// holder.btopentext.setTag(textHolder);
		// holder.btopentext.setOnClickListener(new OnClickListener() {
		// public void onClick(View arg0) {
		// TextHolder textHolder = (TextHolder) arg0.getTag();
		// textHolder.TextView.setText(textHolder.text);
		// textHolder.button.setVisibility(View.GONE);
		// }
		// });
		// } else {
		//
		// holder.tvzhiboitem.setText(detail);
		// }
		// //
		// }
		//
		// } else {
		// //为普通文本类型
		// // 设置详细文本
		// holder.tvzhiboitem.setVisibility(View.VISIBLE);
		// holder.tvzhiboitem.setText(detail);
		//
		// }

		return convertView;
	}



	private class MediaIDHolder {
		private ProgressBar pBar;
		public VideoView videoView;
		public String path;
		public ImageView playButton;
		public int position;
	}

	private class TextHolder {
		public TextView TextView;
		public String text;
		public String textdetail;
		public Button button;
	}

	private class AudioIDHolder {
		public ImageButton btzhiboitem4;
		public SeekBar sbitem4;
		public TextView tvzhiboitem4;
		public String url;
		public int position;
	}

	private class HolderZhibo {

		public TextView tvzhibodate;
		public TextView tvzhiboname;
		public TextView tvzhibotime;
		public CircularImage ivzhiboicon;
		public TextView tvzhibotitle;
		public TextView tvzhiboitem;
		public Button btopentext;

		public ImageView ivzhiboitem2;
		public IjkVideoView vvzhiboitem3;
		public ImageView ivzhiboitem3;

		public RelativeLayout rlzhiboitem4;
		public ImageButton btzhiboitem4;
		public SeekBar sbitem4;
		public TextView tvzhiboitem4;
		public ProgressBar pbzhiboitem4;
		public RecyclerView mRecyclerView;
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

	public static String dateChoice(String date1, String date2) {
		try {
			Date dt1 = mDf.parse(date1);
			Date dt2 = mDf.parse(date2);
			if (dt1.getTime() < dt2.getTime()) {
				return date1;
			}
		} catch (Exception e) {

		}
		return "";
	}

	private final Handler myAudioIDHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 成功
			case 10:
				// HolderMediaPlayer holder = (HolderMediaPlayer) msg.obj;

				break;
				// 失败
			case 0:
				Toast.makeText(context, "音频无法播放！", Toast.LENGTH_SHORT).show();
				((ImageButton) msg.obj)
				.setImageResource(R.drawable.zhibo_seekbar_play);
				((ImageButton) msg.obj).setEnabled(true);
				break;
				// 视频 初始化成功
			case 100:
				mpBar.setVisibility(View.GONE);
				break;
				// 视频 播放错误
			case 9090:
				Toast.makeText(context, "打开视频失败!", Toast.LENGTH_SHORT).show();
				mVideoView.stopPlayback();
				mVideoView.setVisibility(View.GONE);
				mPlay.setVisibility(View.VISIBLE);
				mpBar.setVisibility(View.GONE);
				mVideoView = null;
				mPlay = null;
				mpBar = null;
				break;
			case 9010:
				mPlay.setVisibility(View.VISIBLE);
				// startErr.setVisibility(View.VISIBLE);
				break;
				// VideoView点击事件
			case 101:
				mPlay.setVisibility(View.VISIBLE);
				mpBar.setVisibility(View.GONE);
				mVideoView.pause();

				break;
			}

		}
	};


	private class myMediaID implements OnClickListener {

		public void onClick(View v) {
			MediaIDHolder mediaIDHolder = (MediaIDHolder) v.getTag();
			Intent intent = new Intent();
			// Intent intent = new Intent(context, NewsVideoPlay.class);
			VideoUtils.chooeseVideo(context, intent);
			intent.putExtra("video_url", mediaIDHolder.path);
			context.startActivity(intent);
			// if (mPosition == mediaIDHolder.position && mVideoView != null) {
			// mVideoView.start();
			//
			// mediaIDHolder.playButton.setVisibility(View.GONE);
			// } else {
			// if (mVideoView != null) {
			// mVideoView.stopPlayback();
			// mVideoView.setVisibility(View.GONE);
			// mPlay.setVisibility(View.VISIBLE);
			// mpBar.setVisibility(View.GONE);
			// mVideoView = null;
			// mPlay = null;
			// mpBar = null;
			// }
			// if (mPlayer != null) {
			// try {
			// if (mPlayer.getMediaPlayer().isPlaying()) {
			// mPlayer.getMediaPlayer().pause();
			// mPlayer.mWasPlaying = false;
			// }
			// } catch (Exception e) {
			// }
			// mPlayer.interrupt();
			// mButton.setEnabled(true);
			// mSeekBar.setProgress(0);
			// mButton.setImageResource(R.drawable.zhibo_seekbar_play);
			// mTime.setText("0:00");
			// mPlayer = null;
			// mSeekBar = null;
			// mButton = null;
			// mTime = null;
			// }
			// mPosition = mediaIDHolder.position;
			// mpBar = mediaIDHolder.pBar;
			// mPlay = mediaIDHolder.playButton;
			// mPlay.setVisibility(View.GONE);
			// mVideoView = mediaIDHolder.videoView;
			// mVideoView.setVideoPath(mediaIDHolder.path);
			// mVideoView.start();
			// mVideoView.setVisibility(View.VISIBLE);
			// mpBar.setVisibility(View.VISIBLE);
			// mVideoView.setOnCompletionListener(new OnCompletionListener() {
			//
			// @Override
			// public void onCompletion(MediaPlayer mp) {
			// // TODO Auto-generated method stub
			//
			// Log.i("通知", "播放完成");
			// // Log.i("www", "onCompletion 播放完成");
			// myAudioIDHandler.sendEmptyMessage(9010);
			// }
			// });
			// mVideoView.setOnErrorListener(new OnErrorListener() {
			//
			// @Override
			// public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
			// Log.i("通知", "播放中出现错误");
			// myAudioIDHandler.sendEmptyMessage(9090);
			// // Log.i("www", "setOnErrorListener");
			// return true;
			// }
			// });
			// mVideoView.setOnPreparedListener(new OnPreparedListener() {
			//
			// @Override
			// public void onPrepared(MediaPlayer arg0) {
			// // TODO Auto-generated method stub
			// // Log.i("www", "670 预处理完成 onPrepared");
			// myAudioIDHandler.sendEmptyMessage(100);
			// }
			// });
			// mVideoView.setOnTouchListener(new OnTouchListener() {
			//
			// public boolean onTouch(View v, MotionEvent event) {
			// // TODO Auto-generated method stub
			// myAudioIDHandler.sendEmptyMessage(101);
			// return false;
			// }
			// });
			//
			// }
		}

	}

}
