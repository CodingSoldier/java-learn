package org.cpq.netty4userguide.c_time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * https://waylau.com/netty-4-user-guide/Getting-Started/Writing-a-Time-Client.html
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        ByteBuf m = (ByteBuf) msg;
        try {
            long currentTimeMIllis = (m.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println("TimeClientHandler: " + new Date(currentTimeMIllis));
            ctx.close();
        } finally {
            m.release();
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
