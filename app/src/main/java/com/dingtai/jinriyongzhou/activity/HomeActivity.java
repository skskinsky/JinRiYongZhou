package com.dingtai.jinriyongzhou.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.base.application.ExitApplication;
import com.dingtai.base.model.UpdateVersionModel;
import com.dingtai.base.receiver.NetstateReceiver;
import com.dingtai.base.utils.AppUploadUtil;
import com.dingtai.base.view.CustomViewPager;
import com.dingtai.base.view.ZDYProgressDialog;
import com.dingtai.dtactivities.activity.ActivitiesActivity;
import com.dingtai.dtflash.model.StartPageModel;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.application.MyApplication;
import com.dingtai.jinriyongzhou.fragment.ActivityUserCenter;
import com.dingtai.jinriyongzhou.fragment.HallFragment1;
import com.dingtai.jinriyongzhou.fragment.IndexNewsActivity2;
import com.dingtai.jinriyongzhou.fragment.MinFragment;
import com.dingtai.jinriyongzhou.fragment.XueFragment;
import com.dingtai.newslib3.model.ParentChannelModel;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseFragmentActivity {

    private String tag = "IndexNewsActivity";
    // 栏目列表
    ExpandableListView mExpandableListView;
    // 栏目列表
    ListView lvParent;
    ListView lvSub;

    public static int ChaItem = 0;
    private CustomViewPager mViewPager;
    public static String lanmuname = "新闻";
    public static String lanmuID;// 新闻栏目id
    public static String TypeGo = "";// 频道跳转判断
    /**
     * 首页LOGO效果
     */
    private ImageView home_iv;
    // 页面列表
    private ArrayList<Fragment> fragmentList;
    public List<ParentChannelModel> channelBeans;
    private NetstateReceiver netReceiver;
    FrameLayout my_content_view;
    // private TextView net_net;

    public static UpdateVersionModel versionModel;// 服务器返回版本信息
    private ZDYProgressDialog dialog;
    private AppUploadUtil app;
    private SharedPreferences sp;
    private boolean isUpload = false;
    private RuntimeExceptionDao<UpdateVersionModel, String> UpdateVersion;// 更新版本信息
    public static Float localVersion = (float) 1.0;// 本地安装版本
    public static Float serverVersion = (float) 1.0;// 服务器版本
    private UpdateVersionModel model;
    private MyApplication myApp;
    private ZDYProgressDialog loading;
    private boolean isMoreChange = false;
    private boolean isAdd = false;
    // （再按一次退出程序）
    private long exitTime = 0;
    private IndexNewsActivity2 indexNewsActivity;
    private ActivitiesActivity activeActivity;
    private RuntimeExceptionDao<StartPageModel, String> startPage;
    public SharedPreferences spAnimation;
    private ImageView iv_xinwen, iv_wenzheng, iv_bianmin, iv_wenhua, iv_wode;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 6666:
//                    if (loading != null && loading.getDialog() != null
//                            && loading.getDialog().isShowing()) {
//                        loading.dismissDialog();
//                    }
                    break;
                case 100:
//                    if (dialog != null) {
//                        dialog.dismissDialog();
//                    }
//                    boolean f = (Boolean) msg.obj;
//
//                    if (f) {
//                        UpdateManager update = new UpdateManager(
//                                HomeActivity.this, app.getVersionModel());
//                        update.Update(R.string.app_name + "", app.getVersionModel()
//                                .getDownLoadUrl());
//                        sp.edit().putString("isUpload", "Upload").commit();
//                    } else {
//                        sp.edit().putString("isUpload", "notUpload").commit();
//                        if (isUpload) {
//                            Toast.makeText(getApplication(), "已经是最新的版本",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_home);
        Log.e(tag, "onCreate");
        myApp = (MyApplication) getApplication();
        spAnimation = getSharedPreferences("spAnimation", Context.MODE_PRIVATE);
        sp = getSharedPreferences("XGFLAG", Context.MODE_PRIVATE);
        // 检查更新
//        UpdateVersion = getHelper().getMode(UpdateVersionModel.class);
//        localVersion = (float) Float.valueOf("1.0");
//        Message msg = new Message();
//        msg.what = 6666;
//        handler.sendMessageDelayed(msg, 1000 * 3);
//
//        try {
//            if (UpdateVersion.isTableExists()) {
//                List<UpdateVersionModel> queryForAll = UpdateVersion
//                        .queryForAll();
//                if (queryForAll != null && queryForAll.size() > 0) {
//                    model = queryForAll.get(0);
//                }
//                if (model != null) {
//                    serverVersion = Float.parseFloat(model.getVersionNo());
//                }
//            }
//        } catch (Exception e) {
//        }
        // 初始化控件
        initView();
        String parterid = sp.getString("parterid", "");
//        if (parterid != null && !"".equals(parterid)) {
//            Assistant.NEWS_PARENTER = parterid;
//        }
//        try {
//            if (myApp != null && myApp.isUpload()) {
//                app = new AppUploadUtil(HomeActivity.this, handler);
//                app.updateVersion();
//                myApp.setUpload(false);
//            }
//        } catch (Exception e) {
//        }
        Intent intent = getIntent();

        if ("CHI".equals(intent.getStringExtra("CHI"))) {
            loading = new ZDYProgressDialog(this);
            loading.createDialog("正在加载...");
            loading.showDialog();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void initView() {
        home_iv = (ImageView) findViewById(R.id.home_iv);
        mViewPager = (CustomViewPager) findViewById(R.id.viewpager);
        mViewPager.setNoScroll(true);
        iv_xinwen = (ImageView) findViewById(R.id.iv_xinwen);
        iv_wenzheng = (ImageView) findViewById(R.id.iv_wenzheng);
        iv_bianmin = (ImageView) findViewById(R.id.iv_bianmin);
        iv_wenhua = (ImageView) findViewById(R.id.iv_wenhua);
        iv_wode = (ImageView) findViewById(R.id.iv_wode);

        iv_xinwen.setOnClickListener(this);
        iv_wenzheng.setOnClickListener(this);
        iv_bianmin.setOnClickListener(this);
        iv_wenhua.setOnClickListener(this);
        iv_wode.setOnClickListener(this);

        Intent intent = getIntent();

        startPage = getHelper().getMode(StartPageModel.class);
        initePager();
    }

    private void initePager() {
        fragmentList = new ArrayList<Fragment>();
        indexNewsActivity = new IndexNewsActivity2();
//        IndexNewsFragment indexNewsFragment = new IndexNewsFragment();
        fragmentList.add(indexNewsActivity);
//        fragmentList.add(new WenFragment());
//        fragmentList.add(new ())PrimaryFragment;
        fragmentList.add(new HallFragment1());
        fragmentList.add(new XueFragment());
        fragmentList.add(new MinFragment());
        activeActivity = new ActivitiesActivity();
//        fragmadd(new YunFragment());
  
        fragmentList.add(new ActivityUserCenter());
        mViewPager
                .setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(5);

    }

    @Override
    protected void onDestroy() {
        if (netReceiver != null) {
            this.unregisterReceiver(netReceiver);
            netReceiver = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 可以根据多个请求代码来作相应的操作
        if (20 == resultCode) {
            isAdd = true;
        }
        if (1 == resultCode) {
            isMoreChange = true;
        }
        if(RESULT_OK==resultCode){
            indexNewsActivity.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMoreChange) {
            indexNewsActivity.cjNewsActivity.resultFromMore();
            isMoreChange = false;
        }
        if (isAdd) {
            indexNewsActivity.addSub();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isAdd = false;
        isMoreChange = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ExitApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_xinwen:
                mViewPager.setCurrentItem(0);
                iv_xinwen.setImageResource(R.drawable.dt_wen_blue);
                iv_wenzheng.setImageResource(R.drawable.dt_we_black);
                iv_bianmin.setImageResource(R.drawable.dt_min_black);
                iv_wenhua.setImageResource(R.drawable.dt_yun_black);
                iv_wode.setImageResource(R.drawable.dt_wo_black);

                break;
            case R.id.iv_wenzheng:

                mViewPager.setCurrentItem(1);
                iv_xinwen.setImageResource(R.drawable.dt_wen_black);
                iv_wenzheng.setImageResource(R.drawable.dt_we_blue);
                iv_bianmin.setImageResource(R.drawable.dt_min_black);
                iv_wenhua.setImageResource(R.drawable.dt_yun_black);
                iv_wode.setImageResource(R.drawable.dt_wo_black);
                break;
            case R.id.iv_bianmin:
                mViewPager.setCurrentItem(2);
                iv_xinwen.setImageResource(R.drawable.dt_wen_black);
                iv_wenzheng.setImageResource(R.drawable.dt_we_black);
                iv_bianmin.setImageResource(R.drawable.dt_min_blue);
                iv_wenhua.setImageResource(R.drawable.dt_yun_black);
                iv_wode.setImageResource(R.drawable.dt_wo_black);
                break;
            case R.id.iv_wenhua:
                mViewPager.setCurrentItem(3);
                iv_xinwen.setImageResource(R.drawable.dt_wen_black);
                iv_wenzheng.setImageResource(R.drawable.dt_we_black);
                iv_bianmin.setImageResource(R.drawable.dt_min_black);
                iv_wenhua.setImageResource(R.drawable.dt_yun_blue);
                iv_wode.setImageResource(R.drawable.dt_wo_black);
                break;
            case R.id.iv_wode:
                mViewPager.setCurrentItem(4);
                iv_xinwen.setImageResource(R.drawable.dt_wen_black);
                iv_wenzheng.setImageResource(R.drawable.dt_we_black);
                iv_bianmin.setImageResource(R.drawable.dt_min_black);
                iv_wenhua.setImageResource(R.drawable.dt_yun_black);
                iv_wode.setImageResource(R.drawable.dt_wo_blue);
                break;
            default:
                break;
        }
    }

    public String getNetType() {
        try {
            ConnectivityManager cm = (ConnectivityManager) this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return "1";
            }

            return "2";// 2,3,4G
        } catch (Exception e) {
            return "0";
        }
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentList.get(arg0);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
}
