package com.dingtai.jinriyongzhou.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "IndexModel")
public class IndexModel {
	@DatabaseField(id = true)
	private String ID = ""; // 模块ID
	@DatabaseField
	private String CreateTime; // 创建时间
	@DatabaseField
	private String moduleName; // 模块名称
	@DatabaseField
	private String moduleType; // 模块类型
	@DatabaseField
	private String moduleLogo; // 模块LOGO
	@DatabaseField
	private String ShowOrder; // 模块排序号
	@DatabaseField
	private String IsDel; // 是否固定
	@DatabaseField
	private String IsHtml; // 是否连接到网页
	@DatabaseField
	private String HtmlUrl; // 网页链接
	@DatabaseField
	private String IsInside; // 是否内置
	@DatabaseField
	private String StID; // 站点ID
	@DatabaseField
	private String JumpTo; // 跳转到
	@DatabaseField
	private String Status; // 状态
	@DatabaseField
	private String ReMark; // 备注
	@DatabaseField
	private String isDelete = "VHJ1ZQ=="; // 是否可删除
	@DatabaseField
	private String beforeIsDel = ""; // 是否可删除
	@DatabaseField
	private String IndexMultiple = "1"; // 模块网页放大倍数


	public String getIsTopices() {
		return IsTopices;
	}

	public void setIsTopices(String isTopices) {
		IsTopices = isTopices;
	}

	@DatabaseField
	private String IsTopices;
	
	public String getBeforeIsDel() {
		return beforeIsDel;
	}

	public void setBeforeIsDel(String beforeIsDel) {
		this.beforeIsDel = beforeIsDel;
	}
	
	public String getIndexMultiple() {
		return IndexMultiple;
	}

	public void setIndexMultiple(String indexMultiple) {
		IndexMultiple = indexMultiple;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
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

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleLogo() {
		return moduleLogo;
	}

	public void setModuleLogo(String moduleLogo) {
		this.moduleLogo = moduleLogo;
	}

	public String getShowOrder() {
		return ShowOrder;
	}

	public void setShowOrder(String showOrder) {
		ShowOrder = showOrder;
	}

	public String getIsDel() {
		return IsDel;
	}

	public void setIsDel(String isDel) {
		IsDel = isDel;
	}

	public String getIsHtml() {
		return IsHtml;
	}

	public void setIsHtml(String isHtml) {
		IsHtml = isHtml;
	}

	public String getHtmlUrl() {
		return HtmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		HtmlUrl = htmlUrl;
	}

	public String getIsInside() {
		return IsInside;
	}

	public void setIsInside(String isInside) {
		IsInside = isInside;
	}

	public String getStID() {
		return StID;
	}

	public void setStID(String stID) {
		StID = stID;
	}

	public String getJumpTo() {
		return JumpTo;
	}

	public void setJumpTo(String jumpTo) {
		JumpTo = jumpTo;
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

}
