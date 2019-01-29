package com.demo.old.boy.model;

import java.io.Serializable;

public class Boy implements Serializable {
    private String id;

    private String bigName;

    private Integer loyalty;

    private Float yanVal;

    private String smallName;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getBigName() {
        return bigName;
    }

    public void setBigName(String bigName) {
        this.bigName = bigName == null ? null : bigName.trim();
    }

    public Integer getLoyalty() {
        return loyalty;
    }

    public void setLoyalty(Integer loyalty) {
        this.loyalty = loyalty;
    }

    public Float getYanVal() {
        return yanVal;
    }

    public void setYanVal(Float yanVal) {
        this.yanVal = yanVal;
    }

    public String getSmallName() {
        return smallName;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName == null ? null : smallName.trim();
    }

    @Override
    public String toString() {
        return "Boy{" +
                "id='" + id + '\'' +
                ", bigName='" + bigName + '\'' +
                ", loyalty=" + loyalty +
                ", yanVal=" + yanVal +
                ", smallName='" + smallName + '\'' +
                '}';
    }
}