package com.kingbo401.commons.marker;

/**
 * 存储上下文marker信息
 *
 * @author kingbo401
 * @date 2019/07/20
 */
public class MarkerHolder {
	private ThreadLocal<String> holder = new ThreadLocal<String>();
	public void clean() {
		this.holder.remove();
	}
	
	public String getMaker(){
		return holder.get();
	}
	
	public void setMaker(String maker){
		holder.set(maker);
	}
}
