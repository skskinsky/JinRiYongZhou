package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MediaInfo")
public class MediaInfo {
	
	//{"MediaInfo":753
	//[{"ID":"NzUz","PaiCount":"MTI=","DigCount":"MA==",
	//"UploadType":"MA==","Tag":"","FlvUrl":"TWVkaWFzL1VwbG9hZHMvc3RyZWFtcy9fZGVmaW5zdF8vTVA0LzIwMTQxMjExMTUwNDM2MDEyNjkyMDAwLm1wNCI=",
	//"Detail":"bnLXNpemU6vZEt546L54+e5Li55aSn6IOG6KO46IOM5Ye66ZWcIOiHquiEseS4iuiho+S4jumDrea2m+a8lOa/gOaDheaIjyZuYnNwOzwvc3Bhbj48L3A+",
	//"ID2":"Nzc2OTU4NDItZDAxYS00NzBmLTgxYzItZGE2MmZjOGMzMDdj","UploadDate":"MjAxNC8xMi8xMSAxNTowNDozNw==","UserName":"ZDVtdGFkbWlu"
	//,"Name":"6YOt5rab5ryU5r+A5oOF5oiPIA==",
	//"ImageUrl":"NzUz","MediaUrl":"6YOt5rab5ryU5r+A5oOF5oiPIA==",
	//"ShareUrl":"","FileSize":"MjAwMC8xLzEgMDowMDo1NQ==","fenxiang":"c2hhcmUvc2hhcmVzLmFzcHg/aWQ9NzUz"}]}
	
	@DatabaseField(id = true)
	private String ID;
	@DatabaseField
	private String PaiCount;
	@DatabaseField
	private String DigCount;
	@DatabaseField
	private String UploadType;
	@DatabaseField
	private String Tag;
	@DatabaseField
	private String FlvUrl;
	@DatabaseField
	private String Detail;
	@DatabaseField
	private String ID2;
	@DatabaseField
	private String UploadDate;
	@DatabaseField
	private String UserName;
	@DatabaseField
	private String Name;
	@DatabaseField
	private String ImageUrl;
	@DatabaseField
	private String MediaUrl;
	@DatabaseField
	private String ShareUrl;
	@DatabaseField
	private String FileSize;
	@DatabaseField
	private String fenxiang;
	
	@DatabaseField
	private String ChannelName;
	
	
	
	public MediaInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public String getChannelName() {
		return ChannelName;
	}


	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}


	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getPaiCount() {
		return PaiCount;
	}
	public void setPaiCount(String paiCount) {
		PaiCount = paiCount;
	}
	public String getDigCount() {
		return DigCount;
	}
	public void setDigCount(String digCount) {
		DigCount = digCount;
	}
	public String getUploadType() {
		return UploadType;
	}
	public void setUploadType(String uploadType) {
		UploadType = uploadType;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getFlvUrl() {
		return FlvUrl;
	}
	public void setFlvUrl(String flvUrl) {
		FlvUrl = flvUrl;
	}
	public String getDetail() {
		return Detail;
	}
	public void setDetail(String detail) {
		Detail = detail;
	}
	public String getID2() {
		return ID2;
	}
	public void setID2(String iD2) {
		ID2 = iD2;
	}
	public String getUploadDate() {
		return UploadDate;
	}
	public void setUploadDate(String uploadDate) {
		UploadDate = uploadDate;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getMediaUrl() {
		return MediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		MediaUrl = mediaUrl;
	}
	public String getShareUrl() {
		return ShareUrl;
	}
	public void setShareUrl(String shareUrl) {
		ShareUrl = shareUrl;
	}
	public String getFileSize() {
		return FileSize;
	}
	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}
	public String getFenxiang() {
		return fenxiang;
	}
	public void setFenxiang(String fenxiang) {
		this.fenxiang = fenxiang;
	}
	
	

}
