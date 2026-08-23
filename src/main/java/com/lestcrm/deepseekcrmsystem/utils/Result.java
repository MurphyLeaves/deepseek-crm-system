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
}