package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * author chenpiqian 2018-05-25
 */
@Component
public class ValidateMain {

    public static final String REGEX_BEGIN = "REGEX_";

    public static final String REQUEST = "request";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String REGEX = "regex";
    public static final String MESSAGE = "message";
    public static final Set<String> ruleKeySet = new HashSet<String>(){{
        add(REQUEST);
        add(MIN_VALUE);
        add(MAX_VALUE);
        add(MIN_LENGTH);
        add(MAX_LENGTH);
        add(REGEX);
        add(MESSAGE);
    }};

    private Set<String> msgSet;  //错误提示信息
    private String ruleKey;  //规则的key

    @Autowired
    private RequestParam requestParam;
    @Autowired
    private RuleFile ruleFile;

    //获取校验注解@ParamsValidate设置的值
    private static ValidateConfig getConfigs(Method method){
        ValidateConfig validateConfig = new ValidateConfig();
        if (method.getAnnotation(ParamsValidate.class) != null){
            String file = method.getAnnotation(ParamsValidate.class).value();
            file = ValidateUtils.isNotBlank(file) ? file : method.getAnnotation(ParamsValidate.class).file();
            String key = method.getAnnotation(ParamsValidate.class).key();
            validateConfig.setFile(file);
            validateConfig.setKey(key);
        }
        return validateConfig;
    }

    //校验结果
    public ResultValidate validateResult(JoinPoint joinPoint) throws Exception{
        ResultValidate resultValidate = new ResultValidate(true);  //默认是校验通过

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();  //获取当前方法
        ValidateConfig validateConfig = getConfigs(method);

        if (ValidateUtils.isNotBlank(validateConfig.getFile())){  //需要校验
            //参数不符合校验规则提示
            msgSet = new TreeSet<>();
            //获取请求参数
            Map<String, Object> allParam = requestParam.mergeParams(joinPoint);
            //获取校验规则
            Map<String, Object> json = ruleFile.ruleFileJsonToMap(validateConfig);
            //执行校验
            validateExecute(json, allParam);
            if (msgSet.size() > 0){
                resultValidate.setPass(false);
                msgSet.remove("");
                resultValidate.setMsgSet(msgSet);
            }
        }
        return resultValidate;
    }

    //校验规则key-value 与 请求参数key-value
    private void validateExecute(Map<String, Object> json, Map<String, Object> paramMap){
        if (json == null || json.size() == 0)
            return ;

        if (paramMap == null){  //参数为空
            checkParamValueNull(json);
            return;
        }

        //循环校验json
        for (String key:json.keySet()){
            Map<String, Object> jsonValue = (Map<String, Object>)json.get(key);
            Object paramValue = paramMap.get(key);
            ruleKey = key;
            if (ruleKeySet.containsAll(jsonValue.keySet())){   //jsonValue为校验规则rules
                checkRuleValue(jsonValue, paramValue);
            }else{
                if (paramValue == null){  //参数为空
                    checkParamValueNull(jsonValue);
                }else if (paramValue instanceof Map){  //paramValue是一个key-value
                    validateExecute(jsonValue, (Map<String, Object>)paramValue);
                }else if (paramValue instanceof List){  //paramValue是一个List
                    List paramList = (List)paramValue;
                    for (Object elem:paramList){
                        if (elem instanceof Map){
                            validateExecute(jsonValue, (Map<String, Object>)elem);
                        }else {
                            throw new ParamsValidateException(String.format("传参或者校验规则错误，校验规则：%s，请求参数：%s", jsonValue, elem));
                        }
                    }
                }else {
                    throw new ParamsValidateException(String.format("传参或者校验规则错误，校验规则：%s，请求参数：%s", jsonValue, paramValue));
                }
            }
        }

    }

    //请求参数是空，校验规则rule有request
    private void checkParamValueNull(Map<String, Object> json){
        Set<String> jsonKeySet = json.keySet();
        if (ruleKeySet.containsAll(jsonKeySet)){
            if (ValidateUtils.isRequest(json)){
                msgSet.add(createFailMsg(json));
            }
        }else {
            for (String key:jsonKeySet){
                if (json.get(key) instanceof Map){
                    checkParamValueNull((Map<String, Object>) json.get(key));
                }
            }
        }
    }

    //rules为校验规则，value为请求值（不包含键）
    public void checkRuleValue(Map<String, Object> rules, Object value){
        if (ValidateUtils.isRequest(rules) && ValidateUtils.isBlankObj(value)){
            //必填&&无值
            msgSet.add(createFailMsg(rules));
        }else if (ValidateUtils.isNotBlankObj(value)){
            //非必填，有值，有校验规则
            if (value instanceof List){  //请求参数：List<基本类型>
                List list = (List)value;
                for (Object elem:list){
                    if (ValidateUtils.isNotBlankObj(elem)){
                        checkRuleValueDetail(rules, elem);
                    }
                }
            }else {
                checkRuleValueDetail(rules, value);  //请求参数：基本类型
            }
        }
    }

    //详细规则校验
    private void checkRuleValueDetail(Map<String, Object> jsonRule, Object val) {
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

            msgSet.add(createFailMsg(jsonRule));
            return;
        }

        //正则校验
        if (ValidateUtils.isNotBlank(regex)){
            if ( regex.startsWith(REGEX_BEGIN)){
                Map<String, String> result = ruleFile.getRegexCommon();  //读取init.json校验规则
                if (result == null || result.size() == 0)
                    throw new ParamsValidateException(String.format("校验异常，init.json未配置，无法获取%s", REGEX_BEGIN));

                regex = result.get(regex);
            }

            if (Pattern.matches(regex, ValidateUtils.objToStr(val)) == false){
                msgSet.add(createFailMsg(jsonRule));
            }
        }
    }

    //处理message为空的情况
    private String createFailMsg(Map<String, Object> jsonRule){
        String message = ValidateUtils.objToStr(jsonRule.get(MESSAGE));
        if (ValidateUtils.isBlank(message)){
            String val = "";
            message = ruleKey + "未通过校验，校验规则：";
            for (String key:jsonRule.keySet()){
                val = ValidateUtils.objToStr(jsonRule.get(key));
                if (ValidateUtils.isNotBlank(val)){
                    val = val.startsWith(REGEX_BEGIN) ? ruleFile.getRegexCommon().get(val) : val;
                    message += key + "：" + val + "; ";
                }
            }
            message = message.substring(0, message.length()-1);
        }
        return message;
    }

}
