package com.dingtai.jinriyongzhou.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.application.MyApplication;
import com.dingtai.base.imgdisplay.ImgTool;
import com.dingtai.base.model.ADModel;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.ChooseLanmu;
import com.dingtai.base.view.MyListView;
import com.dingtai.base.view.VerticalScrollTextView;
import com.dingtai.dtpolitics.activity.AskQuestionActivity;
import com.dingtai.dtpolitics.activity.WenZhengDetailActivity;
import com.dingtai.dtpolitics.adapter.WenZhengAdapter;
import com.dingtai.dtpolitics.model.PoliticsADIndexModel;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.dtpolitics.model.WenZhengModel;
import com.dingtai.dtpolitics.service.WenZhengAPI;
import com.dingtai.dtpolitics.service.WenZhengHttpService;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.NewTopiceActivity;
import com.dingtai.jinriyongzhou.adapter.HallGridviewAdapter;
import com.dingtai.newslib3.adapter.NewsAdapter;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 陈籽屹
 * @时间 2016年5月9日 大厅首页
 */
public class HallFragment1 extends BaseFragment implements OnClickListener {

    private View mHallfragment;
    private boolean downup = false;// 判断上拉下拉
    private String state = "";// 判断上拉下拉状态
    private PullToRefreshScrollView mPullRefreshScrollView;// （再按一次退出程序）

    private int fontType;// 字体类型
    // 记录获取数据的总数量
    private static int countGetDataNum = 0;
    // 广告处理 By李亚军
    SharedPreferences sp;// 广告随机数处理
    private View adView;// list的头部
    private MyListView lvAD; // 广告列表
    //    private MyAdGallery adgallery; // 广告控件
//    private RelativeLayout adLayout;// 广告布局
//    private LinearLayout ovalLayout; // 圆点容器
    private String[] adImageURL;// 广告图片url
    private String[] adTitle;// 广告标题字符串
    //    private TextView txtADTitle;// 广告标题控件
    String LinkTo = "";// 广告链接到
    String LinkUrl = "";// 广告链接URL

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments;
    RelativeLayout ad_news;
    private boolean onlyOne = true;
    /**
     * 选中的颜色
     */
    private int selectColor;
    private String unselectColor = "#000000";

    private RecyclerView mGovernmentRv;
    /**
     * 大厅中间8个按钮适配器
     */
    private HallGridviewAdapter halladapter;
//	private MyFragmentAdapter myFragmentAdapter;
    /**
     * 区域集合
     */
    ArrayList<PoliticsAreaModel> politicsAreaModelList;
    RuntimeExceptionDao<PoliticsAreaModel, String> politicsAreaModel;

    List<PoliticsADIndexModel> politicsADIndexList;
    RuntimeExceptionDao<PoliticsADIndexModel, String> politicsADIndexModel;

    private ArrayList<ADModel> adCacheData;
    private boolean datatrue = false;
    private String ChID = "";

    private LinearLayout ad_text;
    MyListView lv_lastessay;  //最新新闻
    MyListView lv_lastquestion;// 最新提问
    private WenZhengAdapter wenZhengAdapter;
    private NewsAdapter essayAdapter;
    private ArrayList<NewsListModel> politicsNewsList;
    private ArrayList<WenZhengModel> politicsInfoList;
    private ImageView iv_goto;
    private ImageView iv_last_question;
    /**
     * 滚动字幕
     */
    private VerticalScrollTextView scroll_text;
    private LinearLayout mOperateLl;
    private ImageView mOperateIv;
    private boolean isMore = true;
    /**
     * 检测网络状态
     */
    private TextView net_net;

    ArrayList<String> textStr;
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 10:
//                    Toast.makeText(getActivity(), (CharSequence) msg.obj,
//                            Toast.LENGTH_SHORT).show();
                    mPullRefreshScrollView.onRefreshComplete();
                    downup = false;
                    break;
                case 500:
                    ArrayList list = (ArrayList) msg.getData()
                            .getParcelableArrayList("list").get(0);

