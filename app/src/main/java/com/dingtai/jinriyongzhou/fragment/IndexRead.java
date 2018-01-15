package com.dingtai.jinriyongzhou.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.database.DataBaseHelper;
import com.dingtai.base.share.BaseShare;
import com.dingtai.base.view.MyWebChromeClient;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.newslib3.model.ParentChannelModel;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 读报
 *
 * @author 徐峰
 */
public class IndexRead extends BaseFragment implements OnClickListener {

    private WebView webView = null;
    private static String PageUrl;
    private String url = PageUrl;
    private MyWebChromeClient chromeClient;
    private View mMainView;
    private ImageView bt_share;
    private boolean isShare = false;
    private String ShareURL;
    private String currentUrl;
    private ImageView iv_shuaxin;
    RuntimeExceptionDao<ParentChannelModel, String> dao_partent_channel;
    private DataBaseHelper databaseHelper;
    private boolean isIntercept = false;

    public static IndexRead newInstance(String pageUrl) {

        Bundle args = new Bundle();
        args.putString("pageUrl", pageUrl);
        IndexRead fragment = new IndexRead();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.activity_read, container, false);
        initview();
        getData();
        return mMainView;
    }

    public DataBaseHelper getHelper() {
        if (this.dataHelper == null) {
            this.dataHelper = (DataBaseHelper) OpenHelperManager.getHelper(this.getContext(), DataBaseHelper.class);
        }

        return this.dataHelper;
    }

    private void getData() {

        dao_partent_channel = getHelper().getMode(ParentChannelModel.class);
        List<ParentChannelModel> list = dao_partent_channel
                .queryForAll();
        for (int i = 0; i < list.size(); i++) {
            ParentChannelModel parentChannelModel = list.get(i);
            if (parentChannelModel != null && !TextUtils.isEmpty(
                    parentChannelModel.getChannelName()) && "报纸".equals(parentChannelModel.getChannelName()) && !TextUtils.isEmpty(parentChannelModel.getChannelUrl())) {
                PageUrl = parentChannelModel.getChannelUrl();
            }
        }
//        Bundle arguments = getArguments();
//        PageUrl = arguments.getString("pageUrl", "");
        url = PageUrl;
        ShareURL = PageUrl;
        currentUrl = PageUrl;
    }

    public void loading() {
        if (!PageUrl.equals(ShareURL)) {
            webView.clearCache(true);
            webView.loadUrl(PageUrl);
            ShareURL = PageUrl;

        } else {

        }

        IndexNewsActivity2 fragment = (IndexNewsActivity2) getParentFragment();
        fragment.viewpager.requestDisallowInterceptTouchEvent(false);
        fragment.viewpager.setScroll(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        XGPushClickedResult click = XGPushManager
                .onActivityStarted(getActivity());
        if (click != null) {
            // Toast.makeText(this, click.toString(), 1).show();
            String customContent = click.getCustomContent();
            // 获取自定义key-value
            if (customContent != null && customContent.length() != 0) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    // key1为前台配置的key
                    if (!obj.isNull("PageUrl")) {
                        PageUrl = obj.getString("PageUrl");
                    }
                    // ...
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initview() {
        // TODO 自动生成的方法存根

        mMainView.findViewById(R.id.iv_left).setOnClickListener(this);
        mMainView.findViewById(R.id.iv_right).setOnClickListener(this);
        mMainView.findViewById(R.id.iv_refresh).setOnClickListener(this);

        webView = (WebView) mMainView.findViewById(R.id.wvCommon);
        bt_share = (ImageView) mMainView.findViewById(R.id.bt_share);
        iv_shuaxin = (ImageView) mMainView.findViewById(R.id.iv_shuaxin);
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        chromeClient = new MyWebChromeClient();
//		webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                ShareURL = url;
                currentUrl = url;
                view.loadUrl(url);
                IndexRead.this.url = url;
                IndexNewsActivity2 fragment = (IndexNewsActivity2) getParentFragment();

                fragment.viewpager.requestDisallowInterceptTouchEvent(true);

                if (url != null && url.equals(PageUrl)) {
                    isIntercept = false;
                    fragment.viewpager.setScroll(true);
                } else {
                    fragment.viewpager.setScroll(false);
                    isIntercept = true;
                }

//                if (url != null && url.equals(PageUrl)) {
//                    isIntercept = true;
//                } else
//                    isIntercept = true;


                return true;
            }
        });
        bt_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                isShare = true;
                BaseShare bs = null;
                try {
                    bs = new BaseShare(getActivity(), chromeClient.getTitle(),
                            "", ShareURL, "", "99", "");
                    bs.oneShare();
                } catch (Exception e) {
                }
            }
        });
        // 加载需要显示的网页
        webView.loadUrl(PageUrl); // 加载网页


        iv_shuaxin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
            }
        });

//
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                IndexNewsActivity2 fragment = (IndexNewsActivity2) getParentFragment();
//                if (event.getAction() != MotionEvent.ACTION_DOWN && isIntercept) {
//                    fragment.viewpager.requestDisallowInterceptTouchEvent(true);
//                    fragment.viewpager.setScroll(false);
//                } else {
//                    fragment.viewpager.requestDisallowInterceptTouchEvent(false);
//                    fragment.viewpager.setScroll(true);
//                }
//
//                return false;
//            }
//        });


    }


    @Override
    public void onPause() {
        super.onPause();
        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_left:
                webView.goBack();
                break;
            case R.id.iv_right:
                webView.goForward();
                break;
            case R.id.iv_refresh:
                webView.loadUrl(PageUrl);
                IndexNewsActivity2 fragment = (IndexNewsActivity2) getParentFragment();
                fragment.viewpager.requestDisallowInterceptTouchEvent(false);
                fragment.viewpager.setScroll(true);
                break;
        }
    }

    //    public void loading() {
//        webView.clearCache(true);
//        ShareURL = "http://116.62.29.20:8077/LinkTo/Link.htm";
//        webView.loadUrl("http://116.62.29.20:8077/LinkTo/Link.htm");
//    }
    public class PagerDesc {
        private int top;
        private int left;
        private int right;
        private int bottom;

        //get set方法

        public PagerDesc(int top, int left, int right, int bottom) {
            this.top = top;
            this.bottom = bottom;
        }
    }
}

