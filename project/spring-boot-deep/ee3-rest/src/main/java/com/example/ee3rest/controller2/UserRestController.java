package com.example.ee3rest.controller2;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenpiqian
 * @date: 2020-03-13
 */
@RestController
public class UserRestController {

    @PostMapping(value = "/echo/user1")
    public User user1(@RequestBody User user) {
        return user;
    }

    /**
     * application/json是媒体类型，charset=UTF-8是字符编码
     * 请求头中的Content-Type要和consumes一样
     */
    @PostMapping(value = "/echo/user/produces",
            produces = "application/json;charset=UTF-8",
            consumes = "application/json;charset=UTF-8")

            //返回responseHeader的Content-Type=application/json;charset=GBK
            //produces = "application/json;charset=GBK")
    public User user2(@RequestBody User user) {
        return user;
    }

}
