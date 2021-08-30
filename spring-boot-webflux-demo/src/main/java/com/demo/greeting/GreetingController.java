package com.demo.greeting;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.kafka.KafkaConfiguration;
import com.demo.student.Student;

import reactor.core.publisher.Flux;

@RestController
public class GreetingController {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ReactiveRedisOperations<String, Greeting> greetingOps;

	GreetingController(ReactiveRedisOperations<String, Greeting> greetingOps) {
		this.greetingOps = greetingOps;
	}

	@GetMapping("/greetings")
	public Flux<Greeting> all() {
		return greetingOps.keys("*").flatMap(greetingOps.opsForValue()::get);
	}

	@GetMapping(path = "/stream-greetings", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Greeting> streamFlux() {
	    return Flux.interval(Duration.ofSeconds(1))
	      .map(sequence -> new Greeting(UUID.randomUUID().toString(), LocalDateTime.now().toString()));
	}
	
	//@KafkaListener(topics = GreetingKafkaConfig.TOPIC, groupId = "group_id")
    public void consume(Student message) throws IOException {
         logger.info(String.format("#### -> Consumed message -> %s", message));
    }
}
