package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.Parser;
import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;

import java.util.Map;

/**
 * author chenpiqian 2018-05-25
 */
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
     * json解析器
     * 1、使用默认解析器jackson，直接返回null即可。
     * 2、使用gson，请返回 new Parser(Gson.class)。
     * 3、使用fastjson，请返回new Parser(JSON.class, Feature[].class)。
     * 为了支持fastjson，搞得好坑爹。
     */
    Parser getParser();

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
