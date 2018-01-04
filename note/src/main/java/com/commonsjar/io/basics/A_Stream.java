package com.commonsjar.io.basics;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/15
 */
//  http://blog.csdn.net/zhaoyanjun6/article/details/54292148
public class A_Stream {
    //FileInputStream将本地文件读取到内存
    @Test
    public void r() throws Exception{
        //实例化一个输入流对象
        FileInputStream fis = new FileInputStream("E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\io\\b_file01.txt");
        //返回输入流中可被读取的字节(byte)数，文件使用utf-8编码，一个中文为3字节，一个数字、一个字母为1字节
        int size = fis.available();
        byte[] array = new byte[size];
        fis.read(array);  //将文件读取到内存的数组中
        fis.close();
        String resultStr = new String(array);
        System.out.println(resultStr);
    }

    //FileOutputStream将内存中的内容写入文件中
    @Test
    public void w() throws Exception{
        //文件file_w01.txt不存在，则创建文件。文件存在，则新内容覆盖就内容
        FileOutputStream fos = new FileOutputStream("E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\b_file_w01.txt");
        String string = "文字123abc.";
        byte[] array = string.getBytes();
        fos.write(array);  //FileOutputStream操作的是字节流，所以要将string转为字节数组，write参数类型之一也是字节数组
        fos.close();
    }

    //读取文件，写入文件，实现图片复制
    @Test
    public void rw() throws Exception{
        FileInputStream fis = new FileInputStream("E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\b_file-img-r01.png");
        int size = fis.available();
        byte[] array = new byte[size];
        fis.read(array); //将字节流读取到内存字节数组中，array元素全是8为二进制组成的数字

        FileOutputStream fos = new FileOutputStream("E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\b_file-img-r01-copy.png");
        fos.write(array); //字节数组通过FileOutputStream流写入到文件
        fis.close();   //关流后，fis.available()抛异常，提示流已经关闭，关流就将fis释放，占用的内存也释放了
        fos.close();
    }
}










