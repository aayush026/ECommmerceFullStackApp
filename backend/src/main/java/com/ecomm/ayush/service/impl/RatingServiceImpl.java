package com.ecomm.ayush.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.model.Rating;
import com.ecomm.ayush.model.RatingRequest;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.RatingRepo;
import com.ecomm.ayush.service.ProductService;
import com.ecomm.ayush.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService{
	private RatingRepo ratingRepo;
	private ProductService productService;
	
	public RatingServiceImpl(RatingRepo ratingRepo,ProductService productService) {
	this.ratingRepo=ratingRepo;
	this.productService=productService;
	}
	
	@Override
	public Rating createRating(RatingRequest req,User user) throws ProductException {
		Product product=productService.findProductById(req.getProductId());
		Rating rating=new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		return ratingRepo.save(rating);
	}


	@Override
	public List<Rating> getProductsRating(Long productId) {
		return ratingRepo.getAllProductsRating(productId);
	}

	
}
