package com.demo.validate.bean;

public class PerCheck {

    private Boolean isPass;
    private String msg;

    public PerCheck() {
    }

    public PerCheck(Boolean isPass, String msg) {
        this.isPass = isPass;
        this.msg = msg;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
