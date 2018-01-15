package com.dingtai.jinriyongzhou.activity;

/**
 * 作    者： 李亚军
 * 时 间：2015-1-26	下午3:37:06
 * 描   述：
 */

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.api.API;
import com.dingtai.base.api.UsercenterAPI;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.model.UserScoreList;
import com.dingtai.base.userscore.ShowJiFen;
import com.dingtai.base.userscore.UserScoreConstant;
import com.dingtai.base.userscore.UserScoreMode;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.WutuSetting;
import com.dingtai.dtflash.activity.LeadActivity;
import com.dingtai.dtflash.api.StartPageAPI;
import com.dingtai.dtflash.model.OpenPicDetail;
import com.dingtai.dtflash.model.OpenPicModel;
import com.dingtai.dtflash.model.StartPageModel;
import com.dingtai.dtflash.service.StartPageService;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.view.viewpager.FixedSpeedScroller;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 作 者： 李亚军 时 间：2015-1-26 下午3:37:06 开机画面
 */
@SuppressLint("NewApi")
public class StartPageActivity extends BaseActivity {

    private static final int VIEWPAGER_FLAG = -1000;
    private static final long VIEWPAGER_INTERVAL = 1200;
    //    private static final int STARTPIC_FLAG = IndexAPI.START_PICS_API;
//    private static final String STARTPIC_FLAG_MESSAGE = IndexAPI.START_PICS_API_MESSAGE;
    private static final int STARTPIC_FLAG = StartPageAPI.OPENPIC_LIST_API;
    private static final String STARTPIC_FLAG_MESSAGE = StartPageAPI.OPENPIC_LIST_MESSAGE;
    private ImageLoadingListenerImpl mImageLoadingListenerImpl;
    ImageView imgCopyright, imgBig;// 版权信息，
    TextView imgGoTo;// 跳过
    // private TextView autoGoTo; //自动跳转
    TextView txtTitle, txtSubTitle;// 标题
    RelativeLayout rlTitle;// 大背景图,标题布局
    private int time = 3000;
    String ChID = "0";
    // SharedPreferences sp;// 随机数处理
    private SharedPreferences sp1;
    Editor editor;
    static String tempSP;// 临时随机数

    private String setting;// 回看开机画面
    private SharedPreferences lead;
    private String isFirstLead;
    private String bangben;
    private String UserGuid = "";
    private boolean isClick = false;// 防止多次点击
    private UserInfoModel user;
    private RuntimeExceptionDao<StartPageModel, String> startPage;
    StartPageModel startPageModel;
    private CountDownTimer timer = null;
    private ViewPager mViewPager;
    private ArrayList<OpenPicModel> openPicsList;
    private OpenPicAdapter openPicAdapter;
    private int mCurrentPosition = 0;
    private FixedSpeedScroller mScroller;
    private RuntimeExceptionDao<OpenPicModel, String> opDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自动生成的方法存根
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);
        //设置全屏的方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        sp1 = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//		if (sp1 != null) {
