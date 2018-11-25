package com.cpq.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 是否登录过滤器
 */

@Component
public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext rtx = RequestContext.getCurrentContext();
        HttpServletRequest request = rtx.getRequest();
        Object accessToken = request.getParameter("token");
        if ("false".equals(accessToken)){
            rtx.setSendZuulResponse(false);
            rtx.setResponseStatusCode(401);
            return null;
        }
        return null;
    }
}
