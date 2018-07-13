package com.demo.paramsvalidate.bean;

import java.util.Set;

/**
 * author chenpiqian
 */
public class ResultValidate {

    private boolean isPass;

    private Set<String> msgSet;

    private Throwable throwable;

    public ResultValidate() {
    }

    public ResultValidate(boolean isPass) {
        this.isPass = isPass;
    }

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

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
