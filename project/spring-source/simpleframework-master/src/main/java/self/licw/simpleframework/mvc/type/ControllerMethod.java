package self.licw.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 待执行的controller及其对应的待执行方法的方法实例和参数的映射
 * 也就是一个controllermethod实例对应着一个controller里某个方法的实例（包括类 方法 和参数三者）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    //Controller类对应的class对象
    private Class<?> controllerClass;
    //执行的controller方法实例
    private Method invokeMethod;
    //执行的方法的参数名称以及对应的参数类型
    private Map<String,Class<?>> methodParameters;
}
