package com.example.multidatasource.shiro.users.service;

import com.example.multidatasource.shiro.users.entity.Users;
import com.example.multidatasource.shiro.users.mapper.UsersMapper;
import com.example.multidatasource.shiro.users.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chenpiqian
 * @since 2020-03-26
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
