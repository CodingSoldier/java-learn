package com.example.bspringboot.h_properties.aware_processor;

import org.springframework.beans.factory.Aware;

public interface MyAware extends Aware {
    void setFlag(Flag flag);
}
