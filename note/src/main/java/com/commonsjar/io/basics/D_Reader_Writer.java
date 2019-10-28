package com.commonsjar.io.basics;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

//  http://blog.csdn.net/zhaoyanjun6/article/details/54911237
public class D_Reader_Writer {
    /**
     *                 Reader
     *        字符流
     *                 Writer
     *
     * IO流
     *
     *                InputStream
     *        字节流
     *                OutputStream
     */
    //字符流，可以读取文本文件；但不能读取图片、视频这种非文本类型的二进制文件
    @Test
    public void t() throws Exception{
        String r = "getNodeEdgeNum:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\basics\\b_file01.txt";
        String w = "getNodeEdgeNum:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\basics\\d_file01.txt";
        BufferedReader br = new BufferedReader(new FileReader(r));
        BufferedWriter bw = new BufferedWriter(new FileWriter(w));

        String result = null;
        while ((result = br.readLine()) != null){  //readLine读取到没有数据返回null
            bw.write(result);
            bw.newLine();
        }
        bw.flush();
        bw.close();
        br.close();
    }
}
