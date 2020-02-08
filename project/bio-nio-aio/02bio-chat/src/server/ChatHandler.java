package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatHandler implements Runnable {
    private ChatServer server;
    private Socket socket;

    public ChatHandler(ChatServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // 添加上线用户
            server.addClient(socket);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            String msg = null;
            while ((msg = reader.readLine()) != null){
                String fwmsg = "客户端【"+socket.getPort()+"】"+msg+"\n";

                // 当前客户端发的消息转发给其他客户端
                server.forward(socket, fwmsg);

                if (server.readToQuit(msg)){
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                server.removeClient(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
