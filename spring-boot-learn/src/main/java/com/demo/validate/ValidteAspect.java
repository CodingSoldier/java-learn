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
            ResultCheck resultCheck = this.checkParams(joinPoint);
            if (resultCheck.getPass() != false){
                obj = ((ProceedingJoinPoint) joinPoint).proceed();
            }else{
                obj = paramsValidateInterface.validateNotPass(resultCheck);
            }
        }catch (Throwable e){

        }
        return obj;
    }

    private ResultCheck checkParams(JoinPoint joinPoint){
        ResultCheck resultCheck = new ResultCheck(true);
        Set<String> msgSet = new TreeSet<String>();
        AnnoField annoField = getFields(joinPoint);
        Object[] args = joinPoint.getArgs();

        //有request参数且是get请求
        boolean hasGetRequest = this.hasGetRequest(joinPoint);

        if (args != null){
            for (Object per:args){
                PerCheck perCheck = new PerCheck(true, "");
                perCheck.setPass(true);
                if (Util.notRequestResponse(per) && !hasGetRequest){
                    //被检验方法没有request参数，遍历参数校验
                    perCheck = validateMain.checkParam(per, annoField);
                }else if(Util.isRequest(per) && hasGetRequest){
                    //被检验方法有request参数且是GET方法，通过request获取参数校验
                    perCheck = validateMain.checkRequest((HttpServletRequest) per, annoField);
                }

                //当前被校验参数未通过校验
                if (perCheck.getPass() == false){
                    resultCheck.setPass(false);
                    if (Util.isNotEmpty(perCheck.getMsg())){
                        msgSet.add(perCheck.getMsg());
                        resultCheck.setMsgSet(msgSet);
                    }
                }
            }
        }
        return resultCheck;
    }

    //有request参数且是get请求
    private boolean hasGetRequest(JoinPoint joinPoint){
        boolean result = false;
        Object[] args = joinPoint.getArgs();
        if (args != null) {
            String method;
            for (Object per : args) {
                if (Util.isRequest(per)) {
                    method = ((HttpServletRequest) per).getMethod();
                    if (Util.strEqualsIgnoreCase(method, "GET")) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    //获取方法注解设置的值
    private AnnoField getFields(JoinPoint joinPoint){
        AnnoField annoField = new AnnoField("","");
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();

        try{
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for(Method method:methods){
                if(Util.strEquals(methodName, method.getName())){
                    Class[] clazz = method.getParameterTypes();
                    if(clazz.length == arguments.length){
                        if (method.getAnnotation(ParamsValidate.class) != null){
                            String file = method.getAnnotation(ParamsValidate.class).file();
                            String keyName = method.getAnnotation(ParamsValidate.class).keyName();
                            annoField.setFile(file);
                            annoField.setKeyName(keyName);
                            break;
                        }
                    }
                }
            }
        }catch (ClassNotFoundException e){
            //TODO ClassNotFoundException
        }
        return annoField;
    }

}
