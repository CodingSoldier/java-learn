package com.example.shirojwt.model;

import lombok.Data;

import java.util.List;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
@Data
public class Role {

    private String name;

    private List<Permission> permissionList;

    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    public Role(String name, List<Permission> permissionList) {
        this.name = name;
        this.permissionList = permissionList;
    }
}
