package com.dingtai.jinriyongzhou.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.view.CustomViewPager;
import com.dingtai.dtpolitics.activity.AskQuestionActivity;
import com.dingtai.dtpolitics.fragment.HallFragment;
import com.dingtai.dtpolitics.fragment.InstitutionFragment;
import com.dingtai.dtpolitics.fragment.LeadershipFragment;
import com.dingtai.dtpolitics.fragment.MineFragment;

import java.util.ArrayList;

/**
 * 政务中心
 *
 * @author sk
 */
public class PrimaryActivity extends BaseFragmentActivity {

	/*
     * //大厅，机构，领导，我的 private Class fragmentArray[] =
	 * {HallFragment.class,InstitutionFragment.class,
	 * LeadershipFragment.class,MineFragment.class}; //按钮文字 private String
	 * mTextArray[] = {"大厅","机构","领导","我的"}; //按钮图片 private int mImageArray[] =
	 * { R.drawable.dt_standard_index_shouye,
	 * R.drawable.dt_standard_index_shangcheng,
	 * R.drawable.dt_standard_index_baoliao, R.drawable.dt_standard_index_wode};
	 */

    // 大厅，机构，领导，我的
    private TextView tv_dating, tv_jigou, tv_lingdao, tv_wode;
    public CustomViewPager viewpager;
    private ArrayList<Fragment> mFragment;
    private int color1, color2;
    private ImageView ib_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        setContentView(com.dingtai.dtpolitics.R.layout.activity_primary);

