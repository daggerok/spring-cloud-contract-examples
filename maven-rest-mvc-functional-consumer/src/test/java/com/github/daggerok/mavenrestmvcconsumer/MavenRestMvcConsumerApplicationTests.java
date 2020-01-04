package com.github.daggerok.mavenrestmvcconsumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Function;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@DirtiesContext
@AutoConfigureJson
@AutoConfigureMockMvc
@AutoConfigureStubRunner(ids = {
        // "maven-rest-mvc-functional-producer",
        // ":maven-rest-mvc-functional-producer:+:stubs",
        // "com.github.daggerok:maven-rest-mvc-functional-producer:+:stubs",
        "com.github.daggerok:maven-rest-mvc-functional-producer:+:stubs:8080",
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class MavenRestMvcConsumerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @StubRunnerPort("maven-rest-mvc-functional-producer")
    // private Integer port;

    @Test
    void should_request_statistics() throws Exception {
        mockMvc.perform(get("/statistics")
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(result -> {
                   log.info("request: {}", result.getRequest().getContentAsString());
                   log.info("response: {}", result.getResponse().getContentAsString());
               })
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("ACCEPTED")))
               .andExpect(content().string(containsString("REJECTED")));
    }

    @Test
    void should_request_accepted_beer_order() throws Exception {
        mockMvc.perform(post("/beer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(new BeerRequest().setName("Maksimko")
                                                               .setAge(36))))
               .andDo(result -> {
                   log.info("request: {}", result.getRequest().getContentAsString());
                   log.info("response: {}", result.getResponse().getContentAsString());
               })
               .andExpect(status().isAccepted())
               .andExpect(content().string(containsString("ACCEPTED")))
        ;
    }

    @Test
    void should_receive_rejected_beer_order() throws Exception {
        mockMvc.perform(post("/beer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json(new BeerRequest().setName("Little Billy")
                                                               .setAge(16))))
               .andDo(result -> {
                   log.info("request: {}", result.getRequest().getContentAsString());
                   log.info("response: {}", result.getResponse().getContentAsString());
               })
               .andExpect(status().isBadRequest())
               .andExpect(content().string(containsString("REJECTED")))
        ;
    }

    private <T> String json(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
