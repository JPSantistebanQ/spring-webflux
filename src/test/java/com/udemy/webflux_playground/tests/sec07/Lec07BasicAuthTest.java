package com.udemy.webflux_playground.tests.sec07;

import com.udemy.webflux_playground.tests.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class Lec07BasicAuthTest extends AbstractWebClient {
    private final WebClient client = createWebClient(b -> b.defaultHeaders(headers -> headers.setBasicAuth("java", "secret")));

    @Test
    void defaultHeader() {
        this.client.get()
                .uri("/lec07/product/{id}", 1)
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
