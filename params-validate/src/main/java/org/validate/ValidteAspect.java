package org.validate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Aspect
@Component
public class ValidteAspect {

    @Pointcut("@annotation(org.validate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        System.out.println("@Around环绕通知，方法执行前");
        Object obj = null;
        try {
            System.out.println(joinPoint.getArgs());
            String path = "config/validate.json";
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = in.readLine()) != null){
                buffer.append(line);
            }
            String jsonStr = buffer.toString();
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            System.out.println(jsonObject.toString());

            ((ProceedingJoinPoint) joinPoint).proceed();

        }catch (Throwable e){
            System.out.println("@Around执行方法异常");
        }
        //System.out.println("@Around环绕通知，方法执行后");
        return obj;
    }

}
