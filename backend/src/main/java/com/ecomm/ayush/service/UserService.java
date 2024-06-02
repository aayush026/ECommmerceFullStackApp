package com.ecomm.ayush.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ecomm.ayush.exception.UserException;
import com.ecomm.ayush.model.User;

public interface UserService {

     public User findUserById(Long userId) throws UserException;
     
     public User findUserProfileByJWT(String JWT) throws UserException;

}
