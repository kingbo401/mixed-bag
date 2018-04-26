package kingbo401.commons.sensitive;


public interface SensitiveWordManager extends Runnable{
	public boolean hasSensitiveWord(String word);
}
