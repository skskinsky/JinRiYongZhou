package com.dingtai.jinriyongzhou.model;

import java.util.ArrayList;
import java.util.List;

public class HomeListModel {
    private List<CJIndexNewsListModel> ResList = new ArrayList<CJIndexNewsListModel>();
    private List<IndexAD> IndexAD = new ArrayList<IndexAD>();
    private List<IndexAD> IndexRecommendAD = new ArrayList<IndexAD>();

    public List<CJIndexNewsListModel> getResList() {
        return ResList;
    }

    public void setResList(List<CJIndexNewsListModel> resList) {
        ResList = resList;
    }

    public List<IndexAD> getIndexAD() {
        return IndexAD;
    }

    public void setIndexAD(List<IndexAD> indexAD) {
        IndexAD = indexAD;
    }

    public List<IndexAD> getIndexRecommendAD() {
        return IndexRecommendAD;
    }

    public void setIndexRecommendAD(List<IndexAD> indexRecommendAD) {
        IndexRecommendAD = indexRecommendAD;
    }
}
