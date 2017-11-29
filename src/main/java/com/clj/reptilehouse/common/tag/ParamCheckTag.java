package com.clj.reptilehouse.common.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.clj.reptilehouse.common.util.ServiceUtil;
import com.clj.reptilehouse.system.service.ParamItemService;


public class ParamCheckTag extends BodyTagSupport {

	private static final long serialVersionUID = 8880L;

	private static ParamItemService paramItemService = ServiceUtil.getParamService();
	
	private String paramCode;
	private String checkWay;
	private String checkValue;

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public void setCheckWay(String checkWay) {
		this.checkWay = checkWay;
	}
	
	public void setCheckValue(String checkValue) {
		this.checkValue = checkValue;
	}

	public int doStartTag() throws JspException {
		if (paramItemService.checkParam(paramCode, checkWay, checkValue )) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}
}
