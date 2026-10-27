package com.pragma.onclass_bootcamp_service.infraestructure.entrypoints;

import com.pragma.onclass_bootcamp_service.infraestructure.entrypoints.handler.BootcampHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BootcampRoute {

    @Bean
    public RouterFunction<ServerResponse> bootcampRoutes(BootcampHandler handler) {
        return route()
                .POST("/api/v1/bootcamp", handler::createBootcamp)
                .GET("/api/v1/bootcamps", handler::listBootcamps)
                .build();
    }
}
