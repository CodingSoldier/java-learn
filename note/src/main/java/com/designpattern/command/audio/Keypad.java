package com.designpattern.command.audio;

public class Keypad {
    private Command playCommand;
    private Command rewindCommand;
    private Command stopCommand;

    public void setPlayCommand(Command playCommand){
        this.playCommand=playCommand;
    }

    public void setRewindCommand(Command rewindCommand) {
        this.rewindCommand = rewindCommand;
    }

    public void setStopCommand(Command stopCommand) {
        this.stopCommand = stopCommand;
    }

    public void rewind(){
        rewindCommand.execute();
    }
    public void play(){
        playCommand.execute();
    }
    public void stop(){
        stopCommand.execute();
    }
}
