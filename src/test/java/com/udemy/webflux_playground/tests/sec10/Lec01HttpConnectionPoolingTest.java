package com.udemy.webflux_playground.tests.sec10;

import com.udemy.webflux_playground.tests.sec10.dto.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

class Lec01HttpConnectionPoolingTest extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> {
        int poolSize = 501;
        var provider = ConnectionProvider.builder("JP")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize * 5)
                .build();
        var httpClient = HttpClient.create(provider)
                .compress(true)
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));
    });

    @Test
    void concurrentRequests() {
        int max = 501;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(i -> Assertions.assertEquals(max, i.size()))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
