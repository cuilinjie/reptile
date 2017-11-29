package com.clj.reptilehouse.common.view;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * Plain text view
 * @author jxs
 * @since 1.0.1
 */
public class PlainTextView  extends AbstractView {
	
	private static final String MIME = "text/plain";
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	private String text;
	private String charset;
	
	/**
	 * Construct a PlainTextView with UTF-8 charset
	 * @param text
	 */
	public PlainTextView(String text) {
		this(text, DEFAULT_CHARSET);
	}
	
	public PlainTextView(String text, String charset) {
		this.setText(text);
		this.setContentType(MIME);
		this.charset = charset;
	}
	
	public PlainTextView(String text, String charset, String mime) {
		this.setText(text);
		this.setContentType(mime);
		this.charset = charset;
	}
	
	
	public String getCharset() {
		return charset;
	}


	public PlainTextView setCharset(String charset) {
		this.charset = charset;
		return this;
	}


	public String getText() {
		return text;
	}


	public PlainTextView setText(String text) {
		this.text = text;
		return this;
	}
	

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos.write(this.text.getBytes(this.charset));
		this.writeToResponse(response, baos);
	}
}
