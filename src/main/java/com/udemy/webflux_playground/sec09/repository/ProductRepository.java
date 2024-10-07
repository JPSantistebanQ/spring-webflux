package com.udemy.webflux_playground.sec09.repository;

import com.udemy.webflux_playground.sec09.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {
    Flux<Product> findByPriceBetween(int price1, int price2);

    Flux<Product> findBy(Pageable pageable);
}
