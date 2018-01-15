package com.dingtai.jinriyongzhou.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
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
import com.dingtai.base.model.TopicChann;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DeviceCommonInfoByActivity;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.MyAdGallery;
import com.dingtai.base.view.MyGridView;
import com.dingtai.base.view.MyListView;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.newslib3.activity.CityHotImgActivity;
import com.dingtai.newslib3.activity.NewsListActivity;
import com.dingtai.newslib3.activity.NewsThemeList;
import com.dingtai.newslib3.activity.TestNewsDetailActivity;
import com.dingtai.newslib3.adapter.NewsAdapter;
import com.dingtai.newslib3.adapter.PersonNewsAdapter;
import com.dingtai.newslib3.service.ADHttpService;
import com.dingtai.newslib3.tool.HttpRequest;
import com.dingtai.newslib3.tool.StartActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.dingtai.newslib3.tool.HttpRequest.get_new_list;

//import com.dingtai.jinriyongzhou.adapter.NewsAdapter;

/**
 * @author Administrator
 */
@SuppressWarnings("deprecation")
public class NewsActivity extends BaseFragment {
    private boolean downup = false;// 判断上拉下拉
    private View mMainView;// 主视图
    public String state = "";// 状态
    public NewsAdapter adapter;// 新闻数据
    private TextView ad_title;
    private RadioGroup rad;
    private List<NewsListModel> listdate;// 列表数据
    private List<NewsListModel> picdate = new ArrayList<NewsListModel>();// 图集数据
    private List<NewsListModel> tem_listdate;// 临时数据
    public static View net_net;// 网络请求
    // 记录获取数据的总数量
    private String lanmuID;
    private boolean toNoImg = false;
    private DataBaseHelper help;
    private RuntimeExceptionDao<NewsListModel, String> new_list_mode;
    private ArrayList<String> adData;
    public boolean isFirst;// 第一次进入界面
    // 广告处理
    SharedPreferences sp;// 广告随机数处理
    private MyAdGallery adgallery; // 广告控件
    private RelativeLayout adLayout;// 广告布局
    private LinearLayout ovalLayout; // 圆点容器
    private String[] adImageURL;// 广告图片url
    private String[] adTitle;// 广告标题字符串
    private TextView txtADTitle;// 广告标题控件
    String LinkTo = "";
    String LinkUrl = "";
    String ChID = "";
    private RelativeLayout news_no_data;
    private AnimationDrawable animationDrawable;
    private boolean isToNoimg;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private ScrollView mScrollView;
    private MyListView xlvIndexList;
    private ArrayList<NewsListModel> temp_list1;
    private boolean datatrue = false;// 是否有广告数据
    private boolean istorefresh = false;// 判断是否需要刷新
    SharedPreferences spf;// 定时刷新
    private boolean isInite;
    private boolean isChild;
    private String parentId;

    private boolean isRefresh = true;

    private List<TopicChann> topList = new ArrayList<TopicChann>();
    private PersonNewsAdapter personAdapter;
    private MyGridView personGridView;
    private LinearLayout person_zhuanti;

    private ImageView iv_channelLogo;

    private RelativeLayout city_rel;
    private TextView point_city;

    public void setLanmuID(String ID) {
        this.lanmuID = ID;
    }

    public void setIsChild(boolean isChild) {
        this.isChild = isChild;
    }

    public void setParentID(String parentId) {
        this.parentId = parentId;
    }

    public void setIsInite(boolean isInite) {
        this.isInite = isInite;
    }

    private boolean isPause = false;
    // 接收信息
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            downup = false;

