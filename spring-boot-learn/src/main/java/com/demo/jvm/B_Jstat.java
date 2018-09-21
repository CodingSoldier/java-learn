package com.demo.jvm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jvm/jstat")
public class B_Jstat {
    Logger logger = LoggerFactory.getLogger(A_OutOfMemoryDemo.class);
    static List<A_OutOfMemoryDemo.OOMObject2> list = new ArrayList<>();

/*
https://www.cnblogs.com/duanxz/p/3520829.html
http://lovestblog.cn/blog/2016/10/29/metaspace/

jstat统计监控，最好打成jar再测试
1、启动jar包    java -jar -Xms512m -Xmx512m spring-boot-lean.jar
2、jstat -gc 线程号 10000 200
3、发送请求  localhost:8080/jvm/jstat/obj
监控结果

 S0C        S1C      S0U     S1U      EC        EU        OC         OU       MC      MU       CCSC    CCSU     YGC     YGCT  FGC     FGCT     GCT
17920.0   18432.0    0.0    9471.3 137216.0  136419.9  349696.0   28723.9   42280.0  41126.5  5416.0  5185.2     13    0.197   2      0.092    0.290
17920.0   17408.0   8202.6   0.0   139264.0   2089.6   349696.0   31476.0   43176.0  41996.8  5544.0  5299.3     14    0.218   2      0.092    0.310
17920.0   17408.0   8202.6   0.0   139264.0   2095.3   349696.0   31476.0   43176.0  41996.8  5544.0  5299.3     14    0.218   2      0.092    0.310
17920.0   17408.0   8202.6   0.0   139264.0   2397.6   349696.0   31476.0   43176.0  41996.8  5544.0  5299.3     14    0.218   2      0.092    0.310
17408.0   16896.0    0.0     0.0   140288.0  140288.0  349696.0   339953.4  43688.0  42686.1  5544.0  5378.2     16    0.272   6      0.517    0.789
17408.0   16896.0    0.0     0.0   140288.0  140288.0  349696.0   339953.9  43688.0  42686.1  5544.0  5378.2     16    0.272   6      0.517    0.789

上面数值的单位是KB
S0C  survivor区0总容量，单位KB
S1C  survivor区1总容量，单位KB
S0U  survivor区0使用量，单位KB
S1U  survivor区1使用量，单位KB
EC   Eden区总容量，单位KB
EU   Eden区使用量，单位KB
OC   老年代总容量，单位KB
OU   老年代使用量，单位KB
MC    ：Metaspace 总大小
MU    ：Metaspace 使用大小
CCSC   ：Metaspace中Klass Metaspace总大小
CCSU   ：Metaspace中Klass Metaspace使用大小
YGC     ：Young GC 次数
YGCT   ：Young GC 消耗总时间
FGC     ：FullGC 次数
FGCT   ：FullGC 消耗总时间
GCT     ：GC总消耗时间
*/
    static class OOMObject2{
        public byte[] placeholder = new byte[1024 * 1024 * 10];  //10M byte数组
    }
    public static void fillOutOfMemory(int num) throws InterruptedException{
         list = new ArrayList<>();
        for (int i = 0; i < num ; i++){
            list.add(new A_OutOfMemoryDemo.OOMObject2());
        }
    }
    @GetMapping("/obj")
    public String outOfMemory(HttpServletRequest request) throws Exception{
        logger.info("***********************outOfMemory***************");
        fillOutOfMemory(1000);
        return "完成";
    }

    //内存溢出后是否还能请求接口
    @GetMapping("/afterOutOfMemory")
    public String afterOutOfMemory(HttpServletRequest request) throws Exception{
        logger.info("***********************outOfMemory***************");
        return "返回结果";
    }
}
