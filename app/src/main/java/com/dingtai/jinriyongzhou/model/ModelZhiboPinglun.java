package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "ModelZhiboPinglun")
public class ModelZhiboPinglun {
	@DatabaseField(id = true)
	String ID;// 事件ID
	@DatabaseField
	String CreateTime;// 创建时间
	@DatabaseField
	String CommentContent;// 评论内容
	@DatabaseField
	String CommentTime;// 评论时间
	@DatabaseField
	String UserGUID;// 用户Guid
	@DatabaseField
	String UserIP;// 用户ip
	@DatabaseField
	String GetGoodPoint;// 赞
	@DatabaseField
	String UserIcon;// 用户图像
	@DatabaseField
	String UserNickName;// 用户昵称
	@DatabaseField
	String UserName;// 用户名称
	@DatabaseField
	String LimitTime;// 评论时间间隔
	@DatabaseField
	ModelZhiboComments SubCommentList;// （以JSON格式嵌套在SubCommentList下）
	
	// 自定义的楼层显隐
	boolean IsShow = false;

	public boolean isIsShow() {
		return IsShow;
	}

	public void setIsShow(boolean isShow) {
		IsShow = isShow;
	}

	public ModelZhiboComments getSubCommentList() {
		return SubCommentList;
	}

	public void setSubCommentList(ModelZhiboComments subCommentList) {
		SubCommentList = subCommentList;
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

	public String getCommentContent() {
		return CommentContent;
	}

	public void setCommentContent(String commentContent) {
		CommentContent = commentContent;
	}

	public String getCommentTime() {
		return CommentTime;
	}

	public void setCommentTime(String commentTime) {
		CommentTime = commentTime;
	}

	public String getUserGUID() {
		return UserGUID;
	}

	public void setUserGUID(String userGUID) {
		UserGUID = userGUID;
	}

	public String getUserIP() {
		return UserIP;
	}

	public void setUserIP(String userIP) {
		UserIP = userIP;
	}

	public String getGetGoodPoint() {
		return GetGoodPoint;
	}

	public void setGetGoodPoint(String getGoodPoint) {
		GetGoodPoint = getGoodPoint;
	}

	public String getUserIcon() {
		return UserIcon;
	}

	public void setUserIcon(String userIcon) {
		UserIcon = userIcon;
	}

	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getLimitTime() {
		return LimitTime;
	}

	public void setLimitTime(String limitTime) {
		LimitTime = limitTime;
	}

}
