package com.kingbosky.commons.sensitive;

import com.kingbosky.commons.sync.IDataSync;

public interface SensitiveWordManager extends IDataSync{
	public boolean hasSensitive(String word);
}
