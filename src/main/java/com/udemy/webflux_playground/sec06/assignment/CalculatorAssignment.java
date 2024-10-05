package com.udemy.webflux_playground.sec06.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import java.util.function.BiFunction;

@Configuration
public class CalculatorAssignment {

    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes() {
        return RouterFunctions.route()
                .path("calculator", this::calculator)
                .build();
    }

    private RouterFunction<ServerResponse> calculator() {
        return RouterFunctions.route()
                .GET("{a}/0", isOperation("/"), badRequest("b cannot be 0"))
                .GET("{a}/{b}", isOperation("+"), handle((a, b) -> a + b))
                .GET("{a}/{b}", isOperation("-"), handle((a, b) -> a - b))
                .GET("{a}/{b}", isOperation("*"), handle((a, b) -> a * b))
                .GET("{a}/{b}", isOperation("/"), handle((a, b) -> a / b))
                .GET("{a}/{b}", badRequest("Operation header should be + - * /"))
                .build();
    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders().getFirst("operation")));
    }

    private HandlerFunction<ServerResponse> handle(BiFunction<Integer, Integer, Integer> operation) {
        return request -> {
            Integer a = Integer.parseInt(request.pathVariable("a"));
            Integer b = Integer.parseInt(request.pathVariable("b"));
            return ServerResponse.ok().bodyValue(operation.apply(a, b));
        };
    }

    private HandlerFunction<ServerResponse> badRequest(String message) {
        return request -> ServerResponse.badRequest().bodyValue(message);
    }
}
