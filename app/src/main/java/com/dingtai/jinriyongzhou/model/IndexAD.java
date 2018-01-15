package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "IndexAD")
public class IndexAD {
	@DatabaseField(id = true)
	private String ID;// 广告ID
	@DatabaseField
	private String ADName;// 广告名称
	@DatabaseField
	private String ImgUrl;// 广告图片
	@DatabaseField
	private String LinkUrl;// 广告链接
	@DatabaseField
	private String StID;// 广告站点ID
	@DatabaseField
	private String CreateTime;// 广告创建时间
	@DatabaseField
	private String ChID;// 广告栏目ID
	@DatabaseField
	private String ADType;// 广告类型
	@DatabaseField
	private String ADFor;// 广告来自
	@DatabaseField
	private String RandomNum;// 广告随机数
	@DatabaseField
	private String LinkTo;// 广告跳转
	@DatabaseField
	private String isScrollText = "false";// 是否是滚动广告
	@DatabaseField
	private String isAD = "false";// 是否是广告

	public String getIsAD() {
		return isAD;
	}

	public void setIsAD(String isAD) {
		this.isAD = isAD;
	}

	public String getIsScrollText() {
		return isScrollText;
	}

	public void setIsScrollText(String isScrollText) {
		this.isScrollText = isScrollText;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
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

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
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

	public String getRandomNum() {
		return RandomNum;
	}

	public void setRandomNum(String randomNum) {
		RandomNum = randomNum;
	}

	public String getLinkTo() {
		return LinkTo;
	}

	public void setLinkTo(String linkTo) {
		LinkTo = linkTo;
	}
}
