package com.salesregister.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperationRequest {
    private String name;
    private String description;
    private String operation;
    private Integer amount;
    private BigDecimal price;
}
