package com.cpq.mybatispulslearn.saas.tenantrole.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cpq.mybatispulslearn.saas.tenantrole.entity.TTenantRole;
import com.cpq.mybatispulslearn.saas.tenantrole.mapper.TTenantRoleMapper;
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
    @Override
    public void test() {
        LambdaQueryWrapper<TTenantRole> lqw = new QueryWrapper<TTenantRole>().lambda();
        LambdaQueryWrapper<TTenantRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<TTenantRole> l = Wrappers.<TTenantRole>lambdaQuery();

        lqw.eq(TTenantRole::getRoleName, "1");
        lambdaQueryWrapper.eq(TTenantRole::getRoleName, "1");
        l.eq(TTenantRole::getRoleName, "1");
    }
}
