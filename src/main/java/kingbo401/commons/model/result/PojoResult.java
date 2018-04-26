package kingbo401.commons.model.result;

public class PojoResult<T> extends BaseResult{
	private static final long serialVersionUID = 7054429837818609212L;
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