                    if (list != null && list.size() > 0) {
                        ArrayList<PoliticsAreaModel> tempList = (ArrayList<PoliticsAreaModel>) list.get(1);
                        if (tempList != null && tempList.size() > 0) {
                            if (politicsAreaModelList == null) {
                                politicsAreaModelList = new ArrayList<>();
                            } else {
                                politicsAreaModelList.clear();

                            }
                            politicsAreaModelList.addAll(tempList);
                            if (politicsAreaModelList.size() > MAX_GOVERNMENT) {
                                mOperateLl.setVisibility(View.VISIBLE);
                            } else {
                                mOperateLl.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();
                        }


                        // 最新提问
                        politicsInfoList.clear();
                        ArrayList<WenZhengModel> tempList1 = (ArrayList<WenZhengModel>) list.get(2);
                        if (tempList1 != null && tempList.size() > 0) {
                            if (politicsInfoList == null) {
                                politicsInfoList = new ArrayList<>();
                            } else {
                                politicsInfoList.clear();
                            }
                            politicsInfoList.addAll(tempList1);
                            wenZhengAdapter = new WenZhengAdapter(getActivity(),
                                    politicsInfoList);
                            lv_lastquestion.setAdapter(wenZhengAdapter);
                            wenZhengAdapter.notifyDataSetChanged();

                        }

                        // 最新文章
                        ArrayList<NewsListModel> tempList2 = (ArrayList<NewsListModel>) list.get(3);
                        if (tempList2 != null && tempList2.size() > 0) {
                            if (politicsNewsList == null) {
                                politicsNewsList = new ArrayList<>();
                            } else {
                                politicsNewsList.clear();
                            }
                            politicsNewsList.addAll(tempList2);
                            if (politicsNewsList.size() > 0) {
                                iv_last_question.setVisibility(View.VISIBLE);
                            } else {
                                iv_last_question.setVisibility(View.GONE);  
                            }
                            essayAdapter = new NewsAdapter(getActivity(),
                                    politicsNewsList, getFragmentManager());
                            lv_lastessay.setAdapter(essayAdapter);
                            wenZhengAdapter.notifyDataSetChanged();
                        }

                        // 广告
                        politicsADIndexList = (List<PoliticsADIndexModel>) list
                                .get(0);
                        if (politicsADIndexList != null && politicsADIndexList.size() > 0) {
//                            adLayout.setVisibility(RelativeLayout.VISIBLE);
                            adImageURL = new String[politicsADIndexList.size()];
                            adTitle = new String[politicsADIndexList.size()];
                            datatrue = true;

                            for (int i = 0; i < politicsADIndexList.size(); i++) {
                                textStr.add(politicsADIndexList.get(i).getADName());
                                adImageURL[i] =
                                        politicsADIndexList.get(i).getImgUrl();
                                adTitle[i] = politicsADIndexList.get(i).getADName();
                            }
                            if (textStr.size() > 0) {
                                mHallfragment.findViewById(R.id.ad_text).setVisibility(View.VISIBLE);
                                scroll_text.setData(textStr);
                                scroll_text
                                        .setTextOnItemClick(new VerticalScrollTextView.OnTextItemClickListener() {

                                            @Override
                                            public void onItemClick(int position) {
                                                PoliticsADIndexModel politicsADIndex = politicsADIndexList
                                                        .get(position);
                                                String LinkTo = politicsADIndex.getLinkTo();
                                                String LinkUrl = politicsADIndex
                                                        .getLinkUrl();
                                                String chid = politicsADIndex.getChID();
                                                clickAd(chid, LinkTo, LinkUrl);

                                            }
                                        });

                            } else {
                                scroll_text.setVisibility(View.GONE);
                                mHallfragment.findViewById(R.id.ad_text).setVisibility(View.GONE);
                            }
                        }
                    }
                    mPullRefreshScrollView.onRefreshComplete();
                    break;

                default:
                    break;
            }
        }

        ;
    };
    private MyAdapter mAdapter;
    private static final int MAX_GOVERNMENT = 8;

    private void clickAd(String chid, String linkTo, String linkUrl) {
        try {
            if (linkTo.equals("1")) {// 1、详情2、 列表
                String[] LinkUrls = linkUrl.split(",");
                if (LinkUrls.length > 1) {
                    String strTempID = LinkUrls[0];
                    String strTempGUID = LinkUrls[1];
                    Intent intent = new Intent();
                    intent.setAction(basePackage + "NewsDetail");
                    intent.putExtra("ID", strTempGUID);
                    intent.putExtra("ResourceType", strTempID);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                }
            } else {
                Intent intent = new Intent();
                intent.setAction(basePackage + "newsList");
                intent.putExtra("lanmuChID", chid);
                intent.putExtra("ChannelName", "新闻列表");
                startActivity(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        mHallfragment = inflater.inflate(R.layout.fragment_hall1, container,
                false);
        iniView();
        return mHallfragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (onlyOne) {
            // initFragment();
            onlyOne = false;
        }
    }

    private void iniView() {
        // TODO 自动生成的方法存根
        mPullRefreshScrollView = (PullToRefreshScrollView) mHallfragment
                .findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(getActivity()));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
        if (MyApplication.RefreshVersion == 2) {
            mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this.getActivity()));
            mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

            // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
            // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
            // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
            mPullRefreshScrollView.setHasPullUpFriction(true); // 设置没有上拉阻力
        }
        // selectColor = getResources().getColor(R.color.common_color);
        if (textStr == null) {
            textStr = new ArrayList<String>();
        }

        politicsInfoList = new ArrayList<>();
        net_net = (TextView) mHallfragment.findViewById(R.id.net_net);
        Assistant.setNetworkState(getActivity(), net_net, mHandler);
        mGovernmentRv = (RecyclerView) mHallfragment.findViewById(R.id.mGovernmentRv);
        mGovernmentRv.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);

        mGovernmentRv.setLayoutManager(gridLayoutManager);
        mAdapter = new MyAdapter();
