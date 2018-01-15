package com.dingtai.jinriyongzhou.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.ADAPI;
import com.dingtai.base.api.API;
import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.model.ADModel;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.model.NewsPhotoModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.receiver.NetstateReceiver;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.utils.DensityUtil;
import com.dingtai.base.utils.DeviceCommonInfoByActivity;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.MyAdGallery;
import com.dingtai.base.view.MyListView;
import com.dingtai.base.view.VerticalScrollTextView;
import com.dingtai.dtsetting.activity.SettingActivityNew;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.CJIndexNewsAdapter;
import com.dingtai.jinriyongzhou.adapter.GridviewAdapter;
import com.dingtai.jinriyongzhou.api.HomeAPI;
import com.dingtai.jinriyongzhou.model.CJIndexNewsListModel;
import com.dingtai.jinriyongzhou.model.HomeListModel;
import com.dingtai.jinriyongzhou.model.IndexAD;
import com.dingtai.jinriyongzhou.model.IndexModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.activity.CommonActivity;
import com.dingtai.newslib3.activity.ImageDetailActivity;
import com.dingtai.newslib3.activity.NewsListActivity;
import com.dingtai.newslib3.activity.NewsTheme;
import com.dingtai.newslib3.activity.NewsWebView;
import com.dingtai.newslib3.activity.TestNewsDetailActivity;
import com.dingtai.newslib3.service.ADHttpService;
import com.dingtai.newslib3.tool.HttpRequest;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class CJNewsActivity extends BaseFragment implements OnClickListener {
    private boolean downup = false;// 判断上拉下拉
    private View mMainView;// 主视图
    public String state = "";// 状态
    public CJIndexNewsAdapter adapter;// 新闻数据
    private List<CJIndexNewsListModel> listdate;// 列表数据
    private List<CJIndexNewsListModel> tem_listdate;// 临时数据
    private boolean datatrue = false;
    private String lanmuID;
    private boolean toNoImg = false;
    private DataBaseHelper help;
    private RuntimeExceptionDao<CJIndexNewsListModel, String> new_list_mode;
    private ArrayList<String> adData;
    private Boolean hasInitData = false;
    private LinearLayout ad_title_linear;
    // 广告处理
    SharedPreferences sp;// 广告随机数处理
    SharedPreferences spf;// 定时刷新
    private MyAdGallery adgallery; // 广告控件
    private RelativeLayout adLayout;// 广告布局
    private LinearLayout ovalLayout; // 圆点容器
    private String[] adImageURL;// 广告图片url
    private String[] adTitle;// 广告标题字符串
    private TextView txtADTitle;// 广告标题控件
    private String LinkTo = "";
    private String ADFor = "";
    private String LinkUrl = "";
    private String ChID = "";
    private RelativeLayout news_no_data;
    private ImageView reload;
    private View item;
    private AnimationDrawable animationDrawable;
    private boolean isToNoimg;// 是否切换无图
    private int sce = SettingActivityNew.sce;
    public PullToRefreshScrollView mPullRefreshScrollView;
    private MyListView xlvIndexList;
    private ArrayList<HomeListModel> temp_list1, temp_list;
    // 记录获取数据的总数量
    private static int countGetDataNum = 0;
    private boolean istorefresh = false;// 判断是否需要刷新
    private GridView gv_button;
    private NetstateReceiver netReceiver;

    public void setLanmuID(String ID) {
        this.lanmuID = ID;
    }

    public void scrollTop() {
        mPullRefreshScrollView.getRefreshableView().fullScroll(ScrollView.FOCUS_UP);
    }


    private List<String> mList;
    List<IndexAD> list = new ArrayList<IndexAD>();
    private List<IndexModel> indexModels = new ArrayList<IndexModel>();
    // 接收信息
    private Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            downup = false;

            switch (msg.what) {
                case 105:
                    if (indexAdDao.isTableExists()) {
                        list = indexAdDao.queryForEq("", "true");
                    }
                    if (list.size() > 0) {
                        if (mList.size() > 0) {
                            mList.clear();
                        }
                        for (int i = 0; i < list.size(); i++) {
                            mList.add(list.get(i).getADName());
                        }
                        if (mList.size() == 0) {
                            ad_text.setVisibility(View.GONE);
                        } else
                            ad_text.setVisibility(View.VISIBLE);
                        scrollTextView.setData(mList);
                    } else {
                        Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT)
                                .show();
                    }
                    break;
                case 106:
                    if (indexAdDao.isTableExists()) {
                        list = indexAdDao.queryForEq("", "false");
                    }
                    initadData(list);
                    break;
                case 1990:
//                    indexAdapter.setData(indexModels);
//                    indexAdapter.notifyDataSetChanged();
                    break;
                case 101010:
                    if ("true".equals(msg.obj)) {
                        getIndexByCache();
                    }
                    // if (indexModels.size() > 0) {
                    // indexModels.clear();
                    // }
                    // indexModels = (ArrayList<IndexModel>) msg.getData()
                    // .getParcelableArrayList("list").get(0);
                    // indexAdapter.setData(indexModels);
                    // indexAdapter.notifyDataSetChanged();
                    break;
                case 666666:
                    adLayout.setVisibility(RelativeLayout.VISIBLE);
                    adImageURL = new String[adCacheData.size()];
                    adTitle = new String[adCacheData.size()];
                    datatrue = true;
                    for (int i = 0; i < adCacheData.size(); i++) {
                        adImageURL[i] = adCacheData.get(i).getImgUrl();
                        adTitle[i] = adCacheData.get(i).getADName();
                    }
                    adgallery.start(getActivity(), adImageURL, new int[]{}, 3000,
                            ovalLayout, R.drawable.dot_focused,
                            R.drawable.dot_normal, txtADTitle, adTitle);

                    adgallery.setMyOnItemClickListener(new MyAdGallery.MyOnItemClickListener() {
                        public void onItemClick(int curIndex) {
                            LinkTo = adCacheData.get(curIndex).getLinkTo();
                            LinkUrl = adCacheData.get(curIndex).getLinkUrl();
                            ADFor = adCacheData.get(curIndex).getADFor();
                            ChID = adCacheData.get(curIndex).getChID();
                            getADClickResult(adCacheData.get(curIndex).getID(),
                                    adCacheData.get(curIndex).getADName());

                        }
                    });
                    break;
                // case 55555:
                // adapter.notifyDataSetChanged();
                // break;
                case 66666:
                    if (Assistant.IsContectInterNet2(getActivity())) {
                        getDate();
                        getADViewFlag();
                    }
                    break;
                case 6666:
                    istorefresh = true;
                    break;
                case ADAPI.AD_CLICK_COUNT_API:
                    try {

                        // ADFor 1
                        // LinkTo 1 详情
                        // LinkTo 2 列表
                        if (ADFor.equals("1")) {
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
                            } else if (LinkTo.equals("3")) {        //专题列表
                                String[] LinkUrls = LinkUrl.split(",");
                                String strTempID = LinkUrls[0];
                                Intent intent = new Intent(getActivity(),
                                        NewsTheme.class);
                                intent.putExtra("LanMuId", strTempID);
                                intent.putExtra("TitleName", "专题列表");
                                intent.putExtra("ChannelLogo", "");
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
                            }
                        }
                        // ADFor 5 图文直播
                        else if (ADFor.equals("5")) {
                            if (LinkTo.equals("1")) {        //图文直播详情
                                String[] LinkUrls = LinkUrl.split(",");
                                String strTempID = LinkUrls[0];
                                String strTempGUID = LinkUrls[1];
                                Intent intent = new Intent();
                                intent.setAction("zhibojian");
                                intent.putExtra("zhiboid", strTempID);
                                intent.putExtra("imglogin", strTempGUID);
                                startActivity(intent);
                            } else if (LinkTo.equals("2")) {    //图文直播列表
                                Intent intent = new Intent();
                                intent.setAction("zhiboList");
                                startActivity(intent);
                            }
                        }

                    } catch (Exception e) {
                        Log.v("dddd", e.toString());
                    }
                    break;
                case ADAPI.AD_GET_ERROR:
                    bindADCacheData();
                    break;
                case ADAPI.AD_COMPARE_API:
                    try {
                        adData = (ArrayList<String>) msg.getData()
                                .getParcelableArrayList("list").get(0);
                        if (!msg.obj.toString().equals("暂无更多数据")) {
                            // 开机画面请求
                            String strRandomNum = sp.getString("RANDOMNUM", "none");
                            String strChid = sp.getString("CHID", "none");
                            String tempRandomNum = adData.get(0).toString();
                            if (!tempRandomNum.equals(strRandomNum)) {
                                strRandomNum = adData.get(0).toString();
                                Editor editor = sp.edit();
                                editor.putString("RANDOMNUM", strRandomNum);
                                editor.putString("CHID", lanmuID);
                                editor.commit();
                                getDataByADType();
                            } else {
                                // 绑定本地开机数据
                                bindADCacheData();
                            }
                        }
                    } catch (Exception e) {
                    }
                    break;
                case ADAPI.AD_LIST_API:
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
                                        new int[]{}, 3000, ovalLayout,
                                        R.drawable.dot_focused,
                                        R.drawable.dot_normal, txtADTitle, adTitle);

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

                                                getADClickResult(
                                                        alADCacheData.get(curIndex)
                                                                .getID(),
                                                        alADCacheData.get(curIndex)
                                                                .getADName());

                                            }
                                        });
                            } else {
                                datatrue = false;
                                adLayout.setVisibility(View.GONE);
                            }
                        } else {
                            datatrue = false;
                            adLayout.setVisibility(RelativeLayout.GONE);
                        }
                    } catch (Exception e) {

                    }
                    break;
                case 996633:
                    break;
                case 10086:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (reload != null) {
                        news_no_data.removeView(item);// 移除进度条
                    }
                    // tem_listdate.clear();
                    temp_list = (ArrayList<HomeListModel>) msg.getData()
                            .getParcelableArrayList("list").get(0);
                    tem_listdate.addAll(temp_list.get(0).getResList());

                    if (state.equals("up")) { // 上拉
                        if (tem_listdate.size() > 0) {
                            countGetDataNum += tem_listdate.size();
                            listdate.addAll(tem_listdate);
                            for (int i = 0; i < listdate.size(); i++) {
                                Log.e("CJNewsActivity   ", "position --->" + i + "样式---->" + adapter.getItemViewType(i) + " title-->" + listdate.get(i).getTitle() + "ResourceCSS-->" + listdate.get(i).getResourceCSS());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity().getParent(), "暂无更多数据", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        initadData(temp_list.get(0).getIndexAD());
                        if (tem_listdate.size() > 0) {
                            countGetDataNum = tem_listdate.size();
                            listdate.clear();
                            listdate.addAll(tem_listdate);
                        } else if (tem_listdate.size() == 0)
                            return;
                        adapter.notifyDataSetChanged();
                        if (temp_list != null && temp_list.size() > 0) {
                            list = temp_list.get(0).getIndexRecommendAD();
                            if (mList.size() > 0) {
                                mList.clear();
                            }
                            for (int i = 0; i < list.size(); i++) {
                                mList.add(list.get(i).getADName());
                            }
                            if (mList.size() == 0) {
                                ad_text.setVisibility(View.GONE);
                            } else
                                ad_text.setVisibility(View.VISIBLE);
                            scrollTextView.setData(mList);
                        }
                    }
                    break;
                case 10:
                    mPullRefreshScrollView.onRefreshComplete();
                    listdate.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(222);
                    break;
                case 0:
                    mPullRefreshScrollView.onRefreshComplete();
                    // getDateByHuanCun();
                    // temp_list1 = new ArrayList<HomeListModel>();
                    // if (msg != null
                    // && msg.getData() != null
                    // && msg.getData().getParcelableArrayList("list") != null
                    // && msg.getData().getParcelableArrayList("list").get(0) !=
                    // null) {
                    // temp_list1 = (ArrayList<HomeListModel>) msg.getData()
                    // .getParcelableArrayList("list").get(0);
                    // }
                    //
                    // if (temp_list1 != null && temp_list1.size() == 0
                    // && !state.equals("up")) {
                    // handler.sendEmptyMessage(222);
                    // } else {
                    // if (reload != null) {
                    // news_no_data.removeView(item);// 移除进度条
                    // }
                    // if (temp_list1.size() != 0) {
                    // List<CJIndexNewsListModel> resList = temp_list1.get(0)
                    // .getResList();
                    // tem_listdate.addAll(resList);
                    // }
                    //
                    // if (state.equals("up")) { // 上拉
                    // // if (tem_listdate.size() > 0) {
                    // // listdate.addAll(tem_listdate);
                    // // adapter.notifyDataSetChanged();
                    // // } else {
                    // // if (getActivity() != null) {
                    Toast.makeText(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                    // // }
                    // // }
                    // if (getActivity() != null) {
                    // Toast.makeText(getActivity(),
                    // msg.obj.toString() + "", 0).show();
                    // }
                    // } else {
                    // if (tem_listdate.size() > 0) {
                    // listdate.clear();
                    // listdate.addAll(tem_listdate);
                    // }
                    // adapter.notifyDataSetChanged();
                    // }
                    // }
                    break;
                case 2222:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 222:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (animationDrawable != null) {
                        animationDrawable.stop();
                        // reload.setImageResource(R.drawable.dt_standard_index_news_bg);//
                        // 设置加载失败的图片
                        // item.setOnClickListener(new OnClickListener() {
                        // @Override
                        // public void onClick(View v) {
                        // Integer integer = (Integer) reload.getTag();
                        // integer = integer == null ? 0 : integer;
                        // if
                        // (reload.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.dt_standard_index_news_bg).getConstantState()))
                        // {// 这个是加载出错是的点击事件
                        // reload.setImageResource(R.drawable.progress_round2);
                        // animationDrawable = (AnimationDrawable)
                        // reload.getDrawable();
                        // animationDrawable.start();
                        // // init();
                        // // mPullRefreshScrollView.demo();
                        // mPullRefreshScrollView.setRefreshing();
                        //
                        // }
                        // }
                        // });
                    }

                    if (msg.obj != null) {
                        Toast.makeText(getActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (getActivity() != null) {
                            Toast.makeText(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 555:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                    // ll_item.setVisibility(View.GONE);
                    break;
                case 2333:
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 9999:
                    mPullRefreshScrollView.setRefreshing();
                    break;
                default:
                    break;
            }
        }
    };

    private void initadData(List<IndexAD> indexAD) {
        if (indexAD.size() == 1) {
            ad2.setVisibility(View.GONE);
            ad1.setVisibility(View.VISIBLE);
            IndexAD indexAD1 = indexAD.get(0);
            bundleData(indexAD1, iv_top, tv_top);
        } else if (indexAD.size() == 2) {
            ad2.setVisibility(View.VISIBLE);
            ad1.setVisibility(View.GONE);
            IndexAD indexAD2 = indexAD.get(0);
            bundleData(indexAD2, iv_left, tv_left);
            IndexAD indexAD3 = indexAD.get(1);
            bundleData(indexAD3, iv_right, tv_right);
        } else if (indexAD.size() == 3) {
            ad2.setVisibility(View.VISIBLE);
            ad1.setVisibility(View.VISIBLE);
            IndexAD indexAD1 = indexAD.get(0);
            bundleData(indexAD1, iv_top, tv_top);
            IndexAD indexAD2 = indexAD.get(1);
            bundleData(indexAD2, iv_left, tv_left);
            IndexAD indexAD3 = indexAD.get(2);
            bundleData(indexAD3, iv_right, tv_right);
        } else {
            ad2.setVisibility(View.GONE);
            ad1.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
    }

    private void bundleData(IndexAD indexAD3, ImageView iv, TextView tv) {
        DisplayImageOptions option = MyImageLoader.option();
        tv.setText(indexAD3.getADName());
        ImageLoader.getInstance().displayImage(indexAD3.getImgUrl(), iv,
                option);
    }

    public void init() {
        temp_list = new ArrayList<HomeListModel>();
        temp_list.add(new HomeListModel());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.news_actvity1,
                (ViewGroup) getActivity().findViewById(R.id.viewpager), false);
        mList = new ArrayList<String>();
        line = mMainView.findViewById(R.id.line);
        line.setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.ll_indexmodel).setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.view).setVisibility(View.VISIBLE);
        mMainView.findViewById(R.id.view4).setVisibility(View.VISIBLE);
        initADView();// 初始化头部广告信息
        initeADView();
        indexAdDao = getHelper().getMode(IndexAD.class);
        spf = getActivity().getSharedPreferences("refresh_time",
                Context.MODE_PRIVATE);
        xlvIndexList = (MyListView) mMainView.findViewById(R.id.xlvIndexList);
        mPullRefreshScrollView = (PullToRefreshScrollView) mMainView
                .findViewById(R.id.pull_refresh_scrollview);
        this.mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        this.mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this.getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
        this.mPullRefreshScrollView.setHasPullUpFriction(false);
        this.mPullRefreshScrollView
                .setOnRefreshListener(this.mOnRefreshListener);
        isToNoimg = WutuSetting.getIsImg();
        tem_listdate = new ArrayList<CJIndexNewsListModel>();
        listdate = new ArrayList<CJIndexNewsListModel>();
        state = "";
        // programs = new ArrayList<JingPinProgram>();
//        Bundle mBundle = getArguments();
//        lanmuID = mBundle.getString("lanmuid");
        news_no_data = (RelativeLayout) mMainView
                .findViewById(R.id.news_no_data);

        try {
            help = getHelper();
            new_list_mode = help.getMode(CJIndexNewsListModel.class);
        } catch (Exception e) {
        }
        // ll_item = (LinearLayout) mMainView.findViewById(R.id.ll_item);
        // if (!Assistant.IsContectInterNet2(getActivity())) {
        // getDateByHuanCun();
        // bindADCacheData();
        //
        // } else {
        // // getADViewFlag();// 头部广告信息
        // getDateByHuanCun();
        // if(listdate.size() ==0){
        // getDataByADType();
        // getDate();
        // }
        //
        // }

        // if (listdate != null && listdate.size() == 0 && getActivity() !=
        // null) {
        // // 在webview中加载进度条
        // item =
        // getActivity().getLayoutInflater().inflate(R.layout.onclick_reload,
        // null);
        // reload = (ImageView) item.findViewById(R.id.reload_btn);
        // android.widget.RadioGroup.LayoutParams params = new
        // android.widget.RadioGroup.LayoutParams(LayoutParams.MATCH_PARENT,
        // LayoutParams.MATCH_PARENT, Gravity.CENTER);
        // item.setLayoutParams(params);
        // reload.setImageResource(R.drawable.progress_round2);
        // animationDrawable = (AnimationDrawable) reload.getDrawable();
        // animationDrawable.start();
        // news_no_data.addView(item);
        // }

        // if (!Assistant.IsContectInterNet2(getActivity())) {
        // 从缓存中取出图集，并把它设置到新闻列表中
        try {
            RuntimeExceptionDao<NewsPhotoModel, String> new_photo = getHelper()
                    .getMode(NewsPhotoModel.class);// 新闻图片
            if (new_photo != null && new_photo.isTableExists()) {
                for (CJIndexNewsListModel n : listdate) {
                    List<NewsPhotoModel> forEq = new_photo.queryForEq("RID",
                            n.getRPID());
                    if (forEq != null && forEq.size() > 0) {
                        NewsPhotoModel[] ps = new NewsPhotoModel[forEq.size()];
                        for (int i = 0; i < forEq.size(); i++) {
                            ps[i] = forEq.get(i);
                        }
                        n.setPhotos(ps);
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
        }

        // }

        adapter = new CJIndexNewsAdapter(getActivity(), listdate,
                getFragmentManager());

        // getADData();

        // 随机数判断
        try {

            sp = getActivity().getSharedPreferences("SPNews", 0);
            if (sp.getString("RANDOMNUM", "").length() < 1) {
                Editor editor = sp.edit();
                editor.putString("CHID", lanmuID);
                editor.putString("RANDOMNUM", "-1");
                editor.commit();
            }
        } catch (Exception e) {

        }

        xlvIndexList.setAdapter(adapter);
        // 新闻列表的点击事件
        xlvIndexList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // if (adData != null && adData.size() > 0 && adLayout != null
                // && adLayout.getVisibility() == RelativeLayout.VISIBLE) {
                // position = position - 1;
                // }
                if (position <= listdate.size() && position >= 0) {
                    Intent intent;
                    /**
                     *
                     *
                     * <option value="0">请选择</option> <option
                     * value="1">图集</option> <option value="2">专题</option>
                     * <option value="3">视频</option> <option
                     * value="4">推广</option> <option value="5">直播</option>
                     * <option value="6">本地</option> <option
                     * value="7">热点</option> <option value="8">独家</option>
                     *
                     */
                    String type = listdate.get(position).getResourceType();
                    if ("2".equals(type)) {
                        // 使用活动的WebView来显示 网页新闻
                        intent = new Intent(getActivity(), NewsWebView.class);
                        intent.putExtra("Title", listdate.get(position)
                                .getTitle().toString().trim());
                        intent.putExtra("PageUrl", listdate.get(position)
                                .getResourceUrl().toString().trim());
                        if (listdate.get(position).getSmallPicUrl().toString()
                                .length() > 1) {
                            intent.putExtra("LogoUrl", listdate.get(position)
                                    .getSmallPicUrl().toString());
                        }
                        intent.putExtra("summary", listdate.get(position)
                                .getSummary().trim());
                        intent.putExtra("Type", "网页新闻");

                    } else {

                        if ("4".equals(type)) {
                            intent = new Intent(getActivity(), NewsTheme.class);
                            intent.putExtra("TitleName", listdate.get(position)
                                    .getChannelName().toString().trim());
                            intent.putExtra("LanMuId", listdate.get(position)
                                    .getChID().toString().trim());
                            intent.putExtra("ChannelLogo",
                                    listdate.get(position).getChannelLogo()
                                            .toString().trim());

                        } else if ("3".equals(type)) {
                            // 为1的时候表示是图集
                            intent = new Intent(getActivity(),
                                    ImageDetailActivity.class);
                            intent.putExtra("ID", listdate.get(position)
                                    .getResourceGUID().toString().trim());
                            intent.putExtra("RPID", listdate.get(position)
                                    .getRPID().toString().trim());
                            intent.putExtra("newspicture",
                                    listdate.get(position));

                            HashMap<String, String> news = new HashMap<String, String>();
                            news.put("ID", listdate.get(position).getID());
                            news.put("GUID", listdate.get(position)
                                    .getResourceGUID());
                            news.put("Summary", listdate.get(position)
                                    .getSummary());
                            news.put("Title", listdate.get(position).getTitle());
                            news.put("updatetime", listdate.get(position)
                                    .getUpdateTime());
                            news.put("SourceForm", listdate.get(position)
                                    .getSourceForm());
                            news.put("SmallPicUrl", listdate.get(position)
                                    .getSmallPicUrl());
                            news.put("CONTENT", "");
                            news.put("CREATEDATE", listdate.get(position)
                                    .getCreateTime());
                            news.put("CommentNum", listdate.get(position)
                                    .getCommentNum());
                            news.put("RPID", listdate.get(position).getRPID());
                            news.put("ResourceType", listdate.get(position)
                                    .getResourceType());

                            NewsListModel n = new NewsListModel();
                            n.setResourceGUID(listdate.get(position)
                                    .getResourceGUID());
                            n.setCommentNum(listdate.get(position)
                                    .getCommentNum());
                            n.setTitle(listdate.get(position).getTitle());
                            n.setSourceForm(listdate.get(position)
                                    .getSourceForm());
                            n.setCreateTime(listdate.get(position)
                                    .getCreateTime());
                            intent.putExtra("newspicture", n);
                            intent.putExtra("newdetail", news);
                            intent.putExtra("type", "新闻详情图");

                        } else if ("5".equals(type)) {
//							ActivityZhiboList.ZhiboID = listdate.get(position)
//									.getEventID();
                            intent = new Intent();
                            intent.setAction("zhibojian");
                            intent.putExtra("zhiboid", listdate.get(position).getEventID());
                            intent.putExtra("imglogin", listdate.get(position)
                                    .getEventContentLogo());
                        } else {
                            // 跳转到详情页
                            intent = new Intent(getActivity(),
                                    TestNewsDetailActivity.class);
                            intent.putExtra("ID", listdate.get(position)
                                    .getResourceGUID().toString().trim());
                            intent.putExtra("ResourceType",
                                    listdate.get(position).getResourceType()
                                            .toString().trim());

                        }

                    }

                    startActivity(intent);
                }
            }
        });
        handler.sendEmptyMessageDelayed(9999, 3000);
    }


    private void initeADView() {
        ad1 = mMainView.findViewById(R.id.ll_add1);
        ad2 = mMainView.findViewById(R.id.ll_ad2);
        tv_top = (TextView) mMainView.findViewById(R.id.tv_top);
        tv_left = (TextView) mMainView.findViewById(R.id.tv_left);
        tv_right = (TextView) mMainView.findViewById(R.id.tv_right);
        iv_left = (ImageView) mMainView.findViewById(R.id.iv_left);
        iv_right = (ImageView) mMainView.findViewById(R.id.iv_right);
        iv_top = (ImageView) mMainView.findViewById(R.id.iv_top);
        ad_text = (LinearLayout) mMainView.findViewById(R.id.ad_text);
        iv_top.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        iv_right.setOnClickListener(this);
        scrollTextView = (VerticalScrollTextView) mMainView
                .findViewById(R.id.scroll_text);
        scrollTextView.setTextSize(16);
        dao = getHelper().getMode(IndexModel.class);
        scrollTextView.setTextOnItemClick(new VerticalScrollTextView.OnTextItemClickListener() {
            @Override
            public void onItemClick(int position) {
                List<IndexAD> indexRecommendAD = temp_list.get(0)
                        .getIndexRecommendAD();
                IndexAD indexAD = indexRecommendAD.get(position);
                String LinkTo = indexAD.getLinkTo();
                String LinkUrl = indexAD.getLinkUrl();
                String chid = indexAD.getChID();
                String ADFor = indexAD.getADFor();
                clickAd(chid, LinkTo, LinkUrl, ADFor);
            }
        });
    }

    private void clickAd(String chid, String LinkTo, String LinkUrl, String ADFor) {
        try {

            // ADFor 1
            // LinkTo 1 详情
            // LinkTo 2 列表
            if (ADFor.equals("1")) {
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
                } else if (LinkTo.equals("3")) {        //专题列表
                    String[] LinkUrls = LinkUrl.split(",");
                    String strTempID = LinkUrls[0];
                    Intent intent = new Intent(getActivity(),
                            NewsTheme.class);
                    intent.putExtra("LanMuId", strTempID);
                    intent.putExtra("TitleName", "专题列表");
                    intent.putExtra("ChannelLogo", "");
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
                }
            }// ADFor 5 图文直播
            else if (ADFor.equals("5")) {
                if (LinkTo.equals("1")) {        //图文直播详情
                    String[] LinkUrls = LinkUrl.split(",");
                    String strTempID = LinkUrls[0];
                    String strTempGUID = LinkUrls[1];
                    Intent intent = new Intent();
                    intent.setAction("zhibojian");
                    intent.putExtra("zhiboid", strTempID);
                    intent.putExtra("imglogin", strTempGUID);
                    startActivity(intent);
                } else if (LinkTo.equals("2")) {    //图文直播列表
                    Intent intent = new Intent();
                    intent.setAction("zhiboList");
                    startActivity(intent);
                }
            }

        } catch (Exception e) {
        }
    }

    private void getIndexByCache() {
        if (dao.isTableExists()) {
            indexModels = dao.queryForEq("IsDel", "True");
            if (indexModels.size() > 0) {
                handler.sendEmptyMessage(1990);
            } else {
                getDataIndex();
            }
        } else {
            getDataIndex();
        }
    }

    public void refresh(int type) {
        try {
            if (spf == null) {
                spf = getActivity().getSharedPreferences("refresh_time",
                        Context.MODE_PRIVATE);
            }
            try {
                int time = spf.getInt(lanmuID, 0);
                int now = DateTool.getMinute();
                if (type == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshScrollView.setRefreshing();
                        }
                    }, 500);
                } else {
                    if (Math.abs(now - time) > 10) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullRefreshScrollView.setRefreshing();
                            }
                        }, 500);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    public DataBaseHelper getHelper() {
        if (this.dataHelper == null)
            this.dataHelper = ((DataBaseHelper) OpenHelperManager.getHelper(
                    getActivity(), DataBaseHelper.class));
        return this.dataHelper;
    }

    /**
     * TODO 初始化广告控件
     */
    public void initADView() {
        adLayout = (RelativeLayout) mMainView.findViewById(R.id.ad_news);
        adgallery = (MyAdGallery) mMainView.findViewById(R.id.adgallery); // 获取Gallery组件
        ad_title_linear = (LinearLayout) mMainView.findViewById(R.id.ad_title_linear);
        ovalLayout = (LinearLayout) mMainView.findViewById(R.id.ovalLayout);//
        // 获取圆点容器
        txtADTitle = (TextView) mMainView.findViewById(R.id.adtitle);
//        adLayout.setVisibility(View.GONE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 5 / 11 / 6));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()) - DensityUtil.px2dip(getActivity(), 20), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 5 / 11));
        adgallery.setLayoutParams(params);
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.adgallery);
        ad_title_linear.setLayoutParams(layoutParams);
        ovalLayout = (LinearLayout) mMainView.findViewById(R.id.ovalLayout);//
        // 获取圆点容器
        txtADTitle = (TextView) mMainView.findViewById(R.id.adtitle);
        adLayout.setVisibility(View.GONE);
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午5:24:25 描 述：头部广告判断
     */
    public void getADViewFlag() {
        String URL = null;
        // http://standard.d5mt.com.cn/interface/CompareAPI.ashx?action=CompareData&StID=1&ADType=3&ChID=1&sign=1
        URL = API.COMMON_URL + "interface/CompareAPI.ashx?action=CompareData";
        String StID = API.STID;
        String ChID = lanmuID;
        String ADType = "4";
        String sign = API.sign;
        get_ad_compare(getActivity(), URL, StID, ChID, ADType, sign,
                new Messenger(CJNewsActivity.this.handler));
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-26 下午2:04:23 描 述：广告数据对比
     *
     * @param paramContext
     * @param url
     * @param StID
     * @param ChID
     * @param
     * @param sign
     * @param paramMessenger
     */
    public void get_ad_compare(Context paramContext, String url, String StID,
                               String ChID, String ADType, String sign, Messenger paramMessenger) {
        // http://localhost:5460/interface/CompareAPI.ashx?action=CompareData&StID=1&ADType=3&ChID=1&sign=1

        Intent intent = new Intent(paramContext, ADHttpService.class);
        intent.putExtra("api", ADAPI.AD_COMPARE_API);
        intent.putExtra(ADAPI.AD_COMPARE_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        intent.putExtra("ADType", ADType);
        intent.putExtra("ChID", ChID);
        intent.putExtra("sign", sign);
        paramContext.startService(intent);
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-23 下午3:06:46 描 述：获取广告数据
     */
    public void getDataByADType() {
        String URL = null;
        // http://standard.d5mt.com.cn/Interface/ADsAPI.ashx?action=ListAd&stID=1&Chid=21&ADtype=2&ADFor=1
        URL = API.COMMON_URL + "Interface/ADsAPI.ashx?action=ListAd";
        String StID = API.STID;
        String Chid = "0";
        String ADType = "2";
        String sign = API.sign;
        String ADFor = "4";
        String IsIndexAD = "False";
        HttpRequest.get_ad_list(getActivity(), URL, StID, Chid, ADType, ADFor, IsIndexAD,
                sign, new Messenger(CJNewsActivity.this.handler));
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午4:24:37 TODO 描 述：读取广告缓存数据
     */
    public void bindADCacheData() {
        try {
            ArrayList<ADModel> tempADCacheData = new ArrayList<ADModel>();
            adCacheData = new ArrayList<ADModel>();
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

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            String str = DateTool.getCurrentTime();
            // String str = DateUtils.formatDateTime(getActivity(),
            // System.currentTimeMillis(), 10000);
            paramPullToRefreshBase.getLoadingLayoutProxy().setLastUpdatedLabel(
                    str);
            mPullRefreshScrollView.getLoadingLayoutProxy().setTextTypeface(
                    Typeface.SERIF);
            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始刷新");
            if (toNoImg) {
                adapter.notifyDataSetChanged();
                toNoImg = false;
            }
            if (downup == false) {

                if (Assistant.IsContectInterNet(getActivity(), false)) {
                    state = "down";
                    downup = true;
                    getDate();
                    getADViewFlag();
                    getDataIndex();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (temp_list1 != null && temp_list1.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(getActivity(),
            // System.currentTimeMillis(), 10000);
            String str = DateTool.getCurrentTime();
            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在加载");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel(
                    "上拉加载更多");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始加载");
            if (toNoImg) {
                adapter.notifyDataSetChanged();
                toNoImg = false;
            }
            if (downup == false) {
                if (Assistant.IsContectInterNet(getActivity(), false)) {
                    downup = true;
                    state = "up";
                    getDate();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (temp_list1 != null && temp_list1.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }
        }
    };
    private VerticalScrollTextView scrollTextView;
    private ArrayList<ADModel> adCacheData;
    private GridviewAdapter indexAdapter;
    private RuntimeExceptionDao<IndexModel, String> dao;
    private LinearLayout ad_text;
    private View ad1, ad2;
    private TextView tv_top, tv_left, tv_right;
    private ImageView iv_top, iv_left, iv_right;
    private RuntimeExceptionDao<IndexAD, String> indexAdDao;
    private View line;

    // private LinearLayout ll_item;
    // private int width;// 屏幕宽度

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("huahua", "fragment1-->onCreateView()");

        if (mMainView == null) {
            init();
        }
        return mMainView;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!hasInitData) {
            getCache();
        }
    }

    public void resultFromMore() {
        try {
            indexModels.clear();
            indexModels = dao.queryForEq("IsDel", "True");
            if (indexModels.size() > 0) {
                indexAdapter.setData(indexModels);
                indexAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCache() {
        getDateByHuanCun();
        getAdCache();
        getIndexByCache();
        bindADCacheData();
    }

    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mMainView.getParent()).removeView(mMainView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        index = 0;
        Log.v("huahua", "fragment1-->onDestroy()");
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("huahua", "fragment1-->onResume()");

        if (isToNoimg != WutuSetting.getIsImg()) {
            // adapter.notifyDataSetChanged();
            isToNoimg = WutuSetting.getIsImg();
            toNoImg = true;
            // adapter.notifyDataSetChanged();
            if (adLayout.getVisibility() == View.VISIBLE) {
                adLayout.setVisibility(View.GONE);
            } else if (datatrue) {
                adLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("huahua", "fragment1-->onStart()");

    }

    public void refresh() {
        if (istorefresh) {
            state = "down";
            downup = false;
            mPullRefreshScrollView.setRefreshing();
            istorefresh = false;
            Message msg = new Message();
            msg.what = 6666;
            handler.sendMessageDelayed(msg, 1000 * sce);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v("huahua", "fragment1-->onStop()");
    }

    private int index = 0;

    /**
     * TODO 获取列表的数据
     */
    public void getDate() {
        spf.edit().putInt(lanmuID, DateTool.getMinute()).commit();
        tem_listdate.clear();
        if (state.equals("up")) {
            // http://115.28.191.195:8010/interface/AppCSIndexAPI.ashx?action=UpChangjiangAPI&Num=5&dtop=1&StID=1
            // http://115.28.191.195:8010/interface/AppCSIndexAPI.ashx?action=UpChangjiangAPI&Num=5&dtop=1
            String url = "http://116.62.29.20:8077/interface/AppCSIndexAPI.ashx?action=UpChangjiangAPI";
            String top = "10";
            String dtop = listdate.size() + "";
            // String dtop=++index+"";
            get_cj_index_news_list(getActivity(), url, top, dtop, "up",
                    API.STID, new Messenger(CJNewsActivity.this.handler));
        } else {
            //
            // http://115.28.191.195:8010/interface/AppCSIndexAPI.ashx?action=GetChangjiangAPI&Num=5
            index = 0;
            String url = "http://116.62.29.20:8077/interface/AppCSIndexAPI.ashx?action=GetChangjiangAPI";
            String num = "10";
            String sign = "1";

            get_cj_index_news_list(getActivity(), url, num, "0", "", API.STID,
                    new Messenger(CJNewsActivity.this.handler));
        }
    }

    /***
     * 获得长江首页列表
     *
     * @param context
     * @param url
     * @param num
     * @param dtop
     * @param state
     * @param paramMessenger
     */
    public void get_cj_index_news_list(Context context, String url, String num,
                                       String dtop, String state, String stid, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", HomeAPI.CJ_INDEX_NEWS_LIST_API);
        localIntent.putExtra(HomeAPI.CJ_INDEX_NEWS_LIST_MESSAGE,
                paramMessenger);
        localIntent.putExtra("state", state);
        localIntent.putExtra("url", url);
        localIntent.putExtra("stid", stid);
        if ("".equals(state)) {
            localIntent.putExtra("num", num);
        } else {
            localIntent.putExtra("num", num);
            localIntent.putExtra("dtop", dtop);
        }

        context.startService(localIntent);
    }

    // http://192.168.1.12:809/Interface/IndexModule.ashx?action=GetIndexMouble&StID=1&IsDel=1
    private void getDataIndex() {
        String url = API.COMMON_URL
                + "Interface/IndexModule.ashx?action=GetIndexMouble&StID="
                + API.STID;
        requestData(getActivity(), url, new Messenger(handler), API.GET_INDEX,
                API.GET_INDEX_MESSENGER, IndexHttpService.class);
    }

    private void getAdCache() {
        if (indexAdDao.isTableExists()) {
            list = indexAdDao.queryForEq("isScrollText", "true");
            if (list.size() > 0) {
                temp_list.get(0).getIndexRecommendAD().addAll(list);
                if (mList.size() > 0) {
                    mList.clear();
                }
                for (int i = 0; i < list.size(); i++) {
                    mList.add(list.get(i).getADName());
                }
                if (mList.size() == 0) {
                    ad_text.setVisibility(View.GONE);
                } else
                    ad_text.setVisibility(View.VISIBLE);
                scrollTextView.setData(mList);
            }
            list = indexAdDao.queryForEq("isAD", "true");
            if (list.size() > 0) {
                temp_list.get(0).getIndexAD().addAll(list);
                initadData(list);
            }
        }

    }

    /**
     * 通过数据库得到数据
     */
    public void getDateByHuanCun() {
        if (new_list_mode.isTableExists()) {

            List<CJIndexNewsListModel> queryForAll2 = (List<CJIndexNewsListModel>) new_list_mode
                    .queryForAll();
            List<CJIndexNewsListModel> queryForAll = null;
            if (queryForAll2 != null) {
                if (queryForAll2.size() > 15) {
                    queryForAll = queryForAll2.subList(0, 15);
                } else {
                    queryForAll = queryForAll2.subList(0, queryForAll2.size());
                }
            }
            listdate.clear();
            if (queryForAll != null && queryForAll.size() > 0) {
                listdate.addAll(queryForAll);
                hasInitData = true;
                // handler.sendEmptyMessage(55555);
            } else {
                handler.sendEmptyMessage(66666);
            }
        }
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-23 下午3:06:46 描 述：广告数据点击统计
     */
    @SuppressLint("NewApi")
    public void getADClickResult(String ID, String ADName) {
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
                    sign, new Messenger(CJNewsActivity.this.handler));
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        List<IndexAD> indexAD = temp_list.get(0).getIndexAD();
        switch (v.getId()) {
            case R.id.iv_left:
                IndexAD indexAD2 = null;
                if (indexAD.size() == 2) {
                    indexAD2 = indexAD.get(0);
                } else if (indexAD.size() == 3) {
                    indexAD2 = indexAD.get(1);
                }
                if (indexAD2 == null) {
                    return;
                }
                clickAd(indexAD2.getChID(), indexAD2.getLinkTo(),
                        indexAD2.getLinkUrl(), indexAD2.getADFor());
                break;
            case R.id.iv_right:
                IndexAD indexAD3 = null;
                if (indexAD.size() == 2) {
                    indexAD3 = indexAD.get(1);
                } else if (indexAD.size() == 3) {
                    indexAD3 = indexAD.get(2);
                }
                if (indexAD3 == null) {
                    return;
                }
                clickAd(indexAD3.getChID(), indexAD3.getLinkTo(),
                        indexAD3.getLinkUrl(), indexAD3.getADFor());
                break;
            case R.id.iv_top:
                IndexAD indexAD4 = indexAD.get(0);
                clickAd(indexAD4.getChID(), indexAD4.getLinkTo(),
                        indexAD4.getLinkUrl(), indexAD4.getADFor());
                break;

            default:
                break;
        }
    }
}
