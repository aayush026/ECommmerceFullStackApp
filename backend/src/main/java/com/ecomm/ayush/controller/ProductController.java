package com.ecomm.ayush.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.ayush.exception.ProductException;
import com.ecomm.ayush.model.Product;
import com.ecomm.ayush.request.CreateProductRequest;
import com.ecomm.ayush.response.APIResponse;
import com.ecomm.ayush.service.ProductService;


@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	public ProductController(ProductService productService) { // using @Autowired manual initialization is not required
	  this.productService=productService;
	}
	
	@GetMapping("/getProduct")
	public ResponseEntity<Page<Product>> findProductByCategory(@RequestParam String category, @RequestParam List<String> color,@RequestParam List<String> size, @RequestParam Integer minPrice,@RequestParam Integer maxPrice, @RequestParam Integer minDiscount,@RequestParam String sort,@RequestParam String stock,@RequestParam Integer pageNumber,@RequestParam Integer pageSize){
		Page<Product> res=productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
		return new ResponseEntity<Page<Product>>(res,HttpStatus.OK);
	}
	
	@PostMapping("/")
	public ResponseEntity<Product> createProductHandler(@RequestBody CreateProductRequest req) throws ProductException{
		Product createdProduct=productService.createProduct(req);
		return new ResponseEntity<Product>(createdProduct,HttpStatus.ACCEPTED);
		}
	
	@DeleteMapping("{productId}/delete")
	public ResponseEntity<APIResponse> deleteProductHandler(@PathVariable Long productId) throws ProductException{
		String msg=productService.deleteProduct(productId);
		System.out.println("Product deleted : "+msg);
		APIResponse apiResponse=new APIResponse(msg,true);
		return new ResponseEntity<APIResponse>(apiResponse,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/allProducts")
	public ResponseEntity<List<Product>> findAllProducts(){
		System.out.println("ProductController.findAllProducts()");
		List<Product> products=productService.getAllProducts();
		for(Product p:products) {
			System.out.println("Product : "+p);
		}
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	}
}
