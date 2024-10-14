package com.devraf.e_commerce.controller;

import com.devraf.e_commerce.dto.ProductDTO;
import com.devraf.e_commerce.payload.product.CreateProductRequest;
import com.devraf.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDTO(id));
    }

    @PostMapping()
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid CreateProductRequest request) {
        productService.createProduct(request);
        return ResponseEntity.ok().build();
    }
}
