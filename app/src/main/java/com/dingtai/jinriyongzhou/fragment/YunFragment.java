package com.dingtai.jinriyongzhou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.newslib3.activity.CommonActivity;

/**
 * Created by Administrator on 2017/8/26.
 */

public class YunFragment extends BaseFragment {
    private View mMainView;
    private ImageView tv_wen, tv_shu, tv_shi, tv_yue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater
                .inflate(R.layout.fragment_yun, container, false);
        initView();
        return mMainView;
    }

    private void initView() {
        tv_wen = (ImageView) mMainView.findViewById(R.id.tv_wen);
        tv_shu = (ImageView) mMainView.findViewById(R.id.tv_shu);
        tv_shi = (ImageView) mMainView.findViewById(R.id.tv_shi);
        tv_yue = (ImageView) mMainView.findViewById(R.id.tv_yue);
        tv_wen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("id", "477");
                intent.putExtra("name", "文库");
                startActivity(intent);
            }
        });
        tv_shu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("id", "361");
                intent.putExtra("name", "书法");
                startActivity(intent);
            }
        });
        tv_shi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("id", "364");
                intent.putExtra("name", "历史");
                startActivity(intent);
            }
        });
        tv_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent = new Intent(getActivity(), CommonActivity.class);
                intent.putExtra("id", "445");
                intent.putExtra("name", "音乐");
                startActivity(intent);
            }
        });
    }
}
