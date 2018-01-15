package com.dingtai.jinriyongzhou.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.api.UsercenterAPI;
import com.dingtai.base.model.NewsListModel;
import com.dingtai.base.model.UserCollects;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.view.Bookends;
import com.dingtai.base.view.Tag;
import com.dingtai.base.view.TagListView;
import com.dingtai.base.view.TagView;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.model.Topice;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.adapter.RecyclerViewAdapter;
import com.dingtai.newslib3.model.Topics;
import com.dingtai.newslib3.service.NewsHttpService;
import com.dingtai.newslib3.tool.StartActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class NewTopiceActivity extends BaseActivity {

    private ImageView topicslogo;
    private TextView topicsname;
    private TextView remake;
    private TagListView tagview;

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<NewsListModel> mData;

    private final List<Tag> mTags = new ArrayList<Tag>();
    private TextView title;

    private ImageView command_sc, command_share;

    // 用户收藏
    private RuntimeExceptionDao<UserCollects, String> collects;

    // 用户GuID
    private String userGuid = "";
    private String DeptID = "";
    private String userName = "";

    private TextView tv_dingyue,tv_tougao;

    private Bookends<RecyclerViewAdapter> mBookends;

    private List<Integer> poiList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_topice2);
        initView();
        isCollect();
        requestData();
    }

    // 是否点赞收藏
    private void isCollect() {
        // TODO Auto-generated method stub
        if (Assistant.getUserInfoByOrm(this) != null) {
            userGuid = Assistant.getUserInfoByOrm(this).getUserGUID();
            userName = Assistant.getUserInfoByOrm(this).getUserName();
            List<UserCollects> relatedCollects = getRelatedCollects(userGuid);
            for (UserCollects u : relatedCollects) {
                if (u.getCollectID().equals(getIntent().getStringExtra("ParentID"))) {
                    command_sc.setImageResource(R.drawable.news_sc_v2_1);
                    break;
                }
            }
        }

    }

    // TODO 从数据库中的到收藏信息
    private List<UserCollects> getRelatedCollects(String userGuid) {
        collects = getHelper().getMode(UserCollects.class);
        List<UserCollects> collectsList = new ArrayList<UserCollects>();
        try {
            if (collects.isTableExists()) {
                List<UserCollects> forEq = collects.queryForEq("UserGUID",
                        userGuid);
                collectsList.addAll(forEq);

            }
        } catch (Exception e) {
        }
        return collectsList;
    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {

                case API.HANDLER_GETDATA_SUCCESS:
                    ArrayList<String> temp_list = (ArrayList<String>) msg.getData()
                            .getParcelableArrayList("list").get(0);// 获取点击收藏后返回的收藏id
                    String id = "";
                    if (temp_list != null && temp_list.size() > 0) {
                        id = temp_list.get(0);
                        UserCollects queryForId = collects.queryForId(getIntent().getStringExtra("ParentID"));
                        if (queryForId != null) {
                            queryForId.setID(id);
                            collects.update(queryForId);
                        }
                    }
                    Toast.makeText(NewTopiceActivity.this, "成功收藏！", Toast.LENGTH_SHORT).show();
                    break;

                case 100:
                    mData = new ArrayList<>();
                    final ArrayList<Topice> list = (ArrayList<Topice>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    if (list.size() > 0) {
                        if (list.get(0).getTopics().size() > 0) {
                            List<Topics> Topics = list.get(0).getTopics();
                            for (int i = 0; i < Topics.size(); i++) {
                                NewsListModel model = new NewsListModel();
                                model.setTitle(i + 1 + "/" + Topics.size() + " " + Topics.get(i).getTopicsName());
                                model.setChID(Topics.get(i).getTopicsID());
                                model.setChannelName(Topics.get(i).getTopicsName());
                                model.setID("group");
                                poiList.add(mData.size());
                                mData.add(model);
                                if (Topics.get(i).getTopicsNews().size() > 0) {
                                    mData.addAll(Topics.get(i).getTopicsNews());
                                }

                            }
                        }

                        mAdapter = new RecyclerViewAdapter(NewTopiceActivity.this, mData);
                        mBookends = new Bookends<>(mAdapter);
                        // Inflate header and footer views
                        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                        View view = (View) inflater.inflate(R.layout.headview_newstop2, mRecyclerView, false);


                        topicslogo = (ImageView) view.findViewById(R.id.topicslogo);
//                        LayoutParams par = new LayoutParams(DisplayMetricsTool.getWidth(NewTopiceActivity.this), DisplayMetricsTool.getWidth(NewTopiceActivity.this) / 3);
//                        topicslogo.setLayoutParams(par);
                        tv_dingyue = (TextView) view.findViewById(R.id.tv_dingyue);
                        tv_tougao = (TextView) view.findViewById(R.id.tv_tougao);

                        topicsname = (TextView) view.findViewById(R.id.topicsname);
                        remake = (TextView) view.findViewById(R.id.remake);
                        tagview = (TagListView) view.findViewById(R.id.tagview);


                        tagview.setOnTagClickListener(new TagListView.OnTagClickListener() {

                            @Override
                            public void onTagClick(TagView tagView, Tag tag) {

                                mLayoutManager.scrollToPositionWithOffset(poiList.get(tag.getId()) + 1, 0);
                                mLayoutManager.setStackFromEnd(true);

                            }
                        });

                        ImageLoader.getInstance().displayImage(list.get(0).getLogo(), topicslogo);
                        topicsname.setText(list.get(0).getTitle());
                        if (list.get(0).getReMark().equals("")) {
                            remake.setVisibility(View.GONE);
                        } else {
                            remake.setText(list.get(0).getReMark());
                            remake.setVisibility(View.VISIBLE);
                        }
                        if ("0".equals(list.get(0).getIscloose())){
                            tv_dingyue.setText("未订阅");
                        }else {
                            tv_dingyue.setText("已订阅");
                        }

                        tv_tougao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(NewTopiceActivity.this,ContributeActivity.class);
                                intent.putExtra("title",getIntent().getStringExtra("aredNanme"));
                                intent.putExtra("id",DeptID);
                                startActivity(intent);
                            }
                        });

                        tv_dingyue.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Assistant.getUserInfoByOrm(NewTopiceActivity.this)==null){
                                    Toast.makeText(NewTopiceActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.setAction(NewTopiceActivity.this.getPackageName() + "." + "login");
                                    startActivity(intent);
                                }else {
                                    String url = API.COMMON_URL + "Interface/PoliticsAPI.ashx?action=SubscribeAddOrUpdate";
                                    String GuID = Assistant.getUserInfoByOrm(NewTopiceActivity.this).getUserGUID();
                                    String isAdd = list.get(0).getIscloose();
//                                    if ("1".equals(list.get(0).getIscloose())){
//                                        isAdd = "1";
//                                    }else {
//                                        isAdd = "0";
//                                    }
                                    change_state(NewTopiceActivity.this, url,GuID,isAdd,DeptID, new Messenger(
                                            NewTopiceActivity.this.handler));

                                }
                            }
                        });


                        command_share.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                // TODO Auto-generated method stub
                                BaseShare bs = null;
                                try {

                                    bs = new BaseShare(NewTopiceActivity.this,
                                            getIntent().getStringExtra("aredNanme"), API.ShareName + "，主流新媒体，权威发声！", API.ICON_URL + "/Share/ProjectShares.aspx?ID=" + getIntent().getStringExtra("DeptID"), API.ICON_URL + "Images/ic_launcher.png", "99", "");

                                    bs.oneShare();
                                    // // 长江新闻 转发
                                    // CJstatistics(4, rid, rname, news_detail.getResourceGUID(),
                                    // news_detail.getTitle(), news_detail.getChID(),
                                    // news_detail.getChannelName());
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }
                        });

                        for (int i = 0; i < list.get(0).getTopics().size(); i++) {
                            Tag tag = new Tag();
                            tag.setId(i);
                            tag.setChecked(true);
                            tag.setTitle(list.get(0).getTopics().get(i).getTopicsName());
                            mTags.add(tag);

                        }


                        tagview.setTagViewBackgroundRes(R.drawable.tag_topice_bg);
                        tagview.setTags(mTags);

                        mBookends.addHeader(view);
                        mRecyclerView.setAdapter(mBookends);
                        mAdapter.setOnItemClickLitener(new RecyclerViewAdapter.OnItemClickLitener() {

                            @Override
                            public void onItemClick(View view, int position) {
                                // TODO Auto-generated method stub
                                StartActivity.toLanmu(NewTopiceActivity.this, mData.get(position));
                            }
                        });


                    }


                    break;
                case 101:// 获取数据为空

                    Toast.makeText(NewTopiceActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 222:// 无网络
                case 555:
                    Toast.makeText(NewTopiceActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();

                    break;
                case 2220:
                    Toast.makeText(NewTopiceActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 2221:
                    Toast.makeText(NewTopiceActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;


            }
        }
    };


    private void requestData() {
        // TODO Auto-generated method stub
        String url = API.COMMON_URL + "Interface/NewsTopicsAPI.ashx?action=GetPoliticsAreaAndNews&ID=" + DeptID + "&StID=" + API.STID + "&UserGUID=" + userGuid;

        get_newTopice(this, url, new Messenger(
                NewTopiceActivity.this.handler));

    }

    private void change_state(Context context, String url,String UserGuID,String isAdd,String PoliticID, Messenger paramMessenger) {

        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.GET_SUBSCRIBE_UPDATE);
        localIntent.putExtra(IndexAPI.GET_SUBSCRIBE_UPDATE_API, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("GuID", UserGuID);
        localIntent.putExtra("isAdd", isAdd);
        localIntent.putExtra("PoliticID", DeptID);
        context.startService(localIntent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null)
            mAdapter.setUser(Assistant.getUserInfoByOrm(getApplicationContext()));
    }

    private void initView() {
        // TODO Auto-generated method stub


        command_sc = (ImageView) this.findViewById(R.id.command_sc);
        command_share = (ImageView) this.findViewById(R.id.command_share);

        command_sc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // 收藏 点击事件
                boolean flag = Assistant.IsContectInterNet(
                        NewTopiceActivity.this, false);
                if (flag == true) {
                    if (Assistant.getUserInfoByOrm(NewTopiceActivity.this) != null) {
                        boolean f = false;
                        if (mData != null) {
                            if (collects == null) {
                                collects = getHelper().getMode(UserCollects.class);
                            }
                            if (collects.isTableExists()) {
                                List<UserCollects> forEq = collects.queryForEq(
                                        "UserGUID", userGuid);
                                for (UserCollects u : forEq) {
                                    if (u.getCollectID().equals(getIntent().getStringExtra("ParentID"))) {
                                        command_sc.setImageResource(R.drawable.news_sc_v2);
//										del_user_collects(
//												NewTopiceActivity.this,
//												UsercenterAPI.DEL_COLLECTS_URL,
//												u.getID(), new Messenger(handler));
                                        String url = UsercenterAPI.DEL_COLLECTS_URL + "&ID=" + u.getID();
                                        requestData(getApplicationContext(), url, new Messenger(handler), UsercenterAPI.DEL_COLLECTS_API, UsercenterAPI.DEL_COLLECTS_MESSAGE, NewsHttpService.class);
                                        collects.deleteById(getIntent().getStringExtra("ParentID"));
                                        // Toast.makeText(TestNewsDetailActivity.this,
                                        // "已取消收藏！", 1000).show();
                                        f = true;
                                        break;
                                    }
                                }
                                if (!f) {
                                    UserCollects collect = new UserCollects();
                                    collect.setStid(API.STID);
                                    collect.setCollectID(getIntent().getStringExtra("ParentID"));
                                    collect.setUserGUID(userGuid);
                                    collect.setUserName(userName);
                                    collect.setCollectType("3");
                                    if (!collects.idExists(getIntent().getStringExtra("ParentID"))) {
                                        collects.create(collect);
                                    }
//									add_user_collects(NewTopiceActivity.this,
//											UsercenterAPI.ADD_COLLECTS_URL,
//											"3", API.STID,
//											getIntent().getStringExtra("ParentID"), userGuid, userName,
//											new Messenger(handler));
                                    String url = UsercenterAPI.ADD_COLLECTS_URL + "&CollectType=3" + "&stid=" + API.STID + "&CollectID=" + getIntent().getStringExtra("ParentID") + "&UserGUID=" + userGuid + "&userName=" + userName;
                                    requestData(getApplicationContext(), url, new Messenger(handler), UsercenterAPI.ADD_COLLECTS_API, UsercenterAPI.ADD_COLLECTS_MESSAGE, NewsHttpService.class);
                                    command_sc.setImageResource(R.drawable.news_sc_v2_1);
                                }
                            }
                        } else {
                            Toast.makeText(NewTopiceActivity.this,
                                    "数据正在加载中请加载完后再收藏！", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // 未登录，先进行登录操作
                        Toast.makeText(NewTopiceActivity.this, "请您先登录！",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(basePackage + "login");
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(NewTopiceActivity.this, "请检查网络！",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        title = (TextView) findViewById(R.id.command_title);
        title.setText(getIntent().getStringExtra("aredNanme"));
        DeptID = getIntent().getStringExtra("DeptID");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        this.findViewById(R.id.command_return).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        // setlayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //setAdapter


    }


    private void get_newTopice(Context context, String url, Messenger paramMessenger) {

        Intent localIntent = new Intent(context, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.NEWTOPICE_API);
        localIntent.putExtra(IndexAPI.NEWTOPICE_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        context.startService(localIntent);
    }

}
