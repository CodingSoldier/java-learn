package self.licw.simpleframework.aop.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import self.licw.simpleframework.aop.PointcutLocator;

/**
 * 将@Order和@Aspect 封装在一起
 * orderIndex：@Order的值
 * aspect    ：要织入的aspect
 */

@AllArgsConstructor
@Getter
public class AspectInfo {
    private int orderIndex;
    private DefaultAspect aspect;
    //V2.0 新增pointcutlocator 也就是该注解类的表达式  eg：execute （self.licw.o2o..*..）
    private PointcutLocator pointcutLocator;
}
