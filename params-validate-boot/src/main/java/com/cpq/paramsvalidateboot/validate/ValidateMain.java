package com.cpq.paramsvalidateboot.validate;

import com.cpq.paramsvalidateboot.validate.bean.AnnoField;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    @Autowired
    private ParamsValidateInterface paramsValidateInterface;

    //校验params
    public ResultCheck checkHandle(AnnoField annoField, Map<String, Object> requestMap) {
        ResultCheck resultCheck = new ResultCheck();

        try {
            getRegexCommon();
        }catch (IOException e){
            resultCheck.setPass(false);
            resultCheck.setMsgSet(new HashSet<String>(){{
                add("初始化regex-common-json.json失败");
            }});
            e.printStackTrace();
        }

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
        validateParam(requestMap, json, msgSet);


        return resultCheck;
    }

    //校验请求参数
    private void validateParam(Map<String, Object> paramMap, Map<String, Object> json, Set<String> msgSet){
        if (json == null || json.size() == 0 || paramMap == null || paramMap.size() == 0){
            return ;
        }

        //循环校验json
        //Set<String> keySet = json.keySet();
        //Object perRule = null;
        for (String key:json.keySet()){
            Map<String, Object> jsonVal = (Map<String, Object>)json.get(key);
            //json.key对应的值是map
            if (jsonVal instanceof  Map){
                Map<String, Object> jsonValSub = (Map<String, Object>)jsonVal.get(key);
                Set<String> subKeySet = jsonValSub.keySet();
                if (subKeySet.size() == 1){
                    //json.key是bean引用名，json.key引用的值是此bean的属性名
                    validateParam((Map<String, Object>)paramMap.get(key),jsonVal, msgSet);
                }else{
                    //json.key是bean中属性的名称，json.key引用的值是校验规则
                    checkValRule(paramMap.get(key), jsonVal, msgSet);
                }
            }else if (jsonVal instanceof List){
                // TODO 暂时不考虑list
                //List listParam = (List)paramMap.get(key);
                //List listRule = (List)paramMap.get(key);
                //if (listParam != null && listParam.size() > 0){
                //    if ()
                //}
            }
        }

    }


    //校验单个值是否符合规则
    private void checkValRule(Object paramVal, Map<String, Object> jsonRule, Set<String> msgSet){
        if (jsonRule == null){
            return;
        }

        if (Util.isRequest(jsonRule)){ //必填
            if (Util.isBlankStrObj(paramVal)){
                msgSet.add(Util.objToStr(jsonRule.get(MESSAGE))); //必填&&无值
            }else {
                checkDetail(paramVal, jsonRule, msgSet); //必填&&有值
            }
        }else{  //非必填
            if (Util.isNotBlankStrObj(paramVal)){
                checkDetail(paramVal, jsonRule, msgSet);  //非必填&&有值
            }else {
                //非必填&&无值，不校验
            }
        }
    }

    //
    private void checkDetail(Object val, Map<String, Object> jsonRule, Set<String> msgSet){
        Object minValue = jsonRule.get(MIN_VALUE);
        Object maxValue = jsonRule.get(MAX_VALUE);
        Object minLength = jsonRule.get(MIN_LENGTH);
        Object maxLength = jsonRule.get(MAX_LENGTH);
        String regex = Util.objToStr(jsonRule.get(REGEX));
        String message = Util.objToStr(jsonRule.get(MESSAGE));
        //if (minValue != null && )
    }

    //获取需要校验的json
    private Map<String, Object> getValidateJson(AnnoField annoField) throws IOException{
        String basePath = paramsValidateInterface.basePath();
        String filePath = Util.trimBeginEndChar(basePath, '/') + "/"
                + Util.trimBeginChar(annoField.getFile(), '/');
        Map<String, Object> json = Util.readFileToMap(filePath);

        if (Util.isNotBlank(annoField.getKeyName())){
            json = (Map<String, Object>)json.get(annoField.getKeyName());
        }
        return json;
    }

    //读取regex-common-json.json文件到regexCommon
    public Map<String, String> getRegexCommon() throws IOException{
        if (regexCommon != null){
            return regexCommon;
        }
        ObjectMapper mapper = new ObjectMapper();
        String basePath = paramsValidateInterface.basePath();
        String filePath = Util.trimBeginEndChar(basePath, '/')
                + "regex-common-json.json";
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(filePath)){
            regexCommon = is != null ? mapper.readValue(is, Map.class) : new HashMap<>();
        }
        return regexCommon;
    }

}
