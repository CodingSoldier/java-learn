package com.demo.old.beanhibernate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Friend {
    @NotNull
    private Boolean nice;
    @NotNull
    @Pattern(regexp = "^[u4e00-u9fa5a-zA-Z-z0-9]+$", message = "文字")
    private String name;

    public Boolean getNice() {
        return nice;
    }

    public void setNice(Boolean nice) {
        this.nice = nice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
