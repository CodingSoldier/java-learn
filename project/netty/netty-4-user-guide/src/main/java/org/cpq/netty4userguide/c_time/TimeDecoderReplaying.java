package org.cpq.netty4userguide.c_time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * https://waylau.com/netty-4-user-guide/Getting-Started/Dealing-with-a-Stream-based-Transport.html
 */
public class TimeDecoderReplaying extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("TimeDecoderReplaying");
        out.add(in.readBytes(4));
    }
}
