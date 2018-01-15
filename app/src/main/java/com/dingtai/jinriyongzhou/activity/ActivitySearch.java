package com.dingtai.jinriyongzhou.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.application.MyApplication;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.ChooseLanmu;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.view.MyListView;

import com.dingtai.dtsearch.adapter.AdapterSearchHistory;
import com.dingtai.dtsearch.service.SearchService;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.newslib3.adapter.NewsAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ActivitySearch extends BaseActivity implements OnClickListener,
        TextWatcher {
    private MyListView mListview;
    private PullToRefreshScrollView mPullRefreshScrollView;
    private View mVet;// 下划线
    private View mRlview;// 历史列表的界面
    private static String mSearchTex;
    private ImageView mIvClean;
    private List<NewsListModel> mListDate;
    private List<NewsListModel> mTemListDate;
    private NewsAdapter mAdapterSearchList;
    private EditText mEtsearch;
    private List<String> mListHistory;// 搜索 历史全数据
    private List<String> mHistoryList;// 搜索 历史匹配到的数据
    private boolean downup;
    private String state;
    private String STID;
    private InputMethodManager imm;
    private ListView mLvHistory;
    private AdapterSearchHistory mAdapterHistory;
    private int color1;
    private int color2;
    // private int bgColor,bgColor1;
    private boolean mIsSetColor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
    }

    private void initView() {
        imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        ((TextView) findViewById(R.id.command_title)).setText("搜索");
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        mListHistory = new ArrayList<String>();
        mHistoryList = new ArrayList<String>();
        mListDate = new ArrayList<NewsListModel>();
        mTemListDate = new ArrayList<NewsListModel>();
        mAdapterSearchList = new NewsAdapter(this, mListDate);
        color1 = Color.parseColor("#11cd6e");
        color2 = Color.parseColor("#999999");
        // bgColor=Color.parseColor("#ebebeb");
        // bgColor1=Color.parseColor("#ffffff");
        mIsSetColor = true;
        state = "";
        downup = false;
        STID = API.STID;
        mEtsearch = (EditText) findViewById(R.id.etsearch);

        mVet = findViewById(R.id.vet);
        mIvClean = (ImageView) findViewById(R.id.ivclean);
        mRlview = findViewById(R.id.llhistory);
        // mRlview.setVisibility(View.VISIBLE);//
        mIvClean.setOnClickListener(this);
        findViewById(R.id.btsearch).setOnClickListener(this);
        findViewById(R.id.bt_search_clean).setOnClickListener(this);
        findViewById(R.id.command_return).setOnClickListener(this);

        // 初始化历史列表
        getHistorydata();
        mHistoryList.addAll(mListHistory);
        if (mHistoryList.size() > 0) {
            mRlview.setVisibility(View.VISIBLE);
        }
        // 搜索历史列表
        mLvHistory = (ListView) findViewById(R.id.lvhistory);
        mAdapterHistory = new AdapterSearchHistory(this, mHistoryList);
        mLvHistory.setAdapter(mAdapterHistory);
        mLvHistory.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                mSearchTex = arg0.getItemAtPosition(arg2).toString();
                mEtsearch.setText(mSearchTex);
                search();
            }
        });
        // 搜索数据列表
        mListview = (MyListView) findViewById(R.id.listviewsearch);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.search_pulllistview);
        mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        if (MyApplication.RefreshVersion==2){
            mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
            // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
            // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
            // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
            mPullRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);

            mPullRefreshScrollView.setHasPullUpFriction(true); // 设置没有上拉阻力
        }
        mListview.setAdapter(mAdapterSearchList);
        mListview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // item点击事件
                ChooseLanmu.toLanmu(ActivitySearch.this,
                        (NewsListModel) arg0.getItemAtPosition(arg2));
            }
        });
        mEtsearch.addTextChangedListener(this);
        mEtsearch.setOnKeyListener(new OnKeyListener() {
            // 监听回车
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    search();
                    return true;
                }
                return false;
            }
        });
        mEtsearch.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsSetColor) {
                    mVet.setBackgroundColor(color1);
                    mIsSetColor = false;
                }
                return false;
            }
        });
        getKeyWord();
    }

    private void getKeyWord() {
        String url = API.COMMON_URL
                + "Interface/NewsAPI.ashx?action=GetNewsKeyWord&StID=" + API.STID;
        requestData(getApplicationContext(), url, new Messenger(handler),
                API.GET_NEWS_KEYWORD, API.GET_NEWS_KEYWORD_MESSENGER,
                SearchService.class);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downup = false;
            switch (msg.what) {
                case 400:
                    mEtsearch.setHint("大家都在搜:" + msg.obj);
                    break;
                case 404:
                    mEtsearch.setHint("大家都在搜:股市");
                    break;
                // case 722:
                // int k = (Integer) msg.obj; // 位置
                // mListDate.get(k).setIsShow(true);
                // mAdapter.notifyDataSetChanged();
                // // 调到指定位置
                // // mListview.getsetSelection(k);
                // break;
                case 10:// 获取数据为空
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(ActivitySearch.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 222:// 无网络
                case 555:
                    Toast.makeText(ActivitySearch.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 100:// 获取数据
                    mPullRefreshScrollView.onRefreshComplete();
                    ArrayList<NewsListModel> temp_list = (ArrayList<NewsListModel>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTemListDate.addAll(temp_list);
                    // if (mTemListDate.size()>0) {
                    // ll_search.setBackgroundColor(bgColor);
                    // }
                    imm.hideSoftInputFromWindow(mEtsearch.getWindowToken(), 0);
                    if (!mIsSetColor) {
                        mVet.setBackgroundColor(color2);
                        mIsSetColor = true;
                    }
                    if (state.equals("up")) { // 上拉
                        mListDate.addAll(mTemListDate);
                        mAdapterSearchList.notifyDataSetChanged();
                    } else if (mTemListDate.size() > 0) {
                        mListDate.clear();
                        mListDate.addAll(mTemListDate);
                        mAdapterSearchList.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btsearch) {
            search();
        } else if (i == R.id.bt_search_clean) {
            cleanHistory();
            mVet.setBackgroundColor(color2);
            mIsSetColor = true;
            mRlview.setVisibility(View.GONE);
        } else if (i == R.id.ivclean) {
            mEtsearch.setText("");
        } else if (i == R.id.command_return) {
            imm.hideSoftInputFromWindow(mEtsearch.getWindowToken(), 0);
            finish();
        }
    }

    private void search() {
        mSearchTex = mEtsearch.getText().toString();
        if (TextUtils.isEmpty(mSearchTex)) {
            Toast.makeText(ActivitySearch.this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mRlview.getVisibility() == View.VISIBLE) {
            mRlview.setVisibility(View.GONE);
        }
        state = "down";
        getDate();
        saveHistory(mSearchTex);
        getHistorydata();

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

    /**
     * 清空历史
     */
    private void cleanHistory() {
        SharedPreferences sp = getSharedPreferences("search_list", 0);
        String longhistory = sp.getString("history", "");
        if (!TextUtils.isEmpty(longhistory)) {
            sp.edit().putString("history", "").commit();
            mListHistory.clear();
            mHistoryList.clear();
            mAdapterHistory.notifyDataSetChanged();
            Toast.makeText(this, "清除完成！", Toast.LENGTH_SHORT).show();
        }
    }

    private void getDate() {
        mTemListDate.clear();
        if (state.equals("down")){
            mListDate.clear();
            mAdapterSearchList.notifyDataSetChanged();
        }
        String dtop = "" + mListDate.size();
        String url = "";
        try {
            url = API.COMMON_URL
                    + "Interface/NewsAPI.ashx?action=GetNewsKeyWordShangLa&top=10&dtop=" + dtop + "&StID=" + API.STID + "&keywords=" + URLEncoder.encode(mSearchTex, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        requestData(getApplicationContext(), url, new Messenger(handler), NewsAPI.SEARCH_LIST_API, NewsAPI.SEARCH_LIST_MESSAGE, SearchService.class);
    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(ActivitySearch.this,
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
                if (Assistant.IsContectInterNet(ActivitySearch.this, false)) {
                    state = "down";
                    downup = true;
                    getDate();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    m.what = 555;
                    handler.sendMessage(m);
                }
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            // String str = DateUtils.formatDateTime(ActivitySearch.this,
            // System.currentTimeMillis(), 10000);
            String str = DateTool.getCurrentTime();

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
                if (Assistant.IsContectInterNet(ActivitySearch.this, false)) {
                    downup = true;
                    state = "up";
                    getDate();
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    m.what = 555;
                    handler.sendMessage(m);
                }
            }
        }
    };
    private LinearLayout ll_search;

    // 文本框监听
    @Override
    public void afterTextChanged(Editable arg0) {
        int len = arg0.length();
        if (len > 0) {
            mIvClean.setVisibility(View.VISIBLE);

        } else {
            mIvClean.setVisibility(View.INVISIBLE);
            if (mListHistory.size() > 0) {
                mHistoryList.clear();
                mHistoryList.addAll(mListHistory);
                // ll_search.setBackgroundColor(bgColor1);
                mAdapterHistory.notifyDataSetInvalidated();
                mRlview.setVisibility(View.VISIBLE);
            } else {
                mRlview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        mRlview.setVisibility(View.VISIBLE);

        final String key = mEtsearch.getText().toString().trim();
        if (TextUtils.isEmpty(key)) {
            return;
        }
        containKey(key, mListHistory);
        if (mHistoryList.size() > 0) {
            mAdapterHistory.notifyDataSetInvalidated();
        } else {
            mRlview.setVisibility(View.GONE);
        }
    }

    /**
     * 列表关键词项
     */
    private void containKey(String key, List<String> list) {
        mHistoryList.clear();
        for (String str : list) {
            if (str.contains(key) || str.contains(key.toUpperCase())) {
                mHistoryList.add(str);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mRlview.getVisibility() == View.VISIBLE) {
                mRlview.setVisibility(View.GONE);
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
