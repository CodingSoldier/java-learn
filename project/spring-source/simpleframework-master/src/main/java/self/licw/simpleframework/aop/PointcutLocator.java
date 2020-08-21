package self.licw.simpleframework.aop;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;

public class PointcutLocator {
    //pointcut解析器 直接给它赋值上Aspectj的所有表达式，以便支持对众多表达式的解析
    private PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
            //获取所有pointcut语法树，并作为参数，以便该pointcutParser支持所有语法
            PointcutParser.getAllSupportedPointcutPrimitives()
    );

    //表达式解析器。pointcutExpression是pointcutParser解析语法树所得出来的产物
    private PointcutExpression pointcutExpression;

    public PointcutLocator(String expression){
        this.pointcutExpression = pointcutParser.parsePointcutExpression(expression);
    }

    /**
     * 判断传入的class对象是否是Aspect的目标代理类，即匹配Pointcut表达式（初步筛选）
     * @param targetClass 目标类
     * @return            是否匹配
     */
    public boolean roughMatches(Class<?> targetClass){
        //couldMatchJoinPointsInType只能校验whitin语法树，对execution等表达式无法校验，直接返回true
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断传入的Method对象是否是Aspect的目标代理方法，即匹配pointcut表达式（精筛)
     * @param method  目标方法
     * @return
     */
    public boolean accurateMatches(Method method){
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);
        //如果match的匹配结果是全部匹配，则认为是目标方法
        if (shadowMatch.alwaysMatches()){
            return true;
        }else{
            //部分匹配和完全不匹配全部视为非目标方法
            return false;
        }
    }
}
