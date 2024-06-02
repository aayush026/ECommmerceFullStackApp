package com.ecomm.ayush.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecomm.ayush.model.User;


public interface UserRepo extends JpaRepository<User, Long>{

	  public User findByEmail(String email);
}
