package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "ModelZhiboHuifu")
public class ModelZhiboHuifu {

	@DatabaseField(id = true)
	String ID;// 回复ID
	@DatabaseField
	String CommentContent;// 评论内容
	@DatabaseField
	String CommentTime;// 评论时间
	@DatabaseField
	String GetGoodPoint;// 赞
	@DatabaseField
	String UserGUID;// 用户GUID
	@DatabaseField
	String UserName;// 用户名
	@DatabaseField
	String UserIcon;// 用户头像
	@DatabaseField
	String UserNickName;// 用户昵称

	public String getUserNickName() {
		return UserNickName;
	}

	public void setUserNickName(String userNickName) {
		UserNickName = userNickName;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public String getGetGoodPoint() {
		return GetGoodPoint;
	}

	public void setGetGoodPoint(String getGoodPoint) {
		GetGoodPoint = getGoodPoint;
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