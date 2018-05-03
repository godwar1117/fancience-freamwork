package com.fancience.aliyun.sms.util;

/**
 * The interface CheckedVoid.
 *
 * @author zhangpeng
 */
@FunctionalInterface
public interface CheckedVoid {
    /**
     * Call.
     *
     * @throws Exception the exception
     */
    void call() throws Exception;
}
