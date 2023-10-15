package com.ecommerce.orderservice.messaging;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.model.PaymentDto;
import com.ecommerce.orderservice.model.PaymentStatus;
import com.ecommerce.orderservice.service.PaymentManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TopicListener {

	@Value("${consumer.config.topic.name")
	private String topicName;

	@Autowired
	PaymentManagementService paymentManagementService;

	@KafkaListener(id = "${consumer.config.topic.name}", topics = "${consumer.config.topic.name}", groupId = "${consumer.config.group-id}")
	public void consume(ConsumerRecord<String, PaymentDto> payload) {
		log.info("Topic : {}", topicName);
		log.info("Key : {}", payload.key());
		log.info("Headers : {}", payload.headers());
		log.info("Partion : {}", payload.partition());
		log.info("Payment : {}", payload.value());

		PaymentDto payment = payload.value();

		if (payment.getStatus().equals(PaymentStatus.COMPLETED)) {
			paymentManagementService.completeOrder(payment);
		} else if (payment.getStatus().equals(PaymentStatus.FAILED)
				|| payment.getStatus().equals(PaymentStatus.PENDING)) {
			paymentManagementService.updateOrder(payment);
		}

	}

}