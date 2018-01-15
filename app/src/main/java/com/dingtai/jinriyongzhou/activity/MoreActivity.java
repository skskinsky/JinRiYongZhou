package com.dingtai.jinriyongzhou.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dingtai.base.activity.BaseActivity;
import com.dingtai.base.adapter.DragGridBaseAdapter;
import com.dingtai.base.api.API;
import com.dingtai.base.utils.Assistant;
import com.dingtai.base.utils.MyImageLoader;
import com.dingtai.base.view.DragGridView;
import com.dingtai.base.view.MyGridView;
import com.dingtai.dtlogin.activity.LoginActivity;
import com.dingtai.jinriyongzhou.R;
import com.dingtai.jinriyongzhou.adapter.BottomAdapter;
import com.dingtai.jinriyongzhou.config.IndexSujbectConfig;
import com.dingtai.jinriyongzhou.model.IndexModel;
import com.dingtai.jinriyongzhou.service.IndexHttpService;
import com.dingtai.newslib3.activity.ActivityNewsFromIndex;
import com.dingtai.newslib3.activity.CommonActivity;
import com.dingtai.newslib3.activity.NewsTheme;
import com.dingtai.newslib3.activity.NewsWebView;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Administrator
 * 
 */
public class MoreActivity extends BaseActivity implements DragGridView.UpTouch {
	DragGridView dragGridViewTop;
	MyGridView dragGridViewBottom;
	// private List<IndexModel> indexModels = new ArrayList<IndexModel>();
	private RuntimeExceptionDao<IndexModel, String> dao;
	private boolean isDragged = false, isSort = false;
	private List<IndexModel> top, bottom, tempList;
	private TitltAdapter topAdapter;
	private BottomAdapter bottomAdapter;

