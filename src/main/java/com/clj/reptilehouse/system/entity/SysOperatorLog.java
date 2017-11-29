package com.clj.reptilehouse.system.entity;

import java.io.Serializable;

public class SysOperatorLog implements Serializable {
    /**
     * sys_operator_log.operator_time (操作时间)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private Long operatorTime;
    
    private String operatorTimeStr;

    /**
     * sys_operator_log.log_type (日志类型：1:操作日志)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private Integer logType;

    /**
     * sys_operator_log.operator_model (操作模块)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private String operatorModel;

    /**
     * sys_operator_log.operator_action (操作类型 1:查询 2：新增 3：更新 4：删除)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private Integer operatorAction;
    
    private String operatorActionStr;

    public String getOperatorActionStr() {
		return operatorActionStr;
	}

	public void setOperatorActionStr(String operatorActionStr) {
		this.operatorActionStr = operatorActionStr;
	}

	/**
     * sys_operator_log.operator_persion (操作人)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private String operatorPersion;

    /**
     * sys_operator_log.client_ipaddr (操作人IP地址)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private String clientIpaddr;

    /**
     * sys_operator_log.insert_time (插入时间)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private Long insertTime;

    /**
     * sys_operator_log.operator_context (操作内容)
     * @ibatorgenerated 2016-05-12 17:26:13
     */
    private String operatorContext;

    public Long getOperatorTime() {
        return operatorTime;
    }

    public void setOperatorTime(Long operatorTime) {
        this.operatorTime = operatorTime;
    }

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getOperatorModel() {
        return operatorModel;
    }

    public void setOperatorModel(String operatorModel) {
        this.operatorModel = operatorModel;
    }

    public Integer getOperatorAction() {
        return operatorAction;
    }

    public void setOperatorAction(Integer operatorAction) {
        this.operatorAction = operatorAction;
    }

    public String getOperatorPersion() {
        return operatorPersion;
    }

    public void setOperatorPersion(String operatorPersion) {
        this.operatorPersion = operatorPersion;
    }

    public String getClientIpaddr() {
        return clientIpaddr;
    }

    public void setClientIpaddr(String clientIpaddr) {
        this.clientIpaddr = clientIpaddr;
    }

    public Long getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Long insertTime) {
        this.insertTime = insertTime;
    }

    public String getOperatorContext() {
        return operatorContext;
    }

    public void setOperatorContext(String operatorContext) {
        this.operatorContext = operatorContext;
    }

	public String getOperatorTimeStr() {
		return operatorTimeStr;
	}

	public void setOperatorTimeStr(String operatorTimeStr) {
		this.operatorTimeStr = operatorTimeStr;
	}
    
    
}