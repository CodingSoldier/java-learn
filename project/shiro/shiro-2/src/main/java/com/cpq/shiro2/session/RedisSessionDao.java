package com.cpq.shiro2.session;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-01-10
 */
@Component
public class RedisSessionDao extends AbstractSessionDAO {

    @Autowired
    RedisTemplate redisTemplate;

    private final String SHIRO_SESSION_PREFIX = "shiro-session:";

    /**
     * 保存Session
     * @param session
     */
    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            String key = SHIRO_SESSION_PREFIX + session.getId().toString();
            redisTemplate.opsForValue().set(key, session);
            redisTemplate.expire(key, 1, TimeUnit.HOURS);
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session");
        if (sessionId == null) {
            return null;
        }
        String key = SHIRO_SESSION_PREFIX + sessionId.toString();
        return (SimpleSession) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        String key = SHIRO_SESSION_PREFIX + session.getId().toString();
        redisTemplate.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisTemplate.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<>();
        if (CollectionUtils.isEmpty(keys)) {
            return sessions;
        }
        for (String key : keys) {
            Session session = (SimpleSession) redisTemplate.opsForValue().get(key);
            sessions.add(session);
        }
        return sessions;
    }
}
