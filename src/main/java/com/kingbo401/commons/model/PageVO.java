package com.kingbo401.commons.model;

import java.util.List;

import com.kingbo401.commons.model.param.PageParam;

public class PageVO<T> extends BaseObject{
	private static final long serialVersionUID = -3085358027434978191L;
	private Integer pageSize;
	private Integer pageNum;
	private Long total;
	private List<T> items;

	public PageVO() {
		super();
	}
	
	public PageVO(PageParam param){
		if(param == null){
			return;
		}
		this.pageNum = param.getPageNum();
		this.pageSize = param.getPageSize();
	}

	public Integer getTotalPage() {
		if(pageSize == null || pageSize < 1
				|| total == null || total < 1){
			return 0;
		}
		return (int)((total + pageSize -1) / pageSize);
	}

	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
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
	
	public static void main(String[] args) {
        PageVO<Object> page = new PageVO<Object>();
        page.setPageNum(10);
        page.setPageSize(100);
        System.out.println(page);
    }
}
