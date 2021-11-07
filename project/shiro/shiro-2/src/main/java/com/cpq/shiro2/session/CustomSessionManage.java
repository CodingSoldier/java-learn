package com.cpq.shiro2.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.io.Serializable;

/**
 * @author chengchenrui
 * @version Id: CustomSessionManage.java, v 0.1 2018/6/29 1:25 chengchenrui Exp $$
 */
@Component
public class CustomSessionManage extends DefaultWebSessionManager {

    @Resource(name = "redisSessionDao")
    protected SessionDAO sessionDAO;

    @PostConstruct
    public void init() {
        super.setSessionDAO(this.sessionDAO);
    }

    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        if (request != null && sessionId != null) {
            Session session = (Session) request.getAttribute(sessionId.toString());
            if (session != null) {
                return session;
            }
        }
        Session session = super.retrieveSession(sessionKey);
        if (request != null && sessionId != null) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}