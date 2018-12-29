/**
 * Project Name:hht-backend-communication-websocket-test
 * File Name:WebsocketApplication.java
 * Package Name:com.hht
 * Date:2018年11月20日
 * Copyright (c) 2018 深圳市鸿合创新信息技术 Inc.All Rights Reserved.
 */
package com.hht;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author zhangguokang
 *
 * @description
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })

public class WebsocketApplication {
    /**
     * @param args
     */
    public static void main(String[] args) {

        SpringApplication.run(WebsocketApplication.class, args);
    }
}
