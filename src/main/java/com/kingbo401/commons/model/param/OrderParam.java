package com.kingbo401.commons.model.param;

import java.util.regex.Pattern;

import com.kingbo401.commons.util.Constants;

public class OrderParam extends BaseParam{
	private static final Pattern FIELD_PATTERN = Pattern.compile("[a-zA-Z0-9_\\-\\.]+");//防止sql注入
	private String orderField;
	private String orderType = Constants.ORDER_ASC;
	public String getOrderField() {
		checkOrderField();
		return orderField;
	}
	public void setOrderField(String orderField) {
		this.orderField = orderField;
		checkOrderField();
	}
	public String getOrderType() {
		checkOrderType();
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
		checkOrderType();
	}
	private void checkOrderField(){
		if(orderField == null || orderField.isEmpty()){
			return;
		}
		if(!FIELD_PATTERN.matcher(orderField).matches()){
			throw new IllegalArgumentException("orderField illegal:" + orderField);
		}
	}
	
	private void checkOrderType(){
		if(orderField == null || orderField.isEmpty()){
			return;
		}
		if(orderType == null || orderType.isEmpty()){
			orderType = Constants.ORDER_ASC;
		}
		if(!orderType.equalsIgnoreCase(Constants.ORDER_ASC) && !orderType.equalsIgnoreCase(Constants.ORDER_DESC)){
			throw new IllegalArgumentException("sort illegal:" + orderType);
		}
	}
}
