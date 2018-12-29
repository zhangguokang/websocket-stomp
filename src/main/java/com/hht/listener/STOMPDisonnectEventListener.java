/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:STOMPDisonnectEventListener.java
 * Package Name:com.hht.listener
 * Date:2018年11月21日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.listener;

import org.springframework.context.ApplicationListener;

import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author zhangguokang
 *
 * @description 
 */
public class STOMPDisonnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent arg0) {
        String sessionId = arg0.getSessionId();
        // TODO Auto-generated method stub
        
    }

}
