package com.ecomm.ayush.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ecomm.ayush.config.JWTTokenProvider;
import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.UserRepo;
import com.ecomm.ayush.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private UserRepo userRepo;
    private JWTTokenProvider jwtProvider;
	
    
	public UserServiceImpl(UserRepo userRepo,JWTTokenProvider jwtProvider) {
		this.userRepo = userRepo;
		this.jwtProvider=jwtProvider;
	}

	@Override
	public User findUserById(Long userId) throws UserException {
         Optional<User> user=userRepo.findById(userId);
         if(user.isPresent()) {
        	 return user.get();
         }
	 throw new UserException("User not found with id : "+userId);
	}

	@Override
	public User findUserProfileByJWT(String jwt) throws UserException {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        User user=userRepo.findByEmail(email);
        if(user==null) {
        	throw new UserException("User not found with email = "+email);
        }
	      return user;
	}
}
