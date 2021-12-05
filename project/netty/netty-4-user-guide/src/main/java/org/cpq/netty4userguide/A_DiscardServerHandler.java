package org.cpq.netty4userguide;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class A_DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // try {
        //     // 默默丢弃收到的数据
        //     ByteBuf in = (ByteBuf) msg;
        //     while (in.isReadable()){
        //         System.out.println((char) in.readByte());
        //         System.out.flush();
        //     }
        // } finally{
        //     ReferenceCountUtil.release(msg);
        // }

        ctx.write(msg);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        // 当出现异常时就关闭连接
        cause.printStackTrace();
        ctx.close();
    }

}
