package com.kingbo401.commons.model.param;

import java.util.regex.Pattern;

import com.kingbo401.commons.model.BasePojo;
import com.kingbo401.commons.util.Constants;

public class OrderParam extends BasePojo{
	private static final Pattern FIELD_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-\\.]+");//防止sql注入
	private String orderByField;
	private String sort = Constants.ORDER_ASC;
	public String getOrderByField() {
		checkField();
		return orderByField;
	}
	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
		checkField();
	}
	
	public String getSort() {
		checkSort();
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
		checkSort();
	}
	
	private void checkField(){
		if(orderByField == null || orderByField.isEmpty()){
			return;
		}
		if(!FIELD_PATTERN.matcher(orderByField).matches()){
			throw new IllegalArgumentException("orderByField illegal:" + orderByField);
		}
	}
	
	private void checkSort(){
		if(orderByField == null || orderByField.isEmpty()){
			return;
		}
		if(sort == null || sort.isEmpty()){
			throw new IllegalArgumentException("sort cannot null");
		}
		if(!sort.equalsIgnoreCase(Constants.ORDER_ASC) && !sort.equalsIgnoreCase(Constants.ORDER_DESC)){
			throw new IllegalArgumentException("sort illegal:" + sort);
		}
	}
}
