package com.demo.student;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.demo.CustomObjectMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class StudentRedisConfiguration {
	
	@Bean
	ReactiveRedisOperations<String, Student> redisStudentOperations(ReactiveRedisConnectionFactory factory) {
		
		Jackson2JsonRedisSerializer<Student> serializer = new Jackson2JsonRedisSerializer<>(Student.class);
		/*
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// Solve the problem that jackson2 cannot deserialize LocalDateTime
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		om.registerModule(new JavaTimeModule());
		om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		*/
		
		serializer.setObjectMapper(new CustomObjectMapper());
		
		RedisSerializationContext.RedisSerializationContextBuilder<String, Student> builder = RedisSerializationContext
				.newSerializationContext(new StringRedisSerializer());

		RedisSerializationContext<String, Student> context = builder.value(serializer).build();

		return new ReactiveRedisTemplate<>(factory, context);
	}


}
