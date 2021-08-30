package com.demo.greeting;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GreetingHandler {

	public Mono<ServerResponse> hello(ServerRequest request) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
			.body(BodyInserters.fromValue(new Greeting(UUID.randomUUID().toString(),"Hello, Spring!")));
	}
	
    public Mono<ServerResponse> helloStream(ServerRequest request) {
    	
    	return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromServerSentEvents(Flux.create(sink -> { LocalDateTime.now();})));
    	
    }
}
