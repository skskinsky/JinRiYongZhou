package com.dingtai.jinriyongzhou.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.ADAPI;
import com.dingtai.base.api.API;
import com.dingtai.base.model.ADModel;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DeviceCommonInfoByActivity;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.MyAdGallery;
import com.dingtai.base.view.MyListView;
import com.dingtai.dtvoc.activity.VideoMainActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.HomeNewsAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.HomeNewsModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.activity.CommonActivity;
import com.dingtai.newslib3.activity.ImageDetailActivity;
import com.dingtai.newslib3.activity.NewTopiceActivity;
import com.dingtai.newslib3.activity.NewsListActivity;
import com.dingtai.newslib3.activity.NewsThemeList;
import com.dingtai.newslib3.activity.NewsWebView;
import com.dingtai.newslib3.activity.TestNewsDetailActivity;
import com.dingtai.newslib3.tool.HttpRequest;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description: 首页
 * autour: xf
 * date: 2017/10/20 0020 下午 4:30
 * version:
 */

public class FragmentHome extends BaseFragment {
    private View mMainView;
    private PullToRefreshScrollView mPullRefresh;
    private List<HomeNewsModel> list = null;
    private HomeNewsAdapter mAdapter = null;
    private RelativeLayout adLayout;
    private LinearLayout ad_title_linear;
    private MyAdGallery adgallery;
    private TextView txtADTitle;
    private LinearLayout ovalLayout;
    SharedPreferences sp;// 广告随机数处理
    private String lanmuID = "0";
    private String LinkTo;
    private String ADFor = "";
    private String LinkUrl;
    private String ResType = "";
    private String ResUrl = "";
    private String AdName = "";
    private String ChID;
    private ArrayList<ADModel> adCacheData;
    private ArrayList<String> adData;
    private String[] adImageURL;
    private String[] adTitle;
    private boolean isToNoimg;// 是否切换无图
    private boolean datatrue = false;
    private boolean isUp = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_home, container, false);
        inite();
        initADView();
        return mMainView;
    }

    /**
     * TODO 初始化广告控件
     */
    private void initADView() {
        adLayout = (RelativeLayout) mMainView.findViewById(R.id.ad_news);
        ad_title_linear = (LinearLayout) mMainView.findViewById(R.id.ad_title_linear);
        adgallery = (MyAdGallery) mMainView.findViewById(R.id.adgallery); // 获取Gallery组件
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 9 / 16 / 6));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 9 / 16));
        adgallery.setLayoutParams(params);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.adgallery);
        ad_title_linear.setLayoutParams(layoutParams);
        ovalLayout = (LinearLayout) mMainView.findViewById(R.id.ovalLayout);//
        // 获取圆点容器
        txtADTitle = (TextView) mMainView.findViewById(R.id.adtitle);
        adLayout.setVisibility(View.GONE);

    }

    public void refresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefresh.setRefreshing();
            }
        }, 500);
        VideoMainActivity aw;
    }

    @Override
    public void inite() {
        mPullRefresh = (PullToRefreshScrollView) mMainView.findViewById(R.id.pull_refresh);
        MyListView mListView = (MyListView) mMainView.findViewById(R.id.lv_news);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeNewsModel thisNews = list.get(position);

                String newsType = thisNews.getResourceType();
                Intent intent = null;
                try {
                    if ("2".equals(newsType)) {
                        // 使用活动的WebView来显示 网页新闻
                        intent = new Intent(getActivity(), NewsWebView.class);
                        intent.putExtra("Title", thisNews.getTitle().toString().trim());
                        intent.putExtra("PageUrl", thisNews.getResourceUrl().toString()
                                .trim());
                        intent.putExtra("GUID", thisNews.getResourceGUID());
                        if (thisNews.getSmallPicUrl().toString().length() > 1) {
                            intent.putExtra("LogoUrl", thisNews.getSmallPicUrl()
                                    .toString());
                        }
                        intent.putExtra("summary", thisNews.getSummary());
                        intent.putExtra("Type", "网页新闻");

                        intent.putExtra("isNews", true);
                        intent.putExtra("gentie", thisNews.getCommentNum());
                        intent.putExtra("date", thisNews.getCreateTime());
                        intent.putExtra("source", thisNews.getSourceForm());
                        intent.putExtra("isComment", thisNews.getIsComment());


                    } else if ("3".equals(newsType)) {
                        // 为1的时候表示是图集
                        intent = new Intent(getActivity(), ImageDetailActivity.class);
                        intent.putExtra("ID", thisNews.getResourceGUID().toString()
                                .trim());
                        intent.putExtra("RPID", thisNews.getRPID().toString().trim());
                        intent.putExtra("newspicture", thisNews);
                        HashMap<String, String> news = new HashMap<String, String>();
                        news.put("ID", thisNews.getID());
                        news.put("GUID", thisNews.getResourceGUID());
                        news.put("Summary", thisNews.getSummary());
                        news.put("Title", thisNews.getTitle());
                        news.put("updatetime", thisNews.getUpdateTime());
                        news.put("SourceForm", thisNews.getSourceForm());
                        news.put("SmallPicUrl", thisNews.getSmallPicUrl());
                        news.put("CONTENT", "");
                        news.put("CREATEDATE", thisNews.getCreateTime());
                        news.put("CommentNum", thisNews.getCommentNum());
                        news.put("RPID", thisNews.getRPID());
                        news.put("ResourceType", newsType);
                        news.put("isComment", thisNews.getIsComment());

                        NewsListModel n = new NewsListModel();
                        n.setResourceGUID(thisNews.getResourceGUID());
                        n.setCommentNum(thisNews.getCommentNum());
                        n.setTitle(thisNews.getTitle());
                        n.setSourceForm(thisNews.getSourceForm());
                        n.setCreateTime(thisNews.getCreateTime());
                        intent.putExtra("newspicture", n);
                        intent.putExtra("newdetail", news);
                        intent.putExtra("type", "新闻详情图");

                    } else if ("4".equals(newsType)) {

                        if (thisNews.getIsNewTopice().equals("True")) {
                            intent = new Intent();
                            intent.putExtra("title", thisNews.getTitle());
                            intent.putExtra("ParentID", thisNews.getChID());
                            intent.setClass(getActivity(), NewTopiceActivity.class);
                        } else {
                            intent = new Intent(getActivity(), NewsThemeList.class);
                            intent.putExtra("LanMuId", thisNews.getChID().toString().trim());
                            intent.putExtra("ChannelLogo", thisNews.getChannelLogo()
                                    .toString().trim());
                            intent.putExtra("TitleName", thisNews.getChannelName()
                                    .toString().trim());
                        }

                    } else if ("5".equals(newsType)) {
//                ActivityZhiboList.ZhiboID = thisNews.getChID();
//                intent = new Intent(context, ActivityZhibo.class);
//                intent.putExtra("imglogin", thisNews.getSmallPicUrl());
                    } else {
                        // 跳转到详情页
                        intent = new Intent(getActivity(), TestNewsDetailActivity.class);
                        intent.putExtra("ID", thisNews.getResourceGUID().toString()
                                .trim());
                        intent.putExtra("ResourceType", newsType.toString().trim());
                    }

                    startActivity(intent);

                } catch (Exception e) {
                    Log.d("xf", e.toString());
                }
            }
        });
        mPullRefresh.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        mPullRefresh.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
        mPullRefresh.setHasPullUpFriction(true); // 设置没有上拉阻力
        mPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                isUp = false;
                getData(0);
                getADViewFlag();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                isUp = true;
                getData(list.size());
            }
        });
        mAdapter = new HomeNewsAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullRefresh.setRefreshing();
            }
        }, 500);

        // 随机数判断
        try {

            sp = getActivity().getSharedPreferences("SPNews", 0);
            if (sp.getString("RANDOMNUM", "").length() < 1) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("CHID", "0");
                editor.putString("RANDOMNUM", "-1");
                editor.commit();
            }
        } catch (Exception e) {

        }
    }

    public void getData(int dtop) {
        //http://118.31.237.123:8080/interface/AppZSHHIndexAPI.ashx?action=GetIndex&StID=0&top=10
        //http://118.31.237.123:8080/interface/AppZSHHIndexAPI.ashx?action=GetShangLaIndex&StID=0&top=1&dtop=0
        String url = "";
        if (isUp) {
            url = API.COMMON_URL + "interface/AppZSHHIndexAPI.ashx?action=GetShangLaIndex&StID=0&top=10&dtop=" + dtop;
        } else {
            url = API.COMMON_URL + "interface/AppZSHHIndexAPI.ashx?action=GetIndex&StID=0&top=10";
        }
        requestData(getActivity(), url, new Messenger(handler), IndexAPI.GET_LIST_NEWS_API, IndexAPI.GET_LIST_NEWS_API_MESSENGER, IndexHttpService.class);
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-23 下午3:06:46 描 述：获取广告数据
     */
    private void getDataByADType() {
        String URL = null;
        // http://standard.d5mt.com.cn/Interface/ADsAPI.ashx?action=ListAd&stID=1&Chid=21&ADtype=2&ADFor=1
        URL = API.COMMON_URL + "Interface/ADsAPI.ashx?action=ListAd";
        String StID = API.STID;
        String Chid = lanmuID;
        String ADType = "2";
        String sign = API.sign;
        String ADFor = "4";
        String IsIndexAD = "False";
        HttpRequest.get_ad_list(getActivity(), URL, StID, Chid, ADType, ADFor, IsIndexAD,
                sign, new Messenger(handler));
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午4:24:37 TODO 描 述：读取广告缓存数据
     */
    private void bindADCacheData() {
        try {
            ArrayList<ADModel> tempADCacheData = new ArrayList<ADModel>();
            RuntimeExceptionDao<ADModel, String> adList = getHelper()
                    .getMode(ADModel.class);
            tempADCacheData = (ArrayList<ADModel>) adList.queryForEq(
                    "ADTypeID", "4");

            if (tempADCacheData.size() > 0) {
                adCacheData.clear();
                adCacheData.addAll(tempADCacheData);
                // initADView();// 初始化头部广告信息
                if (isToNoimg) {
                    if (adLayout.getVisibility() == View.GONE) {
                        handler.sendEmptyMessage(666666);
                    }
                } else {
                    datatrue = false;
                }
            } else {
                getADViewFlag();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午5:24:25 描 述：头部广告判断
     */
    private void getADViewFlag() {
        String URL = null;
        // http://standard.d5mt.com.cn/interface/CompareAPI.ashx?action=CompareData&StID=1&ADType=3&ChID=1&sign=1
        URL = API.COMMON_URL + "interface/CompareAPI.ashx?action=CompareData";
        String StID = API.STID;
        String ChID = lanmuID;
        String ADType = "4";
        String sign = API.sign;
        HttpRequest.get_ad_compare(getActivity(), URL, StID, ChID, ADType, sign,
                new Messenger(handler));
    }

    @SuppressLint("NewApi")
    private void getADClickResult(String ID, String ADName) {
        DeviceCommonInfoByActivity device = new DeviceCommonInfoByActivity();
        try {
            String URL = null;
            // http://standard.d5mt.com.cn/interface/StatisticsAPI.ashx?action=InsertADStatistics&OPType=1&OPID=1&OPName=1&UserGUID=87052e65-b939-4ffd-a785-16b94ef7c8fe&Device=1&System=1&Resolution=1&Network=1&CarrierOperator=1&Sign=1
            URL = API.COMMON_URL
                    + "interface/StatisticsAPI.ashx?action=InsertADStatistics";
            String OPType = "1";
            String OPID = ID;
            String OPName = ADName;
            String UserGUID = "";
            try {
                UserGUID = Assistant.getUserInfoByOrm(getActivity())
                        .getUserGUID();
            } catch (Exception e) {
                UserGUID = "00000000-0000-0000-0000-000000000000";
            }
            if (UserGUID.length() < 1) {
                UserGUID = "00000000-0000-0000-0000-000000000000";
            }
            // 设备型号
            String Device = device.getDevice().replaceAll(" ", "");// 小米
            // 系统版本号
            String System = device.getDeviceVERSION(); // 4.4
            // 分辨率
            String Resolution = "0"; // 720*1080
            WindowManager wm = (WindowManager) getActivity().getSystemService(
                    Context.WINDOW_SERVICE);
            Point size = new Point();
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Resolution = width + "*" + height;
            // 网络
            String Network = "0";
            ConnectivityManager cm = (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                Network = "1";
            } else {
                Network = "2";
            }
            // 获取运营商
            String CarrierOperator = "0";
            TelephonyManager tm = (TelephonyManager) getActivity()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();

            if (imsi != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
                    // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
                    CarrierOperator = "1"; // 中国移动
                } else if (imsi.startsWith("46001")) {
                    CarrierOperator = "2";// 中国联通
                } else if (imsi.startsWith("46003")) {
                    CarrierOperator = "3";// 中国电信
                }
            }

            String sign = API.sign;
            String StID = API.STID;

            HttpRequest.get_ad_click(getActivity(), URL, OPType, OPID, OPName, UserGUID,
                    Device, System, Resolution, Network, CarrierOperator, StID,
                    sign, new Messenger(handler));
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isToNoimg != WutuSetting.getIsImg()) {
            // adapter.notifyDataSetChanged();
            isToNoimg = WutuSetting.getIsImg();
            // adapter.notifyDataSetChanged();
            if (adLayout.getVisibility() == View.VISIBLE) {
                adLayout.setVisibility(View.GONE);
            } else if (datatrue) {
                adLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    // public View share;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 200:
                    mPullRefresh.onRefreshComplete();
                    List<HomeNewsModel> tempList = (List<HomeNewsModel>) msg.getData().getParcelableArrayList("list").get(0);
                    if (tempList != null && tempList.size() > 0) {
                        if (isUp) {
                            list.addAll(tempList);
                        } else {
                            list=tempList;
                        }
                        if (list != null) {
                            mAdapter.setList(list);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    break;
                case 404:
                    Toast.makeText(getActivity(), "暂无数据!", Toast.LENGTH_SHORT).show();
                    break;
                case ADAPI.AD_CLICK_COUNT_API:
                    mPullRefresh.onRefreshComplete();
                    try {
                        // ADFor 1
                        // LinkTo 1 详情
                        // LinkTo 2 列表
                        if (ADFor.equals("1")) {
                            if (LinkTo.equals("1")) {
                                if (ResType.equals("2")) {
                                    Intent intent = new Intent(getActivity(),
                                            NewsWebView.class);
                                    intent.putExtra("Title", AdName);
                                    intent.putExtra("PageUrl", ResUrl);
                                    startActivity(intent);
                                } else {
                                    String[] LinkUrls = LinkUrl.split(",");
                                    String strTempID = LinkUrls[0];
                                    String strTempGUID = LinkUrls[1];
                                    Intent intent = new Intent(getActivity(),
                                            TestNewsDetailActivity.class);
                                    intent.putExtra("ID", strTempGUID);
                                    intent.putExtra("ResourceType", strTempID);
                                    intent.putExtra("type", 1);
                                    startActivity(intent);
                                }
                            } else if (LinkTo.equals("2")) {
                                Intent intent = new Intent(getActivity(),
                                        NewsListActivity.class);
                                intent.putExtra("lanmuChID", ChID);
                                intent.putExtra("ChannelName", "新闻列表");
                                startActivity(intent);
                            } else if (LinkTo.equals("3")) {
                                Intent intent = new Intent();
                                intent.putExtra("title", AdName);
                                intent.putExtra("ParentID", ChID);
                                intent.setClass(getActivity(), NewTopiceActivity.class);
                                startActivity(intent);
                            }
                        }
                        // ADFor 2
                        // LinkTo 1 商品详情
                        // LinkTo 2 商城
                        else if (ADFor.equals("2")) {
                            if (LinkTo.equals("1")) {

                            } else if (LinkTo.equals("2")) {

                            }
                        }
                        // ADFor 3
                        // LinkTo 1 活动详情
                        // LinkTo 2 活动列表
                        else if (ADFor.equals("3")) {
                            if (LinkTo.equals("1")) {
                                Intent intent = new Intent(getActivity(),
                                        NewsWebView.class);
                                intent.putExtra("Title", "网页新闻");
                                intent.putExtra("PageUrl", LinkUrl);
                                startActivity(intent);
                            } else if (LinkTo.equals("2")) {
                                Intent intent = new Intent(getActivity(),
                                        CommonActivity.class);
                                intent.putExtra("name", "活动");
                                startActivity(intent);
                            }
                        }
                        // ADFor 4 和1一样
                        else if (ADFor.equals("4")) {
                            if (LinkTo.equals("1")) {

                                String[] LinkUrls = LinkUrl.split(",");
                                String strTempID = LinkUrls[0];
                                String strTempGUID = LinkUrls[1];
                                Intent intent = new Intent(getActivity(),
                                        TestNewsDetailActivity.class);
                                intent.putExtra("ID", strTempGUID);
                                intent.putExtra("ResourceType", strTempID);
                                intent.putExtra("type", 1);
                                startActivity(intent);
                            } else if (LinkTo.equals("2")) {
                                Intent intent = new Intent(getActivity(),
                                        NewsListActivity.class);
                                intent.putExtra("lanmuChID", ChID);
                                intent.putExtra("ChannelName", "新闻列表");
                                startActivity(intent);
                            } else if (LinkTo.equals("3")) {
                                Intent intent = new Intent();
                                intent.putExtra("title", AdName);
                                intent.putExtra("ParentID", ChID);
                                intent.setClass(getActivity(), NewTopiceActivity.class);
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                case ADAPI.AD_GET_ERROR:
                    bindADCacheData();
                    mPullRefresh.onRefreshComplete();
                    break;
                case ADAPI.AD_COMPARE_API:
                    mPullRefresh.onRefreshComplete();
                    try {
                        adData = (ArrayList<String>) msg.getData()
                                .getParcelableArrayList("list").get(0);
                        if (!msg.obj.toString().equals("暂无更多数据")) {
                            String strRandomNum = sp.getString("RANDOMNUM", "none");
                            String tempRandomNum = adData.get(0).toString();
                            if (!tempRandomNum.equals(strRandomNum)) {
                                strRandomNum = adData.get(0).toString();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("RANDOMNUM", strRandomNum);
                                editor.putString("CHID", lanmuID);
                                editor.commit();
                                getDataByADType();
                            } else {
                                // 绑定本地开机数据
                                bindADCacheData();
                            }
                        }
                    } catch (Exception e) {//news_zan_v2,news_zan_v2_1
                    }
                    break;
                case ADAPI.AD_LIST_API:
                    // if (reload != null) {
                    // reload.setVisibility(View.GONE);
                    // }
                    mPullRefresh.onRefreshComplete();
                    // TODO 广告数据获取
                    try {

                        if (!msg.obj.toString().equals("暂无更多数据")) {
                            // final ArrayList<ADModel> alADCacheData =
                            // (ArrayList<ADModel>)
                            // msg.getData().getParcelableArrayList("list").get(0);
                            ArrayList<ADModel> tempADCacheData = new ArrayList<ADModel>();
                            final ArrayList<ADModel> alADCacheData = new ArrayList<ADModel>();
                            RuntimeExceptionDao<ADModel, String> adList = getHelper()
                                    .getMode(ADModel.class);
                            if (msg.obj.toString().equals("清空")) {
                                adList.delete(adList.queryForEq("ChID", lanmuID));
                            }
                            tempADCacheData = (ArrayList<ADModel>) msg.getData()
                                    .getParcelableArrayList("list").get(0);

                            if (tempADCacheData.size() > 0) {
                                alADCacheData.clear();
                                alADCacheData.addAll(tempADCacheData);
                                if (isToNoimg)
                                    adLayout.setVisibility(RelativeLayout.VISIBLE);
                                datatrue = true;
                                adImageURL = new String[alADCacheData.size()];
                                adTitle = new String[alADCacheData.size()];

                                for (int i = 0; i < alADCacheData.size(); i++) {
                                    adImageURL[i] = alADCacheData.get(i)
                                            .getImgUrl();
                                    adTitle[i] = alADCacheData.get(i).getADName();
                                }
                                adgallery.start(getActivity(), adImageURL,
                                        new int[]{}, 2000, ovalLayout,

                                        R.drawable.normal_dot, R.drawable.dot_focused, txtADTitle, adTitle);

                                adgallery
                                        .setMyOnItemClickListener(new MyAdGallery.MyOnItemClickListener() {

                                            public void onItemClick(int curIndex) {
                                                LinkTo = alADCacheData
                                                        .get(curIndex).getLinkTo();
                                                LinkUrl = alADCacheData.get(
                                                        curIndex).getLinkUrl();
                                                ChID = alADCacheData.get(curIndex)
                                                        .getChID();
                                                ADFor = alADCacheData.get(curIndex)
                                                        .getADFor();
                                                ResType = alADCacheData.get(curIndex)
                                                        .getResType();
                                                ResUrl = alADCacheData.get(curIndex)
                                                        .getResUrl();
                                                AdName = alADCacheData.get(curIndex).getADName();
                                                getADClickResult(
                                                        alADCacheData.get(curIndex)
                                                                .getID(),
                                                        alADCacheData.get(curIndex)
                                                                .getADName());

                                            }
                                        });
                            } else {
                                datatrue = false;
                                adLayout.setVisibility(RelativeLayout.GONE);
                            }
                        } else {
                            datatrue = false;
                            adLayout.setVisibility(RelativeLayout.GONE);
                        }
                    } catch (Exception e) {

                    }
                    break;
            }
        }
    };
}
