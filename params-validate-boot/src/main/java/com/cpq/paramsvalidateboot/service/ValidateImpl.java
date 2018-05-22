package com.cpq.paramsvalidateboot.service;


import com.cpq.paramsvalidateboot.validate.ValidateInterface;
import com.cpq.paramsvalidateboot.validate.bean.ResultValidate;
import org.springframework.stereotype.Component;

@Component
public class ValidateImpl implements ValidateInterface {
    @Override
    public String basePath() {
        return "config/validate/json/";
    }

    @Override
    public Object validateNotPass(ResultValidate resultValidate) {
        return resultValidate;
    }
}
