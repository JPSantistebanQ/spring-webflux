package com.udemy.webflux_playground.tests.sec02;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"sec=sec02",
        //"logging.level.org.springframework.r2dbc=DEBUG"
})
public abstract class AbstractTest {
}
