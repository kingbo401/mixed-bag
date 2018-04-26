package kingbo401.commons.model.result;

import java.util.List;

public class ListResult<T> extends BaseResult{
	private static final long serialVersionUID = -3387209402002990065L;
	private List<T> content;

	public ListResult() {
		super();
	}

	public ListResult(List<T> content) {
		super();
		this.content = content;
	}


	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
