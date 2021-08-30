package com.demo.tutorial;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TutorialRedisService {
	
	private final static String CACHE = "tutorials";
	
	@Autowired
	private ReactiveRedisConnectionFactory factory;
	
	@Autowired
	private ReactiveRedisOperations<String, Tutorial> redisOps;
	
	public Flux<Tutorial> findAll() {
		return redisOps.keys("*")
		        .flatMap(redisOps.opsForValue()::get);
    }
	
	
	
	public Mono<Boolean> addNew(Tutorial tutorial) {
		tutorial.setCratedDate(LocalDateTime.now());
		
		Mono<Boolean> result = redisOps.opsForValue().set(CACHE, tutorial);
		
		return result;
    }


}
