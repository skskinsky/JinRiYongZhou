package com.dingtai.jinriyongzhou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.imgdisplay.ImgTool;
import com.dingtai.base.model.DigPai;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.newsHolder.BannerViewHolder1;
import com.dingtai.base.newsHolder.BannerViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder1;
import com.dingtai.base.newsHolder.NewsViewHolder2;
import com.dingtai.base.newsHolder.NewsViewHolder3;
import com.dingtai.base.newsHolder.NewsViewHolder4;
import com.dingtai.base.newsHolder.PictureViewHolder1;
import com.dingtai.base.newsHolder.PictureViewHolder2;
import com.dingtai.base.other.IndexType;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.CommonSubscriptMethod;
import com.dingtai.base.utils.DateUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.WutuHolder;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.base.view.BaseTextView;
import com.dingtai.jinriyongzhou.model.HomeNewsModel;
import com.dingtai.newslib3.tool.HttpRequest;
import com.dingtai.votelib.view.ViewType;
import com.dingtai.votelib.view.VoteViewHolder;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class HomeNewsAdapter extends BaseAdapter {
    private Context context;
    private List<HomeNewsModel> list;
    private HashMap<Integer, Fragment> mPageReferenceMap;

    private int maxEms = 0;
    private DisplayImageOptions option;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;
    private DataBaseHelper dataHelper;
    private RuntimeExceptionDao<DigPai, String> digPai;
    private UserInfoModel user;
    private Drawable drawable, drawable1, drawable2, drawable3;

    public HomeNewsAdapter(Context context, List<HomeNewsModel> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
        this.mPageReferenceMap = new HashMap<Integer, Fragment>();
        digPai = getHelper().getMode(DigPai.class);
        option = MyImageLoader.option();
        user = Assistant.getUserInfoByOrm(context);
        drawable = context.getResources().getDrawable(com.dingtai.newslib3.R.drawable.zuo);
        drawable1 = context.getResources().getDrawable(com.dingtai.newslib3.R.drawable.zuolan);
        drawable2 = context.getResources().getDrawable(com.dingtai.newslib3.R.drawable.you);
        drawable3 = context.getResources().getDrawable(com.dingtai.newslib3.R.drawable.youlan);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        drawable1.setBounds(0, 0, drawable1.getMinimumWidth(),
                drawable.getMinimumHeight());
        drawable2.setBounds(0, 0, drawable2.getMinimumWidth(),
                drawable.getMinimumHeight());
        drawable3.setBounds(0, 0, drawable3.getMinimumWidth(),
                drawable.getMinimumHeight());
    }

    public DataBaseHelper getHelper() {
        if (this.dataHelper == null) {
            this.dataHelper = (DataBaseHelper) OpenHelperManager.getHelper(context, DataBaseHelper.class);
        }
        return this.dataHelper;
    }

    public void setList(List<HomeNewsModel> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
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
        return 20;
    } // 重写获取视图缓存

    @Override
    public int getItemViewType(int position) {
        if (list != null && position < list.size()) {
            int cssType = 0;
            int ResourceCSS = 0;
            HomeNewsModel listModel = list.get(position);
            // 主标题，副标题，缩略图，图集,时间
            String title = "", subTitle = "", smallImg = "", arrImg = "", zan = "", ping = "", time = "";
            title = listModel.getTitle();
            subTitle = listModel.getSummary();
            smallImg = listModel.getSmallPicUrl();
            String voteType = listModel.getVoteType();
            try {
                ResourceCSS = Integer.parseInt(listModel.getResourceCSS());
                if (ResourceCSS == 0) {
                    ResourceCSS = 1;
                }
            } catch (Exception e) {
                ResourceCSS = 1;
            }
            int type = 1;
            try {
                type = Integer.parseInt(voteType);
            } catch (Exception e) {
                type = 1;
            }
            if (type == 2 || type == 4) {
                ResourceCSS = 6;
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
                case 6:
                    cssType = 11;
                    break;
                case 4://左边大图右边两小
                    cssType = 13;
                    break;
                case 5://右边大图左边两小
                    cssType = 12;
                    break;
                case 7://专题通栏
                    cssType = 16;
                    break;
                case 8://两张封面
                    cssType = 14;
                    break;
                default:
                    cssType = 1;
                    break;
            }
            return cssType;
        }
        return super.getItemViewType(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // TODO 首页数据绑定
        int CSSType = 0;
        int otherWidth = 0;
        int width = 0;
        int textSize = 0;
        // 主标题，副标题，缩略图，图集,时间
        // Log.i("news_chid", listModel.getChID()+"---"+position);
        String title = "", subTitle = "", smallImg = "", arrImg = "", zan = "", ping = "", time = "", type = "";
        String readNo = "";
        final HomeNewsModel listModel = list.get(position);
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
            zan = StringOper.getNumString(listModel.getNewsGetGoodPoint(), "");
            readNo = StringOper.getNumString(listModel.getFakeReadNo(), "");
            time = listModel.getCreateTime();
            CSSType = getItemViewType(position);
            type = listModel.getResourceFlag();
        } catch (Exception e) {
        }
        if (!WutuSetting.getIsImg()) {
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
        // if (position == 2 && CSSType == 5)
        // Log.i("test", "news" + ",title:" + title + list.size() + "/" +
        // position + ",CSSType:" + CSSType);
        switch (CSSType) {
            case 1:
                // 主,副标题，图都有
                NewsViewHolder1 newsViewHolder1 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.index_news_style1, null);
                    newsViewHolder1 = new NewsViewHolder1();
                    newsViewHolder1.setImgNewsPictureStyle1((ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.imgNewsPictureStyle1));
                    newsViewHolder1
                            .setTxtNewsTitleStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsTitleStyle1));
                    newsViewHolder1
                            .setTxtNewsSummaryStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSummaryStyle1));
                    newsViewHolder1.setTxtNewsZanStyle1((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsZanStyle1));
                    newsViewHolder1
                            .setTxtNewsReviewStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsReviewStyle1));
                    newsViewHolder1
                            .setTxtNewsSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSubscriptStyle1));
                    newsViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle1);
                    newsViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.img_play);
                    convertView.setTag(newsViewHolder1);
                } else {
                    newsViewHolder1 = (NewsViewHolder1) convertView.getTag();
                }
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
                if (!TextUtils.isEmpty(ChannelName) && newsViewHolder1.ChannelName.getVisibility() == View.VISIBLE) {
                    newsViewHolder1.ChannelName.setText(ChannelName);
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
                            com.dingtai.newslib3.R.layout.index_news_style2, null);
                    newsViewHolder2 = new NewsViewHolder2();

                    newsViewHolder2.setImgNewsPictureStyle2((ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.imgNewsPictureStyle2));
                    newsViewHolder2
                            .setTxtNewsTitleStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsTitleStyle2));
                    newsViewHolder2
                            .setTxtNewsReviewStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsReviewStyle2));
                    newsViewHolder2.setTxtNewsZanStyle2((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsZanStyle2));
                    newsViewHolder2
                            .setTxtNewsSubscriptStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSubscriptStyle2));
                    newsViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle2);
                    newsViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.img_play);
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
                newsViewHolder2.getTxtNewsTitleStyle2().setMaxLines(2);
                newsViewHolder2.getTxtNewsTitleStyle2().setText(title);

                newsViewHolder2.getTxtNewsReviewStyle2().setText(ping);


                newsViewHolder2.getTxtNewsZanStyle2().setText(
                        listModel.getFakeReadNo());

                if (!TextUtils.isEmpty(ChannelName) && newsViewHolder2.ChannelName.getVisibility() == View.VISIBLE) {
                    newsViewHolder2.ChannelName.setText(ChannelName);
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
                            com.dingtai.newslib3.R.layout.index_news_style4, null);
                    newsViewHolder4 = new NewsViewHolder4();
                    newsViewHolder4
                            .setTxtNewsTitleStyle4((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsTitleStyle4));
                    newsViewHolder4
                            .setTxtNewsSummaryStyle4((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSummaryStyle4));
                    newsViewHolder4
                            .setTxtNewsReviewStyle4((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsReviewStyle4));
                    newsViewHolder4.setTxtNewsZanStyle4((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsZanStyle4));
                    newsViewHolder4
                            .setTxtNewsSubscriptStyle4((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSubscriptStyle4));
                    newsViewHolder4.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle4);
                    convertView.setTag(newsViewHolder4);
                } else {
                    newsViewHolder4 = (NewsViewHolder4) convertView.getTag();
                }
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
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder4.getTxtNewsZanStyle4().setVisibility(View.GONE);
                } else {
                    newsViewHolder4.getTxtNewsZanStyle4().setText(
                            DateUtil.formatDate(time));
                }
                if (!TextUtils.isEmpty(ChannelName) && newsViewHolder4.ChannelName.getVisibility() == View.VISIBLE) {
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
                            com.dingtai.newslib3.R.layout.index_news_style3, null);
                    newsViewHolder3 = new NewsViewHolder3();
                    newsViewHolder3
                            .setTxtNewsTitleStyle3((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsTitleStyle3));
                    newsViewHolder3
                            .setTxtNewsReviewStyle3((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsReviewStyle3));
                    newsViewHolder3
                            .setTxtNewsSubscriptStyle3((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSubscriptStyle3));
                    newsViewHolder3.setTxtNewsZanStyle3((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsZanStyle3));
                    newsViewHolder3.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle3);
                    convertView.setTag(newsViewHolder3);
                } else {
                    newsViewHolder3 = (NewsViewHolder3) convertView.getTag();
                }
                newsViewHolder3.getTxtNewsTitleStyle3().setMaxLines(2);
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
                if (TextUtils.isEmpty(time)) {
                    newsViewHolder3.getTxtNewsZanStyle3().setVisibility(View.GONE);
                } else {
                    newsViewHolder3.getTxtNewsZanStyle3().setText(
                            DateUtil.formatDate(time));
                }
                if (!TextUtils.isEmpty(ChannelName) && newsViewHolder3.ChannelName.getVisibility() == View.VISIBLE) {
                    newsViewHolder3.ChannelName.setText(ChannelName);
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
                            com.dingtai.newslib3.R.layout.index_picture_style1, null);
                    picutreViewHolder1 = new PictureViewHolder1();
                    picutreViewHolder1
                            .setTxtPictureTitleStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureTitleStyle1));
                    picutreViewHolder1
                            .setImgPictureList1Style1((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList1Style1));
                    picutreViewHolder1
                            .setImgPictureList2Style1((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList2Style1));
                    picutreViewHolder1
                            .setImgPictureList3Style1((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList3Style1));
                    picutreViewHolder1
                            .setTxtPictureSummaryStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureSummaryStyle1));
                    picutreViewHolder1
                            .setTxtPictureReviewStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureReviewStyle1));
                    picutreViewHolder1
                            .setTxtPictureNumStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureNumStyle1));
                    picutreViewHolder1
                            .setTxtPictureZanStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureZanStyle1));
                    picutreViewHolder1
                            .setTxtPictureSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureSubscriptStyle1));
                    picutreViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle5);
                    convertView.setTag(picutreViewHolder1);
                } else {
                    picutreViewHolder1 = (PictureViewHolder1) convertView.getTag();
                }

                String imgURL[] = arrImg.split(",");
                width = DisplayMetricsTool.getWidth(context)
                        - DisplayMetricsTool.dip2px(context, 14);
                double w = (width - 20) / 3.0;
                double h = w * 16 / 9;
                int layout_w = (int) w;
                RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout.leftMargin = 0;
                layout.width = layout_w;
                layout.height = (int) h;
                picutreViewHolder1.getImgPictureList1Style1().setLayoutParams(
                        layout);

                RelativeLayout.LayoutParams layout2 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout2.leftMargin = 10 + layout_w;
                layout2.width = layout_w;
                layout2.height = (int) h;
                picutreViewHolder1.getImgPictureList2Style1().setLayoutParams(
                        layout2);

                RelativeLayout.LayoutParams layout3 = new RelativeLayout.LayoutParams(
                        layout_w, (int) h);
                layout3.leftMargin = 20 + 2 * layout_w;
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
                    // TODO: handle exception
                }
                try {
                    if (imgURL[1].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL[1],
                                picutreViewHolder1.getImgPictureList2Style1(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
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
                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder1.getTxtPictureNumStyle1().setText(arrImg.length()
                // + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder1.getTxtPictureNumStyle1().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName) && picutreViewHolder1.ChannelName.getVisibility() == View.VISIBLE) {
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
                            com.dingtai.newslib3.R.layout.index_picture_style2, null);
                    picutreViewHolder2 = new PictureViewHolder2();
                    picutreViewHolder2
                            .setTxtPictureTitleStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureTitleStyle2));
                    picutreViewHolder2
                            .setImgPictureList1Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList1Style2));
                    picutreViewHolder2
                            .setImgPictureList2Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList2Style2));
                    picutreViewHolder2
                            .setImgPictureList3Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList3Style2));
                    picutreViewHolder2
                            .setTxtPictureReviewStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureReviewStyle2));
                    picutreViewHolder2
                            .setTxtPictureNumStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureNumStyle2));
                    picutreViewHolder2
                            .setTxtPictureZanStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureZanStyle2));
                    picutreViewHolder2
                            .setTxtPictureSubscriptStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureSubscriptStyle2));
                    picutreViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle6);
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
                picutreViewHolder2.getImgPictureList1Style2()
                        .setLayoutParams(rlPic);

                RelativeLayout.LayoutParams rlPic2 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic2.leftMargin = 10 + layout_w1;
                rlPic2.width = layout_w1;
                rlPic2.height = (int) h1;
                picutreViewHolder2.getImgPictureList2Style2().setLayoutParams(
                        rlPic2);

                RelativeLayout.LayoutParams rlPic3 = new RelativeLayout.LayoutParams(
                        layout_w1, (int) h1);
                rlPic3.leftMargin = 20 + 2 * layout_w1;
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
                    picutreViewHolder2.getTxtPictureZanStyle2().setText(
                            DateUtil.formatDate(time));
                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder2.getTxtPictureNumStyle2().setText(arrImg.length()
                // + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder2.getTxtPictureNumStyle2().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName) && picutreViewHolder2.ChannelName.getVisibility() == View.VISIBLE) {
                    picutreViewHolder2.ChannelName.setText(ChannelName);
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
                            com.dingtai.newslib3.R.layout.adapter_index_list_news_banner2, null);
                    bannerViewHolder2 = new BannerViewHolder2();
                    bannerViewHolder2.setImgBanner((ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.imgBanner));
                    bannerViewHolder2.setTvTitle((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvTitle));
                    bannerViewHolder2.setTxtSummary((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtSummary));
                    bannerViewHolder2.setTvReply((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvReply));
                    bannerViewHolder2.setTvTime((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvTime));
                    bannerViewHolder2.setTvZan((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvZan));
                    bannerViewHolder2.setTvSubscript((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvSubscript));
                    bannerViewHolder2.setTvCommunity((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvCommunity));
                    bannerViewHolder2.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle7);
                    bannerViewHolder2.imgPlay = (ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.img_play);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16));

                    bannerViewHolder2.getImgBanner().setLayoutParams(layoutParams);


                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16 / 5));
                    layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    bannerViewHolder2.getTvTitle().setLayoutParams(layoutParams1);


                    convertView.setTag(bannerViewHolder2);
                } else {
                    bannerViewHolder2 = (BannerViewHolder2) convertView.getTag();
                }
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

                if (!TextUtils.isEmpty(ChannelName) && bannerViewHolder2.ChannelName.getVisibility() == View.VISIBLE) {
                    bannerViewHolder2.ChannelName.setText(ChannelName);
                } else {
                    bannerViewHolder2.ChannelName.setVisibility(View.GONE);
                }
                if ("3".equals(listModel.getResourceFlag())) {
                    bannerViewHolder2.imgPlay.setVisibility(View.VISIBLE);
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
                            com.dingtai.newslib3.R.layout.adapter_index_list_news_banner1, null);
                    bannerViewHolder1 = new BannerViewHolder1();
                    bannerViewHolder1.setImgBanner((ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.imgBanner));
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(
                            DisplayMetricsTool.getWidth(context),
                            (int) ((DisplayMetricsTool.getWidth(context)) * 9 / 16));

                    bannerViewHolder1.getImgBanner().setLayoutParams(layoutParams1);

                    bannerViewHolder1.setTvTitle((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvTitle));
                    bannerViewHolder1.setTvReply((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvReply));
                    bannerViewHolder1.setTvTime((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvTime));
                    bannerViewHolder1.setTvZan((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvZan));
                    bannerViewHolder1.setTvSubscript((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvSubscript));
                    bannerViewHolder1.setTvCommunity((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.tvCommunity));
                    bannerViewHolder1.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle8);
                    bannerViewHolder1.imgPlay = (ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.img_play);
                    convertView.setTag(bannerViewHolder1);
                } else {
                    bannerViewHolder1 = (BannerViewHolder1) convertView.getTag();
                }
                bannerViewHolder1.getTvTitle().setText(title);
                bannerViewHolder1.getTvTitle().setMaxLines(2);

                ImageLoader.getInstance().displayImage(smallImg,
                        bannerViewHolder1.getImgBanner(), option,
                        mImageLoadingListenerImpl);

                bannerViewHolder1.getTvReply().setText(" " + ping);
                // if (zan.length() > 0 && !zan.equals("0")) {
                // bannerViewHolder1.getTvZan().setText(" "+zan);
                // bannerViewHolder1.getTvZan().setVisibility(View.VISIBLE);
                // } else {
                // // 赞数少于1不可见
                // bannerViewHolder1.getTvZan().setVisibility(View.GONE);
                // }

                bannerViewHolder1.getTvTime()
                        .setText(readNo);
                if (!TextUtils.isEmpty(ChannelName) && bannerViewHolder1.ChannelName.getVisibility() == View.VISIBLE) {
                    bannerViewHolder1.ChannelName.setText(ChannelName);
                } else {
                    bannerViewHolder1.ChannelName.setVisibility(View.GONE);
                }
