package com.udemy.webflux_playground.sec09.mapper;

import com.udemy.webflux_playground.sec09.dto.ProductDto;
import com.udemy.webflux_playground.sec09.entity.Product;

public class EntityDtoMapper {
    public static Product toEntity(ProductDto dto) {
        Product entity = new Product();
        entity.setId(dto.id());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        return entity;
    }

    public static ProductDto toDto(Product entity) {
        return new ProductDto(
                entity.getId(),
                entity.getDescription(),
                entity.getPrice()
        );
    }
}
