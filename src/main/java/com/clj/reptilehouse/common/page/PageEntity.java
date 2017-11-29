package com.clj.reptilehouse.common.page;

import com.clj.reptilehouse.common.EntityBean;

public class PageEntity extends EntityBean{
	private static final long serialVersionUID = -300790531580509686L;
	protected int pageNum = 1;
	protected int pageSize = 10;
	private int sort = 1;//排序 0正序 ，1 倒序
	private int sortType = 1;//排序类型 0 时间 ，1次数
	private String ids; //多个id
	
	private String ct;//时间字符串
	private String et;
	
	

	public String getCt() {
		return ct;
	}

	public void setCt(String ct) {
		this.ct = ct;
	}

	public String getEt() {
		return et;
	}

	public void setEt(String et) {
		this.et = et;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getSortType() {
		return sortType;
	}
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
