/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:STOMPConnectEventListener.java
 * Package Name:com.hht.listener
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import com.hht.session.SocketSessionRegistry;



/**
 * @author zhangguokang
 *
 * @description STOMP监听类 用于session注册 以及key值获取
 */
@Component
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {
    private Logger logger = LoggerFactory.getLogger(STOMPConnectEventListener.class);

    @Autowired
    private SocketSessionRegistry webAgentSessionRegistry;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        //StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        // login get from browser
        //String agentId = sha.getNativeHeader("user").get(0);
        //logger.info("agentId:"+agentId);
        //String sessionId = sha.getSessionId();
        //logger.info("sessionId:"+sessionId);
        //webAgentSessionRegistry.registerSessionId(agentId, sessionId);
    }
    
}