//        mContentRv.addItemDecoration(new DividerItemDecoration(getActivity(), 2, 1, getResources().getColor(R.color.dt_list_item_underline)));
//        mContentRv.addItemDecoration(new GridDivider(getActivity(), 1, this.getResources().getColor(R.color.dt_list_item_underline)));

        mGovernmentRv.setAdapter(mAdapter);
        mOperateLl = (LinearLayout) mHallfragment.findViewById(R.id.mOperateLl);
        mOperateIv = (ImageView) mHallfragment.findViewById(R.id.mOperateIv);
        mOperateLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isMore = !isMore;
                setMoreStatus();
                mAdapter.notifyDataSetChanged();
            }
        });
        setMoreStatus();

        fontType = MyApplication.FONTTYPE;
        ad_news = (RelativeLayout) mHallfragment.findViewById(R.id.ad_news);
        ad_news.setVisibility(View.GONE);
        lv_lastessay = (MyListView) mHallfragment
                .findViewById(R.id.lv_lastessay);
        lv_lastquestion = (MyListView) mHallfragment
                .findViewById(R.id.lv_lastquestion);
        if (Assistant.IsContectInterNet2(getActivity())) {
            getPoliticIndexInfomation();
        } else {
            Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT).show();
        }
        scroll_text = (VerticalScrollTextView) mHallfragment
                .findViewById(R.id.scroll_text);
        scroll_text.setTextSize(16);

        lv_lastessay.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                ChooseLanmu.toLanmu(getActivity(),
                        politicsNewsList.get(position));
            }
        });
        lv_lastquestion.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO 自动生成的方法存根
                Intent intent = new Intent(getActivity(),
                        WenZhengDetailActivity.class);
                intent.putExtra("ID", politicsInfoList.get(position).getID());
//                intent.putExtra("FLAG", "0");
                startActivity(intent);
            }
        });
        mPullRefreshScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {


                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        // TODO 自动生成的方法存根
                        if (Assistant.IsContectInterNet2(getActivity())) {
                            getPoliticIndexInfomation();
                        } else {
                            Toast.makeText(getActivity(), "请检查网络连接",
                                    Toast.LENGTH_SHORT).show();
                            mPullRefreshScrollView.onRefreshComplete();
                        }
                    }
                });
        initADView();// 初始化头部广告信息
        iv_goto = (ImageView) mHallfragment.findViewById(R.id.iv_goto);
        iv_last_question = (ImageView) mHallfragment.findViewById(R.id.iv_last_question);
        iv_goto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (Assistant.getUserInfoByOrm(getActivity()) == null) {
                    Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setAction(getActivity().getPackageName() + "." + "login");
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity().getApplicationContext(), AskQuestionActivity.class);
                    intent.putExtra("cityName", "永州市");
                    intent.putExtra("cityParentId", "1");
                    startActivity(intent);
                }
            }

        });
    }

    private void setMoreStatus() {
        if (isMore) {
            mOperateIv.setImageDrawable(getResources().getDrawable(R.drawable.dt_standard_zhengwu_gengduo));
        } else {
            mOperateIv.setImageDrawable(getResources().getDrawable(R.drawable.dt_standard_zhengwu_shouqi));
        }
    }


    /**
     * TODO 初始化广告控件
     */
    public void initADView() {
//        adLayout = (RelativeLayout) mHallfragment.findViewById( R.id.ad_news);
//        adgallery = (MyAdGallery) mHallfragment.findViewById( R.id.adgallery); // 获取Gallery组件
//        ovalLayout = (LinearLayout) mHallfragment.findViewById( R.id.ovalLayout);//
//        // 获取圆点容器
//        txtADTitle = (TextView) mHallfragment.findViewById( R.id.adtitle);
//        adLayout.setVisibility(View.GONE);
    }

