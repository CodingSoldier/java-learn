package projectnote.commonsjar.io.basics;

import org.junit.Test;

import java.io.*;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/16
 */
//  http://blog.csdn.net/zhaoyanjun6/article/details/54923506
public class E_In_Re_Ou_Wr {

    /**
     * InputStreamReader、OutputStreamWriter实现字节流、字符流之间的转换。
     */
    @Test
    public void t() throws Exception{
        String i = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\io\\b_file01.txt";
        String o = "E:\\workspace\\java-learn\\web-whole\\src\\main\\java\\ssm\\projectnote\\commonsjar\\io\\E_file01.txt";

        InputStreamReader isr = new InputStreamReader(new BufferedInputStream(new FileInputStream(i)));
        //转换流可设置编码
        OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(o)), "gbk");

        //InputStreamReader读取的数据放在char[]中。char[]可以写入OutputStreamWriter，byte[]不可以。
        char[] chars = new char[1024*8];
        int length = 0;
        while ((length = isr.read(chars)) != -1){
            osw.write(chars, 0, length);
            //字符数组写入输出转换流。文件中的换行不会FileInputStream读取出来，换行符也是一个符号，存储在chars中，不需要考虑换行。
        }
        osw.flush();
        osw.close();
        isr.close();
    }
}
