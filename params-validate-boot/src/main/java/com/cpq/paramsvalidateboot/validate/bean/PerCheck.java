package com.cpq.paramsvalidateboot.validate.bean;

public class PerCheck {

    private boolean isPass;
    private String msg;

    public PerCheck() {
    }

    public PerCheck(boolean isPass) {
        this.isPass = isPass;
    }

    public PerCheck(boolean isPass, String msg) {
        this.isPass = isPass;
        this.msg = msg;
    }

    public boolean getPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
