package com.kingbo401.commons.model.param;

public class PageParam extends OrderParam {
	private static final int DEFAULT_PAGE_SIZE = 10;
	private Integer pageNum = 1;
	private Integer pageSize = DEFAULT_PAGE_SIZE;
	private Integer maxPageSize = 200;
	/**
	 * 是否返回总条数
	 */
	private boolean returnTotalCount = true;

	public PageParam() {
	}

	public PageParam(Integer pageNum, Integer pageSize) {
		super();
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		if(pageSize == null || pageSize < 1){
			pageSize = DEFAULT_PAGE_SIZE;
		}
		if (pageSize > maxPageSize) {
			pageSize = maxPageSize;
		}
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		if (pageNum == null || pageNum < 1) {
			pageNum = 1;
		}
		if (pageSize == null || pageSize < 1) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		return (pageNum - 1) * pageSize;
	}

	public boolean isReturnTotalCount() {
		return returnTotalCount;
	}

	public void setReturnTotalCount(boolean returnTotalCount) {
		this.returnTotalCount = returnTotalCount;
	}
	
	public void initMaxPageSize(Integer maxPageSize){
		this.maxPageSize = maxPageSize;
	}
}
