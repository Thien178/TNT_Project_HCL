package tnt.group.techstore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tnt.group.techstore.model.Product;
import tnt.group.techstore.service.ProductService;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api/product")
public class ProductController {
	
	@Autowired
	ProductService proSer;
	
	@GetMapping("/auth/viewall")
	public ResponseEntity<List<Product>> getAllProduct(){
		return new ResponseEntity<List<Product>>(proSer.getAllProducts(), HttpStatus.OK);
	}
	
	@GetMapping("/auth/{id}")
	public ResponseEntity<Product> getProctById(@PathVariable("id") long id){
		return new ResponseEntity<Product> (proSer.getProductById(id), HttpStatus.OK);
	}
	
	@PostMapping("/createByCategory/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> createProduct(@PathVariable("id") long id,@RequestBody Product product){
		return new ResponseEntity<Product>(proSer.createProduct(product,id), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> updateProduct(@PathVariable("id") long id,
			@RequestBody Product product){
		return new ResponseEntity<Product> (proSer.updateProduct(product, id), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") long id){
		proSer.deleteProduct(id);
		return new ResponseEntity<String> ("Delete Product successfully!.", HttpStatus.OK);
	}

}
