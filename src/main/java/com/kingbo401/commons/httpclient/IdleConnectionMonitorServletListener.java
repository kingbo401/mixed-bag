package com.kingbo401.commons.httpclient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class IdleConnectionMonitorServletListener implements ServletContextListener{
	
	private static ExecutorService execs = Executors.newCachedThreadPool();
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		execs.execute(IdleConnectionMonitorThread.instance(HttpClientUtil.httpConnManager));
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		execs.shutdownNow();
	}

}