            switch (msg.what) {
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
                            ChID = adCacheData.get(curIndex).getChID();
                            getADClickResult(adCacheData.get(curIndex).getID(),
                                    adCacheData.get(curIndex).getADName());

                        }
                    });
                    break;
                case 55555:
                    if (listdate.size() == 0) {
                        mPullRefreshScrollView.setRefreshing();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    isInite = true;
                    break;
                case 66666:
                    getDate();
                    getADViewFlag();
                    break;
                case 844:
                    adapter.notifyDataSetChanged();
                    break;
                case ADAPI.AD_CLICK_COUNT_API:
                    try {
                        if (LinkTo.equals("1")) {// 1、详情2、 列表
                            String[] LinkUrls = LinkUrl.split(",");
                            String strTempID = LinkUrls[0];
                            String strTempGUID = LinkUrls[1];

                            Intent intent = new Intent(getActivity(),
                                    TestNewsDetailActivity.class);
                            intent.putExtra("ID", strTempGUID);
                            intent.putExtra("ResourceType", strTempID);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(),
                                    NewsListActivity.class);
                            intent.putExtra("lanmuChID", ChID);
                            intent.putExtra("ChannelName", "新闻列表");
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        // Toast.makeText(getActivity(), "LinkTo -->" + LinkTo +
                        // "  LinkUrl -->" + LinkUrl, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ADAPI.AD_GET_ERROR:
                    // Toast.makeText(getActivity(), msg.obj.toString(),
                    // Toast.LENGTH_SHORT).show();
                    // adLayout.setVisibility(RelativeLayout.GONE);
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
                case ADAPI.AD_LIST_API: // TODO 广告数据获取
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
                            tempADCacheData = (ArrayList<ADModel>) adList
                                    .queryBuilder().where().eq("ChID", lanmuID)
                                    .and().eq("ADTypeID", "1").query();

                            if (tempADCacheData.size() > 0) {
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
                case 6666:
                    istorefresh = true;
                    break;
                case 1111:
                    mPullRefreshScrollView.onRefreshComplete();
                    tem_listdate.clear();
                    ArrayList<NewsListModel> temp_list = (ArrayList<NewsListModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    tem_listdate.addAll(temp_list);
                    if (state.equals("up")) { // 上拉
                        if (tem_listdate.size() > 0) {
                            listdate.addAll(tem_listdate);
                            adapter.notifyDataSetChanged();
                            if (!TextUtils.isEmpty(listdate.get(0).getChannelLogo())) {
                                ImageLoader.getInstance().displayImage(listdate.get(0).getChannelLogo(), iv_channelLogo);
                                iv_channelLogo.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(getActivity().getParent(), "暂无更多数据", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        if (tem_listdate.size() > 0 && listdate != null) {
                            // if
                            // (!tem_listdate.get(0).getID().equals(listdate.get(0).getID()))
                            // {
                            listdate.clear();
                            // listdate.addAll(tem_listdate);
                            // adapter.notifyDataSetChanged();
                            // }
                            // } else if (listdate.size() == 0) {
                            listdate.addAll(tem_listdate);
                            adapter.notifyDataSetChanged();
                            if (!TextUtils.isEmpty(listdate.get(0).getChannelLogo())) {
                                ImageLoader.getInstance().displayImage(listdate.get(0).getChannelLogo(), iv_channelLogo);
                                iv_channelLogo.setVisibility(View.VISIBLE);
                            }
                            topList.clear();
                            topList.addAll(tem_listdate.get(0).getTopicChann());
                            personAdapter.notifyDataSetChanged();
                            if (topList.size() > 0)
                                person_zhuanti.setVisibility(View.GONE);
                            else
                                person_zhuanti.setVisibility(View.GONE);
                        }
                    }
                    break;
                case 10:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (listdate != null)
                        listdate.clear();
                    adapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessage(222);
                    break;
                case 0:
                    mPullRefreshScrollView.onRefreshComplete();
                    // temp_list1 = new ArrayList<NewsListModel>();
                    // if (msg != null && msg.getData() != null &&
                    // msg.getData().getParcelableArrayList("list") != null &&
                    // msg.getData().getParcelableArrayList("list").get(0) != null)
                    // {
                    // temp_list1 = (ArrayList<NewsListModel>)
                    // msg.getData().getParcelableArrayList("list").get(0);
                    // }
                    //
                    // if (temp_list1 != null && temp_list1.size() == 0 &&
                    // !state.equals("up")) {
                    // handler.sendEmptyMessage(222);
                    // } else {
                    // if (reload != null) {
                    // news_no_data.removeView(item);// 移除进度条
                    // }
                    // tem_listdate.addAll(temp_list1);
                    //
                    // if (state.equals("up")) { // 上拉
                    // // if (tem_listdate.size() > 0) {
                    // // listdate.addAll(tem_listdate);
                    // // adapter.notifyDataSetChanged();
                    // // } else {
                    // // if (getActivity() != null) {
                    // // Toast.makeText(getActivity(), "暂无更多数据", 0).show();
                    // // }
                    // // }
                    // if (getActivity() != null) {
                    // Toast.makeText(getActivity(), msg.obj.toString() + "",
                    // 0).show();
                    // }
                    // } else {
                    // if (tem_listdate.size() > 0) {
                    // countGetDataNum = tem_listdate.size();
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
                    }
                    break;
                case 555:
                    mPullRefreshScrollView.onRefreshComplete();
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 020202:
                    // initNewsAd();
                    break;
                case 999:
                    refresh(1);
                    //mPullRefreshScrollView.setRefreshing();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isRefresh) {
                //handler.sendEmptyMessageDelayed(999, 1500);
                isRefresh = false;
            }
        }

    }

    public void setCityName(String city) {
        point_city.setText(city);
    }

    @Override
    public void inite() {

        // TODO Auto-generated method stub
        // super.onCreate(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMainView = inflater.inflate(R.layout.news_actvity,
                (ViewGroup) getActivity().findViewById(R.id.viewpager), false);
        isFirst = true;
        xlvIndexList = (MyListView) mMainView.findViewById(R.id.xlvIndexList);
        personGridView = (MyGridView) mMainView.findViewById(R.id.person_news);
        person_zhuanti = (LinearLayout) mMainView.findViewById(R.id.person_zhuanti);

        iv_channelLogo = (ImageView) mMainView.findViewById(R.id.iv_channelLogo);

        city_rel = (RelativeLayout) mMainView.findViewById(R.id.city_rel);
        point_city = (TextView) mMainView.findViewById(R.id.point_city);
        String cityID = getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("areaID", "453");
        if (cityID != null && lanmuID != null && lanmuID.equals(cityID)) {
            point_city.setText(getActivity().getSharedPreferences("city", Activity.MODE_PRIVATE).getString("area", "冷水滩"));
            city_rel.setVisibility(View.VISIBLE);
        } else {
            city_rel.setVisibility(View.GONE);
        }
        city_rel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent i = new Intent();
                i.putExtra("CityID", lanmuID);
                i.setClass(getActivity(), CityHotImgActivity.class);
                startActivityForResult(i, 0);
            }
        });

        LayoutParams par = (LayoutParams) iv_channelLogo.getLayoutParams();
        par.width = DisplayMetricsTool.getWidth(getActivity());
        par.height = par.width / 3;
        iv_channelLogo.setLayoutParams(par);

        mPullRefreshScrollView = (PullToRefreshScrollView) mMainView
                .findViewById(R.id.pull_refresh_scrollview);
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(getActivity()));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）

        mPullRefreshScrollView.setHasPullUpFriction(false); // 设置没有上拉阻力
        this.mPullRefreshScrollView
                .setOnRefreshListener(this.mOnRefreshListener);
        isToNoimg = WutuSetting.getIsImg();
        net_net = mMainView.findViewById(R.id.net_net);
        tem_listdate = new ArrayList<NewsListModel>();
        listdate = new ArrayList<NewsListModel>();
        state = "";
        Bundle mBundle = getArguments();
        if (mBundle != null && mBundle.getString("lanmuid") != null) {
            lanmuID = mBundle.getString("lanmuid");

        }
        news_no_data = (RelativeLayout) mMainView
                .findViewById(R.id.news_no_data);

        help = getHelper();
        new_list_mode = help.getMode(NewsListModel.class);
        // if (!Assistant.IsContectInterNet2(getActivity())) {
        // getDateByHuanCun();
        // } else {
        // getDateByHuanCun();
        // if(listdate.size()==0){
        // getDate();
        // }
        //
        // }

        // getDateByHuanCun();
        // if (listdate.size() == 0)
        // getDate();
        adapter = new NewsAdapter(getActivity(), listdate, getFragmentManager());
        xlvIndexList.setAdapter(adapter);


        topList = new ArrayList<TopicChann>();
        personAdapter = new PersonNewsAdapter(getActivity(), topList);
        personGridView.setAdapter(personAdapter);

        personGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(NewsActivity.this.getActivity(), NewsThemeList.class);
                intent.putExtra("LanMuId", topList.get(position).getID());
                intent.putExtra("ChannelLogo", topList.get(position).getImageUrl()
                        .toString().trim());
                intent.putExtra("TitleName", topList.get(position).getChannelName()
                        .toString().trim());
                startActivity(intent);
            }
        });


        picdate = new ArrayList<NewsListModel>();

        // if (!Assistant.IsContectInterNet2(getActivity())) {
        // 从缓存中取出图集，并把它设置到新闻列表中
        try {
            RuntimeExceptionDao<NewsPhotoModel, String> new_photo = getHelper()
                    .getMode(NewsPhotoModel.class);// 新闻图片
            if (new_photo != null && new_photo.isTableExists()) {
                for (NewsListModel n : listdate) {
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
        // getADViewFlag();// 头部广告信息
        initADView();// 初始化头部广告信息

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
                StartActivity.toLanmu(getActivity(), listdate.get(position));
                // if (position <= listdate.size() && position >= 0) {
                //
                // Intent intent = null;
                // /**
                // * <option value="0">请选择</option> <option
                // * value="1">图集</option> <option value="2">专题</option>
                // * <option value="3">视频</option> <option
                // * value="4">推广</option> <option value="5">直播</option>
                // * <option value="6">本地</option> <option
                // * value="7">热点</option> <option value="8">独家</option>
                // */
                //
                // if ("2".equals(listdate.get(position).getResourceType())) {
                // // 使用活动的WebView来显示 网页新闻
                // intent = new Intent(getActivity(), ActiveWebView.class);
                // intent.putExtra("Title",
                // listdate.get(position).getTitle().toString().trim());
                // intent.putExtra("PageUrl",
                // listdate.get(position).getResourceUrl().toString().trim());
                // if
                // (listdate.get(position).getSmallPicUrl().toString().length()
                // > 1) {
                // intent.putExtra("LogoUrl",
                // listdate.get(position).getSmallPicUrl().toString());
                // } else {
                // intent.putExtra("LogoUrl", API.COMMON_URL +
                // "Skins/image/logo.png");
                // }
                // intent.putExtra("Type", "网页新闻");
                //
                // } else if
                // ("3".equals(listdate.get(position).getResourceType())) {
                // // 为1的时候表示是图集
                // intent = new Intent(getActivity(),
                // ImageDetailActivity.class);
                // intent.putExtra("ID",
                // listdate.get(position).getResourceGUID().toString().trim());
                // intent.putExtra("RPID",
                // listdate.get(position).getRPID().toString().trim());
                // intent.putExtra("newspicture", listdate.get(position));
                //
                // HashMap<String, String> news = new HashMap<String, String>();
                // news.put("ID", listdate.get(position).getID());
                // news.put("GUID", listdate.get(position).getResourceGUID());
                // news.put("Summary", listdate.get(position).getSummary());
                // news.put("Title", listdate.get(position).getTitle());
                // news.put("updatetime",
                // listdate.get(position).getUpdateTime());
                // news.put("SourceForm",
                // listdate.get(position).getSourceForm());
                // news.put("SmallPicUrl",
                // listdate.get(position).getSmallPicUrl());
                // news.put("CONTENT", "");
                // news.put("CREATEDATE",
                // listdate.get(position).getCreateTime());
                // news.put("CommentNum",
                // listdate.get(position).getCommentNum());
                // news.put("RPID", listdate.get(position).getRPID());
                // news.put("ResourceType",
                // listdate.get(position).getResourceType());
                //
                // NewsListModel n = new NewsListModel();
                // n.setResourceGUID(listdate.get(position).getResourceGUID());
                // n.setCommentNum(listdate.get(position).getCommentNum());
                // n.setTitle(listdate.get(position).getTitle());
                // n.setSourceForm(listdate.get(position).getSourceForm());
                // n.setCreateTime(listdate.get(position).getCreateTime());
                // intent.putExtra("newspicture", n);
                // intent.putExtra("newdetail", news);
                // intent.putExtra("type", "新闻详情图");
                //
                // } else if
                // ("4".equals(listdate.get(position).getResourceType())) {
                // intent = new Intent(getActivity(), NewsTheme.class);
                // intent.putExtra("LanMuId",
                // listdate.get(position).getChID().toString().trim());
                // intent.putExtra("ChannelLogo",
                // listdate.get(position).getChannelLogo().toString().trim());
                // } else if
                // ("5".equals(listdate.get(position).getResourceType())) {
                // intent = new Intent(getActivity(), ActivityZhibo.class);
                // } else {
                // // 跳转到详情页
                // intent = new Intent(getActivity(),
                // TestNewsDetailActivity.class);
                // intent.putExtra("ID",
                // listdate.get(position).getResourceGUID().toString().trim());
                // intent.putExtra("ResourceType",
                // listdate.get(position).getResourceType().toString().trim());
                // }
                // startActivity(intent);
                // }
            }
        });
    }

    /**
     * TODO 初始化广告控件
     */
    public void initADView() {
        adLayout = (RelativeLayout) mMainView.findViewById(R.id.ad_news);
        adgallery = (MyAdGallery) mMainView.findViewById(R.id.adgallery); // 获取Gallery组件
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 5 / 11 / 6));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayMetricsTool.getWidth(this.getActivity()), (int) (DisplayMetricsTool.getWidth(this.getActivity()) * 5 / 11));
        adgallery.setLayoutParams(params);
        ovalLayout = (LinearLayout) mMainView.findViewById(R.id.ovalLayout);//
        // 获取圆点容器

        txtADTitle = (TextView) mMainView.findViewById(R.id.adtitle);
        adLayout.setVisibility(View.GONE);
    }

    public void initData() {
        if (!isInite) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDateByHuanCun();
                    bindADCacheData();
                }
            }, 500);
        }
    }

    public void refresh(int type) {
        try {
            if (spf == null) {
                Activity activity = getActivity();
                activity.getSharedPreferences("refresh_time",
                        Context.MODE_PRIVATE);
            }
            if (spf == null) {
                return;
            }
            try {
                long time = spf.getLong(lanmuID, 0);
                long now = System.currentTimeMillis();
                if (type == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPullRefreshScrollView.setRefreshing();
                        }
                    }, 500);
                } else {
                    if ((now - time) / (1000 * 60) > 10) {
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

    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午5:24:25 描 述：头部广告判断
     */
    public void getADViewFlag() {
        String URL = null;
        // http://standard.d5mt.com.cn/interface/CompareAPI.ashx?action=CompareData&StID=1&ADType=3&ChID=1&sign=1
        URL = API.COMMON_URL + "interface/CompareAPI.ashx?action=CompareData";
        String StID = API.STID;
        String ChID = lanmuID;
        String ADType = "1";
        String sign = API.sign;
        HttpRequest.get_ad_compare(getActivity(), URL, StID, ChID, ADType, sign,
                new Messenger(NewsActivity.this.handler));
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-23 下午3:06:46 描 述：获取广告数据
     */
    public void getDataByADType() {
        String URL = null;
        // http://standard.d5mt.com.cn/Interface/ADsAPI.ashx?action=ListAd&stID=1&Chid=21&ADtype=2&ADFor=1
        URL = API.COMMON_URL + "Interface/ADsAPI.ashx?action=ListAd";
        String StID = API.STID;
        String Chid = lanmuID;
        String ADType = "2";
        String sign = API.sign;
        String ADFor = "1";
        String IsIndexAD = "False";
        get_ad_list(getActivity(), URL, StID, Chid, ADType, ADFor, IsIndexAD,
                sign, new Messenger(NewsActivity.this.handler));
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-23 下午3:12:21 描 述：获取广告数据
     *
     * @param paramContext
     * @param url
     * @param ADtype         广告类别
     * @param ADFor          广告所属
     * @param Chid           栏目ID
     * @param sign
     * @param StID
     * @param paramMessenger
     */
    public void get_ad_list(Context paramContext, String url, String StID,
                            String Chid, String ADtype, String ADFor, String IsIndexAD,
                            String sign, Messenger paramMessenger) {

        // http://192.168.1.12:809/Interface/ADsAPI.ashx?action=ListAd&stID=1&Chid=1&ADtype=1&ADFor=1

        Intent intent = new Intent(paramContext, ADHttpService.class);
        intent.putExtra("api", ADAPI.AD_LIST_API);
        intent.putExtra(ADAPI.AD_LIST_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("ADType", ADtype);
        intent.putExtra("ADFor", ADFor);
        intent.putExtra("Chid", Chid);
        intent.putExtra("IsIndexAD", IsIndexAD);
        intent.putExtra("sign", sign);
        intent.putExtra("StID", StID);
        paramContext.startService(intent);
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
            tempADCacheData = (ArrayList<ADModel>) adList.queryBuilder()
                    .where().eq("ChID", lanmuID).and().eq("ADTypeID", "1")
                    .query();

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
                datatrue = false;
            }
        } catch (Exception e) {
            Log.d("xf", e.toString());
        }
    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {

            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onRefresh();
                }
            }, 2000);


        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
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
            }, 2000);

        }
    };

    private void onRefresh() {
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

    private ArrayList<ADModel> adCacheData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("huahua", "fragment1-->onCreateView()");

        // ViewGroup p = (ViewGroup) mMainView.getParent();
        // if (p != null) {
        // p.removeAllViewsInLayout();
        // Log.v("huahua", "fragment1-->移除已存在的View");
        // }
        if (mMainView == null) {
            isToNoimg = WutuSetting.getIsImg();
            inite();
            Bundle bundle = getArguments();
            if (bundle != null)
                if (bundle.getBoolean("isInite", false)) {
                    isInite = false;
                    initData();
                }
        }
        return mMainView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainView = null;
        adapter = null;
        listdate = null;
        System.gc();
        Log.v("huahua", "fragment1-->onDestroy()");
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        // if (isToNoimg != WutuSetting.getIsImg()) {
        // // adapter.notifyDataSetChanged();
        // isToNoimg = WutuSetting.getIsImg();
        // adapter.notifyDataSetChanged();
        // }
        if (adapter != null) {
            adapter.setUser(Assistant.getUserInfoByOrm(getActivity()));
        }
        if (spf == null) {
            spf = getActivity().getSharedPreferences("refresh_time",
                    Context.MODE_PRIVATE);
        }
        if (isToNoimg != WutuSetting.getIsImg()) {
            // adapter.notifyDataSetChanged();
            toNoImg = true;
            adapter.notifyDataSetChanged();
            // adapter.notifyDataSetChanged();
            if (adLayout.getVisibility() == View.VISIBLE) {
                adLayout.setVisibility(View.GONE);
            } else if (datatrue) {
                adLayout.setVisibility(View.VISIBLE);
            }
            isToNoimg = WutuSetting.getIsImg();
        }
        if (!isPause) {
            handler.sendEmptyMessageDelayed(999, 1500);
        }

        Log.v("huahua", "fragment1-->onResume()");
    }

    @Override
    public void onStop() {

        Log.v("huahua", "fragment1-->onStop()");

        mPullRefreshScrollView.onRefreshComplete();

        handler.removeMessages(666666);
        handler.removeMessages(12345);
        handler.removeMessages(55555);
        handler.removeMessages(66666);
        handler.removeMessages(844);
        handler.removeMessages(ADAPI.AD_CLICK_COUNT_API);
        handler.removeMessages(ADAPI.AD_GET_ERROR);
        handler.removeMessages(ADAPI.AD_COMPARE_API);
        handler.removeMessages(ADAPI.AD_LIST_API);
        handler.removeMessages(6666);
        handler.removeMessages(1111);
        handler.removeMessages(10);
        handler.removeMessages(0);
        handler.removeMessages(222);
        handler.removeMessages(555);
        handler.removeMessages(2333);
        handler.removeMessages(999);
        handler.removeMessages(020202);
        handler.removeMessages(2222);

        super.onStop();

    }


    public void moveTop() {

        mScrollView.scrollTo(0, 0);
        mPullRefreshScrollView.setRefreshing();

    }

    /**
     * 获取列表的数据
     */
    public void getDate() {
        spf.edit().putLong(lanmuID, System.currentTimeMillis()).commit();
        tem_listdate.clear();
        if (isChild) {
            if (state.equals("up")) {
                // http://115.28.191.195:8010//interface/NewsChildAPI.ashx?action=
                // GetNewsChildUpList&top=10&dtop=10&chid=11
                String url = API.COMMON_URL
                        + "interface/NewsChildAPI.ashx?action=GetNewsChildUpList";
                String top = "10";
                String dtop = listdate.size() + "";
                String chid = lanmuID;
                String sign = API.sign;
                get_new_list(getActivity(), url, top, dtop, chid, sign, "up",
                        new Messenger(NewsActivity.this.handler), parentId);

            } else {
                // http://main.d5mt.com.cn/interface/NewsChildAPI.ashx?action=GetNewsChildDownList&top=8&chid=277&sign=1
                // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action=GetNewsChildDownList&top=10&chid=11
                String url = API.COMMON_URL
                        + "interface/NewsChildAPI.ashx?action=GetNewsChildDownList";
                String num = "8";
                String chid = lanmuID;
                String sign = API.sign;
                get_new_list(getActivity(), url, num, chid, sign, "", "",
                        new Messenger(NewsActivity.this.handler), parentId);
            }
        } else {
            if (state.equals("up")) {
                // http://115.28.191.195:8010//interface/NewsChildAPI.ashx?action=
                // GetNewsChildUpList&top=10&dtop=10&chid=11
                String url = API.COMMON_URL
                        + "interface/NewsChildAPI.ashx?action=GetNewsChildUpList";
                String top = "8";
                String dtop = listdate.size() + "";
                String chid = lanmuID;
                String sign = API.sign;
                get_new_list(getActivity(), url, top, dtop, chid, sign, "up",
                        new Messenger(NewsActivity.this.handler), null);
            } else {
                // http://main.d5mt.com.cn/interface/NewsChildAPI.ashx?action=GetNewsChildDownList&top=8&chid=277&sign=1
                // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action=GetNewsChildDownList&top=10&chid=11
                String url = API.COMMON_URL
                        + "interface/NewsChildAPI.ashx?action=GetNewsChildDownList";
                String num = "8";
                String chid = lanmuID;
                String sign = API.sign;
                HttpRequest.get_new_list(getActivity(), url, num, chid, sign, "", "",
                        new Messenger(NewsActivity.this.handler), null);
            }
        }

    }

    /**
     * 通过数据库得到数据
     */
    public void getDateByHuanCun() {
        try {
            if (new_list_mode.isTableExists()) {
                List<NewsListModel> queryForAll2 = new ArrayList<NewsListModel>();
                if (isChild) {
                    List<NewsListModel> queryForAll3 = new_list_mode
                            .queryForEq("ChID", lanmuID);
                    if (queryForAll2 != null && queryForAll3 != null
                            && queryForAll3.size() > 0) {
                        queryForAll2.addAll(queryForAll3);
                    }
                } else {
                    List<NewsListModel> queryForAll3 = new_list_mode
                            .queryForEq("parentID", lanmuID);
                    if (queryForAll2 != null && queryForAll3 != null
                            && queryForAll3.size() > 0) {
                        queryForAll2.addAll(queryForAll3);
                    }
                }

                List<NewsListModel> queryForAll = null;
                // Collections.sort(queryForAll2);
                if (queryForAll2 != null) {
                    if (queryForAll2.size() > 15) {
                        queryForAll = queryForAll2.subList(0, 15);
                    } else {
                        queryForAll = queryForAll2.subList(0,
                                queryForAll2.size());
                    }
                }
                // List<NewsListModel> queryForAll =
                // get_news_list(getActivity(),
                // lanmuID, 0);
                listdate.clear();
                if (queryForAll != null && queryForAll.size() > 0) {
                    listdate.addAll(queryForAll);
                    handler.sendEmptyMessage(55555);
                } else {
                    handler.sendEmptyMessageDelayed(55555, 200);
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 从缓存中取出某一段数据
     *
     * @param context 上下文
     * @param s       匹配字段
     * @param i       数量
     * @return
     */
    public List<NewsListModel> get_news_list(Context context, String s, int i) {
        List<NewsListModel> list = null;
        // if (!flag)
        if (s != null && !"".equals(s)) {

            list = new_list_mode.queryForEq("ChID", s);
            // Collections.sort(list);
        }
        Object obj;
        if (list != null && list.size() > 10 * i) {
            int j = 10 * i;
            Intent intent;
            int k;
            if (10 * (i + 1) > list.size()) {
                k = list.size();
            } else {
                k = 10 * (i + 1);
            }
            obj = list.subList(j, k);
        } else {
            obj = new ArrayList<NewsListModel>();
        }

        return ((List<NewsListModel>) (obj));
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
                    sign, new Messenger(NewsActivity.this.handler));
        } catch (Exception e) {

        }
    }

}
