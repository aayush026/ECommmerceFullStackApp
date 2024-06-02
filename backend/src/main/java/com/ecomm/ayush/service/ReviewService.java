package com.ecomm.ayush.service;

import java.util.List;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Review;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.request.ReviewRequest;

public interface ReviewService {

	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review> getAllReview(Long productId);
	
	
}
