package ssm.validate;

import com.github.codingsoldier.paramsvalidate.ParamsValidate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller01 {

    @GetMapping("/test01")
    @ParamsValidate(file = "test.json")
    public Object functionValidate(@RequestParam String num){
        return num;
    }
}
