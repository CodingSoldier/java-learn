package com.example.shirojwt.filter;

import com.example.shirojwt.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // 获取请求头 Authorization
        String authorization = getAuthzHeader(request);
        return new JwtToken(authorization);
    }


    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }
    //
    ///**
    // *
    // */
    //@Override
    //protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
    //    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    //    String authorization = httpServletRequest.getHeader("Authorization");
    //
    //    JWTToken token = new JWTToken(authorization);
    //    // 提交给realm进行登入，如果错误他会抛出异常并被捕获
    //    getSubject(request, response).login(token);
    //    // 如果没有抛出异常则代表登入成功，返回true
    //    return true;
    //}

    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
    // */
    //@Override
    //public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    //
    //    if (isLoginAttempt(request, response)) {
    //        try {
    //            executeLogin(request, response);
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    return true;
    //}


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean r = true;
        if (isLoginAttempt(request, response)){
            try {
                r = executeLogin(request, response);
            }catch (Exception e){
                log.error("token异常", e);
                r = false;
            }
        }
        return r;
    }

    //@Override
    //protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
    //    //Subject subject = this.getSubject(request, response);
    //    //if (subject.getPrincipal() == null) {
    //    //    this.saveRequestAndRedirectToLogin(request, response);
    //    //} else {
    //    //    String unauthorizedUrl = this.getUnauthorizedUrl();
    //    //    if (StringUtils.hasText(unauthorizedUrl)) {
    //    //        WebUtils.issueRedirect(request, response, unauthorizedUrl);
    //    //    } else {
    //    //        WebUtils.toHttp(response).sendError(401);
    //    //    }
    //    //}
    //
    //    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    //    httpResponse.setCharacterEncoding("UTF-8");
    //    httpResponse.setContentType("application/json;charset=UTF-8");
    //    httpResponse.setStatus(Constant.CODE_FAIL);
    //    httpResponse.getWriter().println("token异常".getBytes("UTF-8"));
    //
    //    return false;
    //}

}
