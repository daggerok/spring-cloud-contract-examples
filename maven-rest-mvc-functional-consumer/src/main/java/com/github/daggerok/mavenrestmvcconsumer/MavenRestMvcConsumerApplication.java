package com.github.daggerok.mavenrestmvcconsumer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.*;

@Data
@Accessors(chain = true)
class BeerRequest {
    private String name;
    private int age;
}

@Configuration
class RestClientConfig {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

@Data
@ConfigurationProperties("producer")
class ProducerProps {
    private String url, host;
    private Integer port;
}

@Log4j2
@Component
@RequiredArgsConstructor
class RestClient {

    private final ProducerProps props;
    private final RestTemplate restTemplate;

    Map getStatistics() {
        var url = String.format("%s/statistics", props.getUrl());
        var exchange = restTemplate.exchange(url, HttpMethod.GET, null, Map.class);
        return logAndTransform("getStatistics", exchange);
    }

    public Map postBeerOrder(BeerRequest request) {
        var url = String.format("%s/beer", props.getUrl());
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(request, headers);
        var exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return logAndTransform("orderBeer", exchange);
    }

    private Map logAndTransform(String identifier, ResponseEntity<Map> exchange) {
        log.info("{} result: {} / {}", identifier, exchange.getStatusCode(), exchange.getStatusCodeValue());
        return exchange.getBody();
    }
}

@Configuration
@RequiredArgsConstructor
class ApiConfig {

    private final RestClient restClient;

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route().POST("/beer", this::postBeerOrder)
                      .GET("/statistics", this::getStatistics)
                      .build()
                      .andRoute(path("/**"), this::fallback);
    }

    private ServerResponse postBeerOrder(ServerRequest request) throws ServletException, IOException {
        var body = request.body(BeerRequest.class);
        var result = restClient.postBeerOrder(body);
        return result.containsKey("ACCEPTED")
                ? accepted().body(result)
                : badRequest().body(result);
    }

    private ServerResponse getStatistics(ServerRequest request) throws ServletException, IOException {
        return ok().body(restClient.getStatistics());
    }

    private ServerResponse fallback(ServerRequest request) {
        var uri = request.uri();
        Function<String, String> url = path -> String.format("%s://%s%s", uri.getScheme(), uri.getAuthority(), path);
        return ok().body(Map.of("get statistics", url.apply("/statistics"),
                                "post beer order", url.apply("/beer")));
    }
}

@SpringBootApplication
@EnableConfigurationProperties(ProducerProps.class)
public class MavenRestMvcConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MavenRestMvcConsumerApplication.class, args);
    }
}
