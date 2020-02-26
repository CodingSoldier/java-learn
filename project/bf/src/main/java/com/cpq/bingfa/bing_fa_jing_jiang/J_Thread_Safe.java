package com.cpq.bf.bing_fa_jing_jiang;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-17
 */
public class J_Thread_Safe {

}


/**
 * 模拟new一个未完整对象的例子，
 * 在对象的构造函数内将this赋值给外部对象，并在赋值后等待一段时间
 * 其他线程获取此对象的时候就不是一个完整的对象
 */
class NewNotFinishObj{
    private final int x, y;
    public NewNotFinishObj(int x, int y) throws InterruptedException{
        this.x = x;
        TestNewNotFinishObj.newNotFinishObj = this;
        TimeUnit.SECONDS.sleep(2);
        this.y = y;
    }
    @Override
    public String toString() {
        return "NewNotFinishObj{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class TestNewNotFinishObj{
    static NewNotFinishObj newNotFinishObj;
    public static void main(String[] args) throws Exception{
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newNotFinishObj = new NewNotFinishObj(1,2);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println("未初始化完成的实例"+newNotFinishObj);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("已经初始化完成的实例"+newNotFinishObj);
    }
}








