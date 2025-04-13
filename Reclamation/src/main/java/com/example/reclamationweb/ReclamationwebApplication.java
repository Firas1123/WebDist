package com.example.reclamationweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReclamationwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReclamationwebApplication.class, args);
    }

}
