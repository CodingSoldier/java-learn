package com.cpq.paramsvalidateboot.validate;

import com.cpq.paramsvalidateboot.validate.bean.AnnoField;
import com.cpq.paramsvalidateboot.validate.bean.PerCheck;
import com.cpq.paramsvalidateboot.validate.bean.ResultCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        String filePath = makeFilePath(basePath, annoField.getFile());
        Map<String, Object> json = Util.readFileToMap(filePath);
        if (Util.isNotBlank(annoField.getKeyName())){
            json = (Map<String, Object>)json.get(annoField.getKeyName());
        }
        return json;
    }

    //返回文件路径
    private static String makeFilePath(String basePath, String file) {
        basePath = Util.trimBeginEndChar(basePath, '/') + "/";
        file = Util.trimBeginChar(file, '/');
        return basePath + file;
    }

    private PerCheck validateMap(Map<String, Object> requestMap, Map<String, Object> json){
        if (json != null){
            for (String key:json.keySet()){

            }
        }
        return new PerCheck();
    }

    private Set<String> validateBodyObj(Object bodyObj, Map<String, Object> json){
        Set<String> errorMsgSet = new HashSet<>();

        return errorMsgSet;
    }

    private void checkVal(Object val, Map<String, Object> json, Set<String> msgSet){
        if (val == null || json == null || msgSet == null){
            return;
        }

        //if ()
    }


}
