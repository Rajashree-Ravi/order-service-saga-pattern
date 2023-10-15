package com.ecommerce.orderservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ecommerce.orderservice.entity.Item;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.service.ItemService;
import com.ecommerce.orderservice.service.OrderService;
import com.ecommerce.orderservice.exception.EcommerceException;
import com.ecommerce.orderservice.model.ItemDto;
import com.ecommerce.orderservice.model.OrderDto;
import com.ecommerce.orderservice.model.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ItemService itemService;

	@Autowired
	private ModelMapper mapper;

	@Override
	public List<OrderDto> getAllOrders() {
		List<OrderDto> orders = new ArrayList<>();
		orderRepository.findAll().forEach(order -> {

			orders.add(mapper.map(order, OrderDto.class));
		});
		return orders;
	}

	@Override
	public OrderDto getOrderById(long id) {
		Optional<Order> order = orderRepository.findById(id);
		return (order.isPresent() ? mapper.map(order.get(), OrderDto.class) : null);
	}

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		Order order = mapper.map(orderDto, Order.class);
		return mapper.map(orderRepository.save(order), OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(long id, OrderDto orderDto) {

		Optional<Order> updatedOrder = orderRepository.findById(id).map(existingOrder -> {

			List<ItemDto> savedItems = new ArrayList<>();

			// Create new items added to order
			for (ItemDto itemDto : orderDto.getItems()) {
				if (itemDto.getId() == null) {
					savedItems.add(itemService.createItem(itemDto));
				} else {
					Item existingItem = existingOrder.getItems().stream().filter(x -> x.getId() == itemDto.getId())
							.findAny().orElse(null);

					if (existingItem == null || existingItem.getId() == null)
						savedItems.add(itemService.createItem(itemDto));
					else
						savedItems.add(itemService.updateItem(itemDto.getId(), itemDto));
				}

			}

			orderDto.setItems(savedItems);

			// Delete items that have been removed from order
			for (Item item : existingOrder.getItems()) {
				ItemDto existingItem = orderDto.getItems().stream().filter(x -> x.getId() == item.getId()).findAny()
						.orElse(null);

				if (existingItem != null && existingItem.getId() != null)
					itemService.deleteItem(existingItem.getId());
			}

			Order order = mapper.map(orderDto, Order.class);
			return orderRepository.save(existingOrder.updateWith(order));
		});

		return (updatedOrder.isPresent() ? mapper.map(updatedOrder.get(), OrderDto.class) : null);
	}

	@Override
	public void deleteOrder(long id) {

		OrderDto orderDto = getOrderById(id);

		if (orderDto == null || orderDto.getId() == null)
			throw new EcommerceException("order-not-found", String.format("Order with id=%d not found", id),
					HttpStatus.NOT_FOUND);

		// Do not delete an order if status is - INTRANSIT, PAYMENTDUE, PICKUPAVAILABLE,
		// PROCESSING
		if (orderDto.getStatus().equals(OrderStatus.PLACED) || orderDto.getStatus().equals(OrderStatus.PAID))
			throw new EcommerceException("order-status-active", "Current Status of the Order with id = " + id + " is "
					+ orderDto.getStatus() + ", Hence it cannot be deleted.", HttpStatus.NOT_FOUND);

		// Remove the items in order before deleting the order
		for (ItemDto itemDto : orderDto.getItems()) {
			itemService.deleteItem(itemDto.getId());
		}

		orderRepository.deleteById(id);
		LOGGER.info("Order deleted Successfully");
	}

}
