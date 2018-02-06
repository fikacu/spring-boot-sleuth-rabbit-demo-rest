package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
//@formatter:off
@RabbitListener(bindings = {
		@QueueBinding(
				value = @Queue(value = MyMessage.QUEUE, autoDelete = "true"), 
				exchange = @Exchange(value = MyMessage.RABBITMQ_EXCHANGE, autoDelete = "true", type = "direct"), 
				key = MyMessage.KEY)}
)
//@formatter:on
public class MyMessageHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyMessageHandler.class);

	@Autowired
	private RestTemplate restTemplate;

	@RabbitHandler
	public void onMessage(MyMessage msg) {
		LOGGER.info("got a message: {}. Testing rest", msg.getGreeting());

		restTemplate.getForEntity("http://localhost:8888/test-rest", String.class);
	}
}
