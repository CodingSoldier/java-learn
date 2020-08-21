package self.licw.simpleframework.mvc.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存储http请求路径和请求方法
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPathInfo {
    //http的请求方法 GET/POST
    private String httpMethod;
    //http的请求路径
    private String httpPath;
}
