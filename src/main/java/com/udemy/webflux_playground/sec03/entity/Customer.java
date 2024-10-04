package com.udemy.webflux_playground.sec03.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@ToString
public class Customer {
    @Id
    private Integer id;
    private String name;
    private String email;
}
