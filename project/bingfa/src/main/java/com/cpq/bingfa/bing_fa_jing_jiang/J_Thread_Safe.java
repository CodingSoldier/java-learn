package com.cpq.bingfa.bing_fa_jing_jiang;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2019-12-17
 */
public class J_Thread_Safe {

}


/**
 * 模拟得到一个未初始化完成对象的例子，
 * 在对象的构造函数内将this赋值给外部对象，并在赋值后等待一段时间
 * 其他线程获取此对象的时候就不是一个完整的对象
 */
class UnfinishedObj {
    private final int x, y;

    public UnfinishedObj(int x, int y){
        this.x = x;
        TestUnfinishedObj.instance = this;
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
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

class TestUnfinishedObj {
    static UnfinishedObj instance;
    public static void main(String[] args) throws Exception{
        new Thread(() -> {
            instance = new UnfinishedObj(1,2);
        }).start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("未初始化完成的实例"+ instance);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("已经初始化完成的实例"+ instance);
    }
}








