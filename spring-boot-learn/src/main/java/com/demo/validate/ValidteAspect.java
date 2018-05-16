package com.demo.validate;

import com.demo.validate.bean.AnnoField;
import com.demo.validate.bean.PerCheck;
import com.demo.validate.bean.ResultCheck;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

@Aspect
@Component
public class ValidteAspect {

    @Autowired
    ValidateMain validateMain;
    @Autowired
    ParamsValidateInterface paramsValidateInterface;

    @Pointcut("@annotation(com.demo.validate.ParamsValidate)")
    public void aspect(){}

    @Around("aspect()")
    public Object around(JoinPoint joinPoint){
        System.out.println("@Around环绕通知，方法执行前");
        Object obj = null;
        try {
            ResultCheck resultCheck = this.check(joinPoint);
            if (resultCheck.getPass() != false){
                obj = ((ProceedingJoinPoint) joinPoint).proceed();
            }else{
                obj = paramsValidateInterface.validateNotPass(resultCheck);
            }
        }catch (Throwable e){

        }
        return obj;
    }

    private ResultCheck check(JoinPoint joinPoint){
        ResultCheck resultCheck = new ResultCheck();
        Set<String> msgSet = new TreeSet<String>();
        AnnoField annoField = getFields(joinPoint);
        Object[] args = joinPoint.getArgs();

        //有request参数且是get请求
        boolean hasGetRequest = this.hasGetRequest(joinPoint);

        if (args != null){
            for (Object per:args){
                PerCheck perCheck = new PerCheck();
                perCheck.setPass(true);
                if (ValidateUtils.notRequestResponse(per) && !hasGetRequest){
                    //被检验方法没有request参数，遍历参数校验
                    perCheck = validateMain.checkParam(per, annoField);
                }else if(ValidateUtils.isRequest(per) && hasGetRequest){
                    //被检验方法有request参数且是GET方法，通过request获取参数校验
                    perCheck = validateMain.checkRequest((HttpServletRequest) per, annoField);
                }

                //当前被校验参数未通过校验
                if (perCheck.getPass() == false){
                    resultCheck.setPass(false);
                    if (ValidateUtils.isNotEmpty(perCheck.getMsg())){
                        msgSet.add(perCheck.getMsg());
                        resultCheck.setMsgSet(msgSet);
                    }
                }
            }
        }
        return resultCheck;
    }

    //有request参数且是get请求
    private Boolean hasGetRequest(JoinPoint joinPoint){
        boolean result = false;
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            String method;
            for (Object per : args) {
                if (ValidateUtils.isRequest(per)) {
                    method = ((HttpServletRequest) per).getMethod();
                    if (ValidateUtils.strEqualsIgnoreCase(method, "GET")) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    //获取方法注解设置的值
    private AnnoField getFields(JoinPoint joinPoint){
        AnnoField paramBean = new AnnoField();
        String file = "";
        String keyName = "";
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        try{
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for(Method method:methods){
                if(ValidateUtils.strEquals(methodName, method.getName())){
                    Class[] clazz = method.getParameterTypes();
                    if(clazz.length == arguments.length){
                        if (method.getAnnotation(ParamsValidate.class) != null){
                            file = method.getAnnotation(ParamsValidate.class).file();
                            keyName = method.getAnnotation(ParamsValidate.class).keyName();
                            paramBean.setFile(file);
                            paramBean.setKeyName(keyName);
                            break;
                        }
                    }
                }
            }
        }catch (ClassNotFoundException e){
            //TODO ClassNotFoundException
        }
        return paramBean;
    }

}
