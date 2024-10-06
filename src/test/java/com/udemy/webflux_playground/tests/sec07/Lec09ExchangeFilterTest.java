package com.udemy.webflux_playground.tests.sec07;

import com.udemy.webflux_playground.tests.sec07.dto.Product;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

@Log4j2
class Lec09ExchangeFilterTest extends AbstractWebClient {
    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator()).filter(logHeaders()));

    @Test
    void exchangeFilter() {
        this.client.get()
                .uri("/lec09/product/{id}", 1)
                .attribute("enable-logging", true)
                .retrieve()
                .bodyToMono(Product.class)
                //.take(Duration.ofSeconds(3))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private ExchangeFilterFunction tokenGenerator() {
        return ((request, next) -> {
            String token = UUID.randomUUID().toString().replace("-", "");
            log.info("Token generated: {}", token);
            ClientRequest newRequest = ClientRequest.from(request).headers(httpHeaders -> httpHeaders.setBearerAuth(token)).build();
            return next.exchange(newRequest);
        });
    }

    private ExchangeFilterFunction logHeaders() {
        return ((request, next) -> {
            boolean isEnabled = (Boolean) request.attributes().getOrDefault("enable-logging", false);
            if (isEnabled) {
                log.info("Method: {}", request.method());
                log.info("URI: {}", request.url());
            }
            return next.exchange(request);
        });
    }
}
