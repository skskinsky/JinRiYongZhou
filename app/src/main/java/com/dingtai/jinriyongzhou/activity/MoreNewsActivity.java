package com.dingtai.jinriyongzhou.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dingtai.base.api.API;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshListView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.R.layout;
import com.dingtai.jinriyongzhou.adapter.IndexNewsAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.HHIndexNewsListModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.jinriyongzhou.tool.ChooseLanmu;

import java.util.ArrayList;
import java.util.List;

public class MoreNewsActivity extends Activity {


    private PullToRefreshListView mPullRefreshScrollView;
    //本土资讯
    private ListView news_recyclerview;
    private IndexNewsAdapter indexNewsAdapter;
    private List<HHIndexNewsListModel> benTuResList;
    private List<HHIndexNewsListModel> list = new ArrayList<>();


    private boolean isdown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_more_news);

        benTuResList = new ArrayList<HHIndexNewsListModel>();

        mPullRefreshScrollView = (PullToRefreshListView) findViewById(R.id.id_stickynavlayout_innerscrollview);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));

        mPullRefreshScrollView.setHasPullUpFriction(true); // 设置没有上拉阻力
        news_recyclerview = mPullRefreshScrollView.getRefreshableView();

        mPullRefreshScrollView
                .setOnRefreshListener(this.mOnRefreshListener);

        findViewById(R.id.command_return).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        //设置布局管理器
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(this);
        newsLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//		news_recyclerview.setLayoutManager(newsLayoutManager);
		indexNewsAdapter = new IndexNewsAdapter(this, benTuResList);
        news_recyclerview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChooseLanmu.homeToLanmu(MoreNewsActivity.this, benTuResList.get(position));
            }
        });
        news_recyclerview.setAdapter(indexNewsAdapter);

        getData("10", "0");


    }

    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            isdown = true;
            getData("10", "0");
        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {
            isdown = false;
            getData("10", String.valueOf(benTuResList.size()));
        }
    };

    private void getData(String top, String dtop) {

        String url = API.COMMON_URL + "interface/AppNewHHIndexAPI.ashx?action=GetMoreNewsByType";
        Intent localIntent = new Intent(MoreNewsActivity.this, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.NEWS_MORE_API);
        localIntent.putExtra(IndexAPI.GET_MORE_NEWS_MESSAGE, new Messenger(handler));
        localIntent.putExtra("url", url);
        localIntent.putExtra("type", getIntent().getStringExtra("type"));
        localIntent.putExtra("top", top);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("StID", API.STID);
        startService(localIntent);
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {


            switch (msg.what) {

                case 555:
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(MoreNewsActivity.this, (CharSequence) msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;

                case 100:
                    mPullRefreshScrollView.onRefreshComplete();
                    list = (ArrayList<HHIndexNewsListModel>) msg.getData()
                            .getParcelableArrayList("list").get(0);
                    if (isdown) {
                        benTuResList.clear();
                        benTuResList.addAll(list);
                        indexNewsAdapter.setData(benTuResList);
                    } else {
                        benTuResList.addAll(list);
                        indexNewsAdapter.setData(benTuResList);
                    }
                    break;


                default:
                    break;
            }
        }

        ;
    };


}
