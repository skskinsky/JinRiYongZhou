package com.dingtai.jinriyongzhou.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.livelib.service.LivesAPI;
import com.dingtai.base.livelib.service.LivesService;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.pullrefresh.IScrollTopCallBack;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.extras.recyclerview.PullToRefreshRecyclerView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.userscore.ShowJiFen;
import com.dingtai.base.userscore.UserScoreConstant;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.view.IjkVideoView;
import com.dingtai.base.view.VideoPlayView;
import com.dingtai.dtlogin.activity.LoginActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.VideoMainActivity;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.MediaList;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Author: 李建 Version V1.0 Date: 2016/10/8 14:15. Description: 视频播放列表(上拉小窗口播放)
 * Modification History: Date Author Version Description
 * ------------------------
 * ----------------------------------------------------------- 2016/10/8 ALee
 * 1.0 1.0 Why & What is modified:
 */

public class VideoFragment_V2 extends BaseFragment {

    protected static final int UI_PLAYER = 101;
    private int lastPostion = -1;
    private int postion = -1;

    private long zanTime = 0;
    private RecyclerViewAdapter myAdapter;
    private PullToRefreshRecyclerView mPullRefreshRecyclerView;
    private RecyclerView recyclerview;
    private List<MediaList> list = new ArrayList<MediaList>();

    private RelativeLayout small_layout; // 视频小窗口
    private ImageView close;
    private FrameLayout layout_video; // 用于存放视频播放器

    private VideoPlayView videoPlayView; // 视频播放界面


    private View mMainView = null;
    private String lanmuID; // 栏目ID

    private boolean isUp = false; // 是否上拉

