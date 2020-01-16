package com.example.shirojwt.controller;

import com.example.shirojwt.JwtToken;
import com.example.shirojwt.common.MyException;
import com.example.shirojwt.common.Result;
import com.example.shirojwt.model.User;
import com.example.shirojwt.model.UserService;
import com.example.shirojwt.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@RestController
@Slf4j
public class Ctrl1 {

    @Autowired
    UserService userService;

    /**
     * admin  admin-pwd
     * cudrOtherUser  cudrOtherUser-pwd
     * viewUser  viewUser-pwd
     */
    @PostMapping("/open/login")
    public Result openLogin(@RequestBody User userVo){
        String username = userVo.getUsername();
        String password = userVo.getPassword();

        User user = userService.getUser(username);
        if (user == null){
            throw new MyException("无此用户");
        }
        if (!user.getPassword().equals(new Md5Hash(password).toString())){
            throw new MyException("用户名或密码错误");
        }

        String authorization = JWTUtil.sign(username, user.getPassword());

        JwtToken jwtToken = new JwtToken(authorization);
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);

        return Result.success(authorization);
    }

    @GetMapping("/user/list")
    public Result list(){
        return Result.success("/user/list");
    }

    @RequiresPermissions("add")
    @GetMapping("/user/add")
    public Result add(){
        return Result.success("/user/add");
    }

    @RequiresPermissions("edit")
    @GetMapping("/user/edit")
    public Result edit(){
        return Result.success("/user/edit");
    }

    @GetMapping("/user/delete")
    public Result delete(){
        return Result.success("/user/delete");
    }


}
