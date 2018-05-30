package com.kingbo401.commons.model.param;

public class PageParam extends OrderParam{
	private int pageNum = 1;
	private int pageSize = 10;
	
	/**
	 * 是否返回总条数
	 */
	private boolean returnTotalCount = true;
	
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
		if(pageNum < 1){
			pageNum = 1;
		}
		if(pageSize < 1){
			pageSize = 10;
		}
		return (pageNum - 1) * pageSize;
	}

	public boolean isReturnTotalCount() {
		return returnTotalCount;
	}

	public void setReturnTotalCount(boolean returnTotalCount) {
		this.returnTotalCount = returnTotalCount;
	}
}
