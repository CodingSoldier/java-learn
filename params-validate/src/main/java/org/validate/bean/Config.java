package org.validate.bean;

public class Config {

    private String file;
    private String keyName;

    public Config() {
    }

    public Config(String file, String keyName) {
        this.file = file;
        this.keyName = keyName;
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
