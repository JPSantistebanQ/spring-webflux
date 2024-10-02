package com.udemy.webflux_playground.sec02.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("customer")
@Data
@ToString
public class Customer {
    @Id
    private Integer id;

    @Column("name")
    private String name;
    private String email;
}
