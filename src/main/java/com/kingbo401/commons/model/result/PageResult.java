package com.kingbo401.commons.model.result;

import com.kingbo401.commons.model.PageVO;

public class PageResult<T> extends BaseResult{
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
