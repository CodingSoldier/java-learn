package ssm.projectnote.commonsjar.io.basics;

import org.junit.Test;

import java.io.*;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/16
 */
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
        String r = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\io\\b_file01.txt";
        String w = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\io\\d_file01.txt";
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
