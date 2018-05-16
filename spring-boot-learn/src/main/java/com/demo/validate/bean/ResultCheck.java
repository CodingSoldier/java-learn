package com.demo.validate.bean;

import java.util.Set;

public class ResultCheck {

    private Boolean isPass;

    private Set<String> msgSet;

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public Set<String> getMsgSet() {
        return msgSet;
    }

    public void setMsgSet(Set<String> msgSet) {
        this.msgSet = msgSet;
    }
}
