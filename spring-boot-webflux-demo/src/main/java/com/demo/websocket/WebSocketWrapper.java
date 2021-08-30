package com.demo.websocket;

import java.time.LocalDateTime;

public class WebSocketWrapper<T extends Object> {
	
	private LocalDateTime timestamp;
	private T body;
	
	public WebSocketWrapper(T body) {
		// TODO Auto-generated constructor stub
		this.body = body;
	}
	
	public LocalDateTime getTimestamp() {
		return LocalDateTime.now();
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

}
