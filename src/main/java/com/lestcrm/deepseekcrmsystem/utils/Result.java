package com.lestcrm.deepseekcrmsystem.utils;

import lombok.Data;

@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    private Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 成功响应（无数据）
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    // 失败响应
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    // 失败响应（自定义状态码，用于 401 未登录 / 403 无权限等场景）
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

    // 未登录
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }

    // 无权限
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }
}