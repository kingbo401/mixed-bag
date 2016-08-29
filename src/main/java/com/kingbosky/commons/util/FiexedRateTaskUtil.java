package com.kingbosky.commons.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务管理器
 */
public class FiexedRateTaskUtil {
	private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

	public static void stop(){
		scheduledExecutorService.shutdown();
	}
	public static void addTask(Runnable task, long initialDelay, long period, TimeUnit unit) {
		scheduledExecutorService.scheduleAtFixedRate(task, initialDelay, period, unit);
	}
}
