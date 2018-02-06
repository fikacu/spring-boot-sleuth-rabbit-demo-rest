package com.example.demo;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(final ConnectionFactory connectionFactory) {
		final SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
		containerFactory.setConnectionFactory(connectionFactory);
		return containerFactory;
	}

	/**
	 * Creates a converter in order for the RabbitListener to receive a Json object.
	 */
	@Bean
	public MessageConverter jacksonConverter() {
		final Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
		converter.setCreateMessageIds(true);
		return converter;
	}

	@Bean
	public DefaultMessageHandlerMethodFactory handlerMethodFactory() {
		return new DefaultMessageHandlerMethodFactory();
	}

}
