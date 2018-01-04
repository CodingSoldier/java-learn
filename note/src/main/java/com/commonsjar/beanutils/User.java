package com.commonsjar.beanutils;

import java.util.Date;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2017/12/10
 */
public class User {
    private String name;
    private String id;
    private Date birthday;

    public User() {
    }

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
