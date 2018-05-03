package com.fancience.api.protocol;

import com.fancience.api.constants.WebFormResultConstant;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leonid on 18/3/21.
 */
public class WebFormResult<T> implements Serializable {

    private boolean success;

    private String code;

    private String message;

    private Date timestamp;

    private T result;

    private String exception;


    private Integer httpState = 200;
    // 是否是系统消息
    private boolean isSystem = false;
    // 日志详细消息
    private String logMessage;

    public WebFormResult(){}

    public WebFormResult(String code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    /**
     * 成功
     * @param code
     * @param result
     * @param message
     * @return
     */
    public static <T>WebFormResult<T> createSuccessResult(String code, T result, String message, String logMessage) {
        WebFormResult resultForm = new WebFormResult(code, result, message);
        resultForm.setSuccess(true);
        resultForm.setTimestamp(new Date());
        resultForm.setLogMessage(logMessage == null ? message : logMessage);
        return resultForm;
    }

    public static <T>WebFormResult<T> createSuccessResult(String code, T result, String message) {
        return createSuccessResult(code, result, message, null);
    }

    public static <T>WebFormResult<T> createSuccessResult(T result, String message) {
        return createSuccessResult(WebFormResultConstant.RESULT_OK, result, message);
    }

    public static <T>WebFormResult<T> createSuccessResultBySaveMsg(T result) {
        return createSuccessResult(result, WebFormResultConstant.RESULT_SAVE_SUCCESS_MESSAGE);
    }

    public static <T>WebFormResult<T> createSuccessResultBySaveMsg() {
        return createSuccessResultBySaveMsg(null);
    }

    public static <T>WebFormResult<T> createSuccessResultBySearchMsg(T result) {
        return createSuccessResult(result, WebFormResultConstant.RESULT_SEARCH_SUCCESS_MESSAGE);
    }

    public static <T>WebFormResult<T> createErrorResultBySaveMsg(T result) {
        return createErrorResult(result, WebFormResultConstant.RESULT_SAVE_FAIL_MESSAGE);
    }

    public static <T>WebFormResult<T> createErrorResultBySaveMsg() {
        return createErrorResultBySaveMsg(null);
    }

    public static <T>WebFormResult<T> createErrorResultBySearchMsg(T result) {
        return createErrorResult(result, WebFormResultConstant.RESULT_SEARCH_FAIL_MESSAGE);
    }

    public static <T>WebFormResult<T> createSuccessResultByUploadMsg(T result) {
        return createSuccessResult(result, WebFormResultConstant.RESULT_IMPORT_SUCCESS_MESSAGE);
    }

    public static <T>WebFormResult<T> createErrorResultByUploadMsg(T result) {
        return createErrorResult(result, WebFormResultConstant.RESULT_IMPORT_FAIL_MESSAGE);
    }

    /**
     * 失败
     * @param code
     * @param result
     * @param message
     * @return
     */
    public static <T>WebFormResult<T> createErrorResult(String code, T result, String message, String logMessage) {
        WebFormResult resultForm = new WebFormResult(code, result, message);
        resultForm.setSuccess(false);
        resultForm.setTimestamp(new Date());
        resultForm.setLogMessage(logMessage == null ? message : logMessage);
        return resultForm;
    }

    public static <T>WebFormResult<T> createErrorResult(String code, T result, String message) {
        return createErrorResult(code, result, message, null);
    }

    public static <T>WebFormResult<T> createErrorResult(T result, String message) {
        return createErrorResult(WebFormResultConstant.RESULT_ERROR, result, message, null);
    }

    public static <T>WebFormResult<T> createErrorResult(String message) {
        return createErrorResult(null, message);
    }

    /**
     * 给到前台result
     * @param resultForm
     * @return
     */
    public static WebFormResult toFrontResult(WebFormResult resultForm) {
        resultForm.setException(null);
        resultForm.setLogMessage(null);
        return resultForm;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Integer getHttpState() {
        return httpState;
    }

    public void setHttpState(Integer httpState) {
        this.httpState = httpState;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }
}
