package com.esprit.ms.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApIgatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApIgatewayApplication.class, args);
    }

    @Bean
    public RouteLocator getrouteApiGateway(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("projet",r->r.path("/api/projets/**")
                        .uri("lb://projet"))

                .route("MScandidature",
                              r -> r.path("/Candidature/**")
                                      .uri("lb://MScandidature"))
                .route("reclamationweb",
                        r -> r.path("/api/reclamations/**")
                                .uri("lb://reclamationweb"))
                .route("MsMeeting",
                        r -> r.path("/web/Meeting/**")
                                .uri("lb://MsMeeting"))
                .route("Stage",
                        r -> r.path("/internship-offers/**")
                                .uri("lb://Stage"))
                .route("MSusers",
                        r -> r.path("/api/**")
                                .uri("lb://node-login-service"))
                .build();

    }


}
