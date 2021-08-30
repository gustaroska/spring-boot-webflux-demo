package com.demo.websocket;


import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;

@Log4j2
@Configuration
class WebSocketConfiguration {

    // <1>
    @Bean
    Executor executorWebSocketWrapper() {
        return Executors.newSingleThreadExecutor();
    }

    // <2>
    @Bean
    HandlerMapping handlerMappingWebSocketWrapper(WebSocketHandler wsh) {
        return new SimpleUrlHandlerMapping() {
            {
                // <3>
                setUrlMap(Collections.singletonMap("/ws/events", wsh));
                setOrder(10);
            }
        };
    }

    // <4>
    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapterWebSocketWrapper() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    WebSocketHandler webSocketHandlerWebSocketWrapper(
        ObjectMapper objectMapper, // <5>
        WebSocketCreatedEventPublisher eventPublisher // <6>
    ) {

        Flux<WebSocketCreatedEvent> publish = Flux.create(eventPublisher).share(); // <7>

        return session -> {

            Flux<WebSocketMessage> messageFlux = publish.map(evt -> {
                try {
                    WebSocketWrapper wrapper = (WebSocketWrapper) evt.getSource(); // <1>
                    return objectMapper.writeValueAsString(wrapper); // <3>
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }).map(str -> {
                log.info("sending " + str);
                return session.textMessage(str);
            });

            return session.send(messageFlux);
        };
    }

}
