package com.demo.testvalidate.bean;

import java.math.BigDecimal;
import java.util.List;

public class UserVo extends User {
    private float salaryNum;
    private BigDecimal bigNum;
    private Girl girl;
    private Family family;
    private List<String> hobbyList;
    private List<Dream> dreamList;
    private List<BaoBao> baoBaoList;

    public float getSalaryNum() {
        return salaryNum;
    }

    public void setSalaryNum(float salaryNum) {
        this.salaryNum = salaryNum;
    }

    public Girl getGirl() {
        return girl;
    }

    public void setGirl(Girl girl) {
        this.girl = girl;
    }

    public List<Dream> getDreamList() {
        return dreamList;
    }

    public void setDreamList(List<Dream> dreamList) {
        this.dreamList = dreamList;
    }

    public BigDecimal getBigNum() {
        return bigNum;
    }

    public void setBigNum(BigDecimal bigNum) {
        this.bigNum = bigNum;
    }

    public List<String> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<String> hobbyList) {
        this.hobbyList = hobbyList;
    }

    public Family getFamily() {
        return family;
    }

    public void setFamily(Family family) {
        this.family = family;
    }

    public List<BaoBao> getBaoBaoList() {
        return baoBaoList;
    }

    public void setBaoBaoList(List<BaoBao> baoBaoList) {
        this.baoBaoList = baoBaoList;
    }
}
