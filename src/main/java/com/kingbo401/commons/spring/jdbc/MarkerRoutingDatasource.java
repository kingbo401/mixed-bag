package com.kingbo401.commons.spring.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.kingbo401.commons.marker.MarkerHolder;
import com.kingbo401.commons.util.StringTool;

public class MarkerRoutingDatasource extends AbstractRoutingDataSource {
	private final Logger logger = LoggerFactory.getLogger(MarkerRoutingDatasource.class);
	private MarkerHolder markerHolder;
	protected String defaultMarker = "";

	public MarkerHolder getMarkerHolder() {
		return markerHolder;
	}

	public void setMarkerHolder(MarkerHolder markerHolder) {
		this.markerHolder = markerHolder;
	}

	public String getDefaultMarker() {
		return defaultMarker;
	}

	public void setDefaultMarker(String defaultMarker) {
		this.defaultMarker = defaultMarker;
	}

	protected Object determineCurrentLookupKey() {
		String marker = markerHolder.getMaker();
		if (logger.isDebugEnabled()) {
			logger.debug("determine current data source type is " + marker);
		}
		return StringTool.isEmpty(marker) ? defaultMarker : marker;
	}
}
