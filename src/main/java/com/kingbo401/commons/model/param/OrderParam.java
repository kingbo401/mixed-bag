package com.kingbo401.commons.model.param;

import com.kingbo401.commons.model.BasePojo;

public class OrderParam extends BasePojo{
	private String orderBy;
	private boolean asc = true;
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public boolean isAsc() {
		return asc;
	}
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
}
