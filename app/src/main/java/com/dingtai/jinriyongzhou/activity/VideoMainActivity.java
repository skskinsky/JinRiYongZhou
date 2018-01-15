package com.dingtai.jinriyongzhou.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.api.UsercenterAPI;
import com.dingtai.base.model.DigPai;
import com.dingtai.base.model.UserCollects;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.userscore.ShowJiFen;
import com.dingtai.base.userscore.UserScoreConstant;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.DateUtil;
import com.dingtai.base.utils.HttpUtils;
import com.dingtai.base.utils.RemoteImgGetAndSave2;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.XListView;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.model.MediaComment;
import com.dingtai.jinriyongzhou.model.MediaInfo;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.service.NewsHttpService;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static com.dingtai.jinriyongzhou.R.id.lv_video_comment_content;


public class VideoMainActivity extends BaseActivity implements OnClickListener,
        XListView.IXListViewListener, SurfaceHolder.Callback, View.OnTouchListener {
    private boolean isShouCang = false;// 是否收藏
    private TabHost tabHost;
    private RadioGroup radiogroup;
    private RadioButton radiobutton_detail, radiobutton_pinglun;
    private ImageButton iv_video_new_back;
    private ImageView ding_image, cai_image;
    private ImageView iv_video_pinglun;
    private TextView txt_zancount, zan_and_one, cai_and_one, cai_text;
    private Animation animation;
    private RemoteImgGetAndSave2 rgas;
    private LinearLayout ll_loadingbar;// 加载布局
    private ImageView iv_loadingbar;// 加载动画
    private ScrollView sl_video_details_content;// 内容界面
    private LinearLayout ll_video_details_content;
    private TextView tv_video_details_name;// 视频标题
    private TextView tv_video_details_type;// 视频类别
    private TextView tv_video_details_date;// 视频时间
    private TextView tv_video_details_introduce;// 剧情介绍
    private LinearLayout ll_reload;// 重新加载
    private LinearLayout ll_pretreatment;// 视频进度界面
    private SurfaceView mSurfaceView;// 视频play
    private ImageView iv_comment_loadingbar;// 评论加载动画
    private TextView tv_no_video_comment;// 没有评论界面
    private LinearLayout ll_comment_loadingbar;// 评论界面
    private LinearLayout ll_comment_reload;// 重新加载评论
    private RelativeLayout rl_zan;
    private ImageView fx;
    private ImageView startErr;
    private String DigCount;
    private String CaiCount;
    private String ID2;
    private Map<String, Object> videoDetails;// 视频详情数据集合
    private List<Map<String, String>> comments;// 评论数据集合
    private boolean isState;// 是否为全屏状态
    // 视频详情接口
    private String getVideoDetailUrl = "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=getvideoinfobyid&videoid=";
    // 视频评论接口
    private String commentsUrl;
    private String Detailresult = null;
    private int position;// 播放时间记录
    private int mark = 1;// 评论加载类型 1-第一次加载 2-下拉刷新 3-加载更多
    private List<Map<String, String>> tempCommments;
    private XListView lv_video_comment;
    private static boolean refresh = false;
    private CommentAdapter adapter;
    private ProgressDialog pdialog;
    private MyPopPlViewHolder mppvh;
    private String userName;
    private SharedPreferences sp;
    private PopupWindow pwpl;
    private ImageView btn_sc;
    private String Title = null;
    private LinearLayout ll_functions;
    private RelativeLayout rl_comment_title;
    private boolean isplaycomplement = false;
    private static boolean isInitVedio; // 判断是否视频解析完成
    private String ID = "834";// intent传递过来的ID
    private TextView title_title;
    private int CommentCount = 0;
    // 是否关闭评论 false为关闭
    private boolean CommentIsApproved = true;
    // 添加评论接口
    private String addPLUrl = "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=addcomment&ID2=";
    SurfaceHolder holder;
    private UserInfoModel user;
    private RuntimeExceptionDao<UserCollects, String> collects;// 用户收藏
    private RuntimeExceptionDao<DigPai, String> digPai;
    private float mBrightness = -0.1f;
    private float downX;
    private float downY;
    private boolean isVol;
    private float moveX;
    private float moveY;
    private AudioManager aManager;
    private int yinliang;
    private int volume;
    private RelativeLayout l_yinliang;
    private RelativeLayout l_liangdu;
    private ImageView img_yinliang;
    private TextView t_yinliang;
    private TextView t_liangdu;
    protected int playerWidth, playerHeight;
    private RelativeLayout video_rela_tab1;
    private RelativeLayout video_rela_tab2;
    private IjkMediaPlayer mMediaPlayer;
    private Handler myhandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10001:
                    btn_sc.setImageResource(R.drawable.dt_standard_collect_star);
                    break;
                case 10002:
                    btn_sc.setImageResource(R.drawable.vedia_detail_shoucang);
                    break;
                case 01:// 赞
                    zan_and_one.setVisibility(View.GONE);
                    txt_zancount.setVisibility(View.VISIBLE);
                    if (String.valueOf(DigCount).equals("")) {
                        txt_zancount.setText(1 + "");
                    }
                    txt_zancount.setText(Integer.parseInt(DigCount) + 1 + "");
                    break;
                case 02:// 踩
                    cai_and_one.setVisibility(View.GONE);
                    cai_text.setVisibility(View.VISIBLE);
                    if (String.valueOf(CaiCount).equals("")) {
                        cai_text.setText(1 + "");
                    }
                    cai_text.setText(Integer.parseInt(CaiCount) + 1 + "");
                    break;
                case 3333:
                    // 赞成功
                    ding_image.setClickable(true);
                    String count1 = (String) msg.obj;
                    // txt_zancount.setText(count1+"");
                    zan_and_one.setVisibility(View.VISIBLE);
                    zan_and_one.setAnimation(animation);
                    myhandler.sendEmptyMessageDelayed(01, 1000);
                    Toast.makeText(getApplicationContext(), "赞成功", Toast.LENGTH_SHORT).show();

                    if (Assistant.getUserInfoByOrm(VideoMainActivity.this) != null
                            && UserScoreConstant.ScoreMap
                            .containsKey(UserScoreConstant.SCORE_DIG_PAI)) {
                        new ShowJiFen(VideoMainActivity.this,
                                UserScoreConstant.SCORE_DIG_PAI,
                                UserScoreConstant.SCORE_TYPE_ADD,
                                UserScoreConstant.ExchangeID,
                                Assistant.getUserInfoByOrm(VideoMainActivity.this),
                                "");
                    }

                    break;

                case 3334:

                    // 踩成功
                    cai_image.setClickable(true);
                    String count2 = (String) msg.obj;
                    // txt_zancount.setText(count1+"");
                    cai_and_one.setVisibility(View.VISIBLE);
                    cai_and_one.setAnimation(animation);
                    myhandler.sendEmptyMessageDelayed(02, 1000);
                    Toast.makeText(getApplicationContext(), "踩成功", Toast.LENGTH_SHORT).show();
                    if (Assistant.getUserInfoByOrm(VideoMainActivity.this) != null
                            && UserScoreConstant.ScoreMap
                            .containsKey(UserScoreConstant.SCORE_DIG_PAI)) {
                        new ShowJiFen(VideoMainActivity.this,
                                UserScoreConstant.SCORE_DIG_PAI,
                                UserScoreConstant.SCORE_TYPE_ADD,
                                UserScoreConstant.ExchangeID,
                                Assistant.getUserInfoByOrm(VideoMainActivity.this),
                                "");
                    }

                    break;

                case 33330:
                    rl_zan.setClickable(true);
                    Toast.makeText(getApplicationContext(), "10分钟内不可再顶此视频.",
                            Toast.LENGTH_LONG).show();
                    break;
                case 4444:
                    // 赞失败
                    rl_zan.setClickable(true);
                    Toast.makeText(getApplicationContext(), "赞失败", Toast.LENGTH_SHORT).show();
                    break;
                case 5555:
                    // 踩失败
                    rl_zan.setClickable(true);
                    Toast.makeText(getApplicationContext(), "踩失败", Toast.LENGTH_SHORT).show();
                    break;
                case 000:
                    Toast.makeText(getApplicationContext(), "网络连接失败,请检查网络设置", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 555:
                    Toast.makeText(getApplicationContext(), "获取数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 111:
                    if (videoDetails.get("CommentIsApproved").toString()
                            .equals("true")) {
                        CommentIsApproved = true;
                    } else if (videoDetails.get("CommentIsApproved").toString()
                            .equals("false")) {
                        CommentIsApproved = false;
                    }
                    DigCount = videoDetails.get("DigCount").toString();
                    if (DigCount.equals("0") || DigCount.equals("")) {
                        txt_zancount.setVisibility(View.INVISIBLE);
                    } else {
                        txt_zancount.setText(DigCount.trim());
                    }

                    CaiCount = videoDetails.get("PaiCount").toString();

                    if (CaiCount.equals("0") || CaiCount.equals("")) {
                        cai_text.setVisibility(View.INVISIBLE);
                    } else {
                        cai_text.setText(CaiCount.trim());
                    }

                    CommentCount = Integer.parseInt(videoDetails
                            .get("CommentCount").toString());
                    if (CommentCount > 0) {
                        radiobutton_pinglun.setText("评论");
                    }

                    // 判断是否收藏
                    if (userName != null && !userName.equals("")) {

                        IsCollect();
                    }

                    clearAnim();// 停止视频加载动画
                    loadVideoDetails();// 设置视频详情信息
                    // bool = videoDetails.get("SCbool");
                    // share();// 初始化分享
                    // 隐藏加载提示 显示视频详情信息
                    ll_loadingbar.setVisibility(View.GONE);
                    sl_video_details_content.setVisibility(View.VISIBLE);

				/*
                 * 代码修改 WIFIINFO.xml里面的数据 <int name="WIFISTATE" value="1" />
				 */
                    SharedPreferences wifisp = getSharedPreferences("WIFIINFO",
                            Context.MODE_PRIVATE);
                    String wifiState = Integer.toString(wifisp.getInt("WIFISTATE",
                            1));
                    if ("2".equals(wifiState)) {

                        if (Assistant.IsContectInterNet2(getApplicationContext())) {
                        } else {
                            ll_pretreatment.setVisibility(View.GONE);
                        }
                    } else if ("1".equals(wifiState)) {
                        if (Assistant.IsContectInterNet2(getApplicationContext())) {
                        } else {
                            myhandler.sendEmptyMessage(000);
                        }
                    }

                    ID2 = (String) videoDetails.get("ID2");
                    if (CommentIsApproved) {

                        getVideoComments(0, mark);
                    }
                    break;
                case 9090:// 播放出错
                    ll_pretreatment.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "打开视频失败!", Toast.LENGTH_SHORT).show();
                    startErr.setVisibility(View.VISIBLE);
                    // 保存到播放历史
                    saveHistoryVideo();
                    break;
                case 100:// 视频预处理完成
                    // Log.i("www", "245 case 100");
                    startErr.setVisibility(View.GONE);
                    ll_pretreatment.setVisibility(View.GONE);
                    break;
                case 21:
                    Toast.makeText(getApplicationContext(), "评论失败!", Toast.LENGTH_SHORT).show();
                    if (pwpl != null) {
                        pwpl.dismiss();
                    }
                    pdialog.dismiss();
                    closeKeyboard();
                    break;
                case 23:
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("login");
                    startActivity(intent);
                    break;
                case 24:
                    // 视频无评论
                    lv_video_comment.setVisibility(View.GONE);
                    tv_no_video_comment.setVisibility(View.VISIBLE);
                    ll_comment_loadingbar.setVisibility(View.GONE);
                    ll_comment_reload.setVisibility(View.GONE);
                    refresh = false;
                    mark = 1;
                    onLoad();
                    break;
                case 25:
                    // 视频评论加载完成
                    clearAnim2();
                    if (mark == 1 || mark == 3) {
                        adapter = new CommentAdapter(getApplicationContext(),
                                comments);
                        lv_video_comment.setAdapter(adapter);
                    } else if (mark == 2) {
                        if (comments != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                    ll_comment_reload.setVisibility(View.GONE);
                    ll_comment_loadingbar.setVisibility(View.GONE);
                    tv_no_video_comment.setVisibility(View.GONE);
                    refresh = false;
                    mark = 1;
                    onLoad();
                    break;
                case 26:
                    // 视频评论加载失败
                    tv_no_video_comment.setVisibility(View.GONE);
                    ll_comment_reload.setVisibility(View.VISIBLE);
                    ll_comment_loadingbar.setVisibility(View.GONE);
                    refresh = false;
                    mark = 1;
                    onLoad();
                    break;
                case 27:// 上啦没有更多的数据了
                    lv_video_comment.stopLoadMore();
                    Toast.makeText(getApplicationContext(), "没有更多的数据了!", Toast.LENGTH_SHORT).show();
                    refresh = false;
                    mark = 1;
                    break;
                case 777:

                    btn_sc.setImageResource(R.drawable.dt_standard_collect_star);
                    // new ShowJiFen(getApplicationContext(), "6",sp);
                    Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                    break;
                case 666:
                    Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    break;
                case 888:
                    Toast.makeText(getApplicationContext(), "收藏失败", Toast.LENGTH_SHORT).show();
                case 999:
                    Toast.makeText(getApplicationContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                    btn_sc.setImageResource(R.drawable.vedia_detail_shoucang);
                    break;
                case 121:
                    Toast.makeText(getApplicationContext(), "获取评论数据失败!", Toast.LENGTH_SHORT).show();
                    break;
                case 787878:
                    // 回复视频积分操作
                    // new ShowJiFen(getApplicationContext(), "10", sp);
                    Toast.makeText(getApplicationContext(), "评论成功!", Toast.LENGTH_SHORT).show();
                    CommentCount++;
                    radiobutton_pinglun.setText("评论(" + CommentCount + ")");

                    if (pdialog != null && pdialog.isShowing()) {
                        pdialog.dismiss();
                    }
                    if (pwpl != null) {
                        pwpl.dismiss();
                    }
                    mppvh.plpop_edit_content.setText("");
                    clearAnim2();
                    lv_video_comment.setVisibility(View.VISIBLE);
                    CommentAdapter adapter = new CommentAdapter(
                            getApplicationContext(), tempCommments);
                    lv_video_comment.setAdapter(adapter);
                    // adapter.notifyDataSetChanged();
                    ll_comment_reload.setVisibility(View.GONE);
                    ll_comment_loadingbar.setVisibility(View.GONE);
                    tv_no_video_comment.setVisibility(View.GONE);

                    break;
                case 11:
                    if (pdialog != null) {
                        if (pdialog.isShowing()) {
                            pdialog.dismiss();
                            Toast.makeText(VideoMainActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    break;

                default:
                    break;
            }
        }

    };

    // 保存播放历史记录 视频播放失败 视频没加载完成 时调用此方法
    public void saveHistoryVideo() {
        // if(videoDetails != null){
        // HistoryDetailDao ehDao = new
        // HistoryDetailDao(VideoMainActivity.this);
        // HistoryDetailBeen ebeen = new HistoryDetailBeen();
        // ebeen.setTime("00:00");
        // ebeen.setImgUrl(videoDetails.get("ImageUrl").toString());
        // ebeen.setTitle(videoDetails.get("Name").toString());
        // ebeen.setPackID(videoDetails.get("ID").toString());
        // ehDao.add(ebeen);
        // }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.video_main);
        // ID=getIntent().getStringExtra("ID");
        videoDetails = new HashMap<String, Object>();
        comments = new ArrayList<Map<String, String>>();
        if (getIntent() != null) {
            ID = getIntent().getStringExtra("ID");
            Title = getIntent().getStringExtra("Title");
        }

        user = Assistant.getUserInfoByOrm(this);
        if (user != null) {
            userName = user.getUserName();
        }

        if (getIntent() != null
                && getIntent().getStringExtra("Position") != null) {
            position = Integer.parseInt(getIntent().getStringExtra("Position"));
        }
        title_title = (TextView) findViewById(R.id.title_title);

        if (Title != null && Title.length() > 10) {
            title_title.setText(Title.substring(0, 10));
        } else if (Title != null && Title.length() > 5) {
            title_title.setText(Title.substring(0, 5));
        } else {
            title_title.setText(Title);
        }
        rgas = new RemoteImgGetAndSave2(VideoMainActivity.this);
        initWedget();
        sp = getSharedPreferences("USERINFO", 0);
        initTab();
        initVideoDetails();// 初始化视频详情
        initVideoComment();// 初始化视频评论
        getVideoDetails();// 拿到详情数据

        View popplview = getLayoutInflater().inflate(
                R.layout.pinglun_popupwindow, null);
        initpwpl(popplview, 1);
        pwpl = new PopupWindow(popplview, ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pwpl.setFocusable(true);
        pwpl.setBackgroundDrawable(new BitmapDrawable());
        iv_video_pinglun.setOnClickListener(this);
        adapter = new CommentAdapter(getApplicationContext(), comments);
        ding_image.setOnClickListener(this);
        cai_image.setOnClickListener(this);
        btn_sc.setOnClickListener(this);
        iv_video_new_back.setOnClickListener(this);

        if (getIntent() != null && getIntent().hasExtra("ID2")) {
            ID2 = getIntent().getStringExtra("ID2");
        }

        collects = getHelper().getMode(UserCollects.class);
        digPai = getHelper().getMode(DigPai.class);

        boolean f = false;
        if (collects.isTableExists() && user != null) {
            List<UserCollects> forEq = collects.queryForEq("UserGUID",
                    user.getUserGUID());
            for (UserCollects u : forEq) {
                if (u.getCollectID().equals(ID2)) {
                    // btn_sc.setBackgroundResource(R.drawable.collect_01);
                    btn_sc.setImageResource(R.drawable.dt_standard_collect_star);
                    f = true;
                    break;
                }
            }
            if (!f) {
                // btn_sc.setBackgroundResource(R.drawable.vedia_detail_shoucang);
                btn_sc.setImageResource(R.drawable.dt_standard_news_newsdetail_collectbtnimg);
            }
        }

        if (Assistant.getUserInfoByOrm(VideoMainActivity.this) != null
                && UserScoreConstant.ScoreMap
                .containsKey(UserScoreConstant.SCORE_LOOK_VIDEO)) {
            new ShowJiFen(VideoMainActivity.this,
                    UserScoreConstant.SCORE_LOOK_VIDEO,
                    UserScoreConstant.SCORE_TYPE_ADD,
                    UserScoreConstant.ExchangeID,
                    Assistant.getUserInfoByOrm(VideoMainActivity.this),
                    "");
        }
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
    }

    // 初始化评论里的控件(泡泡窗口。写评论)
    private void initpwpl(View v, int i) {
        Log.i("videomainactivity", "初始化控件!");
        mppvh = new MyPopPlViewHolder();
        mppvh.huifu_ll = (LinearLayout) v.findViewById(R.id.huifu_ll);
        mppvh.plpop_btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
        mppvh.plpop_btn_submit = (Button) v.findViewById(R.id.btn_ok);
        mppvh.ews_luntan_pinglun_shenfen = (TextView) v
                .findViewById(R.id.news_luntan_pinglun_shenfen);
        mppvh.plpop_edit_content = (EditText) v
                .findViewById(R.id.et_edit_content);
        pdialog = new ProgressDialog(this);
        mppvh.plpop_btn_cancel.setOnClickListener(new MyPLListener());
        mppvh.plpop_btn_submit.setOnClickListener(new MyPLListener());
        mppvh.huifu_ll.setOnClickListener(new MyPLListener());

        mppvh.ews_luntan_pinglun_shenfen.setText("评 论");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initVedio();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBrightness = getWindow().getAttributes().screenBrightness;
                downX = event.getRawX();
                downY = event.getRawY();
                moveY = 0;
                if (downX > playerWidth / 2) {
                    volume = aManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
                    isVol = true;
                } else {
                    isVol = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getRawX() - downX;
                moveY = event.getRawY() - downY;
                if (isVol && Math.abs(moveX) < 50 && Math.abs(moveY) > 10) {
                    if (l_yinliang.getVisibility() == View.GONE) {
                        l_yinliang.setVisibility(View.VISIBLE);
                    }
                    if (moveY < 0) {
                        volume++;
                        img_yinliang.setImageDrawable(getResources().getDrawable(
                                R.drawable.dt_standard_palys_sound));
                    } else {
                        volume--;
                    }
                    int percentage = (volume * 100) / yinliang;
                    if (volume <= 0) {
                        percentage = 0;
                        img_yinliang.setImageDrawable(getResources().getDrawable(
                                R.drawable.dt_standard_palys_mute));
                    } else if (volume > yinliang) {
                        percentage = 100;
                    }
                    t_yinliang.setText(percentage + "%");
                    aManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
                } else if (!isVol && Math.abs(moveX) < 50 && Math.abs(moveY) > 10) {
                    if (l_liangdu.getVisibility() == View.GONE) {
                        l_liangdu.setVisibility(View.VISIBLE);
                    }
                    if (mBrightness < 0) {
                        if (mBrightness <= 0.00f)
                            mBrightness = 0.50f;
                        if (mBrightness < 0.01f)
                            mBrightness = 0.01f;
                    }
                    WindowManager.LayoutParams lpa = getWindow().getAttributes();
                    lpa.screenBrightness = mBrightness - moveY / playerHeight;
                    if (lpa.screenBrightness > 1.0f)
                        lpa.screenBrightness = 1.0f;
                    else if (lpa.screenBrightness < 0.01f)
                        lpa.screenBrightness = 0.01f;
                    getWindow().setAttributes(lpa);
                    t_liangdu.setText((int) (lpa.screenBrightness * 100) + "%");
                }
                moveY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                // if (Math.abs(moveY) < 10) {
                mediaController.setVisibility(View.VISIBLE);
                l_yinliang.setVisibility(View.GONE);
                l_liangdu.setVisibility(View.GONE);
                sb.requestFocus();
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, 3000);
                // break;
                // }
                if (isVol) {
                    l_yinliang.setVisibility(View.GONE);
                } else {
                    l_liangdu.setVisibility(View.GONE);
                }
                isVol = false;
                break;

            default:
                break;
        }
        return true;
    }


    private final class MyPopPlViewHolder {
        public Button plpop_btn_cancel = null;
        public Button plpop_btn_submit = null;
        public EditText plpop_edit_content = null;
        public TextView ews_luntan_pinglun_shenfen = null;
        public LinearLayout huifu_ll = null;
    }

    /**
     * 评论的事件处理
     */
    private class MyPLListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.btn_ok) {// View vr = getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                if (CommentIsApproved) {
                    if (userName == null || userName.equals("")) {
                        myhandler.sendEmptyMessage(23);
                        return;
                    }
                    if (videoDetails.get("Name") == null
                            || "".equals(videoDetails.get("Name"))) {
                        Toast.makeText(getApplicationContext(),
                                "请等待数据加载完毕再评论!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pdialog.setMessage("正在提交评论...");
                    pdialog.show();

                    // 拿到评论内容并进行处理
                    String strtext = mppvh.plpop_edit_content.getText()
                            .toString();
                    if (strtext == null || "".equals(strtext.trim())) {
                        // 评论内容为空
                        myhandler.sendEmptyMessage(11);

                    } else {
                        if (Assistant
                                .IsContectInterNet2(getApplicationContext())) {

                            String Econ = "";
                            try {
                                Econ = URLEncoder.encode(strtext,
                                        "UTF-8"); // 拿到内容
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            add_comments(Econ, videoDetails.get("ID2")
                                    .toString());
                            // String url = getAddPLUrl(strtext);
                            // String strJson =
                            // NetWorkTool.GetJsonStrByURL(url);
                            // if (strJson != null && strJson.equals("true")) {
                            // RefreshCommentDataAfterPinlun();
                            //
                            // } else {
                            // // 评论失败
                            // myhandler.sendEmptyMessage(21);
                            // }

                        } else {
                            // 网络
                            myhandler.sendEmptyMessage(000);
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "系统目前关闭评论功能",
                            Toast.LENGTH_LONG).show();
                }


            } else if (i == R.id.huifu_ll) {
                pwpl.dismiss();
                closeKeyboard();

            } else if (i == R.id.btn_cancel) {
                pwpl.dismiss();
                closeKeyboard();

            } else {
            }

        }

    }

    /**
     * 评论完毕后 刷新评论数据
     */
    private void RefreshCommentDataAfterPinlun() {
        new Thread(new Runnable() {
            private Map<String, String> videoComment;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    String commentsUrl = "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=getvideocommentxiala"
                            + "&ID2="
                            + ID2
                            + "&newTime=2012-03-12&GetNum=10&forapp=1";
                    // String strJson =
                    // NetWorkTool.GetJsonStrByURL(commentsUrl);
                    String strJson = "";
                    if (strJson != null && !strJson.equals("")) {
                        tempCommments = new ArrayList<Map<String, String>>();
                        JSONArray jarr = new JSONArray(strJson);
                        for (int i = 0; i < jarr.length(); i++) {
                            videoComment = new HashMap<String, String>();
                            JSONObject iobj = jarr.getJSONObject(i);

                            String id = iobj.getString("ID");
                            String userName = iobj.getString("UserName");
                            String context = iobj.getString("Context");
                            String creationDate = iobj
                                    .getString("CreationDate");
                            String UserIcon = iobj.getString("UserIcon");
                            videoComment.put("ID", id);
                            videoComment.put("UserName", userName);
                            videoComment.put("Context", context);
                            videoComment.put("CreationDate",
                                    DateUtil.formatDate(creationDate));
                            videoComment.put("UserIcon", UserIcon);
                            tempCommments.add(videoComment);
                            // 刷新评论列表
                        }
                        comments.clear();
                        comments = tempCommments;
                        myhandler.sendEmptyMessage(787878);
                    } else {
                        // 获取评论数据失败
                        myhandler.sendEmptyMessage(21);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    myhandler.sendEmptyMessage(121);
                }
            }
        }).start();

    }

    /**
     * 添加评论
     */
    public void add_comments(String commentContent, String MID) {

        if (user != null) {
            insert_video_comments(this, API.VIDEO_COMMENTS_INSERT_URL,
                    commentContent, user.getUserGUID(), MID, new Messenger(
                            handler));
        } else {
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 插入视频评论
     *
     * @param paramContext
     * @param url
     * @param commentContent 评论内容
     * @param userGUID       用户guid
     * @param MID            视频GUID
     * @param paramMessenger
     */
    public void insert_video_comments(Context paramContext, String url,
                                      String commentContent, String userGUID, String MID,
                                      Messenger paramMessenger) {
        Intent localIntent = new Intent(paramContext, IndexHttpService.class);
        localIntent.putExtra("api", API.VIDEO_INSERT_COMMENTS_API);
        localIntent.putExtra(API.VIDEO_INSERT_COMMENTS_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("commentContent", commentContent);
        localIntent.putExtra("userGUID", userGUID);
        localIntent.putExtra("MID", MID);
        paramContext.startService(localIntent);
    }

    // 获取添加视频评论接口
    private String getAddPLUrl(String strtext) {
        // 添加评论 userName 用户名 createDate 添加时间 id2 视频id title标题 Context 内容
        // http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=addcomment&
        // ID2=27CDA49A-11D6-4E0D-A39C-EA2130155C4B
        // &username= &CreateDate= &Context= &Title= &forapp=1
        String url = null;
        try {
            url = addPLUrl
                    + videoDetails.get("ID2").toString()
                    + "&username="
                    + URLEncoder.encode(userName, "UTF-8")
                    + "&CreateDate="
                    + URLEncoder.encode(getCurrentDate(new Date()), "UTF-8")
                    + "&Context="
                    + URLEncoder.encode(strtext, "UTF-8")
                    + "&Title="
                    + URLEncoder.encode(videoDetails.get("Name").toString(),
                    "UTF-8") + "&forapp=1";
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return url;
    }

    // 当前日期
    private String getCurrentDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.out.println(format.format(date));
        return format.format(date);
    }

    /**
     * 关闭虚拟键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 初始化视频
     */
    private void initVedio() {
        // Log.i("www", "624 initVedio()");
        mMediaPlayer = new IjkMediaPlayer();
        mMediaPlayer.setDisplay(holder);
        try {
            mMediaPlayer.setDataSource(getIntent().getStringExtra("url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Log.i("www","629 mMediaPlayer.start()");
        isInitVedio = false;
        mMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {

                String position_time = videoDetails.get("FileSize").toString()
                        .split(" ")[1];
                position = getposition(position_time);
                Log.i("通知", "播放完成");
                // Log.i("www", "onCompletion 播放完成");

                isplaycomplement = true;
                Log.i("position", stringForTime(position));
            }

        });
        mMediaPlayer.setOnErrorListener(new IMediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.i("通知", "播放中出现错误");
                myhandler.sendEmptyMessage(9090);
                // Log.i("www", "setOnErrorListener");
                return false;
            }
        });
        mMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                isInitVedio = true;
                myhandler.sendEmptyMessage(100);
                tv_total.setText(getTimeFromInt(mMediaPlayer.getDuration()));
                sb.setMax((int) mMediaPlayer.getDuration());
                handler.removeMessages(2);
                handler.sendEmptyMessageDelayed(2, 1000);
            }
        });
        mMediaPlayer.prepareAsync();
    }

    ;

    private int getposition(String Strtime) {
        String[] Str = Strtime.split(" ");
        Strtime = Str[Str.length - 1];
        String[] time = Strtime.split(":");
        // 毫秒数
        int militimes = 0;
        if (time.length == 2) {
            militimes = Integer.parseInt(time[0]) * 60 * 1000
                    + Integer.parseInt(time[1]) * 1000;
        } else if (time.length == 3) {
            militimes = Integer.parseInt(time[0]) * 60 * 60 * 1000
                    + Integer.parseInt(time[1]) * 60 * 1000
                    + Integer.parseInt(time[2]) * 1000;
        }
        return militimes;
    }

    // 设置视频详情信息
    protected void loadVideoDetails() {
        // 名称
        if (videoDetails.get("Name") == null
                || "".equals(videoDetails.get("Name"))) {
            tv_video_details_name.setText("暂无");
        } else {
            tv_video_details_name.setText((CharSequence) videoDetails
                    .get("Name"));
        }
        // 类型
        if (videoDetails.get("ChannelName") == null
                || "".equals(videoDetails.get("ChannelName"))) {
            tv_video_details_type.setText("暂无");
        } else {
            tv_video_details_type.setText((CharSequence) videoDetails
                    .get("ChannelName"));
        }
        // 时间
        if (videoDetails.get("UploadDate") == null
                || "".equals(videoDetails.get("UploadDate"))) {
            tv_video_details_date.setText("暂无");
        } else {
            tv_video_details_date.setText((CharSequence) videoDetails
                    .get("UploadDate"));
        }
        // 简介
        if (videoDetails.get("Detail") == null
                || "".equals(videoDetails.get("Detail"))) {
            tv_video_details_introduce.setText("暂无");
        } else {
            // tv_video_details_introduce.setText((CharSequence) videoDetails
            // .get("Detail"));
            tv_video_details_introduce.setText(Html
                    .fromHtml((String) videoDetails.get("Detail")));

        }

    }

    // 获取视频详情数据
    private void getVideoDetails() {
        if (Assistant.IsContectInterNet2(getApplicationContext())) {
            getVideoDetail(ID); // 拿到视频详情信息925"946"
            ll_loadingbar.setVisibility(View.VISIBLE);
            sl_video_details_content.setVisibility(View.GONE);
            ll_reload.setVisibility(View.GONE);
        } else {
            ll_loadingbar.setVisibility(View.GONE);
            sl_video_details_content.setVisibility(View.GONE);
            ll_reload.setVisibility(View.VISIBLE);

            ll_comment_loadingbar.setVisibility(View.GONE);
            ll_comment_reload.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "网络连接失败,请检查网络设置", Toast.LENGTH_SHORT).show();
        }

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case API.HANDLER_GETDATA_SUCCESS:
                    ArrayList<MediaInfo> temp_list = (ArrayList<MediaInfo>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    initVideoDate(temp_list);
                    break;
                case API.HANDLER_GETDATA_ERROR:
                    Toast.makeText(VideoMainActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                    break;
                case 2001:
                    ArrayList<MediaComment> temp = (ArrayList<MediaComment>) msg
                            .getData().getParcelableArrayList("list").get(0);
                    initCommentsDate(temp);
                    break;
                case 2002:
                    if (pdialog != null && pdialog.isShowing()) {
                        pdialog.dismiss();
                    }
                    if (pwpl != null) {
                        pwpl.dismiss();
                    }
                    mppvh.plpop_edit_content.setText("");
                    mark = 3;
                    getVideoComments(0, mark);
                    lv_video_comment.setVisibility(View.VISIBLE);
                    CommentAdapter adapter = new CommentAdapter(
                            getApplicationContext(), tempCommments);
                    lv_video_comment.setAdapter(adapter);
                    // adapter.notifyDataSetChanged();
                    ll_comment_reload.setVisibility(View.GONE);
                    ll_comment_loadingbar.setVisibility(View.GONE);
                    tv_no_video_comment.setVisibility(View.GONE);
                    Toast.makeText(VideoMainActivity.this, "评论成功！", Toast.LENGTH_SHORT).show();
                    break;
                case 2000:
                    Toast.makeText(VideoMainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 2003:
                    Toast.makeText(VideoMainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 2005:
                    Toast.makeText(VideoMainActivity.this, "赞成功", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    mediaController.setVisibility(View.GONE);
                    sb.setVisibility(View.GONE);
                    sb.clearFocus();
                    break;
                case 2:
                    tv_current.setText(getTimeFromInt(mMediaPlayer
                            .getCurrentPosition()));
                    sb.setProgress((int) mMediaPlayer.getCurrentPosition());
                    this.sendEmptyMessageDelayed(2, 1000);

                    // Toast.makeText(VideoPlay6.this,
                    // ""+(mMediaPlayer.getCurrentPosition()),
                    // Toast.LENGTH_SHORT).show();
                    break;
            }

        }

    };

    public static String getTimeFromInt(long time) {
        if (time <= 0) {
            return "0:00";
        }
        long secondnd = (time / 1000) / 60;
        long million = (time / 1000) % 60;
        String f = String.valueOf(secondnd);
        String m = million >= 10 ? String.valueOf(million) : "0"
                + String.valueOf(million);
        return f + ":" + m;
    }

    /**
     * 初始化评论数据
     *
     * @param temp
     */
    private void initCommentsDate(ArrayList<MediaComment> temp) {
        // TODO Auto-generated method stub

        boolean b = true;
        if (mark == 3) {
            tempCommments = new ArrayList<Map<String, String>>();
        }
        for (MediaComment m : temp) {
            Map<String, String> videoComment = new HashMap<String, String>();
            if (m.getID().equalsIgnoreCase("-1")) {
                b = false;
                if (mark == 2) {// 当前是上啦刷新 没有数据
                    myhandler.sendEmptyMessage(27);
                } else {
                    Log.i("VideoMainActivity", "无视频评论信息");
                    myhandler.sendEmptyMessage(24);
                }
                break;
            }
            videoComment.put("ID", m.getID());
            videoComment.put("UserName", m.getUserNickName());
            videoComment.put("Context", m.getCommentContent());
            videoComment.put("CreationDate",
                    DateUtil.formatDate(m.getCommentTime()));
            videoComment.put("UserIcon", m.getUserIcon());

            if (mark == 3) {
                // 下拉刷新
                tempCommments.add(videoComment);
            } else {
                comments.add(videoComment);
            }
        }

        if (mark == 3) {
            comments.clear();
            comments.addAll(tempCommments);
        }
        if (b) {
            myhandler.sendEmptyMessage(25);
        }
    }

    /**
     * 初始化video数据
     *
     * @param temp_list
     */
    private void initVideoDate(ArrayList<MediaInfo> temp_list) {

        MediaInfo m = null;
        if (temp_list.size() > 0) {
            m = temp_list.get(0);
        }
        if (m != null) {
            if (videoDetails != null) {
                videoDetails.put("ID", m.getID().toString());
                videoDetails.put("UploadType", m.getUploadType().toString());
                videoDetails.put("Tag", m.getTag().toString());
                videoDetails.put("FlvUrl", m.getFlvUrl().toString());
                videoDetails.put("Detail", m.getDetail().toString());
                videoDetails.put("ID2", m.getID2().toString());
                videoDetails.put("UploadDate", m.getUploadDate().toString());
                videoDetails.put("UserName", m.getUserName().toString());
                videoDetails.put("Name", m.getName().toString());
                videoDetails.put("ImageUrl", m.getImageUrl().toString());
                videoDetails.put("mediourl", m.getMediaUrl().toString());
                videoDetails.put("ResourceType", 2);
                // videoDetails.put("actor",
                // object.get("actor").toString());
                videoDetails.put("CommentCount", m.getDigCount().toString());
                videoDetails.put("DigCount", m.getDigCount().toString());
                videoDetails.put("FileSize", m.getFileSize().toString());
                videoDetails.put("fx", m.getFenxiang().toString());
                videoDetails.put("CommentIsApproved", "");
                videoDetails.put("PaiCount", m.getPaiCount());
                videoDetails.put("ChannelName", m.getChannelName());
                myhandler.sendEmptyMessage(111);
            } else {
                myhandler.sendEmptyMessage(555);
            }
        }
    }

    /**
     * 获得视频详情
     *
     * @param ID
     */
    private void getVideoDetail(String ID) {
        String url = API.VIDEO_DETAIL_URL;
        get_video_detail(this, url, ID, new Messenger(handler));
    }

    /**
     * 获得视频详情
     *
     * @param paramContext
     * @param url
     * @param VideoID        视频ID
     * @param paramMessenger
     */
    public void get_video_detail(Context paramContext, String url,
                                 String VideoID, Messenger paramMessenger) {
        // http://192.168.1.12:809/interface/MediaAPI.ashx?action=GetVideoInfobyId&VideoID=739
        Intent localIntent = new Intent(paramContext, IndexHttpService.class);
        localIntent.putExtra("api", API.VIDEO_DETAIL_API);
        localIntent.putExtra(API.VIDEO_DETAIL_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("VideoID", VideoID);
        paramContext.startService(localIntent);
    }

    /**
     * 获得视频评论数据
     *
     * @param count 已有的评论数
     * @param mark  标记
     */
    private void getVideoComments(final int count, final int mark) {

        if (mark == 1 || mark == 3) {
            String url = API.VIDEO_COMMENTS_XIALA_URL;
            String num = "5";
            String MID = ID2;
            String sign = "1";
            get_video_comments_xiala(this, url, num, MID, sign, new Messenger(
                    handler));
        } else if (mark == 2) {
            String url = API.VIDEO_COMMENTS_SHANGLA_URL;
            String top = "10";
            String dtop = count + "";
            String MID = ID2;
            String sign = "1";
            get_video_comments_shangla(this, url, top, dtop, MID, sign,
                    new Messenger(handler));
        }
    }

    /**
     * 获得视频评论上拉
     *
     * @param paramContext
     * @param url
     * @param top            取数据的条数
     * @param dtop           已取的数据数量
     * @param MID            视频GUID
     * @param sign           签名
     * @param paramMessenger
     */
    public void get_video_comments_shangla(Context paramContext, String url,
                                           String top, String dtop, String MID, String sign,
                                           Messenger paramMessenger) {
        // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=GetMediaCommentListShangla&
        // top=10&dtop=1&MID=209eda29-28b0-4f4d-9d53-37c9765355ca&sign=1
        Intent localIntent = new Intent(paramContext, IndexHttpService.class);
        localIntent.putExtra("api", API.VIDEO_COMMENTS_SHANGLA_API);
        localIntent
                .putExtra(API.VIDEO_COMMENTS_SHANGLA_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("top", top);
        localIntent.putExtra("dtop", dtop);
        localIntent.putExtra("MID", MID);
        localIntent.putExtra("sign", sign);
        paramContext.startService(localIntent);
    }

    /**
     * 获得视频评论下拉
     *
     * @param paramContext
     * @param url
     * @param num            取数据的条数
     * @param MID            视频GUID
     * @param sign           签名
     * @param paramMessenger
     */
    public void get_video_comments_xiala(Context paramContext, String url,
                                         String num, String MID, String sign, Messenger paramMessenger) {
        // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=GetMediaCommentList
        // &num=10&MID=209eda29-28b0-4f4d-9d53-37c9765355ca&sign=1
        Intent localIntent = new Intent(paramContext, IndexHttpService.class);
        localIntent.putExtra("api", API.VIDEO_COMMENTS_XIALA_API);
        localIntent.putExtra(API.VIDEO_COMMENTS_XIALA_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("num", num);
        localIntent.putExtra("MID", MID);
        localIntent.putExtra("sign", sign);
        paramContext.startService(localIntent);
    }
    // /**
    // * 获取评论数据
    // */
    // private void getVideoComment(final int count, final int mark) {
    // new Thread(new Runnable() {
    // private Map<String, String> videoComment;
    //
    // @Override
    // public void run() {
    // try {
    // //http://standard.d5mt.com.cn/interface/MediaCommentAPI.ashx?action=GetMediaCommentList&num=%d&MID=%@&sign=%@
    //
    //
    // if (mark == 1 || mark == 3) {
    // commentsUrl =
    // API.COMMON_URL+"interface/MediaCommentAPI.ashx?action=GetMediaCommentList&num=10&MID="+ID2+"&sign=1";
    //
    // // commentsUrl =
    // "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=getvideocommentxiala"
    // // + "&ID2="
    // // + ID2
    // // + "&newTime=2012-03-12&GetNum=10&forapp=1";
    // } else if (mark == 2) {
    // commentsUrl =
    // API.COMMON_URL+"interface/MediaCommentAPI.ashx?action=GetMediaCommentList&num=10&MID="+ID2+"&sign=1";
    // // commentsUrl =
    // "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=getvideocomment"
    // // + "&ID2="
    // // + ID2
    // // + "&defaultTop=10&AlreadyTop="
    // // + count + "&forapp=1";
    // }
    // boolean b = true;
    // Log.i("commentsUrl", commentsUrl);
    // String strJson = HttpUtils.GetJsonStrByURL(commentsUrl);
    // // String strJson = "";
    // Log.i("初始化评论数据", strJson);
    // //{"MediaComment":
    // //[{"CommentContent":"ZHNhZHNhZHM=","CommentAuditStatus":"MQ==",
    // //"CommentTime":"MjAxNC0xMi0xMiAwODozNTo0OA==","GetGoodPoint":"MA==",
    // //"ID":"Mw==","MID":"MTllMjI5ZGYtOGIwMS00ZTlmLThhY2QtZGY5NmM0ZmUzMjEz",
    // //"UserGUID":"ZTVkOTU5NTktYTEzMS00ZGE0LWJlM2ItYWJlYmIwOTIwNDEx","UserName":"NTU1NTU1",
    // //"UserIcon":""},
    // //{"CommentContent":"ZHNhZHNh","CommentAuditStatus":"MQ==","CommentTime":"MjAxNC0xMi0xMSAxNjo0MToxMQ==","GetGoodPoint":"MA==","ID":"Mg==","MID":"MTllMjI5ZGYtOGIwMS00ZTlmLThhY2QtZGY5NmM0ZmUzMjEz","UserGUID":"ZTVkOTU5NTktYTEzMS00ZGE0LWJlM2ItYWJlYmIwOTIwNDEx","UserName":"NTU1NTU1","UserIcon":""},{"CommentContent":"ZHNhZHNhZHM=","CommentAuditStatus":"MQ==","CommentTime":"MjAxNC0xMi0xMSAxNjo0MTowMQ==","GetGoodPoint":"MA==","ID":"MQ==","MID":"MTllMjI5ZGYtOGIwMS00ZTlmLThhY2QtZGY5NmM0ZmUzMjEz","UserGUID":"ZTVkOTU5NTktYTEzMS00ZGE0LWJlM2ItYWJlYmIwOTIwNDEx","UserName":"NTU1NTU1","UserIcon":""}]}
    //
    // if (strJson != null && !strJson.equals("")) {
    // if (mark == 3) {
    // tempCommments = new ArrayList<Map<String, String>>();
    // }
    //
    // JSONObject ob = new JSONObject(strJson);
    // strJson = ob.getString("MediaComment");
    //
    // JSONArray array = new JSONArray(strJson);
    // for (int i = 0; i < array.length(); i++) {
    // videoComment = new HashMap<String, String>();
    // JSONObject object = array.getJSONObject(i);
    // String ID = object.getString("ID");
    // if (ID.equalsIgnoreCase("-1")) {
    // b = false;
    // if (mark == 2) {// 当前是上啦刷新 没有数据
    // myhandler.sendEmptyMessage(27);
    // } else {
    // Log.i("VideoMainActivity", "无视频评论信息");
    // myhandler.sendEmptyMessage(24);
    // }
    // break;
    // }
    // videoComment.put("ID", object.getString("ID"));
    // videoComment.put("UserName",DecodeStringUtil.DecodeBase64ToUTF8(object.getString("UserName"))
    // );
    // videoComment.put("Context",DecodeStringUtil.DecodeBase64ToUTF8(object.getString("MID"))
    // );
    // videoComment.put("CreationDate",
    // DateUtil.formatDate(DecodeStringUtil.DecodeBase64ToUTF8(object.getString("CommentTime"))));
    // videoComment.put("UserIcon",DecodeStringUtil.DecodeBase64ToUTF8(object.getString("UserIcon"))
    // );
    // if (mark == 3) {
    // // 下拉刷新
    // tempCommments.add(videoComment);
    // } else {
    // comments.add(videoComment);
    // }
    // }
    // if (mark == 3) {
    // comments.clear();
    // comments.addAll(tempCommments);
    // }
    // if (b) {
    // myhandler.sendEmptyMessage(25);
    // }
    // } else {
    // // 暂无评论
    // myhandler.sendEmptyMessage(26);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // myhandler.sendEmptyMessage(26);
    // }
    // }
    // }).start();
    //
    // }

    /**
     * 初始化视频评论
     */
    private void initVideoComment() {
        // 初始化加载动画

        iv_comment_loadingbar = (ImageView) findViewById(R.id.iv_comment_loadingbar);
        tv_no_video_comment = (TextView) findViewById(R.id.tv_no_video_comment);
        Animation anim = initAnim(this);
        if (anim != null) {
            iv_comment_loadingbar.setAnimation(anim);
        }
        ll_comment_loadingbar = (LinearLayout) findViewById(R.id.ll_comment_loadingbar);
        ll_comment_reload = (LinearLayout) findViewById(R.id.ll_comment_reload);
        ll_comment_reload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getVideoDetails();
            }
        });
    }

    /**
     * 初始化视频详情
     */
    private void initVideoDetails() {
        // 初始化加载动画
        Animation anim = initAnim(this);
        if (anim != null) {
            iv_loadingbar.setAnimation(anim);
        }
        ll_reload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 重新获取视频详情信息
                getVideoDetails();
            }
        });
    }

    private void clearAnim() {
        if (iv_loadingbar != null) {
            iv_loadingbar.clearAnimation();
        }
    }

    private void clearAnim2() {
        if (iv_comment_loadingbar != null) {
            iv_comment_loadingbar.clearAnimation();
        }
    }

    // 加载动画
    private Animation initAnim(Context context) {
        Animation operatingAnim = AnimationUtils.loadAnimation(context,
                R.anim.revolve);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        return operatingAnim;
    }

    // 监听返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        // 如果是全屏状态下返回 就设置为横屏 否则就返回首页
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (isState) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fangda.setImageDrawable(getResources().getDrawable(
                        R.drawable.dt_standard_bt_fullscreen));
            } else {
                VideoMainActivity.this.finish();
            }
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("VIDEO", " 现在是横屏");
            isState = true;
            rl_comment_title.setVisibility(View.GONE);
            tabHost.setVisibility(View.GONE);
            ll_functions.setVisibility(View.GONE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i("VIDEO", " 现在是竖屏");
            isState = false;
            rl_comment_title.setVisibility(View.VISIBLE);
            tabHost.setVisibility(View.VISIBLE);
            ll_functions.setVisibility(View.VISIBLE);
            // /mMediaController.show();
        }
    }

    private View mediaController;
    private TextView tv_current, tv_total;
    private SeekBar sb;
    private ImageButton ib_video_play, fangda;

    private void initWedget() {
        l_yinliang = (RelativeLayout) findViewById(R.id.gesture_volume_layout);
        l_liangdu = (RelativeLayout) findViewById(R.id.gesture_bright_layout);
        img_yinliang = (ImageView) findViewById(R.id.gesture_iv_player_volume);
        t_yinliang = (TextView) findViewById(R.id.geture_tv_volume_percentage);
        t_liangdu = (TextView) findViewById(R.id.geture_tv_bright_percentage);
        aManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        yinliang = aManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = aManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        final FrameLayout fl_frame = (FrameLayout) findViewById(R.id.fl_frame);
        findViewById(R.id.fl_frame).setOnTouchListener(this);
        fl_frame.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                // TODO 自动生成的方法存根
                fl_frame.getViewTreeObserver().removeGlobalOnLayoutListener(
                        this);
                playerWidth = fl_frame.getWidth();
                playerHeight = fl_frame.getHeight();
            }
        });
        mediaController = findViewById(R.id.ll_mediacontroller);
        ib_video_play = (ImageButton) findViewById(R.id.ib_video_play);
        ib_video_play.setOnClickListener(this);
        tv_current = (TextView) findViewById(R.id.mediacontroller_time_current);
        tv_total = (TextView) findViewById(R.id.mediacontroller_time_total);
        sb = (SeekBar) findViewById(R.id.fl_video_progress_bar);
        fangda = (ImageButton) findViewById(R.id.ib_video_fullscreen);
        fangda.setOnClickListener(this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        rl_comment_title = (RelativeLayout) findViewById(R.id.rl_comment_title);
        ll_functions = (LinearLayout) findViewById(R.id.ll_functions);
        ll_comment_loadingbar = (LinearLayout) findViewById(R.id.ll_comment_loadingbar);
        tv_no_video_comment = (TextView) findViewById(R.id.tv_no_video_comment);
        iv_comment_loadingbar = (ImageView) findViewById(R.id.iv_comment_loadingbar);
        ll_comment_reload = (LinearLayout) findViewById(R.id.ll_comment_reload);
        ll_pretreatment = (LinearLayout) findViewById(R.id.ll_pretreatment);
        ll_loadingbar = (LinearLayout) findViewById(R.id.ll_loadingbar);
        iv_loadingbar = (ImageView) findViewById(R.id.iv_loadingbar);
        ll_reload = (LinearLayout) findViewById(R.id.ll_reload);
        btn_sc = (ImageView) findViewById(R.id.btn_sc);
        rl_zan = (RelativeLayout) findViewById(R.id.rl_zan);
        fx = (ImageView) findViewById(R.id.fx);
        rl_zan.setOnClickListener(this);
        fx.setOnClickListener(this);
        lv_video_comment = (XListView) findViewById(lv_video_comment_content);
        lv_video_comment.setPullLoadEnable(true);
        lv_video_comment.setPullRefreshEnable(true);
        lv_video_comment.setXListViewListener(this);
        iv_video_pinglun = (ImageView) findViewById(R.id.iv_video_pinglun);
        ll_video_details_content = (LinearLayout) findViewById(R.id.ll_video_details_content);
        sl_video_details_content = (ScrollView) findViewById(R.id.sl_video_details_content);
        tv_video_details_name = (TextView) findViewById(R.id.tv_video_details_name);
        tv_video_details_type = (TextView) findViewById(R.id.tv_video_details_type);
        tv_video_details_date = (TextView) findViewById(R.id.tv_video_details_date);
        tv_video_details_introduce = (TextView) findViewById(R.id.tv_video_details_introduce);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        radiobutton_detail = (RadioButton) findViewById(R.id.rbtn_detail);
        radiobutton_detail.setId(0);
        radiobutton_pinglun = (RadioButton) findViewById(R.id.rbtn_pinglun);
        radiobutton_pinglun.setId(0);
        radiogroup.check(0);
        ding_image = (ImageView) findViewById(R.id.ding_image);
        cai_image = (ImageView) findViewById(R.id.cai_image);
        iv_video_new_back = (ImageButton) findViewById(R.id.iv_video_new_back);
        animation = AnimationUtils.loadAnimation(this, R.anim.zan_animation);
        zan_and_one = (TextView) findViewById(R.id.zan_and_one);
        cai_and_one = (TextView) findViewById(R.id.cai_and_one);
        txt_zancount = (TextView) findViewById(R.id.txt_zancount);
        cai_text = (TextView) findViewById(R.id.cai_text);

        video_rela_tab1 = (RelativeLayout) findViewById(R.id.video_rela_tab1);
        video_rela_tab2 = (RelativeLayout) findViewById(R.id.video_rela_tab2);

        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                int current = tabHost.getCurrentTab();

                if (current == 1) {
                    video_rela_tab1.setVisibility(RelativeLayout.VISIBLE);
                    video_rela_tab2.setVisibility(RelativeLayout.INVISIBLE);
                } else {
                    video_rela_tab2.setVisibility(RelativeLayout.VISIBLE);
                    video_rela_tab1.setVisibility(RelativeLayout.INVISIBLE);
                }

                switch (arg1) {
                    case 0:
                        tabHost.setCurrentTab(arg1);
                        break;
                    case 1:
                        if (CommentIsApproved) {
                            tabHost.setCurrentTab(arg1);
                        } else {
                            tabHost.setCurrentTab(arg1);
                            tv_no_video_comment.setText("系统目前关闭评论功能!");
                            lv_video_comment.setVisibility(View.GONE);
                            tv_no_video_comment.setVisibility(View.VISIBLE);
                            ll_comment_loadingbar.setVisibility(View.GONE);
                            ll_comment_reload.setVisibility(View.GONE);
                            // Toast.makeText(getApplicationContext(),
                            // "系统目前关闭评论功能!", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        startErr = (ImageView) findViewById(R.id.startErr);
        startErr.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                getVideoDetail(ID);
            }
        });
    }

    private void initTab() {
        // TODO Auto-generated method stub

        tabHost.setup();
        TabHost.TabSpec spec1 = tabHost.newTabSpec("tab1");
        spec1.setContent(R.id.rl_video_details_content);
        spec1.setIndicator("",
                getResources().getDrawable(R.drawable.ic_launcher));
        tabHost.addTab(spec1);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("tab2");
        spec2.setContent(R.id.ll_video_comment_content);
        spec2.setIndicator("",
                getResources().getDrawable(R.drawable.ic_launcher));
        tabHost.addTab(spec2);
        tabHost.setCurrentTab(0);
    }

    // 弹出评论登录框
    private void openpwpl(View parent) {
        pwpl.showAtLocation(
                VideoMainActivity.this.findViewById(R.id.ll_video_main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        int i = arg0.getId();
        if (i == R.id.ding_image) {
            if (userName != null && !userName.equals("")) {
                // ding_image.setClickable(false);

                DigPai dp = userIsPaiOrDig();
                if (dp != null) {
                    String str = dp.getDigOrPai();
                    if ("1".equals(str)) {
                        Toast.makeText(VideoMainActivity.this, "已经赞过", Toast.LENGTH_SHORT)
                                .show();
                    } else if ("0".equals(str)) {
                        Toast.makeText(VideoMainActivity.this, "已经踩过不能再赞", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    DigPai p = new DigPai();
                    p.setDigOrPai("1");
                    p.setID(ID);
                    p.setType("视频");
                    if (user != null) {
                        p.setUserGuid(user.getUserGUID());
                    }
                    if (digPai.isTableExists() && !digPai.idExists(ID)) {
                        digPai.create(p);
                    }
                    GetZan(userName);
                }

            } else {
                Intent intent = new Intent();
                intent.setAction("login");
                startActivity(intent);
            }


        } else if (i == R.id.cai_image) {
            if (userName != null && !userName.equals("")) {
                // cai_image.setClickable(false);
                DigPai dp = userIsPaiOrDig();
                if (dp != null) {
                    String str = dp.getDigOrPai();
                    if ("1".equals(str)) {
                        Toast.makeText(VideoMainActivity.this, "已经赞过不能再踩", Toast.LENGTH_SHORT)
                                .show();
                    } else if ("0".equals(str)) {
                        Toast.makeText(VideoMainActivity.this, "已经踩过", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    DigPai p = new DigPai();
                    p.setDigOrPai("0");
                    p.setID(ID);
                    p.setType("视频");
                    if (user != null) {
                        p.setUserGuid(user.getUserGUID());
                    }
                    if (digPai.isTableExists() && !digPai.idExists(ID)) {
                        digPai.create(p);
                    }
                    GetCai(userName);
                }
            } else {
                Intent intent = new Intent();
                intent.setAction("login");
                startActivity(intent);
            }


        } else if (i == R.id.iv_video_pinglun) {
            if (CommentIsApproved) {
                if (userName == null || userName.equals("")) {
                    Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("login");
                    startActivity(intent);
                } else {
                    openpwpl(VideoMainActivity.this
                            .findViewById(R.id.ll_video_main));
                }
            } else {
                Toast.makeText(getApplicationContext(), "系统目前关闭评论功能!",
                        Toast.LENGTH_LONG).show();
            }

        } else if (i == R.id.btn_sc) {
            if (userName != null && !userName.equals("")
                    && videoDetails != null) {

                boolean f = false;
                if (collects.isTableExists()) {
                    List<UserCollects> forEq = collects.queryForEq("UserGUID",
                            user.getUserGUID());
                    for (UserCollects u : forEq) {
                        if (u.getCollectID().equals(ID2)) {
                            // btn_sc.setBackgroundResource(R.drawable.vedia_detail_shoucang);
                            btn_sc.setImageResource(R.drawable.dt_standard_news_newsdetail_collectbtnimg);
//                            del_user_collects(VideoMainActivity.this,
//                                    UsercenterAPI.DEL_COLLECTS_URL, u.getID(),
//                                    new Messenger(shouchang_handler));
                            String url = UsercenterAPI.DEL_COLLECTS_URL + "&ID=" + u.getID();
                            requestData(getApplicationContext(), url, new Messenger(shouchang_handler), UsercenterAPI.DEL_COLLECTS_API, UsercenterAPI.DEL_COLLECTS_MESSAGE, NewsHttpService.class);
                            collects.deleteById(ID2);
                            // Toast.makeText(NewsDetailActivity.this, "已取消收藏！",
                            // 1000).show();
                            f = true;
                        }
                    }
                    if (!f && videoDetails != null
                            && videoDetails.get("ResourceType") != null) {
                        UserCollects collect = new UserCollects();
                        collect.setStid(API.STID);
                        collect.setCollectID(ID2);
                        collect.setUserGUID(user.getUserGUID());
                        collect.setUserName(user.getUserName());
                        collect.setCollectType(videoDetails.get("ResourceType")
                                .toString());
                        if (!collects.idExists(ID2)) {
                            collects.create(collect);
                        }
//                        add_user_collects(VideoMainActivity.this,
//                                UsercenterAPI.ADD_COLLECTS_URL, videoDetails
//                                        .get("ResourceType").toString(),
//                                API.STID, ID2, user.getUserGUID(),
//                                user.getUserName(), new Messenger(
//                                        shouchang_handler));
                        String url = UsercenterAPI.ADD_COLLECTS_URL + "&CollectType=" + videoDetails
                                .get("ResourceType").toString() + "&stid=" + API.STID + "&CollectID=" + ID2 + "&UserGUID=" + user.getUserGUID() + "&userName=" + user.getUserName();
                        requestData(getApplicationContext(), url, new Messenger(shouchang_handler), UsercenterAPI.ADD_COLLECTS_API, UsercenterAPI.ADD_COLLECTS_MESSAGE, NewsHttpService.class);
                        // btn_sc.setBackgroundResource(R.drawable.collect_01);
                        btn_sc.setImageResource(R.drawable.dt_standard_collect_star);
                    }
                }

            } else {
                Toast.makeText(getApplicationContext(), "未登录", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction("login");
                startActivity(intent);
            }

        } else if (i == R.id.fx) {
            if (Assistant.IsContectInterNet2(VideoMainActivity.this)) {
                if (videoDetails == null
                        || (videoDetails != null && videoDetails.size() == 0)) {
                    Toast.makeText(VideoMainActivity.this, "数据正在加载中请稍后", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    showShare();
                }
            } else {
                Toast.makeText(VideoMainActivity.this, "请先连接网络！", Toast.LENGTH_SHORT).show();
            }


        } else if (i == R.id.iv_video_new_back) {// Log.i("www", "isInitvideo "+isInitVedio);
            if (!isInitVedio) {
                // 保存到播放历史里
                saveHistoryVideo();
            }
            finish();

        } else if (i == R.id.ib_video_play) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                // start.setBackgroundResource(R.drawable.dt_standard_palys_zanting);
                ib_video_play.setImageDrawable(getResources().getDrawable(
                        R.drawable.dt_standard_palys_zanting));
            } else {
                mMediaPlayer.start();
                ib_video_play.setImageDrawable(getResources().getDrawable(
                        R.drawable.dt_standard_palys_bofang));
            }

        } else if (i == R.id.ib_video_fullscreen) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                isState = true;
                fangda.setImageDrawable(getResources().getDrawable(
                        R.drawable.dt_standard_bt_no_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                fangda.setImageDrawable(getResources().getDrawable(
                        R.drawable.dt_standard_bt_fullscreen));
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isState = false;
            }

        }
    }

    /*
     * 全屏和非全屏切换
     */
    public void fullScreenChange(boolean isFullScreen) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (!isFullScreen) {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attrs);
            // 取消全屏设置
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private Handler shouchang_handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 10007:
                    Toast.makeText(VideoMainActivity.this, "已取消该收藏！", Toast.LENGTH_SHORT).show();
                    break;
                case API.HANDLER_GETDATA_SUCCESS:
                    ArrayList<String> temp_list = (ArrayList<String>) msg.getData()
                            .getParcelableArrayList("list").get(0);// 获取点击收藏后返回的收藏id
                    String id = "";
                    if (temp_list.size() > 0) {
                        id = temp_list.get(0);
                        UserCollects queryForId = null;
                        if (collects != null && collects.isTableExists()) {
                            queryForId = collects.queryForId(ID2);
                            if (queryForId != null) {
                                queryForId.setID(id);
                                collects.update(queryForId);
                            }
                        }
                    }
                    Toast.makeText(VideoMainActivity.this, "成功收藏！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    /**
     * 判断用户对此视频是否赞过或者踩过
     *
     * @return
     */
    private DigPai userIsPaiOrDig() {
        DigPai dp = null;
        if (digPai.isTableExists()) {
            dp = digPai.queryForId(ID);
        }
        // 如果digpai表存在且有id为ID的记录 但是用户guid对应不一致则说明此时为另外一个用户
        if (dp != null && user != null) {
            String userid = user.getUserGUID();
            if (!userid.equals(dp.getUserGuid())) {
                return null;
            }
        }
        return dp;

    }

    // 按回退键
    @Override
    public void onBackPressed() {
        Log.i("www", "onBackPressed " + isInitVedio);
        if (!isInitVedio) {
            // 保存到播放历史里
            saveHistoryVideo();
        }

        // finish();
    }


    // 赞
    private void GetZan(final String username) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // http://192.168.1.12:809/interface/MediaCommentAPI.ashx?action=DingMediaComment&ID=1
                // String url =
                // "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=GetMediaSustain&mediaId="
                // + ID + "&username=" + username;
                String url = API.VIDEO_DING_URL + "&ID="
                        + videoDetails.get("ID").toString();
                // String result = NetWorkTool.GetJsonStrByURL(url);
                String result = HttpUtils.executeHttpPost(url);
                if (result != null && !result.equals("")) {
                    try {
                        JSONObject job = new JSONObject(result);
                        JSONArray ja = job.getJSONArray("MediaList");
                        JSONObject ob = (JSONObject) ja.get(0);
                        String str = ob.getString("Result");
                        if ("Failed".equals(str)) {
                            myhandler.sendEmptyMessage(4444);
                        } else {
                            Message message = new Message();
                            message.obj = 1;
                            myhandler.sendEmptyMessage(3333);
                        }

                        // JSONArray array = new JSONArray(result);
                        // JSONObject object = array.getJSONObject(0);
                        // String id = object.getString("ID");
                        // String count = object.getString("Count");
                        // Log.i("赞------", result);
                        // if (id != null && !id.equals("-1") &&
                        // !id.equals("-2")) {
                        // Message message = new Message();
                        // message.obj = count;
                        // myhandler.sendEmptyMessage(3333);
                        // } else if (id != null && count.equals("-2")) {
                        // // 10分钟内
                        // myhandler.sendEmptyMessage(33330);
                        // } else {
                        // myhandler.sendEmptyMessage(4444);
                        // }
                    } catch (JSONException e) {
                        myhandler.sendEmptyMessage(4444);
                        e.printStackTrace();
                    }
                } else {
                    // 赞失败
                    myhandler.sendEmptyMessage(4444);
                }
            }
        }).start();

    }

    /**
     * 视频踩
     *
     * @param username
     */
    private void GetCai(final String username) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // http://192.168.1.12:809/interface/MediaAPI.ashx?action=DingMedia&ID=1
                String url = API.VIDEO_CAI_URL + "&ID="
                        + videoDetails.get("ID").toString();
                // String result = NetWorkTool.GetJsonStrByURL(url);
                String result = HttpUtils.executeHttpPost(url);
                if (result != null && !result.equals("")) {
                    try {
                        JSONObject job = new JSONObject(result);
                        JSONArray ja = job.getJSONArray("MediaList");
                        JSONObject ob = (JSONObject) ja.get(0);
                        String str = ob.getString("Result");
                        if ("Failed".equals(str)) {
                            myhandler.sendEmptyMessage(5555);
                        } else {
                            Message message = new Message();
                            message.obj = 1;
                            myhandler.sendEmptyMessage(3334);
                        }

                        // JSONArray array = new JSONArray(result);
                        // JSONObject object = array.getJSONObject(0);
                        // String id = object.getString("ID");
                        // String count = object.getString("Count");
                        // Log.i("赞------", result);
                        // if (id != null && !id.equals("-1") &&
                        // !id.equals("-2")) {
                        // Message message = new Message();
                        // message.obj = count;
                        // myhandler.sendEmptyMessage(3333);
                        // } else if (id != null && count.equals("-2")) {
                        // // 10分钟内
                        // myhandler.sendEmptyMessage(33330);
                        // } else {
                        // myhandler.sendEmptyMessage(4444);
                        // }
                    } catch (JSONException e) {
                        myhandler.sendEmptyMessage(5555);
                        e.printStackTrace();
                    }
                } else {
                    // 踩失败
                    myhandler.sendEmptyMessage(5555);
                }
            }
        }).start();

    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // userName = sp.getString("UserName", "");
        if (userName != null && !userName.equals("")) {

            IsCollect();
        }

        user = Assistant.getUserInfoByOrm(this);
        if (user != null) {
            userName = user.getUserName();
        }

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            Toast.makeText(this, click.toString(), Toast.LENGTH_SHORT).show();
            String customContent = click.getCustomContent();
            // 获取自定义key-value
            if (customContent != null && customContent.length() != 0) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    // key1为前台配置的key
                    if (!obj.isNull("ID")) {
                        ID = obj.getString("ID");
                    }
                    if (!obj.isNull("ID2")) {
                        ID2 = obj.getString("ID2");
                    }
                    if (!obj.isNull("Title")) {
                        Title = obj.getString("Title");
                    }
                    title_title.setText(Title);
                    getVideoDetails();// 拿到详情数据
                    // ...
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void IsCollect() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                String URL = "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=IsExist&mediaId="
                        + videoDetails.get("ID") + "&userName=" + userName;

                // String jsonArrly = NetWorkTool.GetJsonStrByURL(URL);
                String jsonArrly = "";
                if (jsonArrly != null) {
                    JSONArray json;
                    JSONObject js;
                    try {
                        json = new JSONArray(jsonArrly);
                        js = json.getJSONObject(0);
                        if (!js.get("PackID").equals("-1")) {
                            // 已收藏
                            isShouCang = true;
                            myhandler.sendEmptyMessage(10001);

                        } else {
                            // 没有收藏
                            isShouCang = false;
                            myhandler.sendEmptyMessage(10002);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    // myhandler.sendEmptyMessage(888);
                }
            }
        }).start();
    }


    // 取消收藏视频
    private void delateSC() {
        // http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=mediapackadd&mediapackadd=&UserName=&Tag=&Title=&MediaID=&forapp=1

        String URL = "http://weishi-pod.d5mt.com.cn/Interface/Getinfo.ashx?type=GetDelPack&userName="
                + userName + "&mediaId=" + ID;
        Log.i("cytest", "url:" + URL);
        // String jsonArrly = NetWorkTool.GetJsonStrByURL(URL);
        String jsonArrly = "";
        if (jsonArrly != null) {

            try {
                JSONArray array = new JSONArray(jsonArrly);
                JSONObject object = array.getJSONObject(0);
                if ("True".equals(object.getString("id"))) {
                    // 取消
                    isShouCang = false;
                    myhandler.sendEmptyMessage(999);
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                myhandler.sendEmptyMessage(9998);
            }

        } else {

        }

    }

    private class Holder {
        public TextView tv_comment_item_date; // 时间
        public TextView tv_comment_item_nicknamee_and_content; // 用户名及内容
        public CircularImage comment_portrait;
    }

    class CommentAdapter extends BaseAdapter {
        Context context;
        List<Map<String, String>> list;

        public CommentAdapter(Context context, List<Map<String, String>> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (list == null) {
                return null;
            }
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.video_comment_item, null);
                holder.tv_comment_item_date = (TextView) convertView
                        .findViewById(R.id.tv_comment_item_date);
                holder.tv_comment_item_nicknamee_and_content = (TextView) convertView
                        .findViewById(R.id.tv_comment_item_nickname_and_content);
                holder.comment_portrait = (CircularImage) convertView
                        .findViewById(R.id.comment_portrait);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            Map<String, String> comment = list.get(position);
            String nickname = comment.get("UserName");
            if (nickname == null || nickname.equals("")) {
                nickname = userName;
            }
            holder.tv_comment_item_nicknamee_and_content.setText(Html
                    .fromHtml(nickname + "：" + comment.get("Context")));
            holder.tv_comment_item_date.setText(comment.get("CreationDate"));
            String UserIconUrl = comment.get("UserIcon");
            // holder.comment_portrait
            // .setImageDrawable(getResources().getDrawable(R.drawable.myself_portrait2));
            if (UserIconUrl != null) {
                if ("".equals(UserIconUrl)) {
                    holder.comment_portrait.setImageDrawable(getResources()
                            .getDrawable(R.drawable.user_icon));
                } else {
                    try {
                        rgas.DisplayImages2(UserIconUrl,
                                holder.comment_portrait);
                    } catch (Exception e) {
                        // TODO: handle exception
                        holder.comment_portrait.setImageDrawable(getResources()
                                .getDrawable(R.drawable.user_icon));
                    }
                }

            } else {
                holder.comment_portrait.setImageDrawable(getResources()
                        .getDrawable(R.drawable.user_icon));
            }
            return convertView;
        }

    }

    private void onLoad() {
        lv_video_comment.stopRefresh();// 停止刷新,重置标题视图
        lv_video_comment.stopLoadMore();// 停止加载,重置页脚视图。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String FreshDate = sdf.format(new Date());
        lv_video_comment.setRefreshTime(FreshDate); // 最后更新时间
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        if (!refresh) {
            refresh = true;
            mark = 3;// 下拉刷新
            getVideoComments(0, mark);

        }
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        if (!refresh) {
            refresh = true;
            mark = 2;// 上拉加载更多
            getVideoComments(comments.size(), mark);
        }
    }

    // 当activity界面被覆盖
    @Override
    public void onPause() {
        super.onPause();
        // MobclickAgent.onPause(this);
        if (mMediaPlayer != null) {
            if (isplaycomplement) {

            } else {
                position = (int) mMediaPlayer.getCurrentPosition();
            }
            // 当Activity被覆盖时 视频不需要被暂停
            // mMediaPlayer.pause();
        }
        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (pwpl != null) {
            pwpl.dismiss();
        }
        // 销毁视频控件及获取播放的时间
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }

    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(mFormatBuilder,
                Locale.getDefault());

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    // 社会化分享
    private void showShare() {
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if (videoDetails != null) {
            BaseShare share = new BaseShare(this, videoDetails.get("Name")
                    .toString(), "看电视、听广播、读资讯，尽在今日永州！", "http://gd.hh.hn.d5mt.com.cn/Share/MediaShares.aspx?GUID="
                    + videoDetails.get("ID2").toString(), videoDetails.get(
                    "ImageUrl").toString(), "99", "");
            share.oneShare();
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // private FrontiaSocialShare mSocialShare;
    private static boolean boolShare = false;
    // private FrontiaSocialShareContent mImageContent = new
    // FrontiaSocialShareContent();

    // 初始化分享
    // private void share() {
    // mSocialShare = Frontia.getSocialShare();
    // mSocialShare.setContext(this);
    // // wxda37a879c76bc110
    // mSocialShare.setClientId(MediaType.WEIXIN.toString(),
    // "wxf8fc9bcb6ef8626d");
    // mImageContent.setTitle(videoDetails.get("Name").toString());
    // mImageContent.setContent("微视新疆分享：" +videoDetails.get("Detail").toString()
    // );
    // mImageContent.setLinkUrl(videoDetails.get("fx").toString());
    // if
    // (videoDetails.get("ImageUrl")!=null&&!videoDetails.get("ImageUrl").equals(""))
    // {
    //
    // mImageContent.setImageUri(Uri.parse(videoDetails.get("ImageUrl")
    // .toString()));
    // }
    // boolShare = true;
    // }

    // 分享的时间监听
    // private class ShareListener implements FrontiaSocialShareListener {
    //
    // @Override
    // public void onSuccess() {
    // Log.d("Test", "share success");
    // //分享积分操作
    // sp=getSharedPreferences("USERINFO", Context.MODE_PRIVATE);
    // String username=sp.getString("UserName", "");
    // if (username!=null&&!username.equals("")) {
    //
    // new ShowJiFen(getApplicationContext(), "11", sp);
    // }
    // Toast.makeText(getApplicationContext(), "分享成功！", 1).show();
    // }
    //
    // @Override
    // public void onFailure(int errCode, String errMsg) {
    // Log.d("Test", "share errCode " + errCode);
    // }
    //
    // @Override
    // public void onCancel() {
    // Log.d("Test", "cancel ");
    // }
    //
    // }

}
