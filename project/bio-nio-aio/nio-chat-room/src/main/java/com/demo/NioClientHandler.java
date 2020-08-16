package com.demo;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 客户端使用线程轮询selector的channel是否被监听状态
 */
public class NioClientHandler implements Runnable {

    private Selector selector;

    public NioClientHandler(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            for (; ; ) {
                int operationChannel = selector.select();
                if (operationChannel == 0) continue;

                /**
                 * 获取可操作channel集合
                 */
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isReadable()){
                        SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                        readChannel(socketChannel);
                    }
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理channel的可读状态
     */
    private void readChannel(SocketChannel socketChannel) throws IOException{
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String msg = "";
        while (socketChannel.read(byteBuffer) > 0){
            byteBuffer.flip();
            msg += Charset.forName("UTF-8").decode(byteBuffer);
        }
        if (msg.length() > 0){
            System.out.println(msg);
        }
    }
}
