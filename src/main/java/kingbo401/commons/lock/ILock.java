package kingbo401.commons.lock;

public interface ILock {
	public boolean lock(String key);
	public void unLock(String key);
}
