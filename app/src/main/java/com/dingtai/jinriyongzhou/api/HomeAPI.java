package com.dingtai.jinriyongzhou.api;

import com.dingtai.base.api.API;

/**
 * Created by xf on 2017/6/27 0027.
 */

public class HomeAPI extends API {
    // 问政列表下拉http://116.62.29.20:8077
    // http://192.168.1.12:809/Interface/PoliticsAPI.ashx?action=GetPoliticsListXiaLa&top=10&StID=1
    public static String WENZHENG_LIST_DOWN_API_URL = API.COMMON_URL + "Interface/PoliticsAPI.ashx?action=GetPoliticsListXiaLa";
    public static String WENZHENG_LIST_DOWN_MESSAGE = "com.dingtai.jinrichenzhou.wenzheng.list_down_wenzheng.message";
    public final static int WENZHENG_LIST_DOWN_API = 304;

    // 问政列表上拉
    // http://192.168.1.12:809/Interface/PoliticsAPI.ashx?action=GetPoliticsListShangLa
    public static String WENZHENG_LIST_UP_API_URL = API.COMMON_URL + "Interface/PoliticsAPI.ashx?action=GetPoliticsListShangLa";
    public static String WENZHENG_LIST_UP_MESSAGE = "com.dingtai.jinrichenzhou.wenzheng.list_up_wenzheng.message";
    public final static int WENZHENG_LIST_UP_API = 305;

    public static int WENZHENG_GET_ERROR = 000; // 数据获取失败码

    // 首页列表数据请求
    // http://standard.d5mt.com.cn/interface/IndexAPI.ashx?action=GetIndexListShangla&StID=1&top=10&dtop=2  &sign=1
    public static String API_INDEX_LIST_URL = API.COMMON_URL + "interface/IndexAPI.ashx?action=GetIndexListShangla";
    public static String INDEX_LIST_MESSAGE = "com.dingtai.jinrichenzhou.index.list.message";
    public final  static int INDEX_LIST_API = 100;

    // 首页新闻列表数据请求
    // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action= GetNewsChildUpList&top=10&dtop=10&chid=11
    public static String API_INDEX_NEWS_LIST_URL = API.COMMON_URL + "interface/NewsChildAPI.ashx?action=GetNewsChildUpList";
    public static String INDEX_NEWS_LIST_MESSAGE = "com.dingtai.jinrichenzhou.index.news.list.message";
    public final static int INDEX_NEWS_LIST_API = 101;

    // 长江首页新闻列表数据请求
    // http://standard.d5mt.com.cn/interface/NewsChildAPI.ashx?action= GetNewsChildUpList&top=10&dtop=10&chid=11
    public static String CJ_API_INDEX_NEWS_LIST_URL = API.COMMON_URL + "/interface/AppCSIndexAPI.ashx?action=GetChangjiangAPI";
    public static String CJ_INDEX_NEWS_LIST_MESSAGE = "com.dingtai.jinrichenzhou.index.new.news.list.message";
    public final static int CJ_INDEX_NEWS_LIST_API = 10086;

    public final static int AddReadNo_API = 53;
    public static String AddReadNo_MESSAGE = "com.dingtai.jinrichenzhou.addreadno.message";
}
