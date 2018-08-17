package com.demo.old.sysuser.service;

import com.demo.old.sysuser.model.SysUser;

public interface SysUserService {

    int updateByPrimaryKeySelective(SysUser record) throws Exception;
}
