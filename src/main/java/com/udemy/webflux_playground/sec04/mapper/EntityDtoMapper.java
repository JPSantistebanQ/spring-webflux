package com.udemy.webflux_playground.sec04.mapper;

import com.udemy.webflux_playground.sec04.dto.CustomerDto;
import com.udemy.webflux_playground.sec04.entity.Customer;

public class EntityDtoMapper {
    public static Customer toEntity(CustomerDto dto) {
        var customer = new Customer();
        customer.setId(dto.id());
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        return customer;
    }

    public static CustomerDto toDto(Customer entity) {
        return new CustomerDto(entity.getId(),
                entity.getName(),
                entity.getEmail());
    }
}
