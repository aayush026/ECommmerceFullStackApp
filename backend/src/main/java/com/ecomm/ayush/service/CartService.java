package com.ecomm.ayush.service;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public CartItem addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

}
