package com.dingtai.jinriyongzhou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.MyListView;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.PoliticsNewsAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.InstitutionDetailModel;
import com.dingtai.jinriyongzhou.model.PoliticsNewsModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @作者 Zhou
 * @时间 2017年11月29日 机构详情
 */
public class InstitutionDetailActivity extends BaseFragmentActivity {

    private RelativeLayout relative_net;//无网络
    private PullToRefreshScrollView scrollView;
    private CircularImage iv_icon;//机构图标
    private TextView tv_institution;//机构名称
    private TextView tv_dingyue;//订阅标志
    private TextView tv_tougao;//投稿
    private TextView tv_zhubian;//主编
    private TextView tv_detail;//机构内容

    private Intent intent;
    private String DeptID;

    private ArrayList<PoliticsNewsModel> newsList = new ArrayList();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case IndexAPI.GET_INSTITUTION_DETAIL:
                    ArrayList<InstitutionDetailModel> list = (ArrayList<InstitutionDetailModel>) msg.getData().getParcelableArrayList("list").get(0);
                    if (list.size() > 0) {
                        InstitutionDetailModel model = list.get(0);
                        tv_institution.setText(model.getAreaPoliticsAreaName());
                        tv_detail.setText(model.getAreaReMark());
                        tv_zhubian.setText(model.getAreaEditor());
                        if (model.getAreaIsChoose().equals("True")) {
                            tv_dingyue.setText("已订阅");
                        } else {
                            tv_dingyue.setText("未订阅");
                        }
                        ImageLoader.getInstance().displayImage(
                                model.getAreaSmallPicUrl(),
                                iv_icon);
                    }

                    break;
                case IndexAPI.GET_INSTITUTION_LIST:
                    newsList = (ArrayList<PoliticsNewsModel>) msg.getData().getParcelableArrayList("list").get(0);
                    if (newsList.size() > 0) {

                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.institution_detail_activity);
        initView();//初始化控件
        getData();//获取前面页面的数据
    }

    private void getData() {
        intent = getIntent();
        DeptID = intent.getStringExtra("DeptID");
        getIndexDetail(DeptID);


//        intent.putExtra("DeptID", DeptID);
//        intent.putExtra("reMark", reMark);
//        intent.putExtra("aredIcon", aredIcon);
//        intent.putExtra("aredNanme", aredNanme);
    }

    private void initView() {
        relative_net = (RelativeLayout) findViewById(R.id.relative_net);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        iv_icon = (CircularImage) findViewById(R.id.leader_icon);
        tv_institution = (TextView) findViewById(R.id.institution_tv);
        tv_dingyue = (TextView) findViewById(R.id.tv_dingyue);
        tv_tougao = (TextView) findViewById(R.id.tv_tougao);
        tv_zhubian = (TextView) findViewById(R.id.tv_zhubian);
        tv_detail = (TextView) findViewById(R.id.tv_detail);


        tv_tougao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstitutionDetailActivity.this,ContributeActivity.class);
                intent.putExtra("title",tv_institution.getText().toString());
                intent.putExtra("id",DeptID);
                startActivity(intent);
            }
        });
        }


    private void getIndexDetail(String DeptID) {
        String url = API.COMMON_URL
                + "interface/PoliticsNewAPI.ashx?action=GetDeptDetailByID";
        get_index_detail(this, url, DeptID, new Messenger(
                this.handler));

    }

    /**
     * 获取永州号列表
     *
     * @param paramContext
     * @param url
     * @param ID
     * @param paramMessenger
     */
    private void get_instution_list(Context paramContext, String url, String ID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_INSTITUTION_LIST);
        intent.putExtra(IndexAPI.GET_INSTITUTION_LIST_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("ID", ID);
        paramContext.startService(intent);
    }

    /**
     * 获取头部信息
     *
     * @param paramContext
     * @param url
     * @param ID
     * @param paramMessenger
     */
    private void get_index_detail(Context paramContext, String url, String ID, Messenger paramMessenger) {
        Intent intent = new Intent(paramContext, IndexHttpService.class);
        intent.putExtra("api", IndexAPI.GET_INSTITUTION_DETAIL);
        intent.putExtra(IndexAPI.GET_INSTITUTION_DETAIL_API, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("ID", ID);
        paramContext.startService(intent);
    }
}
