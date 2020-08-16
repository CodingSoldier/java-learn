package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * NIO客户端
 */
public class NioClient {

    public void start(String nickname) throws IOException {
        /**
         * 新建客户端channel连接服务器
         */
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));

        acceptServerMsg(socketChannel);

        sendMsg(socketChannel, nickname);
    }

    /**
     * 接收服务器数据
     */
    private void acceptServerMsg(SocketChannel socketChannel) throws IOException {
        /**
         * 创建selector
         * 新开线程，监听selector上channel的状态
         */
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        new Thread(new NioClientHandler(selector)).start();
    }

    /**
     * 想服务端发送数据
     */
    private void sendMsg(SocketChannel socketChannel, String nickname) throws IOException {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            if (msg != null && msg.length() > 0){
                ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(nickname + " ：" + msg);
                socketChannel.write(byteBuffer);
            }
        }
    }
}








