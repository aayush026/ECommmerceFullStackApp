package com.ecomm.ayush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.ayush.exception.OrderException;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.Address;
import com.ecomm.ayush.model.Order;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.service.OrderService;
import com.ecomm.ayush.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

	@PostMapping("/")
	public ResponseEntity<Order> createOrderHandler(@Valid @RequestBody Address shippingAddress,
	        @RequestHeader("Authorization") String jwt) throws UserException {
	    User user = userService.findUserProfileByJWT(jwt);
	    Order order = orderService.createOrder(user, shippingAddress);
	    return new ResponseEntity<>(order, HttpStatus.OK);
	}


	@GetMapping("/user")
	public ResponseEntity<List<Order>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt)
			throws OrderException, UserException {
		User user = userService.findUserProfileByJWT(jwt);
		List<Order> orders = orderService.usersOrderHistory(user.getId());
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> findOrderHandler(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws OrderException, UserException {
		User user = userService.findUserProfileByJWT(jwt);
		Order orders = orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
}
