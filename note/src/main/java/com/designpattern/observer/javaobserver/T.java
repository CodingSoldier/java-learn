package com.designpattern.observer.javaobserver;

import org.junit.Test;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/16
 *
 * https://www.cnblogs.com/java-my-life/archive/2012/05/16/2502279.html
 */
public class T {
    @Test
    public void t(){
        Watched watched = new Watched();
        Watcher watcher = new Watcher(watched);
        watched.setData("changed");
        watched.setData("changed11");
    }
}
