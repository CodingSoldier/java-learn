package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.Parser;
import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

/**
 * author chenpiqian 2018-05-25
 */
@Component
public class ValidateMain {

    private static volatile Map<String, String> regexCommon;

    public static final String REQUEST = "request";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String REGEX = "regex";
    public static final String MESSAGE = "message";
    public static final Set<String> RULE_KEY_SET = new HashSet<String>(){{
        add(REQUEST);
        add(MIN_VALUE);
        add(MAX_VALUE);
        add(MIN_LENGTH);
        add(MAX_LENGTH);
        add(REGEX);
        add(MESSAGE);
    }};

    public static final String REGEX_COMMON_JSON = "init.json";
    public static final String REGEX_BEGIN = "REGEX_";

    private Set<String> msgSet;  //错误提示信息
    private String ruleKey;  //规则的key

    @Autowired
    private ValidateInterface validateInterface;

    //校验params
    public ResultValidate validateEntry(Method method, ValidateConfig validateConfig, Map<String, Object> requestMap) {
        ResultValidate resultValidate = new ResultValidate(true);

        //读取@ParamsValidate中的file
        Map<String, Object> json = new HashMap<>();
        try {
            json = ruleFileJsonToMap(validateConfig);
        }catch (Exception e){
            resultValidate.setPass(false);
            resultValidate.setMsgSet(new HashSet<String>(){{
                add("@ParamsValidate读取、解析json文件失败");
            }});
            ValidateUtils.log(method, e);
        }

        msgSet = new TreeSet<>();
        validateExecute(json, requestMap);
        if (msgSet.size() > 0){
            resultValidate.setPass(false);
            msgSet.remove("");
            resultValidate.setMsgSet(msgSet);
        }

        return resultValidate;
    }

    //校验请求参数
    private void validateExecute(Map<String, Object> json, Map<String, Object> paramMap){
        if (json == null || json.size() == 0 || paramMap == null || paramMap.size() == 0)
            return ;

        if (RULE_KEY_SET.containsAll(json.keySet())){
            //paramMap只有一个key-value，json就是校验规则集合
            for (Object paramVal:paramMap.values()){
                checkRequest(json, paramVal);
                break;
            }
        }else{
            //循环校验json
            for (String key:json.keySet()){
                Map<String, Object> jsonVal = (Map<String, Object>)json.get(key);
                Object paramVal = paramMap.get(key);
                ruleKey = key;
                Set<String> subKeySet = jsonVal.keySet();
                if(paramVal == null){
                    checkChildRequest(jsonVal);
                }else if(RULE_KEY_SET.containsAll(subKeySet)){  //jsonVal是校验规则Rule
                    //paramVal就是前台输入值（基本类型、List<基本类型>），jsonVal是校验规则Map(Rule)
                    checkRequest(jsonVal, paramVal);
                }else if (paramVal instanceof List){
                    //paramVal是List<Bean>
                    for (Object elem: (List)paramVal){
                        validateExecute(jsonVal, (Map<String, Object>)elem);
                    }
                }else{
                    //paramVal是对象
                    validateExecute(jsonVal, (Map<String, Object>)paramVal);
                }
            }
        }
    }

    //请求参数是空，校验json子级有request
    private void checkChildRequest(Object jsonVal){
        if (jsonVal instanceof Map){
            Map<String, Object> jsonRule = (Map<String, Object>)jsonVal;
            Set<String> keySet = jsonRule.keySet();
            if (RULE_KEY_SET.containsAll(keySet) && ValidateUtils.isRequest(jsonRule)){
                msgSet.add(messageReturn(jsonRule));
            }else{
                for (String key:keySet){
                    checkChildRequest(jsonRule.get(key));
                }
            }
        }
    }

