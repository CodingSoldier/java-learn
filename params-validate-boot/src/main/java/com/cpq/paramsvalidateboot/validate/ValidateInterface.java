package com.cpq.paramsvalidateboot.validate;

import com.cpq.paramsvalidateboot.validate.bean.Config;
import com.cpq.paramsvalidateboot.validate.bean.ResultValidate;

import java.util.Map;

public interface ValidateInterface {
    /**
     * 返回json文件基础路径。regex-common-json.json文件必须放在此目录下
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
     * @param config
     * @return  校验规则
     */
    Map<String, Object> getCache(Config config);

    /**
     * 设置校验规则到缓存中
     * @param config
     * @param json 校验规则
     */
    void setCache(Config config, Map<String, Object> json);

}
