package com.demo.paramsvalidate;


import com.demo.paramsvalidate.bean.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author chenpiqian 2018-05-25
 */
public abstract class ValidateInterfaceAdapter implements ValidateInterface{

    @Override
    public String basePath() {
        return "validate/";
    }

    @Override
    public Parser getParser() {
        return null;
    }

    @Override
    public String getLevel(){
        return PvLevel.STRICT;
    }

    @Override
    public Object validateNotPass(ResultValidate resultValidate){
        List<Map<String, String>> msgList = resultValidate.getMsgList();
        Map<String, String> data = new HashMap<>();  //错误信息集合
        for (Map<String, String> elemMap:msgList){
            if (elemMap != null){
                String message = "";
                for (Map.Entry<String, String> entry:elemMap.entrySet()){
                    String key = entry.getKey();
                    String value = entry.getValue();
                    switch (key){
                        case PvMsg.REQUEST:
                            message = Boolean.TRUE.equals(Boolean.parseBoolean(value)) ? (message+"必填，") : message;
                            break;
                        case PvMsg.MIN_VALUE:
                            message = ValidateUtils.isNotBlankObj(value) ? (message+"最小值："+value+"，") : message;
                            break;
                        case PvMsg.MAX_VALUE:
                            message = ValidateUtils.isNotBlankObj(value) ? (message+"最大值："+value+"，") : message;
                            break;
                        case PvMsg.MIN_LENGTH:
                            message = ValidateUtils.isNotBlankObj(value) ? (message+"最小长度："+value+"，") : message;
                            break;
                        case PvMsg.MAX_LENGTH:
                            message = ValidateUtils.isNotBlankObj(value) ? (message+"最大长度："+value+"，") : message;
                            break;
                        case PvMsg.REGEX:
                            //不返回正则表达式
                            break;
                        case PvMsg.MESSAGE:
                            message = ValidateUtils.isNotBlankObj(value) ? (message+value+"，") : message;
                            break;
                        default:
                            break;
                    }
                }
                message = message.substring(0, message.length()-1);
                data.put(elemMap.get(PvMsg.NAME), message);
            }
        }
        Map<String, Object> r = new HashMap<>();
        r.put("code", resultValidate.isPass() ? 0 : 101);
        r.put("data", data);
        return r;
    }

    @Override
    public Map<String, Object> getCache(ValidateConfig validateConfig) {
        return new HashMap<>();
    }

    @Override
    public void setCache(ValidateConfig validateConfig, Map<String, Object> json) {

    }

}
