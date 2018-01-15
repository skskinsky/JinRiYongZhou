package com.dingtai.jinriyongzhou.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.androidtranscoder.compat.MediaTranscoder;
import com.dingtai.base.androidtranscoder.format.MediaFormatStrategyPresets;
import com.dingtai.base.api.API;
import com.dingtai.base.filechoose.ipaulpro.afilechooser.utils.FileUtils;
import com.dingtai.base.listener.ProgressListener;
import com.dingtai.base.model.BaoliaoFileModel;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.recorder.FFmpegRecorderActivity;
import com.dingtai.base.userscore.ShowJiFen;
import com.dingtai.base.userscore.UserScoreConstant;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.CommonTools;
import com.dingtai.base.utils.DateTool;
import com.dingtai.base.utils.StringOper;
import com.dingtai.base.utils.UploadFileConn;
import com.dingtai.base.view.CompressImage;
import com.dingtai.base.view.MyGridView;
import com.dingtai.base.xunfei.VoiceToWord;
import com.dingtai.dtpolitics.activity.ProvinceAreaActivity;
import com.dingtai.dtpolitics.activity.ProvinceOrLeaderActivity;
import com.dingtai.dtpolitics.activity.WenZhengProgressActivity;
import com.dingtai.dtpolitics.service.WenZhengAPI;
import com.dingtai.dtpolitics.service.WenZhengHttpService;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.WenzhengImageAdapter;
import com.dingtai.jinriyongzhou.api.IndexAPI;
import com.dingtai.jinriyongzhou.service.IndexHttpService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 投稿
 */
