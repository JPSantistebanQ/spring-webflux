package com.udemy.webflux_playground.tests.sec08;

import com.udemy.webflux_playground.sec08.dto.ProductDto;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@Log4j2
public class ProductsUploadDownloadTest {

    private final ProductClient productClient = new ProductClient();

    @Test
    void upload() {

        /*
        var flux = Flux.just(
                new ProductDto(null, "product1", 100),
                new ProductDto(null, "product2", 200),
                new ProductDto(null, "product3", 300)
        ).delayElements(Duration.ofSeconds(10));
         */
        var flux = Flux.range(1, 10)
                .map(i -> new ProductDto(null, "product-" + i, i))
                .delayElements(Duration.ofSeconds(2));

        this.productClient.uploadProducts(flux)
                .doOnNext(uploadResponse -> log.info("uploadResponse: {}", uploadResponse))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
