package com.udemy.webflux_playground.tests.sec07;

import com.udemy.webflux_playground.tests.sec07.dto.CalculatorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

class Lec06QueryParamsTest extends AbstractWebClient {
    private final WebClient client = createWebClient(b -> b.defaultHeader("caller-id", "order-service"));

    @Test
    void urlBuilderVariables() {
        String path = "/lec06/calculator";
        String query = "first={first}&second={second}&operation={operation}";
        this.client.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(10, 20, "+"))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void urlBuilderMap() {
        String path = "/lec06/calculator";
        String query = "first={first}&second={second}&operation={operation}";
        Map<String, Object> params = Map.of(
                "first", 10,
                "second", 20,
                "operation", "+");
        this.client.get()
                .uri(uriBuilder -> uriBuilder.path(path).query(query).build(params))
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
