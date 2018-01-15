package com.dingtai.jinriyongzhou.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.base.api.ADAPI;
import com.dingtai.base.api.API;
import com.dingtai.base.application.MyApplication;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.ChooseLanmu;
import com.dingtai.base.utils.DeviceCommonInfoByActivity;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.WindowsUtils;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.MyAdGallery;
import com.dingtai.base.view.MyGridView;
import com.dingtai.base.view.MyListView;
import com.dingtai.base.view.VerticalScrollTextView;
import com.dingtai.dtpolitics.activity.LeaderIndexActivity;
import com.dingtai.dtpolitics.activity.PoliticsMoreNews;
import com.dingtai.dtpolitics.activity.WenZhengDetailActivity;
import com.dingtai.dtpolitics.adapter.AreaLeaderAdapter;
import com.dingtai.dtpolitics.adapter.NewsAdapter;
import com.dingtai.dtpolitics.adapter.WenZhengAdapter;
import com.dingtai.dtpolitics.model.AreaByLeader;
import com.dingtai.dtpolitics.model.PoliticsADIndexModel;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.dtpolitics.model.WenZhengModel;
import com.dingtai.dtpolitics.service.WenZhengAPI;
import com.dingtai.dtpolitics.service.WenZhengHttpService;
import com.dingtai.jinriyongzhou.R;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 陈籽屹
 * @时间 2016年5月9日 机构首页
 */
