package com.dingtai.jinriyongzhou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.TitleFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */

public class XueFragment extends BaseFragment {
    private View mMainView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<BaseFragment> fragments;
    private List<String> titlenames;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.fragment_xue, container, false);
        initView();
        return mMainView;
    }

    private void initView() {
        tabLayout = (TabLayout) mMainView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) mMainView.findViewById(R.id.viewpager);

        fragments = new ArrayList<>();
        NewsActivity wenku = new NewsActivity();
        wenku.setLanmuID("477");
        fragments.add(wenku);
        NewsActivity shufa = new NewsActivity();
        shufa.setLanmuID("361");
        fragments.add(shufa);
        NewsActivity lishi = new NewsActivity();
        lishi.setLanmuID("364");
        fragments.add(lishi);
        NewsActivity yinyue = new NewsActivity();
        yinyue.setLanmuID("445");
        fragments.add(yinyue);

        titlenames = new ArrayList<>();
        titlenames.add("文库");
        titlenames.add("书法");
        titlenames.add("历史");
        titlenames.add("音乐");

        TitleFragmentPagerAdapter adapter = new TitleFragmentPagerAdapter(getChildFragmentManager(), fragments, titlenames);
        viewPager.setAdapter(adapter);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (fragments.get(position) instanceof NewsActivity) {
//                    ((NewsActivity) fragments.get(position)).refresh(1);
                    ((NewsActivity) fragments.get(position)).initData();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                IndicatorTool.setIndicator(tabLayout);
//            }
//        });
    }


}
