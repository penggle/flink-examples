package com.penglecode.flink.examples.common.api.dto;

import org.springframework.http.HttpStatus;

/**
 * @author pengpeng
 * @version 1.0
 * @since 2021/11/26 22:52
 */
public class OpenApiResult<T> {

    private Integer code;

    private String message;

    private T result;

    public OpenApiResult() {
    }

    public OpenApiResult(Integer code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccessful() {
        HttpStatus status;
        if(code != null && (status = HttpStatus.resolve(code)) != null) {
            return status.is2xxSuccessful();
        }
        return false;
    }

}
