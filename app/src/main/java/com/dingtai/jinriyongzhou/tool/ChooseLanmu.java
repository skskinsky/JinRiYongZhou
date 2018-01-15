package com.dingtai.jinriyongzhou.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dingtai.base.model.NewsListModel;
import com.dingtai.jinriyongzhou.model.CJIndexNewsListModel;
import com.dingtai.jinriyongzhou.model.HHIndexNewsListModel;
import com.dingtai.newslib3.activity.ImageDetailActivity;
import com.dingtai.newslib3.activity.NewTopiceActivity;
import com.dingtai.newslib3.activity.NewsThemeList;
import com.dingtai.newslib3.activity.NewsWebView;
import com.dingtai.newslib3.activity.TestNewsDetailActivity;

import java.util.HashMap;

public class ChooseLanmu {

	public static void toLanmu(Context context, NewsListModel thisNews) {
		String newsType = thisNews.getResourceType();
		Intent intent = null;
		try {
			if ("2".equals(newsType)) {
				// 使用活动的WebView来显示 网页新闻
				intent = new Intent(context, NewsWebView.class);
				intent.putExtra("Title", thisNews.getTitle().toString().trim());
				intent.putExtra("PageUrl", thisNews.getResourceUrl().toString()
						.trim());
				intent.putExtra("GUID", thisNews.getResourceGUID());
				if (thisNews.getSmallPicUrl().toString().length() > 1) {
					intent.putExtra("LogoUrl", thisNews.getSmallPicUrl()
							.toString());
				}
				intent.putExtra("summary", thisNews.getSummary());
				intent.putExtra("Type", "网页新闻");

				intent.putExtra("isNews", true);
				intent.putExtra("gentie", thisNews.getCommentNum());
				intent.putExtra("date", thisNews.getCreateTime());
				intent.putExtra("source", thisNews.getSourceForm());
				intent.putExtra("isComment", thisNews.getIsComment());

			} else if ("3".equals(newsType)) {
				// 为1的时候表示是图集
				intent = new Intent(context, ImageDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("RPID", thisNews.getRPID().toString().trim());
				Bundle bundle = new Bundle();
				bundle.putSerializable("newspicture", thisNews);
				intent.putExtras(bundle);
				//				intent.putExtra("newspicture", thisNews);
				HashMap<String, String> news = new HashMap<String, String>();
				news.put("ID", thisNews.getID());
				news.put("GUID", thisNews.getResourceGUID());
				news.put("Summary", thisNews.getSummary());
				news.put("Title", thisNews.getTitle());  
				news.put("updatetime", thisNews.getUpdateTime());
				news.put("SourceForm", thisNews.getSourceForm());
				news.put("SmallPicUrl", thisNews.getSmallPicUrl());
				news.put("CONTENT", "");
				news.put("CREATEDATE", thisNews.getCreateTime());
				news.put("CommentNum", thisNews.getCommentNum());
				news.put("RPID", thisNews.getRPID());
				news.put("ResourceType", newsType);
				news.put("isComment", thisNews.getIsComment());

				NewsListModel n = new NewsListModel();
				n.setResourceGUID(thisNews.getResourceGUID());
				n.setCommentNum(thisNews.getCommentNum());
				n.setTitle(thisNews.getTitle());
				n.setSourceForm(thisNews.getSourceForm());
				n.setCreateTime(thisNews.getCreateTime());
				intent.putExtra("newspicture", n);
				intent.putExtra("newdetail", news);
				intent.putExtra("type", "新闻详情图");

			} else if ("4".equals(newsType)) {
				
				if(thisNews.getIsNewTopice().equals("True")){
					intent = new Intent();
					intent.putExtra("title",thisNews.getTitle());
					intent.putExtra("ParentID",thisNews.getChID());
					intent.setClass(context, NewTopiceActivity.class);
				}else{
					intent = new Intent(context, NewsThemeList.class);
					intent.putExtra("LanMuId", thisNews.getChID().toString().trim());
					intent.putExtra("ChannelLogo", thisNews.getChannelLogo()
							.toString().trim());
					intent.putExtra("TitleName", thisNews.getChannelName()
							.toString().trim());
				}
			} else if ("5".equals(newsType)) {
//				ActivityZhiboList.ZhiboID = thisNews.getChID();
//				intent = new Intent(context, ActivityZhibo.class);
//				intent.putExtra("imglogin", thisNews.getSmallPicUrl());
			} else {
				// 跳转到详情页
				intent = new Intent(context, TestNewsDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("ResourceType", newsType.toString().trim());
			}

			context.startActivity(intent);

		} catch (Exception e) {
			Log.d("xf", e.toString());
		}
	}



	public static void homeToLanmu(Context context,
			HHIndexNewsListModel thisNews) {
		String newsType = thisNews.getResourceType();
		Intent intent = null;
		try {
			if ("2".equals(newsType)) {
				// 使用活动的WebView来显示 网页新闻
				intent = new Intent(context, NewsWebView.class);
				intent.putExtra("Title", thisNews.getTitle().toString().trim());
				intent.putExtra("PageUrl", thisNews.getResourceUrl().toString()
						.trim());
				intent.putExtra("GUID", thisNews.getResourceGUID());
				if (thisNews.getSmallPicUrl().toString().length() > 1) {
					intent.putExtra("LogoUrl", thisNews.getSmallPicUrl()
							.toString());
				}
				intent.putExtra("summary", thisNews.getSummary());
				intent.putExtra("Type", "网页新闻");

				intent.putExtra("isNews", true);
				intent.putExtra("gentie", thisNews.getCommentNum());
				intent.putExtra("date", thisNews.getCreateTime());
				intent.putExtra("source", thisNews.getSourceForm());
				intent.putExtra("isComment", thisNews.getIsComment());




			} else if ("3".equals(newsType)) {
				// 为1的时候表示是图集
				intent = new Intent(context, ImageDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("RPID", thisNews.getRPID().toString().trim());
				intent.putExtra("newspicture", thisNews);

				HashMap<String, String> news = new HashMap<String, String>();
				news.put("ID", thisNews.getID());
				news.put("GUID", thisNews.getResourceGUID());
				news.put("Summary", thisNews.getSummary());
				news.put("Title", thisNews.getTitle());
				news.put("updatetime", thisNews.getUpdateTime());
				news.put("SourceForm", thisNews.getSourceForm());
				news.put("SmallPicUrl", thisNews.getSmallPicUrl());
				news.put("CONTENT", "");
				news.put("CREATEDATE", thisNews.getCreateTime());
				news.put("CommentNum", thisNews.getCommentNum());
				news.put("RPID", thisNews.getRPID());
				news.put("ResourceType", newsType);
				news.put("isComment", thisNews.getIsComment());

				NewsListModel n = new NewsListModel();
				n.setResourceGUID(thisNews.getResourceGUID());
				n.setCommentNum(thisNews.getCommentNum());
				n.setTitle(thisNews.getTitle());
				n.setSourceForm(thisNews.getSourceForm());
				n.setCreateTime(thisNews.getCreateTime());
				intent.putExtra("newspicture", n);
				intent.putExtra("newdetail", news);
				intent.putExtra("type", "新闻详情图");

			} else if ("4".equals(newsType)) {
				
				if(thisNews.getIsNewTopice().equals("True")){
					intent = new Intent();
					intent.putExtra("title",thisNews.getTitle());
					intent.putExtra("ParentID",thisNews.getChID());
					intent.setClass(context, NewTopiceActivity.class);
				}else{
					intent = new Intent(context, NewsThemeList.class);
					intent.putExtra("LanMuId", thisNews.getChID().toString().trim());
					intent.putExtra("ChannelLogo", thisNews.getChannelLogo()
							.toString().trim());
					intent.putExtra("TitleName", thisNews.getChannelName()
							.toString().trim());
				}
			
			} else if ("5".equals(newsType)) {
//				ActivityZhiboList.ZhiboID = thisNews.getChID();
//				intent = new Intent(context, ActivityZhibo.class);
//				intent.putExtra("imglogin", thisNews.getSmallPicUrl());
			} else {
				// 跳转到详情页
				intent = new Intent(context, TestNewsDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("ResourceType", newsType.toString().trim());
			}

			context.startActivity(intent);

		} catch (Exception e) {
			Log.d("xf", e.toString());
		}
	}





	public static void homeToLanmu(Context context,
			CJIndexNewsListModel thisNews) {
		String newsType = thisNews.getResourceType();
		Intent intent = null;
		try {
			if ("2".equals(newsType)) {
				// 使用活动的WebView来显示 网页新闻
				intent = new Intent(context, NewsWebView.class);
				intent.putExtra("Title", thisNews.getTitle().toString().trim());
				intent.putExtra("PageUrl", thisNews.getResourceUrl().toString()
						.trim());
				intent.putExtra("GUID", thisNews.getResourceGUID());
				if (thisNews.getSmallPicUrl().toString().length() > 1) {
					intent.putExtra("LogoUrl", thisNews.getSmallPicUrl()
							.toString());
				}
				intent.putExtra("summary", thisNews.getSummary());
				intent.putExtra("Type", "网页新闻");

				intent.putExtra("isNews", true);
				intent.putExtra("gentie", thisNews.getCommentNum());
				intent.putExtra("date", thisNews.getCreateTime());
				intent.putExtra("source", thisNews.getSourceForm());
				intent.putExtra("isComment", thisNews.getIsComment());




			} else if ("3".equals(newsType)) {
				// 为1的时候表示是图集
				intent = new Intent(context, ImageDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("RPID", thisNews.getRPID().toString().trim());
				intent.putExtra("newspicture", thisNews);

				HashMap<String, String> news = new HashMap<String, String>();
				news.put("ID", thisNews.getID());
				news.put("GUID", thisNews.getResourceGUID());
				news.put("Summary", thisNews.getSummary());
				news.put("Title", thisNews.getTitle());
				news.put("updatetime", thisNews.getUpdateTime());
				news.put("SourceForm", thisNews.getSourceForm());
				news.put("SmallPicUrl", thisNews.getSmallPicUrl());
				news.put("CONTENT", "");
				news.put("CREATEDATE", thisNews.getCreateTime());
				news.put("CommentNum", thisNews.getCommentNum());
				news.put("RPID", thisNews.getRPID());
				news.put("ResourceType", newsType);
				news.put("isComment", thisNews.getIsComment());

				NewsListModel n = new NewsListModel();
				n.setResourceGUID(thisNews.getResourceGUID());
				n.setCommentNum(thisNews.getCommentNum());
				n.setTitle(thisNews.getTitle());
				n.setSourceForm(thisNews.getSourceForm());
				n.setCreateTime(thisNews.getCreateTime());
				intent.putExtra("newspicture", n);
				intent.putExtra("newdetail", news);
				intent.putExtra("type", "新闻详情图");

			} else if ("4".equals(newsType)) {
				intent = new Intent(context, NewsThemeList.class);
				intent.putExtra("LanMuId", thisNews.getChID().toString().trim());
				intent.putExtra("ChannelLogo", thisNews.getChannelLogo()
						.toString().trim());
				intent.putExtra("TitleName", thisNews.getChannelName()
						.toString().trim());
			} else if ("5".equals(newsType)) {
//				ActivityZhiboList.ZhiboID = thisNews.getChID();
//				intent = new Intent(context, ActivityZhibo.class);
//				intent.putExtra("imglogin", thisNews.getSmallPicUrl());
			} else {
				// 跳转到详情页
				intent = new Intent(context, TestNewsDetailActivity.class);
				intent.putExtra("ID", thisNews.getResourceGUID().toString()
						.trim());
				intent.putExtra("ResourceType", newsType.toString().trim());
			}

			context.startActivity(intent);

		} catch (Exception e) {
			Log.d("xf", e.toString());
		}
	}
}
