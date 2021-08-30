package com.demo.greeting;


public class Greeting {

	private String message;
	private String id;

	public Greeting() {
	}

	public Greeting(String id, String message) {
		this.id = id;
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Greeting [message=" + message + ", id=" + id + "]";
	}

	
}
