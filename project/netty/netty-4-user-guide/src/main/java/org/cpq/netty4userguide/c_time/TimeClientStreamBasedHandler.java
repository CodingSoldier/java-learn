package org.cpq.netty4userguide.c_time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Dealing-with-a-Stream-based-Transport.html
 */
public class TimeClientStreamBasedHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        buf = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m);
        m.release();
        if (buf.readableBytes() >= 4){
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println("TimeClientStreamBasedHandler: " + new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
