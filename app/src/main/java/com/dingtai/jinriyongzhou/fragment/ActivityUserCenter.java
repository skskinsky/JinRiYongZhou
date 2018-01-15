package com.dingtai.jinriyongzhou.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.model.UserCollects;
import com.dingtai.base.model.UserInfoModel;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.CommentUtils;
import com.dingtai.base.view.CircularImage;
import com.dingtai.base.view.MyGridView;
import com.dingtai.base.view.ZDYProgressDialog;
import com.dingtai.dtusercenter.activity.CalendarActivity;
import com.dingtai.dtusercenter.activity.ChangePwdActivity;
import com.dingtai.dtusercenter.activity.CollectsActivity;
import com.dingtai.dtusercenter.activity.Edit_UserInfoActivity;
import com.dingtai.dtusercenter.activity.InviteUserSearchActivity;
import com.dingtai.dtusercenter.activity.PushListActivity;
import com.dingtai.dtusercenter.activity.RegisterUserScoreTask;
import com.dingtai.dtusercenter.activity.StoreActivity;
import com.dingtai.dtusercenter.activity.UploadHeadImgActivity;
import com.dingtai.dtusercenter.config.ShareConfig1;
import com.dingtai.dtusercenter.model.SettingBeean;
import com.dingtai.dtusercenter.service.UserCenterService;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.MyInvitationCodeActivity;
import com.dingtai.reporter.activity.MyReporterActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import activity.ActivityWeather;

/**
 * Created by Administrator on 2017/10/27 0027.
 */

public class ActivityUserCenter extends BaseFragment implements View.OnClickListener {
    private TextView mTvUserName;
    private TextView mTvLoginType;
    private TextView userinfo_gold;
    private CircularImage mUserIcon;
    private SettingBeean info_bean;
    private List<SettingBeean> list;
    private SharedPreferences sp;
    private UserInfoModel info;
    private RuntimeExceptionDao<UserInfoModel, String> user_mode;
    private Dialog mDialog;
    private ZDYProgressDialog dialog;
    private boolean isFirst;
    private ImageButton btn_setting;
    private TextView exit_login;
    private View view;

