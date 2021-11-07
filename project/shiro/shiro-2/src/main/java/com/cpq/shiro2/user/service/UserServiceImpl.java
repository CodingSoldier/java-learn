package com.cpq.shiro2.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpq.shiro2.user.entity.User;
import com.cpq.shiro2.user.entity.UserVo;
import com.cpq.shiro2.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cpq
 * @since 2020-01-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public UserVo findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
