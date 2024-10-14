package com.devraf.e_commerce.repository;

import com.devraf.e_commerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDAO extends JpaRepository<Product, Long> {
}
