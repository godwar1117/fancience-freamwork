package com.fancience.logger.support;

import com.fancience.logger.netty.client.SyncLogClient;
import io.netty.buffer.Unpooled;

/**
 * Created by Leonid on 18/3/26.
 */
public class LoggerNettyHandlerImpl implements ILoggerNettyHandler {

    private static final String SEPARATOR = "$_";

    @Override
    public void send(byte[] ioe) throws Exception {
        if (SyncLogClient.LOG_CHANNEL != null) {
            //SyncLogClient.LOG_CHANNEL.writeAndFlush(Unpooled.copiedBuffer((new String(ioe) + "$_").getBytes()));
            SyncLogClient.LOG_CHANNEL.writeAndFlush(Unpooled.copiedBuffer(ioe, SEPARATOR.getBytes()));
        }
    }
}
