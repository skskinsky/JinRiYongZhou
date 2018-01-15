package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.model.BaoLiaoMedia;
import com.dingtai.base.model.Photos;
import com.dingtai.base.utils.DateUtil;
import com.dingtai.base.utils.DensityUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.VideoUtils;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.MyGallery;
import com.dingtai.dtbaoliao.model.BaoliaoModel;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.newslib3.activity.TuJiActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class BaoLiaoAdapter extends BaseAdapter {

	private Context context;
	private List<BaoliaoModel> list;
	private ImageAdapter1 picAdapter;
	private DisplayImageOptions options;

	public BaoLiaoAdapter(Context context, List<BaoliaoModel> list) {
		this.context = context;
		this.list = list;
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
				// 默认Bitmap.Config.ARGB_8888
				.showImageOnLoading(R.drawable.user_headimg) // 默认图片
				.showImageForEmptyUri(R.drawable.user_headimg) // url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.user_headimg)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(1)) // 圆角，不需要请删除
				.build();
	}

	public void setData(List<BaoliaoModel> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list == null ? null : list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View converView, ViewGroup arg2) {
		final ViewHolder holder;
		if (converView == null || converView.getTag() == null) {
			holder = new ViewHolder();
			converView = LayoutInflater.from(context).inflate(
					R.layout.fragment_baoliao_item, null);

			holder.baoliao_head = (CircularImage) converView
					.findViewById(R.id.baoliao_head);
			holder.baoliao_name = (TextView) converView
					.findViewById(R.id.baoliao_name);
			holder.baoliao_time = (TextView) converView
					.findViewById(R.id.baoliao_time);
			holder.baoliao_more = (ImageButton) converView
					.findViewById(R.id.baoliao_more);
			holder.baoliao_content = (TextView) converView
					.findViewById(R.id.baoliao_content);
			holder.baoliao_gallery = (MyGallery) converView
					.findViewById(R.id.baoliao_gallery);
			holder.baoliao_picture_num = (TextView) converView
					.findViewById(R.id.baoliao_picture_num);
			holder.baoliao_comment_num = (TextView) converView
					.findViewById(R.id.baoliao_comment_num);
			holder.baoliao_video_num = (TextView) converView
					.findViewById(R.id.baoliao_video_num);
			holder.baoliao_accept = (ImageView) converView
					.findViewById(R.id.baoliao_accept);

			// holder.baoliao_media = (LinearLayout)
			// converView.findViewById(R.id.baoliao_media);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}
		final BaoliaoModel model = list.get(arg0);
		ImageLoader.getInstance().displayImage(model.getUserIcon(),
				holder.baoliao_head, options);
		holder.baoliao_name.setText(model.getUserNickName());
		holder.baoliao_time.setText(DateUtil.formatDate(model.getCreateTime()));

		if (model.getClassName().equalsIgnoreCase("##")||model.getClassName().equals("")) {

			holder.baoliao_content.setText(model.getRevelationContent());

		} else {
			SpannableStringBuilder builder;

			builder = new SpannableStringBuilder(model.getClassName()
					+ "\u3000" + model.getRevelationContent());
			// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
			ForegroundColorSpan redSpan = new ForegroundColorSpan(context
					.getResources().getColor(R.color.common_color));
			builder.setSpan(redSpan, 0, model.getClassName().length(),
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			holder.baoliao_content.setText(builder);

		}

		if (!model.getReadNo().equalsIgnoreCase("0")) {
			holder.baoliao_video_num.setVisibility(View.VISIBLE);
			holder.baoliao_video_num.setText(model.getReadNo());
		} else {
			holder.baoliao_video_num.setText("0");
		}

		if (!model.getCommentCount().equalsIgnoreCase("0")) {
			holder.baoliao_comment_num.setVisibility(View.VISIBLE);
			holder.baoliao_comment_num.setText(model.getCommentCount());
		} else {
			holder.baoliao_comment_num.setText("0");
		}

		if (model.getIsAccept().equalsIgnoreCase("False")) {
			holder.baoliao_accept
					.setImageResource(R.drawable.dt_standard_baoliao_mybaoliaolist_unaccept);
			holder.baoliao_accept.setVisibility(View.GONE);
		} else {
			holder.baoliao_accept.setVisibility(View.VISIBLE);
			holder.baoliao_accept
					.setImageResource(R.drawable.dt_standard_baoliao_mybaoliaolist_accept);
		}
		//
		// holder.baoliao_more.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View view) {
		// int isVisible = holder.baoliao_gallery.getVisibility();
		// if (isVisible == View.GONE) {
		// holder.baoliao_gallery.setVisibility(view.VISIBLE);
		// } else {
		// holder.baoliao_gallery.setVisibility(view.GONE);
		// }
		// }
		// });
		// 图片
		final List<BaoLiaoMedia> list_url = new ArrayList<BaoLiaoMedia>(); // 画廊小图
		final List<BaoLiaoMedia> list_url2 = new ArrayList<BaoLiaoMedia>(); // 图集大图

		list_url.clear();
		list_url2.clear();
		if (!model.getPicCount().equalsIgnoreCase("0")) {

			if (model.getPicCount().equalsIgnoreCase("1")) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("1");
				media.setUrl(model.getPicSmallUrl());
				media.setImageurl(model.getPicSmallUrl());
				list_url.add(media);
			} else {
				for (String s : StringOper.CutStringWithURL(model
						.getPicSmallUrl())) {
					BaoLiaoMedia media = new BaoLiaoMedia();
					media.setType("1");
					media.setUrl(s);
					media.setImageurl(s);
					list_url.add(media);
				}
			}

		}
		if (!model.getVideoCount().equalsIgnoreCase("0")) {

			if (model.getVideoCount().equalsIgnoreCase("1")) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("2");
				media.setUrl(model.getVideoUrl());
				media.setImageurl(model.getVideoImageUrl());
				list_url.add(media);
			} else {
				String url1[] = model.getVideoUrl().split(",");
				String url2[] = model.getVideoImageUrl().split(",");
				for (int i = 0; i < url1.length; i++) {
					BaoLiaoMedia media = new BaoLiaoMedia();
					media.setType("2");
					media.setUrl(url1[i]);
					media.setImageurl(url2[i]);
					list_url.add(media);
				}

			}

		}
		if (!model.getPicCount().equalsIgnoreCase("0")) {

			if (model.getPicCount().equalsIgnoreCase("1")) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("1");
				media.setUrl(model.getPicUrl());
				media.setImageurl(model.getPicUrl());
				list_url2.add(media);
			} else {
				for (String s : StringOper.CutStringWithURL(model.getPicUrl())) {
					BaoLiaoMedia media = new BaoLiaoMedia();
					media.setType("1");
					media.setUrl(s);
					media.setImageurl(s);
					list_url2.add(media);
				}
			}

		}

		if (!model.getVideoCount().equalsIgnoreCase("0")) {

			if (model.getVideoCount().equalsIgnoreCase("1")) {
				BaoLiaoMedia media = new BaoLiaoMedia();
				media.setType("2");
				media.setUrl(model.getVideoUrl());
				media.setImageurl(model.getVideoImageUrl());
				list_url2.add(media);
			} else {
				String url1[] = model.getVideoUrl().split(",");
				String url2[] = model.getVideoImageUrl().split(",");
				for (int i = 0; i < url1.length; i++) {
					BaoLiaoMedia media = new BaoLiaoMedia();
					media.setType("2");
					media.setUrl(url1[i]);
					media.setImageurl(url2[i]);
					list_url2.add(media);
				}

			}

		}

		picAdapter = new ImageAdapter1(context, list_url);
		holder.baoliao_gallery.setAdapter(picAdapter);
		// holder.baoliao_gallery.setSelection(1);
		holder.baoliao_gallery
				.setOnItemClickListener(new MyGallery.IOnItemClickListener() {

					@Override
					public void onItemClick(int position) {
						// TODO 自动生成的方法存根

						// TODO 自动生成的方法存根
						if (list_url2.get(position).getType()
								.equalsIgnoreCase("2")) {
							Intent intent = new Intent();
							VideoUtils.chooeseVideo(context, intent);
							intent.putExtra("video_url", list_url2
									.get(position).getUrl());
							context.startActivity(intent);
						} else {
							ArrayList<Photos> arrs = new ArrayList<Photos>();
							for (int i = 0; i < list_url2.size(); i++) {
								if (list_url2.get(i).getType()
										.equalsIgnoreCase("1")) {
									Photos p = new Photos();
									p.setPicturePath(list_url2.get(i)
											.getImageurl());
									p.setPhotoTitle("爆料图集");
									p.setPhotoDescription(model
											.getRevelationContent());
									arrs.add(p);
								}
							}

							Intent i = new Intent(context, TuJiActivity.class);
							i.putParcelableArrayListExtra("tuji", arrs);
							Log.i("tag", arrs.size() + "arrs");
							i.putExtra("current", 0);
							context.startActivity(i);
						}

					}

				});

		return converView;
	}
}

