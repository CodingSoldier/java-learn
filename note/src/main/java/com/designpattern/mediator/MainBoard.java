package com.designpattern.mediator;

public class MainBoard implements Mediator {

    //需要知道要交互的同事类——光驱类
    private CDDriver cdDriver = null;
    //需要知道要交互的同事类——CPU类
    private CPU cpu = null;
    //需要知道要交互的同事类——显卡类
    private VideoCard videoCard = null;
    //需要知道要交互的同事类——声卡类
    private SoundCard soundCard = null;

    public void setCdDriver(CDDriver cdDriver) {
        this.cdDriver = cdDriver;
    }

    public void setCpu(CPU cpu) {
        this.cpu = cpu;
    }

    public void setVideoCard(VideoCard videoCard) {
        this.videoCard = videoCard;
    }

    public void setSoundCard(SoundCard soundCard) {
        this.soundCard = soundCard;
    }

    @Override
    public void changed(Colleague c) {
        if(c instanceof CDDriver){
            //表示光驱读取数据了
            this.opeCDDriverReadData((CDDriver)c);
        }else if(c instanceof CPU){
            this.opeCPU((CPU)c);
        }
    }
    /**
     * 处理光驱读取数据以后与其他对象的交互
     */
    private void opeCDDriverReadData(CDDriver cd){
        //先获取光驱读取的数据
        String data = cd.getData();
        //把这些数据传递给CPU进行处理
        cpu.executeData(data);
    }
    /**
     * 处理CPU处理完数据后与其他对象的交互
     */
    private void opeCPU(CPU cpu){
        //先获取CPU处理后的数据
        String videoData = cpu.getVideoData();
        String soundData = cpu.getSoundData();
        //把这些数据传递给显卡和声卡展示出来
        videoCard.showData(videoData);
        soundCard.soundData(soundData);
    }
}
