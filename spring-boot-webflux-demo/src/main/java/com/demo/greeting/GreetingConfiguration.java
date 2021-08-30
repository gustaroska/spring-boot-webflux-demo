package com.demo.greeting;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class GreetingConfiguration {

	@Bean
	  ReactiveRedisOperations<String, Greeting> redisOperations(ReactiveRedisConnectionFactory factory) {
	    Jackson2JsonRedisSerializer<Greeting> serializer = new Jackson2JsonRedisSerializer<>(Greeting.class);

	    RedisSerializationContext.RedisSerializationContextBuilder<String, Greeting> builder =
	        RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

	    RedisSerializationContext<String, Greeting> context = builder.value(serializer).build();

	    return new ReactiveRedisTemplate<>(factory, context);
	  }
	
}
