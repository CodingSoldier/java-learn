package ssm.projectnote.spring.aop;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DAnnoLog {
    public String operationType() default "";
    public String operationName() default "";
}
