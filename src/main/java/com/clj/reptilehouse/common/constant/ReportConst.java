/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：eidlink-boss
 * 
 **************************************************************/
package com.clj.reptilehouse.common.constant;

/**
 * ReportConst.java  报表常量类
 * 
 * @author chenwen
 * @date 2015年6月24日
 */
public class ReportConst {
  
  public final static class UserType {
    
    private UserType() {}
  
    /**
     * 累计用户
     * 
     */
    public final static String ACCUMULATIVE = "A001";
    
    /**
     * 新增用户[月]
     * 
     */
    public final static String ADD_MONTH = "A002";
    
    /**
     * 新增用户[日]
     * 
     */
    public final static String ADD_DAY = "A002";
    
    /**
     * 活跃用户[月]
     * 
     */
    public final static String ACTIVE_MONTH = "A003";
    
    /**
     * 活跃用户[日]
     * 
     */
    public final static String ACTIVE_DAY = "A003";
    
  }

}
