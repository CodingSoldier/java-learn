package com.demo.sysuser.mapper;

import java.util.List;
import java.util.Map;

public interface SysUserExpandMapper {
    List<Map<String, Object>> selectUserRole(String id);
}