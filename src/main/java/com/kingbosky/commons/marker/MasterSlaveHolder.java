package com.kingbosky.commons.marker;

public class MasterSlaveHolder extends MarkerHolder{
	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
	
	public void markMaster(){
		super.setMaker(MASTER);
	}
	
	public void markSlave(){
		super.setMaker(MASTER);
	}
}
