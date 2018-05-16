package com.demo.validate.bean;

public class AnnoField {

    private Boolean isGet;

    //ParamsValidate注解中的成员
    private String file;
    private String keyName;

    public Boolean getGet() {
        return isGet;
    }

    public void setGet(Boolean get) {
        isGet = get;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }
}
