package com.devraf.e_commerce.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_product")
public class CartProducts {

    @Id
    @SequenceGenerator(name = "cart_product_seq", sequenceName = "cart_product_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}
