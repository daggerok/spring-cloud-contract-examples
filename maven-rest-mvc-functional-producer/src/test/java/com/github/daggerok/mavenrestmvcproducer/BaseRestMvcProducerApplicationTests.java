package com.github.daggerok.mavenrestmvcproducer;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(classes = MavenRestMvcProducerApplication.class)
class BaseRestMvcProducerApplicationTests {

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void before() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
