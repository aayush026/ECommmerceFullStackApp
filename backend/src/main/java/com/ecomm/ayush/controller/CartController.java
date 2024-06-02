package com.ecomm.ayush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.request.AddItemRequest;
import com.ecomm.ayush.response.APIResponse;
import com.ecomm.ayush.service.CartService;
import com.ecomm.ayush.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;

	public CartController(CartService cartService, UserService userService) {
		this.cartService = cartService;
		this.userService = userService;
	}

	@GetMapping("/")
	public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
		System.out.println("CartController.findUserHandler()");
		User user = userService.findUserProfileByJWT(jwt);
		System.out.println("user : "+user);
		Cart cart = cartService.findUserCart(user.getId());
		System.out.println("cart : "+cart);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}

	@PutMapping("/add")
	public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
			@RequestHeader("Authorization") String jwt) throws UserException, ProductException {
		User user = userService.findUserProfileByJWT(jwt);
		CartItem cartItem = cartService.addCartItem(user.getId(), req);
		APIResponse res = new APIResponse("Item Added To Cart", true);
		return new ResponseEntity<CartItem>(cartItem, HttpStatus.CREATED);
	}
	
//	@DeleteMapping("/clearCart/{cartId}")
//	public ResponseEntity<APIResponse> deleteItemFromCart(){
//		
//		
//		return null;
//	}
	
}
