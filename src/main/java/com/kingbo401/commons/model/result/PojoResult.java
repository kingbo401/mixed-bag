package com.kingbo401.commons.model.result;

public class PojoResult<T> extends BaseResult{
	private T content;

	public PojoResult() {
		super();
	}

	public PojoResult(T content) {
		super();
		this.content = content;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}
}
