package com.designpattern.mediator;

public class Client {
    public static void main(String[] args) {

        MainBoard mediator = new MainBoard();
        CDDriver cd = new CDDriver(mediator);
        CPU cpu = new CPU(mediator);
        VideoCard vc = new VideoCard(mediator);
        SoundCard sc = new SoundCard(mediator);

        mediator.setCdDriver(cd);
        mediator.setCpu(cpu);
        mediator.setVideoCard(vc);
        mediator.setSoundCard(sc);

        cd.readCD();
    }
}
