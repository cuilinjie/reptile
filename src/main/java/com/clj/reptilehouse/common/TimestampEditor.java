package com.clj.reptilehouse.common;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.util.StringUtils;

public class TimestampEditor extends PropertyEditorSupport {
	
	private String format = "yyyy-MM-dd"; // 缺省

	public void setFormat(String format) {
		this.format = format;
	}

	public void setAsText(String text) throws IllegalArgumentException {

		if (text != null && text!=""|| StringUtils.hasText(text)) {
			try {
				setValue(new Timestamp(
						(new SimpleDateFormat(this.format).parse(text)).getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
