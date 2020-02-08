package client;

import java.io.*;
import java.net.Socket;

public class ChatClient {

     private final String IP = "127.0.0.1";
     private final int PORT = 8888;
     private final String QUIT = "quit";
     private Socket socket;
     private BufferedReader reader;
     private BufferedWriter writer;

    // 创建socket连接
    public void start(){
        try {
            socket = new Socket(IP, PORT);
            System.out.println("连接成功");
            // socket输入流
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            // socket输出流
            writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );

            new Thread(new UserInputHandler(this
            )).start();

            // 循环监控打印接收到的信息
            String msg = null;
            while ((msg = receive()) != null){
                System.out.println(msg);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            close();
        }
    }

     // 发送消息
     public void send(String msg) throws IOException {
         if (!socket.isOutputShutdown()){
             writer.write(msg+"\n");
             writer.flush();
         }
     }

     // 接收消息
     public String receive() throws IOException {
         String msg = null;
         if (!socket.isOutputShutdown()){
             msg = reader.readLine();
         }
         return msg;
     }

     // 退出
     public boolean readToQuit(String msg){
         return QUIT.equals(msg);
     }

     // 关闭
     public void close(){
         if (writer != null){
             try {
                 writer.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }


    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        chatClient.start();
    }

}
