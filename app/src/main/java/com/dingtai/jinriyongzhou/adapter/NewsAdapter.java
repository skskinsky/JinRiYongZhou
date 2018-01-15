package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.newsHolder.BannerViewHolder1;
import com.dingtai.base.newsHolder.BannerViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder1;
import com.dingtai.base.newsHolder.NewsViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder3;
import com.dingtai.base.newsHolder.NewsViewHolder4;
import com.dingtai.base.newsHolder.PictureViewHolder1;
import com.dingtai.base.newsHolder.PictureViewHolder2;
import com.dingtai.base.utils.CommonSubscriptMethod;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.WutuHolder;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.BaseTextView;
import com.dingtai.jinriyongzhou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NewsAdapter extends BaseAdapter {
    private Context context;
    private List<NewsListModel> list;
    private HashMap<Integer, Fragment> mPageReferenceMap;
    private FragmentManager fm;


    WindowManager windowManager;
    private int maxEms = 0;
    private DisplayImageOptions option;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;
    Typeface typeFace;

    public NewsAdapter(Context context, List<NewsListModel> list,
                       FragmentManager paramFragmentManager) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
        this.fm = paramFragmentManager;
        this.mPageReferenceMap = new HashMap<Integer, Fragment>();

        windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
//        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/mysh.ttf");
        option = MyImageLoader.option();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }// 重写获取所有样式总数

    @Override
    public int getViewTypeCount() {
        return 12;
    } // 重写获取视图缓存

    @Override
    public int getItemViewType(int position) {
        if (list != null && position < list.size()) {
            int cssType = 0;
            int ResourceCSS = 0;
            NewsListModel listModel = list.get(position);
            // 主标题，副标题，缩略图，图集,时间
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
                    if (title.length() > 1 && subTitle.length() > 1) {
                        cssType = 9;
                    } else {
                        cssType = 10;
                    }
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
        // super.destroyItem(paramView, paramInt, paramObject);

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO 首页数据绑定
        int CSSType = 0;
        int otherWidth = 0;
        int width = 0;
        int textSize = 0;
        // 主标题，副标题，缩略图，图集,时间
        // Log.i("news_chid", listModel.getChID()+"---"+position);
        String title = "", subTitle = "", smallImg = "", arrImg = "", zan = "", ping = "", time = "", type = "";
        String readNo = "";
        NewsListModel listModel = list.get(position);
        try {
            title = listModel.getTitle();
            subTitle = listModel.getSummary();
            smallImg = listModel.getSmallPicUrl();
            if ("2".equals(listModel.getResourceCSS())
                    && "1".equals(listModel.getResourceType())) {
                arrImg = listModel.getPicPath();
            } else {
                arrImg = listModel.getUploadPicNames();
            }
            // zan = listModel.getGetGoodPoint();
            ping = listModel.getCommentNum();
            zan = StringOper.getNumString(listModel.getGetGoodPoint(), "");
            readNo = StringOper.getNumString(listModel.getFakeReadNo(), "");
            time = listModel.getCreateTime();
            CSSType = getItemViewType(position);
            type = listModel.getResourceFlag();
        } catch (Exception e) {
        }
        if (!WutuSetting.getIsImg()) {
            return WutuSetting.getView(context, convertView, title, subTitle,
                    type, listModel.getFakeReadNo(), list.get(position)
                            .getGetGoodPoint());
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
        // if (position == 2 && CSSType == 5)
        // Log.i("test", "news" + ",title:" + title + list.size() + "/" +
        // position + ",CSSType:" + CSSType);
        switch (CSSType) {
            case 1:
                // 主,副标题，图都有
                NewsViewHolder1 newsViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.index_news_style1, null);
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
                    newsViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle1);
                    newsViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
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
//                if (ping.length() > 0 && !ping.equals("0")) {
//                    newsViewHolder1.getTxtNewsReviewStyle1().setText(" " + ping);
//                    newsViewHolder1.getTxtNewsReviewStyle1().setVisibility(
//                            View.VISIBLE);
//                } else {
//                    // 评论数少于1不可见
//                    newsViewHolder1.getTxtNewsReviewStyle1().setVisibility(
//                            View.GONE);
//                }
                if (TextUtils.isEmpty(readNo)) {
                    newsViewHolder1.getTxtNewsZanStyle1().setVisibility(View.GONE);
                } else {
                    newsViewHolder1.getTxtNewsZanStyle1().setText(
                            readNo);
                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder1.ChannelName.setText(ChannelName);
                    newsViewHolder1.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder1.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    newsViewHolder1.imgPlay.setVisibility(View.GONE);
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
                            R.layout.index_news_style2, null);
                    newsViewHolder2 = new NewsViewHolder2();

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
                    newsViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
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

                newsViewHolder2.getTxtNewsReviewStyle2().setText(ping);


                newsViewHolder2.getTxtNewsZanStyle2().setText(
                        listModel.getFakeReadNo());

                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder2.ChannelName.setText(ChannelName);
                    newsViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    newsViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    newsViewHolder2.imgPlay.setVisibility(View.GONE);
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
                            R.layout.index_news_style4, null);
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
                    newsViewHolder4.getTxtNewsReviewStyle4().setVisibility(
                            View.GONE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder4.getTxtNewsReviewStyle4().setVisibility(
                            View.GONE);
                }
//                if (TextUtils.isEmpty(time)) {
//                    newsViewHolder4.getTxtNewsZanStyle4().setVisibility(View.GONE);
//                } else {
//                    newsViewHolder4.getTxtNewsZanStyle4().setText(
//                            DateUtil.formatDate(time));
//                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder4.ChannelName.setText(ChannelName);
                    newsViewHolder4.ChannelName.setVisibility(View.VISIBLE);
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
                            R.layout.index_news_style3, null);
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
                    newsViewHolder3.setTxtNewsZanStyle3((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle3));
                    newsViewHolder3.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle3);
                    convertView.setTag(newsViewHolder3);
                } else {
                    newsViewHolder3 = (NewsViewHolder3) convertView.getTag();
                }
                newsViewHolder3.getTxtNewsTitleStyle3().setMaxLines(2);
                newsViewHolder3.getTxtNewsTitleStyle3().setTypeface(typeFace);
                newsViewHolder3.getTxtNewsTitleStyle3().setText(title);
                if (ping.length() > 0 && !ping.equals("0")) {
                    newsViewHolder3.getTxtNewsReviewStyle3().setText(" " + ping);
                    newsViewHolder3.getTxtNewsReviewStyle3().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    newsViewHolder3.getTxtNewsReviewStyle3().setVisibility(
                            View.GONE);
                }
