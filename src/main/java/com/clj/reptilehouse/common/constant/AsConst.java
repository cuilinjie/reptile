/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：eidlink-boss
 * 
 **************************************************************/
package com.clj.reptilehouse.common.constant;

/**
 * AsConst.java  As相关信息常量类
 * 
 * @author chenwen
 * @date 2015年6月24日
 */
public class AsConst {
  
  public final static class ChangeType {
    
    private ChangeType() {}
  
    /**
     * 普通信息
     * 
     */
    public final static String ORDINARY_INFORMATION = "01";
    
    /**
     * 关键信息
     * 
     */
    public final static String IMPORTANT_INFORMATION = "02";
    
    /**
     * 秘钥信息
     * 
     */
    public final static String ASKEY_INFORMATION = "03";
    
  }
  
  /**
   * 托管标识
   */
  public final static class AsType {
	    
	    private AsType() {}
	  
	    /**
	     * 托管
	     * 
	     */
	    public final static String YES = "00";
	    
	    /**
	     * 非托管
	     * 
	     */
	    public final static String NO = "01";
	    
	    
	  }

}
