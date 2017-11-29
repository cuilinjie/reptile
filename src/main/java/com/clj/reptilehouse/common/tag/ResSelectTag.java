package com.clj.reptilehouse.common.tag;

import java.io.IOException;
import java.util.List;








import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.clj.reptilehouse.common.Global;
import com.clj.reptilehouse.common.SampleResItem;
import com.clj.reptilehouse.common.tree.TreeUtils;
import com.clj.reptilehouse.common.util.RendererUtil;
import com.clj.reptilehouse.common.util.ServiceUtil;
import com.clj.reptilehouse.system.service.ResItemService;


public class ResSelectTag extends BodyTagSupport {

	private static final long serialVersionUID = 8881L;
	
	private static ResItemService resService = ServiceUtil.getResService();

	private String code;			// 特定属性,对应PubResItem表的资源组编码
	private String filter;

	private Object value;			// 选中的值	
	private String prompt;			// 默认值行显示内容	
	private String promptValue;		// 默认值内容	
	
	private String id;
	private String name;
	private String style;
	private String lclass;
	private String ltype;

	private String onchange;
	private String onblur;	
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public void setPromptValue(String promptValue) {
		this.promptValue = promptValue;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLclass(String lclass) {
		this.lclass = lclass;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	
	public void setLtype(String ltype) {
		this.ltype = ltype;
	}

	public void setOnchange(String onchange) {
		this.onchange = onchange;
	}

	public void setOnblur(String onblur) {
		this.onblur = onblur;
	}

    public ResSelectTag() {
        try {
            Init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void Init() throws Exception {
    }
    
	public int doEndTag() throws JspException{
		List<SampleResItem> items = resService.findSampleResItems(code, filter);
		JspWriter out = pageContext.getOut();
		try {
			RendererUtil.writeBeginTag(out, "select");
			if( id != null){
				RendererUtil.writeAttribute(out, "id", id);
			}
			if (name != null) {
				RendererUtil.writeAttribute(out, "name", name);
			}
			if (style != null) {
				RendererUtil.writeAttribute(out, "style", style);
			}
			if (lclass != null) {
				RendererUtil.writeAttribute(out, "class", lclass);
			}
			if (ltype != null) {
				RendererUtil.writeAttribute(out, "ltype", ltype);
			}
			if (onchange != null) {
				RendererUtil.writeAttribute(out, "onchange", onchange);
			}
			if (onblur != null) {
				RendererUtil.writeAttribute(out, "onblur", onblur);
			}
			RendererUtil.writeCloseBeginTag(out);
			
			if( prompt!= null ){
				RendererUtil.writeBeginTag(out, "option");
				RendererUtil.writeAttribute(out, "value", promptValue != null?promptValue:"");
				RendererUtil.writeCloseBeginTag(out);
				out.write(prompt);				
				RendererUtil.writeEndTag(out, "option");
			}
			if(items!=null){
				for (SampleResItem item : items) {
					out.write("\n");
					RendererUtil.writeBeginTag(out, "option");
					RendererUtil.writeAttribute(out, "value", item.getValue());
					if (item.getValue()!=null && this.value!=null && item.getValue().equals(this.value.toString())) {
						RendererUtil.writeAttribute(out, "selected");
					}
					RendererUtil.writeCloseBeginTag(out);
					if(item.getType()!=null&& item.getType()==Global.GROUP_TYPE_TREE ){
						int level = TreeUtils.TREE_DEFAULT_LEVEL_LENGTH;;
						if( item.getTreeCode() != null ){
							for( int i=level; i<item.getTreeCode().length(); i+=level ){
								out.write("　");
							}
						}					
					}
					out.write(item.getName());				
					RendererUtil.writeEndTag(out, "option");
				}
			}
			RendererUtil.writeEndTag(out, "select");
		} catch(IOException e){
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}

	public void release() {
		code = null;
		filter = null;
		value = null;
		prompt = null;
		promptValue = null;
		id = null;
		name = null;
		style = null;
		lclass = null;
		ltype = null;
		onchange = null;
		onblur = null;
		
		super.release();
	}
}
