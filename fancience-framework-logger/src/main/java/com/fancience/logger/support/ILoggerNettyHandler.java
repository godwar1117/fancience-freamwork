package com.fancience.logger.support;

/**
 * Created by Leonid on 18/3/26.
 */
public interface ILoggerNettyHandler {

    void send(byte[] ioe) throws Exception;

}
