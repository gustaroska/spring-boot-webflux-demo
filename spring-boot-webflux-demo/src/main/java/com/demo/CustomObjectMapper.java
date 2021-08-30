package com.demo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper extends ObjectMapper {


	
	public CustomObjectMapper() {
		// TODO Auto-generated constructor stub
		
		
		setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// Solve the problem that jackson2 cannot deserialize LocalDateTime
		disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		registerModule(new JavaTimeModule());
		activateDefaultTyping(getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
	}
}
