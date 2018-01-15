package com.dingtai.jinriyongzhou.config;

import java.util.HashMap;
import java.util.Map;

/***
 * 首页左边导航栏 栏目配置
 *
 * @author xf
 *
 */
public class IndexSujbectConfig {
    public static String[] MODULE = {
            "News", "NewsList", "Shopping", "ShangCheng", "Video", "BaoLiao", "GoodsBaoLiao",
            "DianZiBao", "zhibojian", "TouPiao", "Weather", "Bus", "HuoDong",
            "ZhengWu", "LiveActive", "yaoyaole", "zhibo", "jiaofei", "dianbo",
            "zhuanti", "ZhouBian", "HaoMaTong", "HuZhu", "Bike", "Park",
            "Traffic", "Coach", "more","lukuang"};        //添加新的东西这里必须填
    public static Map<String, String> IndexModel = new HashMap<String, String>();

    static {
        IndexModel.put("News",
                "com.dingtai.newslib3.activity.ActivityNewsFromIndex");
        IndexModel.put("NewsList",
                "com.dingtai.newslib3.activity.NewsListActivity");
        IndexModel.put("BaoLiao",
                "com.dingtai.dtbaoliao.activity.BaoLiaoMain");
        IndexModel.put("zhibojian",
                "com.dingtai.jinriyongzhou.activity.zhibo.ActivityZhiboList");
        IndexModel.put("TouPiao",
                "com.dingtai.newslib3.activity.NewsWebView");
        IndexModel.put("Weather",
                "activity.ActivityWeather");
        IndexModel.put("Bus",
                "com.dingtai.activity.BusActivity");
        IndexModel.put("HuoDong",
                "com.dingtai.dtactivities.activity.ActiveActivity");
        IndexModel.put("LiveActive",
                "com.dingtai.jinriyongzhou.activity.livevideo.LiveActvie");
        IndexModel.put("yaoyaole",
                "com.dingtai.dtshake.activity.ShakeActivity");
        IndexModel.put("zhibo",
                "com.dingtai.jinriyongzhou.activity.LiveListActivity");
        IndexModel.put("dianbo",
                "com.dingtai.dtvoc.activity.DianBoActivity");
        IndexModel.put("zhuanti",
                "com.dingtai.newslib3.activity.NewsTheme");
        IndexModel.put("more",
                "com.dingtai.jinriyongzhou.activity.MoreActivity");
        IndexModel.put("lukuang",
                "com.dingtai.dttraffic.activity.TrafficNewActivity");
    }
}
