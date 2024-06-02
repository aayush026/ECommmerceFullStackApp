package com.ecomm.ayush.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.CartRepo;
import com.ecomm.ayush.request.AddItemRequest;
import com.ecomm.ayush.service.CartItemService;
import com.ecomm.ayush.service.CartService;
import com.ecomm.ayush.service.ProductService; 

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepo cartRepo;
	private CartItemService cartItemService;
	private ProductService productService;

	public CartServiceImpl(CartRepo cartRepo, CartItemService cartItemService, ProductService productService) {
		this.cartItemService = cartItemService;
		this.cartRepo = cartRepo;
		this.productService = productService;
	}

	@Override
	public Cart createCart(User user) {
		Cart cart = new Cart();
		cart.setUser(user);
		Cart createdCart = cartRepo.save(cart);
		return createdCart;
	}

	@Override
	public CartItem addCartItem(Long userId, AddItemRequest req) throws ProductException {
		System.out.println("CartServiceImpl.addCartItem()");
		Cart cart = cartRepo.findByUserId(userId);
		Product product = productService.findProductById(req.getProductId());
		CartItem isPresent = cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
		System.out.println("isPresent : "+isPresent);
		if (isPresent == null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItem.setQuantity(req.getQuantity());
			cartItem.setUserId(userId);
			int price = req.getQuantity() * product.getDiscountedPrice();
			cartItem.setPrice(price);
			cartItem.setSize(req.getSize());
			CartItem createdCartItem = cartItemService.createCartItem(cartItem);
			cart.getCartItems().add(createdCartItem);
			return createdCartItem;
		}
		return isPresent;
	}

		@Override
		public Cart findUserCart(Long userId) {
			System.out.println("CartServiceImpl.findUserCart() : userId : "+userId);
			Cart cart = cartRepo.findByUserId(userId);
			System.out.println("Cart : "+cart );
			int totalPrice = 0;
			int totalDiscountedPrice = 0;
			int totalItem = 0;
			for (CartItem cartItem : cart.getCartItems()) {
				totalPrice += cartItem.getPrice();
				totalDiscountedPrice = cartItem.getDiscountedPrice();
				totalItem += cartItem.getQuantity();
			}
			cart.setTotalPrice(totalPrice);
			cart.setTotalItem(totalItem);
			cart.setTotalDiscountedPrice(totalDiscountedPrice);
			cart.setDiscounte(totalPrice - totalDiscountedPrice);
			cart.setTotalItem(totalItem);
			return cartRepo.save(cart);
		}
}
