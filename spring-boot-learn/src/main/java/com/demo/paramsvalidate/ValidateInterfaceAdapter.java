package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.Parser;
import com.demo.paramsvalidate.bean.ValidateConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * author chenpiqian 2018-05-25
 */
public abstract class ValidateInterfaceAdapter implements ValidateInterface{

    @Override
    public String basePath() {
        return "validate/";
    }

    @Override
    public Parser getParser() {
        return null;
    }

    @Override
    public String getLevel(){
        return PvLevel.LOOSE;
    }

    //必须覆盖此方法
    //public Object validateNotPass(ResultValidate resultValidate)

    @Override
    public Map<String, Object> getCache(ValidateConfig validateConfig) {
        return new HashMap<>();
    }

    @Override
    public void setCache(ValidateConfig validateConfig, Map<String, Object> json) {

    }

}
