package com.udemy.webflux_playground.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

class Lec04HeaderTest extends AbstractWebClient {
    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    void defaultHeader() {
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .retrieve()
                .bodyToMono(Product.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void overrideHeader() {
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .header("caller-id", "payment-service")
                .retrieve()
                .bodyToMono(Product.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    void headerWithMap() {
        Map<String, String> headers = Map.of("caller-id", "payment-service");
        this.client.get()
                .uri("/lec04/product/{id}", 1)
                .headers(h -> h.setAll(headers))
                .retrieve()
                .bodyToMono(Product.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
