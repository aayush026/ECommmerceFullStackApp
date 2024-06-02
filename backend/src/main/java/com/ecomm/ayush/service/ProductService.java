package com.ecomm.ayush.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.request.CreateProductRequest;

public interface ProductService {
	public Product createProduct(CreateProductRequest req);

	public String deleteProduct(Long productId) throws ProductException;

	public Product updateProduct(Long productId, Product product) throws ProductException;

	public Product findProductById(Long id) throws ProductException;

	public List<Product> findProductByCategory(String category);

	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);

	public List<Product> getAllProducts();

	public List<Product> searchProduct(String query);

	public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);

	public List<Product> recentlyAddedProduct();

	
}
