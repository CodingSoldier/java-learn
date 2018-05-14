package com.cpq.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import javax.servlet.http.HttpServletRequest;

public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
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