    ImageButton command_return;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 444:
                    Toast.makeText(ActivityUserCenter.this.getActivity(), "未开启数据流量", Toast.LENGTH_LONG).show();
                    break;
                case 556:
                    try {
                        ActivityUserCenter.this.info_bean = (SettingBeean) msg.getData().getSerializable("obj");
                        if (ActivityUserCenter.this.info_bean != null) {
                            ActivityUserCenter.this.userinfo_gold.setText(ActivityUserCenter.this.info_bean.UserScore);
                            CommentUtils.setCommentUserName(ActivityUserCenter.this.mTvUserName, ActivityUserCenter.this.info_bean.UserNickName, ActivityUserCenter.this.info_bean.UserName);
                            ActivityUserCenter.this.info = Assistant.getUserInfoByOrm(ActivityUserCenter.this.getActivity());
                            ActivityUserCenter.this.info.setUserScore(ActivityUserCenter.this.info_bean.UserScore);
                            ActivityUserCenter.this.user_mode.update(ActivityUserCenter.this.info);
                        }

                        DisplayImageOptions e = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.dt_standard_index_nav_right_photo).showImageForEmptyUri(R.drawable.dt_standard_index_nav_right_photo).showImageOnFail(R.drawable.dt_standard_index_nav_right_photo).build();
                        ImageLoader.getInstance().displayImage(ActivityUserCenter.this.info_bean.UserIcon, ActivityUserCenter.this.mUserIcon, e);
                        ActivityUserCenter.this.isFirst = false;
                    } catch (Exception var3) {
                        var3.printStackTrace();
                    }
                case 1:

                    break;
            }

        }
    };
    private Handler handler = new Handler() {
    };

    public ActivityUserCenter() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_user_center_new, (ViewGroup) null);
        this.initView(this.view);
        return this.view;
    }

    private void initView(View view) {
        this.isFirst = true;

        this.btn_setting = (ImageButton) view.findViewById(R.id.command_more);
        this.btn_setting.setImageDrawable(getResources().getDrawable(R.drawable.dt_center_setting));
        this.btn_setting.setVisibility(View.VISIBLE);
        this.btn_setting.setOnClickListener(this);
        view.findViewById(R.id.usercenter_dingyueguanli).setOnClickListener(this);
        view.findViewById(R.id.usercenter_gywm).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(ActivityUserCenter.this.basePackage + "aboutus");
                ActivityUserCenter.this.startActivity(intent);
            }
        });
        view.findViewById(R.id.usercenter_yuedurili).setOnClickListener(this);
        view.findViewById(R.id.usercenter_mycollect).setOnClickListener(this);
        view.findViewById(R.id.usercenter_myhuzhux).setOnClickListener(this);
        view.findViewById(R.id.usercenter_mybaoliao).setOnClickListener(this);
        view.findViewById(R.id.usercenter_userstore).setOnClickListener(this);
        view.findViewById(R.id.usercenter_updateinfo).setOnClickListener(this);
        view.findViewById(R.id.usercenter_mygift).setOnClickListener(this);
        view.findViewById(R.id.usercenter_updatepwd).setOnClickListener(this);
        view.findViewById(R.id.usercenter_userscore).setOnClickListener(this);
        view.findViewById(R.id.usercenter_usergift).setOnClickListener(this);
        view.findViewById(R.id.usercenter_userrili).setOnClickListener(this);
        view.findViewById(R.id.usercenter_myyqm).setOnClickListener(this);
        view.findViewById(R.id.usercenter_my_inviteuser).setOnClickListener(this);
        view.findViewById(R.id.usercenter_mygenzong).setOnClickListener(this);
        view.findViewById(R.id.usercenter_register).setOnClickListener(this);
        view.findViewById(R.id.usercenter_subscription).setOnClickListener(this);
        view.findViewById(R.id.tv_saoys).setOnClickListener(this);
        view.findViewById(R.id.tv_collect).setOnClickListener(this);
        view.findViewById(R.id.tv_weather).setOnClickListener(this);
        view.findViewById(R.id.tv_xiaoxi).setOnClickListener(this);
        this.exit_login = (TextView) view.findViewById(R.id.exit_login);
        this.exit_login.setOnClickListener(this);

        this.mTvUserName = (TextView) view.findViewById(R.id.usercenter_username);
        this.mTvLoginType = (TextView) view.findViewById(R.id.usercenter_logintype);
        this.mUserIcon = (CircularImage) view.findViewById(R.id.usercenter_usericon);
        this.mUserIcon.setOnClickListener(this);
        this.userinfo_gold = (TextView) view.findViewById(R.id.userinfo_gold);
        this.showShare();
        View usercenter_myhuzhu = view.findViewById(R.id.usercenter_myhuzhu);
        if (usercenter_myhuzhu != null) {
            usercenter_myhuzhu.setOnClickListener(this);
        }

        View usercenter_myactive = view.findViewById(R.id.usercenter_myactive);
        if (usercenter_myhuzhu != null) {
            usercenter_myactive.setOnClickListener(this);
        }

        View usercenter_mydingdan = view.findViewById(R.id.usercenter_mydingdan);
        if (usercenter_myhuzhu != null) {
            usercenter_mydingdan.setOnClickListener(this);
        }

        View usercenter_download = view.findViewById(R.id.usercenter_download);
        if (usercenter_download != null) {
            usercenter_download.setOnClickListener(this);
        }

    }

    public void onPause() {
        super.onPause();
    }

    public void onResume() {
        super.onResume();
        if (this.isFirst) {
            this.getUserInfo();
        } else if (this.user_mode.queryForAll().size() != 0 && !this.isFirst) {
            if (Assistant.IsContectInterNet2(this.getActivity())) {
                this.getData();
            }
        } else if (this.user_mode.queryForAll().size() == 0 && !this.isFirst) {
            this.mTvUserName.setText("......");
            this.mTvLoginType.setText("请登录");
            this.userinfo_gold.setText("0");
            DisplayImageOptions options = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.dt_standard_index_nav_right_photo).showImageForEmptyUri(R.drawable.dt_standard_index_nav_right_photo).showImageOnFail(R.drawable.dt_standard_index_nav_right_photo).build();
            ImageLoader.getInstance().displayImage((String) null, this.mUserIcon, options);
        }

        if (Assistant.getUserInfoByOrm(this.getActivity()) != null) {
            this.exit_login.setVisibility(View.VISIBLE);
        } else {
            this.exit_login.setVisibility(View.GONE);
        }

    }

    private void getData() {
        String Url = API.COMMON_URL + "Interface/RegisterAPI.ashx?action=GetUserInfo&UserGUID=" + ((UserInfoModel) this.user_mode.queryForAll().get(0)).getUserGUID();
        this.requestData(this.getActivity(), Url, new Messenger(this.mHandler), 879, "setting_info", UserCenterService.class);
    }

    private void getUserInfo() {
        this.user_mode = this.getHelper().getMode(UserInfoModel.class);
        Object u = new ArrayList();
        if (this.user_mode.isTableExists()) {
            u = this.user_mode.queryForAll();
        }

        if (((List) u).size() > 0) {
            this.info = (UserInfoModel) ((List) u).get(0);
        } else {
            this.info = null;
            this.mTvUserName.setText("......");
            this.mTvLoginType.setText("请登录");
            this.userinfo_gold.setText("0");
            DisplayImageOptions mode = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.dt_standard_index_nav_right_photo).showImageForEmptyUri(R.drawable.dt_standard_index_nav_right_photo).showImageOnFail(R.drawable.dt_standard_index_nav_right_photo).build();
            ImageLoader.getInstance().displayImage((String) null, this.mUserIcon, mode);
        }

        if (this.info != null) {
            this.userinfo_gold.setText(this.info.getUserScore());
            CommentUtils.setCommentUserName(this.mTvUserName, this.info.getUserNickName(), this.info.getUserName());
            String mode1 = this.info.getLoginMode();
            if ("1".equals(mode1)) {
                this.mTvLoginType.setText("通过QQ登录");
            } else if ("2".equals(mode1)) {
                this.mTvLoginType.setText("通过新浪微博登录");
            } else if ("3".equals(mode1)) {
                this.mTvLoginType.setText("通过微信登录");
            } else if ("4".equals(mode1)) {
                this.mTvLoginType.setText("通过注册登录");
            }

            DisplayImageOptions options = (new DisplayImageOptions.Builder()).cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.NONE).bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.dt_standard_index_nav_right_photo).showImageForEmptyUri(R.drawable.dt_standard_index_nav_right_photo).showImageOnFail(R.drawable.dt_standard_index_nav_right_photo).build();
            ImageLoader.getInstance().displayImage(this.info.getUserIcon(), this.mUserIcon, options);
            this.isFirst = false;
        }

    }

    private void logoff() {
        this.isFirst = true;
        if (this.user_mode.isTableExists()) {
            this.info_bean = null;
            this.user_mode.delete(this.user_mode.queryForAll());
            this.sp = this.getActivity().getSharedPreferences("UserInfo", 0);
            this.sp.edit().putString("userguid", "").commit();
            this.sp.edit().putString("token", "").commit();
            this.sp.edit().putString("username", "").commit();
        }

        RuntimeExceptionDao collects = this.getHelper().getMode(UserCollects.class);
        if (collects != null && collects.isTableExists() && collects.queryForAll() != null) {
            collects.delete(collects.queryForAll());
        }

        if (this.dialog != null) {
            this.dialog.dismissDialog();
            this.dialog = null;
        }

        Toast.makeText(this.getActivity(), "注销完成！", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setAction(this.basePackage + "login");
        this.startActivity(intent);
    }

    public void onClick(View view) {
        Intent intent;
        if (view.getId() == R.id.command_more) {
            intent = new Intent();
            intent.setAction(this.basePackage + "set");
            this.startActivity(intent);
        } else if (view.getId() == R.id.tv_saoys) {
            intent = new Intent();
            intent.setAction(this.basePackage + "scan");
            this.startActivity(intent);
        } else if (view.getId() == R.id.tv_weather) {
            this.startActivity(new Intent(this.getActivity(), ActivityWeather.class));
        } else {
            if (Assistant.getUserInfoByOrm(this.getActivity()) != null) {
                intent = new Intent();
                int i = view.getId();
                if (i == R.id.usercenter_usericon) {
                    this.startActivity(new Intent(this.getActivity(), UploadHeadImgActivity.class));
                } else if (i == R.id.usercenter_download) {
                    intent = new Intent();
                    intent.setAction(this.basePackage + "downloadActivity");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_usergift) {
                    this.startActivity(new Intent(this.getActivity(), PushListActivity.class));
                } else if (i == R.id.usercenter_userrili) {
                    intent.setAction(this.basePackage + "scan");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_yuedurili) {
                    this.startActivity(new Intent(this.getActivity(), CalendarActivity.class));
                } else if (i == R.id.usercenter_dingyueguanli) {
                    intent.setAction(this.basePackage + "myreporter");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_mygenzong) {
                    intent.setAction(this.basePackage + "mytheme");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_mybaoliao) {
                    intent.setAction(this.basePackage + "mybaoliao");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_myhuzhux) {
                    intent.setAction(this.basePackage + "MyMutual");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_register) {
                    intent.setAction(this.basePackage + "MsgYanzheng");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_subscription) {
                    intent.setAction(this.basePackage + "reporternews");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_userstore) {
                    this.startActivity(new Intent(this.getActivity(), StoreActivity.class));
                } else if (i == R.id.usercenter_mycollect) {
                    this.startActivity(new Intent(this.getActivity(), MyReporterActivity.class));
                } else if (i == R.id.usercenter_mygift) {
                    intent.setAction(this.basePackage + "mygift");
                    this.startActivity(intent);
                } else if (i == R.id.usercenter_updateinfo) {
                    this.startActivity(new Intent(this.getActivity(), Edit_UserInfoActivity.class));
                } else if (i == R.id.usercenter_updatepwd) {
//                    Intent xiugai = new Intent();
//                    xiugai.setAction(this.basePackage + "xiugai");
//                    xiugai.putExtra("xiugai", "修改密码");
                    Intent xiugai = new Intent(getActivity(),
                            ChangePwdActivity.class);
                    this.startActivity(xiugai);
                } else if (i == R.id.usercenter_userscore) {
                    this.startActivity(new Intent(this.getActivity(), RegisterUserScoreTask.class));
                } else if (i == R.id.usercenter_myhuzhu) {
                    intent.setAction(this.basePackage + "MyMutual");
                    this.startActivity(intent);
                } else if (i != R.id.usercenter_myactive) {
                    if (i == R.id.usercenter_mydingdan) {
                        intent.setAction(this.basePackage + "myOrder");
                        this.startActivity(intent);
                    } else if (i == R.id.exit_login) {
                        this.dialog = new ZDYProgressDialog(this.getActivity());
                        this.dialog.createDialog("正在注销...");
                        this.dialog.showDialog();
                        this.logoff();
                    } else if (i == R.id.usercenter_myyqm) {
                        intent = new Intent(this.getActivity(), MyInvitationCodeActivity.class);
                        intent.putExtra("code", Assistant.getUserInfoByOrm(this.getActivity()).getInviteCode());
                        this.startActivity(intent);
                    } else if (i != R.id.command_return && i == R.id.usercenter_my_inviteuser) {
                        this.startActivity(new Intent(this.getActivity(), InviteUserSearchActivity.class));
                    } else if (i == R.id.tv_xiaoxi) {
                        this.startActivity(new Intent(this.getActivity(), PushListActivity.class));
                    } else if (i == R.id.tv_collect) {
                        this.startActivity(new Intent(this.getActivity(), CollectsActivity.class));
                    }
                }
            } else {
                Toast.makeText(this.getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
                intent = new Intent();
                intent.setAction(this.basePackage + "login");
                this.startActivity(intent);
            }

        }
    }

    private void showShare() {
        View share = this.view.findViewById(R.id.gvShare);
        if (share != null) {
            MyGridView gvShare = (MyGridView) share;
            ArrayList lstImageItem = new ArrayList();

            for (int saImageItems = 0; saImageItems < ShareConfig1.SHARE_NAME.length; ++saImageItems) {
                HashMap map = new HashMap();
                map.put("ItemImage", Integer.valueOf(ShareConfig1.SHARE_ICON[saImageItems]));
                map.put("ItemText", ShareConfig1.SHARE_NAME[saImageItems]);
                lstImageItem.add(map);
            }

            SimpleAdapter var6 = new SimpleAdapter(this.getActivity(), lstImageItem, R.layout.index_drawer_gridview_list_item, new String[]{"ItemImage", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemText});
            gvShare.setAdapter(var6);
            gvShare.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    String ShareName = API.ShareName;
                    String URL = API.ShareURL;
                    String ShareCotent = Html.fromHtml(API.shareContent).toString();
                    BaseShare bs = new BaseShare(ActivityUserCenter.this.getActivity(), ShareName, ShareCotent, URL, "", "99", "");
                    switch (position) {
                        case 0:
                            bs.ShareFriends();
                            break;
                        case 1:
                            bs.ShareWeiXin();
                            break;
                        case 2:
                            bs.ShareQQZone();
                            break;
                        case 3:
                            bs.ShareQQ();
                            break;
                        case 4:
                            bs.shareShortMessage();
                    }

                }
            });
        }

    }

    void showImagePicker() {
        this.mDialog = new Dialog(this.getActivity(), R.style.Dialog_Fullscreen);
        this.mDialog.setContentView(R.layout.dialog_img_choose);
        this.mDialog.findViewById(R.id.camera).setOnClickListener(this);
        this.mDialog.findViewById(R.id.pic).setOnClickListener(this);
        this.mDialog.findViewById(R.id.cancel).setOnClickListener(this);
        this.mDialog.findViewById(R.id.dialog_layout).setOnClickListener(this);
        this.mDialog.show();
    }

}