package com.example.shirojwt.filter;

import com.example.shirojwt.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenpiqian
 * @date: 2020-01-16
 */
@Slf4j
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 返回自定义信息给前端
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = response.getWriter();){
            Map<String, String> result = new HashMap<>();
            if (out != null){
                throw new IOException("io异常");
            }
            out.print(objectMapper.writeValueAsString(Result.failMap("权限不足")));
        }catch (IOException e){
            log.error("", e);
        }
        return false;
    }
}
