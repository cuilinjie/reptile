/**************************************************************
 * Copyright © 2015-2020 www.eidlink.com All rights reserved.
 *
 * 系统名称：eidlink-boss
 * 
 **************************************************************/
package com.clj.reptilehouse.common.constant;

/**
 * AsConst.java  Ap相关信息常量类
 * 
 * @author chenwen
 * @date 2015年6月24日
 */
public class ApConst {
  
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
    public final static String APKEY_INFORMATION = "03";
    
    /**
     * 服务信息
     * 
     */
    public final static String SERVICE_INFORMATION = "04";
    
  }
  
}
