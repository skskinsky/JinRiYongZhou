package com.dingtai.jinriyongzhou.activity;

import android.os.Bundle;

import com.dingtai.base.activity.BaseFragmentActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.fragment.IndexRead1;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class PaperReadActivity extends BaseFragmentActivity {

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_read);
        initeTitle();
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        setTitle(title);
        IndexRead1 fragment = IndexRead1.newInstance(url);
        fragment.setPageUrl(url);
        getSupportFragmentManager().beginTransaction().add(R.id.mContentFl, fragment).commit();
    }
}
