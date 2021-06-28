package com.salesregister.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String operation;
    private String description;
    private Integer amount;
    @Temporal(value = TemporalType.DATE)
    private Date date;
    private BigDecimal price;
    @OneToOne
    private User user;
}
