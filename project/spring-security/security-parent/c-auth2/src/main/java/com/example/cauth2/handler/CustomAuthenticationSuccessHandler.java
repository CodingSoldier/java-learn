package com.example.cauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 登陆成功处理器
 */
@Slf4j
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    AuthorizationServerTokenServices authorizationServerTokenServices;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        // 校验client_id、client_secret
        String clientIdParam = request.getParameter("client_id");
        String clientSecretParam = request.getParameter("client_secret");
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientIdParam);
        if (clientDetails == null){
            throw new  UnapprovedClientAuthenticationException("client_id错误");
        }

        String clientIdServer = clientDetails.getClientId();
        String clientSecretServer = clientDetails.getClientSecret();
        if (StringUtils.isBlank(clientIdServer) || !clientIdServer.equals(clientIdParam)) {
            throw new UnapprovedClientAuthenticationException("client_id 错误" + clientIdParam);
        }
        if (!passwordEncoder.matches(clientSecretParam, clientSecretServer)) {
            throw new UnapprovedClientAuthenticationException("client_secret 错误" + clientIdParam);
        }

        TokenRequest tokenRequest = new TokenRequest(new HashMap<>(), clientIdParam, clientDetails.getScope(), "custom");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        log.info("登陆成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));

    }
}
