package com.fancience.aliyun.sms.util;

/**
 * The interface CheckedSupplier.
 *
 * @param <T> the type parameter
 *
 * @author zhangpeng
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
    /**
     * Get t.
     *
     * @return the t
     *
     * @throws Exception the exception
     */
    T get() throws Exception;
}
