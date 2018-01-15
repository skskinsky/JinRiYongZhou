package com.dingtai.jinriyongzhou.model;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class SubscribeListsModel {


    /**
     * ID : MQ==
     * PoliticID : NDA=
     * CreateTime : MjAxNi0wNS0wNCAxNjowOToyMg==
     * Status : MQ==
     * IsPolitic : MA==
     * PoliticsAreaName : 6Zu26Zm15Yy65YWs5a6J5bGA
     * SmallPicUrl : aHR0cDovLzExNi42Mi4yOS4yMDo4MDc3L1VwbG9hZHMvMi5qcGc=
     * ReMark : 5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA5YyX5rmW5Yy65YWs5a6J5bGA
     */

    private String ID;
    private String PoliticID;
    private String CreateTime;
    private String Status;
    private String IsPolitic;
    private String PoliticsAreaName;
    private String SmallPicUrl;
    private String ReMark;
    private String AreaId;

    public String getAreaID() {
        return AreaId;
    }

    public void setAreaID(String areaID) {
        AreaId = areaID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPoliticID() {
        return PoliticID;
    }

    public void setPoliticID(String PoliticID) {
        this.PoliticID = PoliticID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getIsPolitic() {
        return IsPolitic;
    }

    public void setIsPolitic(String IsPolitic) {
        this.IsPolitic = IsPolitic;
    }

    public String getPoliticsAreaName() {
        return PoliticsAreaName;
    }

    public void setPoliticsAreaName(String PoliticsAreaName) {
        this.PoliticsAreaName = PoliticsAreaName;
    }

    public String getSmallPicUrl() {
        return SmallPicUrl;
    }

    public void setSmallPicUrl(String SmallPicUrl) {
        this.SmallPicUrl = SmallPicUrl;
    }

    public String getReMark() {
        return ReMark;
    }

    public void setReMark(String ReMark) {
        this.ReMark = ReMark;
    }
}

