package com.dingtai.jinriyongzhou.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.fragment.TVShowActivity;

/**
 * Created by xf on 2017/7/21 0021.
 */

public class LiveListActivity extends BaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_list);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TVShowActivity fragment = new TVShowActivity();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        fragment.setBackDisplay();
    }
}
