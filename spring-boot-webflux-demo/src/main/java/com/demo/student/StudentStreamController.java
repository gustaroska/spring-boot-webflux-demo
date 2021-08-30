package com.demo.student;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/stream/students")
public class StudentStreamController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	final Sinks.Many<Student> sink;
	
	public StudentStreamController() {
		// TODO Auto-generated constructor stub
		this.sink = Sinks.many().multicast().onBackpressureBuffer();
	}
	
    
	@GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Student>> stream() throws IOException {
		return sink.asFlux().map(e -> ServerSentEvent.builder(e).build());
    }
	
//	@KafkaListener(topics = StudentKafkaConfig.TOPIC, groupId = "group_id", containerFactory = "kafkaStudentListenerContainerFactory")
    public void consume(Student message) throws IOException {
         logger.info(String.format("#### -> Consumed message -> %s", message));
         
         /*
         EmitResult result = sink.tryEmitNext(message);

         if (result.isFailure()) {
           // do something here, since emission failed
         }
         */
    }
}
