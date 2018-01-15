package com.dingtai.jinriyongzhou.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.jinriyongzhou.R;

/**
 * @author 谢慧强 广电视频直播列表
 */
public class LiveVideoListActivity2 extends BaseFragment {
    private View mMainView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyAdapter myAdapter;
    private String[] tabs;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.activity_live_video_list1,
                container, false);
        intiView();

        return mMainView;
    }


    private void intiView() {
        mTabLayout = (TabLayout) mMainView.findViewById(R.id.mTabLayout);
        mViewPager = (ViewPager) mMainView.findViewById(R.id.mViewPager);

        tabs = new String[]{"电视频道", "广播频率", "现场直播", "点播"};
        myAdapter = new MyAdapter(getChildFragmentManager());
        mViewPager.setAdapter(myAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.common_color));

    }

    public class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            if (position != tabs.length - 1) {
                return LiveVideoListActivity.getInstance(position + 1 + "");
            } else {
                return new DianboListFragment();

            }

        }

        @Override
        public int getCount() {

            return tabs == null ? 0 : tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
