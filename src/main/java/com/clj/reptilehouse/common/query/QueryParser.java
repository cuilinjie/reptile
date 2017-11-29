package com.clj.reptilehouse.common.query;

/**
 * Query 解析器
 * @author jiangxs
 * @since 1.0.1(Jul 4, 2015)
 */
public interface QueryParser {

	/**
	 * 解析Query
	 * @param query
	 * @return
	 * @author jiangxs
	 * @since 1.0.1
	 */
	public StringBuffer parse(Query query); 
}
