/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:WebSocketStompConfig.java
 * Package Name:com.hht.config
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.config;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.hht.interceptor.SessionAuthHandshakeInterceptor;

/**
 * @author zhangguokang
 *
 * @description springboot websocket stomp配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig extends AbstractWebSocketMessageBrokerConfigurer {
    private Logger logger = LoggerFactory.getLogger(WebSocketStompConfig.class);
    private static long HEART_BEAT = 5000;

    /**
     * 注册stomp的端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 允许使用socketJs方式访问，访问点为webSocketServer，允许跨域
        // 在网页上我们就可以通过这个链接
        // http://localhost:8080/webSocketServer
        // 来和服务器的WebSocket连接
        registry.addEndpoint("/webSocketServer").addInterceptors(new SessionAuthHandshakeInterceptor()).setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置信息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 心跳定时任务
        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();

        // 订阅Broker名称
        registry.enableSimpleBroker("/queue", "/topic").setHeartbeatValue(new long[] { HEART_BEAT, HEART_BEAT }).setTaskScheduler(te);

        // 全局使用的消息前缀（客户端订阅路径上会体现出来）
        registry.setApplicationDestinationPrefixes("/app");
        // 点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        // registry.setUserDestinationPrefix("/user/");
    }

    @Override
    public void configureWebSocketTransport(final WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {

                        // 客户端与服务器端建立连接后，此处记录谁上线了
                        // String username = session.getPrincipal().getName();
                        // System.out.println("online: " + username);
                        logger.info("ConnectionEstablished");
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

                        // 客户端与服务器端断开连接后，此处记录谁下线了
                        // String username = session.getPrincipal().getName();
                        // System.out.println("offline: " + username);
                        logger.info("ConnectionClosed");
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
        registration.setSendTimeLimit(15 * 1000).setSendBufferSizeLimit(512 * 1024);
    }

    /**
     * 1、设置拦截器 2、首次连接的时候，获取其Header信息，利用Header里面的信息进行权限认证 3、通过认证的用户，使用
     * accessor.setUser(user); 方法，将登陆信息绑定在该 StompHeaderAccessor
     * 上，在Controller方法上可以获取 StompHeaderAccessor 的相关信息
     * 
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // 1、判断是否首次连接
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // 2、判断用户名和密码
                    String username = accessor.getNativeHeader("username").get(0);
                    String password = accessor.getNativeHeader("password").get(0);

                    if ("admin".equals(username) && "admin".equals(password)) {
                        Principal principal = new Principal() {
                            @Override
                            public String getName() {
                                return username;
                            }
                        };
                        accessor.setUser(principal);
                        return message;
                    } else {
                        return null;
                    }
                }
                // 不是首次连接，已经登陆成功
                return message;
            }

        });

        registration.taskExecutor().corePoolSize(32).maxPoolSize(200).queueCapacity(10000);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {

        registration.taskExecutor().corePoolSize(100).maxPoolSize(400).queueCapacity(20000);
    }

}
