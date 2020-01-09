package com.cpq.shiro;

import com.cpq.shiro.user.entity.UserVo;
import com.cpq.shiro.user.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    void contextLoads() {
        UserVo userVo = userMapper.findByUsername("admin");
        System.out.println(userVo.toString());
    }

}
