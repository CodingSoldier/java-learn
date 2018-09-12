package com.demo.paramsvalidate.bean;

import java.util.List;

/**
 * author chenpiqian
 */
public class ResultValidate {

    private boolean pass;

    private List<String> msgList;

    public ResultValidate() {
    }

    public ResultValidate(boolean pass) {
        this.pass = pass;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }
}
