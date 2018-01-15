package com.dingtai.jinriyongzhou.api;

import com.dingtai.base.api.API;

/**
 * Created by xf on 2017/7/20 0020.
 */

public class IndexAPI extends API {
    // 直播间插入
    public static String API_LIVEROOM_INSERT_URL = API.COMMON_URL + "interface/NewsLiveEventsAPI.ashx?action=InserNewsContent";
    public static String LIVEROOM_INSERT_MESSAGE = "com.dingtai.jinriyongzhou.liveroom.insert.message";
    public final static int LIVEROOM_INSERT_API = 212;

    public final static int ZHIBO_EVENT_LIST_API = 444;
    public static String ZHIBO_EVENT_LIST_MESSAGE = "com.dingtai.jinriyongzhou.zhiboevent.message";

    // 首页列表数据请求
    // http://standard.d5mt.com.cn/interface/IndexAPI.ashx?action=GetIndexListShangla&StID=1&top=10&dtop=2  &sign=1
    public static String API_INDEX_LIST_URL = API.COMMON_URL + "interface/IndexAPI.ashx?action=GetIndexListShangla";
    public static String INDEX_LIST_MESSAGE = "com.dingtai.huoliyongzhou.index.list.message";
    public final static int INDEX_LIST_API = 100;

    // 首页新闻列表数据请求
    // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action= GetNewsChildUpList&top=10&dtop=10&chid=11
    public static String API_INDEX_NEWS_LIST_URL = API.COMMON_URL + "interface/NewsChildAPI.ashx?action=GetNewsChildUpList";
    public static String INDEX_NEWS_LIST_MESSAGE = "com.dingtai.huoliyongzhou.index.news.list.message";
    public final static int INDEX_NEWS_LIST_API = 101;

    // 长江首页新闻列表数据请求
    // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action= GetNewsChildUpList&top=10&dtop=10&chid=11
    public static String CJ_API_INDEX_NEWS_LIST_URL = API.COMMON_URL + "/interface/AppCSIndexAPI.ashx?action=GetChangjiangAPI";
    public static String CJ_INDEX_NEWS_LIST_MESSAGE = "com.dingtai.huoliyongzhou.index.new.news.list.message";
    public final static int CJ_INDEX_NEWS_LIST_API = 10001;
    public final static int GET_INDEX_MOKUAI = 34;
    public static String GET_INDEX_MOKUAI_MESSENGER = "com.dingtai.chenzhou.GET.INDEX_MOKUAI.message";

    //爆料详情点赞
    public static String API_BAOLIAO_DEL_GOODTOP_URL = API.COMMON_URL + "interface/RevelationManagementAPI.ashx?action=DelGoodPointMTM";
    public static String BAOLIAO_DEL_GOODTOP_MESSAGE = "com.dingtai.jinriyongzhou.baoliao.delgoodstop.message";
    public final static int BAOLIAO_DEL_GOODTOP__API = 214;

    //爆料详情点赞
    public static String API_BAOLIAO_GOODTOP_URL = API.COMMON_URL + "interface/RevelationManagementAPI.ashx?action=AddGoodPointMTM";
    public static String BAOLIAO_GOODTOP_MESSAGE = "com.dingtai.jinriyongzhou.baoliao.goodstop.message";
    public final static int BAOLIAO_GOODTOP__API = 211;

    /**
     * 更多新闻
     */
    public final static int NEWS_MORE_API = 64;
    public static String GET_MORE_NEWS_MESSAGE = "com.dingtai.jinriyongzhou.activity.morenews.message";

    public final static int SEARCH_LIST_API = 705;
    public final static int SEARCH_LIVE_LIST_API = 706;
    public final static int SEARCH_DB_LIST_API = 707;
    public final static int SEARCH_HD_LIST_API = 708;
    public static String SEARCH_LIST_MESSAGE = "com.dingtai.jinriyongzhou.setting.activitySearch.FragmentHUDONGList.message";
    public static String SEARCH_LIVE_LIST_MESSAGE = "com.dingtai.jinriyongzhou.setting.activitySearch.live.message";
    public static String SEARCH_DB_LIST_MESSAGE = "com.dingtai.jinriyongzhou.setting.activitySearch.dianbo.message";
    public static String SEARCH_HD_LIST_MESSAGE = "com.dingtai.jinriyongzhou.setting.activitySearch.active.message";

    public final static int VIDEO_LIST_REMOVEZAN_API = 136;
    public static String VIDEO_LIST_REMOVEZAN_MESSENGER = "com.dingtai.jinriyongzhou.video.removezan.message";
    public static final String VIDEO_LIST_REMOVEZAN_URL = COMMON_URL + "interface/MediaAPI.ashx?action=DelGoodPointMTM";

