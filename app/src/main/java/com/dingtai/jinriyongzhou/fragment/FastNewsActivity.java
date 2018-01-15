package com.dingtai.jinriyongzhou.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.dingtai.base.view.LazyLoadFragment;
import com.dingtai.dtbaoliao.activity.FastNewsFragment;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.TV_ViewPagerAdapter;

import java.util.ArrayList;

public class FastNewsActivity extends LazyLoadFragment {



	private ViewPager mViewPager;
	// 页面列表
	private ArrayList<Fragment> fragmentList;
	private TV_ViewPagerAdapter adapter;
	
	private ImageView baoliao,tuwen;
	
	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.activity_fast_news;
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

	public void inite() {
		// TODO Auto-generated method stub
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		fragmentList = new ArrayList<>();
		fragmentList.add(new FastNewsFragment());
		fragmentList.add(new ZhiBoFragment());
		adapter = new TV_ViewPagerAdapter(getChildFragmentManager(), fragmentList);
		mViewPager.setAdapter(adapter);
		
		baoliao = (ImageView)findViewById(R.id.baoliao);
		tuwen = (ImageView) findViewById(R.id.tuwen);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				switch (arg0) {
				case 0:
					baoliao.setImageDrawable(getResources().getDrawable(R.drawable.index_fastnews1));
					tuwen.setImageDrawable(getResources().getDrawable(R.drawable.index_tuwen));
					break;
					
				case 1:
					baoliao.setImageDrawable(getResources().getDrawable(R.drawable.index_fastnews));
					tuwen.setImageDrawable(getResources().getDrawable(R.drawable.index_tuwen1));
					break;

				default:
					break;
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

		findViewById(R.id.baoliao).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(0);
				baoliao.setImageDrawable(getResources().getDrawable(R.drawable.index_fastnews1));
				tuwen.setImageDrawable(getResources().getDrawable(R.drawable.index_tuwen));
			}
		});

		findViewById(R.id.tuwen).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mViewPager.setCurrentItem(1);
				baoliao.setImageDrawable(getResources().getDrawable(R.drawable.index_fastnews));
				tuwen.setImageDrawable(getResources().getDrawable(R.drawable.index_tuwen1));
			}
		});

	}

	@Override
	protected void stopLoad() {
		// TODO Auto-generated method stub
		
	}



}
