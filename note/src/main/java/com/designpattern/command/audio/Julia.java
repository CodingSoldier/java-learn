package com.designpattern.command.audio;

public class Julia {

    public static void main(String[]args){

        AudioPlayer player = new AudioPlayer();
        Command playCommand = new PlayCommand(player);
        Command rewindCommand = new RewindCommand(player);
        Command stopCommand = new StopCommand(player);

        Keypad keypad = new Keypad();
        keypad.setPlayCommand(playCommand);
        keypad.setRewindCommand(rewindCommand);
        keypad.setStopCommand(stopCommand);

        keypad.play();
        keypad.rewind();
        keypad.stop();
        keypad.play();
        keypad.stop();

    }

}
