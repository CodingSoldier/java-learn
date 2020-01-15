package com.example.shirojwt.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
@Data
public class User {
    private String username;
    private String password;

    private List<Role> roleList;

    public User() {
    }

    public User(String username, String password, List<Role> roleList) {
        this.username = username;
        this.password = password;
        this.roleList = roleList;
    }
}
