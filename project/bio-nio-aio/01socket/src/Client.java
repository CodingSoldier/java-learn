import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private static final String QUIT_MARK = "quit";
    public static void main(String[] args) {
        try (
            // 创建socket
            Socket socket = new Socket("127.0.0.1", Server.DEFAULT_PORT);
            // 读socket输入流
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );
            // 写socket输出流
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())
            );
            // 读取屏幕输入
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(System.in)
            )
        ){
            System.out.println("与"+socket.getPort()+"建立连接");

            while (true){
                System.out.println("请输入：");
                // 屏幕输入读一行
                String msg = reader.readLine();

                // 写入socket
                bw.write(msg+"\n");
                bw.flush();

                // 打印socket接收的输入流
                String msgAccept = br.readLine();
                System.out.println(msgAccept);

                if (QUIT_MARK.equals(msg)){
                    break;
                }
            }

        }catch (Exception e){

        }
    }
}
