package com.demo.oauth2.oauthuser.model;

import java.io.Serializable;

public class OauthUser implements Serializable {
    private String id;

    private String password;

    private String roleList;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRoleList() {
        return roleList;
    }

    public void setRoleList(String roleList) {
        this.roleList = roleList == null ? null : roleList.trim();
    }
}