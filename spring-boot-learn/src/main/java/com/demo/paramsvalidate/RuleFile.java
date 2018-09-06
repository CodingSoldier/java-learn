package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.Parser;
import com.demo.paramsvalidate.bean.ValidateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;

@Component
public class RuleFile {

    private static volatile Map<String, String> regexCommon;

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
            if (json == null || json.size() == 0)
                throw new ParamsValidateException(String.format("读取%s,结果是null或者空json", filePath));

            String key = validateConfig.getKey();
            if (ValidateUtils.isNotBlank(key)){
                json = (Map<String, Object>)json.get(key);
                if (json != null){
                    validateInterface.setCache(validateConfig, json);
                }else{
                    throw new ParamsValidateException(String.format("%s文件中无key: %s", filePath, key));
                }
            }
        }
        return json;
    }

    //读取json文件到Map<String, Object>
    private Map<String, Object> ruleFileRead(String filePath) throws Exception{
        Map<String, Object> json = null;
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
                        throw new ParamsValidateException("json解析器不符合规范，请修改getParser()");
                    }
                }else{
                    //使用Jackson解析
                    ObjectMapper mapper = new ObjectMapper();
                    json = mapper.readValue(is, Map.class);
                }
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
