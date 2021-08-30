package com.demo.greeting;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;

@Component
public class GreetingLoader {
  private final ReactiveRedisConnectionFactory factory;
  private final ReactiveRedisOperations<String, Greeting> greetingOps;

  public GreetingLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Greeting> greetingOps) {
    this.factory = factory;
    this.greetingOps = greetingOps;
  }

  /*
  @PostConstruct
  public void loadData() {
    factory.getReactiveConnection().serverCommands().flushAll().thenMany(
        Flux.just("Jet Black Redis", "Darth Redis", "Black Alert Redis")
            .map(name -> new Greeting(UUID.randomUUID().toString(), name))
            .flatMap(greeting -> greetingOps.opsForValue().set(greeting.getId(), greeting)))
        .thenMany(greetingOps.keys("*")
            .flatMap(greetingOps.opsForValue()::get))
        .subscribe(System.out::println);
  }
  */
}