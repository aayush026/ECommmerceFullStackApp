package com.ecomm.ayush.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecomm.ayush.exception.CartItemException;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.CartItem;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.response.APIResponse;
import com.ecomm.ayush.service.CartItemService;
import com.ecomm.ayush.service.UserService;

//import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/cart_items")
//@Tag(name="Cart Item Management", description = "create cart item delete cart item")
public class CartItemController {

	private CartItemService cartItemService;
	private UserService userService;
	
	@DeleteMapping("/{cartItemId}")
	public ResponseEntity<APIResponse>deleteCartItemHandler(@PathVariable Long cartItemId, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		User user=userService.findUserProfileByJWT(jwt);
		System.out.println("user : "+user);
		cartItemService.removeCartItem(user.getId(), cartItemId);
		APIResponse res=new APIResponse("Item Remove From Cart",true);
		return new ResponseEntity<APIResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{cartItemId}")
	public ResponseEntity<CartItem>updateCartItemHandler(@PathVariable Long cartItemId, @RequestBody CartItem cartItem, @RequestHeader("Authorization")String jwt) throws CartItemException, UserException{
		User user=userService.findUserProfileByJWT(jwt);
		System.out.println("user : "+user);
		CartItem updatedCartItem =cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
		//ApiResponse res=new ApiResponse("Item Updated",true);
		return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
	}
}
