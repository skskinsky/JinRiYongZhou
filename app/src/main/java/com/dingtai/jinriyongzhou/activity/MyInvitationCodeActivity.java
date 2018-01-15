package com.dingtai.jinriyongzhou.activity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.share.BaseShare;
import com.dingtai.jinriyongzhou.R;

public class MyInvitationCodeActivity extends BaseFragmentActivity implements OnClickListener {


    private static final double QRCODE_RATE = 268.0 / 751;
    private String mCode;
    private ImageView title_bar_search;
    private ImageView mLogoIv;
    private TextView mTitle1Tv;
    private RecyclerView mRecyclerView;
    private String[] names = new String[]{"朋友圈", "微信", "QQ空间", "QQ", "短信", "微博"};
    private int[] logos = new int[]{R.drawable.friend, R.drawable.weixin, R.drawable.zone, R.drawable.qq, R.drawable.duanxin,R.drawable.weibo};
    private MyAdapter mAdapter;
    private PullToRefreshScrollView mPullRefresh;

    protected void onCreate(Bundle paramBundle) {
        // TODO Auto-generated method stub
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_my_invitation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mCode = getIntent().getStringExtra("code");
        initView();
        initeTitle();
        setTitle("我的邀请码");

    }

    private void initView() {
        title_bar_search = (ImageView) findViewById(R.id.title_bar_search);
        title_bar_search.setVisibility(View.VISIBLE);
        title_bar_search.setImageDrawable(getResources().getDrawable(R.drawable.baocun));
        title_bar_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO  保存图片
            }
        });
        mTitle1Tv = (TextView) findViewById(R.id.mTitle1Tv);
        mLogoIv = (ImageView) findViewById(R.id.mLogoIv);
        mTitle1Tv.setText("我的邀请码为: " + mCode);
        SharedPreferences userInfo = getSharedPreferences("UserInfo", 0);
        String imgUrl = userInfo.getString("qrcode", "");
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLogoIv.getLayoutParams();
//        int side_lenght = (int) (DisplayMetricsTool.getWidth(this) * QRCODE_RATE);
//        layoutParams.width = side_lenght;
//        layoutParams.height = side_lenght;
//        mLogoIv.setLayoutParams(layoutParams);
        Glide.with(this).load(imgUrl).asBitmap().placeholder(R.drawable.dt_standard_index_news_bg)
                .error(R.drawable.dt_standard_index_news_bg).centerCrop().into(mLogoIv);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager layout = new GridLayoutManager(this, 5);
        mRecyclerView.setLayoutManager(layout);
        mAdapter = new MyAdapter();
//        mContentRv.addItemDecoration(new DividerItemDecoration(getActivity(), 2, 1, getResources().getColor(R.color.dt_list_item_underline)));
//        mContentRv.addItemDecoration(new GridDivider(getActivity(), 1, this.getResources().getColor(R.color.dt_list_item_underline)));

        mRecyclerView.setAdapter(mAdapter);

        mPullRefresh = (PullToRefreshScrollView) findViewById(R.id.pull_refresh);
        mPullRefresh.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefresh.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));
        mPullRefresh.setHasPullUpFriction(true); // 设置没有上拉阻力
        mPullRefresh.setMode(PullToRefreshBase.Mode.DISABLED);
        mPullRefresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {
                initView();
                mPullRefresh.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> pullToRefreshBase) {

            }
        });
    }


    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new SecondHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_paper_net, parent, false));
            return new DefaultHolder(LayoutInflater.from(MyInvitationCodeActivity.this).inflate(R.layout.item_invite_code_share, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.onbind(position);
        }

        @Override
        public int getItemCount() {
            if (names != null && logos != null) {
                return names.length != logos.length ? Math.min(names.length, logos.length) : names.length;
            }
            return 0;
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


    public class DefaultHolder extends BaseHolder {
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

            String ShareName = API.ShareName + "移动客户端";
            String URL = API.ShareURL;
            String ShareCotent = Html.fromHtml(API.shareContent + mCode + ",大家快来下载!").toString();
            BaseShare bs = new BaseShare(MyInvitationCodeActivity.this, ShareName, ShareCotent,
                    URL, API.ICON_URL + "/Images/ic_launcher.png", "99", "");
            switch (position) {
                case 4:
                    bs.shareShortMessage();
                    break;
                case 0:
                    bs.ShareFriends();
                    break;
                case 1:
                    bs.ShareWeiXin();
                    break;
                case 2:
                    bs.ShareQQZone();
                    break;
                case 3:
                    bs.ShareQQ();
                    break;
                case 5:
                    bs.ShareWeibo();
                    break;
                default:
                    break;
            }
        }

        public void onbind(int position) {
            String name = names[position];
            int logo = logos[position];

            mTitleTv.setText(name);
            mLogoIv.setImageDrawable(getResources().getDrawable(logo));

        }
    }


}
