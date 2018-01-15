package com.dingtai.jinriyongzhou.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/16 0016.
 */
@DatabaseTable(
        tableName = "PoliticsNewsModel"
)
public class PoliticsNewsModel implements Serializable {
    /**
     * ID : MTIwMDc=
     * AreaID : NDA=
     * PoliticsAreaName : 6Zu26Zm15Yy65YWs5a6J5bGA
     * AreaLogo : aHR0cDovLzExNi42Mi4yOS4yMDo4MDc3L1VwbG9hZHMvSW1hZ2VzL01pZFRodW1ibmFpbC8yMDE3MDgzMTE2MjQxNjU0ODY2MTAwMC5qcGc=
     * ForAPP : MA==
     * ResType : MA==
     * VideoUrl :
     * VideoCount : MA==
     * VideoImageUrl :
     * ResourceGUID : NTIwMDY5YzMtZDU3YS00MDA3LTlkMGMtMjBlOTNmNDNmNGM0
     * ChannelName : 5Y2B5Lmd5aSn
     * Title : 5rC45bee5biC5aeU5a6j6K6y5Zui5Yiw5rGf5rC45a6j6K6y5Y2B5Lmd5aSn57K+56We
     * Summary :
     * SourceForm : 5rC45bee5paw6Ze7572R
     * CreateTime : MjAxNy0xMS0zMCAwODo1MDozNw==
     * UpdateTime : MjAxNy0xMS0zMCAwOToxODozMA==
     * AuditTime : MjAxNy0xMS0zMCAwODo1MTozNw==
     * IsComment : VHJ1ZQ==
     * IsCommentNoName : RmFsc2U=
     * ResourceType : NA==
     * ResourceFlag : Mg==
     * ResourceUrl :
     * PicPath : aHR0cDovL2dkLmhoLmhuLmQ1bXQuY29tLmNuLy9VcGxvYWRzL0ltYWdlcy9NaWRUaHVtYm5haWwvLy8yMDE3MTEzMDA4NTEyNzUyNTg4MTAwMC5qcGc=
     * IsNewTopice : RmFsc2U=
     * FakeReadNo : Mw==
     * GetGoodPoint : MA==
     * ThemeID : MA==
     * ResourceCSS : MQ==
     * UploadPicNames :
     * SmallPicUrl : aHR0cDovLzExNi42Mi4yOS4yMDo4MDc3L1VwbG9hZHMvSW1hZ2VzL01pZFRvbmdMYW4vMjAxNzExMzAwODUxMjc1MjU4ODEwMDAuanBn
     * ChID : NTM1
     * ChannelLogo :
     * ShowOrder : MTIwMDc=
     * CommentNum : MA==
     * Longitude : MA==
     * Latitude : MA==
     * ReadNo : MQ==
     * RPID : MA==
     * CommunityName :
     * TopicChann : []
     * RPNum : MA==
     * BandChID : MA==
     */

    @DatabaseField(defaultValue = "")
    private String ID;
    @DatabaseField(defaultValue = "")
    private String AreaID;
    @DatabaseField(defaultValue = "")
    private String PoliticsAreaName;
    @DatabaseField(defaultValue = "")
    private String AreaLogo;
    @DatabaseField(defaultValue = "")
    private String ForAPP;
    @DatabaseField(defaultValue = "")
    private String ResType;
    @DatabaseField(defaultValue = "")
    private String VideoUrl;
    @DatabaseField(defaultValue = "")
    private String VideoCount;
    @DatabaseField(defaultValue = "")
    private String VideoImageUrl;
    @DatabaseField(defaultValue = "")
    private String ResourceGUID;
    @DatabaseField(defaultValue = "")
    private String ChannelName;
    @DatabaseField(defaultValue = "")
    private String Title;
    @DatabaseField(defaultValue = "")
    private String Summary;
    @DatabaseField(defaultValue = "")
    private String SourceForm;
    @DatabaseField(defaultValue = "")
    private String CreateTime;
    @DatabaseField(defaultValue = "")
    private String UpdateTime;
    @DatabaseField(defaultValue = "")
    private String AuditTime;
    @DatabaseField(defaultValue = "")
    private String IsComment;
    @DatabaseField(defaultValue = "")
    private String IsCommentNoName;
    @DatabaseField(defaultValue = "")
    private String ResourceType;
    @DatabaseField(defaultValue = "")
    private String ResourceFlag;
    @DatabaseField(defaultValue = "")
    private String ResourceUrl;
    @DatabaseField(defaultValue = "")
    private String PicPath;
    @DatabaseField(defaultValue = "")
    private String IsNewTopice;
    @DatabaseField(defaultValue = "")
    private String FakeReadNo;
    @DatabaseField(defaultValue = "")
    private String GetGoodPoint;
    @DatabaseField(defaultValue = "")
    private String ThemeID;
    @DatabaseField(defaultValue = "")
    private String ResourceCSS;
    @DatabaseField(defaultValue = "")
    private String UploadPicNames;
    @DatabaseField(defaultValue = "")
    private String SmallPicUrl;
    @DatabaseField(defaultValue = "")
    private String ChID;
    @DatabaseField(defaultValue = "")
    private String ChannelLogo;
    @DatabaseField(defaultValue = "")
    private String ShowOrder;
    @DatabaseField(defaultValue = "")
    private String CommentNum;
    @DatabaseField(defaultValue = "")
    private String Longitude;
    @DatabaseField(defaultValue = "")
    private String Latitude;
    @DatabaseField(defaultValue = "")
    private String ReadNo;
    @DatabaseField(defaultValue = "")
    private String RPID;
    @DatabaseField(defaultValue = "")
    private String CommunityName;
    @DatabaseField(defaultValue = "")
    private String RPNum;
    @DatabaseField(defaultValue = "")
    private String BandChID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAreaID() {
        return AreaID;
    }

