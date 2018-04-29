package com.kingbo401.commons.hessian;

import com.caucho.hessian.client.HessianConnectionFactory;
import com.caucho.hessian.client.HessianProxyFactory;

public class SignatureHessianProxyFactory extends HessianProxyFactory {
	private String secretKey;
	
	public SignatureHessianProxyFactory() {
		
	}

	@Override
	protected HessianConnectionFactory createHessianConnectionFactory() {
		return new SignatureHessianConnectionFactory(secretKey);
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSecretKey() {
		return secretKey;
	}
}
