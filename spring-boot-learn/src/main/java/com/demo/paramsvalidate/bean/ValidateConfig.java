package com.demo.paramsvalidate.bean;

/**
 * author chenpiqian
 */
public class ValidateConfig {

    private String file;
    private String key;
    private String level;

    public ValidateConfig() {
    }

    public ValidateConfig(String file, String key) {
        this.file = file;
        this.key = key;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
