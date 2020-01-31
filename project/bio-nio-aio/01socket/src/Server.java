import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int DEFAULT_PORT = 8888;
    private static final String QUIT_MARK = "quit";

    public static void main(String[] args) {
        try (
            // 创建socket
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            // 监听，并阻塞至连接建立完成
            Socket socket = serverSocket.accept();
            // socket读输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            // socket写输出流
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            )
        ){
            while (true){
                System.out.println("等待连接");
                System.out.println("【"+socket.getPort()+"】已连接");

                String msg = null;
                while ((msg = br.readLine()) != null){
                    System.out.println("接收【"+socket.getPort()+"】："+msg);

                    // 向输出端写数据
                    bw.write("【服务端】回复："+msg +"\n");
                    // 确保缓存区内所有数据都写到输出端
                    bw.flush();

                    // 收到quit，主动退出循环
                    if (QUIT_MARK.equals(msg)){
                        System.out.println("【"+socket.getPort()+"】离开");
                        break;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
