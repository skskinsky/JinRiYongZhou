package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "MediaList")
public class MediaList implements Serializable{
	
	//{"MediaList":
	//[{"ID":"NzQ3","Detail":"aIjyZuYnNwOzwvc3Bhbj48L3A+",
	//"ID2":"YTE3YmE5NzYtMjFmMS00YTM4LWJhMzItMDQ1YWYyMTdiOTZi",
	//"UploadDate":"MjAxNC0xMi0xMSAxNTowMjoxOQ==","UserName":"ZDVtdGFkbWlu",
	//"Name":"546L54+e5Li55aSn6IOG6KO46IOM5Ye66ZWcIOiHquiEseS4iuiho+S4jumDrea2m+a8lOa/gOaDheaIjyA=",
	//"ImageUrl":"aHR0cDovL3N0YW5kYXJkLXZpZGVvLWZpbGUuZDVtdC5jb20uY24vSW1hZ2VzLzIwMTQxMjExMTUwMjE2OTYwNTU3MDAwLmpwZw==",
	//"MediaUrl":"546L54+e5Li55aSn6IOG6KO46IOM5Ye66ZWcIOiHquiEseS4iuiho+S4jumDrea2m+a8lOa/gOaDheaIjyA=","DigCount":"MA==",
	//"PaiCount":"MA==","TotalView":"MA==","Tag":"","IsBest":"VHJ1ZQ==","UserTop":"VHJ1ZQ==","FileSize":"MjAwMC8xLzEgMDowMDo1NQ==",
	//"ChannelID":"YjdjNmZhZmMtZmZmMy00MTBhLTlkODgtYzUxMDUyNGYzYTU2","ParentID":"YjdjNmZhZmMtZmZmMy00MTBhLTlkODgtYzUxMDUyNGYzYTU2"},
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	private String ID;
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
	private String DigCount;
	@DatabaseField
	private String PaiCount;
	@DatabaseField
	private String TotalView;
	@DatabaseField
	private String Tag;
	/**
	 * 评论数量
	 */
	@DatabaseField
	private String CommentNum;
	
	/**
	 * 是否推荐
	 */
	@DatabaseField
	private String IsBest;
	/**
	 * 是否置顶
	 */
	@DatabaseField
	private String UserTop;
	@DatabaseField
	private String FileSize;
	@DatabaseField
	private String ChannelID;
	@DatabaseField
	private String ParentID;
	/**
	 * 转换状态
	 *  0：未转换
		2：转换中
		3：转换成功
		4：转换失败

	 */
	@DatabaseField
	private String IsConverted;
	/**
	 * 是否审核 Flase：未审核 True：已审核
	 */
	@DatabaseField
	private String IsApproved;
	@DatabaseField
	private String ClickNum;
	@DatabaseField
	private String GoodPoint;
	@DatabaseField
	private String IsExsitPoint;
	
	public String getIsConverted() {
		return IsConverted;
	}
	public void setIsConverted(String isConverted) {
		IsConverted = isConverted;
	}
	public String getIsApproved() {
		return IsApproved;
	}
	public void setIsApproved(String isApproved) {
		IsApproved = isApproved;
	}
	public String getCommentNum() {
		return CommentNum;
	}
	public void setCommentNum(String commentNum) {
		CommentNum = commentNum;
	}
	public MediaList() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
	public String getDigCount() {
		return DigCount;
	}
	public void setDigCount(String digCount) {
		DigCount = digCount;
	}
	public String getPaiCount() {
		return PaiCount;
	}
	public void setPaiCount(String paiCount) {
		PaiCount = paiCount;
	}
	public String getTotalView() {
		return TotalView;
	}
	public void setTotalView(String totalView) {
		TotalView = totalView;
	}
	public String getTag() {
		return Tag;
	}
	public void setTag(String tag) {
		Tag = tag;
	}
	public String getIsBest() {
		return IsBest;
	}
	public void setIsBest(String isBest) {
		IsBest = isBest;
	}
	public String getUserTop() {
		return UserTop;
	}
	public void setUserTop(String userTop) {
		UserTop = userTop;
	}
	public String getFileSize() {
		return FileSize;
	}
	public void setFileSize(String fileSize) {
		FileSize = fileSize;
	}
	public String getChannelID() {
		return ChannelID;
	}
	public void setChannelID(String channelID) {
		ChannelID = channelID;
	}
	public String getParentID() {
		return ParentID;
	}
	public void setParentID(String parentID) {
		ParentID = parentID;
	}
	public String getClickNum() {
		return ClickNum;
	}
	public void setClickNum(String clickNum) {
		ClickNum = clickNum;
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
	
	
	
	

}
