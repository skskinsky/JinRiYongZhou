package com.dingtai.jinriyongzhou.model;

import java.util.ArrayList;
import java.util.List;

public class NewIndex {
	
	private List<HHIndexNewsListModel> News = new ArrayList<HHIndexNewsListModel>();
	private List<NewsADs> NewADs = new ArrayList<NewsADs>();
	public List<HHIndexNewsListModel> getNews() {
		return News;
	}
	public void setNews(List<HHIndexNewsListModel> news) {
		News = news;
	}
	public List<NewsADs> getNewADs() {
		return NewADs;
	}
	public void setNewADs(List<NewsADs> newADs) {
		NewADs = newADs;
	}

}
