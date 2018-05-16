package com.demo.validate;

import com.demo.validate.bean.AnnoField;
import com.demo.validate.bean.PerCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ValidateMain {

    private static String basePath = "";

    @Autowired
    private ParamsValidateInterface paramsValidateInterface;

    public PerCheck checkParam(Object params, AnnoField paramBean) {
        PerCheck perCheck = new PerCheck(true, "");

        return perCheck;
    }

    public PerCheck checkRequest(HttpServletRequest request, AnnoField paramBean) {
        PerCheck perCheck = new PerCheck();

        return perCheck;
    }

    //读取注解中设置的json文件
    //private JSONObject getJSONObjectByKey(AnnoField annoField){
    //    String keyName = annoField.getKeyName();
    //    String filePath = Util.makeFilePath(basePath, annoField.getFile());
    //    JSONObject jsonObject = Util.readFileToJSONObject(filePath);
    //    if ()
    //    return
    //}

}
