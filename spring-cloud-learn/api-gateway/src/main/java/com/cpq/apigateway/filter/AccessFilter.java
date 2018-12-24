package com.cpq.apigateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 是否登录过滤器
 */

@Component
public class AccessFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(AccessFilter.class);

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
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();

        try {
            //HttpServletRequest request = ctx.getRequest();
            //
            //Object accessToken = request.getParameter("token");
            //if ("false".equals(accessToken)){
            //    ctx.setSendZuulResponse(false);
            //    ctx.setResponseStatusCode(401);
            //    return null;
            //}

        }catch (Exception e){
            logger.error("网关捕获异常", e);
            Map result = new HashMap();
            result.put("code", 500);
            result.put("msg", "系统异常");
            result.put("exception",e.getMessage());
            ctx.setResponseBody(JSON.toJSONString(result));
            ctx.setSendZuulResponse(false);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        }

        return null;
    }
}