    //校验是否必填
    private void checkRequest(Map<String, Object> jsonRule, Object paramVal){
        if (jsonRule == null)
            return;

        if (ValidateUtils.isRequest(jsonRule)){
            if (ValidateUtils.isBlankObj(paramVal)){
                msgSet.add(messageReturn(jsonRule)); //必填&&无值
            }else {
                paramValToElem(jsonRule, paramVal); //必填&&有值
            }
        }else{
            if (ValidateUtils.isNotBlankObj(paramVal)){
                paramValToElem(jsonRule, paramVal); //非必填&&有值
            }
        }
    }

    //详细规则校验
    private void checkDetail( Map<String, Object> jsonRule, Object val){
        Object minValue = jsonRule.get(MIN_VALUE);
        Object maxValue = jsonRule.get(MAX_VALUE);
        Object minLength = jsonRule.get(MIN_LENGTH);
        Object maxLength = jsonRule.get(MAX_LENGTH);
        String regex = ValidateUtils.objToStr(jsonRule.get(REGEX));

        //校验不通过
        if (ValidateUtils.isNotBlankObj(minValue) && ValidateUtils.getDouble(val) < ValidateUtils.getDouble(minValue)
            || ValidateUtils.isNotBlankObj(maxValue) && ValidateUtils.getBigDecimal(val).compareTo(ValidateUtils.getBigDecimal(maxValue)) >= 1
            ||  ValidateUtils.isNotBlankObj(minLength) && ValidateUtils.objToStr(val).length() < ValidateUtils.getDouble(minLength)
            || ValidateUtils.isNotBlankObj(maxLength) && ValidateUtils.objToStr(val).length() > ValidateUtils.getDouble(maxLength)){

            msgSet.add(messageReturn(jsonRule));
            return;
        }

        //正则校验
        if (ValidateUtils.isNotBlank(regex)){
            if ( regex.startsWith(REGEX_BEGIN)){
                Map<String, String> result = getRegexCommon();
                if (result != null && result.size() != 0){
                    regex = result.get(regex);
                }else {
                    msgSet.add("初始化init.json失败");
                }
            }

            if (Pattern.matches(regex, ValidateUtils.objToStr(val)) == false){
                msgSet.add(messageReturn(jsonRule));
            }
        }
    }

    //请求参数值可能是List<基本类型>
    private void paramValToElem(Map<String, Object> jsonRule, Object paramVal){
        if (paramVal instanceof List){   //前台提交值为List
            for (Object elem:(List)paramVal){
                checkDetail(jsonRule, elem);
            }
        }else{  //前台提交值非数组
            checkDetail(jsonRule, paramVal);
        }
    }

    //处理message为空的情况
    private String messageReturn(Map<String, Object> jsonRule){
        String message = ValidateUtils.objToStr(jsonRule.get(MESSAGE));
        if (ValidateUtils.isBlank(message)){
            String val = "";
            message = ruleKey + "未通过校验，校验规则：";
            for (String key:jsonRule.keySet()){
                val = ValidateUtils.objToStr(jsonRule.get(key));
                if (ValidateUtils.isNotBlank(val)){
                    val = val.startsWith(REGEX_BEGIN) ? regexCommon.get(val) : val;
                    message += key + "：" + val + "; ";
                }
            }
            message = message.substring(0, message.length()-1);
        }
        return message;
    }

    //获取需要校验的json
    private Map<String, Object> ruleFileJsonToMap(ValidateConfig validateConfig) throws Exception{
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
                        throw new Exception("ValidateInterface#getParser()设置的解析器不符合规范");
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
    private Map<String, String> getRegexCommon(){
        if (regexCommon != null)
            return regexCommon;

        synchronized (this){
            ObjectMapper mapper = new ObjectMapper();
            String basePath = validateInterface.basePath();
            String filePath = ValidateUtils.trimBeginEndChar(basePath, '/') + "/"+ REGEX_COMMON_JSON;
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath)){
                regexCommon = is == null ? null : mapper.readValue(is, Map.class);
            }catch (IOException e){
                msgSet.add("初始化init.json失败");
                ValidateUtils.log(e);
            }
        }

        return regexCommon;
    }
}
