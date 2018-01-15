package com.dingtai.jinriyongzhou.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.dingtai.base.livelib.activtity.ActiveLiveAcitivty;
import com.dingtai.base.livelib.activtity.PingDaoLiveActivity;
import com.dingtai.base.view.LazyLoadFragment;
import com.dingtai.dtvoc.activity.JieMuDianBoActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.TV_ViewPagerAdapter;

import java.util.ArrayList;

public class TVShowActivity extends LazyLoadFragment {

    public ViewPager viewpager;
    // 页面列表
    private ArrayList<Fragment> fragmentList;
    private RadioGroup group;
    private boolean isDisplay = false;

    public void setBackDisplay() {
        isDisplay = true;
    }

    public void select(int poi) {
        viewpager.setCurrentItem(poi);
    }

    @SuppressWarnings("deprecation")
    public void inite() {
        // TODO Auto-generated method stub
        fragmentList = new ArrayList<Fragment>();
        viewpager = findViewById(R.id.viewpager);
        if (isDisplay) {
            View view = findViewById(R.id.title_bar_back);
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
        fragmentList.add(new PingDaoLiveActivity());
        fragmentList.add(new JieMuDianBoActivity());
        fragmentList.add(new ActiveLiveAcitivty());

        viewpager.setAdapter(new TV_ViewPagerAdapter(getChildFragmentManager(), fragmentList));
        viewpager.setOffscreenPageLimit(3);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 == 0) {
                    group.check(R.id.tv_channle_rgBtn1);
                } else if (arg0 == 1) {
                    group.check(R.id.tv_channle_rgBtn2);
                } else {
                    group.check(R.id.tv_channle_rgBtn3);
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });

        //根据ID找到RadioGroup实例
        group = findViewById(R.id.tv_channle_rgBtn);
        //绑定一个匿名监听器
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                switch (arg0.getCheckedRadioButtonId()) {
                    case R.id.tv_channle_rgBtn1:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.tv_channle_rgBtn2:
                        viewpager.setCurrentItem(1);

                        break;
                    case R.id.tv_channle_rgBtn3:
                        viewpager.setCurrentItem(2);

                        break;

                    default:
                        break;
                }
            }
        });

    }


    @Override
    protected int setContentView() {
        // TODO Auto-generated method stub
        return R.layout.activity_tvshow;
    }

    @Override
    protected void initFragmentView() {
        // TODO Auto-generated method stub


    }

    @Override
    protected void loadData() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void loadCache() {
        // TODO Auto-generated method stub
        inite();
    }

    @Override
    protected void stopLoad() {
        // TODO Auto-generated method stub

    }
}
