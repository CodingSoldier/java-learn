package com.demo.oauth2.oauthrole.model;

import java.io.Serializable;

public class OauthRole implements Serializable {
    private String id;

    private String name;

    private String resourceList;

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

    public String getResourceList() {
        return resourceList;
    }

    public void setResourceList(String resourceList) {
        this.resourceList = resourceList == null ? null : resourceList.trim();
    }
}