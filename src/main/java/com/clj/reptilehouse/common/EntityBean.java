package com.clj.reptilehouse.common;

import java.io.Serializable;

public class EntityBean implements Serializable{
	private static final long serialVersionUID = -1202833984708451998L;
	private Long id;
	private Long createdTime;
	private Long modifiedTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}
	public Long getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Long modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
}
