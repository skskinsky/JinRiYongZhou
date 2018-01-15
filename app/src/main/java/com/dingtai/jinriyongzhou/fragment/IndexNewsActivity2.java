package com.dingtai.jinriyongzhou.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.application.MyApplication;
import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.model.HotArea;
import com.dingtai.base.model.UpdateVersionModel;
import com.dingtai.base.model.UserChannelModel;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.receiver.NetstateReceiver;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DensityUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.WindowsUtils;
import com.dingtai.base.view.NewsTitleTextView;
import com.dingtai.base.view.SyncHorizontalScrollView;
import com.dingtai.dtflash.model.StartPageModel;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.dtpolitics.service.WenZhengAPI;
import com.dingtai.dtpolitics.service.WenZhengHttpService;
import com.dingtai.dtvoc.activity.TuiJianActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.ActivitySearch;
import com.dingtai.jinriyongzhou.activity.InstitutionDetailActivity;
import com.dingtai.jinriyongzhou.activity.NewTopiceActivity;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.jinriyongzhou.view.NoScrollViewPager;
import com.dingtai.newslib3.activity.NewsChannelWebView;
import com.dingtai.newslib3.activity.PinDaoActivity;
import com.dingtai.newslib3.adapter.News_Lanmu_Adapter;
import com.dingtai.newslib3.model.ChannelModel;
import com.dingtai.newslib3.model.ParentChannelModel;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activity.ActivityWeather;
import api.WeatherAPI;
import model.TianQiBean;
import model.WeatherBean;
import service.WeatherService;

import static com.dingtai.newslib3.tool.HttpRequest.get_channel_list;

//import com.dingtai.newslib3.activity.NewsActivity;


/**
 * 新闻列表
 */

/**
 * @author Administrator
 */
