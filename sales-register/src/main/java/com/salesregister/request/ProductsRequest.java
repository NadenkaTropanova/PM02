package com.salesregister.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductsRequest {
    private String name;
    private BigDecimal price;
}
