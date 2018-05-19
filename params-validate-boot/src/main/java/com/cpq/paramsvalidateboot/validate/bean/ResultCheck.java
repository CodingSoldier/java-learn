package com.cpq.paramsvalidateboot.validate.bean;

import java.util.Set;

public class ResultCheck {

    private boolean isPass;

    private Set<String> msgSet;

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public Set<String> getMsgSet() {
        return msgSet;
    }

    public void setMsgSet(Set<String> msgSet) {
        this.msgSet = msgSet;
    }
}
