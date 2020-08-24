package self.licw.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 在MVC中描述一个controller方法，包括controller类、方法、方法参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControllerMethod {
    // Controller的class对象
    private Class<?> controllerClass;
    // Controller方法实例
    private Method invokeMethod;
    // 方法参数名以及对应的参数类型
    private Map<String, Class<?>> methodParameters;
}
