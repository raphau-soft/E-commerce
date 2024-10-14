package com.devraf.e_commerce.payload.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    @Size(min = 5, max = 50)
    private String name;
    @Size(min = 10, max = 200)
    private String description;
    private BigDecimal price;
    @Max(10000)
    @Min(1)
    private Integer quantity;
    @Size(min = 1, max = 10)
    private String sku;
}
