package com.ecomm.ayush.service.impl;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ecomm.ayush.exception.CartItemException;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.CartItemRepo;
import com.ecomm.ayush.service.CartItemService;
import com.ecomm.ayush.service.UserService;

@Service
public class CartItemServiceImpl implements CartItemService {

	private CartItemRepo cartItemRepo;
	private UserService userService;

	public CartItemServiceImpl(CartItemRepo cartItemRepo, UserService userService) {
		this.cartItemRepo = cartItemRepo;
		this.userService = userService;
	}

	@Override
	public CartItem createCartItem(CartItem cartItem) {
		cartItem.setQuantity(1);
		cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
		cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
		CartItem createdCartItem = cartItemRepo.save(cartItem);
		return createdCartItem;
	}

	@Override
	public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
		CartItem item = findCartItemById(id);
		User user = userService.findUserById(item.getUserId());
		if (user.getId().equals(userId)) {
			item.setQuantity(cartItem.getQuantity());
			item.setPrice(item.getQuantity() * item.getProduct().getPrice());
			item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
			return cartItemRepo.save(item);
		} else {
			throw new CartItemException("You can't update  another users cart_item");
		}
	}

	@Override
	public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
		CartItem cartItem = cartItemRepo.isCartItemExist(cart, product, size, userId);
		return cartItem;
	}

	@Override
	public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
		CartItem cartItem = findCartItemById(cartItemId);
		User user = userService.findUserById(cartItem.getUserId());
		User reqUser = userService.findUserById(userId);
		if (user.getId().equals(reqUser.getId())) {
			cartItemRepo.deleteById(cartItem.getId());
		} else {
			throw new UserException("you can't remove anothor users item");
		}
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) throws CartItemException {
		Optional<CartItem> opt = cartItemRepo.findById(cartItemId);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new CartItemException("cartItem not found with id : " + cartItemId);
	}
}
