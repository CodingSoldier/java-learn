package com.cpq.shiro2.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cpq.shiro2.user.entity.User;
import com.cpq.shiro2.user.entity.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cpq
 * @since 2020-01-09
 */
public interface UserMapper extends BaseMapper<User> {

    UserVo findByUsername(@Param("username") String username);

}
