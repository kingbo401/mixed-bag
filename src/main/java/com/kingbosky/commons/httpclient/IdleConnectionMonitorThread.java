package com.kingbosky.commons.httpclient;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpComponents空闲连接踢出线程
 * 默认策略：5秒执行一次，踢出过期的idle连接，同时关闭空闲60S以上的连接
 * @author tianbenzhen@pwrd.com
 * @version 2014-5-8 下午5:30:36
 */
public class IdleConnectionMonitorThread extends Thread{
	
	private final HttpClientConnectionManager connMgr;
    private static IdleConnectionMonitorThread _instance = null;
    private static Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);
    
    public static IdleConnectionMonitorThread instance(HttpClientConnectionManager connMgr){
    	if(_instance == null){  
            synchronized (IdleConnectionMonitorThread.class){  
                if(_instance == null){  
                    _instance = new IdleConnectionMonitorThread(connMgr);  
                }  
            }  
        }  
        return _instance;  
    }

    private IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
        	while (true) {
                synchronized (this) {
                    TimeUnit.SECONDS.sleep(5);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                    // 可选的, 关闭60秒内不活动的连接
                    connMgr.closeIdleConnections(60, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
        	logger.info("IdleConnectionMonitorThread InterruptedException exits!");
        }
    }

}