	@Override
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_more);
		initeTitle();
		setTitle("全部分类");
		initView();
		inteData();
		setListeners();
	}

	private void inteData() {
		dao = getHelper().getMode(IndexModel.class);
		top = new ArrayList<IndexModel>();
		bottom = new ArrayList<IndexModel>();
		tempList = new ArrayList<IndexModel>();
		getIndexByCache();
	}

	private void initView() {
		line = findViewById(R.id.line);
		dragGridViewTop = (DragGridView) findViewById(R.id.mGridView_top);
		dragGridViewBottom = (MyGridView) findViewById(R.id.mGridView_bottom);
		dragGridViewTop.setType(1);
		dragGridViewTop.setListener(this);
		dragGridViewTop.setSelector(new ColorDrawable(Color.TRANSPARENT));
		dragGridViewBottom.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	private void getIndexByCache() {
		RuntimeExceptionDao<IndexModel, String> dao = getHelper()
				.getMode(IndexModel.class);
		top = dao.queryForEq("IsDel", "True");
		if (top.size() > 0) {
			topAdapter = new TitltAdapter(getApplicationContext(), top);
			dragGridViewTop.setAdapter(topAdapter);
			topAdapter.notifyDataSetChanged();
		} else {
			String url = API.COMMON_URL
					+ "Interface/IndexModule.ashx?action=GetIndexMouble&StID="
					+ API.STID;
			getDataIndex(url);
		}
		bottom = dao.queryForEq("IsDel", "False");
		if (bottom.size() > 0) {
			bottomAdapter = new BottomAdapter(getApplicationContext(), bottom);
			dragGridViewBottom.setAdapter(bottomAdapter);
			bottomAdapter.notifyDataSetChanged();
		}
		int dy = line.getTop();
		
	}

	private void getDataIndex(String url) {
		requestData(getApplicationContext(), url, new Messenger(handler),
				API.GET_INDEX, API.GET_INDEX_MESSENGER, IndexHttpService.class);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 101010:
				// indexModels.addAll(top);
				// indexModels.addAll(bottom);
				if (tempList.size() > 0) {
					tempList.clear();
				}
				tempList = (List<IndexModel>) msg.getData()
						.getParcelableArrayList("list").get(0);
				if (tempList.size() > 0) {
					for (IndexModel model : tempList) {
						if ("True".equals(model.getIsDel())) {
							top.add(model);
						} else {
							bottom.add(model);
						}

						// for (int i = 0; i < indexModels.size(); i++) {
						// IndexModel tempModel = indexModels.get(i);
						// if (model.getID().equals(tempModel.getID())) {
						// break;
						// }
						// if (!model.getID().equals(tempModel.getID())
						// && i == indexModels.size() - 1) {
						// indexModels.add(model);
						// }
						//
						// }
					}
				}
				if (bottom.size() > 0) {
					bottomAdapter = new BottomAdapter(getApplicationContext(),
							bottom);
					dragGridViewBottom.setAdapter(bottomAdapter);
					bottomAdapter.notifyDataSetChanged();
				}
				if (top.size() > 0) {
					topAdapter = new TitltAdapter(getApplicationContext(), top);
					dragGridViewTop.setAdapter(topAdapter);
					topAdapter.notifyDataSetChanged();
				}
				break;
			case 333:
				tempList = dao.queryForEq("IsDel", "False");
				if (tempList.size() > 0) {
					bottom.addAll(tempList);
					tempList.clear();
				}
				bottomAdapter = new BottomAdapter(getApplicationContext(),
						bottom);
				dragGridViewBottom.setAdapter(bottomAdapter);
				bottomAdapter.notifyDataSetChanged();
				break;
			case 555:
				tempList = dao.queryForEq("IsDel", "False");
				if (tempList.size() > 0) {
					bottom.addAll(tempList);
					tempList.clear();
				}
				bottomAdapter = new BottomAdapter(getApplicationContext(),
						bottom);
				dragGridViewBottom.setAdapter(bottomAdapter);
				bottomAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	private View line;

	private void setListeners() {
		dragGridViewTop.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// int index = dragGridViewTop.getIndex(x, y);
				IndexModel model = top.get(position);
				if ("True".equals(model.getIsDelete())) {
					return;
				}
				// if (top.size() < 7) {
				model.setIsDel("False");
				bottom.add(model);
				top.remove(position);
				isDragged = true;
				notifyDataChange();
				// MoreActivity.this.onItemClick(top, position);
			}
		});
		dragGridViewBottom.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				IndexModel model = bottom.get(position);
				// if (top.size() < 7) {
				model.setIsDel("True");
				top.add(model);
				bottom.remove(position);
				isDragged = true;
				notifyDataChange();
				// MoreActivity.this.onItemClick(bottom, position);
			}
		});
	}

	private void onItemClick(List<IndexModel> list, int position) {
		Intent intent = new Intent();
		IndexModel model = list.get(position);
		if ("True".equals(model.getIsInside())) {
			if ("True".equals(model.getIsHtml())) {
				intent.putExtra("PageUrl", model.getHtmlUrl());
				intent.putExtra("Title", model.getModuleName());
				intent.setClass(getApplicationContext(), NewsWebView.class);
			} else {
				try {
					Class<?> clazz = Class
							.forName(IndexSujbectConfig.IndexModel.get(model
									.getJumpTo()));
					if (clazz.getName().equals(
							"com.dingtai.guangdianchenzhou.index.IndexRead")) {
						intent.putExtra("name", "读报");
						intent.setClass(getApplicationContext(),
								CommonActivity.class);
					} else if (clazz
							.getName()
							.equals("com.dingtai.guangdianchenzhou.activity.active.ActiveActivity")) {
						intent.putExtra("name", "活动");
						intent.setClass(getApplicationContext(),
								CommonActivity.class);
					} else if (clazz.getName().equals(
							"com.dingtai.guangdianchenzhou.index.ActivityNewsFromIndex")) {
						intent.putExtra("id", model.getReMark());
						intent.putExtra("name", model.getModuleName());
						intent.setClass(getApplicationContext(),
								ActivityNewsFromIndex.class);
					} else if (clazz.getName().equals(
							"com.dingtai.guangdianchenzhou.activity.news.NewsTheme")) {
						intent.setClass(getApplicationContext(),
								NewsTheme.class);
						intent.putExtra("LanMuId", model.getReMark());
						intent.putExtra("TitleName", model.getModuleName());
						startActivity(intent);
						return;
					} else {
						if ("jibu".equals(model.getJumpTo())
								&& Assistant
										.getUserInfoByOrm(getApplicationContext()) == null) {
							Toast.makeText(getApplicationContext(), "请先登录!",
									Toast.LENGTH_SHORT).show();
							startActivity(new Intent(getApplicationContext(),
									LoginActivity.class));
							return;
						}
						intent.putExtra("Title", model.getModuleName());
						intent.setClass(getApplicationContext(), clazz);
					}

				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

		} else {
			intent.putExtra("PageUrl", model.getHtmlUrl());
			intent.putExtra("Title", model.getModuleName());
			intent.setClass(getApplicationContext(), NewsWebView.class);
		}
		startActivity(intent);
	}

	// class MyDragViewAdapter extends DragViewAdaprer {
	// List<IndexModel> models;
	// private ImageLoadingListenerImpl mImageLoadingListenerImpl;
	// private DisplayImageOptions option;
	//
	// public MyDragViewAdapter(List<IndexModel> models) {
	// this.models = models;
	// option = MyImageLoader.option();
	// mImageLoadingListenerImpl = new ImageLoadingListenerImpl();
	// }
	//
	// @Override
	// public int getCount() {
	// return models.size() + 1;
	// }
	//
	// @Override
	// public int getDividerPosition() {
	// return 7;
	// }
	//
	// @Override
	// public View getDividerView(int position, View dividerView) {
	// if (position == getDividerPosition()) {
	// return MoreActivity.this.dividerView;
	// } else {
	// return null;
	// }
	// }
	//
	// @Override
	// public View getView(int position, View convertView) {
	// ViewHolder holder = null;
	// if (convertView == null) {
	// holder = new ViewHolder();
	// convertView = getLayoutInflater().inflate(R.layout.gv_item,
	// dgv, false);
	// holder.iv_logo = (ImageView) convertView
	// .findViewById(R.id.iv_icon);
	// holder.tv_name = (TextView) convertView
	// .findViewById(R.id.tv_name);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	// IndexModel indexModel = models.get(position);
	// holder.tv_name.setText(indexModel.getModuleName());
	// ImageLoader.getInstance().displayImage(indexModel.getModuleLogo(),
	// holder.iv_logo, option, mImageLoadingListenerImpl);
	// return convertView;
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_bar_back:
			try {
				if (isDragged || isSort) {
					dao.delete(dao.queryForAll());
					for (IndexModel model : top) {
						dao.create(model);
					}
					if (isSort) {
						bottom = bottomAdapter.getSwapList();
					}
					for (IndexModel model : bottom) {
						dao.create(model);
					}
					setResult(1);
				}
			} catch (Exception e) {
			}
			finish();
			break;

		default:
			break;
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// if (isDragged) {
	// dao.delete(dao.queryForAll());
	// for (IndexModel model : indexModels) {
	// dao.create(model);
	// }
	// setResult(1);
	// }
	// finish();
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	class ViewHolder {
		TextView tv_name;
		ImageView iv_logo;
	}

	public class TitltAdapter extends BaseAdapter implements
			DragGridBaseAdapter {
		private List<IndexModel> list;
		private Context context;
		private Holder vh = null;
		private int mHidePosition;
		private DisplayImageOptions option;

		public TitltAdapter(Context context, List<IndexModel> list) {
			// TODO Auto-generated constructor stub
			this.context = context;
			this.list = list;
			mHidePosition = list.size();
			option = MyImageLoader.option();
		}

		// public TitltAdapter(Context context,List<MoreChannelModel> list,int
		// flag){
		// this.context = context;
		// this.list = list;
		// mHidePosition = list.size();
		// }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			vh = null;
			// if (arg1 == null) {
			if (arg1 == null) {
				vh = new Holder();
				arg1 = LayoutInflater.from(context).inflate(R.layout.gv_item,
						null);
				vh.title_name = (TextView) arg1.findViewById(R.id.tv_name);
				vh.iv = (ImageView) arg1.findViewById(R.id.iv_icon);
				vh.iv_delelet = (ImageView) arg1.findViewById(R.id.iv_delete);
				arg1.setTag(vh);
			} else {
				vh = (Holder) arg1.getTag();
			}
			// } else {
			// vh = (Holder) arg1.getTag();
			// }

			String name = list.get(position).getModuleName();
			// if ("True".equals(list.get(position).getIsDel())) {
			// vh.title_name.setTextColor(context.getResources().getColor(
			// R.color.grey));
			// vh.title_name.setEnabled(false);
			// }
			if ("True".equals(list.get(position).getIsDelete())) {
				vh.iv_delelet.setVisibility(View.GONE);
			} else {
				vh.iv_delelet.setVisibility(View.VISIBLE);
			}
			ImageLoader.getInstance().displayImage(
					list.get(position).getModuleLogo(), vh.iv, option);
			vh.title_name.setText(name);
			if (position == mHidePosition) {
				arg1.setVisibility(View.INVISIBLE);
			}
			return arg1;
		}

		public class Holder {
			public TextView title_name;
			public ImageView iv, iv_delelet;
		}

		@Override
		public void reorderItems(int oldPosition, int newPosition) {
			// TODO Auto-generated method stub

			IndexModel temp = list.get(oldPosition);
			if (oldPosition < newPosition) {
				for (int i = oldPosition; i < newPosition; i++) {
					Collections.swap(list, i, i + 1);
				}
			} else if (oldPosition > newPosition) {
				for (int i = oldPosition; i > newPosition; i--) {
					Collections.swap(list, i, i - 1);
				}
			}
			isSort = true;
			list.set(newPosition, temp);
		}

		@Override
		public void setHideItem(int hidePosition) {
			// TODO Auto-generated method stub
			this.mHidePosition = hidePosition;
			topAdapter = new TitltAdapter(getApplicationContext(), top);
			dragGridViewTop.setAdapter(topAdapter);
		}

		public List<IndexModel> getSwapList() {
			return list;
		}
	}

	@Override
	public void up(int x, int y, int type) {
//		if (type == 2) {
//			if (y > 0) {
//				return;
//			}
//			// int index = dragGridViewTop.getIndex(x, y);
//			int dragIndex = dragGridViewBottom.getDragIndex();
//			IndexModel model = bottom.get(dragIndex);
//			// if (top.size() < 7) {
//			model.setIsDel("True");
//			top.add(model);
//			bottom.remove(dragIndex);
//		}
//		isDragged = true;
//		notifyDataChange();
		// } else {
		// if (index != -1 && dragIndex != -1) {
		// isDragged = true;
		// IndexModel topString = top.get(index);
		// IndexModel bottomString = bottom.get(dragIndex);
		// top.remove(index);
		// bottom.remove(dragIndex);
		// bottomString.setIsDel("True");
		// topString.setIsDel("False");
		// top.add(index, bottomString);
		// bottom.add(dragIndex, topString);
		// notifyDataChange();
		// }
		// }

	}

	private void notifyDataChange() {
		bottomAdapter = new BottomAdapter(getApplicationContext(), bottom);
		dragGridViewBottom.setAdapter(bottomAdapter);

		topAdapter = new TitltAdapter(getApplicationContext(), top);
		dragGridViewTop.setAdapter(topAdapter);

		topAdapter.notifyDataSetChanged();
		bottomAdapter.notifyDataSetChanged();
	}

	@Override
	public void remove(int position) {
		// IndexModel model = top.get(position);
		// model.setIsDel("False");
		// bottom.add(model);
		// top.remove(position);
		// isDragged = true;
		// notifyDataChange();
	}}
