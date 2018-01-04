package com.commonsjar.io;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/16
 */
// http://blog.csdn.net/zhaoyanjun6/article/details/54972773
public class File_Utils {

    //复制文件夹以及文件夹中的子文件夹、文件
    @Test
    public void t() throws Exception{
        String src = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\basics";
        String tag = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\basics1";
        File s = new File(src);
        File t = new File(tag);
        FileUtils.copyDirectory(s, t);
    }

    //复制文件
    @Test
    public void t2() throws Exception{
        String src = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\Io.java";
        String tag = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\basics1\\I.java";
        File s = new File(src);
        File t = new File(tag);
        FileUtils.copyFile(s, t);
    }

    @Test
    public void t3() throws Exception{
        String tag = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\test.txt";
        File t = new File(tag);
        FileUtils.writeStringToFile(t,"写入到文件的字符串","utf-8", true);
        //writeStringToFile(final File file, final String data, final Charset encoding, final boolean append)
        //参数1：需要写入的文件，如果文件不存在，将自动创建。  参数2：需要写入的内容
        //参数3：编码格式     参数4：是否为追加模式（ ture: 追加模式，把字符串追加到原内容后面）
    }

    //将文件读取到字节数组，字节数组写入文件
    @Test
    public void t4() throws Exception{
        String s = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\Lang3.java";
        String t = "E:\\workspace\\java-learn\\note\\src\\main\\java\\com\\commonsjar\\io\\Lang3.txt";
        File sf = new File(s);
        File tf = new File(t);
        byte[] bytes = FileUtils.readFileToByteArray(sf);
        FileUtils.writeByteArrayToFile(tf, bytes);
    }


}
