package com.clj.reptilehouse.common.query;

import java.io.Serializable;

/**
 * 查询比较器
 * @author jiangxs
 * @since 1.0.1(Jul 4, 2015)
 */
public interface Comparator extends Serializable {
	
	public static final LogicalOperator DEFAULT_LOGICAL_OPERATOR = LogicalOperator.AND;

	public LogicalOperator getLogicalOperator();
	
	/**
	 * 返回字段名称（属性名称）
	 * @return
	 * @author jiangxs
	 * @since 1.0.1
	 */
	public String getName();
	
	public void setName(String name);

	/**
	 * 返回比比较符
	 * @return
	 * @author jiangxs
	 * @since 1.0.1
	 */
	public abstract String getExpression();	
}
