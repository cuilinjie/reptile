/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：eidlink-boss
 * 
 **************************************************************/
package com.clj.reptilehouse.summary.datebase;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 配置多个数据源
 * 
 * @author chenwen
 * @date 2015年6月19日
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
  
  private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

  public static void setDataSourceKey(String dataSource) {
      dataSourceKey.set(dataSource);
  }

  protected Object determineCurrentLookupKey() {
      return dataSourceKey.get();
  }
  
}