public class IndexNewsActivity2 extends BaseFragment implements
        OnCheckedChangeListener, OnClickListener {

    // 栏目列表
    ExpandableListView mExpandableListView;
    // 栏目列表
    ListView lvParent;
    ListView lvSub;

    // 所有父级栏目
    RuntimeExceptionDao<ParentChannelModel, String> dao_partent_channel;
    // 所有栏目或子栏目
    RuntimeExceptionDao<ChannelModel, String> dao_channel;

    private SyncHorizontalScrollView mhsv;
    private ViewGroup rl_scroll;
    private NewsActivity newsActivity;
    public CJNewsActivity cjNewsActivity;

    public static int ChaItem = 0;
    public NoScrollViewPager viewpager;
    public ViewPager mViewpager;
    public static String lanmuname = "新闻";
    public static String lanmuID;// 新闻栏目id
    private int mPreSelectItem;
    private News_Lanmu_Adapter myViewPagerAdapter;// 新闻栏目适配器
    public static String TypeGo = "index";// 频道跳转判断
    // 页面列表
    private ArrayList<Fragment> fragmentList;
    private List<Map<String, String>> fragmentnames;
    public TextView net_net;// 网络请求
    private DataBaseHelper databaseHelper;
    public List<ParentChannelModel> channelBeans;
    RuntimeExceptionDao<UserChannelModel, String> user_channe;// 用户选择的栏目
    private int Hardcolo; // 导航栏字体颜色
    // private int backColor = Color.parseColor("@color/common_color"); //
    // 导航栏背景颜色
    private ImageView reload;
    private RelativeLayout rela_anren;
    private ImageView main_iv_right;
    public static UpdateVersionModel versionModel;// 服务器返回版本信息
    private int color = Color.parseColor("#00000000");
    private View mMainView;
    private PopupWindow pop;
    private boolean isShow = false;
    private int index = 0;
    private int height;
    private List<View> mViews;
    private Drawable drawable;
    private NetstateReceiver netReceiver;
    AnimationDrawable animationdrawable;

    private RelativeLayout rl_drawable;
    private ImageView iv_lishi, iv_dingyue;
    private LinearLayout ll_title;
    private Boolean isTitle = true;
    private TextView tv_tuijian, tv_wode;
    private HotArea hotArea = null;
    private List<PoliticsAreaModel> politicsAreaModelList;
    private List<SubscribeListsModel> subscribeList;
    //    private HallGridviewInTopAdapter hallGridviewAdapter;
    private static final int RESET_UI = 101;
    // tap 条目的比例
    private final static double TYPERATE = 1.4;

    private ViewPager LeftPage, RightPage;
    private LinearLayout ll_oval_left, ll_oval_right;
    private LinearLayout ll_left, ll_right;

    private TextView tv_weather;
    /**
     * 加载的图
     */
//    private MyGridView myGridView;
    private ImageView loading_iv;
    private RuntimeExceptionDao<StartPageModel, String> startPage;

    //
//    private RecyclerView model_recyclerview, subscribe_recyclerview;
//    private TopModelRecyAdapter modelRecyAdapter;
//    private SubscribeRecyAdapter subscribeRecyAdapter;
    private boolean Showmodel = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESET_UI:
                    //addSub();
                    if (cityPosition != -1) {
                        NewsTitleTextView tv = (NewsTitleTextView) rl_scroll.getChildAt(2);
                        int itemWidth = (int) tv.getPaint().measureText(hotArea.getAreaTitle());
                        tv.setLayoutParams(new LayoutParams(
                                (itemWidth + DisplayMetricsTool.dip2px(getActivity(), 30)),
                                LayoutParams.MATCH_PARENT, -1));

                        if (hotArea.getAreaId().equals("453")) {
                            hotArea.setAreaTitle("冷水滩");
                        }
                        tv.setText(hotArea.getAreaTitle());

                        NewsActivity newAcitvity = (NewsActivity) fragmentList.get(cityPosition);
                        newAcitvity.setLanmuID(hotArea.getAreaId());

                        newAcitvity.setCityName(hotArea.getAreaTitle());
                        newAcitvity.refresh(1);

//                    newAcitvity.firstrefresh();

                    }


                    break;
                case 178:
                    List<ParentChannelModel> list = dao_partent_channel
                            .queryForAll();
                    if (list.size() == 0) {
                        getSubject("0");
                    } else {
                        try {
                            // rela_anren.setVisibility(RelativeLayout.GONE);
                            List<UserChannelModel> sublist = user_channe
                                    .queryForAll();
                            if (sublist != null && sublist.size() > 0) {
                                for (UserChannelModel u : sublist) {
                                    ParentChannelModel n = new ParentChannelModel();
                                    n.setID(u.getID());
                                    n.setChannelName(u.getChannelName());
                                    n.setImageUrl(u.getImageUrl());
                                    n.setIsAd(u.getIsAd());
                                    n.setShowOrder(u.getShowOrder());
                                    n.setChannelUrl(u.getChannelUrl());
                                    n.setIsDel(u.getIsDel());
                                    n.setIsHtml(u.getIsHtml());
                                    if (u.getChannelName().equals("区县")) {
                                        n.setChannelName(getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("area", "冷水滩"));
                                        n.setID(getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("areaID", "453"));
                                    }
                                    channelBeans.add(n);
                                }
                            } else {

                                channelBeans.addAll(list);
                            }

                        } catch (Exception e) {

                        }


                        // 初始化栏目
                        initTabValue();
                        // 初始化viewpage
                        initViewPage();
                    }
                    break;
                case 101010:
                    startTranslateAnimation(mViews.get(index));
                    index++;
                    break;
                case 996633:
                    rela_anren.setVisibility(View.VISIBLE);
                    rela_anren.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO 自动生成的方法存根
                            rl_drawable.setVisibility(View.VISIBLE);
                            loading_iv.setVisibility(View.VISIBLE);
                            animationdrawable.start();
                            rela_anren.setVisibility(View.GONE);
                            getSubject("0");
                        }
                    });
                    break;
                case 1:
                    // if (reload != null) {
                    rela_anren.setVisibility(RelativeLayout.GONE);
                    // }
                    dao_partent_channel = databaseHelper.getMode(ParentChannelModel.class);
                    ArrayList<ParentChannelModel> arrs = (ArrayList<ParentChannelModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    // for (ParentChannelModel m :
                    // dao_partent_channel.queryForAll()) {
                    // DrawerView.pidToChanceList.add(dao_channel.queryForEq(
                    // "ParentID", m.getID()));
                    // }
                    channelBeans.addAll(arrs);
                    // 初始化栏目
                    initTabValue();
                    // 初始化viewpage
                    initViewPage();
                    animationdrawable.stop();
                    loading_iv.setVisibility(View.GONE);
                    rl_drawable.setVisibility(View.GONE);
                    break;
                case 0:
                    try {
                        // if (reload != null) {
                        // rela_anren.removeView(item);// 移除进度条
                        rela_anren.setVisibility(RelativeLayout.GONE);
                        // }
                        channelBeans.clear();
                        ArrayList<ParentChannelModel> arrs2 = (ArrayList<ParentChannelModel>) msg
                                .getData().getParcelableArrayList("list").get(0);
                        List<UserChannelModel> all1 = user_channe.queryForAll();
                        if (all1.size() > 0) {
                            for (UserChannelModel u : all1) {
                                ParentChannelModel n = new ParentChannelModel();
                                n.setID(u.getID());
                                n.setChannelName(u.getChannelName());
                                n.setImageUrl(u.getImageUrl());
                                n.setIsAd(u.getIsAd());
                                n.setShowOrder(u.getShowOrder());
                                n.setIsHtml(u.getIsHtml());
                                channelBeans.add(n);
                            }
                        } else {
                            channelBeans.clear();
                            channelBeans.addAll(arrs2);
                        }
                        Log.i("test", "initTabValue:294");
                        initTabValue(); // 初始化栏目
                        initViewPage(); // 初始化viewpage
                        String str1 = (String) msg.obj;
                        Toast.makeText(getActivity(), str1, Toast.LENGTH_SHORT)
                                .show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "未返回栏目列表数据",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1111:
                    // if (reload != null) {
                    // rela_anren.removeView(item);// 移除进度条
                    rela_anren.setVisibility(RelativeLayout.GONE);
                    // }
                    Log.i("test", "initTabValue:308");
                    initTabValue(); // 初始化栏目
                    initViewPage(); // 初始化viewpage
                    break;
                case 2222:
                    Toast.makeText(getActivity(), "栏目获取失败", Toast.LENGTH_SHORT).show();
                    break;
                case 220:

                    ArrayList list2 = (ArrayList) msg.getData().getParcelableArrayList("list").get(0);
                    subscribeList = (List<SubscribeListsModel>) list2;
                    if (subscribeList != null) {
//                        initSubscribe();
                        initPagerRight();
                    }
                    break;
                case 500:
                    try {
                        List<WeatherBean> weatherList = (List<WeatherBean>) msg.getData()
                                .getParcelableArrayList("list").get(0);
                        if (weatherList != null && weatherList.size() > 0) {
                            WeatherBean weatherBean = weatherList.get(0);
                            List<TianQiBean> list123 = null;
                            if (weatherBean != null) {
                                list123 = weatherBean.getList();
                                String wendu = weatherBean.getWendu();
                                String weilaitianqi = list123.get(0).getWenlaitianqi();
                                tv_weather.setText(wendu + "℃");
                                tv_weather.setVisibility(View.VISIBLE);
                                jieWeather(weilaitianqi);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };
    private int cityPosition = -1;


    private void jieWeather(String str) {
        if (str.indexOf("转") > 0) {
            String[] split = str.split("转");
            getWetherBackPic(split[0]);
        } else {
            getWetherBackPic(str);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.index_list_data1, container, false);
        return mMainView;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 500:
                    ArrayList list = (ArrayList) msg.getData().getParcelableArrayList("list").get(0);
                    List<PoliticsAreaModel> temp_list = (List<PoliticsAreaModel>) list.get(1);
                    for (int i = 0; i < temp_list.size() - 1; i++) {
                        if ("1".equals(temp_list.get(i).getIsRec()) || "3".equals(temp_list.get(i).getIsRec())) {
                            politicsAreaModelList.add(temp_list.get(i));
                        }
                    }
                    if (politicsAreaModelList != null) {
//                        hallGridviewAdapter.setData(politicsAreaModelList);
//                        hallGridviewAdapter.notifyDataSetChanged();

//                        initPager();
                        initPagerLeft();
                    }
                    break;


            }
        }
    };

    public void getPoliticIndexInfomation() {
        String url = WenZhengAPI.POLITICS_INDEX_URL;
        String StID = "0";
        this.get_politicsIndex_info(this.getActivity(), url, StID, new Messenger(this.mHandler));
    }

    public void get_politicsIndex_info(Context paramContext, String url, String StID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, WenZhengHttpService.class);
        intent.putExtra("api", 313);
        intent.putExtra(WenZhengAPI.POLITICS_INDEX_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        paramContext.startService(intent);
    }

    public void inite() {
        Hardcolo = getResources().getColor(R.color.common_color);
        // 初始化控件
        initChannel();
        main_iv_right = (ImageView) mMainView.findViewById(R.id.main_iv_right);
        try {
            main_iv_right.setOnClickListener(this);
        } catch (Exception e) {

        }
        SharedPreferences sp = getActivity().getSharedPreferences(
                "spAnimation", Context.MODE_PRIVATE);
        myViewPagerAdapter = new News_Lanmu_Adapter(getChildFragmentManager(),
                fragmentList, fragmentnames);

        databaseHelper = getHelper();
//        mViewpager = (ViewPager) mMainView.findViewById(R.id.mviewpager);
        LeftPage = (ViewPager) mMainView.findViewById(R.id.pager);
        ll_oval_left = (LinearLayout) mMainView.findViewById(R.id.ll_oval);
        RightPage = (ViewPager) mMainView.findViewById(R.id.pager2);
        ll_oval_right = (LinearLayout) mMainView.findViewById(R.id.ll_oval2);

        ll_left = (LinearLayout) mMainView.findViewById(R.id.ll_left);
        ll_right = (LinearLayout) mMainView.findViewById(R.id.ll_right);
        tv_tuijian = (TextView) mMainView.findViewById(R.id.tv_tuijian);
        tv_wode = (TextView) mMainView.findViewById(R.id.tv_wode);
        tv_weather = (TextView) mMainView.findViewById(R.id.tv_weather);
        tv_weather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityWeather.class));
            }
        });
        tv_tuijian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tuijian.setTextColor(getResources().getColor(R.color.common_color));
                tv_wode.setTextColor(getResources().getColor(R.color.black));
