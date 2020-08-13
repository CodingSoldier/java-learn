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


public class FileCopy {

    static String source = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\nio\\111.jpg";
    static String target = "D:\\third-code\\java-learn\\project\\bio-nio-aio\\nio\\222.jpg";


    public static void main(String[] args) {

        //try (
        //    FileInputStream fin = new FileInputStream(source);
        //    FileOutputStream fout = new FileOutputStream(target);
        //){
        //    /**
        //     * fin.read()一个字节一个字节地读，返回的是the next byte of data。返回-1表示读完了
        //     * 详情请查看源码api
        //     */
        //    int n;
        //    while ((n=fin.read()) != -1){
        //        fout.write(n);
        //    }
        //}catch (IOException e){
        //    e.printStackTrace();
        //}



        //try (
        //        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(source));
        //        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(target))
        //){
        //    byte[] buffer = new byte[1024];
        //    int len;
        //    while ((len=bin.read(buffer)) != -1){
        //        bout.write(buffer, 0, len);
        //    }
        //}catch (IOException e){
        //    e.printStackTrace();
        //}


        /**
         * 常见的Channel.jpg
         * Channel（通道），类似于BIO中的Stream（流）
         */
        try (
                FileChannel fcin = new FileInputStream(source).getChannel();
                FileChannel fcout = new FileOutputStream(target).getChannel()
        ){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            /**
             * 输入通道读取系统文件，并写入buffer。
             * 通道读取数据时buffer处于写模式
             */
            while (fcin.read(byteBuffer) != -1){
                // 写入buffer完成，转读buffer模式
                byteBuffer.flip();
                // 若buffer中还有数据没被读取，则继续从buffer中读取数据
                while (byteBuffer.hasRemaining()){
                    /**
                     * 输出通道写数据到系统文件，数据是从buffer读取的。
                     * 通道写数据时，buffer处于读模式
                     */
                    fcout.write(byteBuffer);
                }
                // buffer没残留数据，全部数据被读取完毕，转写模式
                byteBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        //try (
        //    FileChannel fcin = new FileInputStream(source).getChannel();
        //    FileChannel fcout = new FileOutputStream(target).getChannel();
        //) {
        //    long transize = 0;
        //    long size = fcin.size()-2048;
        //    // transferTo不能保证将缓冲区的数据全部刷新到输出流
        //    // 需要通过while比较源文件、目标文件的大小来保证
        //    while (transize <= size){
        //        transize += fcin.transferTo(0, fcin.size(), fcout);
        //    }
        //}catch (IOException e){
        //    e.printStackTrace();
        //}


        /**
         * java的BIO底层实现类实际上也用了nio的方式重写
         * 使用带缓冲区的bio效率与bio相差不大
         */
        //System.out.println(date);

    }

}