//			UserInfo.USER_LOGINED_USERNAME = sp1.getString("UserNickName", "");
//			UserInfo.USER_LOGINED_PHONE = sp1.getString("userphone", "");
//			UserInfo.USER_LOGINED_GUID = sp1.getString("userguid", "");
//			UserInfo.USER_LOGINED_UserNickName = sp1.getString("UserNickName",
//					"");
//			UserGuid = sp1.getString("userguid", "");
//		}
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        get_action_detail(this);

        imgCopyright = (ImageView) findViewById(R.id.imgCopyright);
        imgGoTo = (TextView) findViewById(R.id.imgGoTo);
        // autoGoTo = (TextView) this.findViewById(R.id.autoGoTo);
        imgBig = (ImageView) findViewById(R.id.imgBig);
        rlTitle = (RelativeLayout) findViewById(R.id.rlTitle);
        // txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtSubTitle = (TextView) findViewById(R.id.txtSubTitle);
        rlTitle.setVisibility(View.GONE);
        // 获取设置配置
        SharedPreferences mSp = getSharedPreferences("SETTING",
                Context.MODE_PRIVATE);
        WutuSetting.setImgType(mSp.getInt("READTYPE", 0));
        // MyImageLoader.init(this, SettingActivityNew.IS_NIGHT);


        // 长江统计 启动APP
        // CJstatistics();
        if (startPageModel == null) {
            startPageModel = new StartPageModel();
        }
        startPage = getHelper().getMode(StartPageModel.class);
        if (getIntent() != null) {
            setting = getIntent().getStringExtra("setting");
        }
        // lead = getSharedPreferences("Lead", Context.MODE_PRIVATE);
        // isFirstLead = lead.getString("isFirstLead", "");
        // bangben = lead.getString("banben", "");
        // try {
        //
        // sp = this.getSharedPreferences("SPStartPage", 0);
        // if (sp.getString("RANDOMNUM", "").length() < 1) {
        // editor = sp.edit();
        //
        // editor.putString("RANDOMNUM", "-1");
        // editor.commit();
        // }
        // } catch (Exception e) {
        //
        // }
        // sp.edit().clear().commit();
        if (Assistant.IsContectInterNet2(getApplicationContext())) {
            getData();
        } else {
            bindOPCacheData();
            startCount();
        }
        imgGoTo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (timer != null)
                    timer.cancel();
                // startActivity(new Intent(StartPageActivity.this,
                // IndexNewsActivity.class));
                // StartPageActivity.this.finish();
                if (isClick) {
                    return;
                }
                isClick = true;
                if (setting != null && !"".equals(setting)) {
                    StartPageActivity.this.finish();
                } else {

                    PackageInfo packageInfo2 = null;
                    try {
                        packageInfo2 = getApplicationContext()
                                .getPackageManager().getPackageInfo(
                                        getPackageName(), 0);
                    } catch (NameNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    String str = "";
                    if (packageInfo2 != null) {
                        str = packageInfo2.versionName;
                    }
                    if (/* isFirstLead.equals("") || !str.equals(bangben) */startPage
                            .queryForAll().size() == 0) {
                        Intent i = new Intent();
                        i.setClass(StartPageActivity.this, LeadActivity.class);
                        StartPageActivity.this.startActivity(i);
                        StartPageActivity.this.finish();
                        startPageModel.setIsShow("unShow");
                        startPage.create(startPageModel);
                        // lead.edit().putString("isFirstLead",
                        // "first").commit();
                        // lead.edit().putString("banben", str).commit();
                    } else {
                        Intent i = new Intent();
                        i.setAction(basePackage + "home");
                        i.putExtra("isAnimation", "True");
                        StartPageActivity.this.startActivity(i);
                        // lead.edit().putString("banben", str).commit();
                        StartPageActivity.this.finish();
                    }
                }

            }
        });

        // // 定时器，3秒过后，进入主界面
        // TimerTask task = new TimerTask() {
        // public void run() {
        // timer.cancel();
        // if (setting != null && !"".equals(setting)) {
        // StartPageActivity.this.finish();
        // } else {
        // // Intent i = new Intent();
        // // i.setClass(StartPageActivity.this,
        // // IndexNewsActivity.class);
        // // StartPageActivity.this.startActivity(i);
        // // StartPageActivity.this.finish();
        //
        // PackageInfo packageInfo2 = null;
        // try {
        // packageInfo2 = getApplicationContext()
        // .getPackageManager().getPackageInfo(
        // getPackageName(), 0);
        // } catch (NameNotFoundException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // }
        // String str = "";
        // if (packageInfo2 != null) {
        // str = packageInfo2.versionName;
        // }
        //
        // if (/* isFirstLead.equals("") || !str.equals(bangben) */startPage
        // .queryForAll().size() == 0) {
        // Intent i = new Intent();
        // i.setClass(StartPageActivity.this, LeadActivity.class);
        // startPageModel.setIsShow("unShow");
        // startPage.create(startPageModel);
        // StartPageActivity.this.startActivity(i);
        // StartPageActivity.this.finish();
        // // lead.edit().putString("isFirstLead",
        // // "first").commit();
        // // lead.edit().putString("banben", str).commit();
        // } else {
        // Intent i = new Intent();
        // i.putExtra("isAnimation", "True");
        // i.setClass(StartPageActivity.this, HomeActivity.class);
        // StartPageActivity.this.startActivity(i);
        // // lead.edit().putString("banben", str).commit();
        // StartPageActivity.this.finish();
        // }
        //
        // }
        //
        // }
        // };

        // timer = new Timer(true);

        // timer.schedule(task, 5000);
        // 随机数判断
        user = Assistant.getUserInfoByOrm(StartPageActivity.this);
        if (user != null) {
            getCache();
            // user_mode = getHelper().get_user_mode();
            // Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            // SimpleDateFormat format = new SimpleDateFormat(
            // "yyyy-MM-dd HH:mm:ss");
            // String str = format.format(curDate);
            // user.setCreateTime(str);
            // user_mode.update(user);
        }
        getVersion();

    }

    // 获取活动界面详情
    public void get_action_detail(Context paramContext) {
        Intent localIntent = new Intent(paramContext,
                StartPageService.class);
        localIntent.putExtra("api", UsercenterAPI.USER_ACTION);
        paramContext.startService(localIntent);
    }

    public String GetImei() {
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        return deviceId;
    }

    // private void CJstatistics() {
    // // TODO Auto-generated method stub
    // // 设备型号
    // try {
    // //DeviceCommonInfoByActivity device = new DeviceCommonInfoByActivity();
    // String Device = GetImei();
    // Intent localIntent1 = new Intent();
    // localIntent1.putExtra(CountAPI.METHOD_KEY, CountAPI.GET_TOKEN_API);
    // localIntent1.putExtra(CountAPI.UID, UserGuid);
    // localIntent1.putExtra(CountAPI.IMEI, Device);
    // Thread thread1 = new CountHttpService(this, localIntent1);
    // thread1.start();
    // } catch (Exception e) {
    // // TODO: handle exception
    // Log.e("xhqxxx", e.toString());
    // }
    //
    // }

    // Timer timer;


    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (handler != null) {
            handler.removeMessages(VIEWPAGER_FLAG);
        }
        super.onDestroy();
        StartPageActivity.this.finish();
    }

    // 接收信息
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case VIEWPAGER_FLAG:
                    handler.removeMessages(VIEWPAGER_FLAG);
                    mCurrentPosition++;
                    mViewPager.setCurrentItem(mCurrentPosition);
