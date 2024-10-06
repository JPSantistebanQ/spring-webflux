package com.udemy.webflux_playground.tests.sec07;

import com.udemy.webflux_playground.tests.sec07.dto.CalculatorResponse;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Log4j2
class Lec05ErrorResponseTest extends AbstractWebClient {
    private final WebClient client = createWebClient();

    @Test
    void successOperation() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "+")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void handleError() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "@")
                .retrieve()
                .bodyToMono(CalculatorResponse.class)
                //.onErrorReturn(new CalculatorResponse(0, 0, null, 0.0))
                .doOnError(WebClientResponseException.class, ex -> {
                    log.info("Handling error: {}", ex.getResponseBodyAs(ProblemDetail.class));
                })
                .onErrorReturn(WebClientResponseException.InternalServerError.class, new CalculatorResponse(0, 0, null, 0.0))
                .onErrorReturn(WebClientResponseException.BadRequest.class, new CalculatorResponse(0, 0, null, -1.0))
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }


    @Test
    void exchangeError() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "@")
                .exchangeToMono(this::decode)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    void exchangeSuccess() {
        this.client.get()
                .uri("/lec05/calculator/{a}/{b}", 10, 20)
                .header("operation", "+")
                .exchangeToMono(this::decode)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
        //* clientResponse.cookies()
        // * clientResponse.headers()
        log.info("Status code: {}", clientResponse.statusCode());
        if (clientResponse.statusCode().isError()) {
            return clientResponse.bodyToMono(ProblemDetail.class)
                    .doOnNext(pd -> log.info("Problem detail: {}", pd))
                    .then(Mono.empty());
        }
        return clientResponse.bodyToMono(CalculatorResponse.class);
    }

}