//                myGridView.setVisibility(View.GONE);
                ll_left.setVisibility(View.VISIBLE);
                ll_right.setVisibility(View.GONE);
            }
        });
        tv_wode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_tuijian.setTextColor(getResources().getColor(R.color.black));
                tv_wode.setTextColor(getResources().getColor(R.color.comment_blue));
                ll_left.setVisibility(View.GONE);
                ll_right.setVisibility(View.VISIBLE);
                if (Assistant.getUserInfoByOrm(getActivity()) == null) {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction(getActivity().getPackageName() + "." + "login");
                    startActivity(intent);
                } else {

                    getSubscribe();
                }
            }
        });
        ll_title = (LinearLayout) mMainView.findViewById(R.id.ll_title);

        politicsAreaModelList = new ArrayList<>();
        subscribeList = new ArrayList<>();
        iv_lishi = (ImageView) mMainView.findViewById(R.id.iv_lishi);
        iv_lishi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivitySearch.class));
            }
        });
//        mMainView.findViewById(R.id.iv_user).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), CommonActivity.class);
//                intent.putExtra("name", "我的");
//                startActivity(intent);
//            }
//        });
        iv_dingyue = (ImageView) mMainView.findViewById(R.id.iv_dingyue);
        iv_dingyue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (politicsAreaModelList != null && politicsAreaModelList.size() == 0) {
                    ll_title.setVisibility(View.VISIBLE);
                    isTitle = false;
                    getPoliticIndexInfomation();
                } else {
                    if (isTitle) {
                        ll_title.setVisibility(View.VISIBLE);
                        isTitle = false;
                    } else {
                        ll_title.setVisibility(View.GONE);
                        isTitle = true;
                    }
                }
            }
        });

        loading_iv = (ImageView) mMainView.findViewById(R.id.loading_iv);
        rl_drawable = (RelativeLayout) mMainView.findViewById(R.id.rl_drawable);
        loading_iv.setImageDrawable(getResources().getDrawable(
                R.drawable.progress_round));
        animationdrawable = (AnimationDrawable) loading_iv.getDrawable();
        user_channe = databaseHelper.getMode(UserChannelModel.class);
        dao_partent_channel = databaseHelper.getMode(ParentChannelModel.class);
        dao_channel = databaseHelper.getMode(ChannelModel.class);
        handler.sendEmptyMessageDelayed(178, 300);
        net_net = (TextView) mMainView.findViewById(R.id.net_net);
        // 注册网络广播监听
        netReceiver = new NetstateReceiver(getActivity(), net_net, handler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        getActivity().registerReceiver(netReceiver, intentFilter);
        net_net.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent wifiSettingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(wifiSettingsIntent);
            }
        });

        getCityWeather();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inite();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case -1:
                hotArea = (HotArea) data.getSerializableExtra("hotArea");
                saveArea(hotArea);
                handler.sendEmptyMessage(RESET_UI);
                break;
            default:
                if (requestCode == 10) {
                    addSub();
                }
                break;
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveArea(HotArea hotArea) {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("city",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("area", hotArea.getAreaTitle());
        //提交当前数据
        editor.commit();
    }

    /**
     * 初始化控件及事件绑定
     */
    public void initChannel() {
        // 新闻列表标题
        mhsv = (SyncHorizontalScrollView) mMainView.findViewById(R.id.mhsv);
        rl_scroll = (ViewGroup) mMainView.findViewById(R.id.tab_content);
        rela_anren = (RelativeLayout) mMainView.findViewById(R.id.rela_anren);
        try {
            mhsv.setSomeParam(rl_scroll, null, null, getActivity());
            viewpager = (NoScrollViewPager) mMainView.findViewById(R.id.viewpager);
            fragmentList = new ArrayList<Fragment>();
            fragmentnames = new ArrayList<Map<String, String>>();
            channelBeans = new ArrayList<ParentChannelModel>();
        } catch (Exception e) {
        }
    }

    /**
     * 对于栏目里面的数据的初始化，及页面监听
     */
    public void initViewPage() {
        viewpager.setScroll(true);
        WindowsUtils.getWindowSize(getActivity());
        Log.d("xf", WindowsUtils.width + "------->");
        // 初始化ID
        try {
            fragmentList.clear();
//            int j = 0;
            for (int i = 0; i < channelBeans.size() + 1; i++) {
//                    j = i - 5;
                if (i == 0) {
                    CJNewsActivity home = new CJNewsActivity();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", "头条");
                    map.put("id", "0");
                    fragmentList.add(home);
                    fragmentnames.add(map);
                } else {
                    int j = i - 1;
                    if ("广电直播".equals(channelBeans.get(j).getChannelName())) {
                        fragmentList.add(new LiveVideoListActivity2());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", "广电直播");
                        map.put("id", "2");
                        fragmentnames.add(map);
                    } else if ("报纸".equals(channelBeans.get(j).getChannelName())) {
                        fragmentList.add(new IndexRead());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", "报纸");
                        map.put("id", "3");
                        fragmentnames.add(map);
                    } else if ("政务".equals(channelBeans.get(j).getChannelName())) {
                        fragmentList.add(new ZhengwuFragment());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", "政务");
                        map.put("id", "4");
                        fragmentnames.add(map);
                    } else if ("视频".equals(channelBeans.get(j).getChannelName())) {
                        fragmentList.add(new TuiJianActivity());
                        Map<String, String> map = new HashMap<>();
                        map.put("name", "视频");
                        map.put("id", "5");
                        fragmentnames.add(map);
                    } else if ("活动".equals(channelBeans.get(j).getChannelName())) {
                        fragmentList.add(new ActiveFragment());
                        Map<String, String> map = new HashMap<>();
                        map.put("name", "活动");
                        map.put("id", "6");
                        fragmentnames.add(map);
                    } else if ("互动".equals(channelBeans.get(j).getChannelName())) {
                        CooperationFragment fragment = new CooperationFragment();
                        fragmentList.add(fragment);
                        Map<String, String> map = new HashMap<>();
                        map.put("name", "互动");
                        map.put("id", "7");
                        fragmentnames.add(map);
                    } else if ("报纸网络".equals(channelBeans.get(j).getChannelName())) {
                        PaperNetFragment fragment = new PaperNetFragment();
                        fragment.setChannalId(channelBeans.get(j).getID());
                        fragmentList.add(fragment);
                        Map<String, String> map = new HashMap<>();
                        map.put("name", "报纸网络");
                        map.put("id", "8");
                        fragmentnames.add(map);
                    } else if ("True".equals(channelBeans.get(j).getIsHtml())) {
                        NewsChannelWebView fragment = new NewsChannelWebView();
                        fragment.setUrl(channelBeans.get(j).getChannelUrl());
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", channelBeans.get(j).getChannelName());
                        map.put("id", channelBeans.get(j).getID());
                        fragmentnames.add(map);
                        fragmentList.add(fragment);
                    } else {
                        newsActivity = new NewsActivity();
                        newsActivity.setLanmuID(channelBeans.get(j).getID()
                                + "");
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("name", channelBeans.get(j).getChannelName());
                        map.put("id", channelBeans.get(j).getID());
                        fragmentList.add(newsActivity);
                        fragmentnames.add(map);
                    }
                }
                ParentChannelModel parentChannelModel;
                String id;
                int id1;
                for (int i1 = 0; i1 < channelBeans.size(); i1++) {
                    parentChannelModel = channelBeans.get(i1);
                    id = parentChannelModel.getID();
                    id1 = Integer.valueOf(id);
                    if (id1 == 443 || id1 == 449 || (id1 >= 450 && id1 <= 459)) {
                        cityPosition = i1 + 1;
                    }
                }

//                else {
//                    Map<String, String> map = new HashMap<String, String>();
//                    cjNewsActivity = new CJNewsActivity();
//                    fragmentList.add(cjNewsActivity);
//                    map.put("name", "头条");
//                    map.put("id", "0");
//                    fragmentnames.add(map);
//
//                    fragmentList.add(new LiveVideoListActivity());
//                    map.put("name", "电视");
//                    map.put("id", "2");
//                    fragmentnames.add(map);
//
//                    fragmentList.add(new IndexRead());
//                    map.put("name", "报纸");
//                    map.put("id", "3");
//                    fragmentnames.add(map);
            }


            viewpager.setAdapter(myViewPagerAdapter);
            // 缓存所有页面
            viewpager.setOffscreenPageLimit(fragmentList.size());

            myViewPagerAdapter.notifyDataSetChanged();
            viewpager.setOnPageChangeListener(new OnPageChangeListener() {
                int screenWidth = getActivity().getWindowManager()
                        .getDefaultDisplay().getWidth();// 720

                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
                @SuppressLint("NewApi")
                @Override
                public void onPageSelected(int arg0) {
                    viewpager.setScroll(true);
                    if (arg0 > 0) {
                        if (fragmentList.get(arg0) instanceof NewsActivity) {
                            ((NewsActivity) fragmentList.get(arg0)).refresh(1);
                            ((NewsActivity) fragmentList.get(arg0)).initData();
                        } else if (fragmentList.get(arg0) instanceof ZhengwuFragment) {
                            ((ZhengwuFragment) fragmentList.get(
                                    arg0))
                                    .inite();
                        } else if (fragmentList.get(arg0) instanceof CJNewsActivity) {//首页
                            ((CJNewsActivity) fragmentList.get(arg0)).refresh();
                        } else if (fragmentList.get(arg0) instanceof IndexRead) {//读报
                            ((IndexRead) fragmentList.get(arg0)).loading();
                        } else if (fragmentList.get(arg0) instanceof TuiJianActivity) {//视频
                            ((TuiJianActivity) fragmentList.get(arg0)).refresh(0);
                        } else if (fragmentList.get(arg0) instanceof LiveVideoListActivity) {//电视
                            ((LiveVideoListActivity) fragmentList.get(arg0)).inite();
                        } else if (fragmentList.get(arg0) instanceof CooperationFragment) {
//                            ((CooperationFragment) fragmentList.get(x)).
                        }
                        lanmuname = channelBeans.get(arg0 - 1).getChannelName();
                        lanmuID = channelBeans.get(arg0 - 1).getID() + "";
                    } else {
                        ((CJNewsActivity) fragmentList.get(arg0)).refresh();
                        lanmuname = channelBeans.get(0).getChannelName();
                        lanmuID = channelBeans.get(0).getID() + "";
                    }


                    // 点击当前按钮左边按钮的宽度
                    int visiableWidth = 0;
                    // HorizontalScrollView的宽
                    int scrollViewWidth = 0;
                    // 当前点击按钮的宽�?
                    int nextTitleWidth = 0;
                    for (int i = 0; i < rl_scroll.getChildCount(); i++) {
                        NewsTitleTextView itemView = (NewsTitleTextView) rl_scroll
                                .getChildAt(i);
                        int width = itemView.getMeasuredWidth();
                        if (i < arg0) {
                            visiableWidth += width;
                        }
                        scrollViewWidth += width;
                        if (arg0 != i) {
                            itemView.setTextColor(Color.GRAY);
                            itemView.setIsHorizontaline(false);
                            itemView.setCompoundDrawables(null, null, null,
                                    null);
                            // itemView.setBackgroundColor(Color.WHITE);
                            // itemView.setTextSize(16);
                        } else {
                            itemView.setTextColor(Hardcolo);
                            itemView.setIsHorizontaline(false);
                            itemView.setCompoundDrawables(null, null, null,
                                    drawable);
                            ;
                        }
                    }
                    int titleWidth = rl_scroll.getChildAt(arg0)
                            .getMeasuredWidth(); // 146
                    final int move = visiableWidth - (screenWidth - titleWidth)
                            / 2;
                    if (mPreSelectItem < arg0) {// 向屏幕右边移边
                        if ((visiableWidth + titleWidth + nextTitleWidth) >= (screenWidth / 2)) {
                            mhsv.setScrollX(move);
                        }
                    } else {// 向屏幕左边移边
                        if ((scrollViewWidth - visiableWidth) >= (screenWidth / 2)) {
                            mhsv.setScrollX(move);
                        }
                    }
                    mPreSelectItem = arg0;
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
        } catch (Exception e) {
        }
    }


    private LinearLayout view;

    private void startTranslateAnimation(final View view) {
        TranslateAnimation animation = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, 0,
                TranslateAnimation.RELATIVE_TO_SELF, -0.1f,
                TranslateAnimation.RELATIVE_TO_SELF, 0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        // // 修改view的y属性, 从当前位置移动到300.0f
        // ObjectAnimator yBouncer = ObjectAnimator.ofFloat(view, "y",
        // view.getY(), view.getY() - 20);
        // yBouncer.setDuration(1000);
        // // 设置插值器(用于调节动画执行过程的速度)
        animation.setRepeatMode(Animation.REVERSE);
        // // 设置重复次数(缺省为0,表示不重复执行)
        animation.setRepeatCount(1);
        // //animation设置重复模式(RESTART或REVERSE),重复次数大于0或INFINITE生效
        // yBouncer.setRepeatMode(ValueAnimator.REVERSE);
        // // 设置动画开始的延时时间(200ms)
        animation.setStartTime(200);
        // // 开始动画
        // yBouncer.start();
        view.startAnimation(animation);
        animation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
                if (index == mViews.size()) {
                    index = 0;
                    return;
                }
                handler.sendEmptyMessageDelayed(101010, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }
        });
    }

    private String getCityCode(String cityName) {//根据城市名称，到资源文件中找城市id
        Resources res = getActivity().getResources();
        try {
            Field field = R.string.class.getField(cityName);
            int id = field.getInt(new R.string());
            String cityCode = res.getString(id);
            Log.d("xf", cityCode);
            return cityCode;
        } catch (Exception e) {
            Log.e("xf", e.toString());
        }
        return "101250301";
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    private void getWetherBackPic(String string) {
        Drawable drawable = null;
        Resources resources = getActivity().getResources();
        if ("大雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_dayu1);
        }
        if ("阵雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_zhenyu1);
        }
        if ("雷阵雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_leizhenyu1);
        }
        if ("雷阵雨有冰雹".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_leizhenyujiabingbao1);
        }
        if ("雨夹雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_yujiaxue1);
        }
        if ("中雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_zhongyu1);
        }
        if ("暴雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_baoyu1);
        }
        if ("大暴雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_dabaoyu1);
        }
        if ("特大暴雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_tedabaoyu1);
        }
        if ("冻雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_dongyu1);
        }
        if ("多云".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_duoyun1);
        }
        if ("雷电".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_leizhenyu1);
        }
        if ("晴".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_qing1);
        }
        if ("雾".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_wu1);
        }
        if ("雾霾".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_wumai1);
        }
        if ("沙尘暴".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_shachenbao1);
        }
        if ("浮尘".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_fuchen1);
        }
        if ("扬沙".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_yangsha1);
        }
        if ("强沙尘暴".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_qiangshachenbao1);
        }
        if ("小雨".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_xiaoyu1);
        }
        if ("阵雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_zhenxue1);
        }
        if ("小雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_xiaoxue1);
        }
        if ("中雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_zhongxue1);
        }
        if ("大雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_daxue1);
        }
        if ("暴雪".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_baoxue1);
        }
        if ("阴".equals(string)) {
            drawable = resources.getDrawable(R.drawable.l_yin1);
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_weather.setCompoundDrawables(drawable, null, null, null);
        }
    }

    //获取城市天气
    private void getCityWeather() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String cityCode = getCityCode(getCityName());
                String url = API.COMMON_URL
                        + "Interface/WeatherApi.ashx?action=Weather&CityID="
                        + cityCode + "&STid=" + API.STID;
                requestData(getContext(), url, new Messenger(handler), WeatherAPI.TIANQI_API, WeatherAPI.TIANQI_MSG, WeatherService.class);
            }
        });
    }


    private String getCityName() {//获取城市名称
        String cityName = ((MyApplication) getActivity().getApplication()).city;
        if (cityName == null) {
            cityName = API.city;
        }
        if (cityName.contains("市")) {
            cityName = cityName.substring(0, cityName.indexOf("市"));
        } else if (cityName.contains("区")) {
            cityName = cityName.substring(0, cityName.indexOf("区"));
        } else if (cityName.contains("县")) {
            cityName = cityName.substring(0, cityName.indexOf("县"));
        } else if (cityName.contains("自治区")) {
            cityName = cityName.substring(0, cityName.indexOf("自治区"));
        }
        return cityName;
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 对于栏目控件的初始化及事件监听
     */
    int j;

    private void initTabValue() {

        drawable = getResources().getDrawable(
                R.drawable.dt_standard_shouye_xiahuaxian);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight()); // 设置边界
        try {
            rl_scroll.removeAllViews();
            // 加入订阅的栏目
            // addSub();

            for (int i = 0; i < channelBeans.size() + 1; i++) {

                if (i == 0) {
                    NewsTitleTextView tv = new NewsTitleTextView(getActivity());
                    int itemWidth = (int) tv.getPaint().measureText("头条");
                    tv.setLayoutParams(new LayoutParams(
                            (int) (TYPERATE * itemWidth),
                            LayoutParams.MATCH_PARENT, 1));
                    tv.setGravity(Gravity.CENTER);
                    tv.setTextSize(16);
                    tv.setText("头条");// channelBeans.get(i).getChannelName()
                    // tv.getPaint().setFakeBoldText(true);
                    tv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            for (int i = 0; i < rl_scroll.getChildCount(); i++) {
                                NewsTitleTextView child = (NewsTitleTextView) rl_scroll
                                        .getChildAt(i);
                                if (child == v) {
                                    lanmuname = "头条";
                                    lanmuID = "0";
                                    child.setCompoundDrawables(null, null,
                                            null, drawable);
                                    child.setTextColor(getResources().getColor(R.color.common_color));
                                    viewpager.setCurrentItem(i);
                                    break;
                                } else {
                                    child.setBackgroundColor(getResources().getColor(R.color.grey));
                                    child.setCompoundDrawables(null, null,
                                            null, null);

                                }
                            }
                        }
                    });
                    tv.setCompoundDrawables(null, null,
                            null, drawable);
                    tv.setTextColor(getResources().getColor(R.color.common_color));
                    viewpager.setCurrentItem(i);
                    rl_scroll.addView(tv);
                } else {
                    j = i - 1;
                    NewsTitleTextView tv = new NewsTitleTextView(getActivity());
                    int itemWidth = (int) tv.getPaint().measureText(
                            channelBeans.get(j).getChannelName());
                    tv.setLayoutParams(new LayoutParams(
                            (int) (TYPERATE * itemWidth),
                            LayoutParams.MATCH_PARENT, 1));
                    tv.setTextSize(16);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText(channelBeans.get(j).getChannelName());// channelBeans.get(i).getChannelName()
                    tv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            rl_scroll.setFocusable(true);
                            for (int j = 0; j < rl_scroll.getChildCount(); j++) {
                                NewsTitleTextView child = (NewsTitleTextView) rl_scroll
                                        .getChildAt(j);
                                if (child == v) {
                                    if (j > 0) {
                                        if (fragmentList.get(j) instanceof NewsActivity) {
                                            ((NewsActivity) fragmentList.get(j))
                                                    .refresh(0);
                                            ((NewsActivity) fragmentList.get(j)).initData();
                                        } else if (fragmentList.get(j) instanceof ZhengwuFragment) {
                                            ((ZhengwuFragment) fragmentList.get(
                                                    j))
                                                    .inite();
                                        } else if (fragmentList.get(j) instanceof CJNewsActivity) {//首页
                                            ((CJNewsActivity) fragmentList.get(j)).inite();
                                        } else if (fragmentList.get(j) instanceof IndexRead) {//读报
                                            ((IndexRead) fragmentList.get(j)).loading();
                                        } else if (fragmentList.get(j) instanceof TuiJianActivity) {//视频
                                            ((TuiJianActivity) fragmentList.get(j)).refresh(0);
                                        } else if (fragmentList.get(j) instanceof LiveVideoListActivity) {//电视
                                            ((LiveVideoListActivity) fragmentList.get(j)).inite();
                                        } else if (fragmentList.get(j) instanceof CooperationFragment) {
                                            ((CooperationFragment) fragmentList.get(j)).inite();
                                        }
                                        lanmuname = channelBeans.get(index)
                                                .getChannelName();
                                        lanmuID = channelBeans.get(index)
                                                .getID() + "";
                                    }

                                    viewpager.setCurrentItem(j);
                                    break;
                                } else {
                                    child.setBackgroundColor(color);
                                }
                            }
                        }
                    });
                    if (i == 0) {
                        tv.setTextColor(Hardcolo);
                        tv.setCompoundDrawables(null, null, null, drawable);
                    } else {
                        tv.setIsHorizontaline(false);
                        tv.setTextColor(Color.GRAY);
                        tv.setCompoundDrawables(null, null, null, null);
                    }
                    rl_scroll.addView(tv);

                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 从服务端获取栏目 subID为0表示获取顶级栏目
     */
    public void getSubject(String subID) {
        String url = API.COMMON_URL
                + "interface/NewsChannelAPI.ashx?action=GetNewsChannelList";
        String stID = API.STID;
        get_channel_list(getActivity(), url, "0", stID, new Messenger(
                this.handler));
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (Assistant.IsContectInterNet(getActivity(), false)) {
            if (channelBeans.size() == rl_scroll.getChildCount()) { // 必须等横向item加载完成以后才能够点
                viewpager.setCurrentItem(checkedId);
            }
        }
    }

    @Override
    public void onClick(View v) {// TODO 点击事件处理
        Intent i = null;
        switch (v.getId()) {
            case R.id.main_iv_right:
                i = new Intent(getActivity(), PinDaoActivity.class);
                TypeGo = "index";
                startActivityForResult(i, 10);
                break;
            default:
                break;
        }
    }

    public void addSub() {
        try {
            channelBeans.clear();
            fragmentnames.clear();
            mPreSelectItem = 0;
            List<UserChannelModel> all = user_channe.queryForAll();
            for (UserChannelModel u : all) {
                ParentChannelModel n = new ParentChannelModel();
                n.setID(u.getID());
                n.setChannelName(u.getChannelName());
                n.setImageUrl(u.getImageUrl());
                n.setIsAd(u.getIsAd());
                n.setShowOrder(u.getShowOrder());
                n.setChannelUrl(u.getChannelUrl());
                n.setIsDel(u.getIsDel());
                n.setIsHtml(u.getIsHtml());
                channelBeans.add(n);

            }
            if (channelBeans.size() > 0) {
                for (int i = 0; i < channelBeans.size(); i++) {
                    ParentChannelModel parentChannelModel = channelBeans.get(i);
                    if (parentChannelModel.getChannelName().equals("区县")) {
                        parentChannelModel.setChannelName(getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("area", "冷水滩"));
                        parentChannelModel.setID(getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("areaID", "453"));
                    }
                }
            }
            initTabValue();
            initViewPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器获取我的订阅
     */
    public void getSubscribe() {
        UserInfoModel model = Assistant.getUserInfoByOrm(getActivity());
        String url = API.COMMON_URL
                + "Interface/PoliticsAPI.ashx?action=SubscribeForMy";
        String GUID = model.getUserGUID();
        get_subscribe_list(getActivity(), url, GUID, new Messenger(
                this.handler));

    }

    /**
     * 获取我的订阅
     *
     * @param paramContext
     * @param url
     * @param GUID
     * @param paramMessenger
     */
    public static void get_subscribe_list(Context paramContext, String url, String GUID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_SUBSCRIBE_LIST);
        intent.putExtra(IndexAPI.GET_SUBSCRIBE_LIST_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("GUID", GUID);
        paramContext.startService(intent);
    }


    private ArrayList<AreaModelFragment> list;
    private List<View> dots = new ArrayList<View>();

    private void initPagerLeft() {
        LeftPage.removeAllViews();
        list = new ArrayList<AreaModelFragment>();

        int size = politicsAreaModelList.size();
        int pagerNumber = 0;
        if (size % 10 == 0) {
            pagerNumber = size / 8;
        } else {
            pagerNumber = size / 8 + 1;
        }
        if (pagerNumber >= 1) {
            initeOvalLeft(pagerNumber);
        }
        for (int i = 0; i < pagerNumber; i++) {
            AreaModelFragment fragment = new AreaModelFragment(getActivity());
            List<PoliticsAreaModel> subList = null;
            if (pagerNumber - 1 == i) {
                subList = politicsAreaModelList.subList(i * 8,
                        size % 8 == 0 ? (i + 1) * 8 : i * 8 + size % 8);
            } else {
                subList = politicsAreaModelList.subList(i * 8, (i + 1) * 8);
            }
            fragment.setData(subList);
            fragment.setItemClick(new AreaModelFragment.ItemClick() {
                @Override
                public void onItemClick(int position, PoliticsAreaModel indexModel) {
                    //跳转
                    String DeptID = politicsAreaModelList.get(position).getAreaID();
                    String aredIcon = politicsAreaModelList.get(position).getAreaSmallPicUrl();
                    String aredNanme = politicsAreaModelList.get(position).getAreaPoliticsAreaName();
                    String reMark = politicsAreaModelList.get(position).getAreaReMark();
//                    Intent intent = new Intent(getActivity(), InstitutionDetailActivity.class);
                    Intent intent = new Intent(getActivity(), NewTopiceActivity.class);
                    intent.putExtra("DeptID", DeptID);
                    intent.putExtra("reMark", reMark);
                    intent.putExtra("aredIcon", aredIcon);
                    intent.putExtra("aredNanme", aredNanme);
                    startActivity(intent);
                }
            });
            list.add(fragment);
        }
        // fragmentAdapter = new
        // MutualFragmentAdapter(getChildFragmentManager(),
        // list);
        LeftPage.setAdapter(new MyAdapter());
        LeftPage.setOffscreenPageLimit(list.size());
        LeftPage.setOnPageChangeListener(new OnPageChangeListener() {

            @SuppressLint("NewApi")
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < dots.size(); i++) {
                    if (i == arg0) {
                        dots.get(i).setBackground(
                                getActivity().getResources().getDrawable(
                                        R.drawable.dot_foucus));
                    } else {
                        dots.get(i).setBackground(
                                getActivity().getResources().getDrawable(
                                        R.drawable.normal_dot));
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    /**
     * 初始化圆点
     */
    @SuppressLint("NewApi")
    private void initeOvalLeft(int num) {
        try {
            ll_oval_left.removeAllViews();
            if (num == 1) {
                return;
            }
            dots.clear();
            for (int i = 0; i < num; i++) {
                View view = new View(getActivity());
                if (i == 0) {
                    view.setBackground(getActivity().getResources()
                            .getDrawable(R.drawable.dot_foucus));
                } else {
                    view.setBackground(getActivity().getResources()
                            .getDrawable(R.drawable.normal_dot));
                }
                LayoutParams params = new LayoutParams(DensityUtil.dip2px(
                        getActivity(), 8), DensityUtil.dip2px(getActivity(), 8));
                params.leftMargin = DensityUtil.dip2px(getActivity(), 10);
                view.setLayoutParams(params);
                dots.add(view);
                ll_oval_left.addView(view);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewGroup) container).addView(list.get(position).mMainView);// 添加页卡
            return list.get(position).mMainView;
        }

    }


    private ArrayList<SubscribeModelFragment> rightlist;

    private void initPagerRight() {
        RightPage.removeAllViews();
        rightlist = new ArrayList<SubscribeModelFragment>();

        int size = subscribeList.size();
        int pagerNumber = 0;
        if (size % 10 == 0) {
            pagerNumber = size / 10;
        } else {
            pagerNumber = size / 10 + 1;
        }
        if (pagerNumber >= 1) {
            initeOvalRight(pagerNumber);
        }
        for (int i = 0; i < pagerNumber; i++) {
            SubscribeModelFragment fragment = new SubscribeModelFragment(getActivity());
            List<SubscribeListsModel> subList = null;
            if (pagerNumber - 1 == i) {
                subList = subscribeList.subList(i * 8,
                        size % 8 == 0 ? (i + 1) * 8 : i * 8 + size % 8);
            } else {
                subList = subscribeList.subList(i * 8, (i + 1) * 8);
            }
            fragment.setData(subList);
            fragment.setItemClick(new SubscribeModelFragment.ItemClick() {
                @Override
                public void onItemClick(int position, SubscribeListsModel indexModel) {
                    String DeptID = subscribeList.get(position).getAreaID();
                    String aredIcon = subscribeList.get(position).getSmallPicUrl();
                    String aredNanme = subscribeList.get(position).getPoliticsAreaName();
                    String reMark = subscribeList.get(position).getReMark();
                    Intent intent = new Intent(getActivity(), InstitutionDetailActivity.class);
                    intent.putExtra("DeptID", DeptID);
                    intent.putExtra("reMark", reMark);
                    intent.putExtra("aredIcon", aredIcon);
                    intent.putExtra("aredNanme", aredNanme);
                    startActivity(intent);
                }
            });
            rightlist.add(fragment);
        }
        // fragmentAdapter = new
        // MutualFragmentAdapter(getChildFragmentManager(),
        // list);
        RightPage.setAdapter(new MyAdapter2());
        RightPage.setOffscreenPageLimit(rightlist.size());
        RightPage.setOnPageChangeListener(new OnPageChangeListener() {

            @SuppressLint("NewApi")
            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < dots.size(); i++) {
                    if (i == arg0) {
                        dots.get(i).setBackground(
                                getActivity().getResources().getDrawable(
                                        R.drawable.dot_foucus));
                    } else {
                        dots.get(i).setBackground(
                                getActivity().getResources().getDrawable(
                                        R.drawable.normal_dot));
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    /**
     * 初始化圆点-右
     */
    @SuppressLint("NewApi")
    private void initeOvalRight(int num) {
        try {
            ll_oval_right.removeAllViews();
            if (num == 1) {
                return;
            }
            dots.clear();
            for (int i = 0; i < num; i++) {
                View view = new View(getActivity());
                if (i == 0) {
                    view.setBackground(getActivity().getResources()
                            .getDrawable(R.drawable.dot_foucus));
                } else {
                    view.setBackground(getActivity().getResources()
                            .getDrawable(R.drawable.normal_dot));
                }
                LayoutParams params = new LayoutParams(DensityUtil.dip2px(
                        getActivity(), 8), DensityUtil.dip2px(getActivity(), 8));
                params.leftMargin = DensityUtil.dip2px(getActivity(), 10);
                view.setLayoutParams(params);
                dots.add(view);
                ll_oval_right.addView(view);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    class MyAdapter2 extends PagerAdapter {

        @Override
        public int getCount() {
            return rightlist.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewGroup) container).addView(rightlist.get(position).mMainView);// 添加页卡
            return rightlist.get(position).mMainView;
        }

    }
}
