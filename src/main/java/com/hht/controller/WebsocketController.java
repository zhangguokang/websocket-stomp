/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:WebsocketController.java
 * Package Name:com.hht.controller
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.controller;

import java.security.Principal;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.WebSocketSession;

import com.hht.entity.ClientMessage;
import com.hht.entity.ServerMessage;
import com.hht.session.SocketSessionRegistry;

/**
 * @author zhangguokang
 *
 * @description websocket控制层
 */
@RestController
@RequestMapping("/websocket")
public class WebsocketController {
    private Logger logger = LoggerFactory.getLogger(WebsocketController.class);

    @Autowired
    private SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/sendTest")
    // @SendTo("/topic/subscribeTest")
    public ServerMessage sendDemo(Principal principal, ClientMessage message) {
        String name = principal.getName();
        

        logger.info("接收到了信息:user:" + name + "-------" + message.getName());

        return new ServerMessage("你发送的消息为:" + message.getName());
    }

    @SubscribeMapping("/subscribeTest")
    public ServerMessage sub(Principal principal) {
        String name = principal.getName();

        return new ServerMessage(name + ",感谢你订阅了我。。。");
    }

    /**
     * 广播消息，不指定用户，所有订阅此的用户都能收到消息
     * 
     * @param message
     */
    @MessageMapping("/broadcastShout")
    public void broadcast(ServerMessage message) {
        simpMessageSendingOperations.convertAndSend("/topic/shouts", message);
    }

    /**
     * 给用户发消息
     * 
     * @param message
     * @param stompHeaderAccessor
     */
    @MessageMapping("/singleShout")
    public void singleUser(ServerMessage message, StompHeaderAccessor stompHeaderAccessor) {
        String responseMessage = message.getResponseMessage();
        logger.info("接收到消息：" + responseMessage);
        Principal user = stompHeaderAccessor.getUser();
        simpMessageSendingOperations.convertAndSendToUser(user.getName(), "/queue/shouts", responseMessage);
    }

    /**
     * @param 异常
     * @return
     */
    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public Exception handleExceptions(Exception t) {
        t.printStackTrace();
        return t;
    }

}
