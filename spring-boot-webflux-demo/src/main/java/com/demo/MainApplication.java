package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.resource.ResourceWebHandler;

@SpringBootApplication
@EnableR2dbcRepositories
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
		
		
		/*
		ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
		GreetingClient greetingClient = context.getBean(GreetingClient.class);
		// We need to block for the content here or the JVM might exit before the message is logged
		System.out.println(">> message = " + greetingClient.getMessage().block());
		
		*/
		
		
	}
	
	

}
