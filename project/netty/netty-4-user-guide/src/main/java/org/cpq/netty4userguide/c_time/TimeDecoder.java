package org.cpq.netty4userguide.c_time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Dealing-with-a-Stream-based-Transport.html
 */
public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4){
            return;
        }
        System.out.println("TimeDecoder");
        out.add(in.readBytes(4));
    }
}
