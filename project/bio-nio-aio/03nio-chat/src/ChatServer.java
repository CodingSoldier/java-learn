package src;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Set;

public class ChatServer {
    private  final int PORT = 8888;
    private  ByteBuffer rBuffer;
    private  ByteBuffer wBuffer;
    private  final int BUFFER_SIZE = 1024;
    private  final String QUIT = "qiut";
    private  final Charset charset = Charset.forName("UTF-8");
    private ServerSocketChannel server = null;

    public void star() {
        try {

            //启动服务器
            server = ServerSocketChannel.open();
            //默认是阻塞式的调用
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(PORT));
            System.out.println("启动服务器,监听端口" + server.socket().getLocalPort());


            rBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            wBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            Selector selector = Selector.open();
            //注册事件
            server.register(selector, SelectionKey.OP_ACCEPT);

            //轮询处理事件//select()返回触发的事件个数
            while (true) {
                selector.select();
                //得到触发的事件key
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey key : selectionKeys) {
                    //处理事件
                    handles(key);
                }
                //手动清空处理多的key,如果不clear的话,处理过的key还在里面
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理请求
    private void handles(SelectionKey key) throws IOException {

        // 处理ACCEPT事件
        if (key.isAcceptable()) {
            SocketChannel socketChannel = server.accept();
            //默认为阻塞式的
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
            System.out.println("客户端:" + socketChannel.socket().getPort() + "已连接");

            // 处理READ事件
        } else if (key.isReadable()) {
            //读取消息
            String msg = readMsg((SocketChannel) key.channel());
            System.out.println("客户端["+((SocketChannel) key.channel()).socket().getPort()+"]"+msg);
            // 客户端异常
            if (msg.isEmpty()) {
                key.cancel();
                key.selector().wakeup();
            } else {
                //转发消息
                forwardMsg((SocketChannel) key.channel(),key, msg);
                //判断是否要退出
                if (QUIT.equals(msg)) {
                    key.cancel();
                    key.selector().wakeup();
                    System.out.println("客户端" + ((SocketChannel) key.channel()).socket().getPort() + "已退出");
                }
            }

        }
    }

    // 读消息
    private String readMsg(SocketChannel channel) throws IOException {

        rBuffer.clear();
        // 如果用户一直输入的话,这里就一直读取,因为只要不关channel,用户可能一直输入
        while (channel.read(rBuffer) > 0) ;
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));

    }

    // 转发消息
    private void forwardMsg(SocketChannel client, SelectionKey key, String msg) throws IOException {
        // 只要注册的key都显示
        Set<SelectionKey> keys = key.selector().keys();
        for (SelectionKey selectionKey : keys) {
            Channel channel = selectionKey.channel();
            if (selectionKey.channel() instanceof ServerSocketChannel) continue;
            //判断selector是否正常
            if (key.isValid() && !selectionKey.channel().equals(client)) {
                wBuffer.clear();
                wBuffer.put(msg.getBytes());
                wBuffer.flip();
                while (wBuffer.hasRemaining()) {
                    ((SocketChannel)channel).write(wBuffer);
                }
            }
        }

    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.star();
    }


}
