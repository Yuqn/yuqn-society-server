package com.yuqn.vo;

import com.yuqn.constant.ResultConstant;
import lombok.Data;
import java.io.Serializable;

/**
 * @author: yuqn
 * @Date: 2024/4/23 14:38
 * @description:
 * 封装请求返回类
 * @version: 1.0
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    // 成功标志
    private boolean success = true;
    // 返回处理消息
    private String message = "";
    // 返回代码
    private Integer code = 0;
    // 返回数据对象 data
    private T result;
    // 时间戳
    private long timestamp = System.currentTimeMillis();

    public Result<T> success(String message) {
        this.message = message;
        this.code = ResultConstant.SC_OK_200;
        this.success = true;
        return this;
    }

    public static<T> Result<T> ok() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        return r;
    }

    public static<T> Result<T> ok(String msg) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        r.setResult((T) msg);
        r.setMessage(msg);
        return r;
    }

    public static<T> Result<T> ok(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static<T> Result<T> fail() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_201);
        return r;
    }

    public static<T> Result<T> fail(String msg) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_201);
        r.setResult((T) msg);
        r.setMessage(msg);
        return r;
    }

    public static<T> Result<T> OK() {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        return r;
    }

    public static<T> Result<T> OK(T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        r.setResult(data);
        return r;
    }

    public static<T> Result<T> OK(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(true);
        r.setCode(ResultConstant.SC_OK_200);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static<T> Result<T> error(String msg, T data) {
        Result<T> r = new Result<T>();
        r.setSuccess(false);
        r.setCode(ResultConstant.SC_INTERNAL_SERVER_ERROR_500);
        r.setMessage(msg);
        r.setResult(data);
        return r;
    }

    public static<T> Result<T> error(String msg) {
        return error(ResultConstant.SC_INTERNAL_SERVER_ERROR_500, msg);
    }

    public static<T> Result<T> error(int code, String msg) {
        Result<T> r = new Result<T>();
        r.setCode(code);
        r.setMessage(msg);
        r.setSuccess(false);
        return r;
    }

    public Result<T> error500(String message) {
        this.message = message;
        this.code = ResultConstant.SC_INTERNAL_SERVER_ERROR_500;
        this.success = false;
        return this;
    }

    public Result() {
    }
}
