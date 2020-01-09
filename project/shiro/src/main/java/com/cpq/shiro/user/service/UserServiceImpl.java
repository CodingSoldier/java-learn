package com.cpq.shiro.user.service;

import com.cpq.shiro.user.entity.User;
import com.cpq.shiro.user.mapper.UserMapper;
import com.cpq.shiro.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cpq
 * @since 2020-01-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
