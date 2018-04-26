package kingbo401.commons.marker;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kingbo401.commons.marker.annotation.Master;
import kingbo401.commons.marker.annotation.Slave;
import kingbo401.commons.util.StringUtil;

public class MasterSlaveMarkInterceptor implements MethodInterceptor{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private MasterSlaveHolder masterSlaveHolder;

	public MasterSlaveHolder getMasterSlaveHolder() {
		return this.masterSlaveHolder;
	}

	public void setMasterSlaveHolder(MasterSlaveHolder masterSlaveHolder) {
		this.masterSlaveHolder = masterSlaveHolder;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String orginMaker = this.masterSlaveHolder.getMaker();
		boolean isSlave = invocation.getMethod().isAnnotationPresent(Slave.class);
		if(isSlave) {
			this.masterSlaveHolder.markSlave();
			this.logger.debug("Parse slave annotaion, and set slave in masterSlaveHolder");
		}
		boolean isMaster = invocation.getMethod().isAnnotationPresent(Master.class);
		if(isMaster) {
			this.masterSlaveHolder.markMaster();
			this.logger.debug("Parse master annotaion, and set master in masterSlaveHolder");
		}
		
		if(!isMaster && !isSlave){
			this.logger.debug("Master slave annotaion is not mark, and set master in masterSlaveHolder");
			this.masterSlaveHolder.markMaster();
		}
		try {
			return invocation.proceed();
		}finally {
			if (StringUtil.isNotEmpty(orginMaker)) {
				this.masterSlaveHolder.setMaker(orginMaker);
				this.logger.debug("Set thread local masterSlaveHolder to before value:" + orginMaker);
			} else {
				this.masterSlaveHolder.clean();
				this.logger.debug("Clear thread local masterSlaveHolder");
			}
		}
	}
}