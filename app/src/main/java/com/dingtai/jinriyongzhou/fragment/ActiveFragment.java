package com.dingtai.jinriyongzhou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.application.MyApplication;
import com.dingtai.jinriyongzhou.bean.ActiveListBean;
import com.dingtai.jinriyongzhou.service.IndexHttpService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/1/14 0014.
 */

public class ActiveFragment extends BaseFragment {

    private View mMainView;
    private PullToRefreshScrollView mPullRefresh;
    private RecyclerView mContentRv;
    private ActiveFragment.MyAdapter mAdapter;
    private ArrayList<ActiveListBean.DetialBean> mDatalist;
    private String parentId;
    private final static int API_UP_FLAG = IndexAPI.ACTIV_UP_LIST_API;
    private final static String UP_MESSAGE_FLAG = IndexAPI.ACTIV_UP_LIST_API_MESSAGE;

    private final static int API_DOWM_FLAG = IndexAPI.ACTIV_DOWN_LIST_API;
    private final static String DOWN_MESSAGE_FLAG = IndexAPI.ACTIV_DOWN_LIST_API_MESSAGE;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_active_xyyou, container, false);
        inite();
        return mMainView;
    }

    public void inite() {
        initView();
        if (mDatalist == null) {
            mDatalist = new ArrayList<>();
            refreshData();
            mPullRefresh.setRefreshing(true);
        }
    }


    private void initView() {
        mContentRv = (RecyclerView) mMainView.findViewById(R.id.mContentRv);
        mContentRv.setNestedScrollingEnabled(false);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());

        mContentRv.setLayoutManager(layout);
        mAdapter = new MyAdapter();
//        mContentRv.addItemDecoration(new DividerItemDecoration(getActivity(), 2, 1, getResources().getColor(R.color.dt_list_item_underline)));
//        mContentRv.addItemDecoration(new GridDivider(getActivity(), 1, this.getResources().getColor(R.color.dt_list_item_underline)));

        mContentRv.setAdapter(mAdapter);


        mPullRefresh = (PullToRefreshScrollView) mMainView.findViewById(R.id.pull_refresh);
        mPullRefresh.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
        mPullRefresh.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
        mPullRefresh.setHasPullUpFriction(true); // 设置没有上拉阻力
        mPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                loadMore();
            }
        });
    }

    private void loadMore() {
        //http://slb1.app.hn0746.com:8888/interface/ActiveAPI.ashx?action=GetActiveListShangla&top=10&dtop=0&StID=0
        int size = mDatalist == null ? 0 : mDatalist.size();

        String url = API.COMMON_URL + "interface/ActiveAPI.ashx?action=GetActiveListShangla&top=10&dtop=" + size + "&StID=0&sign=" + API.sign;
        requestData(getActivity(), url, new Messenger(handler), API_UP_FLAG, UP_MESSAGE_FLAG, IndexHttpService.class);
    }

    private void refreshData() {
        //
//        String url = API.COMMON_URL + "/Interface/NewsChildAPI.ashx?action=GetNewsChannelByPolitics&ChId=" + id + "&StID=" + API.STID + "&top=10";
        String url = API.COMMON_URL + "interface/ActiveAPI.ashx?action=GetActiveList&num=10&StID=0&sign=" + API.sign;
        requestData(getActivity(), url, new Messenger(handler), API_DOWM_FLAG, DOWN_MESSAGE_FLAG, IndexHttpService.class);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case API_UP_FLAG:
                    mPullRefresh.onRefreshComplete();
                    if (!msg.obj.toString().equals("暂无更多数据")) {
                        ArrayList<ActiveListBean.DetialBean> tempList = (ArrayList<ActiveListBean.DetialBean>) msg.getData().getParcelableArrayList("list").get(0);
                        if (tempList != null && tempList.size() > 0) {

                            mDatalist.addAll(tempList);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(MyApplication.context, "暂无更多数据", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MyApplication.context, "暂无更多数据", Toast.LENGTH_LONG).show();
                    }
                    break;
                case API_DOWM_FLAG:
                    mPullRefresh.onRefreshComplete();
                    if (!msg.obj.toString().equals("暂无更多数据")) {
                        ArrayList<ActiveListBean.DetialBean> tempList = (ArrayList<ActiveListBean.DetialBean>) msg.getData().getParcelableArrayList("list").get(0);
                        if (tempList != null && tempList.size() > 0) {
                            mDatalist.clear();
                            mDatalist.addAll(tempList);
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
    public void onResume() {
        super.onResume();

    }


    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new SecondHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_paper_net, parent, false));
            return new DefaultHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_active_default, parent, false));
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
        private final static int PIC_INTERVAL = 20;
        private final static double PIC_WIDTH_HEIGHT_RATE = 4.0 / 7;
        private ImageView mLogoIv;
        private TextView mTitleTv;

        public DefaultHolder(View itemView) {
            super(itemView);
            mLogoIv = (ImageView) itemView.findViewById(R.id.mLogoIv);
            mTitleTv = (TextView) itemView.findViewById(R.id.mTitleTv);

        }

        public void onItemClick(int position) {
            ActiveListBean.DetialBean itemBean = mDatalist.get(position);
            Intent intent;
            try {
                String strType = itemBean.getActiveType();

                if (strType.equals("0")) {
                    intent = null;
                    String strConvenienceName = itemBean.getActiveName().toString();
                    String strConvenienceUrl = itemBean.getActiveUrl().toString();
                    String strLogoUrl = itemBean.getActiveLogo().toString();
                    String status = itemBean.getActiveStatus();
                    intent = new Intent();
                    intent.setAction(getActivity().getPackageName() + "." + "NewsWeb");
                    intent.putExtra("PageUrl", strConvenienceUrl);
                    intent.putExtra("LogoUrl", strLogoUrl);
                    intent.putExtra("Title", strConvenienceName);
                    intent.putExtra("id", itemBean.getID());
                    intent.putExtra("Type", "1");
                    intent.putExtra("Status", status);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), itemBean.getActiveContent().toString(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception var12) {
                intent = new Intent();
                intent.setAction(getActivity().getPackageName() + "." + "NewsWeb");
                intent.putExtra("PageUrl", "http://www.baidu.com");
                intent.putExtra("LogoUrl", "http://www.baidu.com/img/bd_logo1.png");
                intent.putExtra("Title", "活动");
                intent.putExtra("Type", "1");
                startActivity(intent);
            }


        }

        public void onbind(int position) {
            ActiveListBean.DetialBean itemBean = mDatalist.get(position);
            int w = (int) (DisplayMetricsTool.getWidth(getActivity()));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    w - DensityUtil.dip2px(getActivity(), PIC_INTERVAL),
                    (int) (w * PIC_WIDTH_HEIGHT_RATE));

            mLogoIv.setLayoutParams(layoutParams);


            ImgTool.getInstance().loadRoundImg(itemBean.getActiveLogo(), mLogoIv, 0);
            mTitleTv.setText(itemBean.getActiveName());

        }
    }
}
