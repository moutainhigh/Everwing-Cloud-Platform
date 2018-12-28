package com.everwing.server.wy.web.socket;/**
 * Created by wust on 2018/8/22.
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * Function:Configure Spring for STOMP messaging
 * Reason:
 *      WebSocketConfig is annotated with @Configuration to indicate that it is a Spring configuration class.
 *      It is also annotated @EnableWebSocketMessageBroker.
 *      As its name suggests, @EnableWebSocketMessageBroker enables WebSocket message handling, backed by a message broker.
 * Date:2018/8/22
 *
 * @author wusongti@lii.com.cn
 */
@Configuration
@EnableWebSocketMessageBroker
public class WyServerWebSocketMessageBrokerConfigurer extends AbstractWebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/sockjs/wyServerWebSocketServer").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry messageBrokerRegistry) {
        /**
         *  It starts by calling enableSimpleBroker() to enable a simple memory-based message broker to carry the login messages back to the client on destinations prefixed with "/topic/loginForScanCode".
         */
        messageBrokerRegistry.enableSimpleBroker("/topic");

        /**
         *  This prefix will be used to define all the message mappings;
         *  for example, "/wy/loginForScanCode" is the endpoint that the LoginController.loginForScanCode() method is mapped to handle.
         */
        messageBrokerRegistry.setApplicationDestinationPrefixes("/wy");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
