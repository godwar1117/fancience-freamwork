package com.fancience.aliyun.sms.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.fancience.aliyun.sms.exception.SmsException;

import java.util.Map;
import java.util.Random;

public class Utils {
    private static final String SUCCESS_CODE = "OK";
    private static final Random RANDOM = new Random();

    /**
     * 生成随机验证码.
     *
     * @return 随机数
     */
    public static int randomCode() {
        return 100_000 + RANDOM.nextInt(1_000_000 - 100_000);
    }

    /**
     * 校验 SendSmsResponse 状态.
     *
     * @param response the SendSmsResponse
     */
    public static void checkSmsResponse(final SendSmsResponse response) {
        if (null == response) {
            throw new SmsException("Response is null");
        }
        if (!SUCCESS_CODE.equalsIgnoreCase(response.getCode())) {
            throw new SmsException("Response code is '" + response.getCode() + "'");
        }
    }

    /**
     * 校验手机号码（中国）.
     *
     * @param phoneNumber the phone number
     */
    public static void checkPhoneNumber(final String phoneNumber) {
        if (StrUtil.isEmpty(phoneNumber) || Validator.isMobile(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
    }

    /**
     * 校验字符串不为空.
     *
     * @param str the str
     * @param message the message
     */
    public static void checkNotEmpty(final String str, final String message) {
        if (null == str || str.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 处理受检查异常.
     *
     * @param <T> the type parameter
     * @param fun the fun
     *
     * @return the fun return
     */
    public static <T> T tryChecked(final CheckedSupplier<T> fun) {
        try {
            return fun.get();
        } catch (final Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new SmsException(e);
            }
        }
    }

    /**
     * 处理受检查异常.
     *
     * @param fun the fun
     */
    public static void tryChecked(final CheckedVoid fun) {
        try {
            fun.call();
        } catch (final Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new SmsException(e);
            }
        }
    }
}
