package com.ecomm.ayush.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.ayush.model.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {

}
