package com.cpq.paramsvalidateboot.bean2;

public class ResultCheckVo extends User {
    private Boolean isPass;
    private String a;
    private String bb;
    private String name;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public Boolean getPass() {
        return isPass;
    }

    public void setPass(Boolean pass) {
        isPass = pass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
