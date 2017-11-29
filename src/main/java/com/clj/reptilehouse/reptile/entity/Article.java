package com.clj.reptilehouse.reptile.entity;

public class Article {
	private long id;
	private String sourceId;
	private String sourceAuthorId;
	private String sourceAuthorName;
	private String source;
	private String title;
	private String shortTitle;
	private String sourceUrl;
	private String sourcePublishTime;
	private int commentNum;
	private int likedNum;
	private int collectedNum;
	private String collectTime;
	private int publishStatus;
	private String content;
	private int hasVideo;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getSourceAuthorId() {
		return sourceAuthorId;
	}
	public void setSourceAuthorId(String sourceAuthorId) {
		this.sourceAuthorId = sourceAuthorId;
	}
	public String getSourceAuthorName() {
		return sourceAuthorName;
	}
	public void setSourceAuthorName(String sourceAuthorName) {
		this.sourceAuthorName = sourceAuthorName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getLikedNum() {
		return likedNum;
	}
	public void setLikedNum(int likedNum) {
		this.likedNum = likedNum;
	}
	public int getCollectedNum() {
		return collectedNum;
	}
	public void setCollectedNum(int collectedNum) {
		this.collectedNum = collectedNum;
	}
	public String getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
	public String getSourcePublishTime() {
		return sourcePublishTime;
	}
	public void setSourcePublishTime(String sourcePublishTime) {
		this.sourcePublishTime = sourcePublishTime;
	}
	public int getPublishStatus() {
		return publishStatus;
	}
	public void setPublishStatus(int publishStatus) {
		this.publishStatus = publishStatus;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getHasVideo() {
		return hasVideo;
	}
	public void setHasVideo(int hasVideo) {
		this.hasVideo = hasVideo;
	}	
}
