 package com.kingbo401.commons.enums;

 /**
 * 错误编码
 * @author kingbo401
 * @date 2019/07/20
 */
public enum ErrorCode {
    /**
     * 成功
     */
    SUCCESS("200", "success"),
    /**
     * 参数非法
     */
    PARAM_INVALID("400", "param invalid"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR("500", "system error")
    ;
    
    private String code;
    private String message;
    
    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
