import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

    static String source = "E:\\workspace\\java-learn\\project\\bio-nio-aio\\nio\\buffer理解.jpg";
    static String target = "E:\\workspace\\java-learn\\project\\bio-nio-aio\\nio\\111.jpg";


    public static void main(String[] args) {

        //try (
        //    FileInputStream fin = new FileInputStream(source);
        //    FileOutputStream fout = new FileOutputStream(target);
        //){
        //    int n;
        //    while ((n=fin.read()) != -1){
        //        fout.write(n);
        //    }
        //}catch (IOException e){
        //    e.printStackTrace();
        //}



        //try (
        //    BufferedInputStream bin = new BufferedInputStream(new FileInputStream(source));
        //    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(target))
        //){
        //    byte[] buffer = new byte[1024];
        //    int len;
        //    while ((len=bin.read(buffer)) != -1){
        //        bout.write(buffer, 0, len);
        //    }
        //}catch (IOException e){
        //    e.printStackTrace();
        //}


        //try (
        //    FileChannel fcin = new FileInputStream(source).getChannel();
        //    FileChannel fcout = new FileOutputStream(target).getChannel()
        //){
        //    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //    int len;
        //    // 输入流数据写入buffer
        //    while ((len = fcin.read(byteBuffer)) != -1){
        //        // buffer写入完成，转读buffer模式
        //        byteBuffer.flip();
        //        // 没读取完，继续循环
        //        while (byteBuffer.hasRemaining()){
        //            // 输出流从buffer中读取数据
        //            fcout.write(byteBuffer);
        //        }
        //        // buffer中的数据全部读取完毕，转写模式
        //        byteBuffer.clear();
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}


        try (
            FileChannel fcin = new FileInputStream(source).getChannel();
            FileChannel fcout = new FileOutputStream(target).getChannel();
        ) {
            long transize = 0;
            long size = fcin.size();
            // transferTo不能保证将缓冲区的数据全部刷新到输出流
            // 需要通过while比较源文件、目标文件的大小来保证
            while (transize != size){
                transize += fcin.transferTo(0, fcin.size(), fcout);
            }
        }catch (IOException e){
            e.printStackTrace();
        }



        /**
         * java的BIO底层实现类实际上也用了nio的方式重写
         * 使用带缓冲区的bio效率与bio相差不大
         */

    }

}
