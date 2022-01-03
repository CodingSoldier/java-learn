package org.cpq.netty4userguide.c_time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Writing-a-Time-Client.html
 */
public class TimeClient {

    public static void main(String[] args) throws Exception{
        String host = "127.0.0.1";
        int port = 8080;
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // ch.pipeline().addLast(new TimeClientHandler());

                    // ch.pipeline().addLast(new TimeClientStreamBasedHandler());

                    // ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());

                    ch.pipeline().addLast(new TimeDecoderReplaying(), new TimeClientHandler());
                }
            });

            ChannelFuture f = b.connect(host, port).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
