package com.fancience.api.exception;

/**
 * service服务exception
 * Created by Leonid on 17/11/3.
 */
public class ServiceException extends Exception {

    private Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
