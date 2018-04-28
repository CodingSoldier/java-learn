package com.t;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class GF {


    /**
     * id : 0
     * title_hello : aaa
     * time : 2018-01-01
     * success : true
     * children : [{"id":9,"title_hello":"QA测试12","time":"2018-01-01","success":false,"children":null},{"id":21,"title_hello":"鱿鱼","success":false,"time":"2018-01-01","children":null}]
     */

    @JSONField(name = "id")
    private int id;
    @JSONField(name = "title_hello")
    private String titleHello;
    @JSONField(name = "time")
    private String time;
    @JSONField(name = "success")
    private boolean success;
    @JSONField(name = "children")
    private List<ChildrenBean> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitleHello() {
        return titleHello;
    }

    public void setTitleHello(String titleHello) {
        this.titleHello = titleHello;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }
}
