package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.Parser;
import com.demo.paramsvalidate.bean.ValidateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class RuleFile {

    public static volatile Map<String, String> regexCommon;

    public static final String REGEX_COMMON_JSON = "init.json";

    @Autowired
    private ValidateInterface validateInterface;

    //获取需要校验的json
    public Map<String, Object> ruleFileJsonToMap(ValidateConfig validateConfig) throws Exception{
        String basePath = validateInterface.basePath();
        String filePath = ValidateUtils.trimBeginEndChar(basePath, '/') + "/"
            + ValidateUtils.trimBeginChar(validateConfig.getFile(), '/');

        Map<String, Object> json = validateInterface.getCache(validateConfig);
        if (json == null || json.size() == 0){
            json = ruleFileRead(filePath);
            if (json == null)
                throw new Exception("@ParamsValidate元素value、file错误");

            if (ValidateUtils.isNotBlank(validateConfig.getKeyName())){
                json = (Map<String, Object>)json.get(validateConfig.getKeyName());
                if (json != null){
                    validateInterface.setCache(validateConfig, json);
                }else{
                    throw new Exception("@ParamsValidate元素keyName错误");
                }
            }
        }
        return json;
    }

    //读取json文件到Map<String, Object>
    private Map<String, Object> ruleFileRead(String filePath) throws Exception{
        Map<String, Object> json = new HashMap<>();
        Parser parser = validateInterface.getParser();
        try (InputStream is = ValidateUtils.class.getClassLoader().getResourceAsStream(filePath)){
            if (is != null){
                if (parser != null && parser.getParserClass() != null){
                    Class parserClazz = parser.getParserClass();
                    Class featureArrClass = parser.getFeatureArrClass();
                    if ("com.google.gson.Gson".equals(parserClazz.getName())){
                        //使用gson解析
                        Object gson = parserClazz.newInstance();
                        Method method = parserClazz.getMethod("fromJson", Reader.class, Class.class);
                        json = (Map<String, Object>)method
                            .invoke(gson, new InputStreamReader(new BufferedInputStream(is)), Map.class );
                    }else if ("com.alibaba.fastjson.JSON".equals(parserClazz.getName())
                        && featureArrClass != null
                        && "Feature[]".equals(featureArrClass.getSimpleName())){
                        //使用fastjson解析
                        Method method = parserClazz.getMethod("parseObject", InputStream.class, Type.class, featureArrClass);
                        json = (Map<String, Object>)method.invoke(null, is, Map.class, null);
                    }else {
                        throw new Exception("json解析器不符合规范");
                    }
                }else{
                    //使用Jackson解析
                    ObjectMapper mapper = new ObjectMapper();
                    json = mapper.readValue(is, Map.class);
                }
            }else{
                throw new IOException("@ParamsValidate读取file失败");
            }
        }
        return json;
    }

    //读取init.json文件到regexCommon
    public Map<String, String> getRegexCommon() throws IOException{
        if (regexCommon != null)
            return regexCommon;

        synchronized (this){
            ObjectMapper mapper = new ObjectMapper();
            String basePath = validateInterface.basePath();
            String filePath = ValidateUtils.trimBeginEndChar(basePath, '/') + "/"+ REGEX_COMMON_JSON;
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath)){
                regexCommon = is == null ? null : mapper.readValue(is, Map.class);
            }
        }

        return regexCommon;
    }

}
