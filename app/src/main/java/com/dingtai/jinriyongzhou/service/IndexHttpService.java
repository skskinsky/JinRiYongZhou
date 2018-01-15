package com.dingtai.jinriyongzhou.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.base.model.ActiveModel;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.model.NewsPhotoModel;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DecodeStringUtil;
import com.dingtai.base.utils.DecodeUtils;
import com.dingtai.base.utils.HttpUtils;
import com.dingtai.base.utils.JosnOper;
import com.dingtai.dtbaoliao.model.ModelZhiboList;
import com.dingtai.dtflash.api.StartPageAPI;
import com.dingtai.dtflash.model.OpenPicModel;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.dtpolitics.model.WenZhengModel;
import com.dingtai.dtvoc.model.TuiJianProgram;
import com.dingtai.jinriyongzhou.api.HomeAPI;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.bean.ActiveListBean;
import com.dingtai.jinriyongzhou.bean.DianBoDetialListBean;
import com.dingtai.jinriyongzhou.bean.DianBoDetialListBean2;
import com.dingtai.jinriyongzhou.bean.DianBoListBean;
import com.dingtai.jinriyongzhou.bean.PaperNet;
import com.dingtai.jinriyongzhou.bean.StartPics;
import com.dingtai.jinriyongzhou.model.CJIndexNewsListModel;
import com.dingtai.jinriyongzhou.model.HHIndexNewsListModel;
import com.dingtai.jinriyongzhou.model.HomeListModel;
import com.dingtai.jinriyongzhou.model.HomeNewsModel;
import com.dingtai.jinriyongzhou.model.IndexAD;
import com.dingtai.jinriyongzhou.model.IndexModel;
import com.dingtai.jinriyongzhou.model.InstitutionDetailModel;
import com.dingtai.jinriyongzhou.model.MediaComment;
import com.dingtai.jinriyongzhou.model.MediaInfo;
import com.dingtai.jinriyongzhou.model.MediaList;
import com.dingtai.jinriyongzhou.model.ModelZhiboDetail;
import com.dingtai.jinriyongzhou.model.PoliticsNewsModel;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;
import com.dingtai.jinriyongzhou.model.Topice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class IndexHttpService extends IntentService {

    private DataBaseHelper databaseHelper;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public IndexHttpService(String name) {
        super(name);
    }

    @SuppressLint({"SimpleDateFormat"})
    public IndexHttpService() {
        super("com.dingtai.huoliyongzhou.service.IndexHttpService");
    }

    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        return super.onStartCommand(paramIntent, paramInt1, paramInt2);
    }

    /**
     * 将传入的字符串数组转化为URL
     *
     * @param str
     * @return
     */
    public String getUrlByString(String[] str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            if (i < str.length - 1) {
                sb.append(str[i] + "&");
            } else {
                sb.append(str[i]);
            }
        }
        return sb.toString();

    }

    protected void onHandleIntent(Intent intent) {
        int i = intent.getIntExtra("api", 0);
        if (i == 0) {
            return;
        }
        switch (i) {
            case HomeAPI.CJ_INDEX_NEWS_LIST_API:
                get_cj_index_news_list(intent);
                break;
            case IndexAPI.GET_INDEX:
                get_index_model(intent);
                break;
            case IndexAPI.GET_INDEX_MOKUAI:
                get_index_model1(intent);
                break;
            case IndexAPI.GET_INDEX_NEWS:
                get_index_news(intent);
                break;
            case IndexAPI.LIVEROOM_INSERT_API:
                insert_liveroom(intent);
                break;
            case IndexAPI.ZHIBO_EVENT_LIST_API:
                get_liveroom(intent);
                break;
            case IndexAPI.BAOLIAO_DEL_GOODTOP__API:
                removeGoodPoint(intent);
                break;
            case IndexAPI.BAOLIAO_GOODTOP__API:
                addGoodPoint(intent);
                break;
            case IndexAPI.NEWS_MORE_API:
                getMoreNews(intent);
                break;
            case IndexAPI.VIDEO_LIST_ZAN_API:
                zan_videolist(intent);
                break;

            case IndexAPI.VIDEO_LIST_REMOVEZAN_API:
                del_zan_videolist(intent);
                break;
            case API.VIDEO_LIST_API:
                get_video_list(intent);
                break;
            case IndexAPI.LIVEROOM_ZAN_API:
                zan_liveroom(intent);
                break;
            case IndexAPI.LIVEROOM_DEL_ZAN_API:
                del_zan_liveroom(intent);
                break;
            case NewsAPI.ZHIBO_LIST_API:
                get_zhibo_list(intent);
                break;
            case NewsAPI.ZHIBODETAIL_LIST_API:
                get_zhibodetail_list(intent);
                break;
            case IndexAPI.SEARCH_LIVE_LIST_API:
                get_search_livelist(intent);
                break;
            case IndexAPI.SEARCH_DB_LIST_API:
                get_search_dianbolist(intent);
                break;
            case IndexAPI.SEARCH_HD_LIST_API:
                get_search_activelist(intent);
                break;
            case NewsAPI.SEARCH_LIST_API:
                get_search_list(intent);
                break;
            case API.VIDEO_INSERT_COMMENTS_API:
                insert_video_comments(intent);
                break;
            case API.VIDEO_DETAIL_API:
                get_video_detail(intent);
                break;
            case API.VIDEO_COMMENTS_SHANGLA_API:
                video_comments_up(intent);
                break;
            case API.VIDEO_COMMENTS_XIALA_API:
                video_comments_down(intent);
                break;
            case IndexAPI.GET_SUBSCRIBE_LIST:
                get_subscribe_list(intent);
                break;
            case IndexAPI.GET_ZWSUBSCRIBE_LIST:
                get_zwsubscribe_list(intent);
            case IndexAPI.GET_SUBSCRIBE_UPDATE:
                get_zwsubscribe_update(intent);
                break;
            case IndexAPI.GET_NEW_POLITICS:
                get_new_politics(intent);
                break;
            case IndexAPI.GET_LIST_NEWS_API:
                getList(intent);
                break;
            case IndexAPI.GET_INSTITUTION_DETAIL:
                get_institution_detail(intent);
                break;
            case IndexAPI.GET_INSTITUTION_LIST:
                get_institution_list(intent);
                break;
            case IndexAPI.NEWTOPICE_API:
                get_news_newtopice(intent);
                break;
            case IndexAPI.WENZHENG_ADD_API:
                add_institutions_message(intent);
                break;
            case IndexAPI.SUBSCRIBE_STATE_API:
                change_institutions_state(intent);
                break;
            case IndexAPI.PAPER_NET_API:
                paper_net(intent, i);
                break;
            case IndexAPI.START_PICS_API:
                start_pics(intent, i);
                break;
            case IndexAPI.DIANBO_LIST_API:
                dianbo_list(intent, i);
                break;
            case IndexAPI.DIANBO_DETIAL_LIST_API:
                dianbo_detial_list(intent, i);
                break;
            case IndexAPI.ACTIV_UP_LIST_API:
                active_up(intent, i);
                break;
            case IndexAPI.ACTIV_DOWN_LIST_API:
                active_down(intent, i);
                break;
            case IndexAPI.DIANBO_DETIAL_LIST_API2:
                dianbo_detial_list2(intent, i);
                break;
            default:
                break;
        }
    }

    private void dianbo_detial_list2(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            DianBoDetialListBean2 dianBoDetialListBean = gson.fromJson(jsonstr, DianBoDetialListBean2.class);
            DecodeUtils.decode(dianBoDetialListBean.getVODChannelsContent());

            sendMsgToAct(intent, successId, "", dianBoDetialListBean.getVODChannelsContent());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }

    private void active_down(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            ActiveListBean activeListBean = gson.fromJson(jsonstr, ActiveListBean.class);
            DecodeUtils.decode(activeListBean.getActiveList());

            sendMsgToAct(intent, successId, "", activeListBean.getActiveList());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }

    private void active_up(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            ActiveListBean activeListBean = gson.fromJson(jsonstr, ActiveListBean.class);
            DecodeUtils.decode(activeListBean.getActiveList());

            sendMsgToAct(intent, successId, "", activeListBean.getActiveList());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }

    private void dianbo_detial_list(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            DianBoDetialListBean dianBoDetialListBean = gson.fromJson(jsonstr, DianBoDetialListBean.class);
            DecodeUtils.decode(dianBoDetialListBean.getChannleByProgram());

            sendMsgToAct(intent, successId, "", dianBoDetialListBean.getChannleByProgram());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }

    private void dianbo_list(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            DianBoListBean dianBoListBean = gson.fromJson(jsonstr, DianBoListBean.class);
            DecodeUtils.decode(dianBoListBean.getVodClassList());

            sendMsgToAct(intent, successId, "", dianBoListBean.getVodClassList());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }


    // 重写开机页面
    private void start_pics(Intent intent, int successid) {
        // TODO 得到开机画面列表

        String strURLGet = "";// 正式发送请求的链接

        String url = intent.getStringExtra("url");
        String StID = "StID=" + intent.getStringExtra("StID");
        String sign = "sign=" + intent.getStringExtra("sign");
        String ForApp = "ForApp=" + intent.getStringExtra("ForApp");

        String[] arr = new String[]{url, StID, sign, ForApp};
        strURLGet = getUrlByString(arr);
        // 网络数据
        ArrayList<OpenPicModel> opList = new ArrayList<OpenPicModel>();

        boolean isConnect = Assistant.IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", opList);
            return;
        }

        String strJson = "";
        String strError = "";

        try {

            strJson = HttpUtils.GetJsonStrByURL2(strURLGet);
            Gson gson = new Gson();
            StartPics startPics = gson.fromJson(strJson, StartPics.class);
            DecodeUtils.decode(startPics.getOpenPic());
            if (startPics.getOpenPic().get(0).getOpenPicDetail().size() > 0) {
                opList.addAll(startPics.getOpenPic().get(0).getOpenPicDetail());
                sendMsgToAct(intent, successid, "", opList);
            } else {
                sendMsgToAct(intent, successid, "暂无更多数据", null);
            }

        } catch (Exception e) {
            sendMsgToAct(intent, successid, "暂无更多数据", null);
        }
    }

    /**
     * 作 者： 李亚军
     * 时 间：2015-1-26 下午2:30:20
     * 描 述：开机画面数据获取
     *
     * @param intent
     */
    public void get_openpic_list(Intent intent) {
        // TODO 得到开机画面列表
        RuntimeExceptionDao<OpenPicModel, String> op_list = getHelper().getMode(OpenPicModel.class);
        String strURLGet = "";// 正式发送请求的链接

        String url = intent.getStringExtra("url");
        String StID = "StID=" + intent.getStringExtra("StID");
        String sign = "sign=" + intent.getStringExtra("sign");
        String ForApp = "ForApp=" + intent.getStringExtra("ForApp");

        String[] arr = new String[]{url, StID, sign, ForApp};
        strURLGet = getUrlByString(arr);
        // 网络数据
        ArrayList<OpenPicModel> opList = new ArrayList<OpenPicModel>();

        boolean isConnect = Assistant.IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", opList);
            return;
        }

        String strJson = "";
        String strError = "";

        try {

            strJson = HttpUtils.GetJsonStrByURL2(strURLGet);
            JSONObject object = new JSONObject(strJson);
            strJson = object.getString("OpenPic");

            if (HttpUtils.isJson(strJson)) {
                if (strJson.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<OpenPicModel> listdata = json.ConvertJsonToArray(strJson, new TypeToken<List<OpenPicModel>>() {
                    }.getType());
                    // op_list.delete(op_list.queryForAll());
                    for (int i = 0; i < listdata.size(); i++) {
                        OpenPicModel listmodel = new OpenPicModel();

                        // 通用
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getID()).length() > 0) {
                                listmodel.setID(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getID()));
                            } else {
                                Log.d("获取ID：", "数据为空");
                                listmodel.setID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("ID：", "获取失败");
                            listmodel.setID("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getCreateTime()).length() > 0) {
                                listmodel.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getCreateTime()));
                            } else {
                                Log.d("获取创建时间：", "数据为空");
                                listmodel.setCreateTime("-2");
                            }
                        } catch (Exception e) {
                            Log.d("创建时间：", "获取失败");
                            listmodel.setCreateTime("1990-1-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getADContent()).length() > 0) {
                                listmodel.setADContent(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getADContent()));
                            } else {
                                Log.d("获取内容：", "数据为空");
                                listmodel.setADContent(" ");
                            }
                        } catch (Exception e) {
                            Log.d("获取内容：", "获取失败");
                            listmodel.setADContent(" ");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getADFor()).length() > 0) {
                                listmodel.setADFor(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getADFor()));
                            } else {
                                Log.d("获取广告所属：", "数据为空");
                                listmodel.setADFor("-2");
                            }
                        } catch (Exception e) {
                            Log.d("广告所属：", "获取失败");
                            listmodel.setADFor(" -1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getOpenPicName()).length() > 0) {
                                listmodel.setOpenPicName(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getOpenPicName()));
                            } else {
                                Log.d("开机画面名称：", "数据为空");
                                listmodel.setOpenPicName("-2");
                            }
                        } catch (Exception e) {
                            Log.d("开机画面名称：", "获取失败");
                            listmodel.setOpenPicName("服务器未设置开机画面名称");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getImgUrl()).length() > 0) {
                                listmodel.setImgUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getImgUrl()));
                            } else {
                                Log.d("获取图片：", "数据为空");
                                listmodel.setImgUrl("-2");
                            }
                        } catch (Exception e) {
                            Log.d("获取图片：", "获取失败");
                            listmodel.setImgUrl("服务器未设置广告图片");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getLinkUrl()).length() > 0) {
                                listmodel.setLinkUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getLinkUrl()));
                            } else {
                                Log.d("获取链接路径：", "数据为空");
                                listmodel.setLinkUrl("-2");
                            }
                        } catch (Exception e) {
                            Log.d("链接路径：", "获取失败");
                            listmodel.setLinkUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getStID()).length() > 0) {
                                listmodel.setStID(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getStID()));
                            } else {
                                Log.d("获取站点ID：", "数据为空");
                                listmodel.setStID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("站点ID：", "获取失败");
                            listmodel.setStID("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getLinkTo()).length() > 0) {
                                listmodel.setLinkTo(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getLinkTo()));
                            } else {
                                Log.d("获取链接到：", "数据为空");
                                listmodel.setLinkTo("-2");
                            }
                        } catch (Exception e) {
                            Log.d("链接到：", "获取失败");
                            listmodel.setLinkTo("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getChID()).length() > 0) {
                                listmodel.setChID(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getChID()));
                            } else {
                                Log.d("获取栏目ID：", "数据为空");
                                listmodel.setChID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("栏目ID：", "获取失败");
                            listmodel.setChID("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getForApp()).length() > 0) {
                                listmodel.setForApp(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getForApp()));
                            } else {
                                Log.d("获取终端类型：", "数据为空");
                                listmodel.setForApp("-2");
                            }
                        } catch (Exception e) {
                            Log.d("终端类型：", "获取失败");
                            listmodel.setForApp("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getStatus()).length() > 0) {
                                listmodel.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getStatus()));
                            } else {
                                Log.d("获取状态：", "数据为空");
                                listmodel.setStatus("-2");
                            }
                        } catch (Exception e) {
                            Log.d("状态：", "获取失败");
                            listmodel.setStatus("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getRandomNum()).length() > 0) {
                                listmodel.setRandomNum(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getRandomNum()));
                            } else {
                                Log.d("获取 随机数：", "数据为空");
                                listmodel.setRandomNum("-1");
                            }
                        } catch (Exception e) {
                            Log.d("随机数：", "获取失败");
                            listmodel.setRandomNum("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getReMark()).length() > 0) {
                                listmodel.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getReMark()));
                            } else {
                                Log.d("获取 备注：", "数据为空");
                                listmodel.setReMark("-2");
                            }
                        } catch (Exception e) {
                            Log.d("备注：", "获取失败");
                            listmodel.setReMark("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getImgUrls()).length() > 0) {
                                listmodel.setImgUrls(DecodeStringUtil.DecodeBase64ToUTF8(listdata.get(i).getImgUrls()));
                            } else {
                                Log.d("获取 多张图片：", "数据为空");
                                listmodel.setImgUrls("-2");
                            }
                        } catch (Exception e) {
                            Log.d("多张图片：", "获取失败");
                            listmodel.setImgUrls("-1");
                        }
                        try {
                            if (listdata.get(i).getOpenPicDetail() != null && listdata.get(i).getOpenPicDetail().size() > 0) {
                                DecodeUtils.decode(listdata.get(i).getOpenPicDetail());
                                listmodel.setOpenPicDetail(listdata.get(i).getOpenPicDetail());
                            } else {
                                Log.d("获取 多张图片：", "数据为空");
                            }
                        } catch (Exception e) {
                            Log.d("多张图片：", "获取失败");
                        }


                        // TODO: 添加开机数据
                        opList.add(listmodel);
                        if (op_list.isTableExists()) {// 这个是检查数据库是否存在这个对象的
                            op_list.delete(op_list.queryForAll());
                            op_list.create(listmodel);
                        }
                    }

                    opList.addAll(op_list.queryForAll());
                    sendMsgToAct(intent, StartPageAPI.OPENPIC_LIST_API, "", opList);

                    return;
                } else {
                    sendMsgToAct(intent, StartPageAPI.OPENPIC_LIST_API, "暂无更多数据", null);
                    return;
                }
            } else {
                sendMsgToAct(intent, StartPageAPI.OPENPIC_LIST_API, "暂无更多数据", null);
                return;
            }

        } catch (Exception e) {
            sendMsgToAct(intent, StartPageAPI.OPENPIC_LIST_API, "暂无更多数据", null);
        }
    }


    private void paper_net(Intent intent, int successId) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            Gson gson = new Gson();
            PaperNet paperNet = gson.fromJson(jsonstr, PaperNet.class);
            DecodeUtils.decode(paperNet.getNewsChannel());

            sendMsgToAct(intent, successId, "", paperNet.getNewsChannel());
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }
    }


    /**
     * 修改机构订阅状态
     *
     * @param intent
     */
    private void change_institutions_state(Intent intent) {
        String url = intent.getStringExtra("url");
        String GuID = "UserGUID=" + intent.getStringExtra("UserGuID");
        String isAdd = "isAdd=" + intent.getStringExtra("isAdd");
        String PoliticID = "PoliticID=" + intent.getStringExtra("PoliticID");
        String[] arr = new String[]{url, GuID, isAdd, PoliticID};
        String strURLGet = this.getUrlByString(arr);

        String jsonstr = null;
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(strURLGet);
            if (HttpUtils.isJson(jsonstr)) {
                JSONObject job = null;
                job = new JSONObject(jsonstr);
                jsonstr = job.getString("Subscribes");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 提交投稿
     *
     * @param intent
     */
    private void add_institutions_message(Intent intent) {
        String url = intent.getStringExtra("url");
        String StID = "StID=" + intent.getStringExtra("StID");
        String Title = "Title=" + intent.getStringExtra("Title");
        String ResourceContent = "ResourceContent=" + intent.getStringExtra("ResourceContent");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");
        String DepartmentID = "DepartmentID=" + intent.getStringExtra("DepartmentID");
        String ResType = "Restype=" + intent.getStringExtra("ResType");
        String PicPath = "PicPath=" + intent.getStringExtra("PicPath");
        String VideoUrl = "VideoUrl=" + intent.getStringExtra("VideoUrl");
        String UploadType = "UploadType=" + intent.getStringExtra("UploadType");
        String FileDate = "FileDate=" + intent.getStringExtra("FileDate") + "/";
        String[] arr = new String[]{url, StID, Title, ResourceContent, UserGUID, DepartmentID, ResType, PicPath, VideoUrl, UploadType, FileDate};
        String strURLGet = this.getUrlByString(arr);

        try {
            String e = HttpUtils.GetJsonStrByURL2(strURLGet);
            if (e.indexOf("Success") != -1) {
                this.sendMsgToAct(intent, 100, "提问成功", (List) null);
            } else {
                this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            }
        } catch (HttpHostConnectException var22) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var22.printStackTrace();
        } catch (ConnectTimeoutException var23) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var23.printStackTrace();
        } catch (SocketTimeoutException var24) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var24.printStackTrace();
        } catch (ClientProtocolException var25) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var25.printStackTrace();
        } catch (JSONException var26) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var26.printStackTrace();
        } catch (IOException var27) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var27.printStackTrace();
        } catch (Exception var28) {
            this.sendMsgToAct(intent, 0, "提问失败", (List) null);
            var28.printStackTrace();
        }

    }

    /**
     * 获取永州号列表
     *
     * @param intent
     */
    private void get_institution_list(Intent intent) {
        String url = intent.getStringExtra("url");
        String ID = "DeptID=" + intent.getStringExtra("ID");
        String[] strs = new String[]{url, ID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<PoliticsNewsModel> tempList = new ArrayList<>();
        if (HttpUtils.isJson(jsonstr)) {
            JSONObject job = null;
            try {
                job = new JSONObject(jsonstr);
                jsonstr = job.getString("PoliticsNews");
                JosnOper json = new JosnOper();
                List<PoliticsNewsModel> list = json.ConvertJsonToArray(jsonstr, new TypeToken<List<PoliticsNewsModel>>() {
                }.getType());
                for (PoliticsNewsModel m : list) {
                    PoliticsNewsModel model = new PoliticsNewsModel();
                    model.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                    model.setAreaLogo(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaLogo()));
                    model.setAuditTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getAuditTime()));
                    model.setBandChID(DecodeStringUtil.DecodeBase64ToUTF8(m.getBandChID()));
                    model.setChannelLogo(DecodeStringUtil.DecodeBase64ToUTF8(m.getChannelLogo()));
                    model.setChannelName(DecodeStringUtil.DecodeBase64ToUTF8(m.getChannelName()));
                    model.setChID(DecodeStringUtil.DecodeBase64ToUTF8(m.getChID()));
                    model.setCommentNum(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentNum()));
                    model.setCommunityName(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommunityName()));
                    model.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getCreateTime()));
                    model.setFakeReadNo(DecodeStringUtil.DecodeBase64ToUTF8(m.getFakeReadNo()));
                    model.setForAPP(DecodeStringUtil.DecodeBase64ToUTF8(m.getForAPP()));
                    model.setGetGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(m.getGetGoodPoint()));
                    model.setID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                    model.setIsComment(DecodeStringUtil.DecodeBase64ToUTF8(m.getIsComment()));
                    model.setIsCommentNoName(DecodeStringUtil.DecodeBase64ToUTF8(m.getIsCommentNoName()));
                    model.setIsNewTopice(DecodeStringUtil.DecodeBase64ToUTF8(m.getIsNewTopice()));
                    model.setLatitude(DecodeStringUtil.DecodeBase64ToUTF8(m.getLatitude()));
                    model.setLongitude(DecodeStringUtil.DecodeBase64ToUTF8(m.getLongitude()));
                    model.setPicPath(DecodeStringUtil.DecodeBase64ToUTF8(m.getPicPath()));
                    model.setPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticsAreaName()));
                    model.setReadNo(DecodeStringUtil.DecodeBase64ToUTF8(m.getReadNo()));
                    model.setResourceCSS(DecodeStringUtil.DecodeBase64ToUTF8(m.getResourceCSS()));
                    model.setResourceFlag(DecodeStringUtil.DecodeBase64ToUTF8(m.getResourceFlag()));
                    model.setResourceGUID(DecodeStringUtil.DecodeBase64ToUTF8(m.getResourceGUID()));
                    model.setResourceType(DecodeStringUtil.DecodeBase64ToUTF8(m.getResourceType()));
                    model.setResourceUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getResourceUrl()));
                    model.setResType(DecodeStringUtil.DecodeBase64ToUTF8(m.getResType()));
                    model.setRPID(DecodeStringUtil.DecodeBase64ToUTF8(m.getRPID()));
                    model.setRPNum(DecodeStringUtil.DecodeBase64ToUTF8(m.getRPNum()));
                    model.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(m.getShowOrder()));
                    model.setSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getSmallPicUrl()));
                    model.setSourceForm(DecodeStringUtil.DecodeBase64ToUTF8(m.getSourceForm()));
                    model.setSummary(DecodeStringUtil.DecodeBase64ToUTF8(m.getSummary()));
                    model.setThemeID(DecodeStringUtil.DecodeBase64ToUTF8(m.getThemeID()));
                    model.setTitle(DecodeStringUtil.DecodeBase64ToUTF8(m.getTitle()));
                    model.setUpdateTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getUpdateTime()));
                    model.setUploadPicNames(DecodeStringUtil.DecodeBase64ToUTF8(m.getUploadPicNames()));
                    model.setVideoCount(DecodeStringUtil.DecodeBase64ToUTF8(m.getVideoCount()));
                    model.setVideoImageUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getVideoImageUrl()));
                    model.setVideoUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getVideoUrl()));
                    tempList.add(model);
                }
                sendMsgToAct(intent, 602, "", tempList);
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }


    /**
     * 获取永州号模块详情与专题
     *
     * @param intent
     */
    private void get_news_newtopice(Intent intent) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant.IsContectInterNet2(this.getApplicationContext());
        if (!isConnect) {
            this.sendMsgToAct(intent, 222, "请检查网络连接！", (List) null);
        } else {
            String jsonstr = "";

            try {
                jsonstr = HttpUtils.GetJsonStrByURL2(url);
                JosnOper e = new JosnOper();
                Topice topice = (Topice) JosnOper.ConvertJsonToModel(jsonstr, Topice.class);
                ArrayList listdata = new ArrayList();
                listdata.add(topice);
                DecodeUtils.decode(listdata);
                if (listdata.size() > 0) {
                    this.sendMsgToAct(intent, 100, "", listdata);
                } else {
                    this.sendMsgToAct(intent, 555, "暂无数据!", (List) null);
                }
            } catch (HttpHostConnectException var8) {
                this.sendMsgToAct(intent, 555, "", (List) null);
                var8.printStackTrace();
            } catch (ConnectTimeoutException var9) {
                this.sendMsgToAct(intent, 555, "网络连接超时！", (List) null);
                var9.printStackTrace();
            } catch (SocketTimeoutException var10) {
                this.sendMsgToAct(intent, 555, "网络连接超时！", (List) null);
                var10.printStackTrace();
            } catch (ClientProtocolException var11) {
                this.sendMsgToAct(intent, 555, "", (List) null);
                var11.printStackTrace();
            } catch (JSONException var12) {
                this.sendMsgToAct(intent, 555, "", (List) null);
                var12.printStackTrace();
            } catch (IOException var13) {
                this.sendMsgToAct(intent, 555, "", (List) null);
                var13.printStackTrace();
            } catch (Exception var14) {
                this.sendMsgToAct(intent, 555, "", (List) null);
                var14.printStackTrace();
            }

        }
    }


    /**
     * 获取永州号模块数据详情
     *
     * @param intent
     */
    private void get_institution_detail(Intent intent) {
        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String[] strs = new String[]{url, ID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<InstitutionDetailModel> tempList = new ArrayList<>();
        if (HttpUtils.isJson(jsonstr)) {
            JSONObject job = null;
            try {
                job = new JSONObject(jsonstr);
                jsonstr = job.getString("PoliticsArea");
                JosnOper json = new JosnOper();
                List<InstitutionDetailModel> list = json.ConvertJsonToArray(jsonstr, new TypeToken<List<InstitutionDetailModel>>() {
                }.getType());
                for (InstitutionDetailModel m : list) {
                    InstitutionDetailModel s = new InstitutionDetailModel();
                    s.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaID()));
                    s.setAreaCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaCreateTime()));
                    s.setAreaEnName(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaEnName()));
                    s.setAreaIsChoose(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaIsChoose()));
                    s.setAreaPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaPoliticsAreaName()));
                    s.setAreaReMark(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaReMark()));
                    s.setAreaSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaSmallPicUrl()));
                    s.setAreaEditor(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaEditor()));
                    tempList.add(s);
                }
                sendMsgToAct(intent, 601, "", tempList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * description: 获取首页数据
     * autour: xf
     * date: 2017/10/20 0020 下午 4:48
     * version:1.0
     */
    private void getList(Intent intent) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("ResList");
            JosnOper json = new JosnOper();
            List<HomeNewsModel> listdate = json.ConvertJsonToArray(jsonstr, new TypeToken<List<HomeNewsModel>>() {
            }.getType());
            DecodeUtils.decode(listdate);
            sendMsgToAct(intent, 200, "", listdate);
            if (url.contains("dtop=0")) {
                RuntimeExceptionDao<HomeNewsModel, String> mode = getHelper().getMode(HomeNewsModel.class);
                mode.delete(mode.queryForAll());
                for (int i = 0; i < listdate.size(); i++) {
                    mode.createIfNotExists(listdate.get(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMsgToAct(intent, 404, "数据异常！", null);
        }

    }

    /**
     * 获取政务中心大厅数据
     *
     * @param intent
     */
    private void get_new_politics(Intent intent) {
        ArrayList completeData = new ArrayList();
        ArrayList politicsAreaList = new ArrayList();
        ArrayList politicsInfoList = new ArrayList();
        ArrayList politicsNewsList = new ArrayList();

        String url = intent.getStringExtra("url");
        String StID = intent.getStringExtra("StID");
        String[] strs = new String[]{url, StID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (HttpUtils.isJson(jsonstr)) {
            try {
                JSONObject jsonArray = new JSONObject(jsonstr);
                String strarea = jsonArray.getString("PoliticsIndexArea");
                String strleader = jsonArray.getString("PoliticsLeader");
                String strnews = jsonArray.getString("PoliticsNews");
                if ("[]".equals(jsonstr)) {
                    this.sendMsgToAct(intent, 10, "暂无数据", null);
                    return;
                }
                if (HttpUtils.isJson(jsonstr) && jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List leaderdata = json.ConvertJsonToArray(strleader, (new TypeToken() {
                    }).getType());
                    List newsdata = json.ConvertJsonToArray(strnews, (new TypeToken() {
                    }).getType());
                    List areadata = json.ConvertJsonToArray(strarea, (new TypeToken() {
                    }).getType());

                    for (int i = 0; i < areadata.size(); ++i) {
                        PoliticsAreaModel var72 = new PoliticsAreaModel();
                        var72.setAreaCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaCreateTime()));
                        var72.setAreaEnName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaEnName()));
                        var72.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaID()));
                        var72.setAreaIsChoose(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaIsChoose()));
                        var72.setAreaParentID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaParentID()));
                        var72.setAreaPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaPoliticsAreaName()));
                        var72.setAreaReMark(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaReMark()));
                        var72.setAreaShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaShowOrder()));
                        var72.setAreaStatus(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaStatus()));
                        var72.setAreaStID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaStID()));
                        var72.setAreaSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getAreaSmallPicUrl()));
                        var72.setIsRec(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) areadata.get(i)).getIsRec()));
                        politicsAreaList.add(var72);
                    }
                    for (int i = 0; i < leaderdata.size(); ++i) {
                        PoliticsAreaModel var72 = new PoliticsAreaModel();
                        var72.setAreaCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaCreateTime()));
                        var72.setAreaEnName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaEnName()));
                        var72.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaID()));
                        var72.setAreaIsChoose(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaIsChoose()));
                        var72.setAreaParentID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaParentID()));
                        var72.setAreaPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaPoliticsAreaName()));
                        var72.setAreaReMark(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaReMark()));
                        var72.setAreaShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaShowOrder()));
                        var72.setAreaStatus(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaStatus()));
                        var72.setAreaStID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaStID()));
                        var72.setAreaSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getAreaSmallPicUrl()));
                        var72.setIsRec(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsAreaModel) leaderdata.get(i)).getIsRec()));
                        politicsInfoList.add(var72);
                    }
                    for (int i = 0; i < politicsInfoList.size(); ++i) {
                        WenZhengModel var73 = new WenZhengModel();

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getID()).length() > 0) {
                                var73.setID(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getID()));
                            } else {
                                var73.setID("0");
                            }
                        } catch (Exception var64) {
                            var73.setID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getCreateTime()).length() > 0) {
                                var73.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getCreateTime()));
                            } else {
                                var73.setCreateTime("0");
                            }
                        } catch (Exception var63) {
                            var73.setCreateTime("1900-1-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsTitle()).length() > 0) {
                                var73.setPoliticsTitle(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsTitle()));
                            } else {
                                var73.setPoliticsTitle("0");
                            }
                        } catch (Exception var62) {
                            var73.setPoliticsTitle("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsContent()).length() > 0) {
                                var73.setPoliticsContent(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsContent()));
                            } else {
                                var73.setPoliticsContent("0");
                            }
                        } catch (Exception var61) {
                            var73.setPoliticsContent("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsType()).length() > 0) {
                                var73.setPoliticsType(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsType()));
                            } else {
                                var73.setPoliticsType("0");
                            }
                        } catch (Exception var60) {
                            var73.setPoliticsType("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsTypeCombine()).length() > 0) {
                                var73.setPoliticsTypeCombine(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsTypeCombine()));
                            } else {
                                var73.setPoliticsTypeCombine("0");
                            }
                        } catch (Exception var59) {
                            var73.setPoliticsTypeCombine("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicUrl()).length() > 0) {
                                var73.setPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicUrl()));
                            } else {
                                var73.setPicUrl("0");
                            }
                        } catch (Exception var58) {
                            var73.setPicUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicMidUrl()).length() > 0) {
                                var73.setPicMidUrl(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicMidUrl()));
                            } else {
                                var73.setPicMidUrl("0");
                            }
                        } catch (Exception var57) {
                            var73.setPicMidUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicSmallUrl()).length() > 0) {
                                var73.setPicSmallUrl(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicSmallUrl()));
                            } else {
                                var73.setPicSmallUrl("0");
                            }
                        } catch (Exception var56) {
                            var73.setPicSmallUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicCount()).length() > 0) {
                                var73.setPicCount(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPicCount()));
                            } else {
                                var73.setPicCount("0");
                            }
                        } catch (Exception var55) {
                            var73.setPicCount("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserLOGO()).length() > 0) {
                                var73.setUserLOGO(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserLOGO()));
                            } else {
                                var73.setUserLOGO("0");
                            }
                        } catch (Exception var54) {
                            var73.setUserLOGO("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoUrl()).length() > 0) {
                                var73.setVideoUrl(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoUrl()));
                            } else {
                                var73.setVideoUrl("0");
                            }
                        } catch (Exception var53) {
                            var73.setVideoUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoCount()).length() > 0) {
                                var73.setVideoCount(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoCount()));
                            } else {
                                var73.setVideoCount("0");
                            }
                        } catch (Exception var52) {
                            var73.setVideoCount("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoImageUrl()).length() > 0) {
                                var73.setVideoImageUrl(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getVideoImageUrl()));
                            } else {
                                var73.setVideoImageUrl("0");
                            }
                        } catch (Exception var51) {
                            var73.setVideoImageUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsPublic()).length() > 0) {
                                var73.setIsPublic(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsPublic()));
                            } else {
                                var73.setIsPublic("0");
                            }
                        } catch (Exception var50) {
                            var73.setIsPublic("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsReply()).length() > 0) {
                                var73.setIsReply(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsReply()));
                            } else {
                                var73.setIsReply("0");
                            }
                        } catch (Exception var49) {
                            var73.setIsReply("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReply()).length() > 0) {
                                var73.setReply(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReply()));
                            } else {
                                var73.setReply("0");
                            }
                        } catch (Exception var48) {
                            var73.setReply("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReplyTime()).length() > 0) {
                                var73.setReplyTime(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReplyTime()));
                            } else {
                                var73.setReplyTime("0");
                            }
                        } catch (Exception var47) {
                            var73.setReplyTime("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReplyDepartment()).length() > 0) {
                                var73.setReplyDepartment(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReplyDepartment()));
                            } else {
                                var73.setReplyDepartment("0");
                            }
                        } catch (Exception var46) {
                            var73.setReplyDepartment("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getProgress()).length() > 0) {
                                var73.setProgress(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getProgress()));
                            } else {
                                var73.setProgress("0");
                            }
                        } catch (Exception var45) {
                            var73.setProgress("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsAreaCode()).length() > 0) {
                                var73.setPoliticsAreaCode(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsAreaCode()));
                            } else {
                                var73.setPoliticsAreaCode("0");
                            }
                        } catch (Exception var44) {
                            var73.setPoliticsAreaCode("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsAreaName()).length() > 0) {
                                var73.setPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getPoliticsAreaName()));
                            } else {
                                var73.setPoliticsAreaName("0");
                            }
                        } catch (Exception var43) {
                            var73.setPoliticsAreaName("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsNoName()).length() > 0) {
                                var73.setIsNoName(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsNoName()));
                            } else {
                                var73.setIsNoName("0");
                            }
                        } catch (Exception var42) {
                            var73.setIsNoName("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getAuditAdminID()).length() > 0) {
                                var73.setAuditAdminID(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getAuditAdminID()));
                            } else {
                                var73.setAuditAdminID("0");
                            }
                        } catch (Exception var41) {
                            var73.setAuditAdminID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getAuditTime()).length() > 0) {
                                var73.setAuditTime(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getAuditTime()));
                            } else {
                                var73.setAuditTime("0");
                            }
                        } catch (Exception var40) {
                            var73.setAuditTime("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsComment()).length() > 0) {
                                var73.setIsComment(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getIsComment()));
                            } else {
                                var73.setIsComment("0");
                            }
                        } catch (Exception var39) {
                            var73.setIsComment("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUploadType()).length() > 0) {
                                var73.setUploadType(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUploadType()));
                            } else {
                                var73.setUploadType("0");
                            }
                        } catch (Exception var38) {
                            var73.setUploadType("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReadNo()).length() > 0) {
                                var73.setReadNo(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReadNo()));
                            } else {
                                var73.setReadNo("0");
                            }
                        } catch (Exception var37) {
                            var73.setReadNo("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserGUID()).length() > 0) {
                                var73.setUserGUID(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserGUID()));
                            } else {
                                var73.setUserGUID("0");
                            }
                        } catch (Exception var36) {
                            var73.setUserGUID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserPhone()).length() > 0) {
                                var73.setUserPhone(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserPhone()));
                            } else {
                                var73.setUserPhone("0");
                            }
                        } catch (Exception var35) {
                            var73.setUserPhone("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getCommentCount()).length() > 0) {
                                var73.setCommentCount(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getCommentCount()));
                            } else {
                                var73.setCommentCount("0");
                            }
                        } catch (Exception var34) {
                            var73.setCommentCount("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserRealName()).length() > 0) {
                                var73.setUserRealName(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getUserRealName()));
                            } else {
                                var73.setUserRealName("0");
                            }
                        } catch (Exception var33) {
                            var73.setUserRealName("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getShowOrder()).length() > 0) {
                                var73.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getShowOrder()));
                            } else {
                                var73.setShowOrder("0");
                            }
                        } catch (Exception var32) {
                            var73.setShowOrder("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getStID()).length() > 0) {
                                var73.setStID(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getStID()));
                            } else {
                                var73.setStID("0");
                            }
                        } catch (Exception var31) {
                            var73.setStID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getStatus()).length() > 0) {
                                var73.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getStatus()));
                            } else {
                                var73.setStatus("0");
                            }
                        } catch (Exception var30) {
                            var73.setStatus("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReMark()).length() > 0) {
                                var73.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(((WenZhengModel) politicsInfoList.get(i)).getReMark()));
                            } else {
                                var73.setReMark("0");
                            }
                        } catch (Exception var29) {
                            var73.setReMark("-1");
                        }

                        politicsInfoList.add(var73);
                    }

                    for (int i = 0; i < newsdata.size(); ++i) {
                        PoliticsNewsModel model = new PoliticsNewsModel();
                        model.setID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getID()));
                        model.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getAreaID()));
                        model.setAreaLogo(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getAreaLogo()));
                        model.setAuditTime(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getAuditTime()));
                        model.setBandChID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getBandChID()));
                        model.setChannelLogo(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getChannelLogo()));
                        model.setChannelName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getChannelName()));
                        model.setCommentNum(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getCommentNum()));
                        model.setCommunityName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getCommunityName()));
                        model.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getCreateTime()));
                        model.setFakeReadNo(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getFakeReadNo()));
                        model.setGetGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getGetGoodPoint()));
                        model.setIsComment(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getIsComment()));
                        model.setIsCommentNoName(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getIsCommentNoName()));
                        model.setIsNewTopice(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getIsNewTopice()));
                        model.setLatitude(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getLatitude()));
                        model.setLongitude(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getLongitude()));
                        model.setPicPath(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getPicPath()));
                        model.setResourceCSS(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getResourceCSS()));
                        model.setResourceFlag(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getResourceFlag()));
                        model.setResourceGUID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getResourceGUID()));
                        model.setResourceType(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getResourceType()));
                        model.setResourceUrl(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getResourceUrl()));
                        model.setRPID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getRPID()));
                        model.setReadNo(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getReadNo()));
                        model.setRPNum(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getRPNum()));
                        model.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getShowOrder()));
                        model.setSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getSmallPicUrl()));
                        model.setSourceForm(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getSourceForm()));
                        model.setSummary(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getSummary()));
                        model.setThemeID(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getThemeID()));
                        model.setTitle(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getTitle()));
                        model.setUpdateTime(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getUpdateTime()));
                        model.setUploadPicNames(DecodeStringUtil.DecodeBase64ToUTF8(((PoliticsNewsModel) newsdata.get(i)).getUploadPicNames()));
                        politicsNewsList.add(model);
                    }

                    completeData.add(politicsAreaList);
                    completeData.add(politicsInfoList);
                    completeData.add(politicsNewsList);
                    this.sendMsgToAct(intent, 330, "", completeData);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新订阅状态
     *
     * @param intent
     */
    private void get_zwsubscribe_update(Intent intent) {
        String url = intent.getStringExtra("url");
        String GUID = "UserGUID=" + intent.getStringExtra("GuID");
        String isAdd = "isAdd=" + intent.getStringExtra("isAdd");
        String PoliticID = "PoliticID=" + intent.getStringExtra("PoliticID");
        String[] strs = new String[]{url, GUID, isAdd, PoliticID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (HttpUtils.isJson(jsonstr)) {
            try {
                JSONObject job = new JSONObject(jsonstr);
                JSONArray ja = job.getJSONArray("Subscribes");
                JSONObject ob = (JSONObject) ja.get(0);
                String str = ob.getString("Result");
                String st = DecodeStringUtil.DecodeBase64ToUTF8(ob.getString("Message"));
                if ("Failed".equals(str)) {
                    sendMsgToAct(intent, 2220, st, null);
                } else {
                    sendMsgToAct(intent, 2221, st, null);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 获取我的订阅
     *
     * @param intent
     */
    private void get_subscribe_list(Intent intent) {
        //http://116.62.29.20:8077/Interface/PoliticsAPI.ashx?action=SubscribeForMy&UserGUID=8c8b5b5b-dbfb-4162-b7f7-dee431a6a052

        String url = intent.getStringExtra("url");
        String GUID = "UserGUID=" + intent.getStringExtra("GUID");
        String[] strs = new String[]{url, GUID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<SubscribeListsModel> tempList = new ArrayList<>();
        if (HttpUtils.isJson(jsonstr)) {
            try {
                JSONObject job = new JSONObject(jsonstr);
                jsonstr = job.getString("SubscribeLists");
                JosnOper json = new JosnOper();
                List<SubscribeListsModel> list = json.ConvertJsonToArray(jsonstr, new TypeToken<List<SubscribeListsModel>>() {
                }.getType());
                for (SubscribeListsModel m : list) {
                    SubscribeListsModel s = new SubscribeListsModel();
                    s.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getCreateTime()));
                    s.setID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                    s.setIsPolitic(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticID()));
                    s.setPoliticID(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticID()));
                    s.setPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticsAreaName()));
                    s.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(m.getReMark()));
                    s.setSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getSmallPicUrl()));
                    s.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(m.getStatus()));
                    s.setAreaID(DecodeStringUtil.DecodeBase64ToUTF8(m.getAreaID()));
                    tempList.add(s);
                }
                sendMsgToAct(intent, 220, "", tempList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 获取永州号
     *
     * @param intent
     */
    private void get_zwsubscribe_list(Intent intent) {
        //http://116.62.29.20:8077/Interface/PoliticsAPI.ashx?action=SubscribeForMy&UserGUID=8c8b5b5b-dbfb-4162-b7f7-dee431a6a052

        String url = intent.getStringExtra("url");
        String GUID = "UserGUID=" + intent.getStringExtra("GUID");
        String[] strs = new String[]{url, GUID};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<SubscribeListsModel> tempList = new ArrayList<>();
        if (HttpUtils.isJson(jsonstr)) {
            try {
                JSONObject job = new JSONObject(jsonstr);
                jsonstr = job.getString("PoliticsAreas");
                JosnOper json = new JosnOper();
                List<SubscribeListsModel> list = json.ConvertJsonToArray(jsonstr, new TypeToken<List<SubscribeListsModel>>() {
                }.getType());
                for (SubscribeListsModel m : list) {
                    SubscribeListsModel s = new SubscribeListsModel();
                    s.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getCreateTime()));
                    s.setID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                    s.setIsPolitic(DecodeStringUtil.DecodeBase64ToUTF8(m.getIsPolitic()));
                    s.setPoliticID(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticID()));
                    s.setPoliticsAreaName(DecodeStringUtil.DecodeBase64ToUTF8(m.getPoliticsAreaName()));
                    s.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(m.getReMark()));
                    s.setSmallPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(m.getSmallPicUrl()));
                    s.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(m.getStatus()));
                    tempList.add(s);
                }
                sendMsgToAct(intent, 221, "", tempList);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 视频评论下拉
     *
     * @param intent
     */
    public void video_comments_down(Intent intent) {
        // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=GetMediaCommentList
        // &num=10&MID=209eda29-28b0-4f4d-9d53-37c9765355ca&sign=1

        String url = intent.getStringExtra("url");
        String num = "num=" + intent.getStringExtra("num");
        String MID = "MID=" + intent.getStringExtra("MID");
        String sign = "sign=" + intent.getStringExtra("sign");
        String[] strs = new String[]{url, num, MID, sign};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<MediaComment> temp_list = new ArrayList<MediaComment>();
        if (HttpUtils.isJson(jsonstr)) {
            // {"MediaComment":[
            // {"CommentContent":"cXdzYXNhc2F3cQ==","CommentAuditStatus":"MQ==",
            // "CommentTime":"MjAxNC0xMS0yNiAxMTowMDo0MQ==","GetGoodPoint":"MA==",
            // "ID":"NQ==","MID":"MjA5ZWRhMjktMjhiMC00ZjRkLTlkNTMtMzdjOTc2NTM1NWNh",
            // "UserGUID":"ODcwNTJlNjUtYjkzOS00ZmZkLWE3ODUtMTZiOTRlZjdjOGZl","UserName":"","UserIcon":""},
            try {
                JSONObject job = new JSONObject(jsonstr);
                jsonstr = job.getString("MediaComment");
                JosnOper json = new JosnOper();
                List<MediaComment> listdate = json.ConvertJsonToArray(jsonstr, new TypeToken<List<MediaComment>>() {
                }.getType());
                for (MediaComment m : listdate) {
                    MediaComment e = new MediaComment();
                    e.setCommentAuditStatus(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentAuditStatus()));
                    e.setCommentContent(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentContent()));
                    e.setCommentTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentTime()));
                    e.setGetGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(m.getGetGoodPoint()));
                    e.setID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                    e.setMID(DecodeStringUtil.DecodeBase64ToUTF8(m.getMID()));
                    e.setUserGUID(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserGUID()));
                    e.setUserIcon(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserIcon()));
                    e.setUserName(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserName()));
                    e.setUserNickName(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserNickName()));
                    temp_list.add(e);
                }

                sendMsgToAct(intent, 2001, "", temp_list);

            } catch (JSONException e) {
                sendMsgToAct(intent, API.HANDLER_GETDATA_ERROR, "JSONException异常", null);
                e.printStackTrace();
            }

        }

    }

    /**
     * 视频评论上拉
     *
     * @param intent
     */
    public void video_comments_up(Intent intent) {
        // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=GetMediaCommentListShangla&
        // top=10&dtop=1&MID=209eda29-28b0-4f4d-9d53-37c9765355ca&sign=1

        String url = intent.getStringExtra("url");
        String top = "top=" + intent.getStringExtra("top");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String MID = "MID=" + intent.getStringExtra("MID");
        String sign = "sign=" + intent.getStringExtra("sign");
        String[] strs = new String[]{url, top, dtop, MID, sign};
        String url2 = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url2);
        List<MediaComment> temp_list = new ArrayList<MediaComment>();
        if (HttpUtils.isJson(jsonstr)) {
            // {"MediaComment":[
            // {"CommentContent":"cXdzYXNhc2F3cQ==","CommentAuditStatus":"MQ==",
            // "CommentTime":"MjAxNC0xMS0yNiAxMTowMDo0MQ==","GetGoodPoint":"MA==",
            // "ID":"NQ==","MID":"MjA5ZWRhMjktMjhiMC00ZjRkLTlkNTMtMzdjOTc2NTM1NWNh",
            // "UserGUID":"ODcwNTJlNjUtYjkzOS00ZmZkLWE3ODUtMTZiOTRlZjdjOGZl","UserName":"","UserIcon":""},
            try {
                JSONObject job = new JSONObject(jsonstr);
                jsonstr = job.getString("MediaComment");
                JosnOper json = new JosnOper();
                List<MediaComment> listdate = json.ConvertJsonToArray(jsonstr, new TypeToken<List<MediaComment>>() {
                }.getType());
                if (listdate != null && listdate.size() == 0) {
                    sendMsgToAct(intent, 2003, "暂无更多评论", null);
                } else {
                    for (MediaComment m : listdate) {
                        MediaComment e = new MediaComment();
                        e.setCommentAuditStatus(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentAuditStatus()));
                        e.setCommentContent(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentContent()));
                        e.setCommentTime(DecodeStringUtil.DecodeBase64ToUTF8(m.getCommentTime()));
                        e.setGetGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(m.getGetGoodPoint()));
                        e.setID(DecodeStringUtil.DecodeBase64ToUTF8(m.getID()));
                        e.setMID(DecodeStringUtil.DecodeBase64ToUTF8(m.getMID()));
                        e.setUserGUID(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserGUID()));
                        e.setUserIcon(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserIcon()));
                        e.setUserName(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserName()));
                        e.setUserNickName(DecodeStringUtil.DecodeBase64ToUTF8(m.getUserNickName()));
                        temp_list.add(e);
                    }
                }

                sendMsgToAct(intent, 2001, "", temp_list);

            } catch (JSONException e) {
                sendMsgToAct(intent, API.HANDLER_GETDATA_ERROR, "JSONException异常", null);
                e.printStackTrace();
            }

        }

    }

    /**
     * 得到视频详情
     *
     * @param intent
     */
    public void get_video_detail(Intent intent) {
        // http://192.168.1.12:809/interface/MediaAPI.ashx?action=GetVideoInfobyId&VideoID=739
        String url = intent.getStringExtra("url");
        String VideoID = "VideoID=" + intent.getStringExtra("VideoID");

        String[] strs = new String[]{url, VideoID};

        url = getUrlByString(strs);

        String Detailresult = HttpUtils.GetJsonStrByURL(url);
        String jsonstr = "";
        ArrayList<MediaInfo> am = new ArrayList<MediaInfo>();
        if (HttpUtils.isJson(Detailresult)) {
            JSONObject object;
            try {
                object = new JSONObject(Detailresult);
                jsonstr = object.getString("MediaInfo");
                if (HttpUtils.isJson(jsonstr)) {
                    if (jsonstr.indexOf("-1") <= -1) {
                        JosnOper json = new JosnOper();
                        List<MediaInfo> listdate = json.ConvertJsonToArray(jsonstr, new TypeToken<List<MediaInfo>>() {
                        }.getType());
                        for (int i = 0; i < listdate.size(); i++) {
                            MediaInfo m = new MediaInfo();
                            m.setDetail(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getDetail()));
                            m.setDigCount(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getDigCount()));
                            m.setFileSize(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getFileSize()));
                            m.setID(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getID()));
                            m.setID2(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getID2()));
                            m.setImageUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getImageUrl()));
                            m.setMediaUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getMediaUrl()));
                            m.setName(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getName()));
                            m.setPaiCount(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getPaiCount()));
                            m.setTag(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getTag()));
                            m.setUploadDate(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUploadDate()));
                            m.setUserName(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUserName()));
                            m.setFlvUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getFlvUrl()));
                            m.setFileSize(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getFileSize()));
                            m.setUploadType(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUploadType()));
                            m.setFenxiang(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getFenxiang()));
                            m.setChannelName(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getChannelName()));
                            am.add(m);
                        }
                        sendMsgToAct(intent, API.HANDLER_GETDATA_SUCCESS, "", am);
                    }
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

    }

    /**
     * 插入视频评论
     *
     * @param intent
     */
    public void insert_video_comments(Intent intent) {
        // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=InsertMediaComment&
        // commentContent=qwsasasawq&userGUID=87052e65-b939-4ffd-a785-16b94ef7c8fe&MID=209eda29-28b0-4f4d-9d53-37c9765355ca
        String url = intent.getStringExtra("url");
        String commentContent = "commentContent=" + intent.getStringExtra("commentContent");
        String userGUID = "userGUID=" + intent.getStringExtra("userGUID");
        String MID = "MID=" + intent.getStringExtra("MID");
        String StID = "StID=" + API.STID;
        String[] strs = new String[]{url, commentContent, userGUID, MID, StID};
        url = getUrlByString(strs);
        String jsonstr = HttpUtils.GetJsonStrByURL(url);
        if (HttpUtils.isJson(jsonstr)) {
            // {"MediaList":[{"Result":"Failed","ErrorMessage":"ZGF0YSBpcyBub25lIQ=="}]}
            try {
                JSONObject job = new JSONObject(jsonstr);
                JSONArray ja = job.getJSONArray("comments");
                JSONObject ob = (JSONObject) ja.get(0);
                String str = ob.getString("Result");
                if ("Failed".equals(str)) {
                    String st = DecodeStringUtil.DecodeBase64ToUTF8(ob.getString("ErrorMessage"));
                    sendMsgToAct(intent, 2000, st, null);
                } else {
                    sendMsgToAct(intent, 2002, "", null);
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }

        } else {
            String st = "评论失败！";
            sendMsgToAct(intent, 2000, st, null);
        }

        // {"comments":[{"Result":"Success","ID":""}]}
    }

    public void get_search_list(Intent intent) {
        String url = intent.getStringExtra("url");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String num = "num=" + intent.getStringExtra("num");
        String StID = "StID=" + intent.getStringExtra("StID");
        String keywords = "keywords=" + intent.getStringExtra("keywords");
        String top = "top=" + intent.getStringExtra("num");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop, StID, keywords};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, num, StID, keywords};
            url2 = getUrlByString(strs);
        }

        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", null);
            return;
        }

        String jsonstr = "";
        try {

            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("newses");
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "暂无多数据", null);
                return;
            }

            ArrayList<NewsListModel> lists = new ArrayList<NewsListModel>();

            JosnOper json = new JosnOper();
            List<NewsListModel> listdate = json.ConvertJsonToArray(jsonstr,
                    new TypeToken<List<NewsListModel>>() {
                    }.getType());
            for (int i = 0; i < listdate.size(); i++) {
                NewsListModel listmodel = new NewsListModel();
                listmodel.setSmallPicUrl(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getSmallPicUrl()));
                listmodel.setAuditTime(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getAuditTime()));
                listmodel.setBandChID(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getBandChID()));
                listmodel.setChannelName(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getChannelName()));
                listmodel.setChID(DecodeStringUtil.DecodeBase64ToUTF8(listdate
                        .get(i).getChID()));
                listmodel.setCreateTime(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getCreateTime()));
                listmodel.setID(DecodeStringUtil.DecodeBase64ToUTF8(listdate
                        .get(i).getID()));
                listmodel.setIsComment(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getIsComment()));
                listmodel.setLatitude(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getLatitude()));
                listmodel.setLongitude(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getLongitude()));
                listmodel.setReadNo(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getReadNo()));
                listmodel.setResourceGUID(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getResourceGUID()));
                listmodel.setResourceType(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getResourceType()));
                listmodel.setResourceFlag(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getResourceFlag()));
                listmodel.setResourceUrl(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getResourceUrl()));
                listmodel.setShowOrder(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getShowOrder()));
                listmodel.setSourceForm(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getSourceForm()));
                listmodel.setSummary(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getSummary()));
                listmodel.setTitle(DecodeStringUtil.DecodeBase64ToUTF8(listdate
                        .get(i).getTitle()));
                listmodel.setUpdateTime(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getUpdateTime()));
                listmodel
                        .setUploadPicNames(DecodeStringUtil
                                .DecodeBase64ToUTF8(listdate.get(i)
                                        .getUploadPicNames()));
                listmodel.setCommentNum(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getCommentNum()));
                listmodel.setRPID(DecodeStringUtil.DecodeBase64ToUTF8(listdate
                        .get(i).getRPID()));
                try {
                    listmodel.setChannelLogo(DecodeStringUtil
                            .DecodeBase64ToUTF8(listdate.get(i)
                                    .getChannelLogo()));
                } catch (Exception e) {
                    listmodel.setChannelLogo(" ");
                }
                listmodel.setFakeReadNo(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getFakeReadNo()));
                listmodel.setGetGoodPoint(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getGetGoodPoint()));
                listmodel.setIsCommentNoName(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i)
                                .getIsCommentNoName()));
                listmodel.setResourceCSS(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getResourceCSS()));
                listmodel.setThemeID(DecodeStringUtil
                        .DecodeBase64ToUTF8(listdate.get(i).getThemeID()));
                listmodel.setIsNewTopice(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getIsNewTopice()));
                if (listdate.get(i).getRPNum() != null) {
                    listmodel.setRPNum(DecodeStringUtil
                            .DecodeBase64ToUTF8(listdate.get(i).getRPNum()));
                } else {
                    listmodel.setRPNum("0");
                }
                if (listmodel.getUploadPicNames() != null
                        && !"".equals(listmodel.getUploadPicNames())) {
                    String photo_img = listmodel.getUploadPicNames();
                    String[] photo = photo_img.split(",");
                    NewsPhotoModel[] photos = new NewsPhotoModel[photo.length];
                    for (int j = 0; j < photo.length; j++) {
                        NewsPhotoModel p = new NewsPhotoModel();
                        if (photo[j].indexOf("http") == -1) {
                            photo[j] = API.COMMON_URL + photo[j];
                        }
                        p.setID(listmodel.getID() + j);
                        p.setRID(listmodel.getRPID());
                        p.setPhotoUrl(photo[j]);
                        p.setCreateTime(listmodel.getCreateTime());
                        p.setPhotoDescription("");
                        photos[j] = p;

                    }
                    listmodel.setPhotos(photos);
                }
                lists.add(listmodel);
            }
            // Gson gson = new Gson();
            // java.lang.reflect.Type type = new
            // TypeToken<ArrayList<NewsListModel>>() {
            // }.getType();
            // lists = gson.fromJson(jsonstr, type);
            sendMsgToAct(intent, 100, "", lists);
        } catch (Exception e) {
            sendMsgToAct(intent, 10, "暂无更多数据", null);
        }
    }

    public void get_search_activelist(Intent intent) {


        String url = intent.getStringExtra("url");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String type = "type=" + intent.getStringExtra("type");
        String StID = "StID=" + intent.getStringExtra("StID");
        String keyword = "keyword=" + intent.getStringExtra("keyword");
        String top = "top=" + intent.getStringExtra("top");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop, StID, keyword, type};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, top, "dtop=0", StID, keyword, type};
            url2 = getUrlByString(strs);
        }

        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 222, "请检查网络连接！", null);
            return;
        }
        String jsonstr;

        try {

            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("SearchData");

            if (HttpUtils.isJson(jsonstr)) {
                if (jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<ActiveModel> listdata = json.ConvertJsonToArray(
                            jsonstr, new TypeToken<List<ActiveModel>>() {
                            }.getType());
                    List<ActiveModel> acList = new ArrayList<ActiveModel>();
                    for (int i = 0; i < listdata.size(); i++) {
                        ActiveModel listmodel = new ActiveModel();

                        // 通用
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getID()).length() > 0) {
                                listmodel.setID(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getID()));
                            } else {
                                Log.d("获取ID：", "数据为空");
                                listmodel.setID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("ID：", "获取失败");
                            listmodel.setID("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getCreateTime()).length() > 0) {
                                listmodel.setCreateTime(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getCreateTime()));
                            } else {
                                Log.d("获取创建时间：", "数据为空");
                                listmodel.setCreateTime("-2");
                            }
                        } catch (Exception e) {
                            Log.d("创建时间：", "获取失败");
                            listmodel.setCreateTime("1990-1-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveName()).length() > 0) {
                                listmodel.setActiveName(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveName()));
                            } else {
                                Log.d("活动名称：", "数据为空");
                                listmodel.setActiveName("-2");
                            }
                        } catch (Exception e) {
                            Log.d("活动名称：", "获取失败");
                            listmodel.setActiveName("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveType()).length() > 0) {
                                listmodel.setActiveType(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveType()));
                            } else {
                                Log.d("获取活动类型：", "数据为空");
                                listmodel.setActiveType("-2");
                            }
                        } catch (Exception e) {
                            Log.d("获取活动类型：", "获取失败");
                            listmodel.setActiveType("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveUrl()).length() > 0) {
                                listmodel.setActiveUrl(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveUrl()));
                            } else {
                                Log.d("获取链接路径：", "数据为空");
                                listmodel.setActiveUrl("-2");
                            }
                        } catch (Exception e) {
                            Log.d("链接路径：", "获取失败");
                            listmodel.setActiveUrl("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveContent())
                                    .length() > 0) {
                                listmodel.setActiveContent(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveContent()));
                            } else {
                                Log.d("获取活动内容：", "数据为空");
                                listmodel.setActiveContent("-2");
                            }
                        } catch (Exception e) {
                            Log.d("站点活动内容:", "获取失败");
                            listmodel.setActiveContent("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveLogo()).length() > 0) {
                                listmodel.setActiveLogo(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveLogo()));
                            } else {
                                Log.d("活动图标：", "数据为空");
                                listmodel.setActiveLogo("-2");
                            }
                        } catch (Exception e) {
                            Log.d("活动图标：", "获取失败");
                            listmodel.setActiveLogo("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getShowOrder()).length() > 0) {
                                listmodel.setShowOrder(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getShowOrder()));
                            } else {
                                Log.d("获取排序：", "数据为空");
                                listmodel.setShowOrder("-2");
                            }
                        } catch (Exception e) {
                            Log.d("排序：", "获取失败");
                            listmodel.setShowOrder("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveMasterPageID())
                                    .length() > 0) {
                                listmodel
                                        .setActiveMasterPageID(DecodeStringUtil
                                                .DecodeBase64ToUTF8(listdata
                                                        .get(i)
                                                        .getActiveMasterPageID()));
                            } else {
                                Log.d("获取模版ID：", "数据为空");
                                listmodel.setActiveMasterPageID("-1");
                            }
                        } catch (Exception e) {
                            Log.d("模版ID：", "获取失败");
                            listmodel.setActiveMasterPageID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getBeginDate()).length() > 0) {
                                listmodel.setBeginDate(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getBeginDate()));
                            } else {
                                Log.d("获取 开始时间：", "数据为空");
                                listmodel.setBeginDate("-1");
                            }
                        } catch (Exception e) {
                            Log.d("开始时间：", "获取失败");
                            listmodel.setBeginDate("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getEndDate()).length() > 0) {
                                listmodel.setEndDate(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getEndDate()));
                            } else {
                                Log.d("获取 结束时间：", "数据为空");
                                listmodel.setEndDate("-1");
                            }
                        } catch (Exception e) {
                            Log.d("结束时间：", "获取失败");
                            listmodel.setEndDate("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getAuditTime()).length() > 0) {
                                listmodel.setAuditTime(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getAuditTime()));
                            } else {
                                Log.d("获取审核时间：", "数据为空");
                                listmodel.setAuditTime("-2");
                            }
                        } catch (Exception e) {
                            Log.d("审核时间：", "获取失败");
                            listmodel.setAuditTime("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getAuditStatus()).length() > 0) {
                                listmodel.setAuditStatus(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getAuditStatus()));
                            } else {
                                Log.d("获取审核状态：", "数据为空");
                                listmodel.setAuditStatus("-2");
                            }
                        } catch (Exception e) {
                            Log.d("审核状态：", "获取失败");
                            listmodel.setAuditStatus("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getAuditAdminID()).length() > 0) {
                                listmodel.setAuditAdminID(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getAuditAdminID()));
                            } else {
                                Log.d("获取审核人：", "数据为空");
                                listmodel.setAuditAdminID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("审核人：", "获取失败");
                            listmodel.setAuditAdminID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getStID()).length() > 0) {
                                listmodel.setStID(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getStID()));
                            } else {
                                Log.d("获取站点ID：", "数据为空");
                                listmodel.setStID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("站点ID：", "获取失败");
                            listmodel.setStID("-1");
                        }
                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveStatus()).length() > 0) {
                                listmodel.setActiveStatus(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveStatus()));
                            } else {
                                Log.d("获取活动状态：", "数据为空");
                                listmodel.setActiveStatus("-2");
                            }
                        } catch (Exception e) {
                            Log.d("活动状态：", "获取失败");
                            listmodel.setActiveStatus("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getStatus()).length() > 0) {
                                listmodel.setStatus(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getStatus()));
                            } else {
                                Log.d("获取状态：", "数据为空");
                                listmodel.setStatus("-2");
                            }
                        } catch (Exception e) {
                            Log.d("状态：", "获取失败");
                            listmodel.setStatus("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getReMark()).length() > 0) {
                                listmodel.setReMark(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getReMark()));
                            } else {
                                Log.d("获取 备注：", "数据为空");
                                listmodel.setReMark("-2");
                            }
                        } catch (Exception e) {
                            Log.d("备注：", "获取失败");
                            listmodel.setReMark("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getIsLive()).length() > 0) {
                                listmodel.setIsLive(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getIsLive()));
                            } else {
                                Log.d("获取 地址：", "数据为空");
                                listmodel.setIsLive("-2");
                            }
                        } catch (Exception e) {
                            Log.d("地址：", "获取失败");
                            listmodel.setIsLive("-1");
                        }


                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getLiveID()).length() > 0) {
                                listmodel.setLiveID(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getLiveID()));
                            } else {
                                Log.d("获取 地址：", "数据为空");
                                listmodel.setLiveID("-2");
                            }
                        } catch (Exception e) {
                            Log.d("地址：", "获取失败");
                            listmodel.setLiveID("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getLiveUrl()).length() > 0) {
                                listmodel.setLiveUrl(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getLiveUrl()));
                            } else {
                                Log.d("获取 地址：", "数据为空");
                                listmodel.setLiveUrl("-2");
                            }
                        } catch (Exception e) {
                            Log.d("地址：", "获取失败");
                            listmodel.setLiveUrl("-1");
                        }


                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getActiveAdress()).length() > 0) {
                                listmodel.setActiveAdress(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getActiveAdress()));
                            } else {
                                Log.d("获取 地址：", "数据为空");
                                listmodel.setReMark("-2");
                            }
                        } catch (Exception e) {
                            Log.d("地址：", "获取失败");
                            listmodel.setReMark("-1");
                        }


                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getLiveBeginType()).length() > 0) {
                                listmodel.setLiveBeginType(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getLiveBeginType()));
                            } else {
                                Log.d("获取直播类型：", "数据为空");
                                listmodel.setLiveBeginType("-2");
                            }
                        } catch (Exception e) {
                            Log.d("直播类型：", "获取失败");
                            listmodel.setLiveBeginType("-1");
                        }

                        try {
                            if (DecodeStringUtil.DecodeBase64ToUTF8(
                                    listdata.get(i).getLiveBeginMedia()).length() > 0) {
                                listmodel.setLiveBeginMedia(DecodeStringUtil
                                        .DecodeBase64ToUTF8(listdata.get(i)
                                                .getLiveBeginMedia()));
                            } else {
                                Log.d("获取直播类型：", "数据为空");
                                listmodel.setLiveBeginMedia("-2");
                            }
                        } catch (Exception e) {
                            Log.d("直播类型：", "获取失败");
                            listmodel.setLiveBeginMedia("-1");
                        }


                        acList.add(listmodel);

                    }


                    sendMsgToAct(intent, 400, "",
                            acList);


                    return;
                    // TODO: 活动列表结束
                } else {
                    sendMsgToAct(intent, 10,
                            "暂无更多数据", null);
                    return;
                }
            } else {
                sendMsgToAct(intent, 10, "暂无更多数据",
                        null);
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();

            sendMsgToAct(intent, 555, "获取数据异常", null);
            return;
        }


    }


    public void get_search_dianbolist(Intent intent) {


        String url = intent.getStringExtra("url");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String type = "type=" + intent.getStringExtra("type");
        String StID = "StID=" + intent.getStringExtra("StID");
        String keyword = "keyword=" + intent.getStringExtra("keyword");
        String top = "top=" + intent.getStringExtra("top");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop, StID, keyword, type};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, top, "dtop=0", StID, keyword, type};
            url2 = getUrlByString(strs);
        }

        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 222, "请检查网络连接！", null);
            return;
        }
        String jsonstr;

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            if (object.has("SearchData")) {
                jsonstr = object.getString("SearchData");
            }
            if ("[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "", null);
                return;
            }

            if (HttpUtils.isJson(jsonstr)) {
                JosnOper json = new JosnOper();
                List<TuiJianProgram> listdata = json.ConvertJsonToArray(jsonstr, new TypeToken<List<TuiJianProgram>>() {
                }.getType());
                List<TuiJianProgram> programs = new ArrayList<TuiJianProgram>();


                for (int i = 0; i < listdata.size(); i++) {
                    TuiJianProgram program = listdata.get(i);
                    program.setID(DecodeStringUtil.DecodeBase64ToUTF8(program.getID()));
                    program.setCreateTime((DecodeStringUtil.DecodeBase64ToUTF8(program.getCreateTime())));
                    program.setIsRec(DecodeStringUtil.DecodeBase64ToUTF8(program.getIsRec()));
                    program.setProgramLogo(DecodeStringUtil.DecodeBase64ToUTF8(program.getProgramLogo()));
                    program.setProgramName(DecodeStringUtil.DecodeBase64ToUTF8(program.getProgramName()));
                    program.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(program.getReMark()));
                    program.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(program.getStatus()));
                    program.setVODChID(DecodeStringUtil.DecodeBase64ToUTF8(program.getVODChID()));
                    program.setVODType(DecodeStringUtil.DecodeBase64ToUTF8(program.getVODType()));
                    program.setNewDateTime((DecodeStringUtil.DecodeBase64ToUTF8(program.getNewDateTime())));
                    program.setProgramHornLogo((DecodeStringUtil.DecodeBase64ToUTF8(program.getProgramHornLogo())));
                    programs.add(program);

                }
                sendMsgToAct(intent, 300, "", programs);
            }
        } catch (Exception e) {
            e.printStackTrace();

            sendMsgToAct(intent, 555, "获取数据异常", null);
            return;
        }


    }


    public void get_search_livelist(Intent intent) {


        String url = intent.getStringExtra("url");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String type = "type=" + intent.getStringExtra("type");
        String StID = "StID=" + intent.getStringExtra("StID");
        String keyword = "keyword=" + intent.getStringExtra("keyword");
        String top = "top=" + intent.getStringExtra("top");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop, StID, keyword, type};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, top, "dtop=0", StID, keyword, type};
            url2 = getUrlByString(strs);
        }

        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 222, "请检查网络连接！", null);
            return;
        }
        String jsonstr;
        List<LiveChannelModel> list = new ArrayList<LiveChannelModel>();
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "暂无多数据", null);
                return;
            }
            JSONObject job = new JSONObject(jsonstr);
            jsonstr = job.getString("SearchData");
            JosnOper json = new JosnOper();
            List<LiveChannelModel> listdate = json.ConvertJsonToArray(jsonstr,
                    new TypeToken<List<LiveChannelModel>>() {
                    }.getType());

            // 清除缓存
            for (LiveChannelModel j : listdate) {
                LiveChannelModel l = new LiveChannelModel();
                if ("-1".equals(j.getID())) {
                    sendMsgToAct(intent, 555, "获取数据异常", null);
                    return;
                }

                l.setID(DecodeStringUtil.DecodeBase64ToUTF8(j.getID()));
                l.setCreateTime(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getCreateTime()));
                l.setIsAD(DecodeStringUtil.DecodeBase64ToUTF8(j.getIsAD()));
                l.setIsDel(DecodeStringUtil.DecodeBase64ToUTF8(j.getIsDel()));
                l.setIsHide(DecodeStringUtil.DecodeBase64ToUTF8(j.getIsHide()));
                l.setIsShowHome(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getIsShowHome()));
                l.setLiveChannelName(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveChannelName()));
                l.setLiveImageUrl(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveImageUrl()));
                l.setLiveProgramDate(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveProgramDate()));
                l.setLiveProgramName(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveProgramName()));
                l.setLiveNativeRandomNum(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveNativeRandomNum()));
                l.setLiveRandomNum(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveRandomNum()));
                l.setLiveRTMPUrl(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveRTMPUrl()));
                l.setParentID(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getParentID()));
                l.setPicPath(DecodeStringUtil.DecodeBase64ToUTF8(j.getPicPath()));
                l.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(j.getReMark()));
                l.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getShowOrder()));
                l.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(j.getStatus()));
                l.setVideoUrl(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getVideoUrl()));
                l.setCommentsNum(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getCommentsNum()));
                l.setLiveType(DecodeStringUtil.DecodeBase64ToUTF8(j
                        .getLiveType()));
                l.setWeek(DecodeStringUtil.DecodeBase64ToUTF8(j.getWeek()));
                l.setLiveBeginDate(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveBeginDate()));
                l.setLiveEndDate(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveEndDate()));
                l.setLiveBeginMedia(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveBeginMedia()));
                l.setLiveBeginType(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveBeginType()));
                l.setLiveBeginLogo(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveBeginLogo()));
                //l.setLiveChannleLogo(DecodeStringUtil.DecodeBase64ToUTF8(j.getLiveChannleLogo()));
                // 这个是检查数据库是否存在这个对象的

                list.add(l);

            }
            sendMsgToAct(intent, 200, "获取数据成功", list);
            return;
        } catch (Exception e) {
            e.printStackTrace();

            sendMsgToAct(intent, 555, "获取数据异常", null);
            return;
        }


    }

    public void get_zhibodetail_list(Intent intent) {
        String url = intent.getStringExtra("url");
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String num = "num=" + intent.getStringExtra("num");
        String eid = "eid=" + intent.getStringExtra("eid");
        String top = "top=" + intent.getStringExtra("num");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop, eid, UserGUID};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, num, eid, UserGUID};
            url2 = getUrlByString(strs);
        }
        Log.i("tag", url2);
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", null);
            return;
        }

        String jsonstr = "";
        try {

            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("LiveContent");
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "暂无多数据", null);
                return;
            }

            // RuntimeExceptionDao<ModelZhiboDetail, String> zhibo_detail_list =
            // getHelper().get_zhibo_detail_list();
            // //刷新时清除缓存
            // if ("dtop=".equals(dtop)&&zhibo_detail_list.isTableExists()) {
            // try {
            // zhibo_detail_list.delete(zhibo_detail_list.queryForEq("NewsLiveEventID",
            // intent.getStringExtra("eid")));
            // } catch (Exception e) {
            // }
            // }
            ArrayList<ModelZhiboDetail> lists = new ArrayList<ModelZhiboDetail>();
            if (HttpUtils.isJson(jsonstr)) {
                if (jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<ModelZhiboDetail> listdate = json.ConvertJsonToArray(
                            jsonstr, new TypeToken<List<ModelZhiboDetail>>() {
                            }.getType());
                    for (ModelZhiboDetail n : listdate) {
                        ModelZhiboDetail news = new ModelZhiboDetail();
                        news.setEventName(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventName()));
                        news.setEventLogo(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventLogo()));
                        news.setContentLogo(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getContentLogo()));
                        news.setID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getID()));
                        news.setCreateTime(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getCreateTime()));
                        news.setNewsLiveTitle(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getNewsLiveTitle()));
                        news.setNewsLiveContent(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getNewsLiveContent()));
                        news.setPicUrl(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getPicUrl()));
                        news.setMediaID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getMediaID()));
                        news.setAudioID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getAudioID()));
                        news.setLinkUrl(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getLinkUrl()));
                        news.setSendStatus(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getSendStatus()));
                        news.setAdminID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getAdminID()));
                        news.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getStatus()));
                        news.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getReMark()));
                        news.setNewsLiveEventID(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getNewsLiveEventID()));
                        news.setMediaPic(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getMediaPic()));
                        news.setMediaUrl(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getMediaUrl()));
                        news.setAudioUrl(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getAudioUrl()));
                        news.setNewsLiveType(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getNewsLiveType()));
                        news.setGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(n.getGoodPoint()));
                        news.setIsExsitPoint(DecodeStringUtil.DecodeBase64ToUTF8(n.getIsExsitPoint()));
                        news.setUserLogo(DecodeStringUtil.DecodeBase64ToUTF8(n.getUserLogo()));
                        news.setUserName(DecodeStringUtil.DecodeBase64ToUTF8(n.getUserName()));
                        // if (zhibo_detail_list.isTableExists()) {//
                        // 这个是检查数据库是否存在这个对象的
                        // zhibo_detail_list.create(news);
                        // }
                        lists.add(news);
                    }

                    if (lists.size() > 0) {
                        sendMsgToAct(intent, 100, "", lists);
                    }

                }
            }

        } catch (Exception e) {
            sendMsgToAct(intent, 10, "暂无多数据", null);
        }
    }

    public void get_zhibo_list(Intent intent) {
        String dtop = "dtop=" + intent.getStringExtra("dtop");
        String num = "num=" + intent.getStringExtra("num");
        String url = intent.getStringExtra("url");
        String top = "top=" + intent.getStringExtra("num");
        String url2;
        if (!"dtop=".equals(dtop)) {
            String[] strs = new String[]{url, top, dtop};
            url2 = getUrlByString(strs);
        } else {
            String[] strs = new String[]{url, num};
            url2 = getUrlByString(strs);
        }

        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", null);
            return;
        }

        String jsonstr = "";
        String error_str = "";
        try {

            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("LiveEvents");
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "暂无多数据", null);
                return;
            }
            ArrayList<ModelZhiboList> lists = new ArrayList<ModelZhiboList>();
            if (HttpUtils.isJson(jsonstr)) {
                if (jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<ModelZhiboList> listdate = json.ConvertJsonToArray(
                            jsonstr, new TypeToken<List<ModelZhiboList>>() {
                            }.getType());
                    // RuntimeExceptionDao<ModelZhiboList, String> zhibo_list =
                    // getHelper().get_zhibo_list();
                    // //刷新时清除缓存
                    // if ("dtop=".equals(dtop)&&zhibo_list.isTableExists()) {
                    // try {
                    // zhibo_list.delete(zhibo_list.queryForAll());
                    // } catch (Exception e) {
                    // }
                    //
                    // }
                    for (ModelZhiboList n : listdate) {
                        ModelZhiboList news = new ModelZhiboList();
                        news.setID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getID()));
                        news.setCreateTime(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getCreateTime()));
                        news.setEventName(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventName()));
                        news.setEventLogo(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventLogo()));
                        news.setContentLogo(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getContentLogo()));
                        news.setEventCSS(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventCSS()));
                        news.setIsPublic(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsPublic()));
                        news.setStID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getStID()));
                        news.setIsTop(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsTop()));
                        news.setIsRec(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsRec()));
                        news.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getShowOrder()));
                        news.setEventStatus(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getEventStatus()));
                        news.setJoinNum(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getJoinNum()));
                        news.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getStatus()));
                        news.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getReMark()));
                        news.setEventSurmmy(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getEventSurmmy()));
                        // if (zhibo_list.isTableExists()) {// 这个是检查数据库是否存在这个对象的
                        // zhibo_list.create(news);
                        // }
                        lists.add(news);
                    }

                    if (lists.size() > 0) {
                        sendMsgToAct(intent, 100, "", lists);
                    }

                }
            }

        } catch (Exception e) {
            sendMsgToAct(intent, 10, "暂无多数据", null);
        }

    }
    //直播间点赞

    private void del_zan_liveroom(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("LiveContent");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS_2, "取消点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "取消点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "点赞异常！", null);
                e.printStackTrace();
            }
        }


    }


    //直播间点赞

    private void zan_liveroom(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("LiveContent");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS_2, "点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "点赞异常！", null);
                e.printStackTrace();
            }
        }


    }


    /**
     * 得到视频列表
     *
     * @param intent
     */
    public void get_video_list(Intent intent) {


        RuntimeExceptionDao<MediaList, String> video_list;// 视频列表
        video_list = getHelper().getMode(MediaList.class);
        String type = intent.getStringExtra("type");
        String url = intent.getStringExtra("url");
        String StID = "StID=" + intent.getStringExtra("StID");
        String top = "top=" + intent.getStringExtra("top");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String dtop = "";
        if ("up".equals(type)) {
            dtop = "dtop=" + intent.getStringExtra("dtop");
        }
        String ChannelID = "ChannelID=" + intent.getStringExtra("ChannelID");
        String[] strs = null;
        if ("up".equals(type)) {
            strs = new String[]{url, StID, top, dtop, UserGUID, ChannelID};
        } else {
            strs = new String[]{url, StID, top, UserGUID, ChannelID};
        }
        String url2 = getUrlByString(strs);
        String jsonstr;
        ArrayList<MediaList> arrs = new ArrayList<MediaList>();
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
            JSONObject object = new JSONObject(jsonstr);
            if (object.has("MediaList")) {
                jsonstr = object.getString("MediaList");
            }
            if ("[]".equals(jsonstr)) {
                sendMsgToAct(intent, 333, "", null);
                return;
            }
            if (HttpUtils.isJson(jsonstr)) {
                if (jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<MediaList> listdate = json.ConvertJsonToArray(jsonstr, new TypeToken<List<MediaList>>() {
                    }.getType());

                    if (dtop.equals("0")) {
                        video_list.delete(video_list.queryForAll());
                    }

                    for (int i = 0; i < listdate.size(); i++) {
                        MediaList m = new MediaList();
                        m.setChannelID(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getChannelID()));
                        m.setDetail(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getDetail()));
                        m.setDigCount(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getDigCount()));
                        m.setFileSize(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getFileSize()));
                        m.setID(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getID()));
                        m.setID2(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getID2()));
                        m.setImageUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getImageUrl()));
                        m.setIsBest(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getIsBest()));
                        m.setMediaUrl(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getMediaUrl()));
                        m.setName(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getName()));
                        m.setPaiCount(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getPaiCount()));
                        m.setParentID(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getParentID()));
                        m.setTag(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getTag()));
                        m.setTotalView(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getTotalView()));
                        m.setUploadDate(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUploadDate()));
                        m.setUserName(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUserName()));
                        m.setUserTop(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getUserTop()));
                        m.setCommentNum(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getCommentNum()));
                        m.setClickNum(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getClickNum()));
                        m.setGoodPoint(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getGoodPoint()));
                        m.setIsExsitPoint(DecodeStringUtil.DecodeBase64ToUTF8(listdate.get(i).getIsExsitPoint()));
                        arrs.add(m);
                        if (video_list.isTableExists() && !video_list.idExists(m.getID())) {
                            video_list.create(m);
                        }
                    }

                    // if(video_list.isTableExists()){
                    // video_list.delete(video_list.queryForAll());
                    // for(MediaList m:arrs){
                    // if (video_list.isTableExists() &&
                    // !video_list.idExists(m.getID())) {
                    // video_list.create(m);
                    // }
                    // }
                    //
                    // }

                    sendMsgToAct(intent, API.HANDLER_GETDATA_SUCCESS, "", arrs);

                } else {
                    sendMsgToAct(intent, 222, "", null);
                }
            } else {
                sendMsgToAct(intent, 222, "", null);
            }

        } catch (HttpHostConnectException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (JSONException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (IOException e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        } catch (Exception e) {
            sendMsgToAct(intent, 1200, "", null);
            e.printStackTrace();
        }

    }

    //视频列表

    private void del_zan_videolist(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("MediaList");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS_2, "取消点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "取消点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "取消点赞异常！", null);
                e.printStackTrace();
            }
        }


    }


    //视频列表

    private void zan_videolist(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("MediaList");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, 112, "点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "点赞异常！", null);
                e.printStackTrace();
            }
        }


    }

    private void getMoreNews(Intent intent) {
        String url = intent.getStringExtra("url");


        String top = "top="
                + intent.getStringExtra("top");
        String dtop = "dtop="
                + intent.getStringExtra("dtop");
        String type = "type="
                + intent.getStringExtra("type");
        String StID = "StID="
                + intent.getStringExtra("StID");
        String[] arr = new String[]{url, top, dtop, type, StID};
        String url2 = getUrlByString(arr);


        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 222, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url2);

            JosnOper json = new JosnOper();
            JSONObject object = new JSONObject(jsonstr);
            if (object.has("MoreNews")) {
                jsonstr = object.getString("MoreNews");
            }
            if ("[]".equals(jsonstr)) {
                sendMsgToAct(intent, 555, "暂无数据!", null);
                return;
            }
            List<HHIndexNewsListModel> listdata = json.ConvertJsonToArray(jsonstr,
                    new TypeToken<List<HHIndexNewsListModel>>() {
                    }.getType());

            DecodeUtils.decode(listdata);
            if (listdata.size() > 0) {
                sendMsgToAct(intent, 100, "", listdata);
            } else {
                sendMsgToAct(intent, 555, "暂无数据!", null);
            }

        } catch (HttpHostConnectException e) {
            sendMsgToAct(intent, 555, "网络异常", null);
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            sendMsgToAct(intent, 555, "网络连接超时！", null);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            sendMsgToAct(intent, 555, "网络连接超时！", null);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            sendMsgToAct(intent, 555, "数据异常", null);
            e.printStackTrace();
        } catch (JSONException e) {
            sendMsgToAct(intent, 555, "数据异常", null);
            e.printStackTrace();
        } catch (IOException e) {
            sendMsgToAct(intent, 555, "数据异常", null);
            e.printStackTrace();
        } catch (Exception e) {
            sendMsgToAct(intent, 555, "数据异常", null);
            e.printStackTrace();
        }
    }

    //爆料点赞

    private void addGoodPoint(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("RevelationGood");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS_2, "点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "点赞异常！", null);
                e.printStackTrace();
            }
        }


    }
