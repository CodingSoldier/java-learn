package com.designpattern.mediator;

public class SoundCard extends Colleague {
    /**
     * 构造函数
     */
    public SoundCard(Mediator mediator) {
        super(mediator);
    }
    /**
     * 按照声频数据发出声音
     */
    public void soundData(String data){
        System.out.println("画外音：" + data);
    }
}
