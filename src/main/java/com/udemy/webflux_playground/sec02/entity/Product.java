package com.udemy.webflux_playground.sec02.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("product")
@Data
@ToString
public class Product {
    @Id
    private Integer id;
    private String description;
    private Integer price;
}
