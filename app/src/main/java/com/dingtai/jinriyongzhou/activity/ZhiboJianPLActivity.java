package com.dingtai.jinriyongzhou.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.api.NewsAPI;
import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.model.DigPai;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshScrollView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.userscore.ShowJiFen;
import com.dingtai.base.userscore.UserScoreConstant;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.CommentUtils;
import com.dingtai.base.utils.DecodeStringUtil;
import com.dingtai.base.utils.DisplayMetricsTool;
import com.dingtai.base.view.MyListView;
import com.dingtai.dtlogin.activity.LoginActivity;
import com.dingtai.dtsetting.activity.SettingActivityNew;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.ModelZhiboHuifu;
import com.dingtai.jinriyongzhou.model.ModelZhiboPinglun;
import com.dingtai.newslib3.service.NewsHttpService;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ZhiboJianPLActivity extends BaseActivity {
    private LinearLayout ll_bottom;// 评论

    private PullToRefreshScrollView mPullRefreshScrollView;
    private MyListView mListview;
    private List<ModelZhiboPinglun> mTemListDate;
    private List<ModelZhiboPinglun> mListDate;
    private boolean downup;
    private String state;
    private UserInfoModel mUser;
    private InputMethodManager imm;
    private CommentAdapter mAdapter;
    private String STID;// 实例化站点ID
    private String mZhiboID;// 实例化zhiboId
    private static boolean ispl = true;
    private PopupWindow pwpl;
    private String mPid;// 直播的评论ID
    private MyPopPlViewHolder mppvh;// 泡泡窗口
    private String ZhiboAPI;// 实例化API
    private boolean isNight;
    private String EventStatus = "0";


    private TextView live_room_title, live_room_time;


    // 115.28.191.195:8010
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            downup = false;
            switch (msg.what) {
                case 722:
                    int k = (Integer) msg.obj; // 位置
                    mListDate.get(k).setIsShow(true);
                    mAdapter.notifyDataSetChanged();
                    // 调到指定位置
                    // mListview.getsetSelection(k);
                    break;
                case 10:// 获取数据为空
                    mPullRefreshScrollView.onRefreshComplete();
                    Toast.makeText(ZhiboJianPLActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                    break;
                case 222:// 无网络
                    Toast.makeText(ZhiboJianPLActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 555:
                    if (Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this) != null
                            && UserScoreConstant.ScoreMap
                            .containsKey(UserScoreConstant.SCORE_DIG_PAI)) {
                        new ShowJiFen(ZhiboJianPLActivity.this,
                                UserScoreConstant.SCORE_DIG_PAI,
                                UserScoreConstant.SCORE_TYPE_ADD,
                                UserScoreConstant.ExchangeID,
                                Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this),
                                "");
                    }
                    Toast.makeText(ZhiboJianPLActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    mPullRefreshScrollView.onRefreshComplete();
                    break;
                case 7000:// 提示消息
                    Toast.makeText(ZhiboJianPLActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 7001:// 完成提示消息并刷新
                    if (Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this) != null
                            && UserScoreConstant.ScoreMap
                            .containsKey(UserScoreConstant.SCORE_DIG_PAI)) {
                        new ShowJiFen(ZhiboJianPLActivity.this,
                                UserScoreConstant.SCORE_DIG_PAI,
                                UserScoreConstant.SCORE_TYPE_ADD,
                                UserScoreConstant.ExchangeID,
                                Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this),
                                "");
                    }
                    imm.hideSoftInputFromWindow(
                            mppvh.plpop_edit_content.getWindowToken(), 0);
                    Toast.makeText(ZhiboJianPLActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    mppvh.plpop_edit_content.setText("");
                    state = "down";


                    getDate();
                    break;


                case 100:// 获取数据
                    mPullRefreshScrollView.onRefreshComplete();
                    ArrayList<ModelZhiboPinglun> temp_list = (ArrayList<ModelZhiboPinglun>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    mTemListDate.addAll(temp_list);
                    if (state.equals("up")) { // 上拉
                        if (mTemListDate.size() > 0) {
                            mListDate.addAll(mTemListDate);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ZhiboJianPLActivity.this, "暂无更多数据", Toast.LENGTH_SHORT).show();
                        }
                    } else if (mTemListDate.size() > 0) {
                        mListDate.clear();
                        mListDate.addAll(mTemListDate);

                        mAdapter.notifyDataSetChanged();
                    }


                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        initView();
    }


    @Override
    public void onStart() {
        super.onStart();
        mUser = Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this);
    }


    private void initView() {
        imm = (InputMethodManager) ZhiboJianPLActivity.this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (SettingActivityNew.IS_NIGHT) {
            isNight = true;
        } else {
            isNight = false;
        }
        // 数据初始化
        ZhiboAPI = NewsAPI.ZHIBOHUDONG_HUDONG_TEST;
        STID = API.STID;
        mZhiboID = getIntent().getStringExtra("zhiboid");
        state = "";
        downup = false;


        live_room_title = (TextView) findViewById(R.id.live_room_title);
        live_room_title.setText(getIntent().getStringExtra("title"));

        live_room_time = (TextView) findViewById(R.id.live_room_time);
        live_room_time.setText(getIntent().getStringExtra("time"));


        // mUser = Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this);
        // // 测试数据
        // mUser = new UserInfoModel();
        // mUser.setUserGUID("E5D95959-A131-4DA4-BE3B-ABEBB0920411");
        // mUser.setUserName("");
        // // 测试数据
        mUser = Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this);
        mListDate = new ArrayList<ModelZhiboPinglun>();
        mTemListDate = new ArrayList<ModelZhiboPinglun>();
        // listview适配
        mListview = (MyListView) findViewById(R.id.listviewzhibo);
        // mPullRefreshScrollView = (PullToRefreshScrollView) mMainView
        // .findViewById(R.id.pull_refresh_scrollview);
        // mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        // mListview.setMode(Mode.BOTH);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.zhibo_pulllistview);
        mPullRefreshScrollView.setOnRefreshListener(mOnRefreshListener);
        mPullRefreshScrollView.setHeaderLayout(new PullHeaderLayout(this));
        mPullRefreshScrollView.setFooterLayout(new LoadingFooterLayout(this, PullToRefreshBase.Mode.PULL_FROM_END));

        // 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
        // 如修改（Mode.BOTH为Mode.PULL_FROM_START）
        // 修改（Mode.PULL_FROM_END 为Mode.DISABLE）

        mPullRefreshScrollView.setHasPullUpFriction(false); // 设置没有上拉阻力
        getDate();
        mAdapter = new CommentAdapter(ZhiboJianPLActivity.this, mListDate);
        mListview.setAdapter(mAdapter);
        // 评论
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);

        // 写评论的窗口
        View popplview = ZhiboJianPLActivity.this.getLayoutInflater().inflate(
                R.layout.pinglun_popupwindow, null);
        // 初始化泡泡窗口（写评论的窗口）
        initpwpl(popplview, 1);

        pwpl = new PopupWindow(popplview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pwpl.setFocusable(true);
        pwpl.setBackgroundDrawable(new BitmapDrawable());

        findViewById(R.id.btn_news_return).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        findViewById(R.id.txt_pinglun).setOnTouchListener(
                new OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            if (mUser != null) {
                                // if (Assistant.getUserInfoByOrm(ZhiboJianPLActivity.this)
                                // == null) {


                                mppvh.ews_luntan_pinglun_shenfen.setText("评论");
                                ispl = true;
                                openpwpl();

                            } else {
                                Toast.makeText(ZhiboJianPLActivity.this, "请先登录！", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent2 = new Intent(ZhiboJianPLActivity.this,
                                        LoginActivity.class);
                                startActivity(intent2);
                            }
                        }
                        return false;
                    }
                });


        EventStatus = getIntent().getStringExtra("isEnd");

        if ("0".equals(EventStatus) || "1".equals(EventStatus)) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }

    }

    // 初始化评论里的控件(泡泡窗口。写评论)
    private void initpwpl(View v, int i) {
        // 实例化浮动窗口的组件类
        mppvh = new MyPopPlViewHolder();
        mppvh.huifu_ll = (LinearLayout) v.findViewById(R.id.huifu_ll);
        mppvh.plpop_btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        mppvh.plpop_btn_submit = (Button) v.findViewById(R.id.btn_ok);
        mppvh.ews_luntan_pinglun_shenfen = (TextView) v
                .findViewById(R.id.news_luntan_pinglun_shenfen);
        mppvh.plpop_edit_content = (EditText) v
                .findViewById(R.id.et_edit_content);
        // 取消
        mppvh.plpop_btn_cancel.setOnClickListener(new MyPLListener());
        // 提交
        mppvh.plpop_btn_submit.setOnClickListener(new MyPLListener());
        // 回复
        mppvh.huifu_ll.setOnClickListener(new MyPLListener());
        // 获取用户数据
        // share = getSharedPreferences("UserInfo", 0);
        // if(user != null){
        // uname = user.getUserName();
        // uid = user.getUserGUID();
        // }
        mppvh.ews_luntan_pinglun_shenfen.setText("评论");
    }

    // 评论和回复数据提交事件
    class MyPLListener implements OnClickListener {
        private String s3 = "请您先登录后再评论！";
        private String strtext;

        @Override
        public void onClick(View v) {

            ll_bottom.setFocusable(true);
            ll_bottom.setFocusableInTouchMode(true);
            switch (v.getId()) {
                // 提交、回复
                case R.id.btn_ok:
                    strtext = mppvh.plpop_edit_content.getText().toString();
                    if (strtext.isEmpty()) {
                        Toast.makeText(ZhiboJianPLActivity.this, "请先输入内容！", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    if (!Assistant.IsContectInterNet(ZhiboJianPLActivity.this, false)) {
                        handler.sendEmptyMessage(222);
                    }
                    String Econ = "";
                    try {
                        Econ = java.net.URLEncoder.encode(strtext, "UTF-8"); // 拿到内容
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mUser != null) {
                        String userId = mUser.getUserGUID();
                        // 评论(默认pid为空)
                        if (ispl) { // 判断是不是评论

                            add_ZhiboPinglun(ZhiboJianPLActivity.this, "", Econ, "0", userId,
                                    STID,
                                    new Messenger(ZhiboJianPLActivity.this.handler));
                            // 回复
                        } else {
                            add_ZhiboPinglun(ZhiboJianPLActivity.this, mPid, Econ, "0",
                                    userId, STID, new Messenger(
                                            ZhiboJianPLActivity.this.handler));
                        }
                    } else {
                        // 未登录，先进行登录操作
                        if (!ispl) {
                            s3 = "请先登录后再回复!";
                        }
                        Toast.makeText(ZhiboJianPLActivity.this, s3, Toast.LENGTH_SHORT)
                                .show();
                        Intent intent2 = new Intent(ZhiboJianPLActivity.this,
                                LoginActivity.class);
                        ZhiboJianPLActivity.this.startActivity(intent2);
                    }

                case R.id.huifu_ll:
                    pwpl.dismiss();
                    break;

                case R.id.btn_cancel:
                    pwpl.dismiss();
                    break;

                default:
                    break;
            }
        }

    }

    private void getDate() {
        mTemListDate.clear();
        String num = "10";
        String dtop = "";
        String url;
        if (state.equals("up")) {
            // up
            url = ZhiboAPI + "GetLiveCommentSubUpList";
            dtop = "" + mListDate.size();
        } else {
            url = ZhiboAPI + "GetLiveCommentSubDownList";
        }
        get_ZhiboHudong_list(ZhiboJianPLActivity.this, url, num, dtop, new Messenger(
                ZhiboJianPLActivity.this.handler));
    }

    private void get_ZhiboHudong_list(Context context, String url, String num,
                                      String dtop, Messenger paramMessenger) {

        Intent localIntent = new Intent(context, NewsHttpService.class);
        localIntent.putExtra("api", NewsAPI.ZHIBOHUDONG_LIST_API);
        localIntent.putExtra(NewsAPI.ZHIBOHUDONG_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("rid", mZhiboID);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("num", num);

        context.startService(localIntent);
    }

    private void AddGoodPoint(Context context, String Cid,
                              Messenger paramMessenger) {
        String url = ZhiboAPI + "AddGoodPoint";
        Intent localIntent = new Intent(context, NewsHttpService.class);
        localIntent.putExtra("api", NewsAPI.ZHIBOHUDONG_AddGoodPoint_API);
        localIntent.putExtra(NewsAPI.ZHIBOHUDONG_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("Cid", Cid);

        context.startService(localIntent);
    }

    private void add_ZhiboPinglun(Context context, String pid,
                                  String commentContent, String getGoodPoint, String userGUID,
                                  String StID, Messenger paramMessenger) {
        String url = ZhiboAPI + "InsertContent";
        Intent localIntent = new Intent(context, NewsHttpService.class);
        localIntent.putExtra("api", NewsAPI.ZHIBOHUDONG_ADDPINGLUN_API);
        localIntent.putExtra(NewsAPI.ZHIBOHUDONG_LIST_MESSAGE, paramMessenger);// "Zhibo.Messenger"
        localIntent.putExtra("url", url);
        localIntent.putExtra("pid", pid);
        localIntent.putExtra("commentContent", commentContent);
        localIntent.putExtra("getGoodPoint", getGoodPoint);
        localIntent.putExtra("userGUID", userGUID);
        localIntent.putExtra("rid", mZhiboID);
        localIntent.putExtra("StID", StID);
        context.startService(localIntent);
    }

    // add_ZhiboPinglun(ZhiboJianPLActivity.this, "pid", "commentContent",
    // "getGoodPoint", "userGUID", "StID", new Messenger(
    // FragmentHudong.this.handler));

    // AddGoodPoint(ZhiboJianPLActivity.this, "Cid", new Messenger(
    // FragmentHudong.this.handler));
    private PullToRefreshBase.OnRefreshListener2 mOnRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        public void onPullDownToRefresh(PullToRefreshBase paramPullToRefreshBase) {


            if (downup == false) {
                if (Assistant.IsContectInterNet(ZhiboJianPLActivity.this, false)) {
                    state = "down";
                    downup = true;
                    getDate();
                    // add_ZhiboPinglun(ZhiboJianPLActivity.this, "pid", "commentContent",
                    // "getGoodPoint", "userGUID", "StID", new Messenger(
                    // FragmentHudong.this.handler));
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (mListDate != null && mListDate.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }

        }

        public void onPullUpToRefresh(PullToRefreshBase paramPullToRefreshBase) {

            if (downup == false) {
                if (Assistant.IsContectInterNet(ZhiboJianPLActivity.this, false)) {
                    downup = true;
                    state = "up";
                    getDate();
                    // AddGoodPoint(ZhiboJianPLActivity.this, "Cid", new Messenger(
                    // FragmentHudong.this.handler));
                } else {
                    Message m = handler.obtainMessage();
                    m.obj = "请检查网络连接！";
                    if (mListDate != null && mListDate.size() == 0) {
                        m.what = 222;
                    } else {
                        m.what = 555;
                    }
                    handler.sendMessage(m);
                }
            }
        }
    };

    // 弹出评论登录框
    private void openpwpl() {
        pwpl.showAtLocation(findViewById(R.id.details_main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    static class MyPopPlViewHolder {
        public Button plpop_btn_cancel = null;
        public Button plpop_btn_submit = null;
        public EditText plpop_edit_content = null;
        public TextView ews_luntan_pinglun_shenfen = null;
        public LinearLayout huifu_ll = null;

    }

    // 评论适配器
    public class CommentAdapter extends BaseAdapter {
        private Context context;
        private List<ModelZhiboPinglun> list;
        private final static int COMMENT_INDEX = 5; // 楼层过高时,保留嵌套楼层数
        private final static int COMMENT_MAX = 7; // 楼层大于这个数的时候减少嵌套(嵌套过多会堆栈溢出)
        private int PADDING = 3; // 每层楼之间的间距
        private LinearLayout ll_pinglun;
        private TextView tv_develop_hide;
        RuntimeExceptionDao<DigPai, String> digPaiDAO;

        public CommentAdapter(Context context, List<ModelZhiboPinglun> list) {
            this.context = context;
            this.list = list;
            PADDING = new DisplayMetricsTool().dip2px(context, PADDING);
            digPaiDAO = ((DataBaseHelper) OpenHelperManager.getHelper(context,
                    DataBaseHelper.class)).getMode(DigPai.class);
        }

        public void setData(List<ModelZhiboPinglun> list) {
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
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            CommentViewHolder holder = null;

            if (convertView == null) {
                holder = new CommentViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.news_pinglun_item, null);
                holder.ll_floor = (LinearLayout) convertView
                        .findViewById(R.id.item_linearLayout);
                holder.idView = (TextView) convertView
                        .findViewById(R.id.tv_Item_ID);
                holder.commentContentView = (TextView) convertView
                        .findViewById(R.id.item_textView);
                holder.headPortraitView = (ImageView) convertView
                        .findViewById(R.id.iv_user_headPortrait);
                holder.userNameView = (TextView) convertView
                        .findViewById(R.id.tv_user_name);
                holder.userDateView = (TextView) convertView
                        .findViewById(R.id.tv_user_date);
                holder.iv_dingView = (ImageView) convertView
                        .findViewById(R.id.iv_ding);
                holder.iv_pingView = (ImageView) convertView
                        .findViewById(R.id.iv_ping);
                holder.tv_dingView = (TextView) convertView
                        .findViewById(R.id.tv_ding);
                holder.iv_plus1View = (ImageView) convertView
                        .findViewById(R.id.iv_plus1);
                holder.iv_zhidingView = (ImageView) convertView
                        .findViewById(R.id.iv_pl_zhiding);
                holder.iv_zjrzView = (ImageView) convertView
                        .findViewById(R.id.iv_pl_zjrz);
                // holder.tv_floorView = (TextView) convertView
                // .findViewById(R.id.tv_floor);
                convertView.setTag(holder);
            } else {
                holder = (CommentViewHolder) convertView.getTag();
                holder.ll_floor.setTag(null);
            }
            // 评论在集合第一个 后面的都是回复

            // 长点击后弹出文本复制对话框
            holder.commentContentView
                    .setOnLongClickListener(new OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // TODO Auto-generated method stub
                            selectdialog((TextView) v);
                            return true;
                        }
                    });
            // holder.tv_floorView.setText((position + 1) + "楼");// 楼层
            String Content = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getCommentContent());
            String pid = DecodeStringUtil.DecodeBase64ToUTF8(list.get(position)
                    .getID());
            String NickName = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getUserNickName());
            String UserName = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getUserName());
            String CommentTime = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getCommentTime());
            String GoodPoint = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getGetGoodPoint());
            holder.idView.setText(pid); // 评论id
            holder.commentContentView.setText(Content); // 评论内容
            holder.userDateView.setText(CommentTime); // 发表时间
            CommentUtils.setCommentUserName(holder.userNameView, NickName,
                    UserName);// 设置评论用户的显示
            // if ("1".equals(comment.getZjrz())) { // 专家认证
            // holder.iv_zjrzView.setVisibility(View.VISIBLE);
            // } else {
            // holder.iv_zjrzView.setVisibility(View.GONE);
            // }

            // if ("1".equals(comment.getZhiding())) {// 置顶
            // holder.iv_zhidingView.setVisibility(View.VISIBLE);
            // } else {
            // holder.iv_zhidingView.setVisibility(View.GONE);
            // }
            holder.tv_dingView.setText(GoodPoint == null ? "0" : GoodPoint); // 顶数
            // 用户头像图片
            String imgurl = DecodeStringUtil.DecodeBase64ToUTF8(list.get(
                    position).getUserIcon());
            if (imgurl == null || imgurl == "" || imgurl.equalsIgnoreCase("")) {
                holder.headPortraitView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.user_headimg));

            } else {
                holder.headPortraitView.setImageDrawable(getResources()
                        .getDrawable(R.drawable.user_head));
                try {
                    ImageLoader.getInstance().displayImage(imgurl,
                            holder.headPortraitView);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    holder.headPortraitView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.user_head));
                }
            }
            // 顶的点击事件
            holder.iv_dingView.setOnClickListener(new DingOnClickListener(
                    holder.iv_plus1View, holder.iv_dingView,
                    holder.tv_dingView, pid));
            // 回复事件(pid评论id name评论UserNickName)
            holder.iv_pingView.setOnClickListener(new PingOnClickListener(pid,
                    NickName));
            try {


                DigPai digpai = digPaiDAO.queryForId(DecodeStringUtil
                        .DecodeBase64ToUTF8(list.get(position).getID()));
                if (digpai != null && mUser != null
                        && mUser.getUserGUID().equals(digpai.getUserGuid())) {
                    holder.iv_dingView
                            .setImageDrawable(context
                                    .getResources()
                                    .getDrawable(
                                            R.drawable.biaoliao_dianzan1));
                } else {
                    holder.iv_dingView
                            .setImageDrawable(context
                                    .getResources()
                                    .getDrawable(
                                            R.drawable.baoliao_dianzan));
                }


            } catch (NotFoundException e) {
                e.printStackTrace();
            }


            // 开始盖楼(if语句是为了避免拖动listview的时候刷新导致数据重复调用递归)
            if (holder.ll_floor.getTag() == null) {
                holder.ll_floor.removeAllViews();
                holder.ll_floor.setTag("mark");
                List<ModelZhiboHuifu> list2 = list.get(position)
                        .getSubCommentList().getComments();
                if (list2.size() > 0) { // 保证至少有 一条回复
                    int number = list2.size();
                    if (list.get(position).isIsShow()) {
                        holder.ll_floor.addView(noHideFloor(context, number,
                                list2, true, number, 1));
                    } else {
                        holder.ll_floor.addView(hideFloor(number, list2,
                                number, number, position));
                    }

                }
            }

            return convertView;
        }

        private class CommentViewHolder {
            /**
             * 加载楼层的LinearLayout布局
             */
            public LinearLayout ll_floor;

            public TextView idView;
            /**
             * 用户的头像
             */
            public ImageView headPortraitView;
            /**
             * 评论的TextView
             */
            public TextView commentContentView;
            /**
             * 用户名
             */
            public TextView userNameView;

            /**
             * 发表时间
             */
            public TextView userDateView;

            public ImageView iv_pingView; // 发表回复按钮
            public ImageView iv_dingView; // 点击顶
            public TextView tv_dingView; // 顶数
            public ImageView iv_plus1View; // +1
            public ImageView iv_zhidingView; // 置顶
            public ImageView iv_zjrzView;// 专家认证
            // public TextView tv_floorView; // 楼层

        }

        // 顶评论事件
        private class DingOnClickListener implements OnClickListener {
            private ImageView iv_plus1, iv_ding;
            private String pinglunId;
            private TextView tv_ding;

            public DingOnClickListener(ImageView iv_plus1, ImageView iv_ding,
                                       TextView tv_ding, String pinglunId) {
                this.iv_plus1 = iv_plus1;
                this.pinglunId = pinglunId;
                this.tv_ding = tv_ding;
                this.iv_ding = iv_ding;
            }

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("0".equals(EventStatus) || "1".equals(EventStatus)) {
                    boolean bool = false;//
                    if (mUser == null) {

                        Intent intent = new Intent(ZhiboJianPLActivity.this,
                                LoginActivity.class);
                        ZhiboJianPLActivity.this.startActivity(intent);
                    } else {
                        DigPai dp = null;
                        if (digPaiDAO.isTableExists()
                                && digPaiDAO.idExists(pinglunId)) {
                            dp = digPaiDAO.queryForId(pinglunId);
                        }
                        // 如果digpai表存在且有id为ID的记录 但是用户guid对应不一致则说明此时为另外一个用户
                        if (dp != null) {
                            String userid = mUser.getUserGUID();
                            if (userid.equals(dp.getUserGuid())) {
                                bool = true;
                            } else {
                                bool = false;
                            }
                        } else {
                            bool = false;
                        }
                    }
                    if (bool) { // 缓冲存在当前评论的id 表示已经顶过了
                        if (mUser != null) {
                            Toast.makeText(ZhiboJianPLActivity.this, "评论已经赞过了！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (mUser != null) {
                        // 记录用户顶过了的行为
                        boolean isConnect = Assistant
                                .IsContectInterNet2(ZhiboJianPLActivity.this);
                        if (!isConnect) {
                            Toast.makeText(ZhiboJianPLActivity.this, "请检查网络连接！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DigPai dig = new DigPai();
                        dig.setID(pinglunId);
                        dig.setType("图文直播");
                        dig.setDigOrPai("1");
                        dig.setUserGuid(mUser.getUserGUID());
                        if (digPaiDAO.isTableExists()
                                && !digPaiDAO.idExists(pinglunId)) {
                            digPaiDAO.create(dig);
                        }
                        iv_plus1.setVisibility(View.VISIBLE);
                        iv_plus1.setImageResource(R.drawable.plus1);
                        AnimationDrawable animationDrawable = (AnimationDrawable) iv_plus1
                                .getDrawable();
                        AddGoodPoint(ZhiboJianPLActivity.this, pinglunId, new Messenger(
                                ZhiboJianPLActivity.this.handler));
                        animationDrawable.stop();
                        animationDrawable.start();
                        String str = tv_ding.getText().toString();
                        int number = Integer.parseInt(str);
                        tv_ding.setText((number + 1) + "");
                        iv_ding.setImageDrawable(context
                                .getResources()
                                .getDrawable(
                                        R.drawable.biaoliao_dianzan1));
                    } else {
                        Toast.makeText(ZhiboJianPLActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(ZhiboJianPLActivity.this, "直播已结束不能点赞", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private class PingOnClickListener implements OnClickListener {
            private String pid;
            private String name;

            public PingOnClickListener(String pid, String name) {
                this.pid = pid;
                this.name = name;
            }

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mUser == null) {
                    Toast.makeText(ZhiboJianPLActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    ZhiboJianPLActivity.this.startActivity(
                            new Intent(ZhiboJianPLActivity.this, LoginActivity.class));
                    return;
                }


                if ("0".equals(EventStatus) || "1".equals(EventStatus)) {


                    if (name.length() > 8) {
                        name = name.substring(0, 8);
                    }
                    mppvh.ews_luntan_pinglun_shenfen.setText("回复(" + name + ")");
                    ispl = false;
                    mPid = pid;
                    openpwpl();


                } else {
                    Toast.makeText(context, "直播已结束,不能回复", Toast.LENGTH_SHORT).show();
                }

            }

        }

        /**
         * 递归隐藏楼层
         *
         * @param control  //控制变量
         * @param reply    一条评论(包含回复)数据
         * @param len      总楼层数
         * @param floor    楼层
         * @param position 数据列表位置
         * @return
         */
        private LinearLayout hideFloor(int control,
                                       final List<ModelZhiboHuifu> reply, int len, int floor,
                                       final int position) {
            if (len >= 10) {
                LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(
                        context).inflate(R.layout.news_pinglun_add, null);
                if (control == len - 1) {// 倒数第二层 显示 展开隐藏楼层
                    ll_pinglun = (LinearLayout) itemLayout
                            .findViewById(R.id.ll_pinglun);
                    ll_pinglun.setVisibility(View.GONE);
                    tv_develop_hide = (TextView) itemLayout
                            .findViewById(R.id.tv_develop_hide);
                    tv_develop_hide.setVisibility(View.VISIBLE);
                    tv_develop_hide.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Message msg = handler.obtainMessage();
                            msg.obj = position;
                            msg.what = 722;
                            handler.sendMessage(msg);
                        }
                    });
                    control = 3; // 控制器跳到顺数第二层
                    floor = 3; // 当前隐藏楼层的 的楼
                } else {
                    addDate(itemLayout, reply.get(control - 1), floor);
                }
                LinearLayout wrapUpLayout = new LinearLayout(context);
                wrapUpLayout.setOrientation(LinearLayout.VERTICAL);
                wrapUpLayout.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.huifu_item_background));

                wrapUpLayout.setPadding(PADDING, PADDING, PADDING, PADDING);
                // 当前楼层不是嵌套楼层的开始位置就递归楼层 否则结束嵌套循环
                if (control != 1) {
                    wrapUpLayout.addView(hideFloor(--control, reply, len,
                            --floor, position));
                }
                wrapUpLayout.addView(itemLayout);
                return wrapUpLayout;
            } else {
                return noHideFloor(context, reply.size(), reply, true, len, 1);
            }
        }

        // 复制文本
        public void selectdialog(final TextView v) {
            final String[] items = new String[]{"复制"};
            AlertDialog.Builder builder = new AlertDialog.Builder(ZhiboJianPLActivity.this);
            builder.setTitle("提示");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    ClipboardManager cmb = (ClipboardManager) ZhiboJianPLActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(v.getText());
                    Toast.makeText(ZhiboJianPLActivity.this, "已复制到剪贴板!", Toast.LENGTH_SHORT).show();
                }
            });
            // 设置选项
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        /**
         * 处理无隐藏楼层
         *
         * @param context 上下文
         * @param i       控制变量 用于控制嵌套楼层
         * @param reply   数据
         * @param b       用于判断当前是否楼层过高，楼层过高就减少嵌套楼层，加载正常楼层
         * @param len     总楼层数
         * @param index   当楼层过高的时候的开始位置
         * @return
         */
        private LinearLayout noHideFloor(Context context, int i,
                                         List<ModelZhiboHuifu> reply, boolean b, int len, int index) {
            if (len > COMMENT_MAX && b) { // 当楼层过高的时候 嵌套会出现堆栈溢出
                b = false;
                index = len - COMMENT_INDEX; // 嵌套楼层开始位置

            }
            LinearLayout itemLayout = (LinearLayout) LayoutInflater.from(
                    context).inflate(R.layout.news_pinglun_add, null);
            addDate(itemLayout, reply.get(i - 1), i);
            // 当楼层过高并且 当前楼层为嵌套楼层的开始位置
            if (!b && i == index) {
                LinearLayout ll_aad = (LinearLayout) itemLayout
                        .findViewById(R.id.add_linearLayout);
                // 以下循环是加载正常楼层
                for (int j = 1; j < index; j++) {
                    LinearLayout normalComment = (LinearLayout) LayoutInflater
                            .from(context).inflate(R.layout.news_pinglun_add,
                                    null);
                    addDate(normalComment, reply.get(j - 1), j); // 填充数据
                    normalComment.setBackgroundDrawable(context
                            .getResources().getDrawable(
                                    R.drawable.huifu_item_background));

                    ll_aad.addView(normalComment);
                }
            }
            // /每个嵌套条目的外层
            LinearLayout wrapUpLayout = new LinearLayout(context);
            wrapUpLayout.setOrientation(LinearLayout.VERTICAL);
            wrapUpLayout.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.huifu_item_background));

            // 正常楼层如果setPadding的话会撑大正常楼层 导致正常楼层和嵌套楼层尺寸不一致
            if (!b && i == index) {
                wrapUpLayout.setPadding(0, 0, 0, 0);
            } else {
                wrapUpLayout.setPadding(PADDING, PADDING, PADDING, PADDING);
            }
            // 当前楼层不是嵌套楼层的开始位置就递归楼层 否则结束嵌套循环
            if (i != index) {
                wrapUpLayout.addView(noHideFloor(context, --i, reply, b,
                        reply.size(), index));
            }
            wrapUpLayout.addView(itemLayout);
            return wrapUpLayout;
        }

        private class ReplyViewHolder {
            /**
             * 用户名
             */
            public TextView userNameView;

            /**
             * 发表时间
             */
            /* public TextView userDateView; */
            /**
             * 楼层
             */
            public TextView userFloorView;
            // 评论ID
            public TextView idView;

            /**
             * 评论的TextView
             */
            public TextView replyContentView;
        }

        /**
         * 后期封装数据
         *
         * @param layout 需要填充数据的布局
         * @param floor  楼层
         */
        private void addDate(LinearLayout layout, ModelZhiboHuifu npl, int floor) {
            // 获得显示用户名、楼层数、用户评论内容的TextView
            final ReplyViewHolder holder = new ReplyViewHolder();
            // 获得ID,用户名、楼层数、用户评论内容的View
            holder.idView = (TextView) layout.findViewById(R.id.tv_Item_ID);
            holder.userNameView = (TextView) layout
                    .findViewById(R.id.tv_add_user_name);
            holder.userFloorView = (TextView) layout
                    .findViewById(R.id.tv_add_user_floor);
            holder.replyContentView = (TextView) layout
                    .findViewById(R.id.tv_add_reply_content);
            /*
             * holder.userDateView = (TextView)
			 * layout.findViewById(R.id.tv_add_user_date);
			 */

            holder.idView.setText(DecodeStringUtil.DecodeBase64ToUTF8(npl
                    .getID()));// 评论id
            String userName = DecodeStringUtil.DecodeBase64ToUTF8(npl
                    .getUserName());
            CommentUtils.setCommentUserName(holder.userNameView,
                    DecodeStringUtil.DecodeBase64ToUTF8(npl.getUserNickName()),
                    userName);
            holder.userFloorView.setText(floor + "");// 楼层
			/*
			 * holder.userDateView.setText(npl.getAddTime());//时间
			 */
            holder.replyContentView.setText(DecodeStringUtil
                    .DecodeBase64ToUTF8(npl.getCommentContent()));// 内容
            layout.setFocusable(false);
            layout.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    selectdialog((TextView) holder.replyContentView);
                    return false;
                }
            });
        }
    }
}
