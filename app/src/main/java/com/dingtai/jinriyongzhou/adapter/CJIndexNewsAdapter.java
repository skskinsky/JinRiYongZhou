package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dingtai.base.imgdisplay.ImgTool;
import com.dingtai.base.newsHolder.BannerViewHolder1;
import com.dingtai.base.newsHolder.BannerViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder1;
import com.dingtai.base.newsHolder.NewsViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder3;
import com.dingtai.base.newsHolder.NewsViewHolder4;
import com.dingtai.base.newsHolder.PictureViewHolder1;
import com.dingtai.base.newsHolder.PictureViewHolder2;
import com.dingtai.base.newsHolder.ZhiBoViewHolder1;
import com.dingtai.base.newsHolder.ZhiBoViewHolder2;
import com.dingtai.base.utils.CommonSubscriptMethod;
import com.dingtai.base.utils.DateUtil;
import com.dingtai.base.utils.DensityUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.WutuHolder;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.BaseTextView;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.CJIndexNewsListModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CJIndexNewsAdapter extends BaseAdapter {

    private Context context;
    private List<CJIndexNewsListModel> list;
    private HashMap<Integer, Fragment> mPageReferenceMap;
    private FragmentManager fm;
    WindowManager windowManager;
    private int maxEms = 0;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;
    private DisplayImageOptions option;
    SharedPreferences mReadPreferences;
    private String str = "";
    private Typeface typeFace;
    private static final int GLOBAL_RADIUS = 5;
    private final static int MYLISTVIEW_HORIZONTAL_INTERVAL = 20;
    private final static int VIEWTYPE_8_HORIZONTAL_INTERVAL = 0;
    private final static double VIEWTYPE_8_IMG_RATE = 5.0 / 11;

    public CJIndexNewsAdapter(Context context, List<CJIndexNewsListModel> list,
                              FragmentManager paramFragmentManager) {
        try {
            mReadPreferences = context.getSharedPreferences("IsReadHome",
                    Context.MODE_PRIVATE);
            str = mReadPreferences.getString("isRead", "");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        this.context = context;
        this.list = list;
        mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
        this.fm = paramFragmentManager;
        this.mPageReferenceMap = new HashMap<Integer, Fragment>();
        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        option = MyImageLoader.option();
//        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/mysh.ttf");
    }

    public void setData(List<CJIndexNewsListModel> list) {
        this.list = list;
        // str = mReadPreferences.getString("isRead", "");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 重写获取所有样式总数
    @Override
    public int getViewTypeCount() {
        return 12;
    } // 重写获取视图缓存

    @Override
    public int getItemViewType(int position) {
        if (list != null && position < list.size()) {
            int cssType = 1;
            int ResourceCSS = 0;
            // 主标题，副标题，缩略图，图集,时间
            CJIndexNewsListModel listModel = list.get(position);
            String title = "", subTitle = "", smallImg = "", arrImg = "", zan = "", ping = "", time = "";
            title = listModel.getTitle();
            subTitle = listModel.getSummary();
            smallImg = listModel.getSmallPicUrl();
            try {
                ResourceCSS = Integer.parseInt(listModel.getResourceCSS());
                if (ResourceCSS == 0) {
                    ResourceCSS = 1;
                }
            } catch (Exception e) {
                ResourceCSS = 1;
            }
            switch (ResourceCSS) {
                case 1:
                    if (title.length() > 1 && subTitle.length() > 1
                            && smallImg.length() > 1) {
                        cssType = 1;
                    } else if (title.length() > 1 && smallImg.length() > 1
                            && subTitle.length() < 1) {
                        cssType = 2;
                    } else if (title.length() > 1 && subTitle.length() > 1) {
                        cssType = 3;
                    } else {
                        cssType = 4;
                    }
                    break;
                case 2:
                    if (title.length() > 1 && subTitle.length() > 1) {
                        cssType = 5;
                    } else {
                        cssType = 6;
                    }
                    break;
                case 3:
                    if (title.length() > 1 && subTitle.length() > 1) {
                        cssType = 7;
                    } else {
                        cssType = 8;
                    }
                    break;
                case 4:
                    // if (title.length() > 1 && subTitle.length() > 1) {
                    cssType = 9;
                    // } else {
                    // cssType = 10;
                    // }
                    break;
                case 5:
                    cssType = 1;
                    break;
                default:
                    cssType = 1;
                    break;
            }
            return cssType;
        }
        return super.getItemViewType(position);
    }

    public void destroyItem(View paramView, int paramInt, Object paramObject) {
        FragmentTransaction localFragmentTransaction = ((Fragment) paramObject)
                .getFragmentManager().beginTransaction();
        if (localFragmentTransaction != null) {
            localFragmentTransaction.remove((Fragment) paramObject);
            localFragmentTransaction.commitAllowingStateLoss();
        }
        if ((this.mPageReferenceMap != null)
                && (this.mPageReferenceMap.containsKey(Integer
                .valueOf(paramInt))))
            this.mPageReferenceMap.remove(Integer.valueOf(paramInt));
    }

    public void setFragments() {
        FragmentTransaction localFragmentTransaction = null;
        if ((this.mPageReferenceMap != null)
                && (this.mPageReferenceMap.size() > 0)) {
            localFragmentTransaction = this.fm.beginTransaction();
            if (localFragmentTransaction == null)
                ;
        }
        for (int i = 0; ; ++i) {
            if (i >= this.mPageReferenceMap.size()) {
                localFragmentTransaction.commitAllowingStateLoss();
                this.fm.executePendingTransactions();
                return;
            }
            Fragment localFragment = (Fragment) this.mPageReferenceMap
                    .get(Integer.valueOf(i));
            if (localFragment == null)
                continue;
            localFragmentTransaction.remove(localFragment);
        }
    }

    public void updateView(int position, View view,
                           CJIndexNewsListModel newsListModel) {
        String title = newsListModel.getTitle();
        if (view == null) {
            return;
        }
        Object obj = view.getTag();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO 首页数据绑定
        int CSSType = 0;
        int otherWidth = 0;
        int width = 0;
        int textSize = 0;

        // Log.i("news_chid","index"+
        // listModel.getChID()+"---"+position);
        // 主标题，副标题，缩略图，图集,时间,类别
        String title = "", subTitle = "", isread = "", smallImg = "", arrImg = "", zan = "", ping = "", time = "", type = "";
        // str = mReadPreferences.getString(listModel.getResourceGUID(),
        // "");
        CJIndexNewsListModel listModel = list.get(position);
        try {
            title = listModel.getTitle();
            subTitle = listModel.getSummary();
            smallImg = listModel.getSmallPicUrl();
            // arrImg = listModel.getUploadPicNames();
            // zan = listModel.getNewsGetGoodPoint();
            // ping = listModel.getCommentNum();
            zan = StringOper.getNumString(listModel.getNewsGetGoodPoint(), "");
            ping = StringOper.getNumString(listModel.getFakeReadNo(), "");
            time = listModel.getCreateTime();
            CSSType = getItemViewType(position);
            type = listModel.getResourceFlag();
            if ("2".equals(listModel.getResourceCSS())
                    && "1".equals(listModel.getResourceType())) {
                arrImg = listModel.getPicPath();
            } else {
                arrImg = listModel.getUploadPicNames();
            }
        } catch (Exception e) {
        }
        if (str.contains(listModel.getResourceGUID())) {
            isread = "true";
        } else {
            isread = "false";
        }
        if (!WutuSetting.getIsImg()) {

            if (CSSType == 9) {
                if ("True".equals(listModel.getEventIsPublic())) {
                    subTitle = "正在直播";
                } else {
                    subTitle = "直播结束";
                }
                title = listModel.getEventName();
                type = "5";
            }
            return WutuSetting.getView(context, convertView, title, subTitle,
                    type, listModel.getFakeReadNo(), list.get(position)
                            .getNewsGetGoodPoint());
        }
        try {
            WutuHolder wutuHolder = (WutuHolder) convertView.getTag();
            convertView = null;
        } catch (Exception e) {
        }
        String ChannelName = "";
        try {
            ChannelName = listModel.getChannelName();
            // if (ChannelName.length() > 12) {
            // ChannelName = StringOper.CutStringWithDot(12, ChannelName);
            // }
        } catch (Exception e) {
            // TODO: handle exception
        }
        // Log.i("test", "index" + position + ",CSSType:" + CSSType);
        switch (CSSType) {
            case 1:
                // 主,副标题，图都有
                NewsViewHolder1 newsViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_news_style1, null);
                    newsViewHolder1 = new NewsViewHolder1();
                    newsViewHolder1.setImgNewsPictureStyle1((ImageView) convertView
                            .findViewById(R.id.imgNewsPictureStyle1));
                    newsViewHolder1
                            .setTxtNewsTitleStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsTitleStyle1));
                    newsViewHolder1
                            .setTxtNewsSummaryStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSummaryStyle1));
                    newsViewHolder1.setTxtNewsZanStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle1));
                    newsViewHolder1
                            .setTxtNewsReviewStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsReviewStyle1));
                    newsViewHolder1
                            .setTxtNewsSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle1));
                    newsViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
                    newsViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle1);
                    convertView.setTag(newsViewHolder1);
                } else {
                    newsViewHolder1 = (NewsViewHolder1) convertView.getTag();
                }
                newsViewHolder1.getTxtNewsTitleStyle1().setTypeface(typeFace);
                newsViewHolder1.getTxtNewsTitleStyle1().setText(title);
                newsViewHolder1.getTxtNewsSummaryStyle1().setText(subTitle);
                ImageLoader.getInstance().displayImage(smallImg,
                        newsViewHolder1.getImgNewsPictureStyle1(), option,
                        mImageLoadingListenerImpl);
                if (ping.length() > 0 && !ping.equals("0")) {
                    newsViewHolder1.getTxtNewsReviewStyle1().setText(" " + ping);
                    newsViewHolder1.getTxtNewsReviewStyle1().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder1.getTxtNewsReviewStyle1().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder1.getTxtNewsZanStyle1().setVisibility(View.GONE);
                } else {
                    newsViewHolder1.getTxtNewsZanStyle1().setText(
                            DateUtil.formatDate(time));
                }
                // if (zan.length() > 0 && !zan.equals("0")) {
                // newsViewHolder1.getTxtNewsZanStyle1().setText(" " + zan);
                // newsViewHolder1.getTxtNewsZanStyle1().setVisibility(
                // View.VISIBLE);
                // } else {
                // // 赞数少于1不可见
                // newsViewHolder1.getTxtNewsZanStyle1().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder1.ChannelName.setText(ChannelName);
                    newsViewHolder1.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder1.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    // newsViewHolder1.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder1.imgPlay.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(newsViewHolder1,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 2:
                // 主,图都有,无副标题
                NewsViewHolder2 newsViewHolder2 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_news_style2, null);
                    newsViewHolder2 = new NewsViewHolder2();
                    newsViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
                    newsViewHolder2.setImgNewsPictureStyle2((ImageView) convertView
                            .findViewById(R.id.imgNewsPictureStyle2));
                    newsViewHolder2
                            .setTxtNewsTitleStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsTitleStyle2));
                    newsViewHolder2
                            .setTxtNewsReviewStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsReviewStyle2));
                    newsViewHolder2.setTxtNewsZanStyle2((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle2));
                    newsViewHolder2
                            .setTxtNewsSubscriptStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle2));
                    newsViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle2);
                    convertView.setTag(newsViewHolder2);
                } else {
                    newsViewHolder2 = (NewsViewHolder2) convertView.getTag();
                }
                ImageLoader.getInstance().displayImage(smallImg,
                        newsViewHolder2.getImgNewsPictureStyle2(), option,
                        mImageLoadingListenerImpl);
                // otherWidth = DisplayMetricsTool.dip2px(context, 150);
                // width = DisplayMetricsTool.getWidth(context);
                // textSize = DisplayMetricsTool.sp2px(context, 8);
                // maxEms = (width - otherWidth) / textSize;
                // StringOper.CutStringWithDot(maxEms, title)
                newsViewHolder2.getTxtNewsTitleStyle2().setTypeface(typeFace);
                newsViewHolder2.getTxtNewsTitleStyle2().setMaxLines(2);
                newsViewHolder2.getTxtNewsTitleStyle2().setText(title);
                if (ping.length() > 0 && !ping.equals("0")) {
                    newsViewHolder2.getTxtNewsReviewStyle2().setText(" " + ping);
                    newsViewHolder2.getTxtNewsReviewStyle2().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder2.getTxtNewsReviewStyle2().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder2.getTxtNewsZanStyle2().setVisibility(View.GONE);
                } else {
                    newsViewHolder2.getTxtNewsZanStyle2().setText(
                            DateUtil.formatDate(time));
                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder2.ChannelName.setText(ChannelName);
                    newsViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    // newsViewHolder2.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder2.imgPlay.setVisibility(View.GONE);
                }
                CommonSubscriptMethod.getNewsSubscript(newsViewHolder2,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 3:
                // 主,副标题都有
                NewsViewHolder4 newsViewHolder4 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_news_style4, null);
                    newsViewHolder4 = new NewsViewHolder4();
                    newsViewHolder4
                            .setTxtNewsTitleStyle4((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsTitleStyle4));
                    newsViewHolder4
                            .setTxtNewsSummaryStyle4((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSummaryStyle4));
                    newsViewHolder4
                            .setTxtNewsReviewStyle4((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsReviewStyle4));
                    newsViewHolder4.setTxtNewsZanStyle4((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle4));
                    newsViewHolder4
                            .setTxtNewsSubscriptStyle4((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle4));
                    newsViewHolder4.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle4);
                    convertView.setTag(newsViewHolder4);
                } else {
                    newsViewHolder4 = (NewsViewHolder4) convertView.getTag();
                }
                newsViewHolder4.getTxtNewsTitleStyle4().setTypeface(typeFace);
                newsViewHolder4.getTxtNewsTitleStyle4().setText(title);
                // otherWidth = DisplayMetricsTool.dip2px(context, 1);
                // width = DisplayMetricsTool.getWidth(context);
                // textSize = DisplayMetricsTool.sp2px(context, 8);
                // maxEms = (width - otherWidth) / textSize;
                newsViewHolder4.getTxtNewsSummaryStyle4().setText(subTitle);
                if (ping.length() > 0 && !ping.equals("0")) {
                    newsViewHolder4.getTxtNewsReviewStyle4().setText(" " + ping);
                    // newsViewHolder4.getTxtNewsReviewStyle4().setVisibility(
                    // View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder4.getTxtNewsReviewStyle4().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder4.getTxtNewsZanStyle4().setVisibility(View.GONE);
                } else {
                    newsViewHolder4.getTxtNewsZanStyle4().setText(
                            DateUtil.formatDate(time));
                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    // newsViewHolder4.ChannelName.setText(ChannelName);
                    // newsViewHolder4.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder4.ChannelName.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(newsViewHolder4,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 4:
                // 只有主标题
                NewsViewHolder3 newsViewHolder3 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_news_style3, null);
                    newsViewHolder3 = new NewsViewHolder3();
                    newsViewHolder3
                            .setTxtNewsTitleStyle3((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsTitleStyle3));
                    newsViewHolder3
                            .setTxtNewsReviewStyle3((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsReviewStyle3));
                    newsViewHolder3
                            .setTxtNewsSubscriptStyle3((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle3));
                    newsViewHolder3.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle3);
                    newsViewHolder3.setTxtNewsZanStyle3((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle3));
                    convertView.setTag(newsViewHolder3);
                } else {
                    newsViewHolder3 = (NewsViewHolder3) convertView.getTag();
                }
                newsViewHolder3.getTxtNewsTitleStyle3().setTypeface(typeFace);
                newsViewHolder3.getTxtNewsTitleStyle3().setMaxLines(2);
                newsViewHolder3.getTxtNewsTitleStyle3().setText(title);
                if (ping.length() > 0 && !ping.equals("0")) {
                    newsViewHolder3.getTxtNewsReviewStyle3().setText(" " + ping);
                    // newsViewHolder3.getTxtNewsReviewStyle3().setVisibility(
                    // View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder3.getTxtNewsReviewStyle3().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder3.getTxtNewsZanStyle3().setVisibility(View.GONE);
                } else {
                    newsViewHolder3.getTxtNewsZanStyle3().setText(
                            DateUtil.formatDate(time));
                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    // newsViewHolder3.ChannelName.setText(ChannelName);
                    // newsViewHolder3.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder3.ChannelName.setVisibility(View.GONE);
                }
                CommonSubscriptMethod.getNewsSubscript(newsViewHolder3,
                        listModel.getResourceFlag(), context);

                break;
            case 5:

                PictureViewHolder1 picutreViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_picture_style1, null);
                    picutreViewHolder1 = new PictureViewHolder1();
                    picutreViewHolder1
                            .setTxtPictureTitleStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureTitleStyle1));
                    picutreViewHolder1
                            .setImgPictureList1Style1((ImageView) convertView
                                    .findViewById(R.id.imgPictureList1Style1));
                    picutreViewHolder1
                            .setImgPictureList2Style1((ImageView) convertView
                                    .findViewById(R.id.imgPictureList2Style1));
                    picutreViewHolder1
                            .setImgPictureList3Style1((ImageView) convertView
                                    .findViewById(R.id.imgPictureList3Style1));
                    picutreViewHolder1
                            .setTxtPictureSummaryStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureSummaryStyle1));
                    picutreViewHolder1
                            .setTxtPictureReviewStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureReviewStyle1));
                    picutreViewHolder1
                            .setTxtPictureNumStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureNumStyle1));
                    picutreViewHolder1
                            .setTxtPictureZanStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureZanStyle1));
                    picutreViewHolder1
                            .setTxtPictureSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureSubscriptStyle1));
                    picutreViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle5);
                    convertView.setTag(picutreViewHolder1);
                } else {
                    picutreViewHolder1 = (PictureViewHolder1) convertView.getTag();
                }

                String imgURL[] = arrImg.split(",");
                width = DisplayMetricsTool.getWidth(context) - DisplayMetricsTool.dip2px(context, 14);
                double w = (width - 26) / 3.0;
                double h = w / 1.357142;
                int layout_w = (int) w;
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout.width = layout_w;
                layout.height = (int) h;
                picutreViewHolder1.getImgPictureList1Style1().setLayoutParams(
                        layout);

                RelativeLayout.LayoutParams layout2 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout2.leftMargin = 13 + layout_w;
                layout2.width = layout_w;
                layout2.height = (int) h;
                picutreViewHolder1.getImgPictureList2Style1().setLayoutParams(
                        layout2);

                RelativeLayout.LayoutParams layout3 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout3.leftMargin = 26 + 2 * layout_w;
                layout3.width = layout_w;
                layout3.height = (int) h;
                picutreViewHolder1.getImgPictureList3Style1().setLayoutParams(
                        layout3);
                picutreViewHolder1.getImgPictureList1Style1().setImageBitmap(null);
                picutreViewHolder1.getImgPictureList2Style1().setImageBitmap(null);
                picutreViewHolder1.getImgPictureList3Style1().setImageBitmap(null);
                // 获取图片
                try {
                    if (imgURL[0].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL[0],
                                picutreViewHolder1.getImgPictureList1Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL[1].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL[1],
                                picutreViewHolder1.getImgPictureList2Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL[2].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL[2],
                                picutreViewHolder1.getImgPictureList3Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                // listModel.getAppIndexTitle() 获取标题
                picutreViewHolder1.getTxtPictureTitleStyle1().setTypeface(typeFace);
                picutreViewHolder1.getTxtPictureTitleStyle1().setText(title);
                // listModel.getAppIndexSummary() 获取副标题
                // picutreViewHolder1.getTxtPictureSummaryStyle1().setText(listModel.getAppIndexSummary());
                otherWidth = DisplayMetricsTool.dip2px(context, 14);
                width = DisplayMetricsTool.getWidth(context);
                textSize = DisplayMetricsTool.sp2px(context, 7);

                maxEms = (width - otherWidth) / textSize;
                picutreViewHolder1.getTxtPictureSummaryStyle1().setText(
                        StringOper.CutStringWithDot(maxEms, subTitle));

                if (ping.length() > 0 && !ping.equals("0")) {
                    picutreViewHolder1.getTxtPictureReviewStyle1().setText(
                            " " + ping);
                    picutreViewHolder1.getTxtPictureReviewStyle1().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    picutreViewHolder1.getTxtPictureReviewStyle1().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    picutreViewHolder1.getTxtPictureZanStyle1().setVisibility(
                            View.GONE);
                } else {
                    picutreViewHolder1.getTxtPictureZanStyle1().setText(
                            DateUtil.formatDate(time));
                    picutreViewHolder1.getTxtPictureZanStyle1().setVisibility(
                            View.VISIBLE);
                }
                // if (arrImg.length() > 0) {
                picutreViewHolder1.getTxtPictureNumStyle1().setText(
                        arrImg.length() + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder1.getTxtPictureNumStyle1().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName)) {
                    picutreViewHolder1.ChannelName.setText(ChannelName);
                    picutreViewHolder1.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    picutreViewHolder1.ChannelName.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(picutreViewHolder1, list
                        .get(position).getResourceFlag(), context);
                break;
            case 6:
                PictureViewHolder2 picutreViewHolder2 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_picture_style2, null);
                    picutreViewHolder2 = new PictureViewHolder2();
                    picutreViewHolder2
                            .setTxtPictureTitleStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureTitleStyle2));
                    picutreViewHolder2
                            .setImgPictureList1Style2((ImageView) convertView
                                    .findViewById(R.id.imgPictureList1Style2));
                    picutreViewHolder2
                            .setImgPictureList2Style2((ImageView) convertView
                                    .findViewById(R.id.imgPictureList2Style2));
                    picutreViewHolder2
                            .setImgPictureList3Style2((ImageView) convertView
                                    .findViewById(R.id.imgPictureList3Style2));
                    picutreViewHolder2
                            .setTxtPictureReviewStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureReviewStyle2));
                    picutreViewHolder2
                            .setTxtPictureNumStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureNumStyle2));
                    picutreViewHolder2
                            .setTxtPictureZanStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureZanStyle2));
                    picutreViewHolder2
                            .setTxtPictureSubscriptStyle2((BaseTextView) convertView
                                    .findViewById(R.id.txtPictureSubscriptStyle2));
                    picutreViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle6);
                    convertView.setTag(picutreViewHolder2);
                } else {
                    picutreViewHolder2 = (PictureViewHolder2) convertView.getTag();
                }
                String imgURL2[] = arrImg.split(",");
                width = DisplayMetricsTool.getWidth(context) - DisplayMetricsTool.dip2px(context, 14);
                double w1 = (width - 26) / 3.0;
                double h1 = w1 / 1.357142;
                int layout_w1 = (int) w1;
                RelativeLayout.LayoutParams rlPic = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);

                rlPic.width = layout_w1;
                rlPic.height = (int) h1;
                picutreViewHolder2.getImgPictureList1Style2()
                        .setLayoutParams(rlPic);

                RelativeLayout.LayoutParams rlPic2 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic2.leftMargin = 13 + layout_w1;
                rlPic2.width = layout_w1;
                rlPic2.height = (int) h1;
                picutreViewHolder2.getImgPictureList2Style2().setLayoutParams(
                        rlPic2);

                RelativeLayout.LayoutParams rlPic3 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic3.leftMargin = 26 + 2 * layout_w1;
                rlPic3.width = layout_w1;
                rlPic3.height = (int) h1;
                picutreViewHolder2.getImgPictureList3Style2().setLayoutParams(
                        rlPic3);
                picutreViewHolder2.getImgPictureList1Style2().setImageBitmap(null);
                picutreViewHolder2.getImgPictureList2Style2().setImageBitmap(null);
                picutreViewHolder2.getImgPictureList3Style2().setImageBitmap(null);
                // 获取图片
                try {
                    if (imgURL2[0].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL2[0],
                                picutreViewHolder2.getImgPictureList1Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL2[1].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL2[1],
                                picutreViewHolder2.getImgPictureList2Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL2[2].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL2[2],
                                picutreViewHolder2.getImgPictureList3Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }

                // listModel.getAppIndexTitle() 获取标题
                picutreViewHolder2.getTxtPictureTitleStyle2().setTypeface(typeFace);
                picutreViewHolder2.getTxtPictureTitleStyle2().setText(title);

                if (ping.length() > 0 && !ping.equals("0")) {
                    picutreViewHolder2.getTxtPictureReviewStyle2().setText(
                            " " + ping);
                    picutreViewHolder2.getTxtPictureReviewStyle2().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    picutreViewHolder2.getTxtPictureReviewStyle2().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    picutreViewHolder2.getTxtPictureZanStyle2().setVisibility(
                            View.GONE);
                } else {
                    picutreViewHolder2.getTxtPictureZanStyle2().setText(" " +
                            DateUtil.formatDate(time));
                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder2.getTxtPictureNumStyle2().setText(arrImg.length()
                // + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder2.getTxtPictureNumStyle2().setVisibility(View.GONE);
                // }
                // 设置角标
                if (!TextUtils.isEmpty(ChannelName)) {
                    // picutreViewHolder2.ChannelName.setText(ChannelName);
                    // picutreViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    picutreViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                CommonSubscriptMethod.getNewsSubscript(picutreViewHolder2, list
                        .get(position).getResourceFlag(), context);
                break;
            case 7:
                // 主,副标题，图都有
                BannerViewHolder2 bannerViewHolder2 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hadapter_index_list_news_banner2, null);
                    bannerViewHolder2 = new BannerViewHolder2();
                    bannerViewHolder2.setImgBanner((ImageView) convertView
                            .findViewById(R.id.imgBanner));
                    bannerViewHolder2.setTvTitle((BaseTextView) convertView
                            .findViewById(R.id.tvTitle));
                    bannerViewHolder2.setTxtSummary((BaseTextView) convertView
                            .findViewById(R.id.txtSummary));
                    bannerViewHolder2.setTvReply((BaseTextView) convertView
                            .findViewById(R.id.tvReply));
                    bannerViewHolder2.setTvTime((BaseTextView) convertView
                            .findViewById(R.id.tvTime));
                    bannerViewHolder2.setTvZan((BaseTextView) convertView
                            .findViewById(R.id.tvZan));
                    bannerViewHolder2.setTvSubscript((BaseTextView) convertView
                            .findViewById(R.id.tvSubscript));
                    bannerViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle7);
                    bannerViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
                    convertView.setTag(bannerViewHolder2);
                } else {
                    bannerViewHolder2 = (BannerViewHolder2) convertView.getTag();
                }
                bannerViewHolder2.getTvTitle().setTypeface(typeFace);
                bannerViewHolder2.getTvTitle().setText(title);
                bannerViewHolder2.getTxtSummary().setText(subTitle);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.MATCH_PARENT
                                , (DisplayMetricsTool.getWidth(context) - DisplayMetricsTool.dip2px(context, 14)) / 2);
                bannerViewHolder2.getImgBanner().setLayoutParams(layoutParams);
                ImageLoader.getInstance().displayImage(smallImg,
                        bannerViewHolder2.getImgBanner(), option,
                        mImageLoadingListenerImpl);
                if (ping.length() > 0 && !ping.equals("0")) {
                    bannerViewHolder2.getTvReply().setText(" " + ping);
                    bannerViewHolder2.getTvReply().setVisibility(View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    bannerViewHolder2.getTvReply().setVisibility(View.GONE);
                }
                if (zan.length() > 0 && !zan.equals("0")) {
//                     bannerViewHolder2.getTvZan().setText(" " + zan);
//                    bannerViewHolder2.getTvZan().setVisibility(View.VISIBLE);
                } else {
                    // 赞数少于1不可见
                    bannerViewHolder2.getTvZan().setVisibility(View.GONE);
                }
//                if (time.length() > 0) {
//                    bannerViewHolder2.getTvTime()
//                            .setText(DateUtil.formatDate(time));
//                } else {
//                    bannerViewHolder2.getTvTime().setVisibility(View.GONE);
//                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    bannerViewHolder2.ChannelName.setText(ChannelName);
                    bannerViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    // bannerViewHolder2.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder2.imgPlay.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(bannerViewHolder2,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 8:
                // 主,图都有,无副标题
                BannerViewHolder1 bannerViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hadapter_index_list_news_banner1, null);
                    bannerViewHolder1 = new BannerViewHolder1();
                    bannerViewHolder1.setImgBanner((ImageView) convertView
                            .findViewById(R.id.imgBanner));
                    bannerViewHolder1.setTvTitle((BaseTextView) convertView
                            .findViewById(R.id.tvTitle));
                    bannerViewHolder1.setTvReply((BaseTextView) convertView
                            .findViewById(R.id.tvReply));
                    bannerViewHolder1.setTvTime((BaseTextView) convertView
                            .findViewById(R.id.tvTime));
                    bannerViewHolder1.setTvZan((BaseTextView) convertView
                            .findViewById(R.id.tvZan));
                    bannerViewHolder1.setTvSubscript((BaseTextView) convertView
                            .findViewById(R.id.tvSubscript));
                    bannerViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle8);
                    bannerViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
                    convertView.setTag(bannerViewHolder1);
                } else {
                    bannerViewHolder1 = (BannerViewHolder1) convertView.getTag();
                }
                bannerViewHolder1.getTvTitle().setTypeface(typeFace);
                bannerViewHolder1.getTvTitle().setText(title);
                bannerViewHolder1.getTvTitle().setMaxLines(2);
