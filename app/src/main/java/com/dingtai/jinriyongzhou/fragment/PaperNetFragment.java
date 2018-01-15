package com.dingtai.jinriyongzhou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.imgdisplay.ImgTool;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.DensityUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.PaperReadActivity;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.model.NewsChannelModel;

import java.util.ArrayList;


/**
 * description: 首页
 * autour: xyyou
 * date: 2017/12/19 下午 15:57
 * version: 1.0
 */

public class PaperNetFragment extends BaseFragment {
    private View mMainView;
    private PullToRefreshScrollView mPullRefresh;
    private RecyclerView mContentRv;
    private MyAdapter mAdapter;
    private ArrayList<NewsChannelModel> mDatalist;
    private String parentId;
    private final static int API_FLAG = IndexAPI.PAPER_NET_API;
    private final static String MESSAGE_FLAG = IndexAPI.PAPER_NET_API_MESSAGE;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_paper_net, container, false);
        inite();
        return mMainView;
    }

    public void inite() {
        initView();
        if (mDatalist == null) {
            mDatalist = new ArrayList<>();
            getData();
            mPullRefresh.setRefreshing(true);
        }
    }


    private void initView() {
        mContentRv = (RecyclerView) mMainView.findViewById(R.id.mContentRv);
        mContentRv.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new GridLayoutManager(getActivity(), 2);

        mContentRv.setLayoutManager(layout);
        mAdapter = new MyAdapter();
//        mContentRv.addItemDecoration(new DividerItemDecoration(getActivity(), 2, 1, getResources().getColor(R.color.dt_list_item_underline)));
//        mContentRv.addItemDecoration(new GridDivider(getActivity(), 1, this.getResources().getColor(R.color.dt_list_item_underline)));

        mContentRv.setAdapter(mAdapter);


        mPullRefresh = (PullToRefreshScrollView) mMainView.findViewById(R.id.pull_refresh);
        final GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        mPullRefresh.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        mPullRefresh.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
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
//        String url = API.COMMON_URL + "/Interface/NewsChildAPI.ashx?action=GetNewsChannelByPolitics&ChId=" + id + "&StID=" + API.STID + "&top=10";
        String url = API.COMMON_URL + "interface/NewsChannelAPI.ashx?action=GetNewsChannelList&parentID=" + parentId + "&StID=0";
        requestData(getActivity(), url, new Messenger(handler), API_FLAG, MESSAGE_FLAG, IndexHttpService.class);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case API_FLAG:
                    mPullRefresh.onRefreshComplete();
                    ArrayList<NewsChannelModel> tempList = (ArrayList<NewsChannelModel>) msg.getData().getParcelableArrayList("list").get(0);
                    if (tempList != null && tempList.size() > 0) {
                        mDatalist.clear();
                        mDatalist.addAll(tempList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }

        }

    };


    @Override
    public void onResume() {
        super.onResume();

    }

    public void setChannalId(String parentId) {
        this.parentId = parentId;
    }

    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SecondHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_paper_net, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.onbind(position);
        }

        @Override
        public int getItemCount() {
            return mDatalist == null ? 0 : mDatalist.size();
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
        private TextView mTitleTv;
        private TextView mContentTv;
        private ImageView mLogoIv;

        public DefaultHolder(View itemView) {
            super(itemView);
            mLogoIv = (ImageView) itemView.findViewById(R.id.mLogoIv);
            mTitleTv = (TextView) itemView.findViewById(R.id.mTitleTv);
            mContentTv = (TextView) itemView.findViewById(R.id.mContentTv);


        }

        public void onItemClick(int position) {

        }

        public void onbind(int position) {
            int newPosition = position;
            NewsChannelModel newsChannelModel = mDatalist.get(newPosition);
            if (newsChannelModel != null) {


                int w = (DisplayMetricsTool.getWidth(getActivity()) - DensityUtil.dip2px(getActivity(), 20)) / 2;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        w,
                        (int) (w * 9 / 16));

                mLogoIv.setLayoutParams(layoutParams);
                ImgTool.getInstance().loadRoundImg(newsChannelModel.getImageUrl(), mLogoIv, 5);

                if (!TextUtils.isEmpty(newsChannelModel.getChannelName()))
                    mTitleTv.setText(newsChannelModel.getChannelName());
                if (!TextUtils.isEmpty(newsChannelModel.getEnChName()))
                    mTitleTv.setText(newsChannelModel.getEnChName());

            }
        }
    }

    private class SecondHolder extends BaseHolder {
        private final static double PIC_WIDTH_IN_WINDOW = 21.0 / 100;
        private final static double PIC_WIDTH_HEIGHT_RATE = 9.0 / 16;
        private TextView mTitleTv;
        private ImageView mLogoIv;

        public SecondHolder(View itemView) {
            super(itemView);
            mLogoIv = (ImageView) itemView.findViewById(R.id.mLogoIv);
            mTitleTv = (TextView) itemView.findViewById(R.id.mTitleTv);


        }

        public void onItemClick(int position) {
            NewsChannelModel newsChannelModel = mDatalist.get(position);
            Intent intent = new Intent(getActivity(), PaperReadActivity.class);
            intent.putExtra("url", newsChannelModel.getChannelUrl());
            intent.putExtra("title", newsChannelModel.getChannelName());
            startActivity(intent);
        }

        public void onbind(int position) {
            int newPosition = position;
            NewsChannelModel newsChannelModel = mDatalist.get(newPosition);
            if (newsChannelModel != null) {


                int w = (int) (DisplayMetricsTool.getWidth(getActivity()) * PIC_WIDTH_IN_WINDOW);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        w,
                        (int) (w * PIC_WIDTH_HEIGHT_RATE));
                layoutParams.setMargins(0, DensityUtil.dip2px(getActivity(), 10), 0, DensityUtil.dip2px(getActivity(), 10));

                mLogoIv.setLayoutParams(layoutParams);
                ImgTool.getInstance().loadRoundImg(newsChannelModel.getImageUrl(), mLogoIv, 2);


                if (!TextUtils.isEmpty(newsChannelModel.getChannelName()))
                    mTitleTv.setText(newsChannelModel.getChannelName());
                if (!TextUtils.isEmpty(newsChannelModel.getEnChName()))
                    mTitleTv.setText(newsChannelModel.getEnChName());

            }
        }
    }
}
