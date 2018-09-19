package com.zorkdata.center.admin.config;

import com.zorkdata.center.admin.rpc.WsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @description: ${todo}
 * @author: zhuzhigang
 * @create: 2018/4/20 14:36
 */
@Configuration
@EnableWebSocket
public class WsConfigure implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("==========================");
        registry.addHandler(myHandler(), "/websocket").setAllowedOrigins("*");
    }

    @Bean
    public WsHandler myHandler() {
        return new WsHandler();
    }
}