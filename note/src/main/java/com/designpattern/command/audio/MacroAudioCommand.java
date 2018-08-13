package com.designpattern.command.audio;

import java.util.ArrayList;
import java.util.List;

public class MacroAudioCommand implements MacroCommand{
    private List<Command> commandList = new ArrayList<>();

    @Override
    public void add(Command cmd) {
        commandList.add(cmd);
    }

    @Override
    public void remove(Command cmd) {
        commandList.remove(cmd);
    }

    @Override
    public void execute() {
        for (Command cmd:commandList){
            cmd.execute();
        }
    }
}
