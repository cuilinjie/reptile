/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：eidlink-boss
 * 
 **************************************************************/
package com.clj.reptilehouse.summary.datebase;

/**
 * TODO: DOCUMENT ME!
 * 
 * @author chenwen
 * @date 2015年6月24日
 */
public class MultipleDataSourceAspectAdvice {
  
  private static final String DATASOURCE = "dataSource";
  
  private static final String REPORT_DATASOURCE = "reportDataSource";

  private static final String EIDLINK_DATASOURCE = "eidlinkDataSource";
  
  public void before(){
    MultipleDataSource.setDataSourceKey(REPORT_DATASOURCE);
  }

  public void beforeeidlink(){
	    MultipleDataSource.setDataSourceKey(EIDLINK_DATASOURCE);
	  }
  
  public void after(){
    MultipleDataSource.setDataSourceKey(DATASOURCE);
  }
  
}
