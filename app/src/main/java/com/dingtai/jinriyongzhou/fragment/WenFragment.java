package com.dingtai.jinriyongzhou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.dtmutual.activity.MutualActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.activity.PrimaryActivity;

/**
 * Created by Administrator on 2017/8/26.
 */

public class WenFragment extends BaseFragment {
    private View mMainView;
    private TextView tv_wenzheng, tv_huzhu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.fragment_wen, container, false);
        initView();
        return mMainView;
    }

    private void initView() {
        tv_wenzheng = (TextView) mMainView.findViewById(R.id.tv_wenzheng);
        tv_huzhu = (TextView) mMainView.findViewById(R.id.tv_huzhu);
        tv_wenzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),PrimaryActivity.class);
                startActivity(intent);
            }
        });
        tv_huzhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MutualActivity.class);
                startActivity(intent);
            }
        });
    }
}
