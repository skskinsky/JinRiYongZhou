package com.dingtai.jinriyongzhou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.imgdisplay.ImgTool;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.dtvoc.activity.VideoListActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.application.MyApplication;
import com.dingtai.jinriyongzhou.bean.DianBoDetialListBean;
import com.dingtai.jinriyongzhou.divider.DividerItemDecoration;
import com.dingtai.jinriyongzhou.service.IndexHttpService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class DianBoListActivity extends BaseActivity {

    private String id;
    private RecyclerView mContentRv;
    private MyAdapter mAdapter;
    private PullToRefreshScrollView mPullRefresh;
    private ArrayList<DianBoDetialListBean.ChannleByProgramBean> mDataList;
    private final static int API_FLAG = IndexAPI.DIANBO_DETIAL_LIST_API;
    private final static String MESSAGE_FLAG = IndexAPI.DIANBO_DETIAL_LIST_API_MESSAGE;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case API_FLAG:
                    mPullRefresh.onRefreshComplete();
                    if (!msg.obj.toString().equals("暂无更多数据")) {
                        ArrayList<DianBoDetialListBean.ChannleByProgramBean> tempList = null;
                        try {
                            tempList = (ArrayList<DianBoDetialListBean.ChannleByProgramBean>) msg.getData().getParcelableArrayList("list").get(0);
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
        LinearLayoutManager layout = new LinearLayoutManager(this);

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
        String url = API.COMMON_URL + "interface/VodByJRYZ.ashx?action=GetChannleByProgram&ClassID=" + id;
        requestData(this, url, new Messenger(handler), API_FLAG, MESSAGE_FLAG, IndexHttpService.class);
    }


    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {


        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ButtonHolder(LayoutInflater.from(DianBoListActivity.this).inflate(R.layout.item_baoliao_button, parent, false));
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


    private class ButtonHolder extends BaseHolder {
        private TextView mTitleTv;
        private RecyclerView mContentRv;
        private ArrayList<DianBoDetialListBean.ChannleByProgramBean.VODChannelsProgramBean> contentList;
        private ButtonAdapter mButtonAdapter;


        public ButtonHolder(View itemView) {


            super(itemView);
            mTitleTv = (TextView) itemView.findViewById(R.id.mTitleTv);
            mContentRv = (RecyclerView) itemView.findViewById(R.id.mContentRv);


        }

        public void onItemClick(int position) {
//            int newPosition = position - mTobList.size();
//            DianBoButtonModel baoliaoButtonModel = mButtomList.get(newPosition);
//            baoliaoButtonModel.getLiveContent();
//            //    Intent intent = this.getIntent();
//            this.path = intent.getStringExtra("video_url");
//            this.name = intent.getStringExtra("name");
//            this.logo = intent.getStringExtra("logo");
//            this.isNews = intent.getBooleanExtra("isNews", false);
//            this.videoFlag = intent.getStringExtra("video_flag");
//            if(intent.hasExtra("shareFlag")) {
//                this.shareFlag = intent.getStringExtra("shareFlag");
//            }
        }

        public void onbind(int position) {
            contentList = new ArrayList<>();

            DianBoDetialListBean.ChannleByProgramBean itemBean = mDataList.get(position);


            if (!TextUtils.isEmpty(itemBean.getLiveChannelName())) {
                mTitleTv.setText(itemBean.getLiveChannelName());
            }
            List<DianBoDetialListBean.ChannleByProgramBean.VODChannelsProgramBean> tempList = itemBean.getVODChannelsProgram();


            if (tempList != null && tempList.size() > 0) {
                contentList.addAll(tempList);

            }
            if (contentList.size() == 0) {
                mContentRv.setVisibility(View.GONE);
            } else {
                mContentRv.setVisibility(View.VISIBLE);
            }
            mButtonAdapter = new ButtonAdapter();

            LinearLayoutManager layout = new LinearLayoutManager(DianBoListActivity.this);
            layout.setOrientation(LinearLayoutManager.HORIZONTAL);
            mContentRv.setLayoutManager(layout);

            mContentRv.setAdapter(mButtonAdapter);

        }

        private class ButtonAdapter extends RecyclerView.Adapter<BaseHolder> {
            @Override
            public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ButtonItemHolder(LayoutInflater.from(DianBoListActivity.this).inflate(R.layout.item_baoliao_button_rv, parent, false));
            }

            @Override
            public void onBindViewHolder(BaseHolder holder, int position) {
                holder.onbind(position);
            }

            @Override
            public int getItemCount() {
                return contentList == null ? 0 : contentList.size();
            }
        }

        private class ButtonItemHolder extends BaseHolder {
            private TextView mTitleTv;
            private ImageView mLogoIv;
            private ImageView iv_bg;

            public ButtonItemHolder(View itemView) {
                super(itemView);
                mLogoIv = (ImageView) itemView.findViewById(R.id.mLogoIv);
                mTitleTv = (TextView) itemView.findViewById(R.id.mTitleTv);
                iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
            }

            public void onItemClick(int position) {
                DianBoDetialListBean.ChannleByProgramBean.VODChannelsProgramBean itemBean = contentList.get(position);
                Intent intent = new Intent(DianBoListActivity.this, VideoListActivity.class);
                intent.putExtra("id", itemBean.getID());
                intent.putExtra("VODType", itemBean.getVODType());
                intent.putExtra("ProgramName", itemBean.getProgramName());
                DianBoListActivity.this.startActivity(intent);
            }

            public void onbind(int position) {
                DianBoDetialListBean.ChannleByProgramBean.VODChannelsProgramBean itemBean = contentList.get(position);
                ImgTool.getInstance().loadImg(itemBean.getProgramLogo(), mLogoIv);
                if (!TextUtils.isEmpty(itemBean.getProgramName()))
                    mTitleTv.setText(itemBean.getProgramName());

            }
        }
    }
}
