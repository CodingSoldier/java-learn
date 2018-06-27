package com.mybatis.wunao.springcacherelevance.model;

import java.io.Serializable;

public class SpringCacheRelevance implements Serializable {
    private String id;

    private String name;

    private String relevanceText;

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

    public String getRelevanceText() {
        return relevanceText;
    }

    public void setRelevanceText(String relevanceText) {
        this.relevanceText = relevanceText == null ? null : relevanceText.trim();
    }
}