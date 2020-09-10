package com.cpq.gradle02java;

public class TodoItem {
    private String name;
    private boolean hasDone;

    public TodoItem(String name) {
        this.name = name;
    }

    public TodoItem(String name, boolean hasDone) {
        this.name = name;
        this.hasDone = hasDone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHasDone() {
        return hasDone;
    }

    public void setHasDone(boolean hasDone) {
        this.hasDone = hasDone;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "name='" + name + '\'' +
                ", hasDone=" + hasDone +
                '}';
    }
}
