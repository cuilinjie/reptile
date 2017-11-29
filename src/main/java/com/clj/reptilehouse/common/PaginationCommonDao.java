package com.clj.reptilehouse.common;

import java.util.List;
import java.util.Map;

public interface PaginationCommonDao<T> {
 
	int count(Map<String,Object> filter);
	
	List<T> listPagination(Map<String,Object> filter);
}
