/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:SocketSessionRegistry.java
 * Package Name:com.hht.session
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;


import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author zhangguokang
 *
 * @description 用户session记录类
 */
@Component
public class SocketSessionRegistry {
    // this map save every session
    // 这个集合存储session
    private final ConcurrentMap<String, Set<String>> userSessionIds = new ConcurrentHashMap();
    private final Object lock = new Object();

    public SocketSessionRegistry() {
    }

    /**
     *
     * 获取sessionId
     * 
     * @param user
     * @return
     */
    public Set<String> getSessionIds(String user) {
        Set set = (Set) this.userSessionIds.get(user);
        return set != null ? set : Collections.emptySet();
    }

    /**
     * 获取所有session
     * 
     * @return
     */
    public ConcurrentMap<String, Set<String>> getAllSessionIds() {
        return this.userSessionIds;
    }

    /**
     * register session
     * 
     * @param user
     * @param sessionId
     */
    public void registerSessionId(String userName, String sessionId) {
        // Assert.assertNotNull(user, "User must not be null");
        // Assert.assertNotNull(sessionId, "Session ID must not be null");
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(sessionId)) {
            System.out.println("User or sessionId must not be null");
        }
        Object var3 = this.lock;
        synchronized (this.lock) {
            Object set = (Set) this.userSessionIds.get(userName);
            if (set == null) {
                set = new CopyOnWriteArraySet();
                this.userSessionIds.put(userName, (Set<String>) set);
            }

            ((Set) set).add(sessionId);
        }
    }

    public void unregisterSessionId(String userName, String sessionId) {
        // Assert.assertNotNull(userName, "User Name must not be null");
        // Assert.assertNotNull(sessionId, "Session ID must not be null");
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(sessionId)) {
            System.out.println("User or sessionId must not be null");
        }
        Object var3 = this.lock;
        synchronized (this.lock) {
            Set set = (Set) this.userSessionIds.get(userName);
            if (set != null && set.remove(sessionId) && set.isEmpty()) {
                this.userSessionIds.remove(userName);
            }

        }
    }
}
