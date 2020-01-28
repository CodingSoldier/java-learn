package com.example.shirojwt.service;

import com.example.shirojwt.model.Permission;
import com.example.shirojwt.model.Role;
import com.example.shirojwt.model.User;
import lombok.Data;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 这个类主要是模拟从数据库获取数据
 */
@Service
@Data
public class UserService {

    // 权限
    Permission userView = new Permission("user/view");
    Permission userAdd = new Permission("user/add");
    Permission userEdit = new Permission("user/edit");
    Permission userDelete = new Permission("user/delete");
    Permission testOther = new Permission("test/other");
    Permission rolePermisssionEdit = new Permission("role/permission/edit名字随便起，别重复就行");

    // 新建角色并设置角色权限
    Role viewRole = new Role("userView", new ArrayList<Permission>(){{add(userView);}});
    Role cudrRole = new Role("cudr", new ArrayList<Permission>(){{
        add(userView);
        add(userAdd);
        add(userEdit);
        add(userDelete);
    }});
    Role otherRole = new Role("testOther", new ArrayList<Permission>(){{add(testOther);}});
    // adminRole关联的权限太多，可在数据库中给adminRole一个特殊标识，表示此角色关联所有权限，写SQL查询时通过特殊标识查出所有权限
    Role adminRole = new Role("admin", new ArrayList<Permission>(){{
        add(userView);
        add(userAdd);
        add(userEdit);
        add(userDelete);
        add(testOther);
        add(rolePermisssionEdit);
    }});

    // 新建用户并设置用户角色
    User admin = new User("admin",
                             new Md5Hash("admin-pwd").toString(), Arrays.asList(adminRole));

    User cudrOtherUser = new User("cudrOtherUser",
                                     new Md5Hash("cudrOtherUser-pwd").toString(), new ArrayList<Role>(){{add(cudrRole);add(otherRole);}});

    User viewUser = new User("viewUser",
                                new Md5Hash("viewUser-pwd").toString(), Arrays.asList(viewRole));

    /**
     * 通过username获取用户，
     * 实际项目中使用数据库中的数据，类似 userService.getUser()
     */
    public User getUser(String username){
        if ("admin".equals(username)){
            return admin;
        }else if ("cudrOtherUser".equals(username)){
            return cudrOtherUser;
        }else if ("viewUser".equals(username)){
            return viewUser;
        }
        return null;
    }

    /**
     * 将rolePermisssionEdit权限添加到cudrRole
     */
    public void cudrRoleAddRolePermisssionEdit(){
        cudrRole.getPermissionList().add(rolePermisssionEdit);
    }

}
