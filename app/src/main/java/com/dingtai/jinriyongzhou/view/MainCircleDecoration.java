package com.dingtai.jinriyongzhou.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dingtai.base.utils.DisplayMetricsTool;

public class MainCircleDecoration extends RecyclerView.ItemDecoration {
    /**
     *
     * @param outRect 边界
     * @param view recyclerView ItemView
     * @param parent recyclerView
     * @param state recycler 内部数据管理
     */
	
	private Context context;
	
	private int offvalue = 0;
	
	public MainCircleDecoration(Context context){
		this.context = context;
		offvalue = DisplayMetricsTool.dip2px(context, 10);
	}
	
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //设定底部边距为1px
      
        if(state.getItemCount()==parent.getChildPosition(view)+1){
        	 outRect.set(offvalue, 0, offvalue, 0);
        }else{
        	  outRect.set(offvalue, 0, 0, 0);
        }
       
        
    }
}