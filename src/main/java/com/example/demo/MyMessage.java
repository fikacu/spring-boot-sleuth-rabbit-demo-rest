package com.example.demo;

public class MyMessage {

	public static final String RABBITMQ_EXCHANGE = "exch.common";
	public static final String QUEUE = "q.msg";
	public static final String KEY = "k.msg";

	private String greeting;

	public MyMessage() {
	}

	public MyMessage(String greeting) {
		this.greeting = greeting;
	}

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

}
