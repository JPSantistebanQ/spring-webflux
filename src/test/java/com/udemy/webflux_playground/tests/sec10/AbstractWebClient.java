package com.udemy.webflux_playground.tests.sec10;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@Log4j2
public class AbstractWebClient {

    protected <T> Consumer<T> print() {
        return response -> log.info("Receive: {}", response);
    }

    protected WebClient createWebClient() {
        return createWebClient(client -> {
        });
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> consumer) {
        WebClient.Builder client = WebClient.builder()
                .baseUrl("http://localhost:7070/demo03");
        consumer.accept(client);
        return client.build();
    }
}
