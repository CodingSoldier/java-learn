package com.demo.validate;

import com.demo.validate.bean.AnnoField;
import com.demo.validate.bean.PerCheck;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ValidateMain {

    public PerCheck checkParam(Object params, AnnoField paramBean) {
        PerCheck perCheck = new PerCheck();
        //String path = "config/validate/validate.json";
        //InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        //BufferedReader in = new BufferedReader(new InputStreamReader(is));
        //StringBuffer buffer = new StringBuffer();
        //String line = "";
        //while ((line = in.readLine()) != null){
        //    buffer.append(line);
        //}
        //String jsonStr = buffer.toString();
        //JSONObject jsonObject = JSON.parseObject(jsonStr);
        //System.out.println(jsonObject.toString());
        return perCheck;
    }

    public PerCheck checkRequest(HttpServletRequest request, AnnoField paramBean) {
        PerCheck perCheck = new PerCheck();
        //String path = "config/validate/validate.json";
        //InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
        //BufferedReader in = new BufferedReader(new InputStreamReader(is));
        //StringBuffer buffer = new StringBuffer();
        //String line = "";
        //while ((line = in.readLine()) != null){
        //    buffer.append(line);
        //}
        //String jsonStr = buffer.toString();
        //JSONObject jsonObject = JSON.parseObject(jsonStr);
        //System.out.println(jsonObject.toString());
        return perCheck;
    }

    //public


}
