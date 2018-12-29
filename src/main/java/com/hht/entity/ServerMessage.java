/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:ServerMessage.java
 * Package Name:com.hht.entity
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht.entity;

/**
 * @author zhangguokang
 *
 * @description 服务端发送消息实体
 */
public class ServerMessage {
    private String responseMessage;

    public ServerMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