//                if (TextUtils.isEmpty(time)) {
//                    newsViewHolder3.getTxtNewsZanStyle3().setVisibility(View.GONE);
//                } else {
//                    newsViewHolder3.getTxtNewsZanStyle3().setText(
//                            DateUtil.formatDate(time));
//                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    newsViewHolder3.ChannelName.setText(ChannelName);
                    newsViewHolder3.ChannelName.setVisibility(View.VISIBLE);
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
                            R.layout.index_picture_style1, null);
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
                width = DisplayMetricsTool.getWidth(context)
                        - DisplayMetricsTool.dip2px(context, 14);
                double w = (width - 20) / 3.0;
                double h = w * 9 / 16;
                int layout_w = (int) w;
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout.leftMargin = 0;
                layout.width = layout_w;
                layout.height = (int) h;

                RelativeLayout.LayoutParams layout2 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout2.leftMargin = 10 + layout_w;
                layout2.width = layout_w;
                layout2.height = (int) h;


                RelativeLayout.LayoutParams layout3 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout3.leftMargin = 20 + 2 * layout_w;
                layout3.width = layout_w;
                layout3.height = (int) h;

                picutreViewHolder1.getImgPictureList1Style1().setImageBitmap(null);
                picutreViewHolder1.getImgPictureList2Style1().setImageBitmap(null);
                picutreViewHolder1.getImgPictureList3Style1().setImageBitmap(null);
                // 获取图片
                try {
                    if (imgURL[0].length() > 0) {
                        picutreViewHolder1.getImgPictureList1Style1().setLayoutParams(
                                layout);
                        ImageLoader.getInstance().displayImage(imgURL[0],
                                picutreViewHolder1.getImgPictureList1Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    if (imgURL[1].length() > 0) {
                        picutreViewHolder1.getImgPictureList2Style1().setLayoutParams(
                                layout2);


                        ImageLoader.getInstance().displayImage(imgURL[1],
                                picutreViewHolder1.getImgPictureList2Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                try {
                    if (imgURL[2].length() > 0) {
                        picutreViewHolder1.getImgPictureList3Style1().setLayoutParams(
                                layout3);
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
                if (TextUtils.isEmpty(readNo)) {
                    picutreViewHolder1.getTxtPictureZanStyle1().setVisibility(
                            View.GONE);
                } else {
                    picutreViewHolder1.getTxtPictureZanStyle1().setText(
                            readNo);
                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder1.getTxtPictureNumStyle1().setText(arrImg.length()
                // + "图");
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
                            R.layout.index_picture_style2, null);
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
                width = DisplayMetricsTool.getWidth(context)
                        - DisplayMetricsTool.dip2px(context, 14);
                double w1 = (width - 20) / 3.0;
                double h1 = w1 * 9 / 16;
                int layout_w1 = (int) w1;
                RelativeLayout.LayoutParams rlPic = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic.leftMargin = 0;
                rlPic.width = layout_w1;
                rlPic.height = (int) h1;


                RelativeLayout.LayoutParams rlPic2 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic2.leftMargin = 10 + layout_w1;
                rlPic2.width = layout_w1;
                rlPic2.height = (int) h1;


                RelativeLayout.LayoutParams rlPic3 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic3.leftMargin = 20 + 2 * layout_w1;
                rlPic3.width = layout_w1;
                rlPic3.height = (int) h1;

                picutreViewHolder2.getImgPictureList1Style2().setImageBitmap(null);
                picutreViewHolder2.getImgPictureList2Style2().setImageBitmap(null);
                picutreViewHolder2.getImgPictureList3Style2().setImageBitmap(null);
                // 获取图片
                try {
                    if (imgURL2[0].length() > 0) {
                        picutreViewHolder2.getImgPictureList1Style2()
                                .setLayoutParams(rlPic);
                        ImageLoader.getInstance().displayImage(imgURL2[0],
                                picutreViewHolder2.getImgPictureList1Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL2[1].length() > 0) {
                        picutreViewHolder2.getImgPictureList2Style2().setLayoutParams(
                                rlPic2);
                        ImageLoader.getInstance().displayImage(imgURL2[1],
                                picutreViewHolder2.getImgPictureList2Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL2[2].length() > 0) {
                        picutreViewHolder2.getImgPictureList3Style2().setLayoutParams(
                                rlPic3);
                        ImageLoader.getInstance().displayImage(imgURL2[2],
                                picutreViewHolder2.getImgPictureList3Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }

                // listModel.getAppIndexTitle() 获取标题
                picutreViewHolder2.getTxtPictureTitleStyle2().setTypeface(typeFace);
                picutreViewHolder2.getTxtPictureTitleStyle2().setText(title);

                if (readNo.length() > 0 && !readNo.equals("0")) {
                    picutreViewHolder2.getTxtPictureReviewStyle2().setText(
                            " " + readNo);
                    picutreViewHolder2.getTxtPictureReviewStyle2().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    picutreViewHolder2.getTxtPictureReviewStyle2().setVisibility(
                            View.GONE);
                }
//                if (TextUtils.isEmpty(time)) {
//                    picutreViewHolder2.getTxtPictureZanStyle2().setVisibility(
//                            View.GONE);
//                } else {
//                    picutreViewHolder2.getTxtPictureZanStyle2().setText(
//                            DateUtil.formatDate(time));
//                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder2.getTxtPictureNumStyle2().setText(arrImg.length()
                // + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder2.getTxtPictureNumStyle2().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName)) {
                    picutreViewHolder2.ChannelName.setText(ChannelName);
                    picutreViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    picutreViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(picutreViewHolder2, list
                        .get(position).getResourceFlag(), context);
                break;
            case 7:
                // 主,副标题，图都有
                BannerViewHolder2 bannerViewHolder2 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.adapter_index_list_news_banner2, null);
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
                    bannerViewHolder2.setTvCommunity((BaseTextView) convertView
                            .findViewById(R.id.tvCommunity));
                    bannerViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle7);
                    bannerViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
//
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                            DisplayMetricsTool.getWidth(context),
//                            (int) ((DisplayMetricsTool.getWidth(context) - 40) * 9 / 16));
//
//                    bannerViewHolder2.getImgBanner().setLayoutParams(layoutParams);
//
//
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16 / 5));
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    bannerViewHolder2.getTvTitle().setLayoutParams(layoutParams1);


                    convertView.setTag(bannerViewHolder2);
                } else {
                    bannerViewHolder2 = (BannerViewHolder2) convertView.getTag();
                }
                bannerViewHolder2.getTvTitle().setTypeface(typeFace);
                bannerViewHolder2.getTvTitle().setText(title);
                bannerViewHolder2.getTxtSummary().setText(subTitle);

                ImageLoader.getInstance().displayImage(smallImg,
                        bannerViewHolder2.getImgBanner(), option,
                        mImageLoadingListenerImpl);

                bannerViewHolder2.getTvReply().setText(" " + ping);
                bannerViewHolder2.getTvReply().setVisibility(View.VISIBLE);

                // if (zan.length() > 0 && !zan.equals("0")) {
                // bannerViewHolder2.getTvZan().setText(" "+zan);
                // bannerViewHolder2.getTvZan().setVisibility(View.VISIBLE);
                // } else {
                // // 赞数少于1不可见
                // bannerViewHolder2.getTvZan().setVisibility(View.GONE);
                // }

                bannerViewHolder2.getTvTime()
                        .setText(readNo);
                bannerViewHolder2.getTvTime().setVisibility(View.VISIBLE);

                if (!TextUtils.isEmpty(ChannelName)) {
                    bannerViewHolder2.ChannelName.setText(ChannelName);
                    bannerViewHolder2.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    bannerViewHolder2.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder2.imgPlay.setVisibility(View.GONE);
                }

                if (listModel.getCommunityName() != null && !listModel.getCommunityName().equals("")) {
                    bannerViewHolder2.getTvCommunity().setText(listModel.getCommunityName());
                    bannerViewHolder2.getTvCommunity().setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder2.getTvCommunity().setVisibility(View.GONE);
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
                            R.layout.adapter_index_list_news_banner1, null);
                    bannerViewHolder1 = new BannerViewHolder1();
                    bannerViewHolder1.setImgBanner((ImageView) convertView
                            .findViewById(R.id.imgBanner));
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16));

                    bannerViewHolder1.getImgBanner().setLayoutParams(layoutParams1);

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
                    bannerViewHolder1.setTvCommunity((BaseTextView) convertView
                            .findViewById(R.id.tvCommunity));
                    bannerViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle8);
                    bannerViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
                    convertView.setTag(bannerViewHolder1);

                    RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16 / 5));

                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    bannerViewHolder1.getTvTitle().setLayoutParams(layoutParams2);

                } else {
                    bannerViewHolder1 = (BannerViewHolder1) convertView.getTag();
                }
                bannerViewHolder1.getTvTitle().setTypeface(typeFace);
                bannerViewHolder1.getTvTitle().setText(title);
                bannerViewHolder1.getTvTitle().setMaxLines(2);

                ImageLoader.getInstance().displayImage(smallImg,
                        bannerViewHolder1.getImgBanner(), option,
                        mImageLoadingListenerImpl);

                bannerViewHolder1.getTvReply().setText(" " + ping);
                bannerViewHolder1.getTvReply().setVisibility(View.VISIBLE);

                // if (zan.length() > 0 && !zan.equals("0")) {
                // bannerViewHolder1.getTvZan().setText(" "+zan);
                // bannerViewHolder1.getTvZan().setVisibility(View.VISIBLE);
                // } else {
                // // 赞数少于1不可见
                // bannerViewHolder1.getTvZan().setVisibility(View.GONE);
                // }

                bannerViewHolder1.getTvTime()
                        .setText(readNo);
                bannerViewHolder1.getTvTime().setVisibility(View.VISIBLE);


                if (!TextUtils.isEmpty(ChannelName)) {
                    bannerViewHolder1.ChannelName.setText(ChannelName);
                    bannerViewHolder1.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder1.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    bannerViewHolder1.imgPlay.setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder1.imgPlay.setVisibility(View.GONE);
                }

                if (listModel.getCommunityName() != null && !listModel.getCommunityName().equals("")) {
                    bannerViewHolder1.getTvCommunity().setText(listModel.getCommunityName());
                    bannerViewHolder1.getTvCommunity().setVisibility(View.VISIBLE);
                } else {
                    bannerViewHolder1.getTvCommunity().setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(bannerViewHolder1,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;

            default:
                NewsViewHolder1 defaultStyle = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            R.layout.index_news_style1, null);
                    defaultStyle = new NewsViewHolder1();
                    defaultStyle.setImgNewsPictureStyle1((ImageView) convertView
                            .findViewById(R.id.imgNewsPictureStyle1));
                    defaultStyle
                            .setTxtNewsTitleStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsTitleStyle1));
                    defaultStyle
                            .setTxtNewsSummaryStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSummaryStyle1));
                    defaultStyle.setTxtNewsZanStyle1((BaseTextView) convertView
                            .findViewById(R.id.txtNewsZanStyle1));
                    defaultStyle
                            .setTxtNewsReviewStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsReviewStyle1));
                    defaultStyle
                            .setTxtNewsSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(R.id.txtNewsSubscriptStyle1));
                    defaultStyle.ChannelName = (BaseTextView) convertView
                            .findViewById(R.id.txtypeStyle1);
                    defaultStyle.imgPlay = (ImageView) convertView
                            .findViewById(R.id.img_play);
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
//                if (ping.length() > 0 && !ping.equals("0")) {
//                    defaultStyle.getTxtNewsReviewStyle1().setText(" " + ping);
//                    defaultStyle.getTxtNewsReviewStyle1().setVisibility(
//                            View.VISIBLE);
//                } else {
//                    // 评论数少于1不可见
//                    defaultStyle.getTxtNewsReviewStyle1().setVisibility(View.GONE);
//                }
                if (TextUtils.isEmpty(readNo)) {
                    defaultStyle.getTxtNewsZanStyle1().setVisibility(View.GONE);
                } else {
                    defaultStyle.getTxtNewsZanStyle1().setText(
                            readNo);
                }
                if (!TextUtils.isEmpty(ChannelName)) {
                    defaultStyle.ChannelName.setText(ChannelName);
                    defaultStyle.ChannelName.setVisibility(View.VISIBLE);
                } else {
                    defaultStyle.ChannelName.setVisibility(View.GONE);
                }

                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(defaultStyle,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
        }

        return convertView;
    }

    class viewholder {
        private FrameLayout layout;
        private ImageView imageview;
        private BaseTextView text_title;
        private ImageView play;
        private BaseTextView text_content;
        private BaseTextView tx_pinlun;
        private BaseTextView news_type;

        private LinearLayout lvItem_layout2;
        private RelativeLayout lvItem_layout;
        private BaseTextView lv_Item_Title2;
        private ImageView new_list_image1;
        private ImageView new_list_image2;
        private ImageView new_list_image3;
        private BaseTextView image_count;
        private BaseTextView tuji_comment;
        private RelativeLayout news_tuji_rela;
        private FrameLayout frameLayoutid;

        private FrameLayout frame_id;

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
                System.out.println("===> loading " + imageUri);
                // }
            }
        }
    }

}
