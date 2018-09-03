package com.demo.paramsvalidate;

import com.demo.paramsvalidate.bean.ResultValidate;
import com.demo.paramsvalidate.bean.ValidateConfig;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
            String keyName = method.getAnnotation(ParamsValidate.class).keyName();
            validateConfig.setFile(file);
            validateConfig.setKeyName(keyName);
        }
        return validateConfig;
    }

    //校验结果
    public ResultValidate validateResult(JoinPoint joinPoint){
        ResultValidate resultValidate = new ResultValidate(true);  //默认是校验通过

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();  //获取当前方法
        ValidateConfig validateConfig = getConfigs(method);

        if (ValidateUtils.isNotBlank(validateConfig.getFile())){  //需要校验

            //获取请求参数
            Map<String, Object> allParam = null;
            try {
                allParam = requestParam.mergeParams(joinPoint);
            }catch (IOException e){
                //异常，无法处理请求参数，返回pass false
                resultValidate.setPass(false);
                resultValidate.setMsgSet(new HashSet<String>(){{
                    add("@ParamsValidate无法处理请求参数");
                }});
                ValidateUtils.log("@ParamsValidate无法处理请求参数", method, e);
            }

            if (allParam != null){
                //读取校验规则
                Map<String, Object> json = new HashMap<>();
                try {
                    json = ruleFile.ruleFileJsonToMap(validateConfig);
                }catch (Exception e){
                    resultValidate.setPass(false);
                    resultValidate.setMsgSet(new HashSet<String>(){{
                        add("@ParamsValidate读取、解析json文件失败");
                    }});
                    ValidateUtils.log(method, e);
                }

                msgSet = new TreeSet<>();
                //执行校验
                validateExecute(json, allParam);
                if (msgSet.size() > 0){
                    resultValidate.setPass(false);
                    msgSet.remove("");
                    resultValidate.setMsgSet(msgSet);
                }
            }
        }
        return resultValidate;
    }

    //校验请求参数
    private void validateExecute(Map<String, Object> json, Map<String, Object> paramMap){
        if (json == null || json.size() == 0 || paramMap == null || paramMap.size() == 0)
            return ;

        if (ruleKeySet.containsAll(json.keySet())){
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
                }else if(ruleKeySet.containsAll(subKeySet)){  //jsonVal是校验规则Rule
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

    //请求参数是空，校验规则rule有request
    private void checkChildRequest(Object jsonVal){
        if (jsonVal instanceof Map){
            Map<String, Object> jsonRule = (Map<String, Object>)jsonVal;
            Set<String> keySet = jsonRule.keySet();
            if (ruleKeySet.containsAll(keySet) && ValidateUtils.isRequest(jsonRule)){
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
                try {  //读取init.json校验规则
                    Map<String, String> result = ruleFile.getRegexCommon();
                    if (result != null && result.size() != 0){
                        regex = result.get(regex);
                    }else {
                        ValidateUtils.log(new Exception("校验异常，init.json未配置"+REGEX_BEGIN));
                        msgSet.add("校验异常");
                    }
                }catch (IOException e){
                    ValidateUtils.log(e);
                    msgSet.add("初始化init.json失败");
                }
            }

            if (Pattern.matches(regex, ValidateUtils.objToStr(val)) == false){
                msgSet.add(messageReturn(jsonRule));
            }
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
                    val = val.startsWith(REGEX_BEGIN) ? RuleFile.regexCommon.get(val) : val;
                    message += key + "：" + val + "; ";
                }
            }
            message = message.substring(0, message.length()-1);
        }
        return message;
    }

}
