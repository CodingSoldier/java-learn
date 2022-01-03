package org.cpq.netty4userguide.d_pojo;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * https://waylau.com/netty-4-user-guide/Getting-Started/Speaking-in-POJO-instead-of-ByteBuf.html
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        ChannelFuture f = ctx.writeAndFlush(new UnixTime());
        f.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
