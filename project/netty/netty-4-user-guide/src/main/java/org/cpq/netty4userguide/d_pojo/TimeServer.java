package org.cpq.netty4userguide.d_pojo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Writing-a-Discard-Server.html
 */
public class TimeServer {

    private int port;

    public TimeServer(int port){
        this.port = port;
    }

    public void run() throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            /**
                             * https://waylau.com/netty-4-user-guide/Getting-Started/Speaking-in-POJO-instead-of-ByteBuf.html
                             */
                            // socketChannel.pipeline().addLast(new TimeEncoder(), new TimeServerHandler());

                            socketChannel.pipeline().addLast(new TimeEncoderMessge(), new TimeServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new TimeServer(8080).run();
    }

}
