package com.dingtai.jinriyongzhou.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RadioGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.livelib.R;
import com.dingtai.base.livelib.activtity.LiveAudioActivity;
import com.dingtai.base.livelib.activtity.LiveMainActivity;
import com.dingtai.base.livelib.adapter.PingDaoZhiBoAdapter;
import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.livelib.service.LivesService;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.view.MyListView;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 谢慧强 广电视频直播列表
 */
public class LiveVideoListActivity extends BaseFragment implements
        OnClickListener, DialogInterface.OnClickListener {
    private PullToRefreshScrollView mPull;
    // private ListView mList;
    private MyListView listView;
    private View mMainView;
    private List<LiveChannelModel> list = new ArrayList<LiveChannelModel>();
    private List<LiveChannelModel> tempList = new ArrayList<LiveChannelModel>();
    private ArrayList<LiveChannelModel> temp_list;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private LiveChannelModel liveChanne;
    private RuntimeExceptionDao<LiveChannelModel, String> liveChannels;
    private PingDaoZhiBoAdapter mAdapter;
    private SharedPreferences mSp;
    private String type;

    public static LiveVideoListActivity getInstance(String top) {
        Bundle bundle = new Bundle();
        bundle.putString("type", top);
        LiveVideoListActivity activity = new LiveVideoListActivity();
        activity.setArguments(bundle);
        return activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString("type", "0");
    }

    private int arg2 = 0;
    private Handler handler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1000:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        if (list != null)
                            list.clear();
                        if (temp_list != null)
                            temp_list.clear();
                        if (tempList != null)
                            tempList.clear();
                        temp_list = (ArrayList<LiveChannelModel>) msg.getData()
                                .getParcelableArrayList("list").get(0);
                        rela_anren.setVisibility(RelativeLayout.GONE);
                        tempList.addAll(temp_list);
                        if (tempList.size() > 0) {
                            list.addAll(tempList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mPullRefreshScrollView.onRefreshComplete();
                            Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                            rela_anren.setVisibility(RelativeLayout.GONE);
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }

                    break;
                case 0:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        rela_anren.setVisibility(RelativeLayout.GONE);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    break;
                case 505:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        Toast.makeText(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                        rela_anren.setVisibility(RelativeLayout.GONE);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    break;
                case 404:
                    try {
                        mPullRefreshScrollView.onRefreshComplete();
                        Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_SHORT).show();
                        rela_anren.setVisibility(RelativeLayout.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }

        ;
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.activity_live_video_list,
                container, false);
        intiView();

        return mMainView;
    }

    private View item;
    private ImageView reload;
    private AnimationDrawable animationDrawable;
    private ViewGroup rela_anren;
    private AlertDialog choose;

    private void intiView() {
        rela_anren = (RelativeLayout) mMainView.findViewById(R.id.rela_anren);
        // 初始化
        listView = (MyListView) mMainView.findViewById(R.id.mlv_live_list);
        mPullRefreshScrollView = (PullToRefreshScrollView) mMainView
                .findViewById(R.id.comment_scrollview);
        this.mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        this.mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this.getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
        this.mPullRefreshScrollView.setHasPullUpFriction(false);
        this.mPullRefreshScrollView
                .setOnRefreshListener(this.mOnRefreshListener);
        mAdapter = new PingDaoZhiBoAdapter(getActivity(), list);
        listView.setAdapter(mAdapter);
        listView.setVerticalScrollBarEnabled(false);
        if (!Assistant.IsContectInterNet2(getActivity())) {
            if (tempList.size() == 0) {
                startLoading();
            } else {
                rela_anren.setVisibility(RelativeLayout.GONE);
            }

            // 获取缓存
            liveChannels = getHelper().getMode(LiveChannelModel.class);
            if (liveChannels.isTableExists()) {
                List<LiveChannelModel> temp_list = liveChannels.queryForAll();
                list.addAll(temp_list);
                mAdapter.notifyDataSetChanged();
            }
            if (tempList.size() == 0) {
                handler.sendEmptyMessage(404);
            }
        } else {
            startLoading();
            getData();
        }

        // 列表点击时间
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                String url = API.COMMON_URL
                        + "interface/LiveAPI.ashx?action=AddReadNum&ID="
                        + list.get(arg2).getID();

                requestData(getActivity(), url, new Messenger(handler),
                        API.GET_REDNUM, API.GET_REDNUM_MESSENGER,
                        LivesService.class);
                LiveVideoListActivity.this.arg2 = arg2;
//                if (Assistant.isWifi(getActivity())) {
                openActivity();
//                } else {
//                    VideoPlaySwitch.swich(getActivity(), LiveVideoListActivity.this);
//                }
            }
        });
    }

    private void openActivity() {
        Intent intent = null;
        if ("1".equals(list.get(arg2).getLiveType())) {
            intent = new Intent(getActivity(),
                    LiveMainActivity.class);
            intent.putExtra("VideoUrl", list.get(arg2)
                    .getVideoUrl());
            intent.putExtra("RTMPUrl", list.get(arg2)
                    .getLiveRTMPUrl());
            intent.putExtra("PicPath", list.get(arg2)
                    .getLiveImageUrl());
            intent.putExtra("ID", list.get(arg2).getID());
            intent.putExtra("Week", list.get(arg2).getWeek());
            intent.putExtra("CommentsNum", list.get(arg2)
                    .getCommentsNum());
            intent.putExtra("name", list.get(arg2)
                    .getLiveProgramName());
            intent.putExtra("livename", list.get(arg2)
                    .getLiveChannelName());
            intent.putExtra("nowtime", list.get(arg2)
                    .getLiveProgramDate());
            intent.putExtra("isLive", list.get(arg2).getIsLive());
            intent.putExtra("picUrl", list.get(arg2).getPicPath());
            intent.putExtra("startTime", list.get(arg2)
                    .getLiveBeginDate());
            intent.putExtra("logo", list.get(arg2)
                    .getLiveBeginLogo());
            intent.putExtra("newsID", list.get(arg2)
                    .getLiveNewChID());
            intent.putExtra("status", list.get(arg2)
                    .getLiveBeginStatus());
            intent.putExtra("liveType", 1);
        } else if ("3".equals(list.get(arg2).getLiveType())) {

            intent = new Intent(getActivity(), LiveMainActivity.class);
            switch (getStatus(list.get(arg2))) {
                case 0:
                    intent.putExtra("RTMPUrl", list.get(arg2).getLiveRTMPUrl());
                    break;
                case 1:
                    intent.putExtra("RTMPUrl", list.get(arg2).getLiveRTMPUrl());
                    break;
                case 2:
                    intent.putExtra("RTMPUrl", list.get(arg2).getLiveLink());
            }

            Bundle bundle = new Bundle();
            bundle.putSerializable("tabList", (Serializable) list.get(arg2).getTabList());
            intent.putExtras(bundle);
            intent.putExtra("VideoUrl", list.get(arg2).getVideoUrl());
            intent.putExtra("PicPath", list.get(arg2).getLiveImageUrl());
            intent.putExtra("ID", list.get(arg2).getID());
            intent.putExtra("Week", list.get(arg2).getWeek());
            intent.putExtra("CommentsNum", list.get(arg2).getCommentsNum());
            intent.putExtra("name", list.get(arg2).getLiveProgramName());
            intent.putExtra("livename", list.get(arg2).getLiveChannelName());
            intent.putExtra("nowtime", list.get(arg2).getLiveProgramDate());
            intent.putExtra("isLive", list.get(arg2).getIsLive());
            intent.putExtra("livelink", list.get(arg2).getLiveLink());
            intent.putExtra("picUrl", list.get(arg2).getPicPath());
            intent.putExtra("startTime", list.get(arg2).getLiveBeginDate());
            intent.putExtra("logo", list.get(arg2).getLiveBeginLogo());
            intent.putExtra("liveEvent", list.get(arg2).getLiveEventID());
            intent.putExtra("newsID", list.get(arg2).getLiveNewChID());
            intent.putExtra("status", list.get(arg2).getLiveBeginStatus());
            intent.putExtra("liveType", 2);
            intent.putExtra("LiveLink", list.get(arg2).getLiveLink());
            intent.putExtra("LiveBeginType", list.get(arg2).getLiveBeginType());
            intent.putExtra("LiveBeginMedia", list.get(arg2).getLiveBeginMedia());
            intent.putExtra("readNum", list.get(arg2).getReadNo());
            intent.putExtra("jianjie", list.get(arg2).getLiveIntroduce());
            intent.putExtra("liveVideoLogo", list.get(arg2).getLiveVideoLogo());
            intent.putExtra("position", list.get(arg2).getLiveVideoLogoPosition());
            intent.putExtra("IsIntroduce", list.get(arg2).getIsIntroduce());
            String liveEndType = list.get(arg2).getLiveEndType();
            intent.putExtra("LiveEndType", liveEndType);
            String liveEndMedia = list.get(arg2).getLiveEndMedia();
            intent.putExtra("LiveEndMedia", liveEndMedia);
            String liveEndLogo = list.get(arg2).getLiveEndLogo();
            intent.putExtra("LiveEndLogo", liveEndLogo);
            intent.putExtra("IsTopAD", list.get(arg2).getIsTopAD());
      


        } else {
            intent = new Intent(getActivity(),
                    LiveAudioActivity.class);
            intent.putExtra("VideoUrl", list.get(arg2)
                    .getVideoUrl());
            intent.putExtra("RTMPUrl", list.get(arg2)
                    .getLiveRTMPUrl());
            intent.putExtra("ID", list.get(arg2).getID());
            intent.putExtra("Week", list.get(arg2).getWeek());
            intent.putExtra("CommentsNum", list.get(arg2)
                    .getCommentsNum());
            intent.putExtra("name", list.get(arg2)
                    .getLiveProgramName());
            intent.putExtra("channelName", list.get(arg2)
                    .getLiveChannelName());
            intent.putExtra("nowtime", list.get(arg2)
                    .getLiveProgramDate());
            intent.putExtra("startTime", list.get(arg2)
                    .getLiveBeginDate());
            intent.putExtra("logo", list.get(arg2)
                    .getLiveBeginLogo());
            intent.putExtra("newsID", list.get(arg2)
                    .getLiveNewChID());
        }
        startActivity(intent);
    }

    private int getStatus(LiveChannelModel model) {
        int strState = -1;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());

        try {
            Date strBegin = formatter.parse(model.getLiveBeginDate());
            Date strEnd = formatter.parse(model.getLiveEndDate());
            Date strNow = DateTool.convertStrToDate2(formatter.format(curDate));
            int Begin = strNow.compareTo(strBegin);
            int End = strNow.compareTo(strEnd);
            if (End > 0) {
                strState = 2;
            } else if (Begin >= 0 && End <= 0) {
                strState = 1;
            } else {
                strState = 0;
            }

            return strState;
        } catch (ParseException var11) {
            return strState;
        } catch (Exception var12) {
            return strState;
        }
    }


    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(getActivity(),
            // System.currentTimeMillis(), 10000);
            String str = DateTool.getCurrentTime();
            paramPullToRefreshBase.getLoadingLayoutProxy().setLastUpdatedLabel(
                    str);
            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始刷新");

            if (Assistant.IsContectInterNet(getActivity(), false)) {
                getData();
            } else {
                handler.sendEmptyMessage(404);
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(getActivity(),
            // System.currentTimeMillis(), 10000);
            String str = DateTool.getCurrentTime();
            paramPullToRefreshBase.getLoadingLayoutProxy().setLastUpdatedLabel(
                    str);
            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在加载");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel(
                    "上拉加载更多");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始加载");
            if (Assistant.IsContectInterNet(getActivity(), false)) {
                getData();
            } else {
                handler.sendEmptyMessage(404);
            }
        }
    };

    private void startLoading() {
        item = getActivity().getLayoutInflater().inflate(
                R.layout.onclick_reload, null);
        reload = (ImageView) item.findViewById(R.id.reload_btn);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, Gravity.CENTER);
        item.setLayoutParams(params);
        reload.setImageResource(R.drawable.progress_round);
        animationDrawable = (AnimationDrawable) reload.getDrawable();
        animationDrawable.start();
        rela_anren.addView(item);
    }

    private void getData() {
        // http://192.168.1.12:8091/Interface/LiveAPI.ashx?action=GetDownLiveChannels&StID=2

        String url = API.COMMON_URL + "Interface/LiveAPI.ashx?action=GetLiveByType&type=" + type + "&top=10&dtop=0";

        get_live_channels(getActivity(), url, API.STID, new Messenger(handler));

    }

    /**
     * 获取广电直播频道列表
     *
     * @param paramContext
     * @param url
     * @param
     * @param paramMessenger
     */
    public void get_live_channels(Context paramContext, String url,
                                  String sign, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, LivesService.class);
        intent.putExtra("api", NewsAPI.LIVE_VIDEO_LIST_API);
        intent.putExtra(NewsAPI.LIVE_VIDEO_LIST_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("sign", sign);
        paramContext.startService(intent);
    }


    @Override
    public void onClick(View arg0) {
        // TODO 自动生成的方法存根
        switch (arg0.getId()) {

            default:
                break;
        }
    }

    /**
     * 对话框监听
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // 打开
                mSp = getActivity().getSharedPreferences("SETTING",
                        Context.MODE_PRIVATE);
                mSp.edit().putBoolean("IS_NET_TYPE", true).commit();
                Toast.makeText(getActivity(), "成功开启!", Toast.LENGTH_SHORT).show();
                openActivity();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // 取消
                choose.dismiss();
                break;
        }

    }
}
