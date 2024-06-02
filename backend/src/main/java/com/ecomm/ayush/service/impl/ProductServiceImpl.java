package com.ecomm.ayush.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Category;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.repo.CategoryRepo;
import com.ecomm.ayush.repo.ProductRepo;
import com.ecomm.ayush.request.CreateProductRequest;
import com.ecomm.ayush.service.ProductService;
import com.ecomm.ayush.service.UserService;

@Service
public class ProductServiceImpl implements ProductService {
	private ProductRepo productRepo;
	private UserService userService;
	private CategoryRepo categoryRepo;

	public ProductServiceImpl(ProductRepo productRepo, UserService userService, CategoryRepo categoryRepo) {
		this.productRepo = productRepo;
		this.userService = userService;
		this.categoryRepo = categoryRepo;
	}

	@Override
	public Product createProduct(CreateProductRequest req) {
		Category topLevel = categoryRepo.findByName(req.getTopLevelCategory());
		if (topLevel == null) {
			Category topLavelCategory = new Category();
			topLavelCategory.setName(req.getTopLevelCategory());
			topLavelCategory.setLevel(1);
			topLevel = categoryRepo.save(topLavelCategory);
		}

		Category secondLevel = categoryRepo.findByNameAndParant(req.getSecondLevelCategory(), topLevel.getName());
		if (secondLevel == null) {
			Category secondLavelCategory = new Category();
			secondLavelCategory.setName(req.getSecondLevelCategory());
			secondLavelCategory.setParentCategory(topLevel);
			secondLavelCategory.setLevel(2);
			secondLevel = categoryRepo.save(secondLavelCategory);
		}

		Category thirdLevel = categoryRepo.findByNameAndParant(req.getThirdLevelCategory(), secondLevel.getName());
		if (thirdLevel == null) {
			Category thirdLavelCategory = new Category();
			thirdLavelCategory.setName(req.getThirdLevelCategory());
			thirdLavelCategory.setParentCategory(secondLevel);
			thirdLavelCategory.setLevel(3);
			thirdLevel = categoryRepo.save(thirdLavelCategory);
		}
		Product product = new Product();
		product.setTitle(req.getTitle());
		product.setColor(req.getColor());
		product.setDescription(req.getDescription());
		product.setDiscountedPrice(req.getDiscountedPrice());
		product.setDiscountPersent(req.getDiscountPresent());
		product.setImageUrl(req.getImageUrl());
		product.setBrand(req.getBrand());
		product.setPrice(req.getPrice());
		product.setSizes(req.getSize());
		product.setQuantity(req.getQuantity());
		product.setCategory(thirdLevel);
		product.setCreatedAt(LocalDateTime.now());
		Product savedProduct = productRepo.save(product);
		System.out.println("products - " + product);
		return savedProduct;
	}

	@Override
	public String deleteProduct(Long productId) throws ProductException {
		Product product = findProductById(productId);
		System.out.println("delete product " + product.getId() + " - " + productId);
		product.getSizes().clear();
		// productRepository.save(product);
		// product.getCategory().
		productRepo.delete(product);
		return "Product deleted Successfully";
	}

	@Override
	public Product updateProduct(Long productId, Product req) throws ProductException {
		Product product = findProductById(productId);
		if (req.getQuantity() != 0) {
			product.setQuantity(req.getQuantity());
		}
		if (req.getDescription() != null) {
			product.setDescription(req.getDescription());
		}
		return productRepo.save(product);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	@Override
	public Product findProductById(Long id) throws ProductException {
		Optional<Product> opt = productRepo.findById(id);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new ProductException("product not found with id " + id);
	}

	@Override
	public List<Product> findProductByCategory(String category) {
		System.out.println("category --- " + category);
		List<Product> products = productRepo.findByCategory(category);
		return products;
	}

	@Override
	public List<Product> searchProduct(String query) {
		List<Product> products = productRepo.searchProduct(query);
		return products;
	}

	@Override
	public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		List<Product> products = productRepo.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
		if (!colors.isEmpty()) {
			products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
					.collect(Collectors.toList());
		}

		if (stock != null) {
			if (stock.equals("in_stock")) {
				products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
			} else if (stock.equals("out_of_stock")) {
				products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
			}
		}
		int startIndex = (int) pageable.getOffset();
		int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
		List<Product> pageContent = products.subList(startIndex, endIndex);
		Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
		return filteredProducts; // If color list is empty, do nothing and return all products
	}

	@Override
	public List<Product> recentlyAddedProduct() {
		return productRepo.findTop10ByOrderByCreatedAtDesc();
	}

	@Override
	public Page<Product> getAllProducts(String category, List<String> colors, List<String> sizes, Integer minPrice,
			Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
		return null;
	}

}
