package com.demo.validate;

import com.alibaba.fastjson.JSONObject;
import com.demo.validate.bean.AnnoField;
import com.demo.validate.bean.ResultCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ValidateMain {

    private static String basePath = "";

    @Autowired
    private ParamsValidateInterface paramsValidateInterface;

    //校验params
    public ResultCheck checkHandle(AnnoField annoField, Map<String, Object> requestMap, Object bodyObj) {

        if (isMerge(requestMap, bodyObj)){
            //已经合并到requestMap
        }else{

        }

        return new ResultCheck();
    }

    //是否合并
    private boolean isMerge(Map<String, Object> requestMap, Object bodyObj){
        boolean isMerge = false;
        if (bodyObj != null && bodyObj instanceof Map){
            Map<String, Object> temp = (Map<String, Object>)bodyObj;
            for (String key: temp.keySet()){
                requestMap.put(key, temp.get(key));
            }
            isMerge = true;
        }
        return isMerge;
    }

    //获取需要校验的json
    private JSONObject getValidateJson(AnnoField annoField){
        String keyName = annoField.getKeyName();
        String filePath = Util.makeFilePath(basePath, annoField.getFile());
        JSONObject validateJson = Util.readFileToJSONObject(filePath);
        if (Util.isNotBlank(annoField.getKeyName())){
            validateJson = validateJson.getObject(annoField.getKeyName(), JSONObject.class);
        }
        return validateJson;
    }


}
