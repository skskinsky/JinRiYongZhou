package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dingtai.dtbaoliao.model.BaoliaoModel;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.HomeActivity;
import com.dingtai.jinriyongzhou.view.DTAdverView;

import java.util.List;

/**
 * Created by Administrator on 2016/3/20.
 * 京东广告栏数据适配器
 *
 */

public class JDViewAdapter {
    private List<BaoliaoModel> mDatas;
    private Context mContext;

    public JDViewAdapter(Context context,List<BaoliaoModel> mDatas) {
    	this.mContext = context;
        this.mDatas = mDatas;
        if (mDatas == null || mDatas.isEmpty()) {
            throw new RuntimeException("nothing to show");
        }
    }

    /**
     * 获取数据的条数
     * @return
     */
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    /**
     * 获取摸个数据
     * @param position
     * @return
     */
    public BaoliaoModel getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 获取条目布局
     * @param parent
     * @return
     */
    public View getView(DTAdverView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adverview_item, null);
    }

    /**
     * 条目数据适配
     * @param view
     */
    public void setItem(final View view, final BaoliaoModel model) {

        TextView tv = (TextView) view.findViewById(R.id.title);
        
//    	if (model.getClassName().equalsIgnoreCase("##")||model.getClassName().equals("")) {

			tv.setText(model.getRevelationContent());

//		} else {
//			SpannableStringBuilder builder;
//
//			builder = new SpannableStringBuilder("【"+model.getClassName()+"】"
//					 + model.getRevelationContent());
//			// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
//			ForegroundColorSpan redSpan = new ForegroundColorSpan(mContext
//					.getResources().getColor(R.color.news_ad_color));
//			builder.setSpan(redSpan, 0, model.getClassName().length()+2,
//					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//
//			tv.setText(builder);
//
//		}
//        
    

        //你可以增加点击事件
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	HomeActivity home = (HomeActivity)mContext;
//				home.showFaseNews();
               
            }
        });
    }
}
