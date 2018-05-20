package com.cpq.paramsvalidateboot.service;


import com.cpq.paramsvalidateboot.validate.ParamsValidateInterface;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import org.springframework.stereotype.Component;

@Component
public class ValidateImpl implements ParamsValidateInterface {
    @Override
    public String basePath() {
        return "config/validate/json";
    }

    @Override
    public Object validateNotPass(ResultCheck resultCheck) {
        return resultCheck;
    }
}
