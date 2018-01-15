package com.dingtai.jinriyongzhou.model;

public class MediaComment {
	//{"MediaComment":[
	//{"CommentContent":"cXdzYXNhc2F3cQ==","CommentAuditStatus":"MQ==",
	//"CommentTime":"MjAxNC0xMS0yNiAxMTowMDo0MQ==","GetGoodPoint":"MA==",
	//"ID":"NQ==","MID":"MjA5ZWRhMjktMjhiMC00ZjRkLTlkNTMtMzdjOTc2NTM1NWNh",
	//"UserGUID":"ODcwNTJlNjUtYjkzOS00ZmZkLWE3ODUtMTZiOTRlZjdjOGZl","UserName":"","UserIcon":""},
	/**
	 * 评论内容
	 */
  private String CommentContent;
  /**
   * 评论状态
   */
  private String CommentAuditStatus;
  /**
   * 评论时间
   */
  private String CommentTime;
  /**
   * 赞
   */
  private String GetGoodPoint;
  /**
   * 评论ID
   */
  private String ID;
  /**
   * 视频GUID
   */
  private String MID;
  /**
   * 用户GUID
   */
  private String UserGUID;
  /**
   * 用户名
   */
  private String UserName;
  /**
   * 用户头像
   */
  private String UserIcon;
  
  /**
   * 用户昵称
   */
  private String UserNickName;
  
  
  
public String getUserNickName() {
	return UserNickName;
}
public void setUserNickName(String userNickName) {
	UserNickName = userNickName;
}
public String getCommentContent() {
	return CommentContent;
}
public void setCommentContent(String commentContent) {
	CommentContent = commentContent;
}
public String getCommentAuditStatus() {
	return CommentAuditStatus;
}
public void setCommentAuditStatus(String commentAuditStatus) {
	CommentAuditStatus = commentAuditStatus;
}
public String getCommentTime() {
	return CommentTime;
}
public void setCommentTime(String commentTime) {
	CommentTime = commentTime;
}
public String getGetGoodPoint() {
	return GetGoodPoint;
}
public void setGetGoodPoint(String getGoodPoint) {
	GetGoodPoint = getGoodPoint;
}
public String getID() {
	return ID;
}
public void setID(String iD) {
	ID = iD;
}
public String getMID() {
	return MID;
}
public void setMID(String mID) {
	MID = mID;
}
public String getUserGUID() {
	return UserGUID;
}
public void setUserGUID(String userGUID) {
	UserGUID = userGUID;
}
public String getUserName() {
	return UserName;
}
public void setUserName(String userName) {
	UserName = userName;
}
public String getUserIcon() {
	return UserIcon;
}
public void setUserIcon(String userIcon) {
	UserIcon = userIcon;
}
  
  
 

}
