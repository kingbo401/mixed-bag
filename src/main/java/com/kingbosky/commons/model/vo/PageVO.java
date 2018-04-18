package com.kingbosky.commons.model.vo;

import java.util.List;

import com.kingbosky.commons.model.BaseObject;

public class PageVO<T> extends BaseObject{
	private static final long serialVersionUID = -3085358027434978191L;
	private int pageSize;
	private int pageNum;
	private long total;
	private List<T> items;

	public int getTotalPage() {
		if(pageSize < 1 || pageNum < 1 || total < 1){
			return 0;
		}
		return (int)((total + pageSize -1) / pageSize);
	}

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
