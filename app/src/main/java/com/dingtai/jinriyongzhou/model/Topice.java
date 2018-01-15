package com.dingtai.jinriyongzhou.model;

import com.dingtai.newslib3.model.Topics;

import java.util.List;

/**
 * Created by Administrator on 2017/12/13.
 */

public class Topice {
    /**
     * Title : 6Zu26Zm15Yy65YWs5a6J5bGA
     * Logo : aHR0cDovLzExNi42Mi4yOS4yMDo4MDc3L1VwbG9hZHMvSW1hZ2VzLzIwMTcwODMxMTYyNDE2NTQ4NjYxMDAwLmpwZw==
     * ReMark : 5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA
     * iscloose : MA==
     * Topics : []
     */

    private String Title;
    private String Logo;
    private String ReMark;
    private String iscloose;
    private List<com.dingtai.newslib3.model.Topics> Topics;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String Logo) {
        this.Logo = Logo;
    }

    public String getReMark() {
        return ReMark;
    }

    public void setReMark(String ReMark) {
        this.ReMark = ReMark;
    }

    public String getIscloose() {
        return iscloose;
    }

    public void setIscloose(String iscloose) {
        this.iscloose = iscloose;
    }

    public List<Topics> getTopics() {
        return this.Topics;
    }

    public void setTopics(List<Topics> topics) {
        this.Topics = topics;
    }
}
