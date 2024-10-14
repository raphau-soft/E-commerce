package com.devraf.e_commerce.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer quantity;
    private String sku;
}
