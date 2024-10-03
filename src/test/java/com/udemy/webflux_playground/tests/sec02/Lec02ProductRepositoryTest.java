package com.udemy.webflux_playground.tests.sec02;

import com.udemy.webflux_playground.sec02.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

@Log4j2
class Lec02ProductRepositoryTest extends AbstractTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAll() {
        this.productRepository.findAll()
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void findProductBetween() {
        this.productRepository.findByPriceBetween(100, 300)
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    @Test
    void findPageable() {
        this.productRepository.findBy(
                        PageRequest.of(0, 3)
                                .withSort(Sort.by("price")
                                        .ascending()))
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
                .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
                .expectComplete()
                .verify();
    }
}
