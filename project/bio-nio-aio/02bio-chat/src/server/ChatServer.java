package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
    private int PORT = 8888;
    private final String QUIT = "quit";
    private ServerSocket serverSocket;
    private Map<Integer, Writer> clientMap;
    private ExecutorService executorService;

    public ChatServer(){
        this.clientMap = new HashMap<>();
        executorService = Executors.newFixedThreadPool(10);
    }

    // 新增客户端
    public synchronized void addClient(Socket socket) throws IOException{
        if (socket != null){
            // 客户端writer
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            // 客户端writer集合
            clientMap.put(socket.getPort(), writer);
        }
    }

    // 删除客户端
    public synchronized void removeClient(Socket socket) throws IOException {
        if (socket != null){
            int port = socket.getPort();
            if (clientMap.containsKey(socket.getPort())){
                clientMap.get(socket.getPort()).close();
            }
            clientMap.remove(socket.getPort());
            System.out.println(port+"已经下线");
        }
    }

    // 转发消息
    public synchronized void forward(Socket socket, String msg) throws IOException {
        for (int port:clientMap.keySet()){
            if (port == socket.getPort()){
                continue;
            }
            Writer writer = clientMap.get(port);
            writer.write(msg);
            writer.flush();
        }
    }

    // 退出字符串
    public boolean readToQuit(String msg){
        return QUIT.equals(msg);
    }

    // 关闭服务
    public void close(){
        if (serverSocket != null){
            try {
                serverSocket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    // 开启服务
    public void start(){
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server启动成功，等待连接");
            // 轮询接收新连接
            while (true){
                // 等待客户端连接
                Socket socket = serverSocket.accept();
                System.out.println(socket.getPort()+"已经连接");

                // 客户端连接任务提交给线程池
                executorService.execute(new ChatHandler(this, socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close();
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}
