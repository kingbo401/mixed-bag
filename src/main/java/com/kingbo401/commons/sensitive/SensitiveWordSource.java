package com.kingbo401.commons.sensitive;

import java.util.List;

/**
 * 敏感词source
 *
 * @author kingbo401
 * @date 2019/07/20
 */
public interface SensitiveWordSource {
    /**
     * 初始化获取敏感词
     * @return
     */
	List<String> initSensitiveWords();
}
