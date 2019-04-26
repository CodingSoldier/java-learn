package com.cpq.mybatispulslearn.tenantrole.mapper;

import com.cpq.mybatispulslearn.tenantrole.entity.TTenantRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 租户角色表，用来控制用户的数据查看范围，如管理员，普通员工，2000自增 Mapper 接口
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
public interface TTenantRoleMapper extends BaseMapper<TTenantRole> {

}
