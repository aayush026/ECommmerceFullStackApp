package com.ecomm.ayush.service;

import java.util.List;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Rating;
import com.ecomm.ayush.model.RatingRequest;
import com.ecomm.ayush.model.User;

public interface RatingService{
	
	public Rating createRating(RatingRequest req,User user) throws ProductException;
	
	public List<Rating> getProductsRating(Long productId);

}