package com.kingbosky.commons.marker;

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
