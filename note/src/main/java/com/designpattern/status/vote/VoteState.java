package com.designpattern.status.vote;

public interface VoteState {
    void vote(String user, String voteItem, VoteManager voteManager);
}
