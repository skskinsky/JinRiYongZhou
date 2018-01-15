package com.dingtai.jinriyongzhou.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 本风格线使用于首页猜你喜欢
 */
public class IndexLikeDividerItemDecoration extends RecyclerView.ItemDecoration{


	/**
	 * item之间分割线的size，默认为1
	 */
	private int mItemSize = 2 ;

	/**
	 * 绘制item分割线的画笔，和设置其属性
	 * 来绘制个性分割线
	 */
	private Paint mPaint ;

	/**
	 * 构造方法传入布局方向，不可不传
	 * @param context
	 */
	public IndexLikeDividerItemDecoration(Context context) {

		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG) ;
		mPaint.setColor(0xf2f2f2);
		/*设置填充*/
		mPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

		drawHorizontal(c,parent) ;

	}


	/**
	 * 绘制横向 item 分割线
	 * @param canvas
	 * @param parent
	 */
	private void drawHorizontal(Canvas canvas,RecyclerView parent){
		final int top = parent.getPaddingTop() ;
		final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() ;
		final int childSize = parent.getChildCount() ;
		for(int i = 0 ; i < childSize ; i ++){
			if(i%2==0){
				final View child = parent.getChildAt( i ) ;
				RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
				final int left = child.getRight() + layoutParams.rightMargin ;
				final int right = left + mItemSize ;
				canvas.drawRect(left,top,right,bottom,mPaint);
			}
		}
	}

	/**
	 * 设置item分割线的size
	 * @param outRect
	 * @param view
	 * @param parent
	 * @param state
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		final int point = parent.getChildPosition(view)+1;
			if(point%2==1){
				outRect.set(0,0,mItemSize,0);
			}
		


	}
}