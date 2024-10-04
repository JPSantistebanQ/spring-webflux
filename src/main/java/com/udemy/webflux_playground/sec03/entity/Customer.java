package com.udemy.webflux_playground.sec03.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Builder(toBuilder = true)
public class Customer {
    @Id
    private Integer id;
    private String name;
    private String email;
}
