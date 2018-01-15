package com.dingtai.jinriyongzhou.view.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/1/12 0012.
 */
public class FixedSpeedScroller extends Scroller {
    private int mDuration = 300;

    public FixedSpeedScroller(Context context) {
        super(context);
        // TODO Auto-generated constructor stub    
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead    
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead    
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * 设置切换时间
     */
    public void setmDuration(int time) {
        mDuration = time;
    }

    /**
     * 获取切换时间
     */
    public int getmDuration() {
        return mDuration;
    }
}  