package com.kingbosky.commons.hessian;

import java.io.IOException;
import java.net.URL;

import com.caucho.hessian.client.HessianConnection;
import com.caucho.hessian.client.HessianURLConnectionFactory;
import com.kingbosky.commons.encrypt.MD5Util;

public class SignatureHessianConnectionFactory extends HessianURLConnectionFactory{
	private String secretKey;

	@Override
	public HessianConnection open(URL url) throws IOException {
		HessianConnection conn = super.open(url);
		long timestmap = System.currentTimeMillis();
		String sign = MD5Util.encrypt(timestmap + secretKey);
		conn.addHeader("Signature-Sign", sign);
		conn.addHeader("Signature-Timestamp", Long.toString(timestmap));
		return conn;
	}

	public SignatureHessianConnectionFactory(String secretKey) {
		super();
		this.secretKey = secretKey;
	}
}
