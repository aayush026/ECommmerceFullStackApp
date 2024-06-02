package com.ecomm.ayush.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.model.Review;
import com.ecomm.ayush.model.User;
import com.ecomm.ayush.repo.ProductRepo;
import com.ecomm.ayush.repo.ReviewRepo;
import com.ecomm.ayush.request.ReviewRequest;
import com.ecomm.ayush.service.ProductService;
import com.ecomm.ayush.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	
	private ReviewRepo reviewRepository;
	private ProductService productService;
	private ProductRepo productRepository;
	
	public ReviewServiceImpl(ReviewRepo reviewRepository,ProductService productService,ProductRepo productRepository) {
		this.reviewRepository=reviewRepository;
		this.productService=productService;
		this.productRepository=productRepository;
	}

	@Override
	public Review createReview(ReviewRequest req,User user) throws ProductException {
		// TODO Auto-generated method stub
		Product product=productService.findProductById(req.getProductId());
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
//		product.getReviews().add(review);
		productRepository.save(product);
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		
		return reviewRepository.getAllProductsReview(productId);
	}

}
