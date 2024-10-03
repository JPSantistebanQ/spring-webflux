package com.udemy.webflux_playground.tests.sec02;

import com.udemy.webflux_playground.sec02.repository.CustomerOrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Log4j2
class Lec03CustomerOrderRepositoryTest extends AbstractTest {
    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    void productsOrderedByCustomer() {
        this.customerOrderRepository.getProductsOrderByCustomer("mike")
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void orderDetailsByProduct() {
        this.customerOrderRepository.getOrderDetailByProduct("iphone 20")
                .doOnNext(log::info)
                .as(StepVerifier::create)
                //.expectNextCount(2)
                .assertNext(p -> Assertions.assertEquals(975, p.amount()))
                .assertNext(p -> Assertions.assertEquals(950, p.amount()))
                .verifyComplete();
    }
}
