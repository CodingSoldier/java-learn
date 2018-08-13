package com.designpattern.memento.p1;

public class Originator {
    private String state;
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
        System.out.println("赋值状态："+state);
    }

    public MementoIF createMemento(){
        return new Memento(state);
    }

    public void restoreMemento(MementoIF memento){
        this.setState(((Memento)memento).getState());
    }

    private class Memento implements MementoIF{
        private String state;

        public Memento(String state) {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
