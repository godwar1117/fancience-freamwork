package com.fancience.aliyun.sms.exception;

public class SmsException extends RuntimeException  {
    /**
     * Instantiates a new SmsException.
     *
     * @param message the message
     */
    public SmsException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new SmsException.
     *
     * @param cause the cause
     */
    public SmsException(final Throwable cause) {
        super(cause);
    }
}
