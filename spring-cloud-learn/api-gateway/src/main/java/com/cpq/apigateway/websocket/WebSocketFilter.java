//package com.cpq.apigateway.filter;
//
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Component
//public class WebSocketFilter extends ZuulFilter {
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//    @Override
//    public Object run() {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//        String upgradeHeader = request.getHeader("Upgrade");
//        if (null == upgradeHeader) {
//            upgradeHeader = request.getHeader("upgrade");
//        }
//        if (null != upgradeHeader && "websocket".equalsIgnoreCase(upgradeHeader)) {
//            context.addZuulRequestHeader("connection", "Upgrade");
//        }
//        return null;
//    }
//}