    public void setAreaID(String AreaID) {
        this.AreaID = AreaID;
    }

    public String getPoliticsAreaName() {
        return PoliticsAreaName;
    }

    public void setPoliticsAreaName(String PoliticsAreaName) {
        this.PoliticsAreaName = PoliticsAreaName;
    }

    public String getAreaLogo() {
        return AreaLogo;
    }

    public void setAreaLogo(String AreaLogo) {
        this.AreaLogo = AreaLogo;
    }

    public String getForAPP() {
        return ForAPP;
    }

    public void setForAPP(String ForAPP) {
        this.ForAPP = ForAPP;
    }

    public String getResType() {
        return ResType;
    }

    public void setResType(String ResType) {
        this.ResType = ResType;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public void setVideoUrl(String VideoUrl) {
        this.VideoUrl = VideoUrl;
    }

    public String getVideoCount() {
        return VideoCount;
    }

    public void setVideoCount(String VideoCount) {
        this.VideoCount = VideoCount;
    }

    public String getVideoImageUrl() {
        return VideoImageUrl;
    }

    public void setVideoImageUrl(String VideoImageUrl) {
        this.VideoImageUrl = VideoImageUrl;
    }

    public String getResourceGUID() {
        return ResourceGUID;
    }

    public void setResourceGUID(String ResourceGUID) {
        this.ResourceGUID = ResourceGUID;
    }

    public String getChannelName() {
        return ChannelName;
    }

    public void setChannelName(String ChannelName) {
        this.ChannelName = ChannelName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String Summary) {
        this.Summary = Summary;
    }

    public String getSourceForm() {
        return SourceForm;
    }

    public void setSourceForm(String SourceForm) {
        this.SourceForm = SourceForm;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getAuditTime() {
        return AuditTime;
    }

    public void setAuditTime(String AuditTime) {
        this.AuditTime = AuditTime;
    }

    public String getIsComment() {
        return IsComment;
    }

    public void setIsComment(String IsComment) {
        this.IsComment = IsComment;
    }

    public String getIsCommentNoName() {
        return IsCommentNoName;
    }

    public void setIsCommentNoName(String IsCommentNoName) {
        this.IsCommentNoName = IsCommentNoName;
    }

    public String getResourceType() {
        return ResourceType;
    }

    public void setResourceType(String ResourceType) {
        this.ResourceType = ResourceType;
    }

    public String getResourceFlag() {
        return ResourceFlag;
    }

    public void setResourceFlag(String ResourceFlag) {
        this.ResourceFlag = ResourceFlag;
    }

    public String getResourceUrl() {
        return ResourceUrl;
    }

    public void setResourceUrl(String ResourceUrl) {
        this.ResourceUrl = ResourceUrl;
    }

    public String getPicPath() {
        return PicPath;
    }

    public void setPicPath(String PicPath) {
        this.PicPath = PicPath;
    }

    public String getIsNewTopice() {
        return IsNewTopice;
    }

    public void setIsNewTopice(String IsNewTopice) {
        this.IsNewTopice = IsNewTopice;
    }

    public String getFakeReadNo() {
        return FakeReadNo;
    }

    public void setFakeReadNo(String FakeReadNo) {
        this.FakeReadNo = FakeReadNo;
    }

    public String getGetGoodPoint() {
        return GetGoodPoint;
    }

    public void setGetGoodPoint(String GetGoodPoint) {
        this.GetGoodPoint = GetGoodPoint;
    }

    public String getThemeID() {
        return ThemeID;
    }

    public void setThemeID(String ThemeID) {
        this.ThemeID = ThemeID;
    }

    public String getResourceCSS() {
        return ResourceCSS;
    }

    public void setResourceCSS(String ResourceCSS) {
        this.ResourceCSS = ResourceCSS;
    }

    public String getUploadPicNames() {
        return UploadPicNames;
    }

    public void setUploadPicNames(String UploadPicNames) {
        this.UploadPicNames = UploadPicNames;
    }

    public String getSmallPicUrl() {
        return SmallPicUrl;
    }

    public void setSmallPicUrl(String SmallPicUrl) {
        this.SmallPicUrl = SmallPicUrl;
    }

    public String getChID() {
        return ChID;
    }

    public void setChID(String ChID) {
        this.ChID = ChID;
    }

    public String getChannelLogo() {
        return ChannelLogo;
    }

    public void setChannelLogo(String ChannelLogo) {
        this.ChannelLogo = ChannelLogo;
    }

    public String getShowOrder() {
        return ShowOrder;
    }

    public void setShowOrder(String ShowOrder) {
        this.ShowOrder = ShowOrder;
    }

    public String getCommentNum() {
        return CommentNum;
    }

    public void setCommentNum(String CommentNum) {
        this.CommentNum = CommentNum;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getReadNo() {
        return ReadNo;
    }

    public void setReadNo(String ReadNo) {
        this.ReadNo = ReadNo;
    }

    public String getRPID() {
        return RPID;
    }

    public void setRPID(String RPID) {
        this.RPID = RPID;
    }

    public String getCommunityName() {
        return CommunityName;
    }

    public void setCommunityName(String CommunityName) {
        this.CommunityName = CommunityName;
    }

    public String getRPNum() {
        return RPNum;
    }

    public void setRPNum(String RPNum) {
        this.RPNum = RPNum;
    }

    public String getBandChID() {
        return BandChID;
    }

    public void setBandChID(String BandChID) {
        this.BandChID = BandChID;
    }

}