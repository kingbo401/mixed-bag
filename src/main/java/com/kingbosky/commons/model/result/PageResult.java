package com.kingbosky.commons.model.result;

import org.apache.poi.ss.formula.functions.T;

import com.kingbosky.commons.model.vo.PageVO;

public class PageResult extends BaseResult{
	private static final long serialVersionUID = -6503398929445154329L;
	private PageVO<T> content;

	public PageResult() {
		super();
	}

	public PageResult(PageVO<T> content) {
		super();
		this.content = content;
	}

	public PageVO<T> getContent() {
		return content;
	}

	public void setContent(PageVO<T> content) {
		this.content = content;
	}
}
