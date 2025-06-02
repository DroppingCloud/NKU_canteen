package com.nku.canteen.util;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果类
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    private Integer code;

    /** 返回信息 */
    private String msg;

    /** 返回数据 */
    private T data;

    private R() {}

    /**
     * 成功返回结果
     */
    public static <T> R<T> success() {
        return success(null);
    }

    /**
     * 成功返回结果
     * @param data 返回数据
     */
    public static <T> R<T> success(T data) {
        return success(data, "操作成功");
    }

    /**
     * 成功返回结果
     * @param data 返回数据
     * @param msg 返回消息
     */
    public static <T> R<T> success(T data, String msg) {
        R<T> r = new R<>();
        r.setCode(0);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param msg 错误消息
     */
    public static <T> R<T> error(int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 参数错误
     */
    public static <T> R<T> paramError(String msg) {
        return error(1, msg);
    }

    /**
     * 未授权
     */
    public static <T> R<T> unauthorized(String msg) {
        return error(2, msg);
    }

    /**
     * 资源不存在
     */
    public static <T> R<T> notFound(String msg) {
        return error(3, msg);
    }

    /**
     * 系统异常
     */
    public static <T> R<T> systemError() {
        return error(999, "系统异常");
    }
} 