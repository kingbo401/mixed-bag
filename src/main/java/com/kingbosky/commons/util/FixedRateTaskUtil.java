package com.kingbosky.commons.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时任务管理器
 */
public class FixedRateTaskUtil {
	private final static Logger logger = LoggerFactory.getLogger(FixedRateTaskUtil.class);
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public static void stop(){
		scheduledExecutorService.shutdown();
	}
	public static void addTask(final Runnable task, long initialDelay, long period, TimeUnit unit) {
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					task.run();
				}catch(Exception e){
					logger.error("FiexedRateTaskUtil error", e);
				}
			}
		}, initialDelay, period, unit);
	}
}
