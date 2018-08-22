package com.designpattern.status.vote;

import java.util.HashMap;
import java.util.Map;

public class VoteManager {

    private VoteState state = null;
    private Map<String, String> mapVote = new HashMap<>();
    private Map<String, Integer> mapVoteCount = new HashMap<>();

    public Map<String, String> getMapVote(){
        return mapVote;
    }

    public void vote(String user, String voteItem){
        Integer oldVoteCount = mapVoteCount.get(user);
        if (oldVoteCount == null){
            
        }
    }

}
