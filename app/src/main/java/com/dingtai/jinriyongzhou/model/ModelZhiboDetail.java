package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "ModelZhiboDetail")
public class ModelZhiboDetail {
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String EventName; // 事件名称
	/**
	 * 事件logo
	 */
	@DatabaseField
	public String EventLogo; // 事件logo
	/**
	 * 内容LOGO
	 */
	@DatabaseField
	public String ContentLogo; // 内容LOGO
	/**
	 * 内容ID
	 */
	@DatabaseField
	public String ID; // 内容ID
	/**
	 * 创建时间
	 */
	@DatabaseField
	public String CreateTime; // 创建时间
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String NewsLiveTitle; // 标题
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String NewsLiveContent; // 内容
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String PicUrl; // 图片路径
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String MediaID; // // 视频id
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String AudioID; // // 音频ID
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String LinkUrl; // // 文章ID
	/**
	 * 事件名称
	 */
	@DatabaseField
	public String SendStatus; // //发送状态
	/**
	 * 用户ID
	 */
	@DatabaseField
	public String AdminID; // //用户ID
	/**
	 * 状态
	 */
	@DatabaseField
	public String Status; // //状态
	/**
	 * 备注
	 */
	@DatabaseField
	public String ReMark; // //备注
	/**
	 * 事件ID
	 */
	@DatabaseField
	public String NewsLiveEventID; // //事件ID
	/**
	 * 样式
	 */
	@DatabaseField
	public String NewsLiveType;//样式
	/**
	 * 音频地址
	 */
	@DatabaseField
	public String AudioUrl;//音频地址
	/**
	 * 视频地址
	 */
	@DatabaseField
	public String MediaUrl;//视频地址
	/**
	 * 视频图片
	 */
	@DatabaseField
	public String MediaPic;//视频图片
	
	@DatabaseField
	public String GoodPoint;
	
	@DatabaseField
	public String IsExsitPoint;
	
	@DatabaseField
	public String UserLogo;
	
	@DatabaseField
	public String UserName;
	
	public boolean isOpen=false;//是否展开
	public String getEventName() {
		return EventName;
	}

	public void setEventName(String eventName) {
		EventName = eventName;
	}

	public String getEventLogo() {
		return EventLogo;
	}

	public void setEventLogo(String eventLogo) {
		EventLogo = eventLogo;
	}

	public String getContentLogo() {
		return ContentLogo;
	}

	public void setContentLogo(String contentLogo) {
		ContentLogo = contentLogo;
	}

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

	public String getNewsLiveTitle() {
		return NewsLiveTitle;
	}

	public void setNewsLiveTitle(String newsLiveTitle) {
		NewsLiveTitle = newsLiveTitle;
	}

	public String getNewsLiveContent() {
		return NewsLiveContent;
	}

	public void setNewsLiveContent(String newsLiveContent) {
		NewsLiveContent = newsLiveContent;
	}

	public String getPicUrl() {
		return PicUrl;
	}

	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}

	public String getMediaID() {
		return MediaID;
	}

	public void setMediaID(String mediaID) {
		MediaID = mediaID;
	}

	public String getAudioID() {
		return AudioID;
	}

	public void setAudioID(String audioID) {
		AudioID = audioID;
	}

	public String getLinkUrl() {
		return LinkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		LinkUrl = linkUrl;
	}

	public String getSendStatus() {
		return SendStatus;
	}

	public void setSendStatus(String sendStatus) {
		SendStatus = sendStatus;
	}

	public String getAdminID() {
		return AdminID;
	}

	public void setAdminID(String adminID) {
		AdminID = adminID;
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

	public String getNewsLiveEventID() {
		return NewsLiveEventID;
	}

	public void setNewsLiveEventID(String newsLiveEventID) {
		NewsLiveEventID = newsLiveEventID;
	}

	public String getNewsLiveType() {
		return NewsLiveType;
	}

	public void setNewsLiveType(String newsLiveType) {
		NewsLiveType = newsLiveType;
	}

	public String getAudioUrl() {
		return AudioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		AudioUrl = audioUrl;
	}

	public String getMediaUrl() {
		return MediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		MediaUrl = mediaUrl;
	}

	public String getMediaPic() {
		return MediaPic;
	}

	public void setMediaPic(String mediaPic) {
		MediaPic = mediaPic;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getGoodPoint() {
		return GoodPoint;
	}

	public void setGoodPoint(String goodPoint) {
		GoodPoint = goodPoint;
	}

	public String getIsExsitPoint() {
		return IsExsitPoint;
	}

	public void setIsExsitPoint(String isExsitPoint) {
		IsExsitPoint = isExsitPoint;
	}

	public String getUserLogo() {
		return UserLogo;
	}

	public void setUserLogo(String userLogo) {
		UserLogo = userLogo;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

}