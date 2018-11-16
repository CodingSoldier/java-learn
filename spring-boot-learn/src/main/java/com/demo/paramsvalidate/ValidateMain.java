package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.PvMsg;
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
@Component
public class ValidateMain {

    public static final String REGEX_BEGIN = "REGEX_";

    private static Set<String> ruleKeySet = new HashSet<>();
    static {
        ruleKeySet.add(PvMsg.REQUEST);
        ruleKeySet.add(PvMsg.MIN_VALUE);
        ruleKeySet.add(PvMsg.MAX_VALUE);
        ruleKeySet.add(PvMsg.MIN_LENGTH);
        ruleKeySet.add(PvMsg.MAX_LENGTH);
        ruleKeySet.add(PvMsg.REGEX);
        ruleKeySet.add(PvMsg.MESSAGE);
    }

    private ThreadLocal<List<Map<String, String>>> msgThreadLocal = new ThreadLocal<>();  //错误提示信息
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
                resultValidate.setMsgList(msgThreadLocal.get());
            }
        }
        return resultValidate;
    }

    //校验规则与请求参数
    private void validateJsonParam(Map<String, Object> json, Map<String, Object> paramMap){
        if (json == null || json.size() == 0)
            return ;

        if (ValidateUtils.isNullEmptyCollection(paramMap)){  //参数为空
            checkParamValueNull(json);
            return;
        }

        //循环校验json
        for (Map.Entry<String, Object> entry:json.entrySet()){
            if (!(entry.getValue() instanceof Map))  //对象校验有request
                continue;

            Map<String, Object> jsonValue = (Map<String, Object>)entry.getValue();
            String key = entry.getKey();
            Object paramValue = paramMap.get(key);
            ruleKeyThreadLocal.set(key);
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
                        if (!(elem instanceof Map)){
                            throw new ParamsValidateException(String.format("传参或者校验规则错误，校验规则：%s，请求参数：%s", jsonValue, elem));
                        }
                        validateJsonParam(jsonValue, (Map<String, Object>)elem);
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
        Object minValue = jsonRule.get(PvMsg.MIN_VALUE);
        Object maxValue = jsonRule.get(PvMsg.MAX_VALUE);
        Object minLength = jsonRule.get(PvMsg.MIN_LENGTH);
        Object maxLength = jsonRule.get(PvMsg.MAX_LENGTH);
        String regex = ValidateUtils.objToStr(jsonRule.get(PvMsg.REGEX));

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

            if (!Pattern.matches(regex, ValidateUtils.objToStr(val))){
                msgThreadLocal.get().add(createFailMsg(jsonRule));
            }
        }
    }

    //返回错误提示
    private Map<String, String> createFailMsg(Map<String, Object> jsonRule){
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put(PvMsg.NAME, ruleKeyThreadLocal.get());
        ruleKeyThreadLocal.remove();
        for (Map.Entry<String, Object> entry:jsonRule.entrySet()){
            String key = entry.getKey();
            String value = ValidateUtils.objToStr(entry.getValue());
            if (ValidateUtils.isNotBlank(key) && ValidateUtils.isNotBlank(value)){
                msgMap.put(key, value);
            }
        }
        return msgMap;
    }

}
