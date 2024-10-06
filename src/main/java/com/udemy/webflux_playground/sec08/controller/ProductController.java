package com.udemy.webflux_playground.sec08.controller;

import com.udemy.webflux_playground.sec08.dto.ProductDto;
import com.udemy.webflux_playground.sec08.dto.UploadResponse;
import com.udemy.webflux_playground.sec08.service.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Log4j2
@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> uploadProducts(@RequestBody Flux<ProductDto> flux) {
        log.info("invoking uploadProducts");
        return this.productService.saveProducts(flux.doOnNext(productDto -> log.info("saving {}", productDto)))
                .then(this.productService.getProductCount())
                .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }
}
