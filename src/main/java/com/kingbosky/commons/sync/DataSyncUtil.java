package com.kingbosky.commons.sync;

import java.util.concurrent.TimeUnit;

public class DataSyncUtil {
	private static DataSyncManager manager = new DataSyncManager(TimeUnit.MINUTES.toMillis(2));//2分钟刷新一次
	public static void start(){
		manager.start();
	}
	public static void addDataSync(IDataSync dataSync) {
		manager.addDataSync(dataSync);
	}
	
	public static void stop(){
		manager.stop();
	}
}
