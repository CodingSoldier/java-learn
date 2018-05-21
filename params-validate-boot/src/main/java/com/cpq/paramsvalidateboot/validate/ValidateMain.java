package com.cpq.paramsvalidateboot.validate;

import com.cpq.paramsvalidateboot.validate.bean.AnnotationField;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

@Component
public class ValidateMain {

    private static Map<String, String> regexCommon = null;

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

    public static final String REGEX_COMMON_JSON = "regex-common-json.json";
    public static final String REGEX_BEGIN = "REGEX_";

    @Autowired
    private ParamsValidateInterface paramsValidateInterface;

    //校验params
    public ResultCheck checkHandle(AnnotationField annoField, Map<String, Object> requestMap) {
        ResultCheck resultCheck = new ResultCheck(true);

        //读取@ParamsValidate中的file
        Map<String, Object> json = new HashMap<>();
        try {
            json = getValidateJson(annoField);
        }catch (IOException e){
            resultCheck.setPass(false);
            resultCheck.setMsgSet(new HashSet<String>(){{
                add("@ParamsValidate读取file失败");
            }});
            e.printStackTrace();
        }

        Set<String> msgSet = new TreeSet<>();
        validateParam(json, requestMap, msgSet);
        if (msgSet.size() > 0){
            resultCheck.setPass(false);
            msgSet.remove("");
            resultCheck.setMsgSet(msgSet);
        }

        return resultCheck;
    }

    //校验请求参数
    private void validateParam(Map<String, Object> json, Map<String, Object> paramMap, Set<String> msgSet){
        if (json == null || json.size() == 0 || paramMap == null || paramMap.size() == 0)
            return ;
        //循环校验json
        for (String key:json.keySet()){
            Map<String, Object> jsonVal = (Map<String, Object>)json.get(key);
            Object paramVal = paramMap.get(key);
            Set<String> subKeySet = jsonVal.keySet();
            if(paramVal == null){
                isJsonChildRequest(jsonVal, msgSet);
            }else if(RULE_KEY_SET.containsAll(subKeySet)){  //jsonVal是校验规则Rule
                //paramVal就是前台输入值（基本类型、List<基本类型>），jsonVal是校验规则Map(Rule)
                checkHandleRequest(jsonVal, paramVal, msgSet);
            }else if (paramVal instanceof List){
                //paramVal是List<Bean>
                for (Object elem: (List)paramVal){
                    validateParam(jsonVal, (Map<String, Object>)elem, msgSet);
                }
            }else{
                //paramVal是对象
                validateParam(jsonVal, (Map<String, Object>)paramVal, msgSet);
            }
        }
    }

    //请求参数是空，校验json子级有request
    private void isJsonChildRequest(Object jsonVal, Set<String> msgSet){
        if (jsonVal instanceof Map){
            Map<String, Object> jsonRule = (Map<String, Object>)jsonVal;
            Set<String> keySet = jsonRule.keySet();
            if (RULE_KEY_SET.containsAll(keySet) && Util.isRequest(jsonRule)){
                msgSet.add(Util.objToStr(jsonRule.get(MESSAGE)));
            }else{
                for (String key:keySet){
                    isJsonChildRequest(jsonRule.get(key), msgSet);
                }
            }
        }
    }

    //校验是否必填
    private void checkHandleRequest(Map<String, Object> jsonRule, Object paramVal, Set<String> msgSet){
        if (jsonRule == null)
            return;

        if (Util.isRequest(jsonRule)){
            if (Util.isBlankStrObj(paramVal)){
                msgSet.add(Util.objToStr(jsonRule.get(MESSAGE))); //必填&&无值
            }else {
                HandleParamVal(jsonRule, paramVal, msgSet); //必填&&有值
            }
        }else{
            if (Util.isNotBlankStrObj(paramVal)){
                HandleParamVal(jsonRule, paramVal, msgSet); //非必填&&有值
            }
        }
    }

    //请求参数值可能是List<基本类型>
    private void HandleParamVal(Map<String, Object> jsonRule, Object paramVal, Set<String> msgSet){
        if (paramVal instanceof List){   //前台提交值为List
            for (Object elem:(List)paramVal){
                checkDetail(jsonRule, elem, msgSet);
            }
        }else{  //前台提交值非数组
            checkDetail(jsonRule, paramVal, msgSet);
        }
    }

    //详细规则校验
    private void checkDetail( Map<String, Object> jsonRule, Object val, Set<String> msgSet){
        Object minValue = jsonRule.get(MIN_VALUE);
        Object maxValue = jsonRule.get(MAX_VALUE);
        Object minLength = jsonRule.get(MIN_LENGTH);
        Object maxLength = jsonRule.get(MAX_LENGTH);
        String regex = Util.objToStr(jsonRule.get(REGEX));
        String message = Util.objToStr(jsonRule.get(MESSAGE));

        //校验不通过
        if (Util.isNotBlankObj(minValue) && Util.getDouble(val) < Util.getDouble(minValue)
            || Util.isNotBlankObj(maxValue) && Util.getBigDecimal(val).compareTo(Util.getBigDecimal(maxValue)) >= 1
            ||  Util.isNotBlankObj(minLength) && Util.objToStr(val).length() < Util.getDouble(minLength)
            || Util.isNotBlankObj(maxLength) && Util.objToStr(val).length() > Util.getDouble(maxLength)){

            msgSet.add(message);
            return;
        }

        //正则校验
        if (regex != ""){
            if ( regex.startsWith(REGEX_BEGIN)){
                try {
                    regex = getRegexCommon().get(regex);
                }catch (IOException e){
                    msgSet.add("初始化regex-common-json.json失败");
                    e.printStackTrace();
                }
            }

            if (Pattern.matches(regex, Util.objToStr(val)) == false){
                msgSet.add(message);
                return;
            }
        }
    }

    //获取需要校验的json
    private Map<String, Object> getValidateJson(AnnotationField annoField) throws IOException{
        String basePath = paramsValidateInterface.basePath();
        String filePath = Util.trimBeginEndChar(basePath, '/') + "/"
                + Util.trimBeginChar(annoField.getFile(), '/');
        Map<String, Object> json = Util.readFileToMap(filePath);

        if (Util.isNotBlankObj(annoField.getKeyName())){
            json = (Map<String, Object>)json.get(annoField.getKeyName());
        }
        return json;
    }

    //读取regex-common-json.json文件到regexCommon
    public Map<String, String> getRegexCommon() throws IOException{
        if (regexCommon != null)
            return regexCommon;

        ObjectMapper mapper = new ObjectMapper();
        String basePath = paramsValidateInterface.basePath();
        String filePath = Util.trimBeginEndChar(basePath, '/') + "/"+ REGEX_COMMON_JSON;
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath)){
            regexCommon = is != null ? mapper.readValue(is, Map.class) : new HashMap<>();
        }
        return regexCommon;
    }

}
