package com.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器端
 */
public class NioServer {

    ServerSocketChannel serverChannel;
    Selector selector;

    /**
     * 启动
     */
    public void start() throws IOException {

        /**
         * 1. 通过ServerSocketChannel创建channel通道
         */
        serverChannel = ServerSocketChannel.open();

        /**
         * 2. 为channel通道绑定监听端口
         */
        serverChannel.bind(new InetSocketAddress(8000));

        /**
         * 3. **设置channel为非阻塞模式**
         */
        serverChannel.configureBlocking(false);

        /**
         * 4. 创建Selector
         */
        selector = Selector.open();

        /**
         * 5. 将channel注册到selector上，监听连接事件
         */
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务器启动成功！");

        /**
         * 6. 循环等待新接入的连接
         */
        for (;;) {
            /**
             *  获取可用channel数量
             */
            int readyChannels = selector.select();
            if (readyChannels == 0) continue;

            /**
             * 获取可用channel的集合
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                /**
                 * selectionKey实例
                 */
                SelectionKey selectionKey = iterator.next();

                /**
                 * 7. 根据就绪状态，调用对应方法处理业务逻辑
                 */
                /**
                 * 如果是 接入事件
                 */
                if (selectionKey.isAcceptable()) {
                    acceptChannel();
                }

                /**
                 * 如果是 可读事件
                 */
                if (selectionKey.isReadable()) {
                    /**
                     * 从selectionKey中获取可读Channel
                     */
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    readChannel(socketChannel);
                }
            }

            /**
             * selectionKey存储处于监听状态的channel，监听的状态处理完后，清空selectionKeys，等待channel下一次处于监听状态
             */
            selectionKeys.clear();
        }
    }

    /**
     * 接入事件处理器
     */
    private void acceptChannel()
            throws IOException {
        /**
         * 如果是接入事件，创建socketChannel
         */
        SocketChannel socketChannel = serverChannel.accept();

        /**
         * 将socketChannel设置为非阻塞模式
         */
        socketChannel.configureBlocking(false);

        /**
         * 将channel注册到selector上，监听可读事件
         */
        socketChannel.register(selector, SelectionKey.OP_READ);

        ByteBuffer byteBUffer = Charset.forName("UTF-8").encode("你已进入聊天室");
        socketChannel.write(byteBUffer);
    }

    /**
     * 读取channel数据
     */
    private void readChannel(SocketChannel socketChannel)
            throws IOException {
        /**
         * 创建buffer
         */
        ByteBuffer bytebuffer = ByteBuffer.allocate(1024);
        /**
         * 客户端数据写入buffer
         */
        String msg = "";
        while (socketChannel.read(bytebuffer)>0){
            /**
             * buffer写入完毕，转读buffer模式
             */
            bytebuffer.flip();
            /**
             * 读取buffer中的数据
             */
            msg += Charset.forName("UTF-8").decode(bytebuffer);
        }
        /**
         * 客户端发送的信息广播给其他客户端
         */
        if (msg.length() > 0){
            broadCast(socketChannel, msg);
        }

    }

    /**
     * 广播给其他客户端
     */
    private void broadCast(SocketChannel sourceChannel, String request){
        /**
         * 获取selector上的所有channel
         */
        Set<SelectionKey> selectionkeys = selector.keys();

        /**
         * 循环所有channel，广播信息
         */
        selectionkeys.forEach(selectionKey -> {
            SelectableChannel targetChannel = selectionKey.channel();
            /**
             * channel不是发送消息的客户端channel，也不是ServerSocketChannel，就接收消息
             */
            if (targetChannel != sourceChannel
                && targetChannel instanceof SocketChannel){
                ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(request);
                try {
                    ((SocketChannel) targetChannel).write(byteBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 主方法
     * @param args
     */
    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }

}
