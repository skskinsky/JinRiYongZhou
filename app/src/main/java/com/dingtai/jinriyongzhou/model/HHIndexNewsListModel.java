/**作    者： 李亚军
 * 时 间：2015-1-14	上午4:59:59
 * 描   述：  
 */
package com.dingtai.jinriyongzhou.model;

import com.dingtai.base.model.NewsPhotoModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 作 者： 李亚军 时 间：2015 上午4:59:59
 * 
 */

public class HHIndexNewsListModel implements Serializable {

	public HHIndexNewsListModel() {
	}

	public void finalize() throws Throwable {

	}


	private String ID;
	
	private String ResourceGUID;
	
	private String Title;

	private String Summary;

	private String SourceForm;
	
	private String PicPath;
	
	private List<NewsADs> NewADs = new ArrayList<NewsADs>();

	private String CreateTime;


	private String UpdateTime;


	private String AuditTime;


	private String IsComment;


	private String IsCommentNoName;

	/***
	 * 1、普通新闻 2、网页新闻 3、图集新闻 4、视频新闻 5、混排新闻 6、音频新闻 7、专题新闻
	 */

	private String ResourceType;

	/***
	 * 1、图集 2、专题 3、视频 4、推广 5、直播 6、本地 7、热点 8、独家 9、问卷 10、图集大通栏 11、专题小通栏 12、深读
	 */
	// 新闻角标
 
	private String ResourceFlag;

	// 新闻地址
 
	private String ResourceUrl;

	// 新闻评论数
 
	private String Comments;
	
	// 1新闻2直播
	 
	private String IndexType;
	
	private String IndexMoreChid;// 横幅
	// 1新闻2直播
	
	private String IndexChannName;
	
	private String ChannelName;

	private String ThemeName;




	// 阅读数
 
	private String FakeReadNo;

	// 新闻点赞数
 
	private String NewsGetGoodPoint;
	

	private String NewsThemeID;

	// 新闻样式
 
	private String ResourceCSS;

	// 新闻类型为图集时在列表显示的图集图片
 
	private String UploadPicNames;

	// 缩略图
 
	private String SmallPicUrl;

	// 栏目ID
 
	private String ChID;
	
	private String ChannelLogo;// 横幅

	// 栏目ID
 
	private String ShowOrder;

	// 评论数量
 
	private String CommentNum;

	// 经度
 
	private String Longitude;

	// 纬度
 
	private String Latitude;

	// 浏览数
 
	private String ReadNo;

	// 图集ID
 
	private String RPID;

	// 音频地址
 
	private String AudioUrl;

	// 音频长度
 
	private String AudioLength;
	
	private String themeid;
	
	private String KeyWords;

	// 绑定的栏目ID
 
	private String BandChID;

	// 事件ID
 
	private String EventID;

	// 事件创建时间
 
	private String EventCreateTime;

	// 事件名
 
	private String EventName;

	// 事件LOGO
 
	private String EventLogo;
	// 事件简介
 
	private String EventSurmmy;
	// 事件内容LOGO
 
	private String EventContentLogo;

	// 事件是否审核
 
	private String EventIsPublic;

	// 事件所属站点
 
	private String EventStID;

	// 事件置顶
 
	private String EventIsTop;

	// 事件推荐
 
	private String EventIsRec;

	
	// 事件排序号
 
	private String EventShowOrder;

	// 事件状态
 
	private String EventStatus;

	// 事件删除状态
 
	private String EventDelStatus;

	// 事件数
 
	private String EventJoinNum;

	// 事件备注
 
	private String EventReMark;

	// 事件GUID
 
	private String EventNewsLiveEventGUID;


	// 1新闻2直播
	
	//是否置顶
	private String IsTop;
	
	private String IsNewTopice;

 




	public String getPicPath() {
		return PicPath;
	}

	public void setPicPath(String picPath) {
		PicPath = picPath;
	}

	public String getIndexMoreChid() {
		return IndexMoreChid;
	}