//                if ("3".equals(listModel.getResourceFlag())) {
//                    bannerViewHolder1.imgPlay.setVisibility(View.VISIBLE);
//                } else {
//                    bannerViewHolder1.imgPlay.setVisibility(View.GONE);
//                }

                bannerViewHolder1.getTvCommunity().setVisibility(View.GONE);
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(bannerViewHolder1,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
            case 11:
                VoteViewHolder voteViewHolder = null;
                if (convertView == null) {
                    voteViewHolder = new VoteViewHolder();
                    convertView = ViewType.getConvertView(voteViewHolder, context);
                } else {
                    voteViewHolder = (VoteViewHolder) convertView.getTag();
                }
                voteViewHolder.tv_title.setText(title);
                voteViewHolder.tv_title1.setText(title);
                if (TextUtils.isEmpty(listModel.getVoteRemark())) {
                    voteViewHolder.tv_summary.setVisibility(View.GONE);
                } else {
                    voteViewHolder.tv_summary.setVisibility(View.VISIBLE);
                    voteViewHolder.tv_summary.setText(listModel.getVoteRemark());
                }
                ImgTool.getInstance().loadImg(smallImg, voteViewHolder.iv_logo);
                if (user != null && digPai != null) {
                    if (digPai.idExists("toupiao" + listModel.getResourceGUID())) {
                        DigPai digPai = this.digPai.queryForId("toupiao" + listModel.getResourceGUID());
                        if (digPai.getType().equals("1"))
                            voteViewHolder.tv_vote1.setCompoundDrawables(drawable1, null, null, null);
                        else if (digPai.getType().equals("2"))
                            voteViewHolder.tv_vote2.setCompoundDrawables(null, null, drawable3, null);
                    } else {
                        voteViewHolder.tv_vote1.setCompoundDrawables(drawable, null, null, null);
                        voteViewHolder.tv_vote2.setCompoundDrawables(null, null, drawable2, null);
                    }
                } else {
                    voteViewHolder.tv_vote1.setCompoundDrawables(drawable, null, null, null);
                    voteViewHolder.tv_vote2.setCompoundDrawables(null, null, drawable2, null);
                }
                voteViewHolder.tv_vote1.setText(listModel.getVoteSubject1Name());
                voteViewHolder.tv_vote1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Assistant.getUserInfoByOrm(context) != null) {
                            if (!HomeNewsAdapter.this.digPai.idExists("toupiao" + listModel.getResourceGUID())) {
                                HttpRequest.vote(1, listModel.getResourceGUID(), context, new Messenger(mHandler));
                                DigPai digPai = new DigPai();
                                digPai.setUserGuid(Assistant.getUserInfoByOrm(context).getUserGUID());
                                digPai.setID("toupiao" + listModel.getResourceGUID());
                                digPai.setType("1");
                                HomeNewsAdapter.this.digPai.create(digPai);

//                                HomeNewsAdapter.this.viewHolder = holder;
//                                clickIndex = 1;
//                                HomeNewsAdapter.this.position = position;
//                                holder.tv_vote1.setCompoundDrawables(drawable1, null, null, null);
//                                int sum = Integer.parseInt(listModel.getVoteNum()) + 1;
//                                int l = Integer.parseInt(listModel.getVoteSubject1()) + 1;
//                                int r = Integer.parseInt(listModel.getVoteSubject2());
//                                holder.tv_center_count.setText(sum + 1);
//                                holder.tv_left_count.setText(l + "(" + (l / (float) sum) + "%)");
//                                holder.tv_right_count.setText(l + "(" + (r / (float) sum) + "%)");
                            } else {
                                Toast.makeText(context, "您已经投了票", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "请先登录!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(context.getPackageName() + ".login");
                            context.startActivity(intent);
                        }
                    }
                });
                voteViewHolder.tv_vote2.setText(listModel.getVoteSubject2Name());
                voteViewHolder.tv_vote2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Assistant.getUserInfoByOrm(context) != null) {
                            if (!HomeNewsAdapter.this.digPai.idExists("toupiao" + listModel.getResourceGUID())) {
                                HttpRequest.vote(2, listModel.getResourceGUID(), context, new Messenger(mHandler));
//                                holder.tv_vote2.setCompoundDrawables(null, null, drawable3, null);
//                                int sum = Integer.parseInt(listModel.getVoteNum()) + 1;
//                                int l = Integer.parseInt(listModel.getVoteSubject1());
//                                int r = Integer.parseInt(listModel.getVoteSubject2()) + 1;
//                                holder.tv_center_count.setText(sum + 1);
//                                holder.tv_left_count.setText(l + "(" + (l / (float) sum) + "%)");
//                                holder.tv_right_count.setText(l + "(" + (r / (float) sum) + "%)");
                                DigPai digPai = new DigPai();
                                digPai.setUserGuid(Assistant.getUserInfoByOrm(context).getUserGUID());
                                digPai.setID("toupiao" + listModel.getResourceGUID());
                                digPai.setType(2 + "");
                                HomeNewsAdapter.this.digPai.create(digPai);
                            } else {
                                Toast.makeText(context, "您已经投了票", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "请先登录!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setAction(context.getPackageName() + ".login");
                            context.startActivity(intent);
                        }

                    }
                });
                voteViewHolder.tv_left_count.setText(listModel.getVoteSubject1() + "(" + listModel.getVoteSubject1Percent() + "%)");
                voteViewHolder.tv_right_count.setText("(" + listModel.getVoteSubject2Percent() + "%)" + listModel.getVoteSubject2());
                voteViewHolder.tv_center_count.setText(listModel.getVoteNum());
                try {
                    int max = Integer.parseInt(listModel.getVoteNum());
                    int progress = Integer.parseInt(listModel.getVoteSubject1());
                    voteViewHolder.seekBar.setMax(max);
                    voteViewHolder.seekBar.setProgress(progress);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                PictureViewHolder3 pictureViewHolder = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.item_picture_style3, parent, false);
                    pictureViewHolder = new PictureViewHolder3();
                    pictureViewHolder.tv_title = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_title);
                    pictureViewHolder.tv_channelName = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_channelName);
                    pictureViewHolder.tv_sub = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_sub);
                    pictureViewHolder.tv_readNum = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_readNum);
                    pictureViewHolder.iv_left_top = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_left_top);
                    pictureViewHolder.iv_left_bottom = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_left_bottom);
                    pictureViewHolder.iv_right = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_right);
                } else {
                    pictureViewHolder = (PictureViewHolder3) convertView.getTag();
                }
                String imgUrls[] = arrImg.split(",");
                pictureViewHolder.iv_left_top.setImageBitmap(null);
                pictureViewHolder.iv_left_bottom.setImageBitmap(null);
                pictureViewHolder.iv_right.setImageBitmap(null);
                ImgTool.getInstance().loadImg(imgUrls[0], pictureViewHolder.iv_left_top);
                ImgTool.getInstance().loadImg(imgUrls[1], pictureViewHolder.iv_left_bottom);
                ImgTool.getInstance().loadImg(imgUrls[2], pictureViewHolder.iv_right);
                pictureViewHolder.tv_title.setText(title);
                pictureViewHolder.tv_readNum.setText("  " + readNo);
                if (TextUtils.isEmpty(listModel.getChannelName())) {
                    pictureViewHolder.tv_channelName.setVisibility(View.GONE);
                } else {
                    pictureViewHolder.tv_channelName.setVisibility(View.VISIBLE);
                    pictureViewHolder.tv_channelName.setText(listModel.getChannelName());
                }
                IndexType indexType = CommonSubscriptMethod.setNewsSubscript(listModel.getResourceFlag(), context);
                if (indexType != null) {
                    pictureViewHolder.tv_sub.setVisibility(View.VISIBLE);
                    pictureViewHolder.tv_sub.setText(indexType.getType());
                    pictureViewHolder.tv_sub.setTextColor(indexType.getColor());
                    pictureViewHolder.tv_sub.setBackgroundDrawable(indexType.getDraw());
                } else {
                    pictureViewHolder.tv_sub.setVisibility(View.GONE);
                }
                break;
            case 14:
                PictureViewHolder4 pictureViewHolder4 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.item_picture_style4, parent, false);
                    pictureViewHolder4 = new PictureViewHolder4();
                    pictureViewHolder4.tv_title = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_title);
                    pictureViewHolder4.tv_channelName = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_channelName);
                    pictureViewHolder4.tv_sub = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_sub);
                    pictureViewHolder4.tv_readNum = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_readNum);
                    pictureViewHolder4.iv_left = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_left);
                    pictureViewHolder4.iv_right = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_right);
                } else {
                    pictureViewHolder4 = (PictureViewHolder4) convertView.getTag();
                }
                String imgUrls4[] = arrImg.split(",");
                pictureViewHolder4.iv_left.setImageBitmap(null);
                pictureViewHolder4.iv_right.setImageBitmap(null);
                ImgTool.getInstance().loadImg(imgUrls4[0], pictureViewHolder4.iv_left);
                ImgTool.getInstance().loadImg(imgUrls4[1], pictureViewHolder4.iv_right);
                pictureViewHolder4.tv_title.setText(title);
                pictureViewHolder4.tv_readNum.setText("  " + readNo);
                if (TextUtils.isEmpty(listModel.getChannelName())) {
                    pictureViewHolder4.tv_channelName.setVisibility(View.GONE);
                } else {
                    pictureViewHolder4.tv_channelName.setVisibility(View.VISIBLE);
                    pictureViewHolder4.tv_channelName.setText(listModel.getChannelName());
                }
                IndexType indexType4 = CommonSubscriptMethod.setNewsSubscript(listModel.getResourceFlag(), context);
                if (indexType4 != null) {
                    pictureViewHolder4.tv_sub.setVisibility(View.VISIBLE);
                    pictureViewHolder4.tv_sub.setText(indexType4.getType());
                    pictureViewHolder4.tv_sub.setTextColor(indexType4.getColor());
                    pictureViewHolder4.tv_sub.setBackgroundDrawable(indexType4.getDraw());
                } else {
                    pictureViewHolder4.tv_sub.setVisibility(View.GONE);
                }
                break;
            case 15:
                PictureViewHolder2 picutreViewHolder6 = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.index_picture_style2, null);
                    picutreViewHolder6 = new PictureViewHolder2();
                    picutreViewHolder6
                            .setTxtPictureTitleStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureTitleStyle2));
                    picutreViewHolder6
                            .setImgPictureList1Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList1Style2));
                    picutreViewHolder6
                            .setImgPictureList2Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList2Style2));
                    picutreViewHolder6
                            .setImgPictureList3Style2((ImageView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.imgPictureList3Style2));
                    picutreViewHolder6
                            .setTxtPictureReviewStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureReviewStyle2));
                    picutreViewHolder6
                            .setTxtPictureNumStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureNumStyle2));
                    picutreViewHolder6
                            .setTxtPictureZanStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureZanStyle2));
                    picutreViewHolder6
                            .setTxtPictureSubscriptStyle2((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtPictureSubscriptStyle2));
                    picutreViewHolder6.ChannelName = (BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtypeStyle6);
                    convertView.setTag(picutreViewHolder6);
                } else {
                    picutreViewHolder6 = (PictureViewHolder2) convertView.getTag();
                }
                String imgURL3[] = arrImg.split(",");
                width = DisplayMetricsTool.getWidth(context)
                        - DisplayMetricsTool.dip2px(context, 14);
                double w2 = (width - 20) / 3.0;
                double h2 = w2 * 16 / 9;
                int layout_w2 = (int) w2;
                RelativeLayout.LayoutParams rlPic4 = new RelativeLayout.LayoutParams(
                        layout_w2, (int) h2);
                rlPic4.leftMargin = 0;
                rlPic4.width = layout_w2;
                rlPic4.height = (int) h2;
                picutreViewHolder6.getImgPictureList1Style2()
                        .setLayoutParams(rlPic4);
                RelativeLayout.LayoutParams rlPic5 = new RelativeLayout.LayoutParams(
                        layout_w2, (int) h2);
                rlPic5.leftMargin = 10 + layout_w2;
                rlPic5.width = layout_w2;
                rlPic5.height = (int) h2;
                picutreViewHolder6.getImgPictureList2Style2().setLayoutParams(
                        rlPic5);
                RelativeLayout.LayoutParams rlPic6 = new RelativeLayout.LayoutParams(
                        layout_w2, (int) h2);
                rlPic6.leftMargin = 20 + 2 * layout_w2;
                rlPic6.width = layout_w2;
                rlPic6.height = (int) h2;
                picutreViewHolder6.getImgPictureList3Style2().setLayoutParams(
                        rlPic6);
                picutreViewHolder6.getImgPictureList1Style2().setImageBitmap(null);
                picutreViewHolder6.getImgPictureList2Style2().setImageBitmap(null);
                picutreViewHolder6.getImgPictureList3Style2().setImageBitmap(null);
                // 获取图片
                try {
                    if (imgURL3[0].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL3[0],
                                picutreViewHolder6.getImgPictureList1Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL3[1].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL3[1],
                                picutreViewHolder6.getImgPictureList2Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }
                try {
                    if (imgURL3[2].length() > 0) {
                        ImageLoader.getInstance().displayImage(imgURL3[2],
                                picutreViewHolder6.getImgPictureList3Style2(),
                                option, mImageLoadingListenerImpl);
                    }
                } catch (Exception e) {
                }

                // listModel.getAppIndexTitle() 获取标题
                picutreViewHolder6.getTxtPictureTitleStyle2().setText(title);

                if (ping.length() > 0 && !ping.equals("0")) {
                    picutreViewHolder6.getTxtPictureReviewStyle2().setText(
                            " " + ping);
                    picutreViewHolder6.getTxtPictureReviewStyle2().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    picutreViewHolder6.getTxtPictureReviewStyle2().setVisibility(
                            View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    picutreViewHolder6.getTxtPictureZanStyle2().setVisibility(
                            View.GONE);
                } else {
                    picutreViewHolder6.getTxtPictureZanStyle2().setText(
                            DateUtil.formatDate(time));
                }
                // if (arrImg.length() > 0) {
                // picutreViewHolder2.getTxtPictureNumStyle2().setText(arrImg.length()
                // + "图");
                // } else {
                // 图集数少于1不可见
                // picutreViewHolder2.getTxtPictureNumStyle2().setVisibility(View.GONE);
                // }
                if (!TextUtils.isEmpty(ChannelName) && picutreViewHolder6.ChannelName.getVisibility() == View.VISIBLE) {
                    picutreViewHolder6.ChannelName.setText(ChannelName);
                } else {
                    picutreViewHolder6.ChannelName.setVisibility(View.GONE);
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(picutreViewHolder6, list
                        .get(position).getResourceFlag(), context);
                break;
            case 16:
                ZhengWuHodler1 zhengWuHodler1 = null;
                if (convertView == null) {
                    zhengWuHodler1 = new ZhengWuHodler1();
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.item_zhengwu_style1, parent, false);
                    zhengWuHodler1.iv_logo = (ImageView) convertView.findViewById(com.dingtai.newslib3.R.id.iv_logo);
                    zhengWuHodler1.tv_channelName = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_channelName);
                    zhengWuHodler1.tv_read = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_readNum);
                    zhengWuHodler1.tv_title = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_title);
                    zhengWuHodler1.tv_sub = (TextView) convertView.findViewById(com.dingtai.newslib3.R.id.tv_sub);
                } else {
                    zhengWuHodler1 = (ZhengWuHodler1) convertView.getTag();
                }
                ImgTool.getInstance().loadImg(listModel.getSmallPicUrl(),zhengWuHodler1.iv_logo);
                zhengWuHodler1.tv_title.setText(title);
                if (TextUtils.isEmpty(listModel.getChannelName())) {
                    zhengWuHodler1.tv_channelName.setVisibility(View.GONE);
                } else {
                    zhengWuHodler1.tv_channelName.setVisibility(View.VISIBLE);
                    zhengWuHodler1.tv_channelName.setText(listModel.getChannelName());
                }
                IndexType indexType5 = CommonSubscriptMethod.setNewsSubscript(listModel.getResourceFlag(), context);
                if (indexType5 != null) {
                    zhengWuHodler1.tv_sub.setVisibility(View.VISIBLE);
                    zhengWuHodler1.tv_sub.setText(indexType5.getType());
                    zhengWuHodler1.tv_sub.setTextColor(indexType5.getColor());
                    zhengWuHodler1.tv_sub.setBackgroundDrawable(indexType5.getDraw());
                } else {
                    zhengWuHodler1.tv_sub.setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(readNo)) {
                    zhengWuHodler1.tv_read.setVisibility(View.GONE);
                } else {
                    zhengWuHodler1.tv_read.setVisibility(View.VISIBLE);
                    zhengWuHodler1.tv_read.setText(readNo);
                }
                break;
