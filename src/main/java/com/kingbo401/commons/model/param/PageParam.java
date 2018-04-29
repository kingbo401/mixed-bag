package com.kingbo401.commons.model.param;

import com.kingbo401.commons.model.BasePojo;

public class PageParam extends BasePojo{
	private int pageNum = 1;
	private int pageSize = 10;
	
	public PageParam() {
	}

	public PageParam(int pageNum, int pageSize) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
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
	
	public int getOffset(){
		return (pageNum - 1) * pageSize;
	}
}
