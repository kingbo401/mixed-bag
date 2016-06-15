package com.kingbosky.commons.web.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class PagerParamTag extends TagSupport {

    private String name;
	private String value;
    private static final long serialVersionUID = -1029467106146206596L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int doStartTag() throws JspException {
        PagerTag pagerTag = (PagerTag) findAncestorWithClass(this, PagerTag.class);
        if (pagerTag == null) {
            Object obj = pageContext.getRequest().getAttribute(PagerTag.DEFAULT_ID);
            if (obj instanceof PagerTag) {
			    pagerTag = (PagerTag) obj;
            }
            if (pagerTag == null) {
                throw new JspTagException("not nested within a pager tag" + " and no pager tag found at request scope.");
			}
        }
        pagerTag.addParam(name, value);
       
        return super.doStartTag();
	}

	public void release() {
		name = null;
		value = null;
		super.release();
	}
}
