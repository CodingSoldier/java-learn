package com.example.shirojwt.model;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
@Service
public class UserService {

    User admin, cudrOtherUser, viewUser;

    {
        // 权限
        Permission view = new Permission("view");
        Permission add = new Permission("add");
        Permission edit = new Permission("edit");
        Permission delete = new Permission("delete");
        Permission other = new Permission("other");

        // 新建角色并设置角色权限
        Role viewRole = new Role("view", Arrays.asList(view));
        Role cudrRole = new Role("cudr", Arrays.asList(view, add, edit, delete));
        Role otherRole = new Role("other", Arrays.asList(other));
        Role adminRole = new Role("admin", Arrays.asList(view, add, edit, delete, other));

        // 新建用户并设置用户角色
        admin = new User("admin",
                new Md5Hash("admin-pwd").toString(), Arrays.asList(adminRole));

        cudrOtherUser = new User("cudrOtherUser",
                new Md5Hash("cudrOtherUser-pwd").toString(), Arrays.asList(cudrRole, otherRole));

        viewUser = new User("viewUser",
                new Md5Hash("viewUser-pwd").toString(), Arrays.asList(viewRole));
    }

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

}