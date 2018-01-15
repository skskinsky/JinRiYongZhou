package com.dingtai.jinriyongzhou.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.application.MyApplication;
import com.dingtai.newslib3.activity.NewsWebView;
import com.dingtai.resource.api.API;

import java.lang.reflect.Field;
import java.util.ArrayList;

import activity.ActivityWeather;
import api.WeatherAPI;
import model.WeatherBean;
import service.WeatherService;

import static com.dingtai.jinriyongzhou.R.id.tv_weizhang;

/**
 * Created by Administrator on 2017/8/26.
 */

public class MinFragment extends BaseFragment implements View.OnClickListener {
    private View mMainView;
    private View background;
    private TextView tv_wendu,tv_qihou,tv_dizhi;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 500:
                    ArrayList<WeatherBean> list = (ArrayList<WeatherBean>) msg.getData().getParcelableArrayList("list")
                            .get(0);
                    WeatherBean bean = list.get(0);
                    getWetherBackPic(bean.getBaitiantianqi());
                    tv_wendu.setText(bean.getZuotiandiwen().substring(2) + "~" + bean.getZuotiangaowen().substring(2));
                    if (bean.getBaitiantianqi().equals(bean.getYewantianqi())){
                        tv_qihou.setText(bean.getYewantianqi());
                    }else {
                        tv_qihou.setText(bean.getBaitiantianqi() + "转" + bean.getYewantianqi());
                    }
                    tv_dizhi.setText(bean.getChengshi());
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.fragment_min, container, false);
        initView();
        getCityWeather();
        return mMainView;
    }

    private void initView() {

        background = mMainView.findViewById(R.id.background);
        tv_wendu = (TextView) mMainView.findViewById(R.id.tv_wendu);
        tv_qihou = (TextView) mMainView.findViewById(R.id.tv_qihou);
        tv_dizhi = (TextView) mMainView.findViewById(R.id.tv_dizhi);

        mMainView.findViewById(R.id.tv_gongjijin).setOnClickListener(this);
        mMainView.findViewById(tv_weizhang).setOnClickListener(this);
        mMainView.findViewById(R.id.tv_shuifei).setOnClickListener(this);
        mMainView.findViewById(R.id.tv_dianfei).setOnClickListener(this);
        mMainView.findViewById(R.id.tv_hangban).setOnClickListener(this);
        mMainView.findViewById(R.id.tv_piaowu).setOnClickListener(this);
        mMainView.findViewById(R.id.tv_jiudian).setOnClickListener(this);
    }

    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.tv_gongjijin:
                intent.putExtra("PageUrl", "http://www.yzsgjj.gov.cn/");
                intent.setClass(getActivity(), NewsWebView.class);
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_weizhang:
                intent.putExtra("PageUrl", "http://m.weizhang8.cn/");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_shuifei:
                intent.putExtra("PageUrl", "https://www.shfft.com/help/help_701245.htm");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_dianfei:
                intent.putExtra("PageUrl", "http://www.95598.cn/person/index.shtml");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_hangban:
                intent.putExtra("PageUrl", "https://m.qunar.com/");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_piaowu:
                intent.putExtra("PageUrl", "http://www.12306.cn/mormhweb/");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
            case R.id.tv_jiudian:
                intent.putExtra("PageUrl", "http://m.ctrip.com/webapp/hotel/?from=http%3A%2F%2Fm.ctrip.com%2Fhtml5%2F");
                intent.setClass(getActivity(), NewsWebView.class);
                startActivity(intent);
                break;
        }

    }

    private String getCityCode(String cityName) {//根据城市名称，到资源文件中找城市id
        Resources res = getActivity().getResources();
        try {
            Field field = R.string.class.getField(cityName);
            int id = field.getInt(new R.string());
            String cityCode = res.getString(id);
            Log.d("xf", cityCode);
            return cityCode;
        } catch (Exception e) {
            Log.e("xf", e.toString());
        }
        return "101250901";
    }

    private String getCityName() {//获取城市名称
        String cityName = ((MyApplication) getActivity().getApplication()).city;
        if (cityName == null) {
            cityName = API.city;
        }
        if (cityName.contains("市")) {
            cityName = cityName.substring(0, cityName.indexOf("市"));
        } else if (cityName.contains("区")) {
            cityName = cityName.substring(0, cityName.indexOf("区"));
        } else if (cityName.contains("县")) {
            cityName = cityName.substring(0, cityName.indexOf("县"));
        } else if (cityName.contains("自治区")) {
            cityName = cityName.substring(0, cityName.indexOf("自治区"));
        }
        return cityName;
    }

    //获取城市天气
    private void getCityWeather() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                String cityCode = getCityCode(getCityName());
                String url = API.COMMON_URL
                        + "Interface/WeatherApi.ashx?action=Weather&CityID="
                        + cityCode + "&STid=" + API.STID;
                requestData(getContext(), url, new Messenger(handler), WeatherAPI.TIANQI_API, WeatherAPI.TIANQI_MSG, WeatherService.class);
            }
        });
    }

    private void getWetherBackPic(String string) {
        // TODO 自动生成的方法存根
        if ("大雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("阵雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("雷阵雨".equals(string)) {
            background.setBackgroundResource(R.drawable.leidian);
            return;
        }
        if ("雷阵雨有冰雹".equals(string)) {
            background.setBackgroundResource(R.drawable.leidian);
            return;
        }
        if ("雨夹雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("中雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("暴雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("大暴雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("特大暴雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("冻雨".equals(string)) {
            background.setBackgroundResource(R.drawable.dayu);
            return;
        }
        if ("多云".equals(string)) {
            background.setBackgroundResource(R.drawable.duoyun);
            return;
        }
        if ("雷电".equals(string)) {
            background.setBackgroundResource(R.drawable.leidian);
            return;
        }
        if ("晴".equals(string)) {
            background.setBackgroundResource(R.drawable.qingtian);
            return;
        }
        if ("雾".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("雾霾".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("沙尘暴".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("浮尘".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("扬沙".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("强沙尘暴".equals(string)) {
            background.setBackgroundResource(R.drawable.wumai);
            return;
        }
        if ("小雨".equals(string)) {
            background.setBackgroundResource(R.drawable.xiaoyu);
            return;
        }
        if ("阵雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("小雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("中雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("大雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("暴雪".equals(string)) {
            background.setBackgroundResource(R.drawable.xuetian);
            return;
        }
        if ("阴".equals(string)) {
            background.setBackgroundResource(R.drawable.yintian);
            return;
        }
    }
}