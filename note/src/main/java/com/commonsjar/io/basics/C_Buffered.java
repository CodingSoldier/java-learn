package com.commonsjar.io.basics;

import org.junit.Test;

import java.io.*;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/16
 */
//  http://blog.csdn.net/zhaoyanjun6/article/details/54894451
public class C_Buffered {
    @Test
    public void file1() throws Exception{

        String ip = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\c_big.png";
        String op = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\c_big_c.png";
        InputStream is = new BufferedInputStream(new FileInputStream(ip));
        OutputStream os = new BufferedOutputStream(new FileOutputStream(op));
        byte[] array = new byte[1024*8];  //BufferedInputStream、BufferedOutputStream的默认的缓冲区大小都是8K
        int length = 0;
        /**
         * 将通过inputstream将文件读取到二进制内存数组，再将二进制数组通过outputstream写入磁盘。
         * read(byte[] array)方法从输入流中读取array.length长度的字节，并返回字节数。最后一次读取的字节不满array.length，则read(byte[] array)返回可读取到的字节数（小于array.length）
         * os.write(byte b[], int off, int len)。
         * b[]：将byte[]array写入输出流，
         * off:从array的off下标开始写入，写0即可。
         * len：array写入输出流的大小，最后一次写入的大小很可能不是array.length，而是read()返回的字节数。此参数可以防止拷贝文件大于源文件。
         */
        while ((length = is.read(array)) != -1){
            os.write(array, 0, length);
        }

        os.flush();  //当buffered缓冲区满了、流close时，会自动调用flush()，但流可能关闭失败且最后一次write的数据不能将缓冲区填满，则最后保留在缓冲区的数据不会写入磁盘，导致写入失败，所以加上flush()更好

        ////使用这代码，每次写入的字节都是array.length，图片会变大
        //while (is.read(array) != -1){
        //    os.write(array);
        //}

        os.close();
        is.close();

    }
}
