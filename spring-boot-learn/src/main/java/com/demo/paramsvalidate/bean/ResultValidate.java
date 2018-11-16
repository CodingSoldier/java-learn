package com.demo.paramsvalidate.bean;

import java.util.List;
import java.util.Map;

/**
 * author chenpiqian
 */
public class ResultValidate {

    private boolean pass;

    private List<Map<String, String>> msgList;

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

    public List<Map<String, String>> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Map<String, String>> msgList) {
        this.msgList = msgList;
    }
}
