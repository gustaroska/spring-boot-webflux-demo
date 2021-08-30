package com.demo.websocket;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Component
class WebSocketCreatedEventPublisher implements
    ApplicationListener<WebSocketCreatedEvent>, // <1>
    Consumer<FluxSink<WebSocketCreatedEvent>> { //<2>

    private final Executor executor;
    private final BlockingQueue<WebSocketCreatedEvent> queue =
        new LinkedBlockingQueue<>(); // <3>

    WebSocketCreatedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    // <4>
    @Override
    public void onApplicationEvent(WebSocketCreatedEvent event) {
        this.queue.offer(event);
    }

     @Override
    public void accept(FluxSink<WebSocketCreatedEvent> sink) {
        this.executor.execute(() -> {
            while (true)
                try {
                    WebSocketCreatedEvent event = queue.take(); // <5>
                    sink.next(event); // <6>
                }
                catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
        });
    }
}
