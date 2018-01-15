package com.dingtai.jinriyongzhou.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseFragment;
import com.dingtai.base.api.API;
import com.dingtai.base.application.MyApplication;
import com.dingtai.base.pullrefresh.PullToRefreshBase;
import com.dingtai.base.pullrefresh.PullToRefreshListView;
import com.dingtai.base.pullrefresh.loadinglayout.LoadingFooterLayout;
import com.dingtai.base.pullrefresh.loadinglayout.PullHeaderLayout;
import com.dingtai.base.utils.Assistant;
import com.dingtai.dtmutual.activity.MutualDetailActivity;
import com.dingtai.dtmutual.activity.PushMutual;
import com.dingtai.dtmutual.adapter.MutualAdapter;
import com.dingtai.dtmutual.model.Hotspot;
import com.dingtai.dtmutual.service.MutualAPI;
import com.dingtai.dtmutual.service.MutualService;
import com.dingtai.jinriyongzhou.R;

import java.util.ArrayList;
import java.util.List;




public class CooperationFragment extends BaseFragment {

	private View mainView = null;
	private PullToRefreshListView listView;
	private MutualAdapter adapter;

	private List<Hotspot> list ;

	private int dtop = 0;
	private boolean isUp = false;

	private boolean isRefresh = true;
	private ImageView iv_goto;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case API.SUCCESS:
				if(!isUp)
					list.clear();
				list.addAll((List) msg.getData().getParcelableArrayList("list")
						.get(0));
				adapter.notifyDataSetChanged();
				listView.onRefreshComplete();
				break;

			case API.UNSUCCESS:
				Toast.makeText(getActivity(), "暂无更多数据", Toast.LENGTH_SHORT).show();
				listView.onRefreshComplete();
				break;

			case API.EXCEPTION:
				Toast.makeText(getActivity(), "获取数据异常，请重试", Toast.LENGTH_SHORT).show();
				listView.onRefreshComplete();
				break;
			case 999:
				listView.setRefreshing();
				break;
			default:
				listView.onRefreshComplete();
				break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(mainView==null){

			mainView = inflater.inflate(R.layout.fragment_mutual1,
					container, false);
			initView();
			initDate();
			requestData();
			}
		//因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
		ViewGroup parent = (ViewGroup)mainView.getParent();
		if(parent != null) {
			parent.removeView(mainView);
		}
		return mainView;
	}
	
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		listView.onRefreshComplete();
		handler.removeMessages(999);
		handler.removeMessages(API.SUCCESS);
		handler.removeMessages(API.UNSUCCESS);
		handler.removeMessages(API.EXCEPTION);
		super.onStop();
		
	}
	



	private void requestData() {
		// TODO Auto-generated method stub
		String url = API.COMMON_URL_JS + "interface/Cooperation.ashx?action=GetDownList";
		get_Coop_Down(getActivity(), url, "10", API.STID, new Messenger(this.handler));
	}

	private void requestUpData(){
		String url = API.COMMON_URL_JS + "interface/Cooperation.ashx?action=GetUpList";

		get_Coop_Up(getActivity(), url, "10", String.valueOf(dtop), API.STID, new Messenger(this.handler));
	}

	private void initDate() {
		// TODO Auto-generated method stub
		list =  new ArrayList<Hotspot>();
		adapter = new MutualAdapter(getActivity(),getActivity().getWindowManager(), list);

		// 设置上拉下拉事件

		listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						isUp = false;
						requestData();
					}
				}, 500);

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						isUp = true;
						dtop = list.size();
						requestUpData();

					}
				}, 500);

			}
		});

		ListView mListView = listView.getRefreshableView();
		// 给listView设置适配器
		mListView.setAdapter(adapter);
		listView.setMode(PullToRefreshBase.Mode.BOTH);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				Intent i = new Intent();
				i.putExtra("id", list.get(arg2-1).getID());
				i.setClass(getActivity(),
						MutualDetailActivity.class);
				startActivity(i);
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		listView = (PullToRefreshListView) mainView.findViewById(R.id.mutual_listview);

		listView.setHeaderLayout(new PullHeaderLayout(getActivity()));
		listView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));
		if (MyApplication.RefreshVersion==2){
			listView.setHeaderLayout(new PullHeaderLayout(getActivity()));
			listView.setFooterLayout(new LoadingFooterLayout(getActivity(), PullToRefreshBase.Mode.PULL_FROM_END));

			// 使用第二底部加载布局,要先禁止掉包含（Mode.PULL_FROM_END）的模式
			// 如修改（Mode.BOTH为Mode.PULL_FROM_START）
			// 修改（Mode.PULL_FROM_END 为Mode.DISABLE）
			listView.setHasPullUpFriction(true); // 设置没有上拉阻力
		}
		//handler.sendEmptyMessageDelayed(999, 2000);

		iv_goto = (ImageView) mainView.findViewById(R.id.iv_goto);
		iv_goto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if (Assistant.getUserInfoByOrm(getActivity()) == null){
					Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
					intent = new Intent();
					intent.setAction(getActivity().getPackageName() + "." + "login");
					startActivity(intent);
				}else {
					intent = new Intent(getActivity(), PushMutual.class);
					intent.putExtra("type", "1");
					startActivityForResult(intent, 0);
					getActivity().overridePendingTransition(R.anim.push_bottom_in, R.anim.push_alpha_out);
				}
			}
		});
	}
	

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			if(isRefresh){
				handler.sendEmptyMessageDelayed(999, 1500);
				isRefresh = false;
			}
		}

	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//handler.sendEmptyMessageDelayed(999, 1500);

	}


	//动态下拉
	public void get_Coop_Down(Context context, String url, String top,
							  String StID,  Messenger paramMessenger) {

		Intent localIntent = new Intent(context, MutualService.class);

		localIntent.putExtra("api", MutualAPI.MUTUAL_COOP_DOWN_API);
		localIntent.putExtra(MutualAPI.MUTUAL_COOP_DOWN_MESSAGE,
				paramMessenger);
		localIntent.putExtra("url", url);
		localIntent.putExtra("top", top);
		localIntent.putExtra("StID", StID);
		context.startService(localIntent);

	}

	//动态上拉
	public void get_Coop_Up(Context context, String url, String top, String dtop,
							String StID, Messenger paramMessenger) {

		Intent localIntent = new Intent(context, MutualService.class);

		localIntent.putExtra("api", MutualAPI.MUTUAL_COOP_UP_API);
		localIntent.putExtra(MutualAPI.MUTUAL_COOP_UP_MESSAGE,
				paramMessenger);
		localIntent.putExtra("url", url);
		localIntent.putExtra("top", top);
		localIntent.putExtra("dtop", dtop);
		localIntent.putExtra("StID", StID);
		context.startService(localIntent);

	}



}
