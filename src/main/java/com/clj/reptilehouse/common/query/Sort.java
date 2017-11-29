package com.clj.reptilehouse.common.query;

import java.io.Serializable;

public class Sort implements Serializable {
	
	private static final long serialVersionUID = -180616073336552137L;

	private String name;
	
	private SortDirection direction;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SortDirection getDirection() {
		return direction;
	}

	public void setDirection(SortDirection direction) {
		this.direction = direction;
	}
	
}
