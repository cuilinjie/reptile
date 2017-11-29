package com.clj.reptilehouse.common.constant;

public class JsonConst {
	
	/**
	 * JSON返回交易状态常量类
	 */
	public final static class ServiceStatus {
			
		private ServiceStatus() {}
		/**
		 * 01交易成功
		 */
		public final static String TRANS_OK = "01"; 
		/**
		 * 02交易未成功
		 */
		public final static String TRANS_FAILED = "02"; 
	
	}
	
	/**
	 * 区域类型常量类
	 */
	public final static class AreaType {
			
		private AreaType() {}
		/**
		 * P省份
		 */
		public final static String PROVINCE = "P"; 
		/**
		 * C城市
		 */
		public final static String CITY = "C"; 
		/**
		 * A区
		 */
		public final static String AREA = "A"; 
	
	}
	
	/**
     * 网点操作常量类
     */
    public final static class BankOrgOpera {
            
        private BankOrgOpera() {}
        /**
         * A添加
         */
        public final static String ADD = "A"; 
        /**
         * D删除
         */
        public final static String DELETE = "D"; 
        /**
         * U更新
         */
        public final static String UPDATE = "U"; 
    
    }
	
}