class ImageAdapter1 extends BaseAdapter {
	private Context context;
	// 图片源数组
	private List<BaoLiaoMedia> list;
	private int width = 0;
	private DisplayImageOptions options;

	public ImageAdapter1(Context c, List list) {
		context = c;
		this.list = list;
		width = DisplayMetricsTool.getWidth(context);
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
				// 默认Bitmap.Config.ARGB_8888
				.showImageOnLoading(R.drawable.default_icon) // 默认图片
				.showImageForEmptyUri(R.drawable.default_icon) // url爲空會显示该图片，自己放在drawable里面的
				.showImageOnFail(R.drawable.default_icon)// 加载失败显示的图片
				.displayer(new RoundedBitmapDisplayer(1)) // 圆角，不需要请删除
				.build();
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list != null ? list.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		final ViewHolder holder;
		if (converView == null || converView.getTag() == null) {
			holder = new ViewHolder();
			converView = LayoutInflater.from(context).inflate(
					R.layout.baoliao_gallery_item, null);

			holder.baoliao_gallery_item_img = (ImageView) converView
					.findViewById(R.id.baoliao_gallery_item_img);
			holder.baoliao_gallery_item_flag = (ImageView) converView
					.findViewById(R.id.baoliao_gallery_item_flag);

			RelativeLayout.LayoutParams para = new RelativeLayout.LayoutParams(
					(int) ((width - DensityUtil.dip2px(context, 60)) / 3),
					(int) (((width - DensityUtil.dip2px(context, 60)) / 3) * 0.75));
			holder.baoliao_gallery_item_img.setLayoutParams(para);
			converView.setTag(holder);
		} else {
			holder = (ViewHolder) converView.getTag();
		}

		ImageLoader.getInstance().displayImage(
				list.get(position).getImageurl(),
				holder.baoliao_gallery_item_img, options);
		if (list.get(position).getType().equalsIgnoreCase("2")) {
			holder.baoliao_gallery_item_flag.setVisibility(View.VISIBLE);
		}
		return converView;
	}
}

class ViewHolder {
	// LinearLayout baoliao_media;
	CircularImage baoliao_head;
	TextView baoliao_name;
	TextView baoliao_time;
	ImageButton baoliao_more;
	TextView baoliao_content;
	// GridView baoliao_pic_grid1;
	TextView baoliao_picture_num;
	TextView baoliao_comment_num;
	TextView baoliao_video_num;
	MyGallery baoliao_gallery;

	ImageView baoliao_gallery_item_img;
	ImageView baoliao_gallery_item_flag;
	ImageView baoliao_accept;

}