//                RelativeLayout.LayoutParams layoutParams1 =
//                        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
//                        , (DisplayMetricsTool.getWidth(context) - DisplayMetricsTool.dip2px(context, 14)) / 2);
                int viewType8ImgWidth = DisplayMetricsTool.getWidth(context) - DensityUtil.dip2px(context, MYLISTVIEW_HORIZONTAL_INTERVAL + VIEWTYPE_8_HORIZONTAL_INTERVAL);
                RelativeLayout.LayoutParams layoutParams1 =
                        new RelativeLayout.LayoutParams(viewType8ImgWidth
                                , (int) (viewType8ImgWidth * VIEWTYPE_8_IMG_RATE));
                bannerViewHolder1.getImgBanner().setLayoutParams(layoutParams1);
//                ImageLoader.getInstance().displayImage(smallImg,
//                        bannerViewHolder1.getImgBanner(), option,
//                        mImageLoadingListenerImpl);
                ImgTool.getInstance().loadRoundImg(smallImg, bannerViewHolder1.getImgBanner(), GLOBAL_RADIUS);
                if (ping.length() > 0 && !ping.equals("0")) {
                    bannerViewHolder1.getTvReply().setText(" " + ping);
                    bannerViewHolder1.getTvReply().setVisibility(View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    bannerViewHolder1.getTvReply().setVisibility(View.GONE);
                }
                if (zan.length() > 0 && !zan.equals("0")) {
                    // bannerViewHolder1.getTvZan().setText(" " + zan);
                    // bannerViewHolder1.getTvZan().setVisibility(View.VISIBLE);
                } else {
                    // 赞数少于1不可见
                    bannerViewHolder1.getTvZan().setVisibility(View.GONE);
                }
//                if (time.length() > 0 && !zan.equals("")) {
//                    bannerViewHolder1.getTvTime()
//                            .setText(DateUtil.formatDate(time));
//                    bannerViewHolder1.getTvTime().setVisibility(View.VISIBLE);
//                } else {
//                    bannerViewHolder1.getTvTime().setVisibility(View.GONE);
//                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    bannerViewHolder1.ChannelName.setText(ChannelName);
                    bannerViewHolder1.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder1.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    // bannerViewHolder1.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder1.imgPlay.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(bannerViewHolder1,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 9:
                // 主,副标题，图都有
                ZhiBoViewHolder2 zhiBoViewHolder2 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.adapter_index_list_news_zhibo2, null);
                    zhiBoViewHolder2 = new ZhiBoViewHolder2();
                    zhiBoViewHolder2.setImgBanner((ImageView) convertView
                            .findViewById(R.id.imgBanner));
                    zhiBoViewHolder2.setTvTitle((BaseTextView) convertView
                            .findViewById(R.id.tvTitle));
                    zhiBoViewHolder2.setTxtSummary((BaseTextView) convertView
                            .findViewById(R.id.txtSummary));
                    zhiBoViewHolder2.setTvZhiBoJiaoBiao((BaseTextView) convertView
                            .findViewById(R.id.tvZhiBoJiaoBiao));
                    convertView.setTag(zhiBoViewHolder2);
                } else {
                    zhiBoViewHolder2 = (ZhiBoViewHolder2) convertView.getTag();
                }
                if (!"True".equals(listModel.getEventIsPublic())) {
                    // 状态部不为直播时
                    zhiBoViewHolder2.getTvZhiBoJiaoBiao().setText("直播结束");
                    zhiBoViewHolder2.getTvZhiBoJiaoBiao().setTextColor(
                            Color.parseColor("#ADADAD"));
                    zhiBoViewHolder2.getTvZhiBoJiaoBiao().setBackgroundResource(
                            R.drawable.bac_huise);

                }
                String EventSurmmy = listModel.getEventSurmmy();
                if (EventSurmmy.length() > 0) {
                    // zhiBoViewHolder2.getTxtSummary().setText(EventSurmmy);
                    // zhiBoViewHolder2.getTxtSummary().setVisibility(View.VISIBLE);
                } else {
                    zhiBoViewHolder2.getTxtSummary().setVisibility(View.GONE);
                }

                zhiBoViewHolder2.getTvTitle().setTypeface(typeFace);
                zhiBoViewHolder2.getTvTitle().setText(listModel.getEventName());
                // zhiBoViewHolder2.getTxtSummary().setText(subTitle);
                ImageLoader.getInstance().displayImage(listModel.getEventLogo(),
                        zhiBoViewHolder2.getImgBanner(), option,
                        mImageLoadingListenerImpl);

                // 设置角标
                // CommonSubscriptMethod.getNewsSubscript(zhiBoViewHolder2,
                // listModel.getResourceFlag(), context); //
                // listModel.getResourceType()
                break;
            case 10:
                // 主,图都有,无副标题
                ZhiBoViewHolder1 zhiBoViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.adapter_index_list_news_zhibo1, null);
                    zhiBoViewHolder1 = new ZhiBoViewHolder1();
                    zhiBoViewHolder1.setImgBanner((ImageView) convertView
                            .findViewById(R.id.imgBanner));
                    zhiBoViewHolder1.setTvTitle((BaseTextView) convertView
                            .findViewById(R.id.tvTitle));
                    zhiBoViewHolder1.setTvZhiBoJiaoBiao((BaseTextView) convertView
                            .findViewById(R.id.tvZhiBoJiaoBiao));
                    convertView.setTag(zhiBoViewHolder1);
                } else {
                    zhiBoViewHolder1 = (ZhiBoViewHolder1) convertView.getTag();
                }
                zhiBoViewHolder1.getTvTitle().setTypeface(typeFace);
                zhiBoViewHolder1.getTvTitle().setText(listModel.getEventName());
                // zhiBoViewHolder1.getTvTitle().setMaxLines(2);
                ImageLoader.getInstance().displayImage(listModel.getEventLogo(),
                        zhiBoViewHolder1.getImgBanner(), option,
                        mImageLoadingListenerImpl);

                // 设置角标
                // CommonSubscriptMethod.getNewsSubscript(zhiBoViewHolder1,
                // listModel.getResourceFlag(), context); //
                // listModel.getResourceType()
                break;

            default:
                NewsViewHolder1 defaultStyle = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.hindex_news_style1, null);
                    defaultStyle = new NewsViewHolder1();
                    defaultStyle.setImgNewsPictureStyle1((ImageView) convertView
                            .findViewById(R.id.imgNewsPictureStyle1));
                    defaultStyle.setTxtNewsTitleStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsTitleStyle1));
                    defaultStyle.setTxtNewsSummaryStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsSummaryStyle1));
                    defaultStyle.setTxtNewsZanStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle1));
                    defaultStyle.setTxtNewsReviewStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsReviewStyle1));
                    defaultStyle
                            .setTxtNewsSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle1));
                    convertView.setTag(defaultStyle);
                } else {
                    defaultStyle = (NewsViewHolder1) convertView.getTag();
                }
                defaultStyle.getTxtNewsTitleStyle1().setTypeface(typeFace);
                defaultStyle.getTxtNewsTitleStyle1().setText(title);
                defaultStyle.getTxtNewsSummaryStyle1().setText(subTitle);
                ImageLoader.getInstance().displayImage(smallImg,
                        defaultStyle.getImgNewsPictureStyle1(), option,
                        mImageLoadingListenerImpl);
                if (ping.length() > 0 && !ping.equals("0")) {
                    defaultStyle.getTxtNewsReviewStyle1().setText(" " + ping);
                    // defaultStyle.getTxtNewsReviewStyle1().setVisibility(
                    // View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    defaultStyle.getTxtNewsReviewStyle1().setVisibility(View.GONE);
                }
                if (zan.length() > 0 && !zan.equals("0")) {
                    // defaultStyle.getTxtNewsZanStyle1().setText(" " + zan);
                    // defaultStyle.getTxtNewsZanStyle1().setVisibility(View.GONE);
                } else {
                    // 赞数少于1不可见
                    defaultStyle.getTxtNewsZanStyle1().setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(defaultStyle,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
        }

        return convertView;
    }

    // 监听图片异步加载
    public static class ImageLoadingListenerImpl extends
            SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imageView = (ImageView) view;
                // boolean isFirstDisplay = !displayedImages.contains(imageUri);
                // if (isFirstDisplay) {
                // 图片的淡入效果
                FadeInBitmapDisplayer.animate(imageView, 500);
                displayedImages.add(imageUri);
                // }
            }
        }
    }

}
