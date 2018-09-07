package ssm.validate;

import com.github.codingsoldier.paramsvalidate.ValidateInterfaceAdapter;
import com.github.codingsoldier.paramsvalidate.bean.ResultValidate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ValidateInterfaceImpl extends ValidateInterfaceAdapter{

    //返回json文件基础路径。init.json文件必须放在此目录下
    @Override
    public String basePath() {
        return "config/validate/";
    }

    //参数校验未通过, 返回自定义数据给客户端的数据
    @Override
    public Object validateNotPass(ResultValidate resultValidate) {
        Set<String> msgSet = resultValidate.getMsgSet();
        Map<String, Object> r = new HashMap<>();
        r.put("code", resultValidate.isPass() ? 0 : 101);
        r.put("data", msgSet);
        return r;
    }

}
