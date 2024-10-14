package com.devraf.e_commerce.service;

import com.devraf.e_commerce.dto.ProductDTO;
import com.devraf.e_commerce.entity.Product;
import com.devraf.e_commerce.payload.product.CreateProductRequest;
import com.devraf.e_commerce.repository.ProductDAO;
import com.devraf.e_commerce.utils.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    public ProductDTO getProductDTO(Long id) {
        return productDAO.findById(id).map(
                        product -> ProductDTO.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .description(product.getDescription())
                                .imageUrl(product.getImageUrl())
                                .quantity(product.getQuantity())
                                .sku(product.getSku())
                                .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public void createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .sku(request.getSku())
                .build();
        productDAO.save(product);
    }
}
