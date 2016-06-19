package com.kingbosky.commons.marker;

import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingbosky.commons.marker.annotation.Marker;
import com.kingbosky.commons.util.StringUtil;

public class MarkerIntecerptor {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private MarkerHolder markerHolder;

	public MarkerHolder getMarkerHolder() {
		return markerHolder;
	}

	public void setMarkerHolder(MarkerHolder markerHolder) {
		this.markerHolder = markerHolder;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String orginMaker = this.markerHolder.getMaker();
		Marker marker = invocation.getMethod().getAnnotation(Marker.class);
		if(marker == null){
			marker = invocation.getClass().getAnnotation(Marker.class);
		}
		if (marker != null) {
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Parse marker annotaion, and set markerHolder value:" + marker.value());
			}
			this.markerHolder.setMaker(marker.value());
		}
		try {
			return invocation.proceed();
		}finally {
			if (StringUtil.isNotEmpty(orginMaker)) {
				this.markerHolder.setMaker(orginMaker);
				this.logger.debug("Set thread local markerHolder to before value:" + orginMaker);
			} else {
				this.markerHolder.clean();
				this.logger.debug("Clear thread local markerHolder");
			}
		}
	}
}