	public void setIndexMoreChid(String indexMoreChid) {
		IndexMoreChid = indexMoreChid;
	}



	public String getChannelLogo() {
		return ChannelLogo;
	}

	public void setChannelLogo(String channelLogo) {
		ChannelLogo = channelLogo;
	}

	public String getIndexType() {
		return IndexType;
	}

	public void setIndexType(String indexType) {
		IndexType = indexType;
	}

	// 图集
	private NewsPhotoModel photos[];

	public NewsPhotoModel[] getPhotos() {
		return photos;
	}

	public void setPhotos(NewsPhotoModel[] photos) {
		this.photos = photos;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getChannelName() {
		return ChannelName;
	}

	public void setChannelName(String channelName) {
		ChannelName = channelName;
	}

	public String getThemeName() {
		return ThemeName;
	}

	public void setThemeName(String themeName) {
		ThemeName = themeName;
	}

	public String getThemeID() {
		return NewsThemeID;
	}

	public void setThemeID(String themeID) {
		NewsThemeID = themeID;
	}

	public String getResourceGUID() {
		return ResourceGUID;
	}

	public void setResourceGUID(String resourceGUID) {
		ResourceGUID = resourceGUID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getSummary() {
		return Summary;
	}

	public void setSummary(String summary) {
		Summary = summary;
	}

	public String getSourceForm() {
		return SourceForm;
	}

	public void setSourceForm(String sourceForm) {
		SourceForm = sourceForm;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	public String getAuditTime() {
		return AuditTime;
	}

	public void setAuditTime(String auditTime) {
		AuditTime = auditTime;
	}

	public String getIsComment() {
		return IsComment;
	}

	public void setIsComment(String isComment) {
		IsComment = isComment;
	}

	public String getIsCommentNoName() {
		return IsCommentNoName;
	}

	public void setIsCommentNoName(String isCommentNoName) {
		IsCommentNoName = isCommentNoName;
	}

	public String getResourceType() {
		return ResourceType;
	}

	public void setResourceType(String resourceType) {
		ResourceType = resourceType;
	}

	public String getResourceFlag() {
		return ResourceFlag;
	}

	public void setResourceFlag(String resourceFlag) {
		ResourceFlag = resourceFlag;
	}

	public String getResourceUrl() {
		return ResourceUrl;
	}

	public void setResourceUrl(String resourceUrl) {
		ResourceUrl = resourceUrl;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public String getFakeReadNo() {
		return FakeReadNo;
	}

	public void setFakeReadNo(String fakeReadNo) {
		FakeReadNo = fakeReadNo;
	}

	public String getNewsGetGoodPoint() {
		return NewsGetGoodPoint;
	}

	public void setNewsGetGoodPoint(String newsGetGoodPoint) {
		NewsGetGoodPoint = newsGetGoodPoint;
	}

	public String getResourceCSS() {
		return ResourceCSS;
	}

	public void setResourceCSS(String resourceCSS) {
		ResourceCSS = resourceCSS;
	}

	public String getUploadPicNames() {
		return UploadPicNames;
	}

	public void setUploadPicNames(String uploadPicNames) {
		UploadPicNames = uploadPicNames;
	}

	public String getSmallPicUrl() {
		return SmallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		SmallPicUrl = smallPicUrl;
	}

	public String getChID() {
		return ChID;
	}

	public void setChID(String chID) {
		ChID = chID;
	}

	public String getShowOrder() {
		return ShowOrder;
	}

	public void setShowOrder(String showOrder) {
		ShowOrder = showOrder;
	}

	public String getCommentNum() {
		return CommentNum;
	}

	public void setCommentNum(String commentNum) {
		CommentNum = commentNum;
	}

	public String getLongitude() {
		return Longitude;
	}

	public void setLongitude(String longitude) {
		Longitude = longitude;
	}

	public String getLatitude() {
		return Latitude;
	}

	public void setLatitude(String latitude) {
		Latitude = latitude;
	}

	public String getReadNo() {
		return ReadNo;
	}

	public void setReadNo(String readNo) {
		ReadNo = readNo;
	}

	public String getRPID() {
		return RPID;
	}

	public void setRPID(String rPID) {
		RPID = rPID;
	}

	public String getAudioUrl() {
		return AudioUrl;
	}

	public void setAudioUrl(String audioUrl) {
		AudioUrl = audioUrl;
	}

	public String getAudioLength() {
		return AudioLength;
	}

	public void setAudioLength(String audioLength) {
		AudioLength = audioLength;
	}

	public String getBandChID() {
		return BandChID;
	}

	public void setBandChID(String bandChID) {
		BandChID = bandChID;
	}

	public String getEventID() {
		return EventID;
	}

	public void setEventID(String eventID) {
		EventID = eventID;
	}

	public String getEventCreateTime() {
		return EventCreateTime;
	}

	public void setEventCreateTime(String eventCreateTime) {
		EventCreateTime = eventCreateTime;
	}

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

	public String getEventContentLogo() {
		return EventContentLogo;
	}

	public void setEventContentLogo(String eventContentLogo) {
		EventContentLogo = eventContentLogo;
	}

	public String getEventIsPublic() {
		return EventIsPublic;
	}

	public void setEventIsPublic(String eventIsPublic) {
		EventIsPublic = eventIsPublic;
	}

	public String getEventStID() {
		return EventStID;
	}

	public void setEventStID(String eventStID) {
		EventStID = eventStID;
	}

	public String getEventIsTop() {
		return EventIsTop;
	}

	public void setEventIsTop(String eventIsTop) {
		EventIsTop = eventIsTop;
	}

	public String getEventIsRec() {
		return EventIsRec;
	}

	public void setEventIsRec(String eventIsRec) {
		EventIsRec = eventIsRec;
	}

	public String getEventShowOrder() {
		return EventShowOrder;
	}

	public String getEventStatus() {
		return EventStatus;
	}

	public void setEventStatus(String eventStatus) {
		EventStatus = eventStatus;
	}

	public void setEventShowOrder(String eventShowOrder) {
		EventShowOrder = eventShowOrder;
	}

	public String getEventDelStatus() {
		return EventDelStatus;
	}

	public void setEventDelStatus(String eventDelStatus) {
		EventDelStatus = eventDelStatus;
	}

	public String getEventJoinNum() {
		return EventJoinNum;
	}

	public void setEventJoinNum(String eventJoinNum) {
		EventJoinNum = eventJoinNum;
	}

	public String getEventReMark() {
		return EventReMark;
	}

	public void setEventReMark(String eventReMark) {
		EventReMark = eventReMark;
	}

	public String getEventNewsLiveEventGUID() {
		return EventNewsLiveEventGUID;
	}

	public void setEventNewsLiveEventGUID(String eventNewsLiveEventGUID) {
		EventNewsLiveEventGUID = eventNewsLiveEventGUID;
	}



	public String getEventSurmmy() {
		return EventSurmmy;
	}

	public void setEventSurmmy(String eventSurmmy) {
		EventSurmmy = eventSurmmy;
	}

	public String getIndexChannName() {
		return IndexChannName;
	}

	public void setIndexChannName(String indexChannName) {
		IndexChannName = indexChannName;
	}

	public List<NewsADs> getNewADs() {
		return NewADs;
	}

	public void setNewADs(List<NewsADs> newADs) {
		NewADs = newADs;
	}

	public String getThemeid() {
		return themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public String getKeyWords() {
		return KeyWords;
	}

	public void setKeyWords(String keyWords) {
		KeyWords = keyWords;
	}

	public String getNewsThemeID() {
		return NewsThemeID;
	}

	public void setNewsThemeID(String newsThemeID) {
		NewsThemeID = newsThemeID;
	}

	public String getIsTop() {
		return IsTop;
	}

	public void setIsTop(String isTop) {
		IsTop = isTop;
	}

	public String getIsNewTopice() {
		return IsNewTopice;
	}

	public void setIsNewTopice(String isNewTopice) {
		IsNewTopice = isNewTopice;
	}

}