//            case 17:
//                ZhengWuHodler2 zhengWuHodler2 = null;
//                if (convertView == null) {
//                    convertView = LayoutInflater.from(context).inflate(
//                            R.layout.item_zhengwu_style2, parent, false);
//                    zhengWuHodler2.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//                    zhengWuHodler2.tv_channelName = (TextView) convertView.findViewById(R.id.tv_channelName);
//                    zhengWuHodler2.tv_read = (TextView) convertView.findViewById(R.id.tv_readNum);
//                    zhengWuHodler2.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
//                    zhengWuHodler2.tv_names = (TextView) convertView.findViewById(R.id.tv_work);
//                    zhengWuHodler2.tv_sub = (TextView) convertView.findViewById(R.id.tv_sub);
//                } else {
//                    zhengWuHodler2 = (ZhengWuHodler2) convertView.getTag();
//                }
//                zhengWuHodler2.tv_title.setText(title);
//                if (TextUtils.isEmpty(listModel.getChannelName())) {
//                    zhengWuHodler2.tv_channelName.setVisibility(View.GONE);
//                } else {
//                    zhengWuHodler2.tv_channelName.setVisibility(View.VISIBLE);
//                    zhengWuHodler2.tv_channelName.setText(listModel.getChannelName());
//                }
//                IndexType indexType6= CommonSubscriptMethod.setNewsSubscript(listModel.getResourceFlag(), context);
//                if (indexType6 != null) {
//                    zhengWuHodler2.tv_sub.setVisibility(View.VISIBLE);
//                    zhengWuHodler2.tv_sub.setText(indexType6.getType());
//                    zhengWuHodler2.tv_sub.setTextColor(indexType6.getColor());
//                    zhengWuHodler2.tv_sub.setBackgroundDrawable(indexType6.getDraw());
//                } else {
//                    zhengWuHodler2.tv_sub.setVisibility(View.GONE);
//                }
//                if (TextUtils.isEmpty(readNo)) {
//                    zhengWuHodler2.tv_read.setVisibility(View.GONE);
//                } else {
//                    zhengWuHodler2.tv_read.setVisibility(View.VISIBLE);
//                    zhengWuHodler2.tv_read.setText(readNo);
//                }
//                break;
            default:
                NewsViewHolder1 defaultStyle = null;
                if (convertView == null) {
                    convertView = LayoutInflater.from(context).inflate(
                            com.dingtai.newslib3.R.layout.index_news_style1, null);
                    defaultStyle = new NewsViewHolder1();
                    defaultStyle.setImgNewsPictureStyle1((ImageView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.imgNewsPictureStyle1));
                    defaultStyle.setTxtNewsTitleStyle1((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsTitleStyle1));
                    defaultStyle.setTxtNewsSummaryStyle1((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsSummaryStyle1));
                    defaultStyle.setTxtNewsZanStyle1((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsZanStyle1));
                    defaultStyle.setTxtNewsReviewStyle1((BaseTextView) convertView
                            .findViewById(com.dingtai.newslib3.R.id.txtNewsReviewStyle1));
                    defaultStyle
                            .setTxtNewsSubscriptStyle1((BaseTextView) convertView
                                    .findViewById(com.dingtai.newslib3.R.id.txtNewsSubscriptStyle1));
                    convertView.setTag(defaultStyle);
                } else {
                    defaultStyle = (NewsViewHolder1) convertView.getTag();
                }
                defaultStyle.getTxtNewsTitleStyle1().setText(title);
                defaultStyle.getTxtNewsSummaryStyle1().setText(subTitle);
                ImageLoader.getInstance().displayImage(smallImg,
                        defaultStyle.getImgNewsPictureStyle1(), option,
                        mImageLoadingListenerImpl);
                if (ping.length() > 0 && !ping.equals("0")) {
                    defaultStyle.getTxtNewsReviewStyle1().setText(" " + ping);
                    defaultStyle.getTxtNewsReviewStyle1().setVisibility(
                            View.VISIBLE);
                } else {
                    // 评论数少于1不可见
                    defaultStyle.getTxtNewsReviewStyle1().setVisibility(View.GONE);
                }
                if (TextUtils.isEmpty(time)) {
                    defaultStyle.getTxtNewsZanStyle1().setVisibility(View.GONE);
                } else {
                    defaultStyle.getTxtNewsZanStyle1().setText(
                            DateUtil.formatDate(time));
                }
                // 设置角标
                CommonSubscriptMethod.getNewsSubscript(defaultStyle,
                        listModel.getResourceFlag(), context); // listModel.getResourceType()
                break;
        }

        return convertView;
    }

    class PictureViewHolder5 {
        TextView tv_title, tv_readNum, tv_channelName, tv_sub;
        ImageView iv_right_top, iv_right_bottom, iv_left;
    }

    class PictureViewHolder3 {
        TextView tv_title, tv_readNum, tv_channelName, tv_sub;
        ImageView iv_left_top, iv_left_bottom, iv_right;
    }

    class PictureViewHolder4 {
        TextView tv_title, tv_readNum, tv_channelName, tv_sub;
        ImageView iv_left, iv_right;
    }

    class ZhengWuHodler1 {
        TextView tv_title, tv_sub, tv_read, tv_channelName;
        ImageView iv_logo;
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    if (user == null)
                        user = Assistant.getUserInfoByOrm(context);
                    if (msg.obj != null && msg.obj.equals("您已经投票!")) {
                        HomeNewsAdapter.this.notifyDataSetChanged();
                    }
//                        if (HomeNewsAdapter.this.viewHolder != null && clickIndex != -1) {
//                            if (clickIndex == 1) {
//                                HomeNewsAdapter.this.viewHolder.tv_vote1.
//                                        setCompoundDrawables(drawable1, null, null, null);
//
//                            } else if (clickIndex == 2) {
//                                HomeNewsAdapter.this.viewHolder.tv_vote2.
//                                        setCompoundDrawables(null, null, drawable3, null);
//                            }
//                            if (!HomeNewsAdapter.this.digPai.idExists("toupiao" + list.get(position).getResourceGUID())) {
//                                DigPai digPai = new DigPai();
//                                digPai.setUserGuid(Assistant.getUserInfoByOrm(context).getUserGUID());
//                                digPai.setID("toupiao" + list.get(position).getResourceGUID());
//                                digPai.setType(clickIndex + "");
//                                HomeNewsAdapter.this.digPai.create(digPai);
//                            }
                    Toast.makeText(context, "您已经投票!", Toast.LENGTH_SHORT);
//            }

                    break;
                case 404:
                    Toast.makeText(context, msg.obj.toString(), Toast.LENGTH_SHORT);
                    break;
            }
            super.

                    handleMessage(msg);
        }
    };

    public void setUser(UserInfoModel user) {
        this.user = user;
    }

    // 监听图片异步加载
    public static class ImageLoadingListenerImpl extends
            SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());
        private static List<Bitmap> bitmaps = new ArrayList<>();

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
                if (bitmap != null)
                    bitmaps.add(bitmap);
                // }
            }
        }

        public static void clearCacheMemory() {
            displayedImages.clear();
            for (Bitmap b : bitmaps) {
                if (!b.isRecycled())
                    b.recycle();
            }
        }
    }

}
