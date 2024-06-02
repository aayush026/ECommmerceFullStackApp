package com.ecomm.ayush.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecomm.ayush.domain.OrderStatus;
import com.ecomm.ayush.domain.PaymentStatus;
import com.ecomm.ayush.exception.OrderException;
import com.ecomm.ayush.model.Address;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.Order;
import com.ecomm.ayush.model.OrderItem;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.AddressRepo;
import com.ecomm.ayush.repo.OrderItemRepo;
import com.ecomm.ayush.repo.OrderRepo;
import com.ecomm.ayush.repo.UserRepo;
import com.ecomm.ayush.service.CartService;
import com.ecomm.ayush.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepo orderRepository;
	@Autowired
	private CartService cartService;
	@Autowired
	private AddressRepo addressRepository;
	@Autowired
	private UserRepo userRepository;
	@Autowired
	private OrderItemRepo orderItemRepository;

	@Override
	public Order createOrder(User user, Address shippAddress) {
		shippAddress.setUser(user);
		System.out.println("*****************");
		System.out.println("Get here");
		Address address = addressRepository.save(shippAddress);
		System.out.println("OrderServiceImpl.createOrder()");
		user.getAddresses().add(address);
		userRepository.save(user);
		Cart cart = cartService.findUserCart(user.getId());
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem item : cart.getCartItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setPrice(item.getPrice());
			orderItem.setProduct(item.getProduct());
			orderItem.setQuantity(item.getQuantity());
			orderItem.setSize(item.getSize());
			orderItem.setUserId(item.getUserId());
			orderItem.setDiscountedPrice(item.getDiscountedPrice());
			OrderItem createdOrderItem = orderItemRepository.save(orderItem);
			orderItems.add(createdOrderItem);
		}
		Order createdOrder = new Order();
		createdOrder.setUser(user);
		createdOrder.setOrderItems(orderItems);
		createdOrder.setTotalPrice(cart.getTotalPrice());
		createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
		createdOrder.setDiscounte(cart.getDiscounte());
		createdOrder.setTotalItem(cart.getTotalItem());
		createdOrder.setShippingAddress(address);
		createdOrder.setOrderDate(LocalDateTime.now());
		createdOrder.setOrderStatus(OrderStatus.PENDING);
		createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
		createdOrder.setCreatedAt(LocalDateTime.now());
		Order savedOrder = orderRepository.save(createdOrder);
		orderItems.forEach(orderItem -> {
			orderItem.setOrder(savedOrder);
			orderItemRepository.save(orderItem);
		});
		return savedOrder;
	}

	@Override
	public Order findOrderById(Long orderId) throws OrderException {
		// Optional<Order> order=orderRepository.findById(orderId);
		// if(order.isPresent()) {
		// return order.get();
		// }
		// throw new OrderException("Order does not exist : "+orderId);
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new OrderException("Order does not exist : " + orderId));

	}

	@Override
	public List<Order> usersOrderHistory(Long userId) {
		// List<Order> orders=orderRepository.getUsersOrders(userId);
		// return orders;
		return orderRepository.getUsersOrders(userId);
	}

	private Order updateOrderStatus(Long orderId, OrderStatus status) throws OrderException {
		Order order = findOrderById(orderId);
		order.setOrderStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public Order placedOrder(Long orderId) throws OrderException {
		// Order order=findOrderById(orderId);
		// order.setOrderStatus(OrderStatus.PLACED);
		// order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		// return order;
		Order order = updateOrderStatus(orderId, OrderStatus.PLACED);
		order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED);
		return order;
	}

	@Override
	public Order confirmedOrder(Long orderId) throws OrderException {
		// Order order=findOrderById(orderId);
		// order.setOrderStatus(OrderStatus.CONFIRMED);
		// return orderRepository.save(order);
		return updateOrderStatus(orderId, OrderStatus.CONFIRMED);
	}

	@Override
	public Order shippedOrder(Long orderId) throws OrderException {
		return updateOrderStatus(orderId, OrderStatus.SHIPPED);
	}

	@Override
	public Order deliveredOrder(Long orderId) throws OrderException {
		return updateOrderStatus(orderId, OrderStatus.DELIVERED);
	}

	@Override
	public Order cancledOrder(Long orderId) throws OrderException {
		return updateOrderStatus(orderId, OrderStatus.CANCELLED);
	}

	@Override
	public List<Order> getAllOrders() {
		return orderRepository.findAllByOrderByCreatedAtDesc();
	}

	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Order order = findOrderById(orderId);
		orderRepository.deleteById(order.getId());
	}

}
