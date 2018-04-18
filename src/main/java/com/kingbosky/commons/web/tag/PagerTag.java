package com.kingbosky.commons.web.tag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class PagerTag extends TagSupport {

	static final String DEFAULT_ID = "pager";

    private static final long serialVersionUID = 1L;
	private String pageParam = "pn";
	private int pageNum = 1; // 当前页面
	private int pageSize = 10;// 每页显示多少数据
	private int total;// 数据总条数
	private String url;// 跳转页面
	private String css = "page";
	private int maxPage = 10;// 显示多少页码
	private Map<String, String> params = new HashMap<String, String>();

    public PagerTag() {
		id = DEFAULT_ID;
	}
	
	final void addParam(String name, String value){
		if (value != null) {
//			value = java.net.URLEncoder.encode(value, "UTF-8");
			params.put(name, value);

		}
	}
	
	public int doStartTag() {
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException {
		StringBuilder tag = new StringBuilder();
		try {
			if (total > pageSize) {
				tag.append("<div class=\"" + css + "\">");
				tag.append(getNavigatorBar());
				// 输出结束标记
				tag.append("</div>");
				pageContext.getOut().write(tag.toString());
			}
			return super.doEndTag();
		} catch (IOException ioe) {
			throw new JspException(ioe.getMessage());
		}
	}
	
	@Override
	public void release() {
		pageParam = null;
		params = null;
		pageNum = 1; // 当前页面
		pageSize = 10;// 每页显示多少数据
		total = 0;// 数据总条数
		url = null;// 跳转页面
		css = "page";
		maxPage = 10;// 显示多少页码
		super.release();
	}

	private String getNavigatorBar() {
		int offset = (pageNum - 1) / maxPage;
		int totalPage = (total + pageSize - 1) / pageSize;
		StringBuilder bar = new StringBuilder();
		if (pageNum > 1) {
			params.put(pageParam, String.valueOf(pageNum - 1));
			bar.append("<a href=\"").append(getUri(url, params)).append("\">上一页</a>");
		}
		if (offset > 0){
			int pNum = offset * maxPage;
			params.put(pageParam, String.valueOf(pNum));
			bar.append("<a href=\"").append(getUri(url, params)).append("\">" + pNum + "</a>");
		}

	    int pageShowNum = 0;
	    if((offset + 1) * maxPage < totalPage){
	    	pageShowNum = (offset + 1) * maxPage;
	    }else{
	    	pageShowNum = totalPage;
	    }
	    if(pageNum <= totalPage){
	    	for(int i = offset * maxPage + 1; i <= pageShowNum; i++){
	    		if(pageNum != i){
	    			params.put(pageParam, String.valueOf(i));
	    			bar.append("<a href=\"").append(getUri(url, params)).append("\">" + i + "</a>");
	    		}else{
	    			bar.append("<a href=\"#\" class=\"current\">" + i + "</a>");
	    		}
	    	}
	    } 
	    
		if (pageNum < totalPage) {
			params.put(pageParam, String.valueOf(pageNum + 1));
			bar.append("<a href=\"").append(getUri(url, params)).append("\">下一页</a>");
		}
		return bar.toString();
	}

	private String getUri(String url, Map<String, String> paramsMap) {
		boolean hasQuestionMark = false;
		if (url.contains("?")) {
			hasQuestionMark = true;
		}
		String params = "";
		for (Iterator<String> iter = paramsMap.keySet().iterator(); iter
				.hasNext();) {
			String key = iter.next();
			String value = paramsMap.get(key);
			try {
				value = URLEncoder.encode(value, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (params.trim().length() == 0) {
				if (hasQuestionMark) {
					params = "&" + key + "=" + value;
				} else {
					params = "?" + key + "=" + value;
				}
			} else {
				params = params + "&" + key + "=" + value;
			}
		}

		return url + params;
	}

	public String getPageParam() {
		return pageParam;
	}

	public void setPageParam(String pageParam) {
		this.pageParam = pageParam;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getForwardUrl() {
		return url;
	}

	public void setForwardUrl(String forwardUrl) {
		this.url = forwardUrl;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}
	
}
