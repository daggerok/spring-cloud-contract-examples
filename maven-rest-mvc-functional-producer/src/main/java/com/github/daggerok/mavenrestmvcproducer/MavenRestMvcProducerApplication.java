package com.github.daggerok.mavenrestmvcproducer;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.*;

@Data
@Accessors(chain = true)
class BeerRequest {
    private String name;
    private int age;
}

@Getter
@RequiredArgsConstructor
enum Result {
    ACCEPTED("Here you are"),
    REJECTED("Just go away");
    private final String message;
}

@Configuration
class ApiConfig {

    @Bean
    Map<Result, BigInteger> statistics() {
        var statistics = new ConcurrentHashMap<Result, BigInteger>();
        statistics.putIfAbsent(Result.ACCEPTED, BigInteger.ZERO);
        statistics.putIfAbsent(Result.REJECTED, BigInteger.ZERO);
        return statistics;
    }

    @Bean
    RouterFunction<ServerResponse> routes(Map<Result, BigInteger> statistics) {
        return route().GET("/statistics", this::getStatistics)
                      .POST("/beer", this::orderBeer)
                      .build()
                      .andRoute(path("/**"), this::fallback);
    }

    private ServerResponse fallback(ServerRequest request) {
        var uri = request.uri();
        Function<String, String> url = path -> String.format("%s://%s%s", uri.getScheme(), uri.getAuthority(), path);
        return ok().body(Map.of("get statistics", url.apply("/statistics"),
                                "order beer", url.apply("/beer")));
    }

    private ServerResponse getStatistics(ServerRequest request) throws ServletException, IOException {
        return ok().body(statistics());
    }

    private ServerResponse orderBeer(ServerRequest request) throws ServletException, IOException {
        var body = request.body(BeerRequest.class);
        var isInvalid = body.getAge() < 21;
        var key = isInvalid ? Result.REJECTED : Result.ACCEPTED;
        statistics().computeIfPresent(key, (k, v) -> v.add(BigInteger.ONE));
        return isInvalid ? badRequest().body(Map.of(key, key.getMessage()))
                : accepted().body(Map.of(key, key.getMessage()));
    }
}

@SpringBootApplication
public class MavenRestMvcProducerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MavenRestMvcProducerApplication.class, args);
    }
}