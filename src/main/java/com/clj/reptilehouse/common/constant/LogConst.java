package com.clj.reptilehouse.common.constant;

public class LogConst {
	/***
	 * 操作对象  1：查询  2：增加  3：更新  4：删除
	 * @author Administrator
	 *
	 */
	public final static class OperatorAction{ 
		public final static int Search = 1;
		public final static int ADD = 2;
		public final static int UPDATA = 3;
		public final static int DELETE = 4;
		public final static int EXPORT = 5;
		public final static int IMPORT = 6;
	}
	
	/***
	 * 操作模块
	 * @author Administrator
	 *
	 */
	public final static class OperatorModel	{
		/**
		 * 计费管理-服务信息管理模块
		 */
		public final static String ACCOUNT_SERVICE_MANAGE = "010001"; 
		/**
		 * 计费管理-计费策略管理模块
		 */
		public final static String ACCOUNT_POLICY_MANAGE = "010002";
		public final static String RECHARGE_MANAGE = "020001";   //账户充值管理
		public final static String BILLING_MANAGE = "030001";  //账单管理
		public final static String BILLING_IDP_MANAGE = "030002";  //与中胜查询
		public final static String CLINETVALUE_ANALYSIS = "040002";  //价值分析模块
		public final static String BUSINESSVALUE_ANALYSIS = "040001";  //价值分析模块
		public final static String CHARGE_RESULTCODE_MANAGE = "050001";//计费结果码管理
	}

}
