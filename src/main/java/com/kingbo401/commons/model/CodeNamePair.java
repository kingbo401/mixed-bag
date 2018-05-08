package com.kingbo401.commons.model;

public class CodeNamePair<C, N> {

	private final C code;
	private final N name;

	public CodeNamePair(C code, N name) {
		this.code = code;
		this.name = name;
	}

	public C getCode() {
		return code;
	}

	public N getName() {
		return name;
	}

	@Override
	public String toString() {
		return code + "=" + name;
	}
}
