package com.cpq.shiro2;

import com.cpq.shiro2.user.entity.UserVo;
import com.cpq.shiro2.user.mapper.UserMapper;
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
