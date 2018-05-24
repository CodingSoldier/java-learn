package ssm.module01.service;

import org.springframework.stereotype.Service;
import ssm.module01.mapper.UserMapper;
import ssm.module01.pojo.User;

import javax.annotation.Resource;

@Service
public class ServiceHtmlToSql {
    @Resource
    private UserMapper userMapper;

    public User testFindUser(int id) throws Exception{

        User user = userMapper.selectByPrimaryKey(id);
        System.out.println(user.toString());
        return user;
    }


}
