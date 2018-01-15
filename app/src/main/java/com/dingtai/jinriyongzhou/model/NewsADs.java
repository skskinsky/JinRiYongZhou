package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "NewsADs")
public class NewsADs implements Serializable{
	@DatabaseField(id = true)
	private String ID;
	@DatabaseField
	private String CreateTime;
	@DatabaseField
	private String  ADName; 
	@DatabaseField
	private String ImgUrl ;
	@DatabaseField
	private String LinkUrl;
	@DatabaseField
	private String StID ;
	@DatabaseField
	private String ChID ;
	@DatabaseField
	private String ADType ;
	@DatabaseField
	private String ADFor;
	@DatabaseField
	private String LinkTo ;
	@DatabaseField
	private String BeginDate;
	@DatabaseField
	private String EndDate;
	@DatabaseField
	private String ADPositionID;
	@DatabaseField
	private String RandomNum;
	@DatabaseField
	private String Status;
	@DatabaseField
	private String ReMark;
	@DatabaseField
	private String ADContent;
	@DatabaseField
	private String MediaID;
	@DatabaseField
	private String IsIndexAD;
	@DatabaseField
	private String GoodType;
	@DatabaseField
	private String CreateAdmin;
	@DatabaseField
	private String ShowOrder;
	@DatabaseField
	private String IsChannel;
	@DatabaseField
	private String NewsType;
	@DatabaseField
	private String ResType;
	@DatabaseField
	private String ResUrl;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getADName() {
		return ADName;
	}
	public void setADName(String aDName) {
		ADName = aDName;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return LinkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		LinkUrl = linkUrl;
	}
	public String getStID() {
		return StID;
	}
	public void setStID(String stID) {
		StID = stID;
	}
	public String getChID() {
		return ChID;
	}
	public void setChID(String chID) {
		ChID = chID;
	}
	public String getADType() {
		return ADType;
	}
	public void setADType(String aDType) {
		ADType = aDType;
	}
	public String getADFor() {
		return ADFor;
	}
	public void setADFor(String aDFor) {
		ADFor = aDFor;
	}
	public String getLinkTo() {
		return LinkTo;
	}
	public void setLinkTo(String linkTo) {
		LinkTo = linkTo;
	}
	public String getBeginDate() {
		return BeginDate;
	}
	public void setBeginDate(String beginDate) {
		BeginDate = beginDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getADPositionID() {
		return ADPositionID;
	}
	public void setADPositionID(String aDPositionID) {
		ADPositionID = aDPositionID;
	}
	public String getRandomNum() {
		return RandomNum;
	}
	public void setRandomNum(String randomNum) {
		RandomNum = randomNum;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getReMark() {
		return ReMark;
	}
	public void setReMark(String reMark) {
		ReMark = reMark;
	}
	public String getADContent() {
		return ADContent;
	}
	public void setADContent(String aDContent) {
		ADContent = aDContent;
	}
	public String getMediaID() {
		return MediaID;
	}
	public void setMediaID(String mediaID) {
		MediaID = mediaID;
	}
	public String getIsIndexAD() {
		return IsIndexAD;
	}
	public void setIsIndexAD(String isIndexAD) {
		IsIndexAD = isIndexAD;
	}
	public String getGoodType() {
		return GoodType;
	}
	public void setGoodType(String goodType) {
		GoodType = goodType;
	}
	public String getCreateAdmin() {
		return CreateAdmin;
	}
	public void setCreateAdmin(String createAdmin) {
		CreateAdmin = createAdmin;
	}
	public String getShowOrder() {
		return ShowOrder;
	}
	public void setShowOrder(String showOrder) {
		ShowOrder = showOrder;
	}
	public String getIsChannel() {
		return IsChannel;
	}
	public void setIsChannel(String isChannel) {
		IsChannel = isChannel;
	}
	public String getNewsType() {
		return NewsType;
	}
	public void setNewsType(String newsType) {
		NewsType = newsType;
	}
	public String getResType() {
		return ResType;
	}
	public void setResType(String resType) {
		ResType = resType;
	}
	public String getResUrl() {
		return ResUrl;
	}
	public void setResUrl(String resUrl) {
		ResUrl = resUrl;
	}
	

}
