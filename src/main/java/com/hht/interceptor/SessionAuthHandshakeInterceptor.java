/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:SessionAuthHandshakeInterceptor.java
 * Package Name:com.hht.interceptor
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.interceptor;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author zhangguokang
 *
 * @description 连接时验证用户是否登录
 */
public class SessionAuthHandshakeInterceptor implements HandshakeInterceptor {
    private Logger logger = LoggerFactory.getLogger(SessionAuthHandshakeInterceptor.class);

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Exception e) {
        logger.info("afterHandshake");

    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler handler, Map<String, Object> object) throws Exception {

        logger.info("beforeHandshake");
       
        
        return true;
    }

}
