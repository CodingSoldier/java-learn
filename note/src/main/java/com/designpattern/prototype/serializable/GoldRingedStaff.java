package com.designpattern.prototype.serializable;

import java.io.Serializable;

public class GoldRingedStaff implements Serializable {
    private float height = 100.0f;
    private float diameter = 10.0f;
    public void grow(){
        this.diameter *= 2;
        this.height *= 2;
    }
    public void shrink(){
        this.diameter /= 2;
        this.height /= 2;
    }
}
