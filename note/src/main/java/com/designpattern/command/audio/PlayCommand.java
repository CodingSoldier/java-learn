package com.designpattern.command.audio;

public class PlayCommand implements Command{

    private AudioPlayer myAudio;

    public PlayCommand(AudioPlayer audioPlayer){
        myAudio = audioPlayer;
    }

    @Override
    public void execute() {
        myAudio.play();
    }
}
