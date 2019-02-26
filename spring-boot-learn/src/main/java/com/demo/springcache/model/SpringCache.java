package com.demo.springcache.model;

import java.io.Serializable;
import java.util.Date;

public class SpringCache implements Serializable {
    private String id;

    private String name;

    private Integer num;

    private Date date;

    private String tc;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc == null ? null : tc.trim();
    }
}