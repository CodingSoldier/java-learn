package org.cpq.netty4userguide.d_pojo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * https://waylau.com/netty-4-user-guide/Getting-Started/Speaking-in-POJO-instead-of-ByteBuf.html
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
