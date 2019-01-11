package ssm.validate.controller;

import com.github.codingsoldier.paramsvalidate.ParamsValidate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controller01 {

    @GetMapping("/test01")
    @ResponseBody
    @ParamsValidate(file = "test.json")
    public Object functionValidate(@RequestParam String num){
        return num;
    }
}
