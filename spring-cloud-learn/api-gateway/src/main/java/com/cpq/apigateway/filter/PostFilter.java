package com.cpq.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class PostFilter extends ZuulFilter {

    @Autowired
    RestTemplate restTemplateBalanced;

    private Logger logger = LoggerFactory.getLogger(PostFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
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
        HttpServletRequest request = ctx.getRequest();
        System.out.println("****request******" + request.getHeader("log-id"));
        System.out.println("*****response*****" + response.getHeader("log-id"));

        Map<String, String> map = ctx.getZuulRequestHeaders();
        System.out.println("****map******" + map.toString());
        System.out.println("****map******" + map.get("log-id"));



        //try {
        //    Object zuulResponse = RequestContext.getCurrentContext().get("zuulResponse");
        //    if (zuulResponse != null) {
        //        RibbonHttpResponse resp = (RibbonHttpResponse) zuulResponse;
        //        String body = StreamUtils.copyToString(resp.getBody(), Charset.forName("UTF-8"));
        //        JSONObject jsonObject = JSONObject.parseObject(ctx.getResponseBody(), JSONObject.class);
        //        System.err.println(body);
        //        resp.close();
        //        RequestContext.getCurrentContext().setResponseBody(body);
        //    }
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}


        //
        //InputStream stream = ctx.getResponseDataStream();
        //try {
        //    String body = StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        //    System.err.println(body);
        //    JSONObject jsonObject = JSONObject.parseObject(body, JSONObject.class);
        //    ctx.setResponseBody(body);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}finally {
        //    //stream.close();
        //}

        Map<String, Object> logParams = new HashMap<>();
        logParams.put("userId", 111);
        logParams.put("loginName", "loginNam11e");
        logParams.put("operateModule", "用户222");
        logParams.put("operateType", "增加");
        logParams.put("operateTime", new Date());
        logParams.put("result", true);
        logParams.put("tenantCode", "tenantcode1");
        //restTemplateBalanced.postForObject("http://tenant-mq/log/producer/send", logParams, String.class);
        //restTemplate.postForObject("http://localhost:8083/tenant-mq/log/producer/send", logParams, String.class);
        //System.out.println("Services: " + discoveryClient.getServices());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity httpEntity = new HttpEntity(logParams, headers);
        restTemplateBalanced.postForObject("http://eureka-consumer-feign/p1", httpEntity, String.class);
                return null;
            }
        }
