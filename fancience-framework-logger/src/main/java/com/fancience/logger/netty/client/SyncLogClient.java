package com.fancience.logger.netty.client;

import com.fancience.logger.support.LoggerNettyHandlerImpl;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by Leonid on 18/3/26.
 */
public class SyncLogClient {

    public static Channel LOG_CHANNEL;

    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            socketChannel.pipeline().addLast(new SyncLogClientHandler());
                        }

                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            LOG_CHANNEL = channelFuture.channel();
            //String s = "Hi, Leonid. Welcome to NettyNettyNettyNettyNettyNetty.$_";
            //channel.writeAndFlush(Unpooled.copiedBuffer(s.getBytes()));
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
