package com.clj.reptilehouse.system.entity;import com.clj.reptilehouse.common.EntityBean;/******************************************************************************* * javaBeans * sys_param_item --> SysParamItem  * <table explanation> * @author 2015-11-27 14:04:52 *  */	public class SysParamItem extends EntityBean {	private static final long serialVersionUID = 1993864368762336709L;	/**  **/	private Long grpId;	/** 参数名称 **/	private String code;	/** 显示名称 **/	private String name;	/** 参数序号,用于参数组内排序 **/	private int no;	/** 参数值类型，其中：
            0代表字符串；
            1代表整数；
            2代表浮点数；
            3代表日期时间（如2000-01-01 10:00:00)；
            4代表日期（如2000-01-01）；
            5代表时间（如10:00:00）； **/	private String valType;	/** 把参数值转换为文本保存 **/	private String valText;	/** 0隐藏
            1显示 **/	private Integer status;	/** 备注（描述等） **/	private String remark;	public Long getGrpId() {		return grpId;	}	public void setGrpId(Long grpId) {		this.grpId = grpId;	}	public String getCode() {		return code;	}	public void setCode(String code) {		this.code = code;	}	public String getName() {		return name;	}	public void setName(String name) {		this.name = name;	}	public int getNo() {		return no;	}	public void setNo(int no) {		this.no = no;	}	public String getValType() {		return valType;	}	public void setValType(String valType) {		this.valType = valType;	}	public String getValText() {		return valText;	}	public void setValText(String valText) {		this.valText = valText;	}	public Integer getStatus() {		return status;	}	public void setStatus(Integer status) {		this.status = status;	}	public String getRemark() {		return remark;	}	public void setRemark(String remark) {		this.remark = remark;	}	//override toString Method 	public String toString() {		StringBuffer sb=new StringBuffer();		sb.append("{");		sb.append("'id':'"+this.getId()+"',");		sb.append("'grpId':'"+this.getGrpId()+"',");		sb.append("'code':'"+this.getCode()+"',");		sb.append("'name':'"+this.getName()+"',");		sb.append("'no':'"+this.getNo()+"',");		sb.append("'valType':'"+this.getValType()+"',");		sb.append("'valText':'"+this.getValText()+"',");		sb.append("'status':'"+this.getStatus()+"',");		sb.append("'remark':'"+this.getRemark()+"',");		sb.append("'createdTime':'"+this.getCreatedTime()+"',");		sb.append("'modifiedTime':'"+this.getModifiedTime()+"'");		sb.append("}");		return sb.toString();	}}