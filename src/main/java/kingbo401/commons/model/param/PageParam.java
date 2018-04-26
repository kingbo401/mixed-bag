package kingbo401.commons.model.param;

import kingbo401.commons.model.BaseObject;

public class PageParam extends BaseObject{
	private static final long serialVersionUID = 803546645053829211L;
	private int pageNum = 1;
	private int pageSize = 10;
	
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
