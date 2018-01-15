package com.dingtai.jinriyongzhou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.jinriyongzhou.R;

/**
 * Created by Administrator on 2017/12/1 0001.
 */

public class CommonActivity extends BaseFragmentActivity {
    FrameLayout frameLayout;
    Fragment fragment;
    String fragmentName = "";
    public CommonActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initView();
        getSupportFragmentManager().beginTransaction().replace(R.id.common_activity, this.fragment).commit();
    }

    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.common_activity);
        Bundle bundle = new Bundle();
        bundle.putString("isShowTitle", "True");
        this.fragmentName = this.getIntent().getStringExtra("name");
        String packgeName = this.getIntent().getStringExtra("packgeName");
        if(TextUtils.isEmpty(this.fragmentName)) {
            this.fragmentName = "我的";
        }

        String id = this.getIntent().getStringExtra("id");Class clazz = null;

        try {
            if(packgeName != null) {
                clazz = Class.forName(packgeName);
            }
        } catch (ClassNotFoundException var15) {
            var15.printStackTrace();
        }
        if(this.fragmentName.equals("我的")) {
            try {
                if(clazz == null) {
                    clazz = Class.forName("com.dingtai.jinriyongzhou.fragment.ActivityUserCenter");
                }
                this.fragment = (BaseFragment)clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.fragment.setArguments(bundle);
        }

    }
}
