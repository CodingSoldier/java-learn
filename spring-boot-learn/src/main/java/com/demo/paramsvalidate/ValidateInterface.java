package com.demo.paramsvalidate;


import com.demo.paramsvalidate.bean.ValidateConfig;
import com.demo.paramsvalidate.bean.ResultValidate;

import java.util.Map;

public interface ValidateInterface {
    /**
     * 返回json文件基础路径。init.json文件必须放在此目录下
     * @return json文件基础路径
     */
    String basePath();

    /**
     * 参数校验未通过
     * @param resultValidate
     * @return 返回给客户端的数据
     */
    Object validateNotPass(ResultValidate resultValidate);

    /**
     * 获取缓存中的校验规则
     * @param validateConfig
     * @return  校验规则
     */
    Map<String, Object> getCache(ValidateConfig validateConfig);

    /**
     * 设置校验规则到缓存中
     * @param validateConfig
     * @param json 校验规则
     */
    void setCache(ValidateConfig validateConfig, Map<String, Object> json);

}
