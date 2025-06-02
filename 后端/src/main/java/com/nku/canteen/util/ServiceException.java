package com.nku.canteen.util;

/**
 * 业务逻辑异常
 */
public class ServiceException extends RuntimeException {
    
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 错误码
     */
    private int code = 500;
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public ServiceException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param code 错误码
     */
    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 异常原因
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 设置错误码
     * 
     * @param code 错误码
     */
    public void setCode(int code) {
        this.code = code;
    }
} 