    RuntimeExceptionDao<MediaList, String> video_list;// 视频列表
    private int tempPostion = 0;
    private String UserGUID = "";
    private MyViewHolder holder = null;
    private Handler handler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case API.HANDLER_GETDATA_SUCCESS:
                    ArrayList<MediaList> temp_list = (ArrayList<MediaList>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    if (!isUp) {
                        list.clear();
                    }
                    list.addAll(temp_list);
                    myAdapter.notifyDataSetChanged();
                    mPullRefreshRecyclerView.onRefreshComplete();
                    break;
                case 333:
                    Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    mPullRefreshRecyclerView.onRefreshComplete();
                    break;
                case 1200:
                case 222:
                    Toast.makeText(getActivity(), "获取视频列表失败", Toast.LENGTH_SHORT).show();
                    mPullRefreshRecyclerView.onRefreshComplete();
                    break;
                case UI_PLAYER:

                    if (small_layout.getVisibility() == View.VISIBLE) {

                        layout_video.removeAllViews();
                        layout_video.addView(videoPlayView);

                    } else {
                        View view = recyclerview.findViewHolderForAdapterPosition(lastPostion).itemView;
                        FrameLayout frameLayout = (FrameLayout) view
                                .findViewById(R.id.item_layout_video);
                        frameLayout.removeAllViews();
                        frameLayout.addView(videoPlayView);

                    }
                    break;
                case API.SUCCESS_2:
                    if (list.size() > tempPostion && list.get(tempPostion).getIsExsitPoint().equals("1")) {
                        holder.video_zan.setTextColor(getResources().getColor(R.color.Text60GreyColor));
                        Drawable dbZan = getResources().getDrawable(
                                R.drawable.baoliao_dianzan);
                        dbZan.setBounds(0, 0, dbZan.getMinimumWidth(),
                                dbZan.getMinimumHeight());
                        holder.video_zan.setCompoundDrawables(dbZan, null, null, null);
                        int num = 0;
                        if (holder.video_zan.getText().toString().equals("赞")) {
                            num = 0;
                        } else {
                            num = Integer.parseInt(holder.video_zan.getText().toString()) - 1;
                        }
                        if (num != 0)
                            holder.video_zan.setText(String.valueOf(Integer.parseInt(holder.video_zan.getText().toString()) - 1));
                        else
                            holder.video_zan.setText("赞");
                        list.get(tempPostion).setIsExsitPoint("0");
                    }
                    break;
                case 112:
                    if (list.size() > tempPostion && !list.get(tempPostion).getIsExsitPoint().equals("1")) {
                        holder.video_zan.setTextColor(getResources().getColor(R.color.common_color));
                        Drawable zan = getResources().getDrawable(
                                R.drawable.biaoliao_dianzan1);
                        zan.setBounds(0, 0, zan.getMinimumWidth(),
                                zan.getMinimumHeight());
                        holder.video_zan.setCompoundDrawables(zan, null, null, null);
                        if (!holder.video_zan.getText().toString().equals("赞"))
                            holder.video_zan.setText(String.valueOf(Integer.parseInt(holder.video_zan.getText().toString()) + 1));
                        else holder.video_zan.setText(String.valueOf(1));
                        list.get(tempPostion).setIsExsitPoint("1");
                    }
                    break;
                case API.SUCCESS_1:
                    Toast.makeText(getActivity(), (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                case API.UNSUCCESS_1:
                    Toast.makeText(getActivity(), (String) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    mPullRefreshRecyclerView.onRefreshComplete();
                    break;
            }
        }

    };


    public void moveTop() {
        recyclerview.scrollToPosition(0);
        mPullRefreshRecyclerView.setRefreshing();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (mMainView == null) {
            mMainView = inflater.inflate(R.layout.video_activity_2, null);
            video_list = getHelper().getMode(MediaList.class);
            initView();
            initActions();
            getCacheData();
            if (Assistant.getUserInfoByOrm(getActivity()) != null) {
                UserGUID = Assistant.getUserInfoByOrm(getActivity()).getUserGUID();
            }
            getDate();
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) mMainView.getParent();
        if (parent != null) {
            parent.removeView(mMainView);
        }


        return mMainView;


    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (Assistant.getUserInfoByOrm(getActivity()) != null) {
            UserGUID = Assistant.getUserInfoByOrm(getActivity()).getUserGUID();
        } else {
            UserGUID = "";
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    // 设置栏目
    public void setLanmuID(String ID) {
        this.lanmuID = ID;
    }

    // 用于全屏是得到播放器View
    public VideoPlayView getVideoPlayView() {
        return videoPlayView;
    }


    /**
     * 获取列表的数据
     */
    public void getDate() {

        if (isUp) {
            String url = API.VIDEO_LIST_SHANGLA_URL;
            String StID = API.STID;
            String top = "10";
            String dtop = list.size() + "";
            String ChannelID = lanmuID;
            get_video_list(getActivity(), url, StID, top, dtop, ChannelID,
                    "up", UserGUID, new Messenger(handler));
        } else {

            String url = API.VIDEO_LIST_XIALA_URL;
            String StID = API.STID;
            String top = "10";
            String ChannelID = lanmuID;
            get_video_list(getActivity(), url, StID, top, "", ChannelID,
                    "down", UserGUID, new Messenger(handler));

        }

    }

    public void get_video_list(Context context, String url, String StID,
                               String top, String dtop, String ChannelID, String type, String UserGUID,
                               Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", API.VIDEO_LIST_API);
        localIntent.putExtra(API.VIDEO_LIST_MESSAGE, paramMessenger);
        localIntent.putExtra("StID", StID);
        localIntent.putExtra("top", top);
        localIntent.putExtra("url", url);
        localIntent.putExtra("type", type);
        localIntent.putExtra("UserGUID", UserGUID);
        localIntent.putExtra("ChannelID", ChannelID);
        if ("up".equals(type)) {
            localIntent.putExtra("dtop", dtop);
        }
        context.startService(localIntent);
    }

    /**
     * TODO 通过数据库得到数据 缓存
     */
    public void getCacheData() {
        try {

            video_list = getHelper().getMode(MediaList.class);
            if (video_list.isTableExists()) {
                List<MediaList> acCacheData = (List<MediaList>) video_list
                        .queryForAll();
                list.clear();
                if (acCacheData != null && acCacheData.size() > 0) {
                    list.addAll(acCacheData);
                    myAdapter.notifyDataSetChanged();
                }

            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "暂无更多数据，等会再试试。", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void addReadNo(String ID) {
        requestData(getActivity(), API.COMMON_URL + "Interface/MediaAPI.ashx?action=AddClickNum&ID=" + ID, new Messenger(handler), LivesAPI.ADD_LIVE_READ_NO, LivesAPI.ADD_LIVE_READ_NO_MESSENGER, LivesService.class);
    }

    private void initView() {
        // TODO Auto-generated method stub
        Bundle mBundle = getArguments();
        if (mBundle != null && mBundle.getString("lanmuid") != null) {
            lanmuID = mBundle.getString("lanmuid");

        }

        videoPlayView = new VideoPlayView(this.getActivity());

        mPullRefreshRecyclerView = (PullToRefreshRecyclerView) mMainView.findViewById(R.id.video_recyclerview);
        mPullRefreshRecyclerView.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        mPullRefreshRecyclerView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）


        mPullRefreshRecyclerView.setHasPullUpFriction(true); // 设置没有上拉阻力
        recyclerview = mPullRefreshRecyclerView.getRefreshableView();
        //设置LayoutManager

        recyclerview.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        myAdapter = new RecyclerViewAdapter(getActivity(), list);
        recyclerview.setAdapter(myAdapter);
        recyclerview.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy == 0) {
                    IScrollTopCallBack.getInstance().publish();

                } else {
                    IScrollTopCallBack.getInstance().move();

                }
            }
        });
        recyclerview.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewDetachedFromWindow(View v) {
                int index = recyclerview.getChildAdapterPosition(v);
                if (index == postion && postion != -1) {
                    FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.item_layout_video);
                    frameLayout.removeAllViews();
                    if (small_layout.getVisibility() == View.GONE && videoPlayView != null
                            && videoPlayView.isPlay()) {
                        videoPlayView.setShowContoller(true);
                        layout_video.removeAllViews();
                        layout_video.addView(videoPlayView);
                        small_layout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onChildViewAttachedToWindow(View v) {
                // TODO Auto-generated method stub

                int index = recyclerview.getChildAdapterPosition(v);
                if (index == postion && postion != -1) {
                    FrameLayout frameLayout = (FrameLayout) v.findViewById(R.id.item_layout_video);
                    frameLayout.removeAllViews();


                    //					if (videoPlayView.VideoStatus() == IjkVideoView.STATE_PAUSED) {
                    //						if (videoPlayView.getParent() != null)
                    //							((ViewGroup) videoPlayView.getParent()).removeAllViews();
                    //						frameLayout.addView(videoPlayView);
                    //						return;
                    //					}

                    if (small_layout.getVisibility() == View.VISIBLE && videoPlayView != null && videoPlayView.isPlay()) {
                        small_layout.setVisibility(View.GONE);
                        layout_video.removeAllViews();
                        videoPlayView.setShowContoller(true);
                        frameLayout.addView(videoPlayView);
                    }
                }

            }
        });


        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                isUp = false;
                if (videoPlayView.isPlay()) {
                    videoPlayView.stop();
                    postion = -1;
                    lastPostion = -1;
                    layout_video.removeAllViews();
                    small_layout.setVisibility(View.GONE);
                }

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getDate();
                    }

                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {


                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isUp = true;
                        getDate();
                    }
                }, 1000);

            }
        });


        small_layout = (RelativeLayout) mMainView
                .findViewById(R.id.small_window_layout);

        layout_video = (FrameLayout) mMainView.findViewById(R.id.layout_video);
        close = (ImageView) mMainView.findViewById(R.id.video_v2_close);

        small_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

            }
        });


        //点击事件
        myAdapter.setClick(new onClick() {
            @Override
            public void onclick(int position) {


                if (Assistant.getUserInfoByOrm(VideoFragment_V2.this.getActivity()) != null
                        && UserScoreConstant.ScoreMap
                        .containsKey(UserScoreConstant.SCORE_LOOK_VIDEO)) {
                    new ShowJiFen(VideoFragment_V2.this.getActivity(),
                            UserScoreConstant.SCORE_LOOK_VIDEO,
                            UserScoreConstant.SCORE_TYPE_ADD,
                            UserScoreConstant.ExchangeID,
                            Assistant.getUserInfoByOrm(VideoFragment_V2.this.getActivity()),
                            "");
                }

                postion = position;

                if (videoPlayView.VideoStatus() == IjkVideoView.STATE_PAUSED || videoPlayView.VideoStatus() == IjkVideoView.STATE_PLAYING) {
                    if (position != lastPostion) {

                        videoPlayView.stop();
                        videoPlayView.release();
                    } else {

                        return;
                    }
                }
                if (small_layout.getVisibility() == View.VISIBLE)

                {
                    small_layout.setVisibility(View.GONE);
                    layout_video.removeAllViews();
                    videoPlayView.setShowContoller(true);
                }

                if (lastPostion != -1)

                {
                    ViewGroup last = (ViewGroup) videoPlayView.getParent();
                    if (last != null) {
                        last.removeAllViews();
                        View itemView = (View) last.getParent();
                        if (itemView != null) {
                            itemView.findViewById(R.id.item_layout_video).setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (videoPlayView.getParent() != null) {
                    ((ViewGroup) videoPlayView.getParent()).removeAllViews();
                }

                View view = recyclerview.findViewHolderForAdapterPosition(postion).itemView;
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.item_layout_video);
                frameLayout.removeAllViews();
                frameLayout.addView(videoPlayView);
                videoPlayView.start(list.get(position).getMediaUrl());
                addReadNo(list.get(position).getID2());
                lastPostion = position;
            }
        });


    }


    public void refresh() {
        mPullRefreshRecyclerView.setRefreshing();
    }

    public void player() {
        if (videoPlayView != null && videoPlayView.isPlay()) {
            //handler.sendEmptyMessageDelayed(UI_PLAYER, 500);
            handler.sendEmptyMessage(UI_PLAYER);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) { // fragment是否显示给用户看到
        // TODO Auto-generated method stub
        super.setUserVisibleHint(isVisibleToUser);
        if (videoPlayView != null && !isVisibleToUser) {// fragment切换停止播放,关闭小窗口
            videoPlayView.stop();
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (videoPlayView != null && videoPlayView.isPlay()) { // 暫停
            //videoPlayView.pause();
            videoPlayView.stop();
            ViewGroup last = (ViewGroup) videoPlayView.getParent();
            if (last != null) {
                last.removeAllViews();

            }
        }

    }

    // 初始化事件
    private void initActions() {
        // TODO Auto-generated method stub

        close.setOnClickListener(new OnClickListener() { // 关闭小窗口视频
            @Override
            public void onClick(View v) {
                if (videoPlayView.isPlay()) {
                    videoPlayView.stop();

                    layout_video.removeAllViews();
                    small_layout.setVisibility(View.GONE);

                    postion = -1;
                    lastPostion = -1;

                }
            }
        });


        // 视频播放器播放完毕事件
        videoPlayView
                .setCompletionListener(new VideoPlayView.CompletionListener() {
                    @Override
                    public void completion(IMediaPlayer mp) {

                        if (small_layout.getVisibility() == View.VISIBLE) {
                            layout_video.removeAllViews();
                            small_layout.setVisibility(View.GONE);
                            videoPlayView.setShowContoller(true);
                        }

                        FrameLayout frameLayout = (FrameLayout) videoPlayView
                                .getParent();
                        videoPlayView.release();
                        if (frameLayout != null
                                && frameLayout.getChildCount() > 0) {
                            frameLayout.removeAllViews();
                        }

                        lastPostion = -1;
                    }
                });

    }


    class RecyclerViewAdapter extends Adapter<ViewHolder> {

        private Context context;
        private List<MediaList> list;
        private UserInfoModel user;


        public RecyclerViewAdapter(Context context, List<MediaList> list) {
            this.context = context;
            this.list = list;

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video_2, parent,
                    false));
            return holder;
        }


        private boolean removeGoodsPoint(String ID) {

            user = Assistant.getUserInfoByOrm(context);
            if (user != null) {
                Intent intent = new Intent(context, IndexHttpService.class);
                intent.putExtra("api", IndexAPI.VIDEO_LIST_REMOVEZAN_API);
                intent.putExtra(IndexAPI.VIDEO_LIST_REMOVEZAN_MESSENGER,
                        new Messenger(handler));
                intent.putExtra("url", IndexAPI.VIDEO_LIST_REMOVEZAN_URL);
                intent.putExtra("ID", ID);
                intent.putExtra("UserGUID", user.getUserGUID());

                context.startService(intent);

                return true;

            } else {
                Toast.makeText(context, "请先登录！",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(context, LoginActivity.class);
                startActivity(i);
                return false;
            }

        }


        private boolean addGoodPoint(String ID) {
            user = Assistant.getUserInfoByOrm(context);
            if (user != null) {
                Intent intent = new Intent(context, IndexHttpService.class);
                intent.putExtra("api", IndexAPI.VIDEO_LIST_ZAN_API);
                intent.putExtra(IndexAPI.VIDEO_LIST_ZAN_MESSENGER,
                        new Messenger(handler));
                intent.putExtra("url", IndexAPI.VIDEO_LIST_ZAN_URL);
                intent.putExtra("ID", ID);
                intent.putExtra("UserGUID", user.getUserGUID());

                context.startService(intent);

                return true;

            } else {
                Toast.makeText(context, "请先登录！",
                        Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(context, LoginActivity.class);
                startActivity(i);
                return false;
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            ((MyViewHolder) holder).tv_comment_num.setText(list.get(position).getCommentNum());
            if (list.get(position).getIsExsitPoint().equals("1")) {
                ((MyViewHolder) holder).video_zan.setTextColor(getResources().getColor(R.color.common_color));
                Drawable dbZan = getResources().getDrawable(
                        R.drawable.biaoliao_dianzan1);
                dbZan.setBounds(0, 0, dbZan.getMinimumWidth(),
                        dbZan.getMinimumHeight());
                ((MyViewHolder) holder).video_zan.setCompoundDrawables(dbZan, null, null, null);
                if (list.get(position).getGoodPoint().equals("0"))
                    ((MyViewHolder) holder).video_zan.setText("赞");
                else
                    ((MyViewHolder) holder).video_zan.setText(list.get(position).getGoodPoint());
            } else {
                ((MyViewHolder) holder).video_zan.setTextColor(getResources().getColor(R.color.Text60GreyColor));
                Drawable dbZan = getResources().getDrawable(
                        R.drawable.baoliao_dianzan);
                dbZan.setBounds(0, 0, dbZan.getMinimumWidth(),
                        dbZan.getMinimumHeight());
                ((MyViewHolder) holder).video_zan.setCompoundDrawables(dbZan, null, null, null);
                if (list.get(position).getGoodPoint().equals("0"))
                    ((MyViewHolder) holder).video_zan.setText("赞");
                else
                    ((MyViewHolder) holder).video_zan.setText(list.get(position).getGoodPoint());

            }


            ((MyViewHolder) holder).video_zan.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    VideoFragment_V2.this.holder = (MyViewHolder) holder;
                    tempPostion = position;
                    // TODO Auto-generated method stub
                    if (System.currentTimeMillis() - zanTime > 500) {
                        if (list.get(position).getIsExsitPoint().equals("1")) {
                            removeGoodsPoint(list.get(position).getID());
                        } else {
                            addGoodPoint(list.get(position).getID());
                        }
                    } else {
                        Toast.makeText(getActivity(), "您点赞频率过快！", Toast.LENGTH_SHORT).show();
                    }
                    zanTime = System.currentTimeMillis();
                }
            });


            ((MyViewHolder) holder).video_comment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(getActivity(), VideoMainActivity.class);
                    intent.putExtra("ID", list.get(position).getID()
                            .toString());
                    intent.putExtra("Title", list.get(position).getName()
                            .toString());
                    intent.putExtra("ID2", list.get(position).getID2()
                            .toString());
                    intent.putExtra("url", list.get(position).getMediaUrl());
                    context.startActivity(intent);
                }
            });


            ((MyViewHolder) holder).video_share.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    BaseShare bs = null;
                    try {
                        bs = new BaseShare(VideoFragment_V2.this.getActivity(),
                                list.get(position).getName(), "看电视、听广播、读资讯，尽在今日永州！", "http://gd.hh.hn.d5mt.com.cn/Share/MediaShares.aspx?GUID=" + list.get(position).getID2(), "http://gd.hh.hn.d5mt.com.cn/Images/ic_launcher.png", "99", "");


                        //						bs = new BaseShare(VideoFragment_V2.this.getActivity(),
                        //								list.get(position).getName(),list.get(position).getName(), "http://gd.hh.hn.d5mt.com.cn/Share/MediaShares.aspx?GUID="+list.get(position).getID2(), "http://gd.hh.hn.d5mt.com.cn/Images/ic_launcher.png", "99", "");

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

            ((MyViewHolder) holder).title.setText(list.get(position).getName());
            ((MyViewHolder) holder).from.setText("日期：" + list.get(position).getUploadDate());
            ((MyViewHolder) holder).tv_comment_num.setText(list.get(position).getCommentNum());
            ((MyViewHolder) holder).tv_read_num.setText(list.get(position).getClickNum());
            ImageLoader.getInstance().displayImage(
                    list.get(position).getImageUrl(), ((MyViewHolder) holder).image_bg);
            ((MyViewHolder) holder).layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // v.setVisibility(View.VISIBLE);
                    if (click != null)
                        click.onclick(position);
                }
            });


        }


        private onClick click;

        public void setClick(onClick click) {
            this.click = click;
        }


    }


    public static interface onClick {
        void onclick(int position);
    }

    public class MyViewHolder extends ViewHolder {
        FrameLayout videoLayout;
        RelativeLayout layout;
        TextView title, from, tv_comment_num;
        TextView tv_read_num;
        ImageView image_bg;
        RelativeLayout rel_layou;
        TextView video_share;
        TextView video_comment;
        TextView video_zan;

        public MyViewHolder(View itemView) {
            super(itemView);
            videoLayout = (FrameLayout) itemView
                    .findViewById(R.id.item_layout_video);
            rel_layou = (RelativeLayout) itemView
                    .findViewById(R.id.rel_layou);
            layout = (RelativeLayout) itemView
                    .findViewById(R.id.video_item_layout);
            image_bg = (ImageView) itemView
                    .findViewById(R.id.item_image_bg);
            title = (TextView) itemView.findViewById(R.id.item_title);

            tv_comment_num = (TextView) itemView.findViewById(R.id.tv_comment_num);
            tv_read_num = (TextView) itemView.findViewById(R.id.tv_read_num);
            from = (TextView) itemView.findViewById(R.id.tv_from);

            video_share = (TextView) itemView.findViewById(R.id.video_share);
            video_comment = (TextView) itemView.findViewById(R.id.video_comment);
            video_zan = (TextView) itemView.findViewById(R.id.video_zan);


            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                    , DisplayMetricsTool.getWidth(VideoFragment_V2.this.getActivity()) * 9 / 16);

            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                    , DisplayMetricsTool.getWidth(VideoFragment_V2.this.getActivity()) * 9 / 16);

            videoLayout.setLayoutParams(layoutParams);
            //			layoutParams1.addRule(RelativeLayout.BELOW, R.id.item_title);
            rel_layou.setLayoutParams(layoutParams1);
        }
    }


}