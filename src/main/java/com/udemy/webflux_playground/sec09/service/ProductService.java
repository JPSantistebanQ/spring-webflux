package com.udemy.webflux_playground.sec09.service;

import com.udemy.webflux_playground.sec09.dto.ProductDto;
import com.udemy.webflux_playground.sec09.mapper.EntityDtoMapper;
import com.udemy.webflux_playground.sec09.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Sinks.Many<ProductDto> sink;

    public Mono<ProductDto> saveProduct(Mono<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .flatMap(productRepository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<ProductDto> productsStream() {
        return this.sink.asFlux();
    }

}
