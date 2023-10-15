package com.ecommerce.orderservice.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.model.OrderDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicProducer {

	@Value("${producer.config.topic.name}")
	private String topicName;

	private final KafkaTemplate<String, OrderDto> kafkaTemplate;

	public void send(OrderDto order) {
		log.info("Payload : {}", order.toString());
		kafkaTemplate.send(topicName, order);
	}

}