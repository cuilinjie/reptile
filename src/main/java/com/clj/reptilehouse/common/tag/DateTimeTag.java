package com.clj.reptilehouse.common.tag;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.lang3.StringUtils;

public class DateTimeTag extends BodyTagSupport {

	private static final long serialVersionUID = 8883L;

	private String value;
	private String format;

    public void setValue(String value) {  
        this.value = value;  
    }  

    public void setFormat(String format) {  
        this.format = format;  
    }  

	public int doStartTag() throws JspException {
		if( StringUtils.isBlank(value) ){
			return 0;
		}
        String val = ""+value;  
        long time = Long.valueOf(val);  
        Calendar c = Calendar.getInstance();  
        c.setTimeInMillis(time);  
        if( format == null ){
        	format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat dateformat =new SimpleDateFormat(format);  
        String s = dateformat.format(c.getTime());  
        try {  
            pageContext.getOut().write(s);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return super.doStartTag();  
	}
}
