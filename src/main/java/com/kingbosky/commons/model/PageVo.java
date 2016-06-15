package com.kingbosky.commons.model;

import java.util.List;

public class PageVo<T> extends BaseObject{
	private static final long serialVersionUID = -7143744393328876636L;
	private long total;//总记录数
	private int pageSize=10;//每页显示的数据条数
	private int curPage=1;//当前页码
	private List<T> datas;//保存查询结果

	public PageVo(){
		
	}
	
    public PageVo(long total, int pageSize, int curPage, List<T> datas) {
        this.total = total;
        this.pageSize = pageSize;
        this.curPage = curPage;
        this.datas = datas;
    }

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
}
