package com.designpattern.status.vote;

public class BlackVoteState implements VoteState {
    @Override
    public void vote(String user, String voteItem, VoteManager voteManager) {
        System.out.println("黑名单，禁止登录使用本系统");
    }
}
