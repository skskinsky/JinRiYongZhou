package com.dingtai.jinriyongzhou.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.Circle;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class MainCircleAdapter extends RecyclerView.Adapter<MainCircleAdapter.ViewHolder>
{



	/** 
	 * ItemClick的回调接口 
	 * 
	 */  
	public interface OnItemClickLitener  
	{  
		void onItemClick(View view, int position);  
	}  

	private OnItemClickLitener mOnItemClickLitener;  

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)  
	{  
		this.mOnItemClickLitener = mOnItemClickLitener;  
	}  

	private LayoutInflater mInflater;
	private List<Circle> mDatas;
	private Context context;

	public MainCircleAdapter(Context context, List<Circle> mDatas)
	{
		mInflater = LayoutInflater.from(context);
		this.context = context;
		this.mDatas = mDatas;
	}
	
	public void setData(List<Circle> mDatas){
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder
	{
		public ViewHolder(View arg0)
		{
			super(arg0);
		}

		ImageView mImg;
		TextView mTxt;
	}

	@Override
	public int getItemCount()
	{
		return mDatas.size();
	}

	/**
	 * 创建ViewHolder
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
	{
		View view = mInflater.inflate(R.layout.item_main_circle,
				viewGroup, false);
		ViewHolder viewHolder = new ViewHolder(view);

		viewHolder.mImg = (ImageView) view.findViewById(R.id.main_circle_image);
		viewHolder.mTxt = (TextView) view.findViewById(R.id.main_circle_status);

		LayoutParams par = (LayoutParams) viewHolder.mImg.getLayoutParams();
		par.width = (DisplayMetricsTool.getWidth(context)-DisplayMetricsTool.dip2px(context, 70))/3;
		par.height = par.width;
		viewHolder.mImg.setLayoutParams(par);

		return viewHolder;
	}

	/**
	 * 设置值
	 */
	@SuppressLint("NewApi") @Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int i)
	{

		ImageLoader.getInstance().displayImage(mDatas.get(i).getCircleLogo(), viewHolder.mImg);
		
		viewHolder.mTxt.setText(mDatas.get(i).getFollowNum()+"人关注");
		
//		if(i==1){
//			viewHolder.mTxt.setText("已关注");
//			viewHolder.mTxt.setTextColor(context.getResources().getColor(R.color.Text60GreyColor));
//			viewHolder.mTxt.setBackground(context.getResources().getDrawable(R.drawable.circle_focus_bg));
//		}else{
//			viewHolder.mTxt.setText("关注");
//			viewHolder.mTxt.setTextColor(context.getResources().getColor(R.color.common_color));
//			viewHolder.mTxt.setBackground(context.getResources().getDrawable(R.drawable.new_community));
//		}
//		
//		viewHolder.mTxt.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				viewHolder.mTxt.setText("已关注");
//				viewHolder.mTxt.setTextColor(context.getResources().getColor(R.color.Text60GreyColor));
//				viewHolder.mTxt.setBackground(context.getResources().getDrawable(R.drawable.circle_focus_bg));
//			 
//			}
//		});

		//如果设置了回调，则设置点击事件  
		if (mOnItemClickLitener != null)  
		{  
			viewHolder.itemView.setOnClickListener(new OnClickListener()  
			{  
				@Override  
				public void onClick(View v)  
				{  
					mOnItemClickLitener.onItemClick(viewHolder.itemView, i);  
				}  
			});  

		}  
	}

}
