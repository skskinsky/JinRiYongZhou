package com.dingtai.jinriyongzhou.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.view.MyListView;
import com.dingtai.dtlogin.activity.LoginActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.AdapterZhiboDetail;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.ModelZhiboDetail;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TuWenDetailActivity extends Activity {

    private MyListView mListview;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private List<ModelZhiboDetail> mTemListDate;
    private List<ModelZhiboDetail> mListDate;
    private boolean downup;
    private String state;
    public static AdapterZhiboDetail mAdapter;
    private String firstTime;//最新一条数据的时间
    RuntimeExceptionDao<ModelZhiboDetail, String> zhiboDetailList;//缓存数据
    private String ZhiboID;
    private Timer mTimer;//计时器
    private int time = 10;//刷新间隔
    private int mSecond;
    private boolean isTimeGo = true;//定时器的子线程开关

    private ImageView ivzhibotitle;

    private TextView zhibojian_comment;

    private TextView command_title, zhibojian_share, zhibojian_zan;


    private String UserGUID = "";


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downup = false;
            switch (msg.what) {
                case API.SUCCESS_2:
                    Toast.makeText(TuWenDetailActivity.this, (String) msg.obj,
                            Toast.LENGTH_SHORT).show();

                    break;

                case API.UNSUCCESS_1:
                    Toast.makeText(TuWenDetailActivity.this, (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case 10:
                    isTimeGo = true;
                    timeToGetDate();
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(TuWenDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 222:
                case 555:
                    isTimeGo = true;
                    timeToGetDate();
                    Toast.makeText(TuWenDetailActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 100:
                    isTimeGo = true;
                    timeToGetDate();
                    ArrayList<ModelZhiboDetail> temp_list = (ArrayList<ModelZhiboDetail>) msg.getData().getParcelableArrayList("list").get(0);
                    mTemListDate.addAll(temp_list);
                    if (mTemListDate.size() > 0) {
                        if (state.equals("up")) { // 上拉

                            mListDate.addAll(mTemListDate);
                            mAdapter.notifyDataSetChanged();
                        } else {


                            if (temp_list.get(0).getIsExsitPoint().equals("1")) {
                                zhibojian_zan.setTextColor(getResources().getColor(R.color.common_color));
                                Drawable dbZan1 = getResources().getDrawable(
                                        R.drawable.biaoliao_dianzan1);
                                dbZan1.setBounds(0, 0, dbZan1.getMinimumWidth(),
                                        dbZan1.getMinimumHeight());
                                zhibojian_zan.setCompoundDrawables(dbZan1, null, null, null);
                                zhibojian_zan.setText(temp_list.get(0).getGoodPoint());
                            } else {
                                zhibojian_zan.setTextColor(getResources().getColor(R.color.Text60GreyColor));
                                Drawable dbZan1 = getResources().getDrawable(
                                        R.drawable.baoliao_dianzan);
                                dbZan1.setBounds(0, 0, dbZan1.getMinimumWidth(),
                                        dbZan1.getMinimumHeight());
                                zhibojian_zan.setCompoundDrawables(dbZan1, null, null, null);
                                zhibojian_zan.setText(temp_list.get(0).getGoodPoint());
                            }


                            if (TextUtils.isEmpty(firstTime)) {
                                firstTime = mTemListDate.get(0).getCreateTime();
                                mListview.setVisibility(View.VISIBLE);
                                mListDate.clear();
                                mListDate.addAll(mTemListDate);
                                mAdapter.notifyDataSetChanged();
                            } else {
                                if (firstTime.equals(mTemListDate.get(0).getCreateTime())) {
                                    //								Toast.makeText(getActivity(), "已是最新数据", 0).show();
                                } else {
                                    mListview.setVisibility(View.VISIBLE);
                                    mListDate.clear();
                                    mListDate.addAll(mTemListDate);
                                    mAdapter.notifyDataSetChanged();
                                }
                            }

                        }
                    } else {

                        Toast.makeText(TuWenDetailActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();

                    }
                    mPullRefreshScrollView.onRefreshComplete();
            }
        }
    };
    private UserInfoModel user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_wen_detail);
        if (Assistant.getUserInfoByOrm(this) != null) {
            UserGUID = Assistant.getUserInfoByOrm(this).getUserGUID();
        }
        initView();


    }


    private void initView() {
        state = "";
        downup = false;

        ZhiboID = getIntent().getStringExtra("zhiboid");
        mListDate = new ArrayList<ModelZhiboDetail>();
        mTemListDate = new ArrayList<ModelZhiboDetail>();

        ivzhibotitle = (ImageView) findViewById(R.id.ivzhibotitle);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) ((DisplayMetricsTool.getWidth(this) * 4.5) / 16));
        ivzhibotitle.setLayoutParams(layoutParams);

        ImageLoader.getInstance().displayImage(getIntent().getStringExtra("imglogin"), ivzhibotitle);

        mListview = (MyListView) findViewById(R.id.listviewzhibo);
        this.findViewById(R.id.command_return).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        command_title = (TextView) findViewById(R.id.command_title);
        zhibojian_share = (TextView) findViewById(R.id.zhibojian_share);
        zhibojian_zan = (TextView) findViewById(R.id.zhibojian_zan);

        zhibojian_zan.setText(getIntent().getStringExtra("GoodPoint"));

        zhibojian_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                BaseShare bs = null;
                try {

                    bs = new BaseShare(TuWenDetailActivity.this,
                            getIntent().getStringExtra("title"), "今日永州，无论身在何处，同样感受精彩！", "http://gd.hh.hn.d5mt.com.cn/Share/LiveEvent.aspx?id=" + ZhiboID, "http://gd.hh.hn.d5mt.com.cn/Images/ic_launcher.png", "99", "");

                    bs.oneShare();
                    // // 长江新闻 转发
                    // CJstatistics(4, rid, rname, news_detail.getResourceGUID(),
                    // news_detail.getTitle(), news_detail.getChID(),
                    // news_detail.getChannelName());
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });

        zhibojian_zan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (mListDate.size() > 0) {
                    if (mListDate.get(0).getIsExsitPoint().equals("1")) {
                        if (removeGoodsPoint()) {
                            zhibojian_zan.setTextColor(getResources().getColor(R.color.Text60GreyColor));
                            Drawable dbZan = getResources().getDrawable(
                                    R.drawable.baoliao_dianzan);
                            dbZan.setBounds(0, 0, dbZan.getMinimumWidth(),
                                    dbZan.getMinimumHeight());
                            zhibojian_zan.setCompoundDrawables(dbZan, null, null, null);
                            zhibojian_zan.setText(String.valueOf(Integer.parseInt(mListDate.get(0).getGoodPoint()) - 1));
                        }
                    } else {
                        if (addGoodPoint()) {
                            zhibojian_zan.setTextColor(getResources().getColor(R.color.common_color));
                            Drawable dbZan = getResources().getDrawable(
                                    R.drawable.biaoliao_dianzan1);
                            dbZan.setBounds(0, 0, dbZan.getMinimumWidth(),
                                    dbZan.getMinimumHeight());
                            zhibojian_zan.setCompoundDrawables(dbZan, null, null, null);
                            zhibojian_zan.setText(String.valueOf(Integer.parseInt(mListDate.get(0).getGoodPoint()) + 1));
                        }
                    }
                }
            }
        });

        command_title.setText(getIntent().getStringExtra("title"));

        zhibojian_comment = (TextView) findViewById(R.id.zhibojian_comment);
        zhibojian_comment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent(TuWenDetailActivity.this, ZhiboJianPLActivity.class);
                i.putExtra("isEnd", getIntent().getStringExtra("isEnd"));
                i.putExtra("title", getIntent().getStringExtra("title"));
                i.putExtra("time", getIntent().getStringExtra("time"));
                i.putExtra("zhiboid", ZhiboID);

                startActivity(i);
            }
        });


        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）

        mPullRefreshScrollView.setHasPullUpFriction(false); // 设置没有上拉阻力
        //		if (!Assistant.IsContectInterNet2(getActivity())) {
        //获取缓存
        //			zhiboDetailList = getHelper().get_zhibo_detail_list();
        //			if (zhiboDetailList.isTableExists()) {
        //				mListDate = zhiboDetailList.queryForEq("NewsLiveEventID", ZhiboID);
        //			}
        //		} else {
        //			getDate();
        //		}

        mAdapter = new AdapterZhiboDetail(this, mListDate);
        mListview.setAdapter(mAdapter);
        mListview.setVisibility(View.INVISIBLE);
        getDate();
    }


    private boolean removeGoodsPoint() {
        user = Assistant.getUserInfoByOrm(this);
        if (user != null) {
            Intent intent = new Intent(this, IndexHttpService.class);
            intent.putExtra("api", IndexAPI.LIVEROOM_DEL_ZAN_API);
            intent.putExtra(IndexAPI.LIVEROOM_DEL_ZAN_MESSAGE,
                    new Messenger(handler));
            intent.putExtra("url", IndexAPI.API_LIVEROOM_DEL_ZAN_URL);
            intent.putExtra("ID", ZhiboID);
            intent.putExtra("UserGUID", user.getUserGUID());

            startService(intent);
            return true;
        } else {
            Toast.makeText(this, "请先登录！", Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            return false;
        }
    }


    private boolean addGoodPoint() {
        user = Assistant.getUserInfoByOrm(this);
        if (user != null) {
            Intent intent = new Intent(this, IndexHttpService.class);
            intent.putExtra("api", IndexAPI.LIVEROOM_ZAN_API);
            intent.putExtra(IndexAPI.LIVEROOM_ZAN_MESSAGE,
                    new Messenger(handler));
            intent.putExtra("url", IndexAPI.API_LIVEROOM_ZAN_URL);
            intent.putExtra("ID", ZhiboID);
            intent.putExtra("UserGUID", user.getUserGUID());

            startService(intent);
            return true;
        } else {
            Toast.makeText(this, "请先登录！",
                    Toast.LENGTH_SHORT).show();
            Intent i = new Intent();
            i.setClass(this, LoginActivity.class);
            startActivity(i);
            return false;
        }
    }


    String zhiboAPI = API.COMMON_URL + "Interface/NewsLiveAPI.ashx?action=";

    private void getDate() {
        mTemListDate.clear();
        String num = "10";
        String dtop = "";
        String url;
        if (state.equals("up")) {
            // 上拉
            url = zhiboAPI + "GetLiveContentUpList";

            dtop = "" + mListDate.size();
        } else {
            url = zhiboAPI + "GetLiveDownContentList";

        }

        get_ZhiboDetail_list(this, url, num, dtop, ZhiboID, UserGUID, new Messenger(this.handler));
    }

    public void get_ZhiboDetail_list(Context context, String url, String num, String dtop, String eid, String UserGUID, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", NewsAPI.ZHIBODETAIL_LIST_API);
        localIntent.putExtra(NewsAPI.ZHIBODETAIL_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("eid", eid);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("num", num);
        localIntent.putExtra("UserGUID", UserGUID);

        context.startService(localIntent);
    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {


            if (downup == false) {
                if (Assistant.IsContectInterNet(TuWenDetailActivity.this, false)) {
                    state = "down";
                    downup = true;
                    getDate();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (mListDate != null && mListDate.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {

            if (downup == false) {
                if (Assistant.IsContectInterNet(TuWenDetailActivity.this, false)) {
                    downup = true;
                    state = "up";
                    getDate();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (mListDate != null && mListDate.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }
        }
    };

    public void onResume() {
        super.onResume();
        timeToGetDate();
        if (Assistant.getUserInfoByOrm(this) != null) {
            UserGUID = Assistant.getUserInfoByOrm(this).getUserGUID();
        } else {
            UserGUID = "";
        }
    }

    ;

    //计时获取数据
    private void timeToGetDate() {
        if (mTimer == null) {
            mTimer = new Timer();
            mSecond = time;
            mTimer.schedule(new TimerTask() {
                public void run() {
                    mSecond--;
                    if (mSecond == 0) {
                        try {
                            mTimer.cancel();
                            mTimer = null;
                        } catch (Exception e) {
                        }
                        timeHandler.sendEmptyMessage(9001);
                    }
                }
                //实际刷新速度过快 调整时间间隔
                //			}, 0, 1000);
            }, 0, 2000);
        }

    }

    final Handler timeHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (isTimeGo && Assistant.IsContectInterNet(TuWenDetailActivity.this, false)) {
                isTimeGo = false;
                state = "";
                getDate();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer = null;
    }
}