public class ContributeActivity extends BaseActivity implements
        OnClickListener {

    EditText dxtTitle, dxtPhone, dxtName;// , dxtContent;
    public EditText dxtContent;
    ImageButton imgPicture, imgVideo, imgAudio;
    TextView btnConfirm;
    private TextView dxtLeader, dxtType, dxtarea;
    private CheckBox wenzheng_isnoname;
    private Intent intent;

    private WenzhengImageAdapter imageAdapter = null;
    private int DataType = -1;
    public static List<String> selected_list;// 选择图片返回的数据
    private String video_path;
    private String img_path, img_name;

    private ArrayList<BaoliaoFileModel> fileList;
    private ArrayList<BaoliaoFileModel> imgList;
    private ArrayList<BaoliaoFileModel> videoList;
    private ArrayList<String> listCphto;
    private CompressImage CI;
    private static final int REQUEST_CODE = 10010;
    private static final int REQUEST_CODE_PICK = 5;
    public static String ResultFilePath = ""; // 转码后文件所存放的路径地址
    private MyGridView baoliao_release_media;
    private String UserGUID = "";
    private String RevelationContent, UserPhone, video_url = "", img_url = "",
            leader, area, type, name, title;
    private String cityParentId;
    private String PoliticsTypeOne = 1 + "";
    private String PoliticsTypeTwo = 1 + "";
    private String isnoname;

    private UserInfoModel userInfoModel;

    private ProgressDialog progressDialog;
    private int BaoliaoType = 1;
    private ImageView btnCancle;
    private String titles, DeptID;
    private TextView tv_titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 问政返回处理
        btnCancle = (ImageView) findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(this);
        // 问政发布处理
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(this);

        // 照片选择
        imgPicture = (ImageButton) findViewById(R.id.imgPicture);
        imgPicture.setOnClickListener(this);

        // 照片选择
        imgAudio = (ImageButton) findViewById(R.id.imgAudio);
        imgAudio.setOnClickListener(this);

        // 视频选择
        imgVideo = (ImageButton) findViewById(R.id.imgVideo);
        imgVideo.setOnClickListener(this);

        tv_titles = (TextView) findViewById(R.id.txtTitle);

        dxtLeader = (TextView) findViewById(R.id.dxtLeader);
        dxtTitle = (EditText) findViewById(R.id.dxtTitle);
        dxtType = (TextView) findViewById(R.id.dxtType);
        dxtPhone = (EditText) findViewById(R.id.dxtPhone);
        if (Assistant.getUserInfoByOrm(this) != null)
            dxtPhone.setText(Assistant.getUserInfoByOrm(this).getUserPhone() + "");
        dxtName = (EditText) findViewById(R.id.dxtName);
        dxtContent = (EditText) findViewById(R.id.dxtContent);
        dxtarea = (TextView) findViewById(R.id.dxtarea);
        wenzheng_isnoname = (CheckBox) findViewById(R.id.wenzheng_isnoname);
        baoliao_release_media = (MyGridView) findViewById(R.id.baoliao_release_media);

        dxtLeader.setOnClickListener(this);
        dxtType.setOnClickListener(this);
        dxtarea.setOnClickListener(this);

        listCphto = new ArrayList<String>();
        fileList = new ArrayList<BaoliaoFileModel>();
        CI = new CompressImage();
        userInfoModel = Assistant.getUserInfoByOrm(this);

        imageAdapter = new WenzhengImageAdapter(this, fileList);
        imageAdapter.setIsDel(0);
        baoliao_release_media.setAdapter(imageAdapter);
        baoliao_release_media
                .setOnItemLongClickListener(new OnItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0,
                                                   View arg1, int arg2, long arg3) {
                        // TODO 自动生成的方法存根
                        imageAdapter.setIsDel(1);
                        imageAdapter.notifyDataSetChanged();
                        return false;
                    }
                });

        intent = getIntent();
        if (intent != null) {/*
            if (intent.hasExtra("cityName")) {
				dxtLeader.setText(intent.getStringExtra("cityName"));
				cityParentId = intent.getStringExtra("cityParentId");

		*/
            titles = intent.getStringExtra("title");
            DeptID = intent.getStringExtra("id");
            tv_titles.setText(titles);
        }

    }

    @Override
    public void onClick(View vw) {
        Intent intent = null;
        int i = vw.getId();
        if (i == R.id.dxtLeader) {
            intent = new Intent(this,
                    ProvinceAreaActivity.class);
            startActivityForResult(intent, 3);

        } else if (i == R.id.btnCancle) {
            finish();

        } else if (i == R.id.dxtType) {
            intent = new Intent(this,
                    ProvinceOrLeaderActivity.class);
            intent.putExtra("PoliticsTypeOne", "2");
            startActivityForResult(intent, 2);

        } else if (i == R.id.dxtarea) {
            intent = new Intent(this,
                    ProvinceOrLeaderActivity.class);
            intent.putExtra("PoliticsTypeOne", "1");
            startActivityForResult(intent, 1);

        } else if (i == R.id.btnConfirm) {
            if (checkMessage()) {
                uploladWenZheng();
            }

        } else if (i == R.id.imgPicture) {
            showPictureDailog();

        } else if (i == R.id.imgAudio) {
            VoiceToWord voice = new VoiceToWord(this, "534e3fe2", 0); // 5592509f
            voice.GetWordFromVoice();
            voice.setListener(new VoiceToWord.VoiceResultListener() {
                @Override
                public void onResult(String s) {
                    dxtContent.setText(dxtContent.getText().toString() + s);
                    dxtContent.setSelection(dxtContent.getText().length());
                }
            });


        } else if (i == R.id.imgVideo) {
            showVideoDailog();

        } else {
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case API.SUCCESS:
                    progressDialog.dismiss();
                    if (Assistant.getUserInfoByOrm(ContributeActivity.this) != null
                            && UserScoreConstant.ScoreMap
                            .containsKey(UserScoreConstant.SCORE_SPEAK_BAOLIAO)) {
                        new ShowJiFen(ContributeActivity.this,
                                UserScoreConstant.SCORE_SPEAK_BAOLIAO,
                                UserScoreConstant.SCORE_TYPE_ADD,
                                UserScoreConstant.ExchangeID,
                                Assistant.getUserInfoByOrm(ContributeActivity.this));
                    }

                    // Toast.makeText(this, "提问提交成功",
                    // 1).show();
                    Intent i = new Intent();
                    i.setAction(basePackage + "mybaoliao");
                    i.putExtra("UserGUID", UserGUID);
                    startActivity(i);
                    finish();
                    break;
                case API.UNSUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(ContributeActivity.this, "提问提交失败", Toast.LENGTH_SHORT).show();
                    break;
                case 100:
                    progressDialog.dismiss();
                    Toast.makeText(ContributeActivity.this, "提问文件上传成功", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(ContributeActivity.this,
//                            WenZhengProgressActivity.class);
//                    startActivity(intent);
                    finish();
                    break;
                case 0:

                    break;
                case 1000:
                    Toast.makeText(ContributeActivity.this, "提问文件上传成功", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                    if (imgList.size() > 0 && imgList != null) {
                        img_url = addSplit(imgList);
                    }

                    if (videoList.size() > 0 && videoList != null) {
                        video_url = addSplit(videoList);
                    }

                    if (!img_url.equalsIgnoreCase("")) {
                        BaoliaoType = 2;
                        if (!video_url.equalsIgnoreCase(""))
                            BaoliaoType = 4;
                    } else if (!video_url.equalsIgnoreCase("")) {
                        BaoliaoType = 3;
                        if (!img_url.equalsIgnoreCase(""))
                            BaoliaoType = 4;
                    } else {
                        BaoliaoType = 1;
                    }

                    if (wenzheng_isnoname.isChecked()) {
                        isnoname = "True";
                    } else {
                        isnoname = "False";
                    }
                    if (!TextUtils.isEmpty(area)) {
                        PoliticsTypeOne = getIndexInType("1", area);
                        PoliticsTypeTwo = getIndexInType("2", type);
                    }

//                        String PoliticsTitle, String PoliticsContent, String UserGUID,
//                        String ResType,
//                        String PicUrl, String VideoUrl,
//                        String UploadType, String FileDate,
//                        Messenger paramMessenger

                    addWenZheng(ContributeActivity.this,
                            IndexAPI.CONTRI_ADD_API, API.STID, title,
                            RevelationContent, userInfoModel.getUserGUID(),
                            "1", img_url, video_url, "1",
                            DateTool.getNowDate(),
                            new Messenger(mHandler));

                    // insert_Baoliao(this,
                    // BaoLiaoAPI.API_BAOLIAO_INSERT_BAOLIAO_URL, API.STID,
                    // RevelationContent, UserPhone, String.valueOf(BaoliaoType),
                    // img_url, video_url, String.valueOf(((MyApplication)
                    // getApplication()).lontitude), String.valueOf(((MyApplication)
                    // getApplication()).latitude), API.sign, UserGUID,
                    // DateTool.getNowDate(), new Messenger(
                    // mHandler));
                    break;
            }
        }

        ;
    };

    /**
     * 给文件路径加分隔符
     */
    public static String addSplit(List<BaoliaoFileModel> list) {
        StringBuffer s = new StringBuffer();
        if (list.size() > 1) {
            for (int i = 0; i < list.size(); i++) {
                if (list.size() == i + 1) {
                    if (list.get(i).getFileType().equalsIgnoreCase("image"))
                        s.append(
                                "/"
                                        + StringOper.formatter.format(new Date(System
                                        .currentTimeMillis())))
                                .append("/").append(list.get(i).getFileName());
                    else
                        s.append(list.get(i).getFileName());
                } else {
                    if (list.get(i).getFileType().equalsIgnoreCase("image"))
                        s.append(
                                "/"
                                        + StringOper.formatter.format(new Date(System
                                        .currentTimeMillis())))
                                .append("/").append(list.get(i).getFileName())
                                .append(",");
                    else
                        s.append(list.get(i).getFileName()).append(",");
                }
            }
            Log.i("tag", s.toString());
            String str = s.toString();
            // str = str.substring(0,str.length()-1);
            return str.toString();
        } else {
            if (list.get(0).getFileType().equalsIgnoreCase("image"))
                s.append(
                        "/"
                                + StringOper.formatter.format(new Date(System
                                .currentTimeMillis()))).append("/")
                        .append(list.get(0).getFileName());
            else
                s.append(list.get(0).getFileName());
            return s.toString();
        }
    }

    /**
     * 判断信息是否填写完整及正确性
     *
     * @return
     */
    public boolean checkMessage() {
        boolean f = false;

        if (dxtTitle.getText().equals("")) {
            Toast.makeText(this, "请输入标题！", Toast.LENGTH_SHORT).show();
        }
//		} else if (dxtLeader.getText().equals("")) {
//			Toast.makeText(this, "请先选择领导！", 0).show();
//		}
//		else if (dxtarea.getText().equals("")) {
//			Toast.makeText(this, "请先选择领域！", 0).show();
//		}
//		else if (dxtType.getText().equals("")) {
//			Toast.makeText(this, "请先选择分类！", 0).show();
//		}
        else if (dxtPhone.getText().equals("")) {
            Toast.makeText(this, "请先输入手机或QQ号码！", Toast.LENGTH_SHORT).show();
        } else if (dxtName.getText().equals("")) {
            Toast.makeText(this, "请先输入姓名！", Toast.LENGTH_SHORT).show();
        } else if (dxtContent.getText().equals("")) {
            Toast.makeText(this, "请先输入提问内容！", Toast.LENGTH_SHORT).show();
        } else if (dxtContent.getText().length() < 5) {
            Toast.makeText(this, "输入字数不可少于5！", Toast.LENGTH_SHORT).show();
        } else {
            f = true;
        }

        return f;
    }

    /**
     * 新增问政发布
     */
    boolean isM;
    float dx;

    public void uploladWenZheng() {
        long progressMax = 0;
        isM = false;// 进度是否是M
        if (fileList.size() > 8) {
            Toast.makeText(this, "单篇提问内容最多允许上传8个附件，请删减附件，谢谢", Toast.LENGTH_SHORT).show();
            return;
        }

        UserInfoModel userInfo = Assistant.getUserInfoByOrm(this);
        if (userInfo != null) {
            try {
                UserGUID = userInfo.getUserGUID();// ,area,type,name,title;
                // if (!VoiceToWord.STRAUDIO.isEmpty()) {
                // dxtContent.setText(dxtContent.getText() +
                // VoiceToWord.STRAUDIO);
                // }

                RevelationContent = URLEncoder.encode(dxtContent.getText()
                        .toString().trim(), "utf-8");
                leader = URLEncoder.encode(dxtLeader.getText().toString()
                        .trim(), "utf-8");
                area = dxtarea.getText().toString().trim();
                type = dxtType.getText().toString().trim();
                name = URLEncoder.encode(dxtName.getText().toString().trim(),
                        "utf-8");
                title = URLEncoder.encode(dxtTitle.getText().toString().trim(),
                        "utf-8");
                UserPhone = URLEncoder.encode(dxtPhone.getText().toString()
                        .trim(), "utf-8");
                UserInfoModel user = Assistant
                        .getUserInfoByOrm(this);

//				String check2 = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
//				Pattern regex = Pattern.compile(check2);
//				Matcher matcher = regex.matcher(UserPhone);
//				boolean isMatched = matcher.matches();

                if (title.equalsIgnoreCase("") || title == "") {
                    Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
                    return;
                }
//				if (leader.equalsIgnoreCase("") || leader == "") {
//					Toast.makeText(this, "请选择领导", 1).show();
//					return;
//				}

//				if (area.equalsIgnoreCase("") || area == "") {
//					Toast.makeText(this, "请选择领域", 1).show();
//					return;
//				}

//				if (type.equalsIgnoreCase("") || type == "") {
//					Toast.makeText(this, "请选择类型", 1).show();
//					return;
//				}

//                if (UserPhone.equalsIgnoreCase("") || UserPhone == "") {
//                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT)
//                            .show();
//                    return;
//                }
//				else if (!isMatched) {
//					Toast.makeText(this, "手机号码不正确", 1)
//							.show();
//					return;
//				}

//                if (name.equalsIgnoreCase("") || name == "") {
//                    Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (!RevelationContent.equalsIgnoreCase("")
                        && RevelationContent != "") {
                    imgList = new ArrayList<BaoliaoFileModel>();
                    videoList = new ArrayList<BaoliaoFileModel>();

                    for (int i = 0; i < fileList.size(); i++) {
                        progressMax = progressMax
                                + fileList.get(i).getFileSize();
                        if (fileList.get(i).getFileType()
                                .equalsIgnoreCase("image")) {
                            imgList.add(fileList.get(i));
                        } else {
                            videoList.add(fileList.get(i));
                        }
                    }

                    if (videoList.size() > 3) {
                        Toast.makeText(this, "提问上传视频最多不能超过3个", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (progressMax > Integer.MAX_VALUE) {
                        progressMax = progressMax / 1024;// 计算多少M
                        isM = true;
                    }
                    dx = progressMax / 100;
                    progressDialog = new ProgressDialog(this);
                    // 设置最大值为100
                    progressDialog.setMax(100);
                    // 设置进度条风格STYLE_HORIZONTAL
                    progressDialog
                            .setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("上传文件中...");
                    progressDialog.show();
                    if (fileList.size() > 0) { // 判断是否有媒体文件
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO 自动生成的方法存根
                                // 在这里执行你要想的操作 比如直接在这里更新ui或者调用回调在 在回调中更新ui
                                UploadFileConn con = new UploadFileConn(
                                        fileList, mHandler);
                                con.setListener(new ProgressListener() {
                                    @Override
                                    public void onProgress(long progress) {
                                        if (isM) {
                                            progressDialog
                                                    .setProgress((int) ((progress / 1024) / dx));
                                        } else {
                                            progressDialog
                                                    .setProgress((int) (progress / dx));
                                        }
                                    }

                                    @Override
                                    public void onUploadFinish(boolean finish) {
                                        if (finish) {
                                            progressDialog.cancel();
                                            isM = false;
                                        }
                                    }
                                });

                            }
                        }).start();
                    } else {

                        if (wenzheng_isnoname.isChecked()) {
                            isnoname = "True";
                        } else {
                            isnoname = "False";
                        }
                        if (!TextUtils.isEmpty(area)) {
                            PoliticsTypeOne = getIndexInType("1", area);
                            PoliticsTypeTwo = getIndexInType("2", type);
                        }


                        addWenZheng(this,
                                IndexAPI.CONTRI_ADD_API, API.STID,
                                title, RevelationContent, user.getUserGUID(),
                                "1",
                                img_url, video_url, "1", DateTool.getNowDate(), new Messenger(mHandler));
                    }

                } else {
                    Toast.makeText(this, "提问内容为空", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (UnsupportedEncodingException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        } else

        {
            Intent intent3 = new Intent();
            intent3.setAction(basePackage + "login");
            startActivity(intent3);
        }

    }

    /**
     * 作 者：李威 日 期： 2015-05-18 下午13:45:00 描 述： 新增我的问政
     */
    public void addWenZheng(Context paramContext, String url, String StID,
                            String PoliticsTitle, String PoliticsContent, String UserGUID,
                            String ResType,
                            String PicUrl, String VideoUrl,
                            String UploadType, String FileDate,
                            Messenger paramMessenger) {
        // =1&=1&=1&=1&=1&=20150122/
        Intent localIntent = new Intent(paramContext, IndexHttpService.class);
        localIntent.putExtra("api", IndexAPI.WENZHENG_ADD_API);
        localIntent.putExtra(IndexAPI.WENZHENG_ADD_MESSAGE, paramMessenger);
        localIntent.putExtra("url", url);
        localIntent.putExtra("StID", StID);
        localIntent.putExtra("Title", PoliticsTitle);
        localIntent.putExtra("ResourceContent", PoliticsContent);
        localIntent.putExtra("DepartmentID", DeptID);


//        localIntent.putExtra("UserPhone", UserPhone);
//        localIntent.putExtra("PoliticsType", PoliticsType);
//        localIntent.putExtra("PoliticsTypeOne", PoliticsTypeOne);
//        localIntent.putExtra("PoliticsTypeTwo", PoliticsTypeTwo);
//        localIntent.putExtra("IsNoName", IsNoName);
//        localIntent.putExtra("PoliticsAreaCode", PoliticsAreaCode);
//        localIntent.putExtra("UserRealName", UserRealName);
        localIntent.putExtra("PicPath", PicUrl);
        localIntent.putExtra("UserGUID", UserGUID);
        localIntent.putExtra("ResType", ResType);

        localIntent.putExtra("VideoUrl", VideoUrl);
        localIntent.putExtra("UploadType", UploadType);

        localIntent.putExtra("FileDate", FileDate);

        paramContext.startService(localIntent);
    }

    // 获得发表问政时选择类型或领域所对应的索引
    public String getIndexInType(String type, String str) {
        String[] strs;
        if ("1".equals(type)) {
            strs = getResources().getStringArray(R.array.PoliticsTypeOne);
        } else {
            strs = getResources().getStringArray(R.array.PoliticsTypeTwo);
        }

        for (int i = 0; i < strs.length; i++) {
            if (str.equals(strs[i])) {
                return String.valueOf(++i);
            }
        }

        return "";

    }

    // 图片 视频

    /**
     * 选择图片或拍摄图片
     */
    private void showPictureDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setItems(new String[]{"选择照片", "拍摄照片", "取消"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int position) {
                        switch (position) {
                            // 图片选择 回调 100
                            case 0:
                                Intent intent = new Intent();
                                intent.setAction(basePackage + "ImageBucket");
                                DataType = 1;
                                intent.putExtra("DataType", DataType);
                                startActivityForResult(intent, 100);
                                break;
                            // 拍摄图片 回调 1000
                            case 1:
                                try {
                                    Intent openCameraIntent = new Intent(
                                            MediaStore.ACTION_IMAGE_CAPTURE);// 弹出照相
                                    String name = "IMG_"
                                            + DateFormat
                                            .format("yyyyMMdd_hhmmss",
                                                    Calendar.getInstance(Locale.CHINA))
                                            + ".jpg"; // 图片名称
                                    File file = new File("/mnt/sdcard/DCIM/Camera");
                                    if (!file.exists()) { // 文件是否存在
                                        file.mkdirs();
                                    }
                                    img_path = file.getPath() + "/" + name; // 组成一个文件的路径
                                    listCphto.clear();
                                    listCphto.add(img_path);
                                    // Log.i("拍照时候输出 照片路径.........",listCphto.get(0)
                                    // +
                                    // "____listCphto");
                                    file = new File(img_path);
                                    Uri uri = Uri.fromFile(file);
                                    // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                                    openCameraIntent.putExtra(
                                            MediaStore.EXTRA_OUTPUT, uri);
                                    // openCameraIntent.setType("image/*");
                                    startActivityForResult(openCameraIntent, 1000); // 回调
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    imageAdapter.notifyDataSetChanged();
                                    Toast.makeText(ContributeActivity.this,
                                            "获取图片失败，请重新拍摄", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                dxtarea.setText(data.getExtras().getString("PoliticsTypeName"));
            } else if (requestCode == 2) {
                dxtType.setText(data.getExtras().getString("PoliticsTypeName"));
            } else if (requestCode == 3 && data != null) {
                cityParentId = data.getExtras().getString("cityParentId");
                dxtLeader.setText(data.getExtras().getString("cityName"));
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == 10010) { // 文件选择取选取的文件
                if (data != null) {

                    final Uri uri = data.getData();
                    try {
                        // Get the file path from the URI
                        final String strFileURL = FileUtils.getPath(this, uri);
                        // 视频路径
                        video_path = strFileURL;
                        // 视频文件后缀名
                        String prefix = strFileURL.substring(
                                strFileURL.lastIndexOf(".") + 1).toLowerCase();
                        if (prefix.equals("mp4") || prefix.equals("mov")
                                || prefix.equals("rm") || prefix.equals("rmvb")
                                || prefix.equals("wmx") || prefix.equals("avi")) {

                            String[] strArray = video_path.split("/");
                            String video_name = strArray[strArray.length - 1]; // 图片名称

                            BitmapFactory.Options bOptions = new BitmapFactory.Options();
                            bOptions.inJustDecodeBounds = true;
                            Bitmap bitmap = getVideoThumbnail(strFileURL, 400,
                                    300,
                                    MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图400,
                            // 300,

                            bOptions.inJustDecodeBounds = false;
                            bOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                            bitmap = getVideoThumbnail(strFileURL, 400, 300,
                                    MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图400,
                            // 300,
                            bitmap = Bitmap.createScaledBitmap(bitmap, 400,
                                    300, true); // 压缩大小
                            bitmap = CI.compressImage(bitmap); // 质量压缩
                            String img_path = CI.setnewPath(bitmap);

                            BaoliaoFileModel fileModel = new BaoliaoFileModel();
                            fileModel.setFileID(String.valueOf(System
                                    .currentTimeMillis()));
                            fileModel.setFileName(video_name);
                            fileModel.setFileUrl(video_path);
                            fileModel.setFileImagUrl(img_path);
                            fileModel.setFileType("video");
                            fileList.add(fileModel);
                            imageAdapter.setIsDel(0);
                            imageAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(
                                    this,
                                    "请选择文件格式为: mp4,mov,avi,rm,rmvb,wmx的格式视频，谢谢。",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                    }
                }
            }

            if (requestCode == 1000) { // 相机拍摄图片
                try {
                    if (Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        BitmapFactory.Options bOptions = new BitmapFactory.Options();
                        bOptions.inJustDecodeBounds = true;
                        Bitmap bitmap = BitmapFactory.decodeFile(
                                listCphto.get(0), bOptions);
                        int h = bOptions.outHeight;
                        int w = bOptions.outWidth;
                        bOptions.inJustDecodeBounds = false;
                        bOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                        bitmap = BitmapFactory.decodeFile(listCphto.get(0),
                                bOptions);// 得到原始图片

                        Log.i("压缩后的图片", "....。路径。。。。：" + img_path);

                        String[] strArray = img_path.split("/");
                        img_name = strArray[strArray.length - 1]; // 图片名称

                        String[] strArray1 = img_name.split("[.]");
                        img_name = DateTool.getDate() + "." + strArray1[1];

                        if (img_path.indexOf("mnt/sdcard") != -1) {
                            if (!new File(img_path).exists()) {
                                img_path.replaceFirst("/mnt/sdcard",
                                        "/storage/sdcard0");
                            }
                        }

                        BaoliaoFileModel fileModel = new BaoliaoFileModel();
                        fileModel.setFileID(String.valueOf(System
                                .currentTimeMillis()));
                        Log.i("压缩后的图片", "....。img_name。。。。：" + img_name);
                        fileModel.setFileName(img_name);
                        fileModel.setFileUrl(img_path);
                        fileModel.setFileImagUrl(img_path);
                        fileModel.setFileType("image");
                        fileList.add(fileModel);
                        imageAdapter.setIsDel(0);
                        imageAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    imageAdapter.setIsDel(0);
                    imageAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "获取图片失败，请重新拍摄",
                            Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 100) { // 相册图片

                List<String> imgList = new ArrayList<String>();
                imgList = data.getStringArrayListExtra("selected_list");
                for (int i = 0; i < imgList.size(); i++) {
                    img_path = imgList.get(i); // 图片 路径

                    String[] strArray = img_path.split("/");
                    img_name = strArray[strArray.length - 1]; // 图片名称
                    String[] strArray1 = img_name.split("[.]");
                    img_name = DateTool.getDate() + "." + strArray1[1];

                    if (img_path.indexOf("mnt/sdcard") != -1) {
                        if (!new File(img_path).exists()) {
                            img_path.replaceFirst("/mnt/sdcard",
                                    "/storage/sdcard0");
                        }
                    }

                    BaoliaoFileModel fileModel = new BaoliaoFileModel();
                    fileModel.setFileID(String.valueOf(System
                            .currentTimeMillis()));
                    fileModel.setFileName(img_name);
                    fileModel.setFileUrl(img_path);
                    fileModel.setFileImagUrl(img_path);
                    fileModel.setFileType("image");
                    fileList.add(fileModel);
                }
                imageAdapter.setIsDel(0);
                imageAdapter.notifyDataSetChanged();
                // 使用ContentProvider通过URI获取原始图片
            } else if (requestCode == 200) { // 选择视频
                ContentResolver resolver = getContentResolver();
                // 照片的原始资源地址
                Uri originalUri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA}; // / 将URI
                // 转换成
                // 存储路径
                Cursor cursor = managedQuery(originalUri, proj, null, null,
                        null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA); // 图片的
                // ID
                cursor.moveToFirst();
                video_path = cursor.getString(column_index); // 视频的路径 根据ID得到视频路径
                File file = new File(video_path);
                long fileSize = 0;
                if (file != null) {
                    fileSize = file.length();
                }
                String video_name = "";
                String[] strArray = video_path.split("/"); // 視頻名稱
                video_name = strArray[strArray.length - 1];
                /* Log.i("<<<<<<<<<video_path<<<<<<<<<<<", video_path); */
                BitmapFactory.Options bOptions = new BitmapFactory.Options();
                bOptions.inJustDecodeBounds = true;
                Bitmap bitmap = getVideoThumbnail(video_path, 190, 157,
                        MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图174,
                // 144,

                bOptions.inJustDecodeBounds = false;
                bOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                bitmap = getVideoThumbnail(video_path, 190, 157,
                        MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图174,
                // 144,
                bitmap = Bitmap.createScaledBitmap(bitmap, 174, 144, true); // 压缩大小
                bitmap = CI.compressImage(bitmap); // 质量压缩
                img_path = CI.setnewPath(bitmap);

                String[] strArray1 = img_path.split("/");
                img_name = strArray[strArray.length - 1]; // 图片名称
                String[] strArray2 = img_name.split("[.]");
                img_name = DateTool.getDate() + "." + strArray2[1];
                if (img_path.indexOf("mnt/sdcard") != -1) {
                    if (!new File(img_path).exists()) {
                        img_path.replaceFirst("/mnt/sdcard", "/storage/sdcard0");
                    }
                }

                BaoliaoFileModel fileModel = new BaoliaoFileModel();
                fileModel.setFileID(String.valueOf(System.currentTimeMillis()));
                Log.i("tag", img_name);
                fileModel.setFileName(img_name);
                fileModel.setFileUrl(video_path);
                fileModel.setFileImagUrl(img_path);
                fileModel.setFileType("video");
                fileList.add(fileModel);
                imageAdapter.setIsDel(0);
                imageAdapter.notifyDataSetChanged();
            } else if (requestCode == 2000) { // 拍摄视频

                // 拍摄视频
                Log.i("tag", "拍摄视频视频");
                video_path = data.getStringExtra("path");
                Log.i("video_path", video_path);
                Bitmap bitmap = getVideoThumbnail(video_path, 400, 300,
                        MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图
                // 获取視頻名称
                String[] strArray = video_path.split("/"); // 視頻名稱;
                bitmap = CI.compressImage(bitmap); // 质量压缩
                img_path = CI.setnewPath(bitmap);

                String[] strArray1 = img_path.split("/");
                img_name = strArray[strArray.length - 1]; // 图片名称
                String[] strArray2 = img_name.split("[.]");
                img_name = DateTool.getDate() + "." + strArray2[1];
                if (img_path.indexOf("mnt/sdcard") != -1) {
                    if (!new File(img_path).exists()) {
                        img_path.replaceFirst("/mnt/sdcard", "/storage/sdcard0");
                    }
                }
                String str = img_path;
                BaoliaoFileModel fileModel = new BaoliaoFileModel();
                fileModel.setFileID(String.valueOf(System.currentTimeMillis()));
                Log.i("tag", video_path + "!");
                fileModel.setFileName(img_name);
                fileModel.setFileUrl(video_path);
                fileModel.setFileImagUrl(img_path);
                fileModel.setFileType("video");
                fileList.add(fileModel);
                imageAdapter.setIsDel(0);
                imageAdapter.notifyDataSetChanged();

            } else if (requestCode == REQUEST_CODE_PICK) {
                // 拍摄视频
                Log.i("tag", "拍摄视频视频1231313");
                // final TextView text_yasuo = (TextView)
                // findViewById(R.id.text_yasuo);
                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

                final File file;
                if (resultCode == RESULT_OK) {
                    try {
                        file = File.createTempFile("video_temp_name", ".mp4",
                                getExternalFilesDir(null));
                    } catch (IOException e) {
                        Toast.makeText(this, "不能创建临时文件.", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    ContentResolver resolver2 = getContentResolver();
                    final ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = resolver2.openFileDescriptor(
                                data.getData(), "r");
                    } catch (FileNotFoundException e) {
                        Log.w("文件不能打开 '" + data.getDataString() + "'", e);
                        Toast.makeText(this, "视频文件未找到.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    final FileDescriptor fileDescriptor = parcelFileDescriptor
                            .getFileDescriptor();
                    progressBar.setMax(1000);
                    final long startTime = SystemClock.uptimeMillis();
                    MediaTranscoder.Listener listener = new MediaTranscoder.Listener() {
                        @Override
                        public void onTranscodeProgress(double progress) {
                            if (progress < 0) {
                                progressBar.setIndeterminate(true);
                            } else {
                                progressBar.setIndeterminate(false);
                                progressBar.setProgress((int) Math
                                        .round(progress * 1000));
                            }
                        }

                        public void onTranscodeCompleted() {
                            // Log.d(TAG, "转换花费： " + (SystemClock.uptimeMillis()
                            // - startTime) + "ms");
                            ResultFilePath = file.toString();
                            // Toast.makeText(Upload_file.this, "转码完成后存放 " +
                            // ResultFilePath, Toast.LENGTH_LONG).show();
                            // findViewById(R.id.select_video_button).setEnabled(true);
                            progressBar.setIndeterminate(false);
                            progressBar.setProgress(1000);
                            // text_yasuo.setText("压缩完成");
                            startActivity(new Intent(Intent.ACTION_VIEW)
                                    .setDataAndType(Uri.fromFile(file),
                                            "video/mp4"));

                            Log.i("ResultFilePath", ResultFilePath);
                            Bitmap bitmap = getVideoThumbnail(ResultFilePath,
                                    190, 157,
                                    MediaStore.Images.Thumbnails.MICRO_KIND); // 获得视频的缩略图
                            // 获取視頻名称
                            String[] strArray = ResultFilePath.split("/"); // 視頻名稱;
                            bitmap = CI.compressImage(bitmap); // 质量压缩
                            img_path = CI.setnewPath(bitmap);

                            String[] strArray1 = img_path.split("/");
                            img_name = strArray[strArray.length - 1]; // 图片名称
                            String[] strArray2 = img_name.split("[.]");
                            img_name = DateTool.getDate() + "." + strArray2[1];
                            if (img_path.indexOf("mnt/sdcard") != -1) {
                                if (!new File(img_path).exists()) {
                                    img_path.replaceFirst("/mnt/sdcard",
                                            "/storage/sdcard0");
                                }
                            }
                            File file = new File(ResultFilePath);
                            long fileSize = 0;
                            if (file != null) {
                                fileSize = file.length();
                            }
                            BaoliaoFileModel fileModel = new BaoliaoFileModel();
                            fileModel.setFileID(String.valueOf(System
                                    .currentTimeMillis()));
                            fileModel.setFileName(System.currentTimeMillis()
                                    + img_name);
                            fileModel.setFileUrl(ResultFilePath);
                            fileModel.setFileImagUrl(img_path);
                            fileModel.setFileType("video");
                            fileList.add(fileModel);

                            imageAdapter.setIsDel(0);
                            imageAdapter.notifyDataSetChanged();

                            try {
                                parcelFileDescriptor.close();
                            } catch (IOException e) {
                                Log.w("Error ", e);
                            }
                        }

                        public void onTranscodeFailed(Exception exception) {
                            progressBar.setIndeterminate(false);
                            progressBar.setProgress(0);
                            // findViewById(R.id.select_video_button).setEnabled(true);
                            Toast.makeText(ContributeActivity.this,
                                    exception.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                            try {
                                parcelFileDescriptor.close();
                            } catch (IOException e) {
                                Log.w("文件关闭错误 ： ", e);
                            }
                        }
                    };
                    Log.d("tag", "转码成 " + file);
                    MediaTranscoder.getInstance().transcodeVideo(
                            fileDescriptor,
                            file.getAbsolutePath(),
                            MediaFormatStrategyPresets
                                    .createAndroid720pStrategy(), listener);
                    // findViewById(R.id.select_video_button).setEnabled(false);

                }
            }

        }
    }

    // 获取视频的缩略图
    private Bitmap getVideoThumbnail(String videoPath, int width, int height,
                                     int kind) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        System.out.println("w" + bitmap.getWidth());
        System.out.println("h" + bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    // 解决点击其他地方输入框隐藏
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    // 分享视频
    private void showVideoDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setItems(new String[]{"选择视频", "拍摄视频", "取消"},
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int position) {
                        switch (position) {
                            case 0:
                                String banben = CommonTools.getPhoneBanben();// 获得手机版本号
                                String s1 = null;
                                String s2 = null;
                                int a = 0;
                                int b = 0;
                                if (banben.indexOf(".") != -1) {
                                    s2 = banben.substring(0, banben.indexOf("."));
                                    s1 = banben.substring(banben.indexOf(".") + 1,
                                            banben.indexOf(".") + 2);
                                }

                                if (s1 != null && !"".equals(s1)) {
                                    a = Integer.parseInt(s2);
                                    b = Integer.parseInt(s1);
                                } else if (s2 != null && !"".equals(s2)) {
                                    a = Integer.parseInt(s2);
                                }

                                String phoneBrand = android.os.Build.BRAND;// 获取
                                // 手机品牌
                                // 如果版本号为4.3以上就对视频进行压缩三星手机除外
                                if (phoneBrand.equals("samsung")) {

                                    Intent target = FileUtils
                                            .createGetContentIntent();
                                    // Create the chooser Intent
                                    Intent iVideo = Intent.createChooser(target,
                                            "选择视频文件");
                                    try {
                                        startActivityForResult(iVideo, REQUEST_CODE);
                                    } catch (ActivityNotFoundException e) {
                                        // The reason for the existence of
                                        // aFileChooser
                                    }
                                } else if ((a >= 5) || (a >= 4 && b >= 3)) {
                                    // Upload_file.this.startActivityForResult(new
                                    // Intent(Intent.ACTION_GET_CONTENT).setType("video/*"),
                                    // REQUEST_CODE_PICK);
                                    Intent videoIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    videoIntent.setType("video/*"); // 获取视频文件
                                    startActivityForResult(videoIntent,
                                            REQUEST_CODE);

                                } else {

                                    Intent videoIntent = new Intent(
                                            Intent.ACTION_GET_CONTENT);
                                    videoIntent.setType("video/*"); // 获取视频文件
                                    startActivityForResult(videoIntent, 200);
                                }
                                break;
                            case 1:

                                Intent intent = new Intent(
                                        ContributeActivity.this,
                                        FFmpegRecorderActivity.class);
                                startActivityForResult(intent, 2000);

                                break;
                        }
                    }
                });
        builder.create().show();
    }

    public ArrayList<BaoliaoFileModel> getFileList() {
        return fileList;
    }

    public void setFileList(int position) {
        // this.fileList.remove(position);
        imageAdapter.notifyDataSetChanged();
    }

}
