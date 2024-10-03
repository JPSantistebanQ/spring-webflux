package com.udemy.webflux_playground.tests.sec02;

import com.udemy.webflux_playground.sec02.dto.OrderDetails;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@Log4j2
class Lec04DatabaseClientTest extends AbstractTest {
    @Autowired
    private DatabaseClient client;

    @Test
    void orderDetailsByProduct() {
        var query = """
                SELECT
                    co.order_id,
                    c.name AS customer_name,
                    p.description AS product_name,
                    co.amount,
                    co.order_date
                FROM
                    customer c
                INNER JOIN customer_order co ON c.id = co.customer_id
                INNER JOIN product p ON p.id = co.product_id
                WHERE
                    p.description = :description
                ORDER BY co.amount DESC
                """;

        this.client.sql(query)
                .bind("description", "iphone 20")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(log::info)
                .as(StepVerifier::create)
                //.expectNextCount(2)
                .assertNext(p -> Assertions.assertEquals(975, p.amount()))
                .assertNext(p -> Assertions.assertEquals(950, p.amount()))
                .verifyComplete();
    }
}
