package com.dingtai.jinriyongzhou.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.livelib.activtity.LiveMainActivity;
import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.model.ActiveModel;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.utils.VideoPlaySwitch;
import com.dingtai.base.view.MyListView;
import com.dingtai.base.view.Tag;
import com.dingtai.base.view.TagListView;
import com.dingtai.base.view.TagView;
import com.dingtai.dtsetting.activity.SettingActivityNew;
import com.dingtai.dtvoc.activity.BroadCastListActivity;
import com.dingtai.dtvoc.activity.VideoListActivity;
import com.dingtai.dtvoc.model.TuiJianProgram;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.ActiveSearchAdapter;
import com.dingtai.jinriyongzhou.adapter.DianBoSearchAdapter;
import com.dingtai.jinriyongzhou.adapter.LiveSearchAdapter;
import com.dingtai.jinriyongzhou.adapter.NewSearchAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.jinriyongzhou.tool.ChooseLanmu;
import com.dingtai.newslib3.activity.NewsWebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewSeachActivity extends Activity implements TextWatcher, DialogInterface.OnClickListener {

    private TagListView mTagListView;
    private final List<Tag> mTags = new ArrayList<Tag>();

    private MyListView mListview;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private EditText mEtsearch;
    private TextView tv_seach;
    private ImageView search_icon;
    private TextView bt_search_clean;
    //新闻
    private List<NewsListModel> mListDate;
    private List<NewsListModel> mTemListDate;
    private NewSearchAdapter mAdapterSearchList;

    //直播
    private List<LiveChannelModel> mTempLiveList;
    private List<LiveChannelModel> mLiveList;
    private LiveSearchAdapter liveAdapter;

    //点播
    private List<TuiJianProgram> mTempDBList;
    private List<TuiJianProgram> mDBList;
    private DianBoSearchAdapter DBAdapter;


    //活动
    private List<ActiveModel> mTempHDList;
    private List<ActiveModel> mHDList;
    private ActiveSearchAdapter HDAdapter;


    private boolean downup;
    private String state = "";
    private String mSearchTex;
    private List<String> mListHistory = new ArrayList<String>();// 搜索 历史全数据
    private LinearLayout linear_layout_listview;

    private String type = "0";

    private AlertDialog choose;
    private SharedPreferences mSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_seach);

        mTagListView = (TagListView) findViewById(R.id.tagview);
        linear_layout_listview = (LinearLayout) findViewById(R.id.linear_layout_listview);
        mEtsearch = (EditText) findViewById(R.id.et_search);
        mEtsearch.addTextChangedListener(this);

        // 搜索数据列表
        mListview = (MyListView) findViewById(R.id.listviewsearch);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.search_pulllistview);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）

        mPullRefreshScrollView.setHasPullUpFriction(false); // 设置没有上拉阻力
        mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        mListview.addHeaderView(LayoutInflater.from(this).inflate(
                R.layout.head_view_splite
                , null));

        //标签
        getHistorydata();
        setUpData();

        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            //新闻适配器
            mListDate = new ArrayList<NewsListModel>();
            mTemListDate = new ArrayList<NewsListModel>();
            mAdapterSearchList = new NewSearchAdapter(this, mListDate);
            mListview.setAdapter(mAdapterSearchList);
            mListview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    // item点击事件
                    ChooseLanmu.toLanmu(NewSeachActivity.this,
                            (NewsListModel) arg0.getItemAtPosition(arg2));
                }
            });

        } else if (type.equals("1")) {
            //活动直播
            mTempLiveList = new ArrayList<LiveChannelModel>();
            mLiveList = new ArrayList<LiveChannelModel>();
            liveAdapter = new LiveSearchAdapter(this, mLiveList);
            mListview.setAdapter(liveAdapter);
            mListview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {

                    if (!Assistant.isWifi(getApplicationContext())) {
                        VideoPlaySwitch.swich(NewSeachActivity.this, NewSeachActivity.this);
                    } else {
                        String type = mLiveList.get(arg2 - 1).getLiveType();
                        if (!"".equals(type) && type != null) {
                            if ("3".equals(type)) {
                                Intent intent = new Intent(NewSeachActivity.this,
                                        LiveMainActivity.class);


                                switch (getStatus(mLiveList.get(arg2 - 1))) {
                                    case 0:// 将开始
                                        intent.putExtra("RTMPUrl", mLiveList.get(arg2 - 1)
                                                .getLiveRTMPUrl());
                                        break;
                                    case 1:// 进行中
                                        intent.putExtra("RTMPUrl", mLiveList.get(arg2 - 1)
                                                .getLiveRTMPUrl());
                                        break;
                                    case 2:// 结束
                                        intent.putExtra("RTMPUrl", mLiveList.get(arg2 - 1)
                                                .getLiveLink());
                                        break;
                                }

                                intent.putExtra("VideoUrl", mLiveList.get(arg2 - 1)
                                        .getVideoUrl());

                                intent.putExtra("PicPath", mLiveList.get(arg2 - 1)
                                        .getLiveImageUrl());
                                intent.putExtra("ID", mLiveList.get(arg2 - 1).getID());
                                intent.putExtra("Week", mLiveList.get(arg2 - 1).getWeek());
                                intent.putExtra("CommentsNum", mLiveList.get(arg2 - 1)
                                        .getCommentsNum());
                                intent.putExtra("name", mLiveList.get(arg2 - 1)
                                        .getLiveProgramName());
                                intent.putExtra("livename", mLiveList.get(arg2 - 1)
                                        .getLiveChannelName());
                                intent.putExtra("nowtime", mLiveList.get(arg2 - 1)
                                        .getLiveProgramDate());
                                intent.putExtra("isLive", mLiveList.get(arg2 - 1).getIsLive());
                                intent.putExtra("picUrl", mLiveList.get(arg2 - 1).getPicPath());
                                intent.putExtra("startTime", mLiveList.get(arg2 - 1)
                                        .getLiveBeginDate());
                                intent.putExtra("logo", mLiveList.get(arg2 - 1)
                                        .getLiveBeginLogo());
                                intent.putExtra("liveEvent", mLiveList.get(arg2 - 1)
                                        .getLiveEventID());
                                intent.putExtra("newsID", mLiveList.get(arg2 - 1)
                                        .getLiveNewChID());
                                intent.putExtra("status", mLiveList.get(arg2 - 1)
                                        .getStatus());
                                intent.putExtra("liveType", 2);

                                intent.putExtra("LiveBeginType", mLiveList.get(arg2 - 1).getLiveBeginType());
                                intent.putExtra("LiveBeginMedia", mLiveList.get(arg2 - 1).getLiveBeginMedia());

                                startActivity(intent);
                            }

                        }


                    }


                }
            });
        } else if (type.equals("2")) {
            //点播
            mTempDBList = new ArrayList<TuiJianProgram>();
            mDBList = new ArrayList<TuiJianProgram>();
            DBAdapter = new DianBoSearchAdapter(this, mDBList);
            mListview.setAdapter(DBAdapter);
            mListview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    if ("1".equals(mDBList.get(arg2 - 1).getVODType())) {
                        Intent intent = new Intent(NewSeachActivity.this, VideoListActivity.class);
                        intent.putExtra("id", mDBList.get(arg2 - 1).getID());
                        intent.putExtra("VODType", mDBList.get(arg2 - 1).getVODType());
                        intent.putExtra("ProgramName", mDBList.get(arg2 - 1).getProgramName());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(NewSeachActivity.this, BroadCastListActivity.class);
                        intent.putExtra("id", mDBList.get(arg2 - 1).getID());
                        intent.putExtra("VODType", mDBList.get(arg2 - 1).getVODType());
                        intent.putExtra("ProgramName", mDBList.get(arg2 - 1).getProgramName());
                        startActivity(intent);
                    }
                }
            });

        } else {
            //活动
            mTempHDList = new ArrayList<ActiveModel>();
            mHDList = new ArrayList<ActiveModel>();
            HDAdapter = new ActiveSearchAdapter(this, mHDList);
            mListview.setAdapter(HDAdapter);
            mListview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {

                    if (mHDList.get(arg2 - 1).getIsLive().equals("True")) {

                        Intent intent = new Intent(NewSeachActivity.this,
                                LiveMainActivity.class);

                        intent.putExtra("RTMPUrl", mHDList.get(arg2 - 1).getLiveUrl());
                        intent.putExtra("VideoUrl", mHDList.get(arg2 - 1).getLiveUrl());
                        intent.putExtra("PicPath", mHDList.get(arg2 - 1).getActiveLogo());
                        intent.putExtra("ID", mHDList.get(arg2 - 1).getLiveID());
                        intent.putExtra("Week", "");
                        intent.putExtra("CommentsNum", "99");
                        intent.putExtra("name", mHDList.get(arg2 - 1)
                                .getActiveName());
                        intent.putExtra("livename", mHDList.get(arg2 - 1)
                                .getActiveName());
                        intent.putExtra("nowtime", mHDList.get(arg2 - 1)
                                .getBeginDate());
                        intent.putExtra("isLive", mHDList.get(arg2 - 1).getIsLive());
                        intent.putExtra("picUrl", mHDList.get(arg2 - 1).getActiveLogo());
                        intent.putExtra("startTime", mHDList.get(arg2 - 1)
                                .getBeginDate());
                        intent.putExtra("logo", mHDList.get(arg2 - 1).getActiveLogo());
                        intent.putExtra("liveEvent", mHDList.get(arg2 - 1)
                                .getLiveID());
                        intent.putExtra("newsID", mHDList.get(arg2 - 1)
                                .getLiveID());
                        intent.putExtra("status", mHDList.get(arg2 - 1)
                                .getActiveStatus());
                        intent.putExtra("liveType", 2);
                        intent.putExtra("LiveBeginType", mHDList.get(arg2 - 1).getLiveBeginType());
                        intent.putExtra("LiveBeginMedia", mHDList.get(arg2 - 1).getLiveBeginMedia());
                        startActivity(intent);

                    } else {
                        try {
                            String strType = mHDList.get(arg2 - 1).getActiveType()
                                    .toString().trim();
                            if (strType.equals("0")) {
                                Intent intent = null;
                                String strConvenienceName = mHDList.get(arg2 - 1)
                                        .getActiveName().toString();
                                String strConvenienceUrl = mHDList.get(arg2 - 1)
                                        .getActiveUrl().toString();
                                String strLogoUrl = mHDList.get(arg2 - 1).getActiveLogo()
                                        .toString();
                                String status = mHDList.get(arg2 - 1).getActiveStatus();
                                intent = new Intent(NewSeachActivity.this, NewsWebView.class);
                                intent.putExtra("PageUrl", strConvenienceUrl);
                                intent.putExtra("LogoUrl", strLogoUrl);
                                intent.putExtra("Title", strConvenienceName);
                                intent.putExtra("id", mHDList.get(arg2 - 1).getID());
                                intent.putExtra("Type", " ");
                                intent.putExtra("Status", status);
                                startActivity(intent);
                            } else {
                                Toast.makeText(
                                        NewSeachActivity.this,
                                        mHDList.get(arg2 - 1).getActiveContent()
                                                .toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {

                            Intent intent = null;
                            intent = new Intent(NewSeachActivity.this, NewsWebView.class);
                            intent.putExtra("PageUrl", "http://www.baidu.com");
                            intent.putExtra("LogoUrl",
                                    "http://www.baidu.com/img/bd_logo1.png");
                            intent.putExtra("Title", "活动");
                            intent.putExtra("Type", " ");
                            startActivity(intent);

                        }
                    }
                }
            });

        }


        mTagListView.setOnTagClickListener(new TagListView.OnTagClickListener() {

            @Override
            public void onTagClick(TagView tagView, Tag tag) {
                // TODO Auto-generated method stub
                mEtsearch.setText(tag.getTitle());
                search();
            }
        });
        tv_seach = (TextView) this.findViewById(R.id.tv_seach);
        search_icon = (ImageView) this.findViewById(R.id.search_icon);
        bt_search_clean = (TextView) this.findViewById(R.id.bt_search_clean);
        bt_search_clean.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cleanHistory();
                mListHistory.clear();
                setUpData();
            }
        });
        search_icon.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                search();
            }
        });


        mEtsearch.setOnKeyListener(new OnKeyListener() {//输入完后按键盘上的搜索键【回车键改为了搜索键】

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {//修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(
                                    NewSeachActivity.this
                                            .getCurrentFocus()
                                            .getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);


                    search();

                }
                return false;
            }
        });


        tv_seach.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

    }


    private int getStatus(LiveChannelModel model) {
        int strState = -1;
        int Begin = -1;
        int End = -1;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        Date strBegin;
        Date strEnd;
        Date strNow;
        try {
            strBegin = formatter.parse(model.getLiveBeginDate());
            strEnd = formatter.parse(model.getLiveEndDate());
            strNow = DateTool.convertStrToDate(formatter.format(curDate));
            Begin = strNow.compareTo(strBegin);
            End = strNow.compareTo(strEnd);

            if (End > 0) {// 结束
                strState = 2;
            } else if (Begin >= 0 && End <= 0) {
                strState = 1;
            } else {
                strState = 0;
            }
        } catch (ParseException e) {
            return strState;
        } catch (Exception e) {
            return strState;

        }

        return strState;

    }

    /**
     * 对话框监听
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // 打开
                SettingActivityNew.IS_NET_TYPE = true;
                mSp = NewSeachActivity.this.getSharedPreferences("SETTING",
                        Context.MODE_PRIVATE);
                mSp.edit().putBoolean("IS_NET_TYPE", true).commit();
                Toast.makeText(NewSeachActivity.this, "成功开启!", Toast.LENGTH_SHORT).show();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                // 取消
                choose.dismiss();
                break;
        }

    }

    // 文本框监听
    @Override
    public void afterTextChanged(Editable arg0) {
        int len = arg0.length();
        if (len == 0) {
            if (linear_layout_listview.isShown()) {
                linear_layout_listview.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }


    private void search() {
        mSearchTex = mEtsearch.getText().toString();
        if (TextUtils.isEmpty(mSearchTex)) {
            Toast.makeText(NewSeachActivity.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (type.equals("0")) {
            getDate();
        } else if (type.equals("1")) {
            getLiveDate();
        } else if (type.equals("2")) {
            getDBDate();
        } else {
            getActiveDate();
        }

        saveHistory(mSearchTex);
        getHistorydata();
        setUpData();

    }

    /**
     * 清空历史
     */
    private void cleanHistory() {
        SharedPreferences sp = getSharedPreferences("search_list", 0);
        String longhistory = sp.getString("history", "");
        if (!TextUtils.isEmpty(longhistory)) {
            sp.edit().putString("history", "").commit();
            mListHistory.clear();

            Toast.makeText(this, "清除完成！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *
     */
    private void getHistorydata() {
        SharedPreferences sp = getSharedPreferences("search_list", 0);
        String longhistory = sp.getString("history", "");
        if (TextUtils.isEmpty(longhistory)) {
            return;
        }
        String[] hisArrays = longhistory.split("@,@");
        mListHistory.clear();
        for (String str : hisArrays) {
            mListHistory.add(str);
        }
    }

    /**
     * 把内容保存到sharedPreference中指定的字符段
     *
     * @param value 值
     */
    private void saveHistory(String value) {
        SharedPreferences sp = getSharedPreferences("search_list", 0);
        String longhistory = sp.getString("history", "");
        String str = value + "@,@";
        if (!longhistory.contains(str)) {// 历史不存在记录
            StringBuilder sb = new StringBuilder(longhistory);
            sb.insert(0, str);
            sp.edit().putString("history", sb.toString()).commit();

        } else if (!longhistory.substring(0, str.length()).equals(str)) {
            // 存在记录且不为第一个时
            String str2 = longhistory.replaceAll("@,@" + str, "@,@");
            StringBuilder sb = new StringBuilder(str2);
            sb.insert(0, str);
            sp.edit().putString("history", sb.toString()).commit();
        }
    }

    private void setUpData() {
        // 初始化历史列表
        mTags.clear();
        for (int i = 0; i < mListHistory.size(); i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setChecked(true);
            tag.setTitle(mListHistory.get(i));
            mTags.add(tag);

        }
        mTagListView.setTags(mTags);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downup = false;
            switch (msg.what) {

                case 100:
                    mTemListDate.clear();
                    mPullRefreshScrollView.onRefreshComplete();
                    linear_layout_listview.setVisibility(View.VISIBLE);
                    ArrayList<NewsListModel> temp_list = (ArrayList<NewsListModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTemListDate.addAll(temp_list);


                    if (state.equals("up")) { // 上拉
                        mListDate.addAll(mTemListDate);
                        mAdapterSearchList.notifyDataSetChanged();
                    } else if (mTemListDate.size() > 0) {
                        mListDate.clear();
                        mListDate.addAll(mTemListDate);
                        mAdapterSearchList.notifyDataSetChanged();
                    }
                    break;

                case 10:// 获取数据为空
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(NewSeachActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 222:// 无网络
                case 555:
                    Toast.makeText(NewSeachActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 200:// 直播搜索
                    mTempLiveList.clear();
                    mPullRefreshScrollView.onRefreshComplete();
                    linear_layout_listview.setVisibility(View.VISIBLE);
                    ArrayList<LiveChannelModel> temp_live = (ArrayList<LiveChannelModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTempLiveList.addAll(temp_live);


                    if (state.equals("up")) { // 上拉
                        mLiveList.addAll(mTempLiveList);
                        liveAdapter.notifyDataSetChanged();
                    } else if (mTempLiveList.size() > 0) {
                        mLiveList.clear();
                        mLiveList.addAll(mTempLiveList);
                        liveAdapter.notifyDataSetChanged();
                    }
                    break;

                case 300:// 点播搜索
                    mTempDBList.clear();
                    mPullRefreshScrollView.onRefreshComplete();
                    linear_layout_listview.setVisibility(View.VISIBLE);
                    ArrayList<TuiJianProgram> temp_dianbo = (ArrayList<TuiJianProgram>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTempDBList.addAll(temp_dianbo);


                    if (state.equals("up")) { // 上拉
                        mDBList.addAll(mTempDBList);
                        DBAdapter.notifyDataSetChanged();
                    } else if (mTempDBList.size() > 0) {
                        mDBList.clear();
                        mDBList.addAll(mTempDBList);
                        DBAdapter.notifyDataSetChanged();
                    }
                    break;

                case 400:// 活动搜索
                    mTempHDList.clear();
                    mPullRefreshScrollView.onRefreshComplete();
                    linear_layout_listview.setVisibility(View.VISIBLE);
                    ArrayList<ActiveModel> temp_active = (ArrayList<ActiveModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTempHDList.addAll(temp_active);


                    if (state.equals("up")) { // 上拉
                        mHDList.addAll(mTempHDList);
                        HDAdapter.notifyDataSetChanged();
                    } else if (mTempHDList.size() > 0) {
                        mHDList.clear();
                        mHDList.addAll(mTempHDList);
                        HDAdapter.notifyDataSetChanged();
                    }
                    break;


            }
        }
    };


    private void getActiveDate() {
        mTempHDList.clear();

        String dtop = "";
        String url;
        if (state.equals("up")) {
            // http://118.178.130.135/Interface/SearchAPI.ashx?action=SearchData&type=2&keyword=%E5%98%89%E5%96%84&top=10&dtop=0&StID=0
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
            dtop = "" + mHDList.size();
        } else {
            mTempHDList.clear();
            HDAdapter.notifyDataSetChanged();
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
        }
        get_Search_HDList(this, url, type, dtop, new Messenger(
                NewSeachActivity.this.handler));

    }

    private void getDBDate() {
        mTempDBList.clear();

        String dtop = "";
        String url;
        if (state.equals("up")) {
            // http://118.178.130.135/Interface/SearchAPI.ashx?action=SearchData&type=2&keyword=%E5%98%89%E5%96%84&top=10&dtop=0&StID=0
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
            dtop = "" + mDBList.size();
        } else {
            mTempDBList.clear();
            DBAdapter.notifyDataSetChanged();
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
        }
        get_Search_DBList(this, url, type, dtop, new Messenger(
                NewSeachActivity.this.handler));

    }

    private void getLiveDate() {
        mTempLiveList.clear();

        String dtop = "";
        String url;
        if (state.equals("up")) {
            // http://118.178.130.135/Interface/SearchAPI.ashx?action=SearchData&type=2&keyword=%E5%98%89%E5%96%84&top=10&dtop=0&StID=0
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
            dtop = "" + mLiveList.size();
        } else {
            mTempLiveList.clear();
            liveAdapter.notifyDataSetChanged();
            url = API.COMMON_URL
                    + "Interface/SearchAPI.ashx?action=SearchData";
        }
        get_Search_LiveList(this, url, type, dtop, new Messenger(
                NewSeachActivity.this.handler));

    }


    private void getDate() {
        mTemListDate.clear();
        String num = "10";
        String dtop = "";
        String url;
        if (state.equals("up")) {
            // http://main.d5mt.com.cn/Interface/NewsAPI.ashx?action=GetNewsKeyWordShangLa&top=3&dtop=0&StID=1&keywords=%E6%98%AF
            // up
            url = API.COMMON_URL
                    + "Interface/NewsAPI.ashx?action=GetNewsKeyWordShangLa";
            dtop = "" + mListDate.size();
        } else {
            mListDate.clear();
            mAdapterSearchList.notifyDataSetChanged();
            url = API.COMMON_URL
                    + "Interface/NewsAPI.ashx?action=GetNewsKeyWordList";
        }
        get_Search_list(this, url, num, dtop, new Messenger(
                NewSeachActivity.this.handler));
    }


    private void get_Search_DBList(Context context, String url, String type,
                                   String dtop, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.SEARCH_DB_LIST_API);
        localIntent.putExtra(IndexAPI.SEARCH_DB_LIST_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("StID", API.STID);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("top", "10");
        localIntent.putExtra("type", type);
        try {
            localIntent.putExtra("keyword",
                    URLEncoder.encode(mSearchTex, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.startService(localIntent);
    }


    private void get_Search_HDList(Context context, String url, String type,
                                   String dtop, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.SEARCH_HD_LIST_API);
        localIntent.putExtra(IndexAPI.SEARCH_HD_LIST_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("StID", API.STID);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("top", "10");
        localIntent.putExtra("type", type);
        try {
            localIntent.putExtra("keyword",
                    URLEncoder.encode(mSearchTex, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.startService(localIntent);
    }


    private void get_Search_LiveList(Context context, String url, String type,
                                     String dtop, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.SEARCH_LIVE_LIST_API);
        localIntent.putExtra(IndexAPI.SEARCH_LIVE_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("StID", API.STID);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("top", "10");
        localIntent.putExtra("type", type);
        try {
            localIntent.putExtra("keyword",
                    URLEncoder.encode(mSearchTex, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.startService(localIntent);
    }

    private void get_Search_list(Context context, String url, String num,
                                 String dtop, Messenger paramMessenger) {
        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", NewsAPI.SEARCH_LIST_API);
        localIntent.putExtra(NewsAPI.SEARCH_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("StID", API.STID);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("num", num);
        try {
            localIntent.putExtra("keywords",
                    URLEncoder.encode(mSearchTex, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        context.startService(localIntent);
    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(NewSeachActivity.this,
            // System.currentTimeMillis(), 10000);
            String str = DateTool.getCurrentTime();
            paramPullToRefreshBase.getLoadingLayoutProxy().setLastUpdatedLabel(
                    str);

            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始刷新");
            if (TextUtils.isEmpty(mSearchTex)) {
                Message k = handler.obtainMessage();
                k.what = 1001;
                handler.sendMessage(k);
                return;
            }
            if (downup == false) {
                if (Assistant.IsContectInterNet(NewSeachActivity.this, false)) {
                    state = "down";
                    downup = true;
                    if (type.equals("0")) {
                        getDate();
                    } else if (type.equals("1")) {
                        getLiveDate();
                    } else if (type.equals("2")) {
                        getDBDate();
                    } else {
                        getActiveDate();
                    }

                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    m.what = 555;
                    handler.sendMessage(m);
                }
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {


            mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
                    "正在加载");
            mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel(
                    "上拉加载更多");
            mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                    "释放开始加载");
            if (TextUtils.isEmpty(mSearchTex)) {
                Message k = handler.obtainMessage();
                k.what = 1001;
                handler.sendMessage(k);
                return;
            }
            if (downup == false) {
                if (Assistant.IsContectInterNet(NewSeachActivity.this, false)) {
                    downup = true;
                    state = "up";
                    if (type.equals("0")) {
                        getDate();
                    } else if (type.equals("1")) {
                        getLiveDate();
                    } else if (type.equals("2")) {
                        getDBDate();
                    } else {
                        getActiveDate();
                    }
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    m.what = 555;
                    handler.sendMessage(m);
                }
            }
        }
    };

}
