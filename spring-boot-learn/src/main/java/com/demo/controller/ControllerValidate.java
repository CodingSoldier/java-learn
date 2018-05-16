package com.demo.controller;

import com.demo.validate.ParamsValidateInterface;
import com.demo.validate.bean.ResultCheck;
import org.springframework.stereotype.Component;

@Component
public class ControllerValidate implements ParamsValidateInterface {
    @Override
    public String basePath() {
        return "config/validate/";
    }

    @Override
    public Object validateNotPass(ResultCheck resultCheck) {
        return resultCheck;
    }
}
