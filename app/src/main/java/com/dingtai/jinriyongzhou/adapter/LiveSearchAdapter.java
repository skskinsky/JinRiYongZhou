package com.dingtai.jinriyongzhou.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingtai.base.livelib.model.LiveChannelModel;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.tool.SearchViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LiveSearchAdapter extends BaseAdapter {
	private Context context;
	private List<LiveChannelModel> list;

	public LiveSearchAdapter(Context context, List<LiveChannelModel> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		 SearchViewHolder holder;
		if (converView == null || converView.getTag() == null) {
			holder = new SearchViewHolder();
			converView = LayoutInflater.from(context).inflate(
					R.layout.item_new_search, null);

			holder.img_newspic = (ImageView) converView
					.findViewById(R.id.img_newspic);
			holder.tv_newstitle = (TextView) converView
					.findViewById(R.id.tv_newstitle);
			holder.tv_readnum = (TextView) converView
					.findViewById(R.id.tv_readnum);


			converView.setTag(holder);
		} else {
			holder = (SearchViewHolder) converView.getTag();
		}



		ImageLoader.getInstance().displayImage(list.get(position).getLiveImageUrl(), holder.img_newspic);

		holder.tv_newstitle.setText(list.get(position).getLiveChannelName());
		//holder.tv_readnum.setText(list.get(position).getCommentsNum());



		return converView;
	}




}
