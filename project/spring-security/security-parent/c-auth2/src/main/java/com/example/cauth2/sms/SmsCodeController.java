package com.example.cauth2.sms;


import com.example.cauth2.model.ValidateCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/code")
public class SmsCodeController {

    /**
     * 验证码的key
     */
    public static final String SESSION_KEY_SMS_CODE = "SESSION_KEY_SMS_CODE";


    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 生成短信验证码
     */
    @GetMapping("/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response){
        String smsCodeStr = RandomStringUtils.randomNumeric(4);
        System.out.println("短信验证码是：" + smsCodeStr);
        ValidateCode smsCode = new ValidateCode(smsCodeStr, 6000);
        // 将短信验证码放入session
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_SMS_CODE, smsCode);

    }


}
