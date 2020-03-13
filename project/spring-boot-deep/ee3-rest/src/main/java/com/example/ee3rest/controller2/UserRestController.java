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

    @PostMapping(value = "/echo/user2",
            consumes = "application/*;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public User user2(@RequestBody User user) {
        return user;
    }

}
