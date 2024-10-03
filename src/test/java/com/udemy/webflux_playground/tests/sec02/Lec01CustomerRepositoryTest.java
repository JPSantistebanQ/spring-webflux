package com.udemy.webflux_playground.tests.sec02;

import com.udemy.webflux_playground.sec02.entity.Customer;
import com.udemy.webflux_playground.sec02.repository.CustomerRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Log4j2
class Lec01CustomerRepositoryTest extends AbstractTest {
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findAll() {
        this.customerRepository.findAll()
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    void findById() {
        this.customerRepository.findById(2)
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByName() {
        this.customerRepository.findByName("jake")
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    void findByEmailEndingWith() {
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    void insertAndDeleteCustomer() {
        Customer newCustomer = new Customer();
        newCustomer.setName("Juan Piero");
        newCustomer.setEmail("jpsantq@gmail.com");

        // * Insert
        this.customerRepository.save(newCustomer)
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(c -> {
                    Assertions.assertNotNull(c.getId());
                    Assertions.assertEquals("Juan Piero", c.getName());
                    Assertions.assertEquals("jpsantq@gmail.com", c.getEmail());
                })
                .expectComplete()
                .verify();

        // * Count 11
        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        // * Delete new Customer
        this.customerRepository.delete(newCustomer)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();

    }

    @Test
    void updateCustomerName() {
        this.customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("Juan Piero"))
                .flatMap(this.customerRepository::save)
                .doOnNext(log::info)
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("Juan Piero", c.getName()))
                .expectComplete()
                .verify();
    }
}
