package com.dingtai.jinriyongzhou.fragment;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.SubscribeAdapter;
import com.dingtai.jinriyongzhou.model.SubscribeListsModel;

import java.util.ArrayList;
import java.util.List;

public class SubscribeModelFragment {

	public View mMainView;
	private GridView gv_button;
	private SubscribeAdapter indexAdapter;
	private List<SubscribeListsModel> indexModels = new ArrayList<SubscribeListsModel>();

	public SubscribeModelFragment(Context context) {
		mMainView = View.inflate(context, R.layout.fragment_model, null);
		initLayout(context);
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// if (mMainView == null) {
	// mMainView = inflater.inflate(R.layout.fragment_model, container,
	// false);
	// }
	//
	// initLayout();
	// return mMainView;
	// }

	private void initLayout(Context context) {
		gv_button = (GridView) mMainView.findViewById(R.id.gv_button);
		indexAdapter = new SubscribeAdapter(context, indexModels);
		gv_button.setAdapter(indexAdapter);
		gv_button.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (itemClick != null) {
					itemClick.onItemClick(arg2, indexModels.get(arg2));
				}
			}
		});
	}

	public void setData(List<SubscribeListsModel> indexModels) {
		this.indexModels = indexModels;
		indexAdapter.setData(indexModels);
		indexAdapter.notifyDataSetChanged();
	}

	private ItemClick itemClick;

	public void setItemClick(ItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public interface ItemClick {
		void onItemClick(int position, SubscribeListsModel indexModel);
	}
}
