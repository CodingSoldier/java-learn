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
 * author chenpiqian
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
    public static final String JSON_KEY = "jsonKey";


    private Set<String> msgSet;  //错误提示信息
    private String ruleKey;  //规则的key

    @Autowired
    private ValidateInterface validateInterface;

    //校验params
    public ResultValidate validateHandle(ValidateConfig validateConfig, Map<String, Object> requestMap) {
        ResultValidate resultValidate = new ResultValidate(true);

        //读取@ParamsValidate中的file
        Map<String, Object> json = new HashMap<>();
        try {
            json = getValidateJson(validateConfig);
        }catch (Exception e){
            resultValidate.setPass(false);
            resultValidate.setMsgSet(new HashSet<String>(){{
                add("@ParamsValidate读取、解析json文件失败");
            }});
            e.printStackTrace();
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
        //循环校验json
        for (String key:json.keySet()){
            ruleKey = key;
            Map<String, Object> jsonVal = (Map<String, Object>)json.get(key);
            Object paramVal = paramMap.get(key);
            Set<String> subKeySet = jsonVal.keySet();
            if(paramVal == null){
                isJsonChildRequest(jsonVal);
            }else if(RULE_KEY_SET.containsAll(subKeySet)){  //jsonVal是校验规则Rule
                //paramVal就是前台输入值（基本类型、List<基本类型>），jsonVal是校验规则Map(Rule)
                checkParamRequest(jsonVal, paramVal);
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

    //请求参数是空，校验json子级有request
    private void isJsonChildRequest(Object jsonVal){
        if (jsonVal instanceof Map){
            Map<String, Object> jsonRule = (Map<String, Object>)jsonVal;
            Set<String> keySet = jsonRule.keySet();
            if (RULE_KEY_SET.containsAll(keySet) && Utils.isRequest(jsonRule)){
                msgSet.add(dealWithMessage(jsonRule));
            }else{
                for (String key:keySet){
                    isJsonChildRequest(jsonRule.get(key));
                }
            }
        }
    }

    //校验是否必填
    private void checkParamRequest(Map<String, Object> jsonRule, Object paramVal){
        if (jsonRule == null)
            return;

        if (Utils.isRequest(jsonRule)){
            if (Utils.isBlankObj(paramVal)){
                msgSet.add(dealWithMessage(jsonRule)); //必填&&无值
            }else {
                manageParamVal(jsonRule, paramVal); //必填&&有值
            }
        }else{
            if (Utils.isNotBlankObj(paramVal)){
                manageParamVal(jsonRule, paramVal); //非必填&&有值
            }
        }
    }

    //请求参数值可能是List<基本类型>
    private void manageParamVal(Map<String, Object> jsonRule, Object paramVal){
        if (paramVal instanceof List){   //前台提交值为List
            for (Object elem:(List)paramVal){
                checkDetail(jsonRule, elem);
            }
        }else{  //前台提交值非数组
            checkDetail(jsonRule, paramVal);
        }
    }

    //详细规则校验
    private void checkDetail( Map<String, Object> jsonRule, Object val){
        Object minValue = jsonRule.get(MIN_VALUE);
        Object maxValue = jsonRule.get(MAX_VALUE);
        Object minLength = jsonRule.get(MIN_LENGTH);
        Object maxLength = jsonRule.get(MAX_LENGTH);
        String regex = Utils.objToStr(jsonRule.get(REGEX));

        //校验不通过
        if (Utils.isNotBlankObj(minValue) && Utils.getDouble(val) < Utils.getDouble(minValue)
            || Utils.isNotBlankObj(maxValue) && Utils.getBigDecimal(val).compareTo(Utils.getBigDecimal(maxValue)) >= 1
            ||  Utils.isNotBlankObj(minLength) && Utils.objToStr(val).length() < Utils.getDouble(minLength)
            || Utils.isNotBlankObj(maxLength) && Utils.objToStr(val).length() > Utils.getDouble(maxLength)){

            msgSet.add(dealWithMessage(jsonRule));
            return;
        }

        //正则校验
        if (regex != ""){
            if ( regex.startsWith(REGEX_BEGIN)){
                try {
                    regex = getRegexCommon().get(regex);
                }catch (IOException e){
                    msgSet.add("初始化init.json失败");
                    e.printStackTrace();
                }
            }

            if (Pattern.matches(regex, Utils.objToStr(val)) == false){
                msgSet.add(dealWithMessage(jsonRule));
                return;
            }
        }
    }

    //处理message为空的情况
    private String dealWithMessage(Map<String, Object> jsonRule){
        String message = Utils.objToStr(jsonRule.get(MESSAGE));
        if (Utils.isBlank(message)){
            String val = "";
            message = ruleKey + "未通过校验，校验规则：";
            for (String key:jsonRule.keySet()){
                val = Utils.objToStr(jsonRule.get(key));
                if (Utils.isNotBlank(val)){
                    val = val.startsWith(REGEX_BEGIN) ? regexCommon.get(val) : val;
                    message += key + "：" + val + "; ";
                }
            }
        }
        return message.substring(0, message.length()-1);
    }

    //读取json文件到Map<String, Object>
    private Map<String, Object> readFileToMap(String filePath) throws Exception{
        Map<String, Object> json = new HashMap<String, Object>();
        if (Utils.isEmpty(filePath)){
            return json;
        }
        Parser parser = validateInterface.getParser();
        try (InputStream is = Utils.class.getClassLoader().getResourceAsStream(filePath)){
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

    //获取需要校验的json
    private Map<String, Object> getValidateJson(ValidateConfig validateConfig) throws Exception{
        String basePath = validateInterface.basePath();
        String filePath = Utils.trimBeginEndChar(basePath, '/') + "/"
                + Utils.trimBeginChar(validateConfig.getFile(), '/');

        Map<String, Object> json = validateInterface.getCache(validateConfig);
        if (json == null || json.size() == 0){
            json = readFileToMap(filePath);
            if (Utils.isNotBlank(validateConfig.getKeyName())){
                json = (Map<String, Object>)json.get(validateConfig.getKeyName());
            }else{
                Iterator<Map.Entry<String, Object>> it = json.entrySet().iterator();
                Map.Entry<String, Object> entry = null;
                while (it.hasNext()){
                    entry = it.next();
                    if (entry.getKey().startsWith(JSON_KEY)){
                        it.remove();
                    }
                }
            }
            validateInterface.setCache(validateConfig, json);
        }
        return json;
    }

    //读取init.json文件到regexCommon
    private Map<String, String> getRegexCommon() throws IOException{
        if (regexCommon != null)
            return regexCommon;

        synchronized (this){
            ObjectMapper mapper = new ObjectMapper();
            String basePath = validateInterface.basePath();
            String filePath = Utils.trimBeginEndChar(basePath, '/') + "/"+ REGEX_COMMON_JSON;
            try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath)){
                regexCommon = is != null ? mapper.readValue(is, Map.class) : new HashMap<>();
            }
        }

        return regexCommon;
    }

}
