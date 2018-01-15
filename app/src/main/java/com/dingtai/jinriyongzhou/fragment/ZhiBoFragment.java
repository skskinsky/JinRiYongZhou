package com.dingtai.jinriyongzhou.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.VideoPlaySwitch;
import com.dingtai.base.view.LazyLoadFragment;
import com.dingtai.base.view.MyListView;
import com.dingtai.dtbaoliao.model.ModelZhiboList;
import com.dingtai.dtlogin.activity.LoginActivity;
import com.dingtai.dtsetting.activity.SettingActivityNew;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.TuWenDetailActivity;
import com.dingtai.jinriyongzhou.adapter.AdapterZhiboList;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.activity.CommonActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

public class ZhiBoFragment extends LazyLoadFragment implements DialogInterface.OnClickListener {

    private MyListView mListview;

    private ImageView title_bar_right_img;

    private AdapterZhiboList mAdapter;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private RuntimeExceptionDao<ModelZhiboList, String> zhibo_list_mode;// 直播缓存列表
    private List<ModelZhiboList> mListDate;// 列表数据
    private List<ModelZhiboList> mTemListDate;// 临时数据
    private boolean downup;
    private String state;
    public static String ZhiboID;
    private AlertDialog choose;    //对话框
    // 接收信息
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            downup = false;
            switch (msg.what) {
                case 10:
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(ZhiBoFragment.this.getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 222:
                case 555:
                    Toast.makeText(ZhiBoFragment.this.getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 100:
                    mPullRefreshScrollView.onRefreshComplete();
                    ArrayList<ModelZhiboList> temp_list = (ArrayList<ModelZhiboList>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTemListDate.addAll(temp_list);
                    if (state.equals("up")) { // 上拉
                        if (mTemListDate.size() > 0) {
                            mListDate.addAll(mTemListDate);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ZhiBoFragment.this.getActivity(), "暂无更多数据", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        if (mTemListDate.size() > 0) {
                            mListDate.clear();
                            mListDate.addAll(mTemListDate);
                            mAdapter.notifyDataSetChanged();
                        }

                    }
                    break;
            }
        }
    };


    private void initView() {
        title_bar_right_img = (ImageView) findViewById(R.id.title_bar_right_img);// 个人中心
        title_bar_right_img.setVisibility(View.GONE);
        title_bar_right_img.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", "我的");
                startIntent(ZhiBoFragment.this.getActivity(), intent);
            }
        });
        //mMainView.findViewById(R.id.zhiboincludetop).setVisibility(View.VISIBLE);
        // ((TextView) findViewById(R.id.command_title)).setText("长江直播");
        //		((TextView) findViewById(R.id.title_bar_center)).setText("直播");
        //		findViewById(R.id.command_return).setOnClickListener(
        //				new OnClickListener() {
        //
        //					@Override
        //					public void onClick(View arg0) {
        //						finish();
        //					}
        //				});
        state = "";
        downup = false;
        mListDate = new ArrayList<ModelZhiboList>();
        mTemListDate = new ArrayList<ModelZhiboList>();

        // if (!Assistant.IsContectInterNet2(this)) {
        // //获取缓存
        // zhibo_list_mode = getHelper().get_zhibo_list();
        // if (zhibo_list_mode.isTableExists()) {
        // mListDate = zhibo_list_mode.queryForAll();
        // }
        // } else {
        getDate();
        // }

        mListview = (MyListView) findViewById(R.id.listviewzhibo);
        // mListview = (PullToRefreshScrollView)
        // findViewById(R.id.pull_refresh_scrollview);
        // mListview.setMode(Mode.BOTH);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.zhibo_pulllistview);
        mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）

        mPullRefreshScrollView.setHasPullUpFriction(false); // 设置没有上拉阻力

        mAdapter = new AdapterZhiboList(this.getActivity(), mListDate);
        mListview.setAdapter(mAdapter);
        downup = false;
        state = "";
        mListview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                /**
                 * 判断是否开启3G/4G下观看视频
                 */
                if (!Assistant.isWifi(getActivity())) {
                    VideoPlaySwitch.swich(getActivity(), ZhiBoFragment.this);
                } else {
                    Intent intent = new Intent(ZhiBoFragment.this.getActivity(),
                            TuWenDetailActivity.class);
                    ZhiboID = mListDate.get((arg2)).getID();
                    intent.putExtra("zhiboid", mListDate.get((arg2)).getID());
                    intent.putExtra("isEnd", mListDate.get((arg2)).getEventStatus());
                    intent.putExtra("imglogin", mListDate.get((arg2)).getContentLogo());
                    intent.putExtra("title", mListDate.get((arg2)).getEventName());
                    intent.putExtra("time", mListDate.get((arg2)).getCreateTime());

                    startActivity(intent);
                }
            }
        });
    }

    String mUrl = API.COMMON_URL + "Interface/NewsLiveEventsAPI.ashx?action=";

    private void getDate() {
        mTemListDate.clear();
        String num = "10";
        String dtop = "";
        String url;
        if (state.equals("up")) {
            // up
            url = mUrl + "GetLiveEventUpList";

            dtop = "" + mListDate.size();
        } else {
            url = mUrl + "GetLiveDownEventList";

        }
        get_Zhibo_list(this.getActivity(), url, num, dtop, new Messenger(handler));
    }

    public void get_Zhibo_list(Context context, String url, String num,
                               String dtop, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", NewsAPI.ZHIBO_LIST_API);
        localIntent.putExtra(NewsAPI.ZHIBO_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("num", num);

        context.startService(localIntent);
    }

    /**
     * 跳转到个人中心
     *
     * @param context
     * @param intent
     */
    public void startIntent(Context context, Intent intent) {

        if (Assistant.getUserInfoByOrm(context) != null) {
            intent.setClass(context, CommonActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(context, "请先登录！", Toast.LENGTH_SHORT).show();
            intent.setClass(context, LoginActivity.class);
            startActivity(intent);
        }

    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (downup == false) {
                        if (Assistant.IsContectInterNet(ZhiBoFragment.this.getActivity(), false)) {
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
            }, 1500);


        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if (downup == false) {
                        if (Assistant.IsContectInterNet(ZhiBoFragment.this.getActivity(), false)) {
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
            }, 1500);


        }
    };

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                //打开
                SettingActivityNew.IS_NET_TYPE = true;
                SharedPreferences mSp = this.getActivity().getSharedPreferences("SETTING", Context.MODE_PRIVATE);
                mSp.edit().putBoolean("IS_NET_TYPE", true).commit();
                Toast.makeText(ZhiBoFragment.this.getActivity(), "成功开启!", Toast.LENGTH_SHORT).show();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //取消
                choose.dismiss();
                break;
        }
    }


    @Override
    protected int setContentView() {
        // TODO Auto-generated method stub
        return R.layout.fragment_zhibolist;
    }


    @Override
    protected void initFragmentView() {
        // TODO Auto-generated method stub
        initView();
    }


    @Override
    protected void loadData() {
        // TODO Auto-generated method stub

    }


    @Override
    protected void loadCache() {
        // TODO Auto-generated method stub

    }


    @Override
    protected void stopLoad() {
        // TODO Auto-generated method stub

    }
}