//                    handler.sendEmptyMessageDelayed(VIEWPAGER_FLAG, VIEWPAGER_INTERVAL);
                    break;
                case 0:
                case -1:
                    // Toast.makeText(StartPageActivity.this, msg.obj.toString(),
                    // Toast.LENGTH_SHORT).show();
                    bindOPCacheData();
                    startCount();
                    break;
                case STARTPIC_FLAG:
                    try {

                        if (!msg.obj.toString().equals("暂无更多数据")) {

                            ArrayList<OpenPicModel> opCacheData = (ArrayList<OpenPicModel>) msg
                                    .getData().getParcelableArrayList("list")
                                    .get(0);
                            if (opCacheData != null && opCacheData.size() > 0) {
                                setViewPage(opCacheData);
                                boolean flag = true;
                                for (int i = 0; i < opCacheData.size(); i++) {
                                    if (TextUtils.isEmpty(opCacheData.get(i).getImgUrl())) {
                                        flag = false;
                                        break;
                                    }
                                    if (flag) {
                                        if (opDao == null) {
                                            opDao = getHelper().getMode(OpenPicModel.class);
                                        }
                                        opDao.delete(opDao.queryForAll());
                                        for (int i1 = 0; i1 < opCacheData.size(); i1++) {
                                            opDao.createIfNotExists(opCacheData.get(i1));
                                        }

                                    }
                                }
                            } else {
                                ArrayList<OpenPicModel> openPicsFromDAO = getOpenPicsFromDAO();
                                if (openPicsFromDAO != null && openPicsFromDAO.size() > 0) {
                                    setViewPage(openPicsFromDAO);
                                }
                            }
                        }
                        startCount();
                    } catch (Exception e) {
                        time = 3000;
                        startCount();
                    }

                    break;
                // case StartPageAPI.OPENPIC_COMPARE_API:
                // try {
                //
                // ArrayList<String> opData = (ArrayList<String>) msg
                // .getData().getParcelableArrayList("list").get(0);
                // if (!msg.obj.toString().equals("暂无更多数据")) {
                // // 开机画面请求
                // String strRandomNum = sp.getString("RANDOMNUM", "none");
                // // Toast.makeText(StartPageActivity.this, "RANDOMNUM:" +
                // // strRandomNum + "     opData:" +
                // // opData.get(0).toString(), Toast.LENGTH_SHORT).show();
                // if (!opData.get(0).toString().equals(strRandomNum)) {
                // tempSP = opData.get(0).toString();
                // editor = sp.edit();
                // editor.putString("RANDOMNUM", tempSP);
                // editor.commit();
                //
                // getData();
                // } else {
                // // 绑定本地开机数据
                // bindOPCacheData();
                // }
                // }
                //
                // } catch (Exception e) {
                // }

                // break;

                default:
                    break;
            }
        }

        ;
    };

    private void setViewPage(ArrayList<OpenPicModel> openPicModels) {
        openPicsList = new ArrayList<>();
        openPicsList.addAll(openPicModels);
        openPicAdapter = new OpenPicAdapter();
        mViewPager.setAdapter(openPicAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        try {
            // 通过class文件获取mScroller属性    
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
            mField.set(mViewPager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mScroller.setmDuration(900);

//        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_DOWN && event.getAction() != MotionEvent.ACTION_UP) {
                    return true;
                }
                return false;
            }
        });

        handler.sendEmptyMessageDelayed(VIEWPAGER_FLAG, VIEWPAGER_INTERVAL);
    }

    private void startCount() {
        if (isClick)
            return;
        timer = new CountDownTimer(time, 10000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub
                // autoGoTo.setText(String.valueOf(millisUntilFinished/1000));

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                timer.cancel();
                if (setting != null && !"".equals(setting)) {
                    StartPageActivity.this.finish();
                } else {
                    // Intent i = new Intent();
                    // i.setClass(StartPageActivity.this,
                    // IndexNewsActivity.class);
                    // StartPageActivity.this.startActivity(i);
                    // StartPageActivity.this.finish();

                    PackageInfo packageInfo2 = null;
                    try {
                        packageInfo2 = getApplicationContext().getPackageManager()
                                .getPackageInfo(getPackageName(), 0);
                    } catch (NameNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    String str = "";
                    if (packageInfo2 != null) {
                        str = packageInfo2.versionName;
                    }

                    if (/* isFirstLead.equals("") || !str.equals(bangben) */startPage
                            .queryForAll().size() == 0) {
                        Intent i = new Intent();
                        i.setClass(StartPageActivity.this, LeadActivity.class);
                        startPageModel.setIsShow("unShow");
                        startPage.create(startPageModel);
                        StartPageActivity.this.startActivity(i);
                        StartPageActivity.this.finish();
                        // lead.edit().putString("isFirstLead",
                        // "first").commit();
                        // lead.edit().putString("banben", str).commit();
                    } else {
                        Intent i = new Intent();
                        i.putExtra("isAnimation", "True");
                        i.setAction(basePackage + "home");
                        StartPageActivity.this.startActivity(i);
                        // lead.edit().putString("banben", str).commit();
                        StartPageActivity.this.finish();
                    }

                }

            }
        };
        timer.start();
    }

    /**
     * description: 根据概率大小计算获取随机图片
     * autour: xf
     * date: 2017/11/6 0006 下午 3:45
     * version:
     */
    private OpenPicDetail getOpenPic(List<OpenPicDetail> list) {
        try {
            int sum = 0;
            for (int i = 0; i < list.size(); i++) {
                OpenPicDetail openPicDetail = list.get(i);
                openPicDetail.setWeight("1");
                try {
                    sum += Integer.parseInt(openPicDetail.getWeight());
                } catch (NumberFormatException e) {
                    sum += 1;
                    e.printStackTrace();
                }
            }
            Random random = new Random();
            int percent = random.nextInt(sum);
            int weight = 0;
            for (int i = 0; i < list.size(); i++) {
                OpenPicDetail openPicDetail = list.get(i);
                if (percent <= Integer.parseInt(openPicDetail.getWeight()) + weight && percent > weight) {
                    return list.get(i);
                } else {
                    weight += Integer.parseInt(openPicDetail.getWeight());
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.get(0);
    }

    /**
     * description: 显示图片
     * autour: xf
     * date: 2017/11/6 0006 下午 3:46
     * version:
     */


    /**
     * 作 者： 李亚军 时 间：2015-1-27 下午4:24:37 描 述：如果没有新的开机画面数据，读取缓存数据
     */
    public void bindOPCacheData() {
        try {

            ArrayList<OpenPicModel> opCacheData = getOpenPicsFromDAO();

            // final String ADFor = opCacheData.get(0).getForApp();
            final String ADName = opCacheData.get(0).getOpenPicName();
            final String LinkTo = opCacheData.get(0).getLinkTo();
            final String ADFor = opCacheData.get(0).getADFor();
            final String LinkUrl = opCacheData.get(0).getLinkUrl();
            final String ChID = opCacheData.get(0).getChID();
            final String ImgUrl = opCacheData.get(0).getImgUrl();

            if (opCacheData.get(0).getADContent().length() > 1) {
                rlTitle.setVisibility(View.VISIBLE);
                txtSubTitle.setText(opCacheData.get(0).getADContent());
            } else {
                rlTitle.setVisibility(View.GONE);
                txtSubTitle.setText("");
            }

            rlTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Log.i("test", "点击405");
                    ADGoTo(ADName, ADFor, LinkTo, LinkUrl, ChID, ImgUrl);// 广告跳转
                }
            });

        } catch (Exception e) {
            // Toast.makeText(StartPageActivity.this, "绑定出错。",
            // Toast.LENGTH_SHORT).show();
        }
    }

    @Nullable
    private ArrayList<OpenPicModel> getOpenPicsFromDAO() {
        ArrayList<OpenPicModel> opCacheData = null;
        RuntimeExceptionDao<OpenPicModel, String> opList = getHelper()
                .getMode(OpenPicModel.class);
        opCacheData = (ArrayList<OpenPicModel>) opList.queryForAll();
        if (opCacheData != null && opCacheData.size() > 0) {
            setViewPage(opCacheData);
        }
        return opCacheData;
    }

    /**
     * 获取列表的数据
     */
    public void getData() {
        String URL = API.COMMON_URL
                + "Interface/OpenPicAPI.ashx?action=GetOpenPicByStID";
        String strStID = API.STID;
        String strSign = API.sign;
        String ForApp = getDeviceResolution();
        get_openpic_list(StartPageActivity.this, URL, strStID, strSign, ForApp,
                new Messenger(StartPageActivity.this.handler));

    }

    /**
     * 作 者： 李亚军 时 间：2015-1-26 下午2:16:41 描 述：开机画面数据获取
     *
     * @param paramContext
     * @param url
     * @param StID
     * @param sign
     * @param paramMessenger
     */
    public void get_openpic_list(Context paramContext, String url, String StID,
                                 String sign, String ForApp, Messenger paramMessenger) {
        // http://localhost:5460/Interface/OpenPicAPI.ashx?action=GetOpenPicByStID&StID=1&sign=1
        Intent intent = new Intent(paramContext, StartPageService.class);
        intent.putExtra("api", STARTPIC_FLAG);
        intent.putExtra(STARTPIC_FLAG_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        intent.putExtra("sign", sign);
        intent.putExtra("ForApp", ForApp);
        paramContext.startService(intent);
    }

    /**
     * 获取列表的数据标志
     */
    public void getDataFlag() {
        String URL = API.COMMON_URL
                + "interface/CompareAPI.ashx?action=CompareOpenPicData";
        String strStID = API.STID;
        String strSign = API.sign;
        String ForApp = getDeviceResolution();
        get_openpic_compare(StartPageActivity.this, URL, strStID, ForApp,
                strSign, new Messenger(StartPageActivity.this.handler));

    }

    /**
     * 作 者： 李亚军 时 间：2015-1-26 下午2:16:41 描 述：开机画面数据对比
     *
     * @param paramContext
     * @param url
     * @param StID
     * @param sign
     * @param paramMessenger
     */
    public void get_openpic_compare(Context paramContext, String url,
                                    String StID, String ForApp, String sign, Messenger paramMessenger) {
        // http://localhost:5460/interface/CompareAPI.ashx?action=CompareOpenPicData&StID=1
        Intent intent = new Intent(paramContext, StartPageService.class);
        intent.putExtra("api", StartPageAPI.OPENPIC_COMPARE_API);
        intent.putExtra(StartPageAPI.OPENPIC_COMPARE_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        intent.putExtra("ForApp", ForApp);
        intent.putExtra("sign", sign);
        paramContext.startService(intent);
    }

    // 监听图片异步加载
    public static class ImageLoadingListenerImpl extends
            SimpleImageLoadingListener {

        public static final List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imgNewsPictureStyle1 = (ImageView) view;
                boolean isFirstDisplay = !displayedImages.contains(imageUri);
                if (isFirstDisplay) {
                    // 图片的淡入效果
                    FadeInBitmapDisplayer.animate(imgNewsPictureStyle1, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**
     * 作 者： 李亚军 时 间： 2015 2015-2-15 上午11:23:35 描 述： 根据手机分辨率返回获取开机画面的图片编号
     *
     * @return 编号
     */
    public String getDeviceResolution() {
        String Resolution = "0"; // 720*1080
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        Display display = wm.getDefaultDisplay();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Resolution = width + "*" + height;

        if (Resolution.equals("320*480")) {
            return "5";

        } else if (Resolution.equals("480*640")) {
            return "6";
        } else if (Resolution.equals("480*800")) {
            return "7";
        } else if (Resolution.equals("540*960")) {
            return "8";
        } else if (Resolution.equals("720*1280")) {
            return "9";
        } else if (Resolution.equals("1080*1920")) {
            return "10";
        } else {
            return "9";
        }

    }

    /**
     * 作 者： 李亚军 时 间：2015-1-30 下午4:19:44 描 述：快捷跳转
     *
     * @param ADFor
     * @param LinkTo
     * @param LinkUrl
     * @param ChID
     */
    public void ADGoTo(String ADName, String ADFor, String LinkTo,
                       String LinkUrl, String ChID, String ImgUrl) {
        try {
            // try {// 积分任务
            // if (Assistant.getUserInfoByOrm(StartPageActivity.this) != null) {
            // new ShowJiFen(StartPageActivity.this,
            // UserScoreConstant.SCORE_CLICK_STARTAD,
            // UserScoreConstant.SCORE_CLICK_STARTAD,
            // UserScoreConstant.ExchangeID,
            // Assistant.getUserInfoByOrm(StartPageActivity.this));
            // }
            // } catch (Exception e) {
            // }

            // Toast.makeText(StartPageActivity.this, "跳转到……" + ADFor + "广告名……"
            // + ADName + "链接到……" + LinkTo + "LinkUrl……" + LinkUrl + "栏目ID……" +
            // ChID, Toast.LENGTH_SHORT).show();

            // Intent intent = null;
            // String[] LinkUrls;
            try {
                Intent i = new Intent();
                i.putExtra("isAnimation", "True");
                i.setAction(basePackage + "home");
                StartPageActivity.this.startActivity(i);
                if (LinkTo.equals("1")) {// 1、详情2、 列表
                    String[] LinkUrls = LinkUrl.split(",");
                    String strTempID = LinkUrls[0];
                    String strTempGUID = LinkUrls[1];
                    timer.cancel();
                    Intent intent = new Intent();
                    intent.setAction(basePackage + "NewsDetail");
                    intent.putExtra("ID", strTempGUID);
                    intent.putExtra("ResourceType", strTempID);
                    intent.putExtra("isHome", true);
                    startActivity(intent);
                } else {

                    timer.cancel();
                    Intent intent = new Intent();
                    intent.setAction(basePackage + "newsList");
                    intent.putExtra("lanmuChID", ChID);
                    intent.putExtra("ChannelName", "新闻列表");
                    startActivity(intent);
                }
                StartPageActivity.this.finish();
            } catch (Exception e) {
                Log.d("xf", e.toString());
            }

            // switch (Integer.parseInt(ADFor)) {
            // case 1:// 新闻
            // intent = new Intent(StartPageActivity.this,
            // IndexNewsActivity.class);
            // startActivity(intent);
            // if (LinkTo.equals("1")) {// 1、详情 2、列表
            // LinkUrls = LinkUrl.split(",");
            // String strTempID = LinkUrls[0];
            // String strTempGUID = LinkUrls[1];
            //
            // intent = new Intent(StartPageActivity.this,
            // NewsDetailActivity.class);
            // intent.putExtra("ID", strTempGUID);
            // intent.putExtra("ResourceType", strTempID);
            //
            // startActivity(intent);
            // } else {
            // String strTempChID = LinkUrl;
            // Bundle mBundle = new Bundle();
            // mBundle.putString("lanmuid", strTempChID);
            // intent = new Intent(StartPageActivity.this,
            // NewsActivityTab.class);
            // Assistant.NEWS_PARENTER = "1112"; // 新闻栏目ID
            // intent.putExtra("lanmuChID", strTempChID);
            //
            // startActivity(intent);
            // }
            // break;
            // case 2:// 电商
            //
            // break;
            // case 3:// 活动
            //
            // intent = new Intent(StartPageActivity.this,
            // IndexNewsActivity.class);
            // startActivity(intent);
            // if (LinkTo.equals("1")) {
            // LinkUrls = LinkUrl.split(",");
            // String Name = LinkUrls[0];
            // String ActiveURL = LinkUrls[1];
            // intent = new Intent(StartPageActivity.this, ActiveWebView.class);
            // intent.putExtra("Type", "活动");
            // intent.putExtra("LogoUrl", ImgUrl);
            // intent.putExtra("Title", Name);
            // intent.putExtra("PageUrl", ActiveURL);
            // } else {
            // intent = new Intent(StartPageActivity.this,
            // ActiveActivity.class);
            // }
            // startActivity(intent);
            // break;
            //
            // default:
            // break;
            // }
        } catch (Exception e) {
            // Toast.makeText(StartPageActivity.this, "跳转到……",
            // Toast.LENGTH_SHORT).show();

        }
    }

    private void getCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RuntimeExceptionDao<UserScoreList, String> userScore = getHelper()
                        .getMode(UserScoreList.class);
                List<UserScoreList> temp_list = userScore.queryForAll();
                /*
                 * if (temp_list == null || temp_list.size() == 0) {
				 * get_userscore_list(this, UsercenterAPI.USER_SCORE_URL,
				 * API.STID, user.getUserGUID(), new Messenger(handler));
				 * return; }
				 */
                if (temp_list != null && temp_list.size() > 0) {
                    UserScoreConstant.ScoreMap.clear();
                    for (UserScoreList userScoreList : temp_list) {
                        try {
                            UserScoreConstant.ScoreMap.put(
                                    userScoreList.getTaskCode(),
                                    new UserScoreMode(userScoreList
                                            .getTaskName(), Integer
                                            .parseInt(userScoreList
                                                    .getTaskScore()), Integer
                                            .parseInt(userScoreList
                                                    .getTaskWorkCount()),
                                            userScoreList.getTaskCode()));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                    try {// 积分任务
                        if (Assistant.getUserInfoByOrm(StartPageActivity.this) != null
                                && UserScoreConstant.ScoreMap
                                .containsKey(UserScoreConstant.SCORE_STARTAPP)) {
                            new ShowJiFen(
                                    StartPageActivity.this,
                                    UserScoreConstant.SCORE_STARTAPP,
                                    UserScoreConstant.SCORE_STARTAPP,
                                    UserScoreConstant.ExchangeID,
                                    Assistant
                                            .getUserInfoByOrm(StartPageActivity.this));
                        }
                    } catch (Exception e) {
                    }
                    get_userscore_list(getApplicationContext(),
                            UsercenterAPI.USER_SCORE_URL, API.STID,
                            user.getUserGUID(), new Messenger(handler));
                }
            }
        }).start();

    }

    /**
     * 获得用户积分任务列表
     *
     * @param paramContext
     * @param url
     * @param StID           站点ID
     * @param UserGUID       用户guid
     * @param paramMessenger
     */
    public void get_userscore_list(Context paramContext, String url,
                                   String StID, String UserGUID, Messenger paramMessenger) {
        // http://standard.d5mt.com.cn/Interface/RegisterUserScoreAPI.ashx?action=GetScoreTaskList&StID=1&UserGUID=e5d95959-a131-4da4-be3b-abebb0920411

        Intent intent = new Intent(paramContext, StartPageService.class);
        intent.putExtra("api", UsercenterAPI.USER_SCORE_API);
        intent.putExtra(UsercenterAPI.USER_SCORE_MESSAGE, paramMessenger);
        intent.putExtra("url", url);
        intent.putExtra("StID", StID);
        intent.putExtra("UserGUID", UserGUID);
        paramContext.startService(intent);

    }

    public void getVersion() {
        int Version = Build.VERSION.SDK_INT;
        if (Version < 15) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    StartPageActivity.this, R.style.dialog2);
            builder.setTitle("重要提示").setMessage(
                    "尊敬的用户，您的手机操作系统版本号为 " + Build.VERSION.RELEASE
                            + " 。因操作系统版本过低，客户端相关功能可能无法正常使用。建议您升级至安卓4.0以上，谢谢。");
            // 退出当前软件，更新系统。
            builder.setPositiveButton(R.string.app_exit, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    finish();
                }
            });
            // 不更新系统，继续使用软件
            builder.setNegativeButton(R.string.no_update_version,
                    new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(basePackage + "home");
                            startActivity(intent);
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                            finish();
                        }
                    });
            builder.show();
        }

    }

    private class OpenPicAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(StartPageActivity.this);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (openPicsList != null && openPicsList.size() > 0) {
                final int currentPosition = position % openPicsList.size();
                Glide.with(StartPageActivity.this).load(openPicsList.get(currentPosition).getImgUrl()).into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(StartPageActivity.this, openPicsList.get(currentPosition).getLinkUrl(), Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                imageView.setImageDrawable(getDrawable(R.drawable.dt_standard_index_news_bg));
            }
            container.addView(imageView);
            return imageView;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }
}
