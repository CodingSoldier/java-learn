package org.cpq.netty4userguide.c_time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Writing-a-Time-Server.html
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx){
        ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
         // 出站操作返回ChannelFuture
        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
