import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ChatClient {
    private final String IP = "127.0.0.1";
    private final int PORT = 8888;
    private final String QUIT = "quit";
    private  final Charset charset = Charset.forName("UTF-8");
    private final int BUFFER_SIZE = 1024;
    private SocketChannel client = null;

    private ByteBuffer rBuffer = ByteBuffer.allocate(BUFFER_SIZE);
    private ByteBuffer wBuffer = ByteBuffer.allocate(BUFFER_SIZE);


    private void star() {
        try {
            client = SocketChannel.open();
            client.configureBlocking(false);

            Selector selector = Selector.open();
            // 注册connect事件
            client.register(selector, SelectionKey.OP_CONNECT);

            //请求连接 异步
            client.connect(new InetSocketAddress(IP, PORT));

            while (true) {
                selector.select();
                //System.out.println("有事件被触发");
                for (SelectionKey key : selector.selectedKeys()) {
                    // 处理事件
                    handles(key);
                }
                selector.selectedKeys().clear();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void handles(SelectionKey key) throws IOException {

        if (key.isConnectable()) {
            //如果返回true就说明连接就绪,可以停止连接这个动作了,false的话说明还没有连接,需要等待
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();

                //处理用户输入
                new Thread(new UserInputHandler(this)).start();
                System.out.println("连接成功");
            }
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
            // READ事件
        } else if (key.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            String msg = readMsg(socketChannel);
            if (msg.isEmpty()) {
                close(key.selector());
            } else {
                System.out.println(msg);
            }
        }

    }

    // 读消息
    private String readMsg(SocketChannel socketChannel) throws IOException {
        rBuffer.clear();
        while (socketChannel.read(rBuffer) > 0) ;
        rBuffer.flip();
        return String.valueOf(charset.decode(rBuffer));
    }

    // 发消息
    public void send(String msg) throws IOException {
        if (msg.isEmpty()) return;
        wBuffer.clear();
        wBuffer.put(charset.encode(msg));
        wBuffer.flip();
        while (wBuffer.hasRemaining()) {
            client.write(wBuffer);
        }

        // 检测用户是否退出
        if (readToQuit(msg)) {
            client.close();
        }

    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //判断是否准备退出
    public boolean readToQuit(String msg) {
        return QUIT.equals(msg);
    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.star();
    }
}
