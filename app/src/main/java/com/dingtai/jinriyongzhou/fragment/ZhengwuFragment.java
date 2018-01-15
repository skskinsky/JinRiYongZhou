package com.dingtai.jinriyongzhou.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.view.MyGridView;
import com.dingtai.base.view.MyListView;
import com.dingtai.dtpolitics.activity.InstitutionIndexActivity;
import com.dingtai.dtpolitics.activity.LeaderIndexActivity;
import com.dingtai.dtpolitics.model.PoliticsAreaModel;
import com.dingtai.dtpolitics.service.WenZhengAPI;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.HallGridviewAdapter;
import com.dingtai.jinriyongzhou.adapter.ModelListAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */

public class ZhengwuFragment extends BaseFragment {
    private View mMainView;
    private LinearLayout ll_layout;
    private MyGridView myGridView;

    private boolean isInite;
    private TextView tv_yongzhouhao, tv_dingyue;
    private MyListView lv_model_list;
    private ModelListAdapter adapter;
    private LinearLayout ll_showlead;
    private List<PoliticsAreaModel> politicsAreaModelList = new ArrayList<>();
    private List<SubscribeListsModel> subscribeList = new ArrayList<>();
    private ArrayList AreaList = new ArrayList();
    private ArrayList InfoList = new ArrayList();
    private ArrayList NewsList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.fragment_zhengwu, container, false);
        initView();
        getNewsPolitics();
        return mMainView;
    }

    private void initView() {
        tv_yongzhouhao = (TextView) mMainView.findViewById(R.id.tv_yongzhouhao);
        tv_dingyue = (TextView) mMainView.findViewById(R.id.tv_dingyue);
        ll_layout = (LinearLayout) mMainView.findViewById(R.id.ll_layout);
        ll_showlead = (LinearLayout) mMainView.findViewById(R.id.ll_showlead);
        myGridView = (MyGridView) mMainView.findViewById(R.id.lv_jiguan);
        this.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (politicsAreaModelList != null && politicsAreaModelList.size() != 0 && position != politicsAreaModelList.size()) {
                    String DeptID = (politicsAreaModelList.get(position)).getAreaID();
                    String aredIcon = (politicsAreaModelList.get(position)).getAreaSmallPicUrl();
                    String aredNanme = (politicsAreaModelList.get(position)).getAreaPoliticsAreaName();
                    String reMark = (politicsAreaModelList.get(position)).getAreaReMark();
                    Intent intent = new Intent(getActivity(), InstitutionIndexActivity.class);
                    intent.putExtra("DeptID", DeptID);
                    intent.putExtra("reMark", reMark);
                    intent.putExtra("aredIcon", aredIcon);
                    intent.putExtra("aredNanme", aredNanme);
                    startActivity(intent);
                }
            }
        });
        tv_yongzhouhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_yongzhouhao.setTextColor(getResources().getColor(R.color.comment_blue));
                tv_dingyue.setTextColor(getResources().getColor(R.color.grey));
                myGridView.setVisibility(View.VISIBLE);
                ll_showlead.setVisibility(View.VISIBLE);
            }
        });
        tv_dingyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_yongzhouhao.setTextColor(getResources().getColor(R.color.grey));
                tv_dingyue.setTextColor(getResources().getColor(R.color.comment_blue));
                myGridView.setVisibility(View.GONE);
                ll_showlead.setVisibility(View.GONE);
            }
        });

        mMainView.findViewById(R.id.iv_ypf).setOnClickListener(this);
        mMainView.findViewById(R.id.iv_lzr).setOnClickListener(this);
        lv_model_list = (MyListView) mMainView.findViewById(R.id.lv_model_list);
        adapter = new ModelListAdapter(getActivity().getLayoutInflater(), subscribeList);
        lv_model_list.setAdapter(adapter);
        getSubscribe();
        lv_model_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                subscribeList.get(position).getID();
