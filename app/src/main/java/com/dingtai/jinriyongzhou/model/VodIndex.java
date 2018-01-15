package com.dingtai.jinriyongzhou.model;

import java.util.ArrayList;
import java.util.List;

public class VodIndex {
	private List<Vod> Vod = new ArrayList<Vod>();
	private List<NewsADs> NewADs = new ArrayList<NewsADs>();
	public List<Vod> getVod() {
		return Vod;
	}
	public void setVod(List<Vod> Vod) {
		this.Vod = Vod;
	}
	public List<NewsADs> getNewADs() {
		return NewADs;
	}
	public void setNewADs(List<NewsADs> newADs) {
		NewADs = newADs;
	}
}
