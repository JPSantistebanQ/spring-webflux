package com.udemy.webflux_playground.sec05.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Log4j2
@Order(3)
@Service
public class WebFilterDemo1 implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("First pre filter");
        return chain.filter(exchange)
                .doOnSuccess(aVoid -> log.info("First post filter"));
    }
}
