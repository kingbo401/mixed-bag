package com.kingbo401.commons.model.param;

import java.io.Serializable;

public class PageObject extends PageParam implements Serializable{
	private static final long serialVersionUID = 8822242814104657286L;

	public PageObject() {
		super();
	}

	public PageObject(int pageNum, int pageSize) {
		super(pageNum, pageSize);
	}
}
