package com.udemy.webflux_playground.tests.sec06;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {"sec=sec06",
        //"logging.level.org.springframework.r2dbc=DEBUG"
})
public abstract class AbstractTest {
}
