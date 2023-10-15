package com.ecommerce.orderservice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.model.OrderDto;
import com.ecommerce.orderservice.model.OrderStatus;
import com.ecommerce.orderservice.model.PaymentDto;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.service.PaymentManagementService;

@Service
public class PaymentManagementServiceImpl implements PaymentManagementService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentManagementServiceImpl.class);

	@Autowired
	private OrderService orderService;

	/**
	 * process order
	 * 
	 * @param payment
	 */
	@Override
	public OrderDto completeOrder(PaymentDto payment) {
		OrderDto order = orderService.getOrderById(payment.getOrderId());
		order.setStatus(OrderStatus.PAID);
		return order;
	}

	/**
	 * process rollback
	 * 
	 * @param payment
	 */
	@Override
	public OrderDto updateOrder(PaymentDto payment) {
		OrderDto order = orderService.getOrderById(payment.getOrderId());
		order.setStatus(OrderStatus.FAILED);
		
		// Revert the stocks in inventory
		LOGGER.info("Stocks updated");
		
		return order;
	}

}
