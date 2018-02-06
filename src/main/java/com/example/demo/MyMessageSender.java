package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

@Component
public class MyMessageSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyMessageSender.class);

	private final AmqpTemplate amqpTemplate;

	public MyMessageSender(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	public void send(MyMessage msg) {
		LOGGER.info("just about to send");
		amqpTemplate.convertAndSend(MyMessage.RABBITMQ_EXCHANGE, MyMessage.KEY, msg);
	}

}
