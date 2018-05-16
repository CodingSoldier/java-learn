package org.validate;

import org.springframework.stereotype.Component;

@Component
public class TestController {

    @ParamsValidate
    public Object request01(Object object) throws Exception{

        System.out.println(object.toString());
        return object;
    }

}
