package com.demo.oauth2.oauthresource.model;

import java.io.Serializable;

public class OauthResource implements Serializable {
    private String id;

    private String name;

    private String urlRegex;

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

    public String getUrlRegex() {
        return urlRegex;
    }

    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex == null ? null : urlRegex.trim();
    }
}