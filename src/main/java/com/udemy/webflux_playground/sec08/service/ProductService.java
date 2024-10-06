package com.udemy.webflux_playground.sec08.service;

import com.udemy.webflux_playground.sec08.dto.ProductDto;
import com.udemy.webflux_playground.sec08.mapper.EntityDtoMapper;
import com.udemy.webflux_playground.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> saveProducts(Flux<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .as(productRepository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Long> getProductCount() {
        return this.productRepository.count();
    }

    public Flux<ProductDto> allProducts() {
        return this.productRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }
}
