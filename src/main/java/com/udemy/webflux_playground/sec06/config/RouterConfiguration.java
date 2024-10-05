package com.udemy.webflux_playground.sec06.config;

import com.udemy.webflux_playground.sec06.exceptions.CustomerNotFoundException;
import com.udemy.webflux_playground.sec06.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Autowired
    private CustomerRequestHandler customerRequestHandler;

    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customRoutes() {
        return RouterFunctions.route()
                .path("customers", this::customerRoutes)
                //.GET("customers", customerRequestHandler::allCustomers)
                //.GET("customers/paginated", customerRequestHandler::getCustomerPaginated)
                //.GET("customers/{id}", customerRequestHandler::getCustomer)
                .POST("customers", customerRequestHandler::saveCustomer)
                .PUT("customers/{id}", customerRequestHandler::updateCustomer)
                .DELETE("customers/{id}", customerRequestHandler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }

    private RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .GET("/paginated", customerRequestHandler::getCustomerPaginated)
                .GET("/{id}", customerRequestHandler::getCustomer)
                .GET(customerRequestHandler::allCustomers)
                .build();
    }
}
