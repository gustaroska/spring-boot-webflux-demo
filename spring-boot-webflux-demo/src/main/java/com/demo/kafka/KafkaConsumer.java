package com.demo.kafka;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.demo.CustomObjectMapper;
import com.demo.student.Student;
import com.demo.websocket.WebSocketCreatedEvent;
import com.demo.websocket.WebSocketWrapper;

@Service
public class KafkaConsumer {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ApplicationEventPublisher publisher;

	@KafkaListener(topics = KafkaConfiguration.TOPIC, groupId = KafkaConfiguration.GROUP_ID, containerFactory = "kafkaListenerContainerFactory")
    public void consume(String message) throws IOException {
         logger.info(String.format("#### -> Consumed message -> %s", message));
         
         CustomObjectMapper com = new CustomObjectMapper();
         Student student = com.readValue(message, Student.class);
         
         this.publisher.publishEvent(new WebSocketCreatedEvent(new WebSocketWrapper<>(student)));
         
    }
	
}
