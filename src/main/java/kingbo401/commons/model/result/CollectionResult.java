package kingbo401.commons.model.result;

import java.util.Collection;

public class CollectionResult<T> extends BaseResult{
	private static final long serialVersionUID = -945634057879603559L;
	private Collection<T> content;
	
	public CollectionResult() {
		super();
	}

	public CollectionResult(Collection<T> content) {
		super();
		this.content = content;
	}

	public Collection<T> getContent() {
		return content;
	}

	public void setContent(Collection<T> content) {
		this.content = content;
	}

}
