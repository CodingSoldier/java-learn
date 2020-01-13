package com.cpq.shiro2.ctrl;

import com.cpq.shiro2.user.entity.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class TestController {

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "login";
    }

    @RequestMapping("unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }

    @RequestMapping("/ad")
    public String admin() {
        return "admin success";
    }

    @RequestMapping("/ed")
    public String edit() {
        return "edit success";
    }

    @RequestMapping("/test/add")
    public String add() {
        return "/test/add";
    }

    @RequestMapping("/test/role/add")
    public String add2() {
        return "/test/role/add";
    }

    /**
     * 登陆接口
     */
    @RequestMapping("/loginUser")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            HttpSession session) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            UserVo userVo = (UserVo) subject.getPrincipal();
            //session.setAttribute("user", userVo);
            return "index";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }

}