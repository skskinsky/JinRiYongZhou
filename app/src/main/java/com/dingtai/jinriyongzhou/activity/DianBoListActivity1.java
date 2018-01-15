package com.dingtai.jinriyongzhou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.application.MyApplication;
import com.dingtai.jinriyongzhou.bean.DianBoDetialListBean2;
import com.dingtai.jinriyongzhou.divider.DividerItemDecoration;
import com.dingtai.jinriyongzhou.service.IndexHttpService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class DianBoListActivity1 extends BaseActivity {

    private String id;
    private RecyclerView mContentRv;
    private MyAdapter mAdapter;
    private PullToRefreshScrollView mPullRefresh;
    private ArrayList<DianBoDetialListBean2.VODChannelsContentBean> mDataList;
    private final static int API_FLAG = IndexAPI.DIANBO_DETIAL_LIST_API2;
    private final static String MESSAGE_FLAG = IndexAPI.DIANBO_DETIAL_LIST_API2_MESSAGE;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case API_FLAG:
                    mPullRefresh.onRefreshComplete();
                    if (!msg.obj.toString().equals("暂无更多数据")) {
                        ArrayList<DianBoDetialListBean2.VODChannelsContentBean> tempList = null;
                        try {
                            tempList = (ArrayList<DianBoDetialListBean2.VODChannelsContentBean>) msg.getData().getParcelableArrayList("list").get(0);
                        } catch (Exception e) {
                            Log.e("xyyou", e.toString());
                            e.printStackTrace();
                        }
                        if (tempList != null && tempList.size() > 0) {
                            mDataList.clear();
                            mDataList.addAll(tempList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MyApplication.context, "暂无更多数据", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MyApplication.context, "暂无更多数据", Toast.LENGTH_LONG).show();
                    }


                    break;
            }

        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dianbo_list);
        initeTitle();
        id = getIntent().getStringExtra("ID");
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        initView();
        initData();
    }


    private void initData() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
            mPullRefresh.setRefreshing(true);
            getData();
        }
    }

    private void initView() {
        mContentRv = (RecyclerView) findViewById(R.id.mContentRv);
        mContentRv.setNestedScrollingEnabled(false);
        GridLayoutManager layout = new GridLayoutManager(this, 2);
        mContentRv.addItemDecoration(new DividerItemDecoration(this, 2, 1, getResources().getColor(R.color.dt_list_item_underline)));

        mContentRv.setLayoutManager(layout);
        mAdapter = new MyAdapter();
        mContentRv.addItemDecoration(new DividerItemDecoration(this, 2, 1, getResources().getColor(R.color.dt_list_item_underline)));

        mContentRv.setAdapter(mAdapter);
        mPullRefresh = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
        mPullRefresh.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefresh.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));
        mPullRefresh.setHasPullUpFriction(true); // 设置没有上拉阻力
        mPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {

            }
        });
    }

    private void getData() {
        String url = API.COMMON_URL + "interface/VodAPI.ashx?action=GetYZProgramList&ClassID=" + id;
        requestData(this, url, new Messenger(handler), API_FLAG, MESSAGE_FLAG, IndexHttpService.class);
    }


    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {


        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new DefaultHolder(LayoutInflater.from(DianBoListActivity1.this).inflate(R.layout.item_baoliao_button, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.onbind(position);
        }

        @Override
        public int getItemCount() {
            return mDataList == null ? 0 : mDataList.size();
        }


    }

    private abstract class BaseHolder extends RecyclerView.ViewHolder {


        public BaseHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getAdapterPosition());
                }


            });
        }

        public abstract void onItemClick(int position);

        public abstract void onbind(int position);
    }


    private class DefaultHolder extends BaseHolder {


        public DefaultHolder(View itemView) {


            super(itemView);


        }

        public void onItemClick(int position) {

        }

        public void onbind(int position) {


        }


    }
}