public class InstitutionIndexActivity extends BaseFragmentActivity implements
        OnClickListener {
    private MyAdGallery adgallery; // 广告控件
    private RelativeLayout adLayout;// 广告布局
    private LinearLayout ovalLayout; // 圆点容器
    private boolean downup = false;// 判断上拉下拉
    private String state = "";// 判断上拉下拉状态
    private PullToRefreshScrollView mPullRefreshScrollView;// （再按一次退出程序）

    private String[] adImageURL;// 广告图片url
    private String[] adTitle;// 广告标题字符串
    private TextView txtADTitle;// 广告标题控件
    String LinkTo = "";// 广告链接到
    String LinkUrl = "";// 广告链接URL
    LinearLayout ad_text;
    /**
     * 滚动字幕
     */
    private VerticalScrollTextView scroll_text;
    private ViewPager mViewPager;
    RelativeLayout ad_news;
    private String ChID = "";

    TextView tv_more;

    MyListView lv_lastessay;
    MyListView lv_lastquestion;
    private WenZhengAdapter wenZhengAdapter;
    private NewsAdapter essayAdapter;
    private List<NewsListModel> politicsNewsList = new ArrayList<>();
    private List<WenZhengModel> politicsInfoList = new ArrayList<>();

    private int width;
    /**
     * 提问更多
     */
    private TextView more_ask;
    /**
     * 新闻更多
     */
    private TextView more_news;

    private MyGridView gv_button;
    /**
     * 大厅中间8个按钮适配器
     */
    private AreaLeaderAdapter halladapter;
    /**
     * 部门名字
     */
    private TextView institution_tv;
    /**
     * 部门介绍，欢迎语
     */
    private TextView introduce_tv;
    /**
     * 部门圆形图片
     */
    private CircularImage leader_icon;

    /**
     * 部门ID
     */
    private String DeptID;
    /**
     * 区域集合
     */
    private List<PoliticsAreaModel> politicsAreaModelList;
    /**
     * 部门头像
     */
    String aredIcon;
    /**
     * 部门名字
     */
    private String aredNanme;
    /**
     * 备注
     */
    private String reMark;
    private List<AreaByLeader> areaByLeader = new ArrayList<>();
    /**
     * 检测网络状态
     */
    private TextView net_net;
    /**
     * 订阅和投稿
     */
    private TextView tv_dingyue,tv_tougao;


    List<PoliticsADIndexModel> politicsADIndexList;
    RuntimeExceptionDao<PoliticsADIndexModel, String> politicsADIndexModel;
    private Handler mHandler = new Handler() {
        @SuppressWarnings({"unchecked", "rawtypes"})
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
                    Toast.makeText(InstitutionIndexActivity.this, (CharSequence) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    mPullRefreshScrollView.onRefreshComplete();
                    downup = false;
                    break;
                case 100:
                    ArrayList list = (ArrayList) msg.getData()
                            .getParcelableArrayList("list").get(0);
                    areaByLeader.clear();
                    areaByLeader = (List<AreaByLeader>) list.get(1);
                    halladapter = new AreaLeaderAdapter(
                            InstitutionIndexActivity.this.getLayoutInflater(),
                            areaByLeader);
                    gv_button.setAdapter(halladapter);
                    halladapter.notifyDataSetChanged();
                    // 最新提问
                    politicsInfoList.clear();
                    politicsInfoList = (List<WenZhengModel>) list.get(2);
                    wenZhengAdapter = new WenZhengAdapter(
                            InstitutionIndexActivity.this, politicsInfoList);
                    lv_lastquestion.setAdapter(wenZhengAdapter);
                    wenZhengAdapter.notifyDataSetChanged();
                    // 最新文章
                    politicsNewsList.clear();
                    politicsNewsList = (List<NewsListModel>) list.get(4);
                    essayAdapter = new NewsAdapter(InstitutionIndexActivity.this,
                            politicsNewsList, getSupportFragmentManager());
                    lv_lastessay.setAdapter(essayAdapter);
                    wenZhengAdapter.notifyDataSetChanged();
                    // 广告
                    politicsADIndexList = (List<PoliticsADIndexModel>) list.get(3);
                    ArrayList<String> textStr = new ArrayList<String>();
                    for (int i = 0; i < politicsADIndexList.size(); i++) {
                        textStr.add(politicsADIndexList.get(i).getADName());
                        // adImageURL[i] = politicsADIndexList.get(i).getImgUrl();
                        // adTitle[i] = politicsADIndexList.get(i).getADName();
                    }
                    scroll_text.setData(textStr);
                    scroll_text.setTextOnItemClick(new VerticalScrollTextView.OnTextItemClickListener() {

                        @Override
                        public void onItemClick(int position) {
                            PoliticsADIndexModel politicsADIndex = politicsADIndexList
                                    .get(position);
                            String LinkTo = politicsADIndex.getLinkTo();
                            String LinkUrl = politicsADIndex.getLinkUrl();
                            String chid = politicsADIndex.getChID();
                            clickAd(chid, LinkTo, LinkUrl);

                        }
                    });
                    // downup=false;
                    mPullRefreshScrollView.onRefreshComplete();
                    break;

                default:
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(com.dingtai.dtpolitics.R.layout.institutionindex_activity);
        initView();

    }

    private void initView() {
        initeTitle();
        setTitle("");

        WindowsUtils.getWindowSize(InstitutionIndexActivity.this);
        width = WindowsUtils.width;
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(com.dingtai.dtpolitics.R.id.pull_refresh_scrollview);
        if (MyApplication.RefreshVersion==2){
            mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
            mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));

            // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
            // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
            // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
            mPullRefreshScrollView.setHasPullUpFriction(true); // 设置没有上拉阻力
        }
        lv_lastessay = (MyListView) findViewById(com.dingtai.dtpolitics.R.id.lv_lastessay);//最新新闻
        lv_lastquestion = (MyListView) findViewById(com.dingtai.dtpolitics.R.id.lv_lastquestion);//最新提问
        institution_tv = (TextView) findViewById(com.dingtai.dtpolitics.R.id.institution_tv);
        introduce_tv = (TextView) findViewById(com.dingtai.dtpolitics.R.id.introduce_tv);
        leader_icon = (CircularImage) findViewById(com.dingtai.dtpolitics.R.id.leader_icon);

        net_net = (TextView) findViewById(com.dingtai.dtpolitics.R.id.net_net);
        Assistant.setNetworkState(InstitutionIndexActivity.this, net_net,
                mHandler);

        more_ask = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_more_ask);
        more_ask.setOnClickListener(this);

        more_news = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_more_news);
        more_news.setOnClickListener(this);

        tv_more = (TextView) findViewById(com.dingtai.dtpolitics.R.id.tv_more);
        ad_text = (LinearLayout) findViewById(com.dingtai.dtpolitics.R.id.ad_text);
        // ad_text.setVisibility(View.VISIBLE);

        scroll_text = (VerticalScrollTextView) findViewById(com.dingtai.dtpolitics.R.id.scroll_text);
        scroll_text.setTextSize(16);

        gv_button = (MyGridView) findViewById(com.dingtai.dtpolitics.R.id.gv_button);

        gv_button.setVisibility(View.VISIBLE);
        mPullRefreshScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        // TODO 自动生成的方法存根
                        if (Assistant
                                .IsContectInterNet2(  InstitutionIndexActivity.this)) {
                            get_InstitutionIndexDate();
                        } else {
                            Toast.makeText(  InstitutionIndexActivity.this,
                                    "请检查网络连接", Toast.LENGTH_SHORT).show();
                            mPullRefreshScrollView.onRefreshComplete();
                        }
                    }
                });
        lv_lastessay.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                ChooseLanmu.toLanmu(  InstitutionIndexActivity.this,
                        politicsNewsList.get(position));
            }
        });
        lv_lastquestion.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                Intent intent = new Intent(  InstitutionIndexActivity.this,
                        WenZhengDetailActivity.class);
                intent.putExtra("ID", politicsInfoList.get(position).getID());
                intent.putExtra("FLAG", "0");
                startActivity(intent);
            }
        });
        gv_button.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent();
                if (areaByLeader != null && areaByLeader.size() != 0) {
                    intent.putExtra("id", areaByLeader.get(position)
                            .getLeaderID());
                    intent.putExtra("aredNanme", aredNanme);
                    intent.setClass(  InstitutionIndexActivity.this,
                            LeaderIndexActivity.class);
                    startActivity(intent);
                }
                // finish();
            }
        });
        ;
        adLayout = (RelativeLayout) findViewById(com.dingtai.dtpolitics.R.id.ad_news);
        // adLayout.setVisibility(View.VISIBLE);
        adgallery = (MyAdGallery) findViewById(com.dingtai.dtpolitics.R.id.adgallery); // 获取Gallery组件
        ovalLayout = (LinearLayout) findViewById(com.dingtai.dtpolitics.R.id.ovalLayout);//
        // 获取圆点容器
        txtADTitle = (TextView) findViewById(com.dingtai.dtpolitics.R.id.adtitle);
        // adLayout.setVisibility(View.GONE);
        if (Assistant.IsContectInterNet2(  InstitutionIndexActivity.this)) {
            get_InstitutionIndexDate();
        } else {
            Toast.makeText(  InstitutionIndexActivity.this, "请检查网络连接",
                    Toast.LENGTH_SHORT).show();
        }

        institution_tv.setText(aredNanme);
        // introduce_tv.setText("欢迎关注嘉善县" + aredNanme + ",请及时浏览相关公告,欢迎提问");
        introduce_tv.setText(reMark);
        new MyImageLoader();
        ImageLoader.getInstance().displayImage(aredIcon, leader_icon,
                MyImageLoader.option());
        tv_dingyue = (TextView) findViewById(R.id.tv_dingyue);
        tv_tougao = (TextView) findViewById(R.id.tv_tougao);
        tv_dingyue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InstitutionIndexActivity.this,"正在开发中..",Toast.LENGTH_SHORT).show();
            }
        });
        tv_tougao.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InstitutionIndexActivity.this,"正在开发中..",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 机构首页数据
     */
    public void get_InstitutionIndexDate() {
        DeptID = getIntent().getStringExtra("DeptID");
        aredIcon = getIntent().getStringExtra("aredIcon");
        aredNanme = getIntent().getStringExtra("aredNanme");
        reMark = getIntent().getStringExtra("reMark");
        get_insitutionData(  InstitutionIndexActivity.this,
                WenZhengAPI.POLITICS_AREA_URL, new Messenger(mHandler), DeptID);
    }

    /**
     * 机构首页
     *
     * @param paramContext
     * @param url
     * @param messenger
     */
    public void get_insitutionData(Context paramContext, String url,
                                   Messenger messenger, String deptID) {
        Intent intent = new Intent(paramContext, WenZhengHttpService.class);
        intent.putExtra("api", WenZhengAPI.POLITICS_AREA_API);
        intent.putExtra(WenZhengAPI.POLITICS_AREA_MESSAGE, messenger);
        intent.putExtra("url", url);
        intent.putExtra("DeptID", deptID);
        paramContext.startService(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        super.onClick(v);
        int i = v.getId();
        if (i == com.dingtai.dtpolitics.R.id.tv_more_ask) {
            intent.putExtra("aredIcon", aredIcon);
            intent.putExtra("aredNanme", aredNanme);
            intent.putExtra("LanmuID", DeptID);
            intent.putExtra("QuestionOrNews", "Question");
            intent.putExtra("reMark", reMark);
            intent.setClass(InstitutionIndexActivity.this,
                    PoliticsMoreNews.class);
            startActivity(intent);

        } else if (i == com.dingtai.dtpolitics.R.id.tv_more_news) {
            intent.putExtra("reMark", reMark);
            intent.putExtra("aredIcon", aredIcon);
            intent.putExtra("aredNanme", aredNanme);
            intent.putExtra("LanmuID", DeptID);
            intent.putExtra("QuestionOrNews", "News");
            intent.setClass(InstitutionIndexActivity.this,
                    PoliticsMoreNews.class);
            startActivity(intent);

        }
    }

    @SuppressLint("NewApi")
    public void getADClickResult(String ID, String ADName) {
        DeviceCommonInfoByActivity device = new DeviceCommonInfoByActivity();
        try {

            String URL = null;
            // http://standard.d5mt.com.cn/interface/StatisticsAPI.ashx?action=InsertADStatistics&OPType=1&OPID=1&OPName=1&UserGUID=87052e65-b939-4ffd-a785-16b94ef7c8fe&Device=1&System=1&Resolution=1&Network=1&CarrierOperator=1&Sign=1
            URL = API.COMMON_URL
                    + "interface/StatisticsAPI.ashx?action=InsertADStatistics"+ "&StID=" + API.STID;
            String OPType = "1";
            String OPID = ID;
            String OPName = ADName;
            String UserGUID = "";
            try {
                UserGUID = Assistant.getUserInfoByOrm(
                        InstitutionIndexActivity.this).getUserGUID();
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
            WindowManager wm = (WindowManager) InstitutionIndexActivity.this
                    .getSystemService(Context.WINDOW_SERVICE);
            Point size = new Point();
            Display display = wm.getDefaultDisplay();
            display.getSize(size);
            int width = size.x;
            int height = size.y;
            Resolution = width + "*" + height;
            // 网络
            String Network = "0";
            ConnectivityManager cm = (ConnectivityManager) InstitutionIndexActivity.this
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                Network = "1";
            } else {
                Network = "2";
            }
            // 获取运营商
            String CarrierOperator = "0";
            TelephonyManager tm = (TelephonyManager) InstitutionIndexActivity.this
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

            get_ad_click(  InstitutionIndexActivity.this, URL, OPType, OPID,
                    OPName, UserGUID, Device, System, Resolution, Network,
                    CarrierOperator, StID, sign, new Messenger(
                            InstitutionIndexActivity.this.mHandler));
        } catch (Exception e) {

        }
    }

    /**
     * 作 者： 李亚军 时 间：2015-1-30 上午11:52:11 描 述：广告点击统计
     *
     * @param paramContext
     * @param url
     * @param OPType
     * @param OPID
     * @param OPName
     * @param UserGUID
     * @param Device
     * @param System
     * @param Resolution
     * @param Network
     * @param CarrierOperator
     * @param sign
     * @param paramMessenger
     */
    public void get_ad_click(Context paramContext, String url, String OPType,
                             String OPID, String OPName, String UserGUID, String Device,
                             String System, String Resolution, String Network,
                             String CarrierOperator, String StID, String sign,
                             Messenger paramMessenger) {
        // http://localhost:5460/interface/CompareAPI.ashx?action=CompareOpenPicData&StID=1
        try {
            Class<?> clazz = Class.forName("com.dingtai.dtnewslist.service.ADHttpService");
            Intent intent = new Intent(paramContext, clazz);
            intent.putExtra("api", ADAPI.AD_CLICK_COUNT_API);
            intent.putExtra(ADAPI.AD_CLICK_COUNT_MESSAGE, paramMessenger);
            intent.putExtra("url", url);
            intent.putExtra("OPType", OPType);
            intent.putExtra("OPID", OPID);
            intent.putExtra("OPName", OPName);
            intent.putExtra("UserGUID", UserGUID);
            intent.putExtra("Device", Device);
            intent.putExtra("System", System);
            intent.putExtra("Resolution", Resolution);
            intent.putExtra("Network", Network);
            intent.putExtra("CarrierOperator", CarrierOperator);
            intent.putExtra("StID", StID);
            intent.putExtra("sign", sign);
            paramContext.startService(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void clickAd(String chid, String linkTo, String linkUrl) {
        try {
            if (linkTo.equals("1")) {// 1、详情2、 列表
                String[] LinkUrls = linkUrl.split(",");
                String strTempID = LinkUrls[0];
                String strTempGUID = LinkUrls[1];
                Intent intent = new Intent();
                intent.setAction(basePackage+"NewsDetail");
                intent.putExtra("ID", strTempGUID);
                intent.putExtra("ResourceType", strTempID);
                intent.putExtra("type", 1);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.setAction(basePackage+"newsList");
                intent.putExtra("lanmuChID", chid);
                intent.putExtra("ChannelName", "新闻列表");
                startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		Assistant.unRegisterNetwork(InstitutionIndexActivity.this);
//	}
}
