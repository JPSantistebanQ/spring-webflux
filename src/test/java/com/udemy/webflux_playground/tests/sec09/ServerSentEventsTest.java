package com.udemy.webflux_playground.tests.sec09;

import com.udemy.webflux_playground.sec09.dto.ProductDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Log4j2
@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec09")
class ServerSentEventsTest {

    @Autowired
    private WebTestClient client;

    @Test
    void serverSentEvents() {
        this.client.get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(3)
                .doOnNext(productDto -> log.info("productDto: {}", productDto))
                .collectList()
                .as((StepVerifier::create))
                .assertNext(list -> {
                    Assertions.assertEquals(3, list.size());
                    Assertions.assertTrue(list.stream().allMatch(p -> p.price() <= 80));
                })
                .expectComplete()
                .verify();
    }
}