        initView();
        initFragment();
        initeTitle();
        setTitle("大厅");
    }

    private void initFragment() {
        // TODO 自动生成的方法存根
        mFragment = new ArrayList<Fragment>();
        mFragment.add(new HallFragment());
        mFragment.add(new InstitutionFragment());
        mFragment.add(new LeadershipFragment());
        mFragment.add(new MineFragment());
        viewpager
                .setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        viewpager.setOffscreenPageLimit(4);
    }

    private void initView() {
        // fragment = getSupportFragmentManager();
        color1 = Color.parseColor("#333333");
        color2 = getResources().getColor(com.dingtai.dtpolitics.R.color.common_color);
        // fragment = getSupportFragmentManager();
        viewpager = (CustomViewPager) findViewById(com.dingtai.dtpolitics.R.id.viewpager);
        tv_dating = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_tvDating);
        tv_jigou = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_tvJigou);
        tv_lingdao = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_tvLingdao);
        tv_wode = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_tvWode);
        viewpager.setNoScroll(true);
        findViewById(com.dingtai.dtpolitics.R.id.relese_politics).setOnClickListener(this);

        ib_search = (ImageView) findViewById(com.dingtai.dtpolitics.R.id.title_bar_search);
        ib_search.setOnClickListener(this);
        ib_search.setVisibility(View.GONE);
        tv_dating.setOnClickListener(this);
        tv_jigou.setOnClickListener(this);
        tv_lingdao.setOnClickListener(this);
        tv_wode.setOnClickListener(this);

        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            Drawable mDating = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_dating);
            Drawable mDating1 = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_dating1);

            Drawable mJigou = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_jigou1);
            Drawable mJigou1 = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_jigou);

            Drawable mLingdao = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_lingdao1);
            Drawable mLingdao1 = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_lingdao);

            Drawable mWode = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_wode1);
            Drawable mWode1 = getResources().getDrawable(
                    com.dingtai.dtpolitics.R.drawable.dt_standard_zhengwu_wode);

            @Override
            public void onPageSelected(int arg0) {
                // TODO 自动生成的方法存根
                mDating.setBounds(0, 0, mDating.getMinimumWidth(),
                        mDating.getMinimumHeight());
                mDating1.setBounds(0, 0, mDating1.getMinimumWidth(),
                        mDating1.getMinimumHeight());

                mJigou.setBounds(0, 0, mJigou.getMinimumWidth(),
                        mJigou.getMinimumHeight());
                mJigou1.setBounds(0, 0, mJigou1.getMinimumWidth(),
                        mJigou1.getMinimumHeight());

                mLingdao.setBounds(0, 0, mLingdao.getMinimumWidth(),
                        mLingdao.getMinimumHeight());
                mLingdao1.setBounds(0, 0, mLingdao1.getMinimumWidth(),
                        mLingdao1.getMinimumHeight());

                mWode.setBounds(0, 0, mWode.getMinimumWidth(),
                        mWode.getMinimumHeight());
                mWode1.setBounds(0, 0, mWode1.getMinimumWidth(),
                        mWode1.getMinimumHeight());

                switch (arg0) {
                    case 0:
                        setTitle("大厅");
                        tv_dating.setTextColor(color2);
                        tv_jigou.setTextColor(color1);
                        tv_lingdao.setTextColor(color1);
                        tv_wode.setTextColor(color1);

                        tv_dating.setCompoundDrawables(null, mDating, null, null);
                        tv_jigou.setCompoundDrawables(null, mJigou, null, null);
                        tv_lingdao.setCompoundDrawables(null, mLingdao, null, null);
                        tv_wode.setCompoundDrawables(null, mWode, null, null);
                        break;
                    case 1:
                        setTitle("机构");
                        tv_dating.setTextColor(color1);
                        tv_jigou.setTextColor(color2);
                        tv_lingdao.setTextColor(color1);
                        tv_wode.setTextColor(color1);

                        tv_dating.setCompoundDrawables(null, mDating1, null, null);
                        tv_jigou.setCompoundDrawables(null, mJigou1, null, null);
                        tv_lingdao.setCompoundDrawables(null, mLingdao, null, null);
                        tv_wode.setCompoundDrawables(null, mWode, null, null);
                        break;
                    case 2:
                        setTitle("领导");
                        tv_dating.setTextColor(color1);
                        tv_jigou.setTextColor(color1);
                        tv_lingdao.setTextColor(color2);
                        tv_wode.setTextColor(color1);

                        tv_dating.setCompoundDrawables(null, mDating1, null, null);
                        tv_jigou.setCompoundDrawables(null, mJigou, null, null);
                        tv_lingdao
                                .setCompoundDrawables(null, mLingdao1, null, null);
                        tv_wode.setCompoundDrawables(null, mWode, null, null);
                        break;
                    case 3:
                        setTitle("我的");
                        tv_dating.setTextColor(color1);
                        tv_jigou.setTextColor(color1);
                        tv_lingdao.setTextColor(color1);
                        tv_wode.setTextColor(color2);

                        tv_dating.setCompoundDrawables(null, mDating1, null, null);
                        tv_jigou.setCompoundDrawables(null, mJigou, null, null);
                        tv_lingdao.setCompoundDrawables(null, mLingdao, null, null);
                        tv_wode.setCompoundDrawables(null, mWode1, null, null);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO 自动生成的方法存根

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO 自动生成的方法存根

            }
        });
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragment.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }
    }

    @Override
    public void onClick(View arg0) {

        super.onClick(arg0);

        int i = arg0.getId();
        if (i == com.dingtai.dtpolitics.R.id.tv_tvDating) {
            setTitle("大厅");
            viewpager.setCurrentItem(0);

        } else if (i == com.dingtai.dtpolitics.R.id.tv_tvJigou) {
            setTitle("机构");
            viewpager.setCurrentItem(1);

        } else if (i == com.dingtai.dtpolitics.R.id.tv_tvLingdao) {
            setTitle("领导");
            viewpager.setCurrentItem(2);

        } else if (i == com.dingtai.dtpolitics.R.id.tv_tvWode) {
            if (Assistant.getUserInfoByOrm(this) == null) {
                Toast.makeText(getApplicationContext(), "请先登录!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(basePackage + "login");
                startActivity(intent);
            } else {
                setTitle("我的");
                viewpager.setCurrentItem(3);
            }

        } else if (i == com.dingtai.dtpolitics.R.id.title_bar_search) {
            Toast.makeText(getApplicationContext(), "搜索", Toast.LENGTH_SHORT)
                    .show();

        } else if (i == com.dingtai.dtpolitics.R.id.relese_politics) {
            if (Assistant.getUserInfoByOrm(this) == null) {
                Toast.makeText(getApplicationContext(), "请先登录!",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(basePackage+"login");
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(),
                        AskQuestionActivity.class);
                intent.putExtra("cityName", "永州市");
                intent.putExtra("cityParentId", "1");
                startActivity(intent);
            }

        } else {
        }
    }
}
