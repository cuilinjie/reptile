package com.clj.reptilehouse.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogOperator {
	
	
	@Autowired
	private SysOperatorLogService sysOperatorLogService;
	
	/**
	 * 
	 * @param operatorModel 操作管理模块,调用方法 LogConst.OperatorModel.
	 * @param operatorAction 操作对象【查询，新增，更新，删除，导出，导入】,调用方法 LogConst.OperatorAction.
	 * @param operatorContext 操作内容
	 * @param operatorPersion 操作人
	 * @param clientIpaddr 客户IP地址
	 */
	public void addLog(String operatorModel,int operatorAction,String operatorContext,String operatorPersion,String clientIpaddr){
		sysOperatorLogService.addOperatorLog(operatorModel, operatorAction, operatorContext, operatorPersion, clientIpaddr);
	}

}
