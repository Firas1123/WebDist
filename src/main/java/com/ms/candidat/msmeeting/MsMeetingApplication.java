package com.ms.candidat.msmeeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsMeetingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsMeetingApplication.class, args);
    }

}
