package com.example.shirojwt.filter;

import com.example.shirojwt.common.Result;
import com.example.shirojwt.util.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Slf4j
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    /**
     * 用户无权访问url时，此方法会被调用
     * 默认实现为org.apache.shiro.web.filter.authz.AuthorizationFilter#onAccessDenied()
     * 覆盖父类的方法，返回自定义信息给前端
     * <p>
     * 接口doc上说：
     * AuthorizationFilter子类(权限授权过滤器)的onAccessDenied()应该永远返回false，那么在onAccessDenied()内就必然要发送response响应给前端，不然前端就收不到任何数据
     * AuthenticationFilter、AuthenticatingFilter子类（身份认证过滤器）的onAccessDenied()的返回值则没有限制
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        WebUtil.sendResponse(response, Result.fail("权限不足"));
        return false;
    }

}
