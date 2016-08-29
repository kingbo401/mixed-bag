package com.kingbosky.commons.sensitive;


public interface SensitiveWordManager extends Runnable{
	public boolean hasSensitive(String word);
}