    public final static int VIDEO_LIST_ZAN_API = 135;
    public static String VIDEO_LIST_ZAN_MESSENGER = "com.dingtai.jinriyongzhou.video.zan.message";
    public static final String VIDEO_LIST_ZAN_URL = COMMON_URL + "interface/MediaAPI.ashx?action=AddGoodPointMTM";

    // 直播间点赞
    public static String API_LIVEROOM_ZAN_URL = API.COMMON_URL + "interface/NewsLiveAPI.ashx?action=AddGoodPointMTM";
    public static String LIVEROOM_ZAN_MESSAGE = "com.dingtai.jinriyongzhou.liveroom.zan.message";
    public final static int LIVEROOM_ZAN_API = 213;

    // 直播间点赞
    public static String API_LIVEROOM_DEL_ZAN_URL = API.COMMON_URL + "interface/NewsLiveAPI.ashx?action=DelGoodPointMTM";
    public static String LIVEROOM_DEL_ZAN_MESSAGE = "com.dingtai.jinriyongzhou.liveroom.del.zan.message";
    public final static int LIVEROOM_DEL_ZAN_API = 215;

    public final static int GET_LIST_NEWS_API = 218;
    public static String GET_LIST_NEWS_API_MESSENGER = "com.dingtai.jinriyongzhou.GET_LIST_NEWS_API.message";

    public final static int GET_SUBSCRIBE_LIST = 220;
    public static String GET_SUBSCRIBE_LIST_API = "com.dingtai.jinriyongzhou.GET_SUBSCRIBE_LIST.message";

    public final static int GET_ZWSUBSCRIBE_LIST = 221;
    public static String GET_ZWSUBSCRIBE_LIST_API = "com.dingtai.jinriyongzhou.GET_ZWSUBSCRIBE_LIST.message";

    public final static int GET_SUBSCRIBE_UPDATE = 222;
    public static String GET_SUBSCRIBE_UPDATE_API = "com.dingtai.jinriyongzhou.GET_SUBSCRIBE_UPDATE.message";

    //政务新接口
    public static String API_NEWS_POLITICS = API.COMMON_URL + "interface/PoliticsNewAPI.ashx?action=GetPoliticsIndex";
    //政务大厅首页
    public final static int GET_NEW_POLITICS = 330;
    public static String GET_NEW_POLITICS_API = "com.dingtai.jinriyongzhou.GET_NEW_POLITICS.message";

    //永州号具体详情模块
    public final static int GET_INSTITUTION_DETAIL = 601;
    public static String GET_INSTITUTION_DETAIL_API = "com.dingtai.jinriyongzhou.GET_INSTITUTION_DETAIL.message";
    public final static int GET_INSTITUTION_LIST = 602;
    public static String GET_INSTITUTION_LIST_API = "com.dingtai.jinriyongzhou.GET_INSTITUTION_LIST_API.message";

    public final static int NEWTOPICE_API = 1101;
    public static String NEWTOPICE_MESSAGE = "com.dingtai.yongzhou.news.newtopice";
    //添加投稿
    public final static String CONTRI_ADD_API = API.COMMON_URL + "Interface/PoliticsNewAPI.ashx?action=InsertRes";
    public final static int WENZHENG_ADD_API = 1201;
    public static String WENZHENG_ADD_MESSAGE = "com.dingtai.yongzhou.news.addpolitics";
    //订阅状态
    public final static int SUBSCRIBE_STATE_API = 1202;
    public static String SUBSCRIBE_STATE_API_MESSAGE = "com.dingtai.yongzhou.news.subscribe.state";
    //报纸网络
    public final static int PAPER_NET_API = 1324;
    public static String PAPER_NET_API_MESSAGE = "com.dingtai.yongzhou.paper_net_api";

    //开机页面  
    public final static int START_PICS_API = 1325;
    public static String START_PICS_API_MESSAGE = "com.dingtai.yongzhou.start_pics_api";

    //点播List
    public final static int DIANBO_LIST_API = 1326;
    public static String DIANBO_LIST_API_MESSAGE = "com.dingtai.yongzhou.dianbo_list_api";


    //点播List点击进去的DetialList
    public final static int DIANBO_DETIAL_LIST_API = 1327;
    public static String DIANBO_DETIAL_LIST_API_MESSAGE = "com.dingtai.yongzhou.dianbo_detial_list_api";
    
    //活动Up
    public final static int ACTIV_UP_LIST_API = 1328;
    public static String ACTIV_UP_LIST_API_MESSAGE = "com.dingtai.yongzhou.activ_up";

    //活动Up
    public final static int ACTIV_DOWN_LIST_API = 1329;
    public static String ACTIV_DOWN_LIST_API_MESSAGE = "com.dingtai.yongzhou.activ_down";

    //点播List点击进去的DetialList2
    public final static int DIANBO_DETIAL_LIST_API2 = 1330;
    public static String DIANBO_DETIAL_LIST_API2_MESSAGE = "com.dingtai.yongzhou.dianbo_detial_list_api2";
}
