package com.cpq.mybatispulslearn.sysrole.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cpq.mybatispulslearn.sysrole.entity.SysRole;
import com.cpq.mybatispulslearn.sysrole.entity.SysRoleVo;
import com.cpq.mybatispulslearn.sysrole.mapper.SysRoleMapper;
import com.cpq.mybatispulslearn.sysrole.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cpq
 * @since 2019-01-08
 */
@RestController
@RequestMapping("/sysrole/sys-role")
public class SysRoleController {

    @Autowired
    ISysRoleService sysRoleService;
    @Autowired
    SysRoleMapper sysRoleMapper;

    @PostMapping("/save")
    public Object save(@RequestBody SysRoleVo sysRoleVo){
        sysRoleVo.setId(UUID.randomUUID().toString().replace("-",""));
        return sysRoleService.save(sysRoleVo);
    }

    @PostMapping("/update")
    public Object update(@RequestBody SysRoleVo sysRoleVo){
        return sysRoleService.updateById(sysRoleVo);
    }

    @PostMapping("/removeById")
    public Object removeById(@RequestBody SysRoleVo sysRoleVo){
        return sysRoleService.removeById(sysRoleVo.getId());
    }

    @PostMapping("/page")
    public Object page(@RequestBody SysRoleVo sysRoleVo){
        //new Page(由1开始, 每页的记录数)
        Page<SysRole> page = new Page(sysRoleVo.getCurrent(),sysRoleVo.getSize());
        QueryWrapper<SysRole> qw = new QueryWrapper();
        //第一个参数是column，数据库原字段
        qw.eq("create_user_id",sysRoleVo.getCreateUserId());
        qw.orderByAsc("create_time");
        return sysRoleService.page(page,qw);
    }

    @PostMapping("/page2")
    public Object page2(@RequestBody SysRoleVo sysRoleVo){
        Page<SysRole> page = new Page(sysRoleVo.getCurrent(),sysRoleVo.getSize());
        QueryWrapper<SysRole> qw = new QueryWrapper();
        qw.setEntity(sysRoleVo);  //设置entry，entry的属性值等于表中的列的值
        return sysRoleService.page(page,qw);
    }

    @PostMapping("/both/table")
    public Object bothTable(@RequestBody SysRoleVo sysRoleVo){
        Map<String, Object> query = new HashMap<>();
        query.put("roleId", sysRoleVo.getId());
        return sysRoleMapper.selectRoleUser(query);
    }

}
