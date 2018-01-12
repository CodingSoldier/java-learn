package com.spring.iocdemo;

import com.spring.iocdemo.scan.Dao2;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/10
 */
public class Service1 {
    //@ResourceCustom(name = "dao1")
    //@ResourceCustom("dao1")
    @ResourceCustom
    private Dao1 dao1;  //dao1在xml中配置了

    //@ResourceCustom(name = "dao222")
    //@ResourceCustom("dao22")
    @ResourceCustom
    private Dao2 dao2;  //dao2使用了自定义注解

    public void fieldMethod(){
        dao1.show();
        dao2.show();
    }
}
