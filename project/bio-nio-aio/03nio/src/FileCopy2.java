package src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

    static String source = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\03nio\\source.jpg";
    static String target = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\03nio\\target.jpg";

    public static void main(String[] args) {
        try (
                FileChannel fcin = new FileInputStream(source).getChannel();
                FileChannel fcout = new FileOutputStream(target).getChannel()
        ){
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            /**
             * 输入通道读取系统文件，并写入buffer。
             * 通道读取数据时buffer处于写模式
             */
            while (fcin.read(buffer) != -1){
                // 写入buffer完成，转读buffer模式
                buffer.flip();
                // 若buffer中还有数据没被读取，则继续从buffer中读取数据
                while (buffer.hasRemaining()){
                    /**
                     * 输出通道写数据到系统文件，数据是从buffer读取的。
                     * 通道写数据时，buffer处于读模式
                     */
                    fcout.write(buffer);
                }
                // buffer没残留数据，全部数据被读取完毕，转写模式
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}












