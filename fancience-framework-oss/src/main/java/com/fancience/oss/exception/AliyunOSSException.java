package com.fancience.oss.exception;

/**
 * @Package com.fancience.oss.exception
 * @Description:
 * @author: shoucai.wang
 * @date: 23/01/2018 17:28
 */
public class AliyunOSSException extends Exception {
  public AliyunOSSException() {
  }

  public AliyunOSSException(String message) {
    super(message);
  }

  public AliyunOSSException(String message, Throwable cause) {
    super(message, cause);
  }

  public AliyunOSSException(Throwable cause) {
    super(cause);
  }

  protected AliyunOSSException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
