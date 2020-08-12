package src;

import java.io.*;

/**
 * NIO使用Channel替代了stream
 *   stream是单方向的
 *   Channel再次双向操作，可读可写
 * 使用Selector监控多条Channel，查看那条Channel的数据已经准备好了
 * 可以在一个线程中处理多个Channel/IO
 *
 * Channel必须配合buffer使用
 *
 * channel之间可以进行数据交换
 */


public class FileCopy2 {

    static String source = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\nio\\buffer理解.jpg";
    static String target = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\nio\\111.jpg";


    public static void main(String[] args) {

        try (
            FileInputStream fin = new FileInputStream(source);
            FileOutputStream fout = new FileOutputStream(target);
            BufferedInputStream bin = new BufferedInputStream(fin);
            BufferedOutputStream bout = new BufferedOutputStream(fout);
        ){
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bin.read(buffer)) != -1){
                bout.write(buffer, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}












