package com.demo.websocket;

import org.springframework.context.ApplicationEvent;

public class WebSocketCreatedEvent extends ApplicationEvent {

    public WebSocketCreatedEvent(WebSocketWrapper message) {
        super(message);
    }
}
