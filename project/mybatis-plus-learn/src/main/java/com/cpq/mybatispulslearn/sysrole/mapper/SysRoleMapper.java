package com.cpq.mybatispulslearn.sysrole.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpq.mybatispulslearn.sysrole.entity.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cpq
 * @since 2019-01-09
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<Map<String, Object>> selectRoleUser(@Param("query") Map<String, Object> query);
}
