package com.example.shirojwt.filter;

import com.example.shirojwt.JwtToken;
import com.example.shirojwt.common.Constant;
import com.example.shirojwt.common.CustomException;
import com.example.shirojwt.common.Result;
import com.example.shirojwt.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        // 获取请求头Authorization的值
        String authorization = getAuthzHeader(request);
        return new JwtToken(authorization);
    }

    /**
     * 执行登录操作
     * 大部分代码跟父类一样，不同之处是catch到异常后返回自定义异常给前端
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        AuthenticationToken token = this.createToken(request, response);
        if (token == null) {
            String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken must be created in order to execute a login attempt.";
            throw new IllegalStateException(msg);
        } else {
            try {
                Subject subject = this.getSubject(request, response);
                subject.login(token);
                return this.onLoginSuccess(token, subject, request, response);
            } catch (AuthenticationException e) {
                Result result = Result.fail("用户认证异常");
                if (e.getCause() instanceof CustomException) {
                    CustomException customException = (CustomException) e.getCause();
                    result = Result.fail(customException.getCode(), customException.getMessage());
                }
                WebUtil.sendResponse(response, result);
                return false;
            }
        }
    }

    /**
     * 也可以将executeLogin()注释掉，再将此方法放开，同样能达到登陆失败后返回自定义信息给前端的作用，
     * 自己看下父类org.apache.shiro.web.filter.authc.AuthenticatingFilter#executeLogin()源码就能理解了
     *
     * 我觉得覆盖executeLogin()可以更好的理解身份认证filter的流程
     */
    //@Override
    //protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
    //    Result result = Result.fail("用户认证异常");
    //    if (e.getCause() instanceof CustomException){
    //        CustomException customException = (CustomException)e.getCause();
    //        result = Result.fail(customException.getCode(), customException.getMessage());
    //    }
    //    try {
    //        WebUtil.sendResponse(response, result);
    //    }catch (IOException ioe){
    //        log.error("发送response响应异常", ioe);
    //    }
    //    return false;
    //}


    /**
     * 身份认证未通过，执行此方法
     * 返回true，继续处理请求
     * 返回false，不继续处理请求，结束过滤器链
     * BasicHttpAuthenticationFilter源码中也是在onAccessDenied()方法内调用executeLogin
     * <p>
     * 调用链源码
     * org.apache.shiro.web.filter.AccessControlFilter#onPreHandle(){
     * this.isAccessAllowed(request, response, mappedValue) || this.onAccessDenied(request, response, mappedValue);
     * }
     * isAccessAllowed()：如果当前用户通过了身份验证，则返回true。
     * 由于工程禁用了session，这导致isAccessAllowed()永远返回false，那么this.onAccessDenied()本方法永远被调用，相当与是每次请求，我们都执行了一次登录操作。
     * <p>
     * 若不禁用session，session中会存储用户标识（在本例中就是请求头Authorization的值）和 用户的认证状态
     * 将com.example.shirojwt.ShiroConfig#securityManager()中禁用session的代码注释掉，则此时的session存储为true
     * debug源码，在下面的代码中打断点
     * org.apache.shiro.web.filter.authc.AuthenticationFilter#isAccessAllowed()
     * 获取当前的subject，可以将subject理解为用户
     * org.apache.shiro.web.mgt.DefaultWebSubjectFactory#createSubject()
     * 主要代码如下：
     * PrincipalCollection principals = wsc.resolvePrincipals();
     * principals = (PrincipalCollection)session.getAttribute(PRINCIPALS_SESSION_KEY);
     * 即principals存储在session中了
     * boolean authenticated = wsc.resolveAuthenticated();
     * Boolean sessionAuthc = (Boolean)session.getAttribute(AUTHENTICATED_SESSION_KEY);
     * 同样的authenticated（是否已经认证也存储在session中）
     * 最后通过session中的属性创建subject
     * new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled, request, response, securityManager);
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean r;
        String authorization = getAuthzHeader(request);
        if (StringUtils.isEmpty(authorization)) {
            WebUtil.sendResponse(response, Result.fail(Constant.CODE_TOKEN_ERROR, "无token"));
            r = false;
        } else {
            r = executeLogin(request, response);
        }
        return r;
    }

}
