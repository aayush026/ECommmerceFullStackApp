package com.ecomm.ayush.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService userService;
	
	public UserController(UserService userService) {
      this.userService=userService;
	}
	
	@GetMapping("/profiles")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization")String jwt) throws UserException{
		User user=userService.findUserProfileByJWT(jwt);
		
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
	}
	
}
