package com.udemy.webflux_playground.tests.sec05;

import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {"sec=sec05",
        //"logging.level.org.springframework.r2dbc=DEBUG"
})
public abstract class AbstractTest {
}
