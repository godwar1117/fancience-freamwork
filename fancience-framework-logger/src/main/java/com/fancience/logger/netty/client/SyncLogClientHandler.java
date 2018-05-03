package com.fancience.logger.netty.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * 日志同步handler
 * Created by Leonid on 18/3/26.
 */
public class SyncLogClientHandler extends ChannelInboundHandlerAdapter {

    public SyncLogClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
//        for (int i = 0; i < 10; i ++) {
//            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
//        }
        ctx.writeAndFlush(Unpooled.copiedBuffer("开启日志传输通道$_".getBytes()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        //System.out.println("This is times receive server : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }

}
