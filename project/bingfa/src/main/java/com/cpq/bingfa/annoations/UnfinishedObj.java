package com.cpq.bingfa.annoations;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-10-15
 */
public class UnfinishedObj {
    private final int x, y;

    public UnfinishedObj(int x, int y) {
        this.x = x;
        TestUnfinishedObj.instance = this;
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.y = y;
    }

    @Override
    public String toString() {
        return "UnfinishedObj{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
