package com.dingtai.jinriyongzhou.model;

import java.util.ArrayList;
import java.util.List;

public class MediaIndex {
	private List<MediaList> MediaList = new ArrayList<MediaList>();
	private List<NewsADs> NewADs = new ArrayList<NewsADs>();
	public List<MediaList> getMediaList() {
		return MediaList;
	}
	public void setMediaList(List<MediaList> MediaList) {
		this.MediaList = MediaList;
	}
	public List<NewsADs> getNewADs() {
		return NewADs;
	}
	public void setNewADs(List<NewsADs> newADs) {
		NewADs = newADs;
	}


}