//爆料点赞

    private void removeGoodPoint(Intent intent) {
        // TODO Auto-generated method stub

        String url = intent.getStringExtra("url");
        String ID = "ID=" + intent.getStringExtra("ID");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");

        String[] strs = new String[]{url, ID, UserGUID};
        String url2 = getUrlByString(strs);

        String jsonstr = "";
        jsonstr = HttpUtils.GetJsonStrByURL(url2);
        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";

                object = new JSONObject(jsonstr);
                jsonstr = object.getString("RevelationGood");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");

                }

                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS_2, "取消点赞成功", null);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS_1, "取消点赞失败！", null);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS_1, "取消点赞异常！", null);
                e.printStackTrace();
            }
        }


    }

    public void get_liveroom(Intent intent) {

        String url = intent.getStringExtra("url");


        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 0, "请检查网络连接！", null);
            return;
        }

        String jsonstr = "";

        try {

            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("LiveEvents");
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 10, "暂无多数据", null);
                return;
            }
            ArrayList<ModelZhiboList> lists = new ArrayList<ModelZhiboList>();
            if (HttpUtils.isJson(jsonstr)) {
                if (jsonstr.indexOf("-1") <= -1) {
                    JosnOper json = new JosnOper();
                    List<ModelZhiboList> listdate = json.ConvertJsonToArray(
                            jsonstr, new TypeToken<List<ModelZhiboList>>() {
                            }.getType());

                    for (ModelZhiboList n : listdate) {
                        ModelZhiboList news = new ModelZhiboList();
                        news.setID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getID()));
                        news.setCreateTime(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getCreateTime()));
                        news.setEventName(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventName()));
                        news.setEventLogo(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventLogo()));
                        news.setContentLogo(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getContentLogo()));
                        news.setEventCSS(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getEventCSS()));
                        news.setIsPublic(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsPublic()));
                        news.setStID(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getStID()));
                        news.setIsTop(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsTop()));
                        news.setIsRec(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getIsRec()));
                        news.setShowOrder(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getShowOrder()));
                        news.setEventStatus(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getEventStatus()));
                        news.setJoinNum(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getJoinNum()));
                        news.setStatus(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getStatus()));
                        news.setReMark(DecodeStringUtil.DecodeBase64ToUTF8(n
                                .getReMark()));
                        news.setEventSurmmy(DecodeStringUtil
                                .DecodeBase64ToUTF8(n.getEventSurmmy()));

                        lists.add(news);
                    }

                    if (lists.size() > 0) {
                        sendMsgToAct(intent, 100, "", lists);
                    }

                }
            }

        } catch (Exception e) {
            sendMsgToAct(intent, 101, "暂无多数据", null);
        }

    }


    // 插入直播间信息
    public void insert_liveroom(Intent intent) {
        String url = intent.getStringExtra("url");

        String Title = "Title=" + intent.getStringExtra("Title");
        String Content = "Content=" + intent.getStringExtra("Content");

        String LiveType = "LiveType=" + intent.getStringExtra("LiveType");
        String PicUrl = "PicUrl=" + intent.getStringExtra("PicUrl");
        String EventID = "EventID=" + intent.getStringExtra("EventID");
        String VideoUrl = "VideoUrl=" + intent.getStringExtra("VideoUrl");

        String UploadType = "UploadType=" + intent.getStringExtra("UploadType");

        String FileDate = "FileDate=" + intent.getStringExtra("FileDate");
        String UserGUID = "UserGUID=" + intent.getStringExtra("UserGUID");
        String Sign = "Sign=" + intent.getStringExtra("Sign");

        String[] strs = new String[]{url, Title, Content, LiveType, PicUrl, VideoUrl, EventID, UploadType, Sign, FileDate, UserGUID};
        String url2 = getUrlByString(strs);

        Log.i("tag", url2);

        String jsonstr = "";
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("Title", intent.getStringExtra("Title"));
//		map.put("Content", intent.getStringExtra("Content"));
//		map.put("LiveType", intent.getStringExtra("LiveType"));
//		map.put("PicUrl", intent.getStringExtra("PicUrl"));
//		map.put("EventID", intent.getStringExtra("EventID"));
//		map.put("VideoUrl", intent.getStringExtra("VideoUrl"));
//		map.put("UploadType", intent.getStringExtra("UploadType"));
//		map.put("UserGUID", intent.getStringExtra("Longitude"));
//		map.put("StID", intent.getStringExtra("StID"));
//		map.put("FileDate", intent.getStringExtra("FileDate")+"/");
//
//		try {
//			jsonstr = HttpUtils.sendPOSTRequest(url, map);
//		} catch (Exception e1) {
//			// TODO 自动生成的 catch 块
//			e1.printStackTrace();
//		}

        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url2);
        } catch (HttpHostConnectException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ConnectTimeoutException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SocketTimeoutException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (jsonstr != null) {
            JSONObject object;
            try {
                String result = "";


                String id = "";
                object = new JSONObject(jsonstr);
                jsonstr = object.getString("NewsContent");
                JSONArray ja = new JSONArray(jsonstr);
                if (ja.length() > 0) {
                    JSONObject js = (JSONObject) ja.get(0);
                    result = (String) js.get("Result");
                    id = DecodeStringUtil.DecodeBase64ToUTF8(js.getString("ID"));
                }
                List<String> data = new ArrayList<String>();
                data.add(id);
                if (result.equals("Success")) {
                    sendMsgToAct(intent, API.SUCCESS, "成功", data);
                } else {
                    sendMsgToAct(intent, API.UNSUCCESS, "失败！", data);
                }
            } catch (JSONException e) {
                sendMsgToAct(intent, API.UNSUCCESS, "失败！", null);
                e.printStackTrace();
            }
        }

    }

    private void get_index_model1(Intent intent) {
        String url = intent.getStringExtra("url");
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        String jsonstr = "";
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            JSONObject object = new JSONObject(jsonstr);
            jsonstr = object.getString("IndexModule");
            if (jsonstr != null && "[]".equals(jsonstr)) {
                sendMsgToAct(intent, 404, "暂无多数据", null);
                return;
            }
            JosnOper json = new JosnOper();
            List<IndexModel> listdate = json.ConvertJsonToArray(jsonstr,
                    new TypeToken<List<IndexModel>>() {
                    }.getType());
            DecodeUtils.decode(listdate);
            if (listdate.size() > 0) {
                sendMsgToAct(intent, 1990, "", listdate);
            } else {
                sendMsgToAct(intent, 404, "暂无多数据", null);
                return;
            }
        } catch (HttpHostConnectException e) {
            sendMsgToAct(intent, 504, "", null);
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            sendMsgToAct(intent, 404, "网络连接超时！", null);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            sendMsgToAct(intent, 404, "网络连接超时！", null);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            sendMsgToAct(intent, 504, "", null);
            e.printStackTrace();
        } catch (JSONException e) {
            sendMsgToAct(intent, 504, "", null);
            e.printStackTrace();
        } catch (IOException e) {
            sendMsgToAct(intent, 504, "", null);
            e.printStackTrace();
        } catch (Exception e) {
            sendMsgToAct(intent, 504, "", null);
            e.printStackTrace();
        }
    }

    private void get_index_news(Intent intent) {
//        boolean isConnect = Assistant
//                .IsContectInterNet2(getApplicationContext());
//        if (!isConnect) {
//            sendMsgToAct(intent, 404, "请检查网络连接！", null);
//            return;
//        }
//        String url = intent.getStringExtra("url");
//        Log.e("tag", url);
//        String jsonstr = "";
//        RuntimeExceptionDao<CJIndexNewsListModel, String> new_index_news_list = getHelper()
//                .getMode(CJIndexNewsListModel.class);
//        RuntimeExceptionDao<NewsADs, String> adDao = getHelper()
//                .getMode(NewsADs.class);
//        try {
//            jsonstr = HttpUtils.GetJsonStrByURL2(url);
//            Gson gson = new Gson();
//            HomeListModel homeListModel = gson.fromJson(jsonstr,
//                    HomeListModel.class);
//
//            DecodeUtils.decode(homeListModel.getResList());
//            DecodeUtils.decode(homeListModel.getHotNewsList());
//            DecodeUtils.decode(homeListModel.getResHotChannList());
//            DecodeUtils.decode(homeListModel.getPoliticsList());
//            DecodeUtils.decode(homeListModel.getLifeList());
//            DecodeUtils.decode(homeListModel.getLikeList());
//            DecodeUtils.decode(homeListModel.getResHotList());
//            DecodeUtils.decode(homeListModel.getResImagesList());
//            DecodeUtils.decode(homeListModel.getActiveList());
//            DecodeUtils.decode(homeListModel.getLive());
//            DecodeUtils.decode(homeListModel.getRevelation());
//            DecodeUtils.decode(homeListModel.getServer());
//            DecodeUtils.decode(homeListModel.getContentList());
//            DecodeUtils.decode(homeListModel.getResLive());
//            DecodeUtils.decode(homeListModel.getEjectAds());
//            DecodeUtils.decode(homeListModel.getBtnTuRes());
//            DecodeUtils.decode(homeListModel.getVodList());
//            DecodeUtils.decode(homeListModel.getLikeMediaList());
//            DecodeUtils.decode(homeListModel.getHotCircle());
//
//            // 活动缓存
//            if (new_index_news_list.isTableExists()
//                    && homeListModel.getActiveList().size() > 0) {
//                new_index_news_list.delete(new_index_news_list.queryForEq(
//                        "newsType", "4"));
//                for (CJIndexNewsListModel activity : homeListModel
//                        .getActiveList()) {
//                    activity.setNewsType("4");
//                    new_index_news_list.create(activity);
//                }
//            }
//            List<HomeListModel> homeListModels = new ArrayList<HomeListModel>();
//            homeListModels.add(homeListModel);
//            sendMsgToAct(intent, 100, "", homeListModels);
//            // 栏目广告缓存
//            if (homeListModel.getEjectAds().size() > 0
//                    && adDao.isTableExists()) {
//                adDao.delete(adDao.queryForAll());
//
//                for (NewsADs ad : homeListModel.getEjectAds()) {
//                    adDao.create(ad);
//                }
//            }
//
//            // 新闻资讯缓存
//            if (new_index_news_list.isTableExists()
//                    && homeListModel.getResList().size() > 0) {
//                new_index_news_list.delete(new_index_news_list.queryForEq(
//                        "newsType", "1"));
//                for (CJIndexNewsListModel newsModel : homeListModel
//                        .getResList()) {
//                    newsModel.setNewsType("1");
//                    new_index_news_list.create(newsModel);
//                }
//            }
//            // 热点新闻缓存
//            if (new_index_news_list.isTableExists()
//                    && homeListModel.getResHotList().size() > 0) {
//                new_index_news_list.delete(new_index_news_list.queryForEq(
//                        "newsType", "2"));
//                for (CJIndexNewsListModel hotListModel : homeListModel
//                        .getResHotList()) {
//                    hotListModel.setNewsType("2");
//                    new_index_news_list.create(hotListModel);
//                }
//            }
//            // 图集缓存
//            if (new_index_news_list.isTableExists()
//                    && homeListModel.getResImagesList().size() > 0) {
//                new_index_news_list.delete(new_index_news_list.queryForEq(
//                        "newsType", "3"));
//                for (CJIndexNewsListModel images : homeListModel
//                        .getResImagesList()) {
//                    images.setNewsType("3");
//                    new_index_news_list.create(images);
//                }
//            }
//
//        } catch (HttpHostConnectException e) {
//            e.printStackTrace();
//        } catch (ConnectTimeoutException e) {
//            sendMsgToAct(intent, 404, "网络连接超时！", null);
//            e.printStackTrace();
//        } catch (SocketTimeoutException e) {
//            sendMsgToAct(intent, 404, "网络连接超时！", null);
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            sendMsgToAct(intent, 404, "数据异常！", null);
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void get_index_model(Intent intent) {
        boolean isConnect = Assistant
                .IsContectInterNet2(getApplicationContext());
        if (!isConnect) {
            sendMsgToAct(intent, 404, "请检查网络连接！", null);
            return;
        }
        RuntimeExceptionDao<IndexModel, String> dao = getHelper()
                .getMode(IndexModel.class);
        String url = intent.getStringExtra("url");
        String jsonstr;
        try {
            List<IndexModel> all = dao.queryForAll();
            jsonstr = HttpUtils.GetJsonStrByURL2(url);
            JSONObject object = new JSONObject(jsonstr);
            if (object.has("IndexModule")) {
                jsonstr = object.getString("IndexModule");
            }
            if ("[]".equals(jsonstr)) {
                sendMsgToAct(intent, 333, "", null);
                return;
            }

            if (HttpUtils.isJson(jsonstr)) {
                JosnOper json = new JosnOper();
                List<IndexModel> listdata = json.ConvertJsonToArray(jsonstr,
                        new TypeToken<List<IndexModel>>() {
                        }.getType());
                if (listdata.size() == 0) {
                    sendMsgToAct(intent, 3333, "暂无数据", null);
                }
                DecodeUtils.decode(listdata);
                List<IndexModel> fixList = dao.queryForAll();
                List<IndexModel> tempList = new ArrayList<IndexModel>();
                for (IndexModel indexModel : listdata) {
                    if ("True".equals(indexModel.getIsDel())) {
                        tempList.add(indexModel);
                    }
                    // 清除缓存
                    if (dao.isTableExists() && dao.idExists(indexModel.getID())) {
                        IndexModel queryForId = dao.queryForId(indexModel
                                .getID());
                        // if (fixList.size() >= 7) {// 判断首页模块是否增加
                        indexModel.setIsDel(queryForId.getIsDel());
                        // }
//						indexModel.setIsDelete(queryForId.getIsDel());
                        dao.updateRaw(indexModel.getID(), "IsDel");
                    } else {
                        indexModel.setIsDelete(indexModel.getIsDel());
                        dao.create(indexModel);
                    }

                }
                // 判断首页固定模块是否减少
                // if (fixList.size() > tempList.size() && tempList.size() > 0)
                // {
                // fixList.removeAll(tempList);
                // for (IndexModel indexModel : fixList) {
                // if (dao.idExists(indexModel.getID())) {
                // indexModel.setIsDel("False");
                // dao.update(indexModel);
                // }
                // }
                // }

//				/ * else if (fixList.size() < tempList.size() && tempList.size()
//				 * < 7) { tempList.removeAll(fixList); for (IndexModel
//				 * indexModel : tempList) { if
//				 * (dao.idExists(indexModel.getID())) {
//				 * indexModel.setIsDel("True"); dao.update(indexModel); } } }
//				 /
                // 判断模块总数是否减少
                if (!url.contains("IsDel=0")) {
                    List<IndexModel> queryForAll = dao.queryForAll();
                    if (queryForAll.size() > listdata.size()) {
                        queryForAll.removeAll(listdata);
                        dao.delete(queryForAll);
                    }
                }
                sendMsgToAct(intent, 101010, "true", listdata);
            }
        } catch (HttpHostConnectException e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            sendMsgToAct(intent, 555, "", null);
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        } catch (JSONException e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        } catch (IOException e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        } catch (Exception e) {
            sendMsgToAct(intent, 3333, "", null);
            e.printStackTrace();
        }
    }


    /***
     * 获取长江新首页新闻列表
     *
     * @param intent
     */
    public void get_cj_index_news_list(Intent intent) {


        // 新闻图片
        String strURL;
        String state = intent.getStringExtra("state");
        String url = intent.getStringExtra("url");

        String StID = "StID=" + intent.getStringExtra("stid");
        if ("up".equals(state)) {
            String num = "num=" + intent.getStringExtra("num");
            String dtop = "dtop=" + intent.getStringExtra("dtop");
            String[] arr = new String[]{url, num, dtop, StID};
            strURL = getUrlByString(arr);
        } else {
            String num = "num=" + intent.getStringExtra("num");
            String[] arr = new String[]{url, num, StID};
            strURL = getUrlByString(arr);
        }
        String jsonstr = "";
        String error_str = "";
        try {
            jsonstr = HttpUtils.GetJsonStrByURL2(strURL);
            JSONObject object = new JSONObject(jsonstr);
            if (HttpUtils.isJson(jsonstr)) {
                if (object.get("ResList") != null
                        && !"[]".equals(object.getString("ResList"))) {
                    RuntimeExceptionDao<NewsPhotoModel, String> new_photo = getHelper()
                            .getMode(NewsPhotoModel.class);
                    RuntimeExceptionDao<IndexAD, String> indexAdDao = getHelper()
                            .getMode(IndexAD.class);
                    Gson gson = new Gson();
                    HomeListModel homeListModel = gson.fromJson(jsonstr,
                            HomeListModel.class);

                    RuntimeExceptionDao<CJIndexNewsListModel, String> new_index_news_list = getHelper()
                            .getMode(CJIndexNewsListModel.class);
                    if (!"up".equals(state)
                            && new_index_news_list.isTableExists()) {
                        try {
                            new_index_news_list.delete(new_index_news_list
                                    .queryForAll());
                        } catch (Exception e) {
                            Log.d("xf", e.toString());
                        }
                    }
                    if (homeListModel.getResList().size() == 0) {
                        sendMsgToAct(intent, HomeAPI.HANDLER_GETDATA_ERROR,
                                "暂无更多数据", null);
                        return;
                    }
                    DecodeUtils.decode(homeListModel.getResList());
                    DecodeUtils.decode(homeListModel.getIndexAD());
                    DecodeUtils.decode(homeListModel.getIndexRecommendAD());
                    if (!"up".equals(state)
                            && new_index_news_list.isTableExists()) {
                        if (homeListModel.getResList().size() > 0) {
                            for (CJIndexNewsListModel cjModel : homeListModel
                                    .getResList()) {
                                new_index_news_list.create(cjModel);
                            }
                        }
                    }
                    if (!"up".equals(state) && indexAdDao.isTableExists()) {
                        indexAdDao.delete(indexAdDao.queryForAll());
                        // 清除添加滚动广告缓存
                        if (homeListModel.getIndexRecommendAD().size() > 0) {
                            for (IndexAD indexAD : homeListModel
                                    .getIndexRecommendAD()) {
                                indexAD.setIsScrollText("true");
                                indexAdDao.create(indexAD);
                            }
                        } else {
//							sendMsgToAct(intent, 105, "暂无更多数据", null);
                        }
                        // 清除添加广告缓存
                        if (homeListModel.getIndexAD().size() > 0) {
                            for (IndexAD indexAD : homeListModel.getIndexAD()) {
                                indexAD.setIsAD("true");
                                if (indexAdDao.idExists(indexAD.getID())) {
                                    indexAdDao.update(indexAD);
                                } else {
                                    indexAdDao.create(indexAD);
                                }
                            }
                        } else {
//							sendMsgToAct(intent, 106, "暂无更多数据", null);
                        }
                    }
                    List<CJIndexNewsListModel> newIndexList = homeListModel
                            .getResList();
                    for (CJIndexNewsListModel listmodel : newIndexList) {
                        if (listmodel.getUploadPicNames() != null
                                && !"".equals(listmodel.getUploadPicNames())) {
                            String photo_img = listmodel.getUploadPicNames();
                            String[] photo = photo_img.split(",");
                            NewsPhotoModel[] photos = new NewsPhotoModel[photo.length];
                            for (int j = 0; j < photo.length; j++) {
                                NewsPhotoModel p = new NewsPhotoModel();
                                if (photo[j].indexOf("http") == -1) {
                                    photo[j] = API.COMMON_URL + photo[j];
                                }
                                p.setID(listmodel.getID() + j);
                                p.setRID(listmodel.getRPID());
                                p.setPhotoUrl(photo[j]);
                                p.setCreateTime(listmodel.getCreateTime());
                                p.setPhotoDescription("");
                                photos[j] = p;
                                if (new_photo != null
                                        && new_photo.isTableExists()
                                        && !new_photo.idExists(p.getID())) {
                                    new_photo.create(p);
                                }
                            }
                            listmodel.setPhotos(photos);
                        }
                    }
                    List<HomeListModel> homeListModels = new ArrayList<HomeListModel>();
                    homeListModels.add(homeListModel);
                    if (homeListModels != null && homeListModels.size() > 0) {
                        sendMsgToAct(intent, 10086, "", homeListModels);
                    } else {
                        sendMsgToAct(intent, HomeAPI.HANDLER_GETDATA_ERROR,
                                "暂无更多数据", null);
                    }
                    return;
                } else {
                    sendMsgToAct(intent, HomeAPI.HANDLER_GETDATA_ERROR,
                            "暂无更多数据", null);
                }
            } else {
                sendMsgToAct(intent, HomeAPI.HANDLER_GETDATA_ERROR, "暂无更多数据",
                        null);
            }
        } catch (Exception e) {
            sendMsgToAct(intent, HomeAPI.HANDLER_GETDATA_ERROR, "暂无更多数据", null);
        }
    }

    /**
     * 获得数据库管理
     *
     * @return
     */
    private DataBaseHelper getHelper() {
        if (this.databaseHelper == null)
            this.databaseHelper = ((DataBaseHelper) OpenHelperManager.getHelper(
                    this, DataBaseHelper.class));
        return this.databaseHelper;
    }

    /**
     * 发送消息给对应的activity
     *
     * @param paramIntent * 意图
     * @param paramInt    * message的arg1参数
     * @param paramString * message的obj参数
     * @param paramList   * 需要传输的数据message的data数据
     */
    private void sendMsgToAct(Intent paramIntent, int paramInt,
                              String paramString, List paramList) {
        int i = paramIntent.getIntExtra("api", 0);
        Bundle localBundle1 = paramIntent.getExtras();
        Messenger localMessenger = null;
        if (localBundle1 != null) {
            localMessenger = null;
            switch (i) {
                case IndexAPI.INDEX_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.INDEX_LIST_MESSAGE);
                    break;
                case IndexAPI.INDEX_NEWS_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.INDEX_NEWS_LIST_MESSAGE);
                    break;
                case HomeAPI.CJ_INDEX_NEWS_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(HomeAPI.CJ_INDEX_NEWS_LIST_MESSAGE);
                    break;
                case API.GET_INDEX:
                    localMessenger = (Messenger) localBundle1
                            .get(API.GET_INDEX_MESSENGER);
                    break;
                case IndexAPI.GET_INDEX_MOKUAI:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.GET_INDEX_MOKUAI_MESSENGER);
                    break;
                case IndexAPI.GET_INDEX_NEWS:
                    localMessenger = (Messenger) localBundle1
                            .get(API.GET_INDEX_NEWS_MESSENGER);
                    break;
                case IndexAPI.LIVEROOM_INSERT_API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.LIVEROOM_INSERT_MESSAGE);
                    break;
                case IndexAPI.ZHIBO_EVENT_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.ZHIBO_EVENT_LIST_MESSAGE);
                    break;
                case IndexAPI.BAOLIAO_DEL_GOODTOP__API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.BAOLIAO_DEL_GOODTOP_MESSAGE);
                    break;
                case IndexAPI.BAOLIAO_GOODTOP__API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.BAOLIAO_GOODTOP_MESSAGE);
                    break;
                case IndexAPI.NEWS_MORE_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.GET_MORE_NEWS_MESSAGE);
                    break;
                case IndexAPI.VIDEO_LIST_ZAN_API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.VIDEO_LIST_ZAN_MESSENGER);
                    break;
                case IndexAPI.VIDEO_LIST_REMOVEZAN_API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.VIDEO_LIST_REMOVEZAN_MESSENGER);
                    break;
                case API.VIDEO_LIST_API:
                    localMessenger = (Messenger) localBundle1.get(API.VIDEO_LIST_MESSAGE);
                    break;
                case IndexAPI.LIVEROOM_ZAN_API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.LIVEROOM_ZAN_MESSAGE);
                    break;
                case IndexAPI.LIVEROOM_DEL_ZAN_API:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.LIVEROOM_DEL_ZAN_MESSAGE);
                    break;
                case NewsAPI.ZHIBO_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(NewsAPI.ZHIBO_LIST_MESSAGE);
                    break;
                case NewsAPI.ZHIBODETAIL_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(NewsAPI.ZHIBODETAIL_LIST_MESSAGE);
                    break;
                case IndexAPI.SEARCH_LIVE_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.SEARCH_LIVE_LIST_MESSAGE);
                    break;
                case IndexAPI.SEARCH_DB_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.SEARCH_DB_LIST_MESSAGE);
                    break;
                case IndexAPI.SEARCH_HD_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.SEARCH_HD_LIST_MESSAGE);
                    break;
                case NewsAPI.SEARCH_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(NewsAPI.SEARCH_LIST_MESSAGE);
                    break;
                case API.VIDEO_INSERT_COMMENTS_API:
                    localMessenger = (Messenger) localBundle1.get(API.VIDEO_INSERT_COMMENTS_MESSAGE);
                    break;
                case API.VIDEO_DETAIL_API:
                    localMessenger = (Messenger) localBundle1.get(API.VIDEO_DETAIL_MESSAGE);
                    break;
                case API.VIDEO_COMMENTS_SHANGLA_API:
                    localMessenger = (Messenger) localBundle1.get(API.VIDEO_COMMENTS_SHANGLA_MESSAGE);
                    break;
                case API.VIDEO_COMMENTS_XIALA_API:
                    localMessenger = (Messenger) localBundle1.get(API.VIDEO_COMMENTS_XIALA_MESSAGE);
                    break;
                case IndexAPI.GET_SUBSCRIBE_LIST:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.GET_SUBSCRIBE_LIST_API);
                    break;
                case IndexAPI.GET_ZWSUBSCRIBE_LIST:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.GET_ZWSUBSCRIBE_LIST_API);
                    break;
                case IndexAPI.GET_SUBSCRIBE_UPDATE:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.GET_SUBSCRIBE_UPDATE_API);
                    break;
                case IndexAPI.GET_NEW_POLITICS:
                    localMessenger = (Messenger) localBundle1.get(IndexAPI.GET_NEW_POLITICS_API);
                    break;
                case IndexAPI.GET_LIST_NEWS_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.GET_LIST_NEWS_API_MESSENGER);
                    break;
                case IndexAPI.GET_INSTITUTION_DETAIL:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.GET_INSTITUTION_DETAIL_API);
                    break;
                case IndexAPI.GET_INSTITUTION_LIST:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.GET_INSTITUTION_LIST_API);
                    break;
                case IndexAPI.NEWTOPICE_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.NEWTOPICE_MESSAGE);
                    break;
                case IndexAPI.WENZHENG_ADD_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.WENZHENG_ADD_MESSAGE);
                    break;
                case IndexAPI.SUBSCRIBE_STATE_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.SUBSCRIBE_STATE_API_MESSAGE);
                    break;
                case IndexAPI.PAPER_NET_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.PAPER_NET_API_MESSAGE);
                    break;
                case IndexAPI.START_PICS_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.START_PICS_API_MESSAGE);
                    break;
                case IndexAPI.DIANBO_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.DIANBO_LIST_API_MESSAGE);
                    break;
                case IndexAPI.DIANBO_DETIAL_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.DIANBO_DETIAL_LIST_API_MESSAGE);
                    break;
                case IndexAPI.ACTIV_UP_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.ACTIV_UP_LIST_API_MESSAGE);
                    break;
                case IndexAPI.ACTIV_DOWN_LIST_API:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.ACTIV_DOWN_LIST_API_MESSAGE);
                    break;
                case IndexAPI.DIANBO_DETIAL_LIST_API2:
                    localMessenger = (Messenger) localBundle1
                            .get(IndexAPI.DIANBO_DETIAL_LIST_API2_MESSAGE);
                    break;
                default:

                    break;
            }
        }
        Message localMessage = Message.obtain();
        localMessage.what = paramInt;
        localMessage.obj = paramString;

        if (paramList != null) {
            Bundle localBundle2 = new Bundle();
            ArrayList localArrayList = new ArrayList();
            localArrayList.add(paramList);
            localBundle2.putParcelableArrayList("list", localArrayList);
            localMessage.setData(localBundle2);
        }

        try {
            localMessenger.send(localMessage);
            return;
        } catch (Exception localException) {
            Log.w(super.getClass().getName(), "Exception Message: "
                    + localException.toString());
        }

    }

}
