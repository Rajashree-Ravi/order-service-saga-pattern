package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.model.OrderDto;
import com.ecommerce.orderservice.model.PaymentDto;

public interface PaymentManagementService {

	OrderDto completeOrder(PaymentDto payment);

	OrderDto updateOrder(PaymentDto payment);

}
