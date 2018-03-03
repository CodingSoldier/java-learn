package projectnote.commonsjar.io;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/15
 */
//  http://blog.csdn.net/zhaoyanjun6/article/details/55051917
//    https://www.cnblogs.com/xing901022/p/5978989.html
public class Io_Utils {
    //从流中读取内容并转为List<String>
    @Test
    public void t1() throws Exception{
        String path1 = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\Lang3.java";
        InputStream is = new FileInputStream(path1);
        List<String> list = IOUtils.readLines(is,"utf-8");
        for (String line:list){
            System.out.println(line);
        }
        is.close();
    }

    //字符写入输出流
    @Test
    public void t2() throws Exception{
        String path1 = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\Lang3.txt";
        OutputStream os = new FileOutputStream(path1);
        IOUtils.write("将字符串写入输出流",os,"utf-8");
        os.close();
    }

    // http://ifeve.com/java-7%E4%B8%AD%E7%9A%84try-with-resources
    // try-width-resource，在try后面的括号中创建流，try代码块运行结束，括号中创建的流将会被自动关闭
    @Test
    public void t3() throws Exception{
        String path = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\Lang3.txt";
        try (
            OutputStream os = new FileOutputStream(path);
            InputStream is = new FileInputStream(path);
        ){
            IOUtils.write("将字符串写入输出流",os,"utf-8");
        }
    }
}











