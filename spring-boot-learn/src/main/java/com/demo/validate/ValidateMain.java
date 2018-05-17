package com.demo.validate;

import com.demo.validate.bean.AnnoField;
import com.demo.validate.bean.PerCheck;
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
        Map<String, Object> json = getValidateJson(annoField);
        //requestMap会被修改
        boolean merge = executeMerge(requestMap, bodyObj);
        if (merge == false){
            validateBodyObj(bodyObj, json);
        }
        validateMap(requestMap, json);

        return new ResultCheck();
    }

    //是否合并
    private boolean executeMerge(Map<String, Object> requestMap, Object bodyObj){
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
    private Map<String, Object> getValidateJson(AnnoField annoField){
        String keyName = annoField.getKeyName();
        String basePath = paramsValidateInterface.basePath();
        String filePath = Util.makeFilePath(basePath, annoField.getFile());
        Map<String, Object> json = Util.readFileToMap(filePath);
        if (Util.isNotBlank(annoField.getKeyName())){
            json = (Map<String, Object>)json.get(annoField.getKeyName());
        }
        return json;
    }

    private PerCheck validateMap(Map<String, Object> requestMap, Map<String, Object> json){
        if (json != null){
            for (String key:json.keySet()){
                // TODO 校验
            }
        }
        return new PerCheck();
    }

    private PerCheck validateBodyObj(Object bodyObj, Map<String, Object> json){

        return new PerCheck();
    }


}
