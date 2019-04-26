package com.cpq.mybatispulslearn;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cpq.mybatispulslearn.adminrole.entity.ZjAdminRole;
import com.cpq.mybatispulslearn.adminrole.mapper.ZjAdminRoleMapper;
import com.cpq.mybatispulslearn.sysmenu.entity.TSysMenu;
import com.cpq.mybatispulslearn.sysmenu.mapper.TSysMenuMapper;
import com.cpq.mybatispulslearn.sysuserextend.entity.TSysUserExtend;
import com.cpq.mybatispulslearn.sysuserextend.mapper.TSysUserExtendMapper;
import com.cpq.mybatispulslearn.tenantmember.entity.TTenantMember;
import com.cpq.mybatispulslearn.tenantmember.mapper.TTenantMemberMapper;
import com.cpq.mybatispulslearn.tenantmember2role.entity.TTenantMember2role;
import com.cpq.mybatispulslearn.tenantmember2role.mapper.TTenantMember2roleMapper;
import com.cpq.mybatispulslearn.tenantrole.entity.TTenantRole;
import com.cpq.mybatispulslearn.tenantrole.mapper.TTenantRoleMapper;
import com.cpq.mybatispulslearn.tenantrole2menu.entity.TTenantRole2menu;
import com.cpq.mybatispulslearn.tenantrole2menu.mapper.TTenantRole2menuMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZhenJiaData {

    @Autowired
    ZjAdminRoleMapper zjAdminRoleMapper;

    @Autowired
    TTenantMemberMapper tTenantMemberMapper;
    @Autowired
    TSysUserExtendMapper tSysUserExtendMapper;

    @Autowired
    TTenantRoleMapper tTenantRoleMapper;
    @Autowired
    TSysMenuMapper tSysMenuMapper;
    @Autowired
    TTenantRole2menuMapper tTenantRole2menuMapper;
    @Autowired
    TTenantMember2roleMapper tTenantMember2roleMapper;

    public static final String TENANT_CODE = "BLWY";


    /**
     * 修改 t_tenant_role 自增主键为1
     */
    @Test
    public void tenantRole() {
        tTenantRoleMapper.delete(new QueryWrapper<>());

        //管理员账号
        TTenantRole tenantRole = new TTenantRole();
        tenantRole.setId(1L);
        tenantRole.setRoleName("admin");
        tenantRole.setComments("admin");
        tenantRole.setTenantCode(TENANT_CODE);
        tenantRole.setRoleType(0);
        tTenantRoleMapper.insert(tenantRole);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_isdelete", 0);
        List<ZjAdminRole> sourceList = zjAdminRoleMapper.selectList(queryWrapper);
        for (ZjAdminRole zjAdminRole:sourceList){
            tenantRole = new TTenantRole();
            tenantRole.setId(Long.parseLong(zjAdminRole.getRoleId().toString()));
            tenantRole.setRoleName(zjAdminRole.getRoleName());
            tenantRole.setComments(zjAdminRole.getRoleDetail());
            tenantRole.setSmsStatus(zjAdminRole.getSmsStatus());
            tenantRole.setLinkRoleId(zjAdminRole.getRoleId());
            tenantRole.setTenantCode(TENANT_CODE);
            tTenantRoleMapper.insert(tenantRole);
        }
    }


    /**
     *  t_tenant_member2role  自增主键为1
     */
    @Test
    public void member2Role() {

        tTenantMember2roleMapper.delete(new QueryWrapper<>());

        //用户
        QueryWrapper memberWrapper = new QueryWrapper();
        memberWrapper.eq("deleted", 0);
        memberWrapper.eq("tenant_code", TENANT_CODE);
        List<TTenantMember> memberList = tTenantMemberMapper.selectList(memberWrapper);

        //用户拓展
        List<TSysUserExtend> userExtendList = tSysUserExtendMapper.selectList(new QueryWrapper());

        QueryWrapper roleWrapper = new QueryWrapper();
        roleWrapper.eq("tenant_code", TENANT_CODE);
        List<TTenantRole> roleList = tTenantRoleMapper.selectList(roleWrapper);

        //成员
        for (TTenantMember member:memberList){
            Long memberId = member.getId();
            //成员臻家roleId
            for (TSysUserExtend userExtend:userExtendList){
                if (member.getUserId() != null && userExtend.getUserId() != null
                        && member.getUserId().longValue() == userExtend.getUserId().longValue()){
                    Integer zjRoleId = userExtend.getRoleId();

                    //成员臻家roleId对应到SaaS的roleid
                    if (zjRoleId != null){
                        if ( zjRoleId.intValue() == 0){
                            //保利角色
                            if ("BLWY".equals(member.getTenantCode())){
                                TTenantMember2role tenantMember2role = new TTenantMember2role();
                                tenantMember2role.setTenantMemberId(memberId);
                                tenantMember2role.setTenantRoleId(1L);
                                tTenantMember2roleMapper.insert(tenantMember2role);
                            }
                        }else{
                            for (TTenantRole role:roleList){
                                if (role.getLinkRoleId() != null && role.getLinkRoleId().intValue() == zjRoleId){
                                    TTenantMember2role tenantMember2role = new TTenantMember2role();
                                    tenantMember2role.setTenantMemberId(memberId);
                                    tenantMember2role.setTenantRoleId(role.getId());
                                    tTenantMember2roleMapper.insert(tenantMember2role);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 修改 t_tenant_role2menu 自增主键为1
     */
    @Test
    public void menu2Role() {
        //门户菜单
        QueryWrapper menuWrapper = new QueryWrapper();
        menuWrapper.eq("menu_type", 1);
        List<TSysMenu> sysMenuList = tSysMenuMapper.selectList(menuWrapper);

        List<TTenantRole> roleList = tTenantRoleMapper.selectList(new QueryWrapper<>());

        tTenantRole2menuMapper.delete(new QueryWrapper<>());
        //admin角色有所有菜单
        for (TTenantRole tenantRole:roleList){
            if (tenantRole.getRoleType() != null && tenantRole.getRoleType() == 0){
                for (TSysMenu sysMenu:sysMenuList){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(sysMenu.getId());
                    role2menu.setRoleId(tenantRole.getId());
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);
                }
            }
        }

        for (TTenantRole tenantRole:roleList){
            Long roleId = tenantRole.getId();
            Integer linkRoleId = tenantRole.getLinkRoleId();
            if (linkRoleId != null){
                //角色
                ZjAdminRole adminRole = zjAdminRoleMapper.selectById(linkRoleId);

                //基本信息
                if (!StringUtils.isEmpty(adminRole.getRoleAccess())
                        && adminRole.getRoleAccess().contains("\"41\"")
                        && adminRole.getRoleAccess().contains("\"46\"")
                        && adminRole.getRoleAccess().contains("\"52\"")){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(1L);
                    role2menu.setRoleId(roleId);
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);
                }
                //组织架构
                if (!StringUtils.isEmpty(adminRole.getRoleAccess()) && adminRole.getRoleAccess().contains("\"41\"")){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(8L);
                    role2menu.setRoleId(roleId);
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);
                }
                //房产信息
                if (!StringUtils.isEmpty(adminRole.getRoleAccess()) && adminRole.getRoleAccess().contains("\"46\"")){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(9L);
                    role2menu.setRoleId(roleId);
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);
                }

                //客户信息
                if (!StringUtils.isEmpty(adminRole.getRoleAccess()) && adminRole.getRoleAccess().contains("\"52\"")){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(10L);
                    role2menu.setRoleId(roleId);
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);
                }

                //收费项目
                if (!StringUtils.isEmpty(adminRole.getRoleAccess()) && adminRole.getRoleAccess().contains("\"62\"")){
                    TTenantRole2menu role2menu = new TTenantRole2menu();
                    role2menu.setMenuId(2000L);
                    role2menu.setRoleId(roleId);
                    role2menu.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu);

                    TTenantRole2menu role2menu1 = new TTenantRole2menu();
                    role2menu1.setMenuId(12L);
                    role2menu1.setRoleId(roleId);
                    role2menu1.setTenantCode(TENANT_CODE);
                    tTenantRole2menuMapper.insert(role2menu1);
                }

            }
        }
    }


}
