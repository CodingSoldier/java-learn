package com.cpq.shiro2.user.service;

import com.cpq.shiro2.user.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cpq.shiro2.user.entity.UserVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author cpq
 * @since 2020-01-09
 */
public interface UserService extends IService<User> {
    UserVo findByUsername(String username);
}
