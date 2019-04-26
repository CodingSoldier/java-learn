package com.cpq.mybatispulslearn.tenantrole.service;

import com.cpq.mybatispulslearn.tenantrole.entity.TTenantRole;
import com.cpq.mybatispulslearn.tenantrole.mapper.TTenantRoleMapper;
import com.cpq.mybatispulslearn.tenantrole.service.TTenantRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户角色表，用来控制用户的数据查看范围，如管理员，普通员工，2000自增 服务实现类
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Service
public class TTenantRoleServiceImpl extends ServiceImpl<TTenantRoleMapper, TTenantRole> implements TTenantRoleService {

}
