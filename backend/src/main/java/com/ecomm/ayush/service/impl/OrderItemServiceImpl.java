package com.ecomm.ayush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ecomm.ayush.model.OrderItem;
import com.ecomm.ayush.repo.OrderItemRepo;
import com.ecomm.ayush.service.OrderItemService;

public class OrderItemServiceImpl implements OrderItemService{

	@Autowired
	private OrderItemRepo orderItemRepo;
	
	@Override
	public OrderItem createOrderItem(OrderItem orderItem) {
		return orderItemRepo.save(orderItem);
	}
}
