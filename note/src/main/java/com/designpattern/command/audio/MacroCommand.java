package com.designpattern.command.audio;

public interface MacroCommand extends Command {
    void add(Command cmd);
    void remove(Command cmd);
}
