package com.designpattern.status.vote;

public class Client {
    public static void main(String[] args) {

        VoteManager vm = new VoteManager();
        for (int i=0; i<20; i++){
            vm.vote("ul", "A");
        }
    }
}
