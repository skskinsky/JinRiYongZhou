package com.dingtai.jinriyongzhou.application;

import com.dingtai.base.api.API;
import com.dingtai.base.api.AppUploadAPI;

public class MyApplication extends com.dingtai.base.application.MyApplication {
    @Override
    public void onCreate() {
        setPackageName("com.dingtai.jinriyongzhou");
//        API.COMMON_URL = "http://116.62.29.20:8077/";
        API.COMMON_URL = "http://slb1.app.hn0746.com:8888/";
        Corner = 1;
        API.ShareName = "今日永州客户端";
        API.shareContent="向您推荐今日永州新媒体手机客户端，请点击下载，谢谢！";
        API.ICON_URL = "http://slb1.app.hn0746.com:8888/";
        API.ShareURL="http://slb1.app.hn0746.com:8888/APP/hmyDown.html";
        API.SHARE_URL_PR = "http://slb1.app.hn0746.com:8888/";
        API.COMMON_URL_JS = "http://slb1.app.hn0746.com:8888/";
        API.SHARE_URL_GUID="http://slb1.app.hn0746.com:8888/Share/Shares.aspx?guid=";
        API.UPLOAD_FILE_IP_ADDRESS = "124.229.182.232:8888";
        AppUploadAPI.API_UPDATE_VERSION_URL = API.COMMON_URL + "Interface/VersionsAPI.ashx?action=GetVersion&StID=" + "0" + "&VersionType=Android&sign=" + API.sign;
        super.onCreate();
    }

}
