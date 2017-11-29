package com.clj.reptilehouse.common.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LigerUIPage<T> {

	private long total;
	private List<T> Rows = new ArrayList<T>();
	private long costTime;//unit seconds
	private PageService<T> pageService;
	
	public long getTotal() {
		return total;
	}
	public List<T> getRows() {
		return Rows;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public void setRows(List<T> rows) {
		Rows = rows;
	}
	public long getCostTime() {
		return costTime;
	}
	public void setCostTime(long costTime) {
		this.costTime = costTime;
	}
	public void setPageService(PageService<T> pageService) {
		this.pageService = pageService;
	}
	
	public void build(Map<String,Object> filter){
		this.total = pageService.count(filter);
		if(this.total>0){
			this.Rows = pageService.listPagination(filter);
		}		
	}
}
