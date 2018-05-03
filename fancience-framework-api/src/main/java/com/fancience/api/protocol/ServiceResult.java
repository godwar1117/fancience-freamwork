package com.fancience.api.protocol;

import java.io.Serializable;

/**
 * 统一service返回类型
 * Created by Leonid on 18/3/21.
 */
public class ServiceResult<T> implements Serializable {

    private T result;

    private String logMessage;

    public ServiceResult() {}

    public ServiceResult(T result, String logMessage) {
        this.result = result;
        this.logMessage = logMessage;
    }

    public static <T>ServiceResult<T> createServiceResult(T result, String logMessage) {
        return new ServiceResult<T>(result, logMessage);
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