//	class MyFragmentAdapter extends FragmentPagerAdapter {
//
//		public MyFragmentAdapter(FragmentManager fm) {
//			super(fm);
//		}
//
//		@Override
//		public Fragment getItem(int arg0) {
//			// TODO 自动生成的方法存根
//			return fragments.get(arg0);
//		}
//
//		@Override
//		public int getCount() {
//			// TODO 自动生成的方法存根
//			return fragments.size();
//		}
//
//	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }


    /**
     * 获取首页数据
     */
    public void getPoliticIndexInfomation() {
        String url = API.COMMON_URL + "Interface/PoliticsAPI.ashx?action=GetPoliticsIndex";
        String StID = API.STID;
        get_politicsIndex_info(getActivity(), url, StID,
                new Messenger(mHandler));

    }

    /**
     * 获取问政首页信息
     *
     * @param paramContext
     * @param url
     * @param StID
     */
    public void get_politicsIndex_info(Context paramContext, String url,
                                       String StID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, WenZhengHttpService.class);
        intent.putExtra("api", WenZhengAPI.POLITICS_INDEX_API);
        intent.putExtra(WenZhengAPI.POLITICS_INDEX_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        paramContext.startService(intent);
    }

//	@Override
//	public void onDestroy() {
//		Assistant.unRegisterNetwork(getActivity());
//		super.onDestroy();
//	}


    private class MyAdapter extends RecyclerView.Adapter<BaseHolder> {

        @Override
        public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return new SecondHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_paper_net, parent, false));
            return new DefaultHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_model1, parent, false));
        }

        @Override
        public void onBindViewHolder(BaseHolder holder, int position) {
            holder.onbind(position);
        }

        @Override
        public int getItemCount() {

            if (isMore) {


                return politicsAreaModelList == null ? 0 : (politicsAreaModelList.size() > MAX_GOVERNMENT ? MAX_GOVERNMENT : politicsAreaModelList.size());
            } else {
                return politicsAreaModelList == null ? 0 : politicsAreaModelList.size();
            }

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

            String DeptID = politicsAreaModelList.get(position)
                    .getAreaID();
            String aredIcon = politicsAreaModelList.get(position)
                    .getAreaSmallPicUrl();
            String aredNanme = politicsAreaModelList.get(position)
                    .getAreaPoliticsAreaName();
            String reMark = politicsAreaModelList.get(position)
                    .getAreaReMark();
            Intent intent = new Intent();
            intent.setClass(getActivity(),
                    NewTopiceActivity.class);
            intent.putExtra("DeptID", DeptID);
            intent.putExtra("reMark", reMark);
            intent.putExtra("aredIcon", aredIcon);
            intent.putExtra("aredNanme", aredNanme);
            startActivity(intent);

        }

        public void onbind(int position) {
            PoliticsAreaModel itemBean = politicsAreaModelList.get(position);
//            int w = (int) (DisplayMetricsTool.getWidth(getActivity()));
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                    w - DensityUtil.dip2px(getActivity(), PIC_INTERVAL),
//                    (int) (w * PIC_WIDTH_HEIGHT_RATE));
//
//            mLogoIv.setLayoutParams(layoutParams);


            ImgTool.getInstance().loadCirclelImg(itemBean.getAreaSmallPicUrl(), mLogoIv);
            mTitleTv.setText(itemBean.getAreaPoliticsAreaName());

        }
    }


}
