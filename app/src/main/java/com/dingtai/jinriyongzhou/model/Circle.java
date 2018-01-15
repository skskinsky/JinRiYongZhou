package com.dingtai.jinriyongzhou.model;

import java.io.Serializable;

public class Circle implements Serializable {

	private String ID;
	private String CreateTime;
	private String CircleName;
	private String CircleLogo;
	private String CircleIntroduce;
	private String Ispublic;
	private String AuditDate;
	private String Status;
	private String Remark;
	private String MediaNum;
	private String FollowNum;
	private String IsFollow;
	private String IsTop;
	
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
	public String getCircleName() {
		return CircleName;
	}
	public void setCircleName(String circleName) {
		CircleName = circleName;
	}
	public String getCircleLogo() {
		return CircleLogo;
	}
	public void setCircleLogo(String circleLogo) {
		CircleLogo = circleLogo;
	}
	public String getCircleIntroduce() {
		return CircleIntroduce;
	}
	public void setCircleIntroduce(String circleIntroduce) {
		CircleIntroduce = circleIntroduce;
	}
	public String getIspublic() {
		return Ispublic;
	}
	public void setIspublic(String ispublic) {
		Ispublic = ispublic;
	}
	public String getAuditDate() {
		return AuditDate;
	}
	public void setAuditDate(String auditDate) {
		AuditDate = auditDate;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getMediaNum() {
		return MediaNum;
	}
	public void setMediaNum(String mediaNum) {
		MediaNum = mediaNum;
	}
	public String getFollowNum() {
		return FollowNum;
	}
	public void setFollowNum(String followNum) {
		FollowNum = followNum;
	}
	public String getIsFollow() {
		return IsFollow;
	}
	public void setIsFollow(String isFollow) {
		IsFollow = isFollow;
	}
	public String getIsTop() {
		return IsTop;
	}
	public void setIsTop(String isTop) {
		IsTop = isTop;
	}

}
