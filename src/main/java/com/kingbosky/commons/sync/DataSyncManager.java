package com.kingbosky.commons.sync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据定时刷新
 */
public class DataSyncManager {
	private static long DEFAULT_INTERVAL = TimeUnit.MINUTES.toMillis(5);
	private long interval;
	private List<IDataSync> lstDataSync = new ArrayList<IDataSync>();
	private final Timer timer = new Timer(true);
	private volatile boolean isStart = false;
	private Object lock = new Object();
	private final static Logger logger = LoggerFactory.getLogger(DataSyncManager.class);
	public DataSyncManager(){
		this.interval = DEFAULT_INTERVAL;
	}
	
	public DataSyncManager(long interval) {
		this.interval = interval;
		if(interval <= 0){
			this.interval = DEFAULT_INTERVAL;
		}
	}

	public void start() {
		if(isStart) return;
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					synchronized (lock) {
						Iterator<IDataSync> iterator = lstDataSync.iterator();
						while (iterator.hasNext()) {
							IDataSync sync = iterator.next();
							sync.sync();
						}
					}
					logger.info("data sync success");
				} catch (Exception e) {
					logger.error("data sync error:" + e);
				}
			}
		}, 0, interval);
	}
	
	public void stop(){
		if(isStart){
			timer.cancel();
		}
	}

	public void addDataSync(IDataSync dataSync) {
		synchronized (lock) {
			lstDataSync.add(dataSync);
		}
	}
}