//                String DeptID = (politicsAreaModelList.get(position)).getAreaID();
//                String aredIcon = (politicsAreaModelList.get(position)).getAreaSmallPicUrl();
//                String aredNanme = (politicsAreaModelList.get(position)).getAreaPoliticsAreaName();
//                String reMark = (politicsAreaModelList.get(position)).getAreaReMark();
//                Intent intent = new Intent(getActivity(), InstitutionIndexActivity.class);
//                intent.putExtra("DeptID", DeptID);
//                intent.putExtra("reMark", reMark);
//                intent.putExtra("aredIcon", aredIcon);
//                intent.putExtra("aredNanme", aredNanme);
//                startActivity(intent);

            }
        });
    }

    @Override
    public void inite() {
        if (!isInite) {
            getPoliticIndexInfomation();
            isInite = true;
        }
    }

    public void getPoliticIndexInfomation() {
        String url = WenZhengAPI.POLITICS_INDEX_URL;
        String StID = "0";
        this.get_politicsIndex_info(this.getActivity(), url, StID, new Messenger(this.mHandler));
    }

    public void getNewsPolitics() {
        String url = IndexAPI.API_NEWS_POLITICS;
        String StID = "0";
        this.get_newspolitics(this.getActivity(), url, StID, new Messenger(this.mHandler));
    }

    private void get_newspolitics(Context paramContext, String url, String StID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_NEW_POLITICS);
        intent.putExtra(IndexAPI.GET_NEW_POLITICS_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        paramContext.startService(intent);
    }

    public void get_politicsIndex_info(Context paramContext, String url, String StID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", 313);
        intent.putExtra(WenZhengAPI.POLITICS_INDEX_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        paramContext.startService(intent);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ArrayList list = (ArrayList) msg.getData().getParcelableArrayList("list").get(0);
                    politicsAreaModelList = (List) list.get(1);
                    if (politicsAreaModelList != null) {
                        myGridView.setAdapter(new HallGridviewAdapter(getActivity().getLayoutInflater(), politicsAreaModelList));
                    }
                    break;
                case 221:

                    ArrayList list2 = (ArrayList) msg.getData().getParcelableArrayList("list").get(0);
                    subscribeList = (List<SubscribeListsModel>) list2;
                    adapter.setData(subscribeList);
                    adapter.notifyDataSetChanged();
                    adapter.setOnImageViewClickListener(new ModelListAdapter.OnImageViewClickListener() {
                        @Override
                        public void onImageViewClick(int position) {
                            SubscribeListsModel subscribe = subscribeList.get(position);
                            if (Assistant.getUserInfoByOrm(getActivity()) != null) {
                                UpadteState(Assistant.getUserInfoByOrm(getActivity()).getUserGUID(), subscribe.getIsPolitic(), subscribe.getPoliticID());
                            } else {
                                Intent intent = new Intent();
                                intent.setAction(basePackage + "login");
                                startActivity(intent);
                            }
                        }
                    });
                    break;
                case 2220:
                    //订阅或者取消订阅失败
                    break;
                case 2221:
                    getSubscribe();
                    break;
                case 404:
                    break;
                case 330:
                    ArrayList completeData = (ArrayList) msg.getData().getParcelableArrayList("list").get(0);
                    Toast.makeText(getActivity(), completeData.size(), Toast.LENGTH_SHORT).show();
//                    AreaList = (ArrayList) completeData.get(0);
//                    InfoList = (ArrayList) completeData.get(1);
//                    NewsList = (ArrayList) completeData.get(2);
                    break;
            }
        }
    };

    @Override
    public void onResume() {

        mHandler.sendEmptyMessage(2221);

        super.onResume();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), LeaderIndexActivity.class);
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_ypf:
                intent.putExtra("id", "20");
                intent.putExtra("aredNanme", "市委市政府");
                intent.putExtra("url", "http://116.62.29.20:8077/Uploads/Images/20170905095331051553000.jpg");
                break;
            case R.id.iv_lzr:
                intent.putExtra("id", "9");
                intent.putExtra("aredNanme", "市委市政府");
                intent.putExtra("url", "http://116.62.29.20:8077/Uploads/Images/20170905111538677864000.jpg");
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    /**
     * 从服务器获取订阅栏目
     */
    public void getSubscribe() {
        UserInfoModel model = Assistant.getUserInfoByOrm(getActivity());
        String url = API.COMMON_URL
                + "Interface/PoliticsAPI.ashx?action=NewsUpdateList";
        String GUID = "";
        if (model != null) {
            GUID = model.getUserGUID();
        }
        get_subscribe_list(getActivity(), url, GUID, new Messenger(
                this.mHandler));

    }

    /**
     * 改变订阅状态
     */
    public void UpadteState(String GUID, String isAdd, String PoliticID) {

        String url = API.COMMON_URL
                + "Interface/PoliticsAPI.ashx?action=SubscribeAddOrUpdate";
        get_subscribe_update(getActivity(), url, GUID, isAdd, PoliticID, new Messenger(this.mHandler));

    }

    /**
     * 改变状态
     *
     * @param paramContext
     * @param url
     * @param GUID
     * @param isAdd
     * @param PoliticID
     * @param paramMessenger
     */
    private void get_subscribe_update(Context paramContext, String url, String GUID, String isAdd, String PoliticID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_SUBSCRIBE_UPDATE);
        intent.putExtra(IndexAPI.GET_SUBSCRIBE_UPDATE_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("GUID", GUID);
        intent.putExtra("isAdd", isAdd);
        intent.putExtra("PoliticID", PoliticID);
        paramContext.startService(intent);
    }

    /**
     * 获取我的订阅
     *
     * @param paramContext
     * @param url
     * @param GUID
     * @param paramMessenger
     */
    public static void get_subscribe_list(Context paramContext, String url, String GUID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_ZWSUBSCRIBE_LIST);
        intent.putExtra(IndexAPI.GET_ZWSUBSCRIBE_LIST_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("GUID", GUID);
        paramContext.startService(intent);
    }
}
