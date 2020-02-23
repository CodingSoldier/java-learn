package com.example.bspringboot.h_properties.aware_processor;

import org.springframework.stereotype.Component;

@Component
public class Flag {

    private boolean canOperate = true;

    public boolean isCanOperate() {
        return canOperate;
    }

    public void setCanOperate(boolean canOperate) {
        this.canOperate = canOperate;
    }

    @Override
    public String toString() {
        return "Flag{" +
                "canOperate=" + canOperate +
                '}';
    }
}
