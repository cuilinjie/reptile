package com.clj.reptilehouse.common.util;

import java.io.IOException;
import java.io.Writer;

/**
 * 呈现标签辅助类
 * @author king
 *
 */
public class RendererUtil {
    public static void write(Writer out, String content) throws IOException {
        out.write(content);
    }
	public static void writeBeginTag(Writer out, String tagName) throws IOException {
		out.write("<");
		out.write(tagName);
	}
	public static  void writeCloseBeginTag(Writer out) throws IOException {
		out.write(">");
	}
	public static  void writeCloseTag(Writer out) throws IOException {
		out.write("/>");
	}
	public static  void writeEndTag(Writer out, String tagName) throws IOException {
		out.write("</");
		out.write(tagName);
		out.write(">");
	}
	public static  void writeAttribute(Writer out, String name, String value) throws IOException {
		out.write(" ");
		out.write(name);
		out.write("=\"");
		out.write(value==null?"":value);
		out.write("\"");
	}
	public static  void writeAttribute(Writer out, String name) throws IOException {
		out.write(" ");
		out.write(name);
	}
	public static  void writeStyleAttribute(Writer out, String name, String value) throws IOException {
		out.write(name);
		out.write(":");
		out.write(value==null?"":value);
		out.write(";");
	}
}
