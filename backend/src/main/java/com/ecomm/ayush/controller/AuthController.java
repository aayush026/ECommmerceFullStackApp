package com.ecomm.ayush.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.ayush.config.JWTTokenProvider;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.Cart;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.UserRepo;
import com.ecomm.ayush.request.LoginRequest;
import com.ecomm.ayush.response.AuthResponse;
import com.ecomm.ayush.service.CartService;
import com.ecomm.ayush.service.CustomUserDetails;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private UserRepo userRepo;
	private PasswordEncoder passwordEncoder;
	private JWTTokenProvider jwtTokenProvider;
	private CustomUserDetails customUserDetails;
	@Autowired
	 private CartService cartService;

	public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider,
			CustomUserDetails customUserDetails) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.jwtTokenProvider = jwtTokenProvider;
		this.customUserDetails = customUserDetails;
		 this.cartService=cartService;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		User isEmailExist = userRepo.findByEmail(email);
		if (isEmailExist != null) {
			throw new UserException("Email Address Exist with another account");
		}
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setLastName(lastName);
		createdUser.setFirstName(firstName);
		createdUser.setCreatedAt(LocalDateTime.now());
		createdUser.setMobile(UUID.randomUUID().toString().substring(0, 9));
		User savedUser = userRepo.save(createdUser);
		Cart cart=cartService.createCart(savedUser); 
		System.out.println("Cart created : "+cart);
		Authentication auth = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
		SecurityContextHolder.getContext().setAuthentication(auth);
		String token = jwtTokenProvider.generateToken(auth);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup Success");
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest) {
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		System.out.println(username + " ----- " + password);
		Authentication authentication = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setMessage("Sign in Success");
		authResponse.setJwt(token);
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetails.loadUserByUsername(username);

		System.out.println("sign in userDetails - " + userDetails);

		if (userDetails == null) {
			System.out.println("sign in userDetails - null " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}
