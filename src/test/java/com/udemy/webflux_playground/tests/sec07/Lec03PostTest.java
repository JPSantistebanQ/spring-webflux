package com.udemy.webflux_playground.tests.sec07;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class Lec03PostTest extends AbstractWebClient {
    private final WebClient client = createWebClient();

    @Test
    void postWithBodyValue() {
        Product product = new Product(null, "iphone", 1000);
        this.client.post()
                .uri("/lec03/product")
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    void postWithBody() {
        Mono<Product> mono = Mono.fromSupplier(() -> new Product(null, "iphone", 1000)
        ).delayElement(Duration.ofSeconds(1));
        this.client.post()
                .uri("/lec03/product")
                .body(mono, Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
