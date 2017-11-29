package com.clj.reptilehouse.common.page;

import java.util.List;
import java.util.Map;

public interface PageService<T> {

	List<T> listPagination(Map<String,Object> filter);
	
	int count(Map<String,Object> filter);
}
