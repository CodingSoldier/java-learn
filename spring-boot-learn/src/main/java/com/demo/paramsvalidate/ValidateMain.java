package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * author chenpiqian 2018-05-25
 */
@Component("prototype")
public class ValidateMain {

    public static final String REGEX_BEGIN = "REGEX_";

    public static final String REQUEST = "request";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";
    public static final String MIN_LENGTH = "minLength";
    public static final String MAX_LENGTH = "maxLength";
    public static final String REGEX = "regex";
    public static final String MESSAGE = "message";
    private static Set<String> ruleKeySet = new HashSet<>();
    static {
        ruleKeySet.add(REQUEST);
        ruleKeySet.add(MIN_VALUE);
        ruleKeySet.add(MAX_VALUE);
        ruleKeySet.add(MIN_LENGTH);
        ruleKeySet.add(MAX_LENGTH);
        ruleKeySet.add(REGEX);
        ruleKeySet.add(MESSAGE);
    }

    private ThreadLocal<List<String>> msgThreadLocal = new ThreadLocal<>();  //错误提示信息
    private ThreadLocal<String> ruleKeyThreadLocal = new ThreadLocal<>();  //规则的key

    @Autowired
    private RequestParam requestParam;
    @Autowired
    private RuleFile ruleFile;



    //校验
    public ResultValidate validateExecute(JoinPoint joinPoint, ValidateConfig validateConfig) throws Exception{
        ResultValidate resultValidate = new ResultValidate(true);  //默认是校验通过
        if (ValidateUtils.isNotBlank(validateConfig.getFile())){  //需要校验
            //初始化list
            msgThreadLocal.set(new ArrayList<>());
            //获取请求参数
            Map<String, Object> allParam = requestParam.mergeParams(joinPoint);
            //获取校验规则
            Map<String, Object> json = ruleFile.ruleFileJsonToMap(validateConfig, allParam.keySet());
            //执行校验
            validateJsonParam(json, allParam);
            if (msgThreadLocal.get().size() > 0){
                resultValidate.setPass(false);
                msgThreadLocal.get().remove("");
                resultValidate.setMsgList(msgThreadLocal.get());
            }
        }
        return resultValidate;
    }

    //校验规则与请求参数
    private void validateJsonParam(Map<String, Object> json, Map<String, Object> paramMap){
        if (json == null || json.size() == 0)
            return ;

        //if (ruleKeySet.containsAll(json.keySet())){   //只校验一个参数，并且指定key的情况
        //    if (ValidateUtils.isNullEmptyCollection(paramMap)){  //参数为空
        //        checkParamValueNull(json);
        //    }else {
        //        for (Map.Entry entry:paramMap.entrySet()){
        //            checkRuleValue(json, entry.getValue());
        //            break;
        //        }
        //    }
        //    return;
        //}

        if (ValidateUtils.isNullEmptyCollection(paramMap)){  //参数为空
            checkParamValueNull(json);
            return;
        }

        //循环校验json
        for (Map.Entry<String, Object> entry:json.entrySet()){
            Object jsonVal = entry.getValue();
            if (!(jsonVal instanceof Map))  //对象校验有request
                continue;

            Map<String, Object> jsonValue = (Map<String, Object>)jsonVal;
            Object paramValue = paramMap.get(entry.getKey());
            ruleKeyThreadLocal.set(entry.getKey());
            if (ruleKeySet.containsAll(jsonValue.keySet())){   //jsonValue为校验规则rules
                checkRuleValue(jsonValue, paramValue);
            }else{
                if (ValidateUtils.isNullEmptyCollection(paramValue)){  //参数为空
                    checkParamValueNull(jsonValue);
                }else if (paramValue instanceof Map){  //paramValue是一个key-value
                    validateJsonParam(jsonValue, (Map<String, Object>)paramValue);
                }else if (paramValue instanceof List){  //paramValue是一个List
                    List paramList = (List)paramValue;
                    for (Object elem:paramList){
                        if (elem instanceof Map){
                            validateJsonParam(jsonValue, (Map<String, Object>)elem);
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
            if (ValidateUtils.isRequestTrue(json)){
                msgThreadLocal.get().add(createFailMsg(json));
            }
        }else if (!ValidateUtils.isRequestFalse(json)){  //对象校验，对象的校验规则未填写request:false，继续校验
            for (String key:jsonKeySet){
                if (json.get(key) instanceof Map){
                    ruleKeyThreadLocal.set(key);
                    checkParamValueNull((Map<String, Object>) json.get(key));
                }
            }
        }
    }

    //rules为校验规则，value为请求值（不包含键）
    private void checkRuleValue(Map<String, Object> rules, Object value){
        if (ValidateUtils.isRequestTrue(rules) && ValidateUtils.isBlankObj(value)){
            //必填&&无值
            msgThreadLocal.get().add(createFailMsg(rules));
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

            msgThreadLocal.get().add(createFailMsg(jsonRule));
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
                msgThreadLocal.get().add(createFailMsg(jsonRule));
            }
        }
    }

    //处理message为空的情况
    private String createFailMsg(Map<String, Object> jsonRule){
        String message = ValidateUtils.objToStr(jsonRule.get(MESSAGE));
        if (ValidateUtils.isBlank(message)){
            String val = "";
            message = ValidateUtils.objToStr(ruleKeyThreadLocal.get()) + "未通过校验，校验规则：";
            for (Map.Entry<String, Object> entry:jsonRule.entrySet()){
                val = ValidateUtils.objToStr(entry.getValue());
                if (ValidateUtils.isNotBlank(val)){
                    val = val.startsWith(REGEX_BEGIN) ? ruleFile.getRegexCommon().get(val) : val;
                    message += entry.getKey() + "：" + val + "; ";
                }
            }
            message = message.substring(0, message.length()-1);
        }
        return message;
    }

}
