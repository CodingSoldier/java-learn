package com.example.multidatasource.shiro.role.service;

import com.example.multidatasource.shiro.role.entity.Role;
import com.example.multidatasource.shiro.role.mapper.RoleMapper;
import com.example.multidatasource.shiro.role.service.RoleService;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
