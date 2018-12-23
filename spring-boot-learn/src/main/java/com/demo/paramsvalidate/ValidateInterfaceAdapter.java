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
                Boolean requestVal = Boolean.parseBoolean(elemMap.get(PvMsg.REQUEST));
                String minVal = elemMap.get(PvMsg.MIN_VALUE);
                String maxVal = elemMap.get(PvMsg.MAX_VALUE);
                String minLen = elemMap.get(PvMsg.MIN_LENGTH);
                String maxLen = elemMap.get(PvMsg.MAX_LENGTH);
                String jsonMsg = elemMap.get(PvMsg.MESSAGE);

                String message = "";
                message = ValidateUtils.isNotBlankObj(jsonMsg) ? (message+jsonMsg+"，") : message;
                message = Boolean.TRUE.equals(requestVal) ? (message+"必填，") : message;
                message = ValidateUtils.isNotBlankObj(minVal) ? (message+"最小值"+minVal+"，") : message;
                message = ValidateUtils.isNotBlankObj(maxVal) ? (message+"最大值"+maxVal+"，") : message;
                message = ValidateUtils.isNotBlankObj(minLen) ? (message+"最小长度"+minLen+"，") : message;
                message = ValidateUtils.isNotBlankObj(maxLen) ? (message+"最大长度"+maxLen+"，") : message;
                message = "".equals(message) ? "未通过校验，" : message;
                message = message.substring(0, message.length()-1);

                String name = elemMap.get(PvMsg.NAME);
                data.put(name, message);
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
