package com.udemy.webflux_playground.tests.sec06;

import com.udemy.webflux_playground.sec03.entity.Customer;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Log4j2
class CustomerServiceTest extends AbstractTest {

    @Autowired
    private WebTestClient client;

    @Test
    void getAllCustomers() {
        this.client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Customer.class)
                .value(customers -> {
                    log.info("customers: {}", customers);
                })
                .hasSize(10);
    }

    @Test
    void paginatedCustomers() {
        this.client.get()
                .uri("/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .consumeWith(r -> {
                    log.info("response: {}", new String(Objects.requireNonNull(r.getResponseBody())));
                })
                .jsonPath("$").isArray()
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(5)
                .jsonPath("$[1].id").isEqualTo(6);
    }

    @Test
    void getCustomersById() {
        this.client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                //.expectBody(Customer.class)
                .expectBody()
                .consumeWith(r -> {
                    log.info("response: {}", new String(Objects.requireNonNull(r.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    void createAndDeleteCustomer() {

        // * Create
        Customer customer = new Customer().toBuilder()
                .name("Juan Piero")
                .email("jpsantq@gmail.com")
                .build();

        this.client.post()
                .uri("/customers")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                //.expectBody(Customer.class)
                .expectBody()
                .consumeWith(r -> {
                    log.info("response: {}", new String(Objects.requireNonNull(r.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("Juan Piero")
                .jsonPath("$.email").isEqualTo("jpsantq@gmail.com");

        // * Delete
        this.client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    void updateCustomer() {

        // * Update
        Customer customer = new Customer().toBuilder()
                .name("Juan Piero")
                .email("jpsantq@gmail.com")
                .build();

        this.client.put()
                .uri("/customers/10")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                //.expectBody(Customer.class)
                .expectBody()
                .consumeWith(r -> {
                    log.info("response: {}", new String(Objects.requireNonNull(r.getResponseBody())));
                })
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("Juan Piero")
                .jsonPath("$.email").isEqualTo("jpsantq@gmail.com");
    }

    @Test
    void customerNotFound() {
        // * get
        this.client.get()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> {
                    log.info("problemDetail: {}", problemDetail);
                    Assertions.assertEquals("Customer [id=11] is not found", problemDetail.getDetail());
                    Assertions.assertEquals("Customer Not Found", problemDetail.getTitle());
                });

        // * delete
        this.client.delete()
                .uri("/customers/11")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ProblemDetail.class)
                .value(problemDetail -> {
                    log.info("problemDetail: {}", problemDetail);
                    Assertions.assertEquals("Customer [id=11] is not found", problemDetail.getDetail());
                    Assertions.assertEquals("Customer Not Found", problemDetail.getTitle());
                });

        // * update
        Customer customer = new Customer().toBuilder()
                .name("Juan Piero")
                .email("jpsantq@gmail.com")
                .build();

        this.client.put()
                .uri("/customers/11")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectStatus().isEqualTo(404)
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id=11] is not found")
                .jsonPath("$.title").isEqualTo("Customer Not Found");
    }

    @Test
    void invalidInput() {
        // * update
        Customer customer = new Customer().toBuilder()
                .email("jpsantq@gmail.com")
                .build();

        this.client.put()
                .uri("/customers/10")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required")
                .jsonPath("$.title").isEqualTo("Invalid Input");

        customer = customer.toBuilder()
                .email("HOLA")
                .name("Juan Piero")
                .build();

        this.client.put()
                .uri("/customers/10")
                .bodyValue(customer)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST)
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required")
                .jsonPath("$.title").isEqualTo("Invalid Input");
    